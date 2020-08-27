package asia.crea.adc.train.adc_train;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

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
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_A_B_5);
    path.add(DataTestUtils.ROUTE_B_C_5);
    path.add(DataTestUtils.ROUTE_C_D_7);
    assertTrue(result.contains(path));
    path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_A_D_15);
    assertTrue(result.contains(path));

    assertEquals(0, 
        RouteUtils.buildPaths(data, "A", "J").size());
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
    
    assertEquals(0, RouteUtils.buildPaths(data, "B", "G").size());
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
    Route expected = new Route("A", "D", 15, 0);
    assertEquals(expected, answer);
    assertEquals(expected.getNumStops(), answer.getNumStops());
  }
  
  @Test
  void testGetDuration() {
    assertEquals(17, RouteUtils.getPathDuration(DataTestUtils.getAD17Path()));
  }
}
