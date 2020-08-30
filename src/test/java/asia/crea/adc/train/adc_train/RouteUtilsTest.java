package asia.crea.adc.train.adc_train;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class RouteUtilsTest {
  @Test
  void testBuildPaths() {
    Multimap<String, Route> data = DataTestUtils.getSampleData();
    
    assertEquals("[[A;B;5;0]]", 
        RouteUtils.buildPaths(data, "A", "B").toString());

    assertEquals("[[A;B;5;0, B;C;5;0]]", 
        RouteUtils.buildPaths(data, "A", "C").toString());
    
    Set<List<Route>> result = RouteUtils.buildPaths(data, "E", "J");
    assertEquals(2, result.size());
    List<Route> path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_E_F_5);
    path.add(DataTestUtils.ROUTE_F_G_5);
    path.add(DataTestUtils.ROUTE_G_H_10);
    path.add(DataTestUtils.ROUTE_H_I_10);
    path.add(DataTestUtils.ROUTE_I_J_5);
    assertTrue(result.contains(path));
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_E_F_5);
    path.add(DataTestUtils.ROUTE_F_G_5);
    path.add(DataTestUtils.ROUTE_G_J_20);
    assertTrue(result.contains(path));

    result = RouteUtils.buildPaths(data, "A", "D");
    assertEquals(2, result.size());
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_A_B_5);
    path.add(DataTestUtils.ROUTE_B_C_5);
    path.add(DataTestUtils.ROUTE_C_D_7);
    assertTrue(result.contains(path));
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_A_D_15);
    assertTrue(result.contains(path));

    assertTrue(RouteUtils.buildPaths(data, "A", "J").isEmpty());
  }

  // TODO These tests work, but the string representation of the set is not stable
  //      between test runs, making it flaky.
  //      Convert to assert the set contains the routes instead
  @Test
  void testBuildPaths_junction() {
    Multimap<String, Route> data = DataTestUtils.getJunctionData();

    assertEquals("[[B;C;1;0, C;H;1;0, H;I;1;0]]", 
        RouteUtils.buildPaths(data, "B", "I").toString());

    //assertEquals("[[G;H;1;0, H;I;1;0, I;J;1;0, J;D;1;0], [G;J;1;0, J;D;1;0]]", 
    //    RouteUtils.buildPaths(data, "G", "D").toString());
    assertEquals(2, RouteUtils.buildPaths(data, "G", "D").size());
    
    //assertEquals("[[A;B;1;0, B;C;1;0, C;H;1;0, H;I;1;0, I;J;1;0, J;D;1;0, D;E;1;0], [A;D;1;0, D;E;1;0], [A;B;1;0, B;C;1;0, C;D;1;0, D;E;1;0]]", 
    //    RouteUtils.buildPaths(data, "A", "E").toString());
    assertEquals(3, RouteUtils.buildPaths(data, "A", "E").size());
    
    //assertEquals("[[F;G;1;0, G;H;1;0, H;I;1;0, I;J;1;0, J;D;1;0, D;E;1;0], [F;G;1;0, G;J;1;0, J;D;1;0, D;E;1;0]]", 
    //    RouteUtils.buildPaths(data, "F", "E").toString());
    assertEquals(2, RouteUtils.buildPaths(data, "F", "E").size());
    
    assertTrue(RouteUtils.buildPaths(data, "B", "G").isEmpty());
  }
  
  @Test
  void testBuildPaths_junctionBidirectional() {
    Multimap<String, Route> data = DataTestUtils.getJunctionBidirectionalData();

    /*assertEquals("[[A;D;1;0, D;C;1;0, C;D;1;0, D;J;1;0, J;I;1;0, I;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;E;1;0, E;D;1;0, D;J;1;0, J;G;1;0, G;F;1;0], " + 
            "[A;D;1;0, D;J;1;0, J;I;1;0, I;H;1;0, H;G;1;0, G;F;1;0], " + 
            "[A;B;1;0, B;C;1;0, C;D;1;0, D;J;1;0, J;I;1;0, I;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;C;1;0, C;D;1;0, D;J;1;0, J;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;E;1;0, E;D;1;0, D;J;1;0, J;I;1;0, I;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;B;1;0, B;C;1;0, C;H;1;0, H;I;1;0, I;J;1;0, J;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;C;1;0, C;H;1;0, H;I;1;0, I;J;1;0, J;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;E;1;0, E;D;1;0, D;C;1;0, C;H;1;0, H;I;1;0, I;J;1;0, J;G;1;0, G;F;1;0], " +
            "[A;B;1;0, B;C;1;0, C;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;E;1;0, E;D;1;0, D;C;1;0, C;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;B;1;0, B;C;1;0, C;D;1;0, D;J;1;0, J;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;J;1;0, J;D;1;0, D;C;1;0, C;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;C;1;0, C;H;1;0, H;G;1;0, G;F;1;0], " +
            "[A;D;1;0, D;J;1;0, J;G;1;0, G;F;1;0]]", 
        RouteUtils.buildPaths(data, "A", "F").toString());*/
    assertEquals(15, RouteUtils.buildPaths(data, "A", "F").size());
  }
  
  @Test
  void testBuildPaths_bidirectional() {
    Multimap<String, Route> data = DataTestUtils.getSampleData();
    data.put("B", DataTestUtils.ROUTE_B_A_5);
    
    Set<List<Route>> result = RouteUtils.buildPaths(data, "B", "D");
    assertEquals(2, result.size());
    List<Route> path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_B_C_5);
    path.add(DataTestUtils.ROUTE_C_D_7);
    assertTrue(result.contains(path));
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_B_A_5);
    path.add(DataTestUtils.ROUTE_A_D_15);
    assertTrue(result.contains(path));
  }

  @Test
  void testGetQuickestPath() {
    Set<List<Route>> paths = new HashSet<>();
    paths.add(DataTestUtils.getAD17Path());
    List<Route> path_AD = new ArrayList<>();
    path_AD.add(DataTestUtils.ROUTE_A_D_15);
    paths.add(path_AD);
    
    Route answer = RouteUtils.getQuickestPath(paths);
    Route expected = new Route("A", "D", 15, 0, path_AD);
    assertEquals(expected, answer);
    assertEquals(expected.getPath(), answer.getPath());
  }
  
  @Test
  void testGetDuration() {
    assertEquals(17, RouteUtils.getPathDuration(DataTestUtils.getAD17Path()));
  }
  
  @Test
  void testGetAllRoutes() {
    String FROM = "FROM %s:\n%s\n";
    String PATH = "\t\t%s\n";
    StringBuilder routes = new StringBuilder();
    Multimap<String, List<Route>> paths = 
        RouteUtils.getAllPaths(DataTestUtils.getJunctionBidirectionalData(), "E");
    paths.keySet().stream().forEach(startStation -> {
            StringBuilder destinationPaths = new StringBuilder();
            paths.get(startStation).stream().forEach(route -> {
                    destinationPaths.append(String.format(PATH,
                        Streams.<Route>join(route, o -> o.getToStation(), App.PATH_DELIM)));
                });
            routes.append(String.format(FROM, startStation, destinationPaths.toString()));
        });
    
    //assertEquals("", "\n" + routes.toString());
  }
  
  @Test
  void benchmark() {
    Random random = new Random();
    Multimap<String, Route> data = HashMultimap.create();
    int numLines = 10;
    int minStopsPerLine = 5;
    int maxStopsPerLine = 20;
    int maxDuration = 8;
    List<Integer> lineNumStops = new ArrayList<>();
    for (int line = 0; line < numLines; line++) {
      int numStops = 0;
      while (numStops < minStopsPerLine) {
        numStops = random.nextInt(maxStopsPerLine);
      }
      lineNumStops.add(numStops);
      for (int x = 0; x < numStops - 1; x++) {
        String from = "L" + line + "-" + x;
        String to = "L" + line + "-" + (x + 1);
        data.put(from, new Route(from, to, random.nextInt(maxDuration)));
        data.put(to, new Route(to, from, random.nextInt(maxDuration)));
      }
    }
    for (int line = 0; line < numLines - 1; line++) {
      int fromStation = random.nextInt(lineNumStops.get(line));
      int toStation = random.nextInt(lineNumStops.get(line + 1));
      String from = "L" + line + "-" + fromStation;
      String to = "L" + (line + 1) + "-" + toStation;
      data.put(from, new Route(from, to, random.nextInt(maxDuration)));
      data.put(to, new Route(to, from, random.nextInt(maxDuration)));
    }
    
    System.out.println("********************************************************************************");
    int numRuns = 50;
    String resultOut = "%s %d : %d milliseconds, average buildPaths: %.6f microseconds";
    for (int x = 0; x < numRuns; x++) {
      RouteResult result = RouteUtils.getAllPaths(data, Mode.FOR, true);
      System.out.println(String.format(resultOut, "FOR", x,
          result.getDuration(),
          result.getDurations().stream().mapToLong(i -> i).average().orElse(0)));
      result = RouteUtils.getAllPaths(data, Mode.STREAM, true);    
      System.out.println(String.format(resultOut, "STREAM", x,
          result.getDuration(),
          result.getDurations().stream().mapToLong(i -> i).average().orElse(0)));
      result = RouteUtils.getAllPaths(data, Mode.PARALLEL, true);
      System.out.println(String.format(resultOut, "PARALLEL", x,
          result.getDuration(),
          result.getDurations().stream().filter(Objects::nonNull)
              .mapToLong(i -> (i == null) ? 0 : i).average().orElse(0)));
    }
    System.out.println("********************************************************************************");
  }
}
