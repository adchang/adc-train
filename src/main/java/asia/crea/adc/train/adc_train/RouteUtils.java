package asia.crea.adc.train.adc_train;

import static asia.crea.adc.train.adc_train.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class RouteUtils {
  private static final Mode DEFAULT_MODE = Mode.FOR;
  
  public static final Set<List<Route>> buildPaths(Multimap<String, Route> data,
      String startStation, String endStation) {
    return buildPaths(data, startStation, endStation, DEFAULT_MODE);
  }
  
  /***
   * Given the starting and ending station names, calculates all possible paths
   * between the two stations.
   * 
   * @param data Multimap of all available routes between adjoining stations
   * @param startStation Name of the starting station
   * @param endStation Name of the ending station
   * @param mode Mode to execute iterations
   * @return All possible paths between the two stations
   */
  public static final Set<List<Route>> buildPaths(Multimap<String, Route> data,
      String startStation, String endStation, Mode mode) {
    Preconditions.checkNotNull(data);
    Preconditions.checkArgument(!isNullOrEmpty(startStation));
    Preconditions.checkArgument(!isNullOrEmpty(endStation));

    Set<List<Route>> paths = new HashSet<>();
    Set<String> visited = new HashSet<>();
    visited.add(startStation);
    
    switch (mode) {
      case STREAM:
        data.get(startStation).stream().forEach(route -> 
            paths.addAll(traverse(data, visited, route, endStation, mode)));
        break;
        
      case PARALLEL:
        data.get(startStation).parallelStream().forEach(route -> 
            paths.addAll(traverse(data, visited, route, endStation, mode)));
        break;
        
      default:
        for (Route route : data.get(startStation)) {
          paths.addAll(traverse(data, visited, route, endStation, mode));
        }
    }

    return paths;
  }

  /***
   * Recursively traverse the path by looking up the next stations in the current
   * route. When the toStation name of the route matches the endStation name, 
   * returns a match.
   * 
   * Along the path, when there are multiple paths to the destination station,
   * will return all possible permutations.
   * 
   * @param data Multimap of all available routes between adjoining stations
   * @param visited Set of station names that have been already visited
   * @param currentRoute The current route in the path
   * @param endStation Name of the ending station
   * @param mode Mode to execute iterations
   * @return All possible paths between the two stations. An empty List when there
   *         are no valid paths to the destination station.
   */
  private static final List<List<Route>> traverse(Multimap<String, Route> data, 
      Set<String> priorVisited, Route currentRoute, String endStation, Mode mode) {
    List<List<Route>> paths = new ArrayList<>();
    String currentStation = currentRoute.getToStation();

    if (currentStation.equals(endStation)) {
      List<Route> path = new ArrayList<>();
      path.add(currentRoute);
      paths.add(path);
      
      return paths;
    }
    
    switch (mode) {
      case STREAM:
        data.get(currentStation).stream().forEach(route -> {
                String toStation = route.getToStation();
                if (!priorVisited.contains(toStation)) {
                  Set<String> visited = new HashSet<>();
                  visited.add(toStation);
                  visited.addAll(priorVisited);
                  traverse(data, visited, route, endStation, mode).stream().forEach(segment -> {
                          segment.add(0, currentRoute);
                          paths.add(segment);
                      });
                }
            });
        break;
    
      case PARALLEL:
        data.get(currentStation).parallelStream().forEach(route -> {
                String toStation = route.getToStation();
                if (!priorVisited.contains(toStation)) {
                  Set<String> visited = new HashSet<>();
                  visited.add(toStation);
                  visited.addAll(priorVisited);
                  traverse(data, visited, route, endStation, mode).parallelStream().forEach(segment -> {
                          segment.add(0, currentRoute);
                          paths.add(segment);
                      });
                }
            });
        break;
    
      default:
        for (Route route : data.get(currentStation)) {
          String toStation = route.getToStation();
          if (!priorVisited.contains(toStation)) {
            Set<String> visited = new HashSet<>();
            visited.add(toStation);
            visited.addAll(priorVisited);
            List<List<Route>> segments = traverse(data, visited, route, endStation, mode);
            for (List<Route> segment : segments) {
              segment.add(0, currentRoute);
              paths.add(segment);
            }
          }
        }
    }

    return paths;
  }

  public static final Route getQuickestPath(Set<List<Route>> paths) {
    return getQuickestPath(paths, DEFAULT_MODE);
  }
    
  public static final Route getQuickestPath(Set<List<Route>> paths, Mode mode) {
    Preconditions.checkNotNull(paths);
    Preconditions.checkArgument(!paths.isEmpty());

    List<Route> quickest;
    Integer quickestDuration = Integer.MAX_VALUE;
    
    switch (mode) {
      case STREAM:
        quickest = paths.stream()
            .min(Comparator.comparing(path -> getPathDuration(path)))
            .orElseThrow(NoSuchElementException::new);
        quickestDuration = getPathDuration(quickest);
        break;
    
      case PARALLEL:
        quickest = paths.parallelStream()
            .min(Comparator.comparing(path -> getPathDuration(path)))
            .orElseThrow(NoSuchElementException::new);
        quickestDuration = getPathDuration(quickest);
        break;
    
      default:
        quickest = null;
        for (List<Route> path : paths) {
          Integer duration = getPathDuration(path);
          if (duration < quickestDuration) {
            quickest = path;
            quickestDuration = duration;
          }
        }
    }
    
    Integer pos = quickest.size() - 1;
    return new Route(quickest.get(0).getFromStation(), 
        quickest.get(pos).getToStation(), quickestDuration, pos, quickest);
  }
  
  @VisibleForTesting
  static final Integer getPathDuration(List<Route> path) {
    return Streams.sum(path, Route.getDuration);
  }
  
  public static final Multimap<String, List<Route>> getAllPaths(Multimap<String, Route> data,
      Mode mode) {
    return getAllPaths(data, mode, false).getPaths();
  }
  
  @SuppressWarnings("unused")
  public static final RouteResult getAllPaths(Multimap<String, Route> data, Mode mode, 
      boolean timed) {
    List<Long> timings = new ArrayList<>();
    Multimap<String, List<Route>> paths = HashMultimap.create();
    Set<String> stations = new HashSet<>();
    data.values().stream().forEach(path -> {
            stations.add(path.getFromStation());
            stations.add(path.getToStation());
        });
    List<String> allStations = Streams.getSortedList(stations);

    Stopwatch run = Stopwatch.createStarted();
    switch (mode) {
      case STREAM:
        allStations.stream().forEach(startStation -> {
                Set<String> destinations = new HashSet<>();
                destinations.addAll(allStations);
                destinations.remove(startStation);
                destinations.stream().forEach(endStation -> {
                        Stopwatch runPath = null;
                        if (timed) runPath = Stopwatch.createStarted();
                        buildPaths(data, startStation, endStation, mode).stream().forEach(path -> {
                                paths.put(path.get(0).getFromStation(), path);
                            });
                        if (timed) timings.add(runPath.elapsed(TimeUnit.MICROSECONDS));
                    });
            });
        break;
    
      case PARALLEL:
        allStations.parallelStream().forEach(startStation -> {
                Set<String> destinations = new HashSet<>();
                destinations.addAll(allStations);
                destinations.remove(startStation);
                destinations.parallelStream().forEach(endStation -> {
                        Stopwatch runPath = null;
                        if (timed) runPath = Stopwatch.createStarted();
                        buildPaths(data, startStation, endStation, mode).parallelStream().forEach(path -> {
                                paths.put(path.get(0).getFromStation(), path);
                            });
                        if (timed) timings.add(runPath.elapsed(TimeUnit.MICROSECONDS));
                    });
            });
        break;
    
      default:
        for (String startStation : allStations) {
          Set<String> destinations = new HashSet<>();
          destinations.addAll(allStations);
          destinations.remove(startStation);
          for (String endStation : destinations) {
            Stopwatch runPath = null;
            if (timed) runPath = Stopwatch.createStarted();
            for (List<Route> path : buildPaths(data, startStation, endStation, mode)) {
              paths.put(path.get(0).getFromStation(), path);
            }
            if (timed) timings.add(runPath.elapsed(TimeUnit.MICROSECONDS));
          }
        }
    }
    run.stop();

    return new RouteResult(paths, run.elapsed(TimeUnit.MILLISECONDS), timings);
  }
  
  public static final Multimap<String, List<Route>> getAllPaths(Multimap<String, Route> data,
      String endStation) {
    Multimap<String, List<Route>> paths = HashMultimap.create();
    
    getAllPaths(data, Mode.PARALLEL).values().stream()
        .filter(path -> path.get(path.size() - 1).getToStation().equals(endStation))
        .forEach(path -> paths.put(path.get(0).getFromStation(), path));
    
    return paths;
  }
  
  private RouteUtils() {
    // Prevent instantiation
  }
}
