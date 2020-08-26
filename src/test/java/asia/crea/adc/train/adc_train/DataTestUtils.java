package asia.crea.adc.train.adc_train;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class DataTestUtils {
  public static final Route ROUTE_A_B_5 = new Route("A", "B", 5);
  public static final Route ROUTE_B_C_5 = new Route("B", "C", 5);
  public static final Route ROUTE_C_D_7 = new Route("C", "D", 7);
  public static final Route ROUTE_A_D_15 = new Route("A", "D", 15);

  public static final Route ROUTE_E_F_5 = new Route("E", "F", 5);
  public static final Route ROUTE_F_G_5 = new Route("F", "G", 5);
  public static final Route ROUTE_G_H_10 = new Route("G", "H", 10);
  public static final Route ROUTE_H_I_10 = new Route("H", "I", 10);
  public static final Route ROUTE_I_J_5 = new Route("I", "J", 5);
  public static final Route ROUTE_G_J_20 = new Route("G", "J", 20);
  
  public static final Route ROUTE_B_A_5 = new Route("B", "A", 5);

  public static final Multimap<String, Route> getSampleData() {
    Multimap<String, Route> data = HashMultimap.create();
    data.put("A", ROUTE_A_B_5);
    data.put("B", ROUTE_B_C_5);
    data.put("C", ROUTE_C_D_7);
    data.put("A", ROUTE_A_D_15);
    
    data.put("E", ROUTE_E_F_5);
    data.put("F", ROUTE_F_G_5);
    data.put("G", ROUTE_G_H_10);
    data.put("H", ROUTE_H_I_10);
    data.put("I", ROUTE_I_J_5);
    data.put("G", ROUTE_G_J_20);

    return data;
  }
  
  public static final Multimap<String, Route> getJunctionData() {
    Multimap<String, Route> data = HashMultimap.create();
    data.put("A", new Route("A", "B", 1));
    data.put("B", new Route("B", "C", 1));
    data.put("C", new Route("C", "D", 1));
    data.put("D", new Route("D", "E", 1));
    data.put("F", new Route("F", "G", 1));
    data.put("G", new Route("G", "H", 1));
    data.put("H", new Route("H", "I", 1));
    data.put("I", new Route("I", "J", 1));
    data.put("J", new Route("J", "K", 1));
    data.put("A", new Route("A", "D", 1));
    data.put("G", new Route("G", "J", 1));
    data.put("C", new Route("C", "H", 1));
    data.put("J", new Route("J", "D", 1));
    
    return data;
  }
  
  public static final Multimap<String, Route> getJunctionBidirectionalData() {
    Multimap<String, Route> data = getJunctionData();
    data.put("B", new Route("B", "A", 1));
    data.put("C", new Route("C", "B", 1));
    data.put("D", new Route("D", "C", 1));
    data.put("E", new Route("E", "D", 1));
    data.put("G", new Route("G", "F", 1));
    data.put("H", new Route("H", "G", 1));
    data.put("I", new Route("I", "H", 1));
    data.put("J", new Route("J", "I", 1));
    data.put("K", new Route("K", "J", 1));
    data.put("D", new Route("D", "A", 1));
    data.put("J", new Route("J", "G", 1));
    data.put("H", new Route("H", "C", 1));
    data.put("D", new Route("D", "J", 1));
    
    return data;
  }
  
  public static final List<Route> getAD17Path() {
    List<Route> path = new ArrayList<>();
    path.add(DataTestUtils.ROUTE_A_B_5);
    path.add(DataTestUtils.ROUTE_B_C_5);
    path.add(DataTestUtils.ROUTE_C_D_7);

    return path;
  }
  
  private DataTestUtils() {
    // Prevent instantiation
  }
}
