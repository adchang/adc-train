package asia.crea.adc.train.adc_train;

import static asia.crea.adc.train.adc_train.StringUtils.isNullOrEmpty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multimap;

public class RouteUtils {
  /***
   * Given the starting and ending station names, calculates all possible paths
   * between the two stations.
   * 
   * @param data Multimap of all available routes between adjoining stations
   * @param startStation Name of the starting station
   * @param endStation Name of the ending station
   * @return All possible paths between the two stations
   */
  public static final Set<List<Route>> buildPaths(Multimap<String, Route> data,
      String startStation, String endStation) {
    Preconditions.checkNotNull(data);
    Preconditions.checkArgument(!isNullOrEmpty(startStation));
    Preconditions.checkArgument(!isNullOrEmpty(endStation));

    Set<List<Route>> paths = new HashSet<>();
    Set<String> visited = new HashSet<>();
    visited.add(startStation);
    for (Route route : data.get(startStation)) {
      paths.addAll(traverse(data, visited, route, endStation));
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
   * @return All possible paths between the two stations. An empty List when there
   *         are no valid paths to the destination station.
   */
  private static final List<List<Route>> traverse(Multimap<String, Route> data, 
      Set<String> priorVisited, Route currentRoute, String endStation) {
    List<List<Route>> paths = new ArrayList<>();
    String currentStation = currentRoute.getToStation();

    if (currentStation.equals(endStation)) {
      List<Route> path = new ArrayList<>();
      path.add(currentRoute);
      paths.add(path);
      
      return paths;
    }
    
    for (Route route : data.get(currentStation)) {
      String toStation = route.getToStation();
      if (!priorVisited.contains(toStation)) {
        Set<String> visited = new HashSet<>();
        visited.add(toStation);
        visited.addAll(priorVisited);
        List<List<Route>> segments = traverse(data, visited, route, endStation);
        for (List<Route> segment : segments) {
          segment.add(0, currentRoute);
          paths.add(segment);
        }
      }
    }
    
    return paths;
  }
  
  public static final Route getQuickestPath(Set<List<Route>> paths) {
    Preconditions.checkNotNull(paths);
    Preconditions.checkArgument(!paths.isEmpty());
    
    List<Route> quickest = null;
    Integer quickestDuration = Integer.MAX_VALUE;
    for (List<Route> path : paths) {
      Integer duration = getPathDuration(path);
      if (duration < quickestDuration) {
        quickest = path;
        quickestDuration = duration;
      }
    }
    
    Integer pos = quickest.size() - 1;
    return new Route(quickest.get(0).getFromStation(), 
        quickest.get(pos).getToStation(), quickestDuration, pos, quickest);
  }
  
  @VisibleForTesting
  static final Integer getPathDuration(List<Route> path) {
    return path.stream()
        .collect(Collectors.summingInt(Route::getDuration));
  }
  
  private RouteUtils() {
    // Prevent instantiation
  }
}
