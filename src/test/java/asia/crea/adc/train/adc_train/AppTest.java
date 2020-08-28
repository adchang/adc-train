package asia.crea.adc.train.adc_train;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Multimap;

public class AppTest {
  Config config = new Config("sampleData.csv", false, false, false);
  Config configBi = new Config("sampleData.csv", true, false, false);

  ByteArrayOutputStream out = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(out);
  
  @Test
  public void testParseArgs()  {
    out.reset();
    Config config = App.parseArgs(printStream, "");
    assertEquals(App.HELP, out.toString());
    assertTrue(config == null);

    out.reset();
    config = App.parseArgs(printStream, "--file=/asf/as.csv", "--help");
    assertEquals(App.HELP, out.toString());
    assertTrue(config == null);

    out.reset();
    config = App.parseArgs(printStream, "--autogen-bidirections=t", "--file=/asf/as.csv");
    assertEquals("", out.toString());
    assertEquals("/asf/as.csv", config.getDataFile());
    assertFalse(config.generateBidirectional());

    out.reset();
    config = App.parseArgs(printStream, "--autogen-bidirections=T", "--file=/asf/as.csv");
    assertEquals("", out.toString());
    assertEquals("/asf/as.csv", config.getDataFile());
    assertTrue(config.generateBidirectional());
  }
  
  @Test
  public void testLoadDataFromFile() {
    out.reset();
    Multimap<String, Route> data = App.loadDataFromFile(printStream,
        new Config("missing.csv", false, false, false));
    assertEquals(App.ERROR_FILE_INVALID, out.toString());
    assertTrue(data == null);

    out.reset();
    data = App.loadDataFromFile(printStream, config);
    assertEquals("", out.toString());
    
    Multimap<String, Route> expected = DataTestUtils.getSampleData();
    for (String key : expected.keys()) {
      assertEquals(expected.get(key).size(), data.get(key).size());
      for (Route route : expected.get(key)) {
        assertTrue("\nDATA: " + data.get(key) + "\nROUTE: " + route.toString(),
            data.get(key).contains(route));
      }
    }
  }
  
  @Test
  public void voidTestLoadData() {
    out.reset();
    List<String> fileData = new ArrayList<>();
    fileData.add("AB5");
    Multimap<String, Route> data = App.loadData(printStream, config, fileData);
    assertEquals(App.ERROR_FILE_BAD, out.toString());
    
    out.reset();
    fileData = new ArrayList<>();
    fileData.add("A,B,D");
    data = App.loadData(printStream, config, fileData);
    assertEquals(App.ERROR_FILE_BAD, out.toString());
    
    out.reset();
    fileData = new ArrayList<>();
    fileData.add("A,B,5");
    fileData.add("A,B,5");
    data = App.loadData(printStream, config, fileData);
    assertEquals("", out.toString());
    assertEquals(1, data.size());
    assertTrue(data.get("A").contains(DataTestUtils.ROUTE_A_B_5));
    
    out.reset();
    fileData = new ArrayList<>();
    fileData.add("A,B,5");
    fileData.add("A,B,10");
    data = App.loadData(printStream, configBi, fileData);
    assertEquals("", out.toString());
    assertEquals(2, data.size());
    assertTrue(data.get("A").contains(DataTestUtils.ROUTE_A_B_5));
    assertTrue(data.get("B").contains(DataTestUtils.ROUTE_B_A_5));
  }
  
  @Test
  public void testRunApp() {
    out.reset();
    App.runApp(new Scanner("\n\na\n\na\na\n\nB\n"), 
        printStream, DataTestUtils.getSampleData(), config);
    assertEquals(App.PROMPT_1 + App.ERROR_ENTER_STATION_NAME +
        App.PROMPT_1 + App.ERROR_ENTER_STATION_NAME +
        App.PROMPT_1 + 
        App.PROMPT_2 + App.ERROR_ENTER_STATION_NAME +
        App.PROMPT_2 + App.ERROR_ENTER_DIFFERENT_STATION_NAME +
        App.PROMPT_2 + App.ERROR_ENTER_DIFFERENT_STATION_NAME +
        App.PROMPT_2 + App.ERROR_ENTER_STATION_NAME +
        App.PROMPT_2 + String.format(App.ERROR_NO_ROUTES, "a", "B"),
        out.toString());

    out.reset();
    App.runApp(new Scanner("A\nB\n"), 
        printStream, DataTestUtils.getSampleData(), config);
    assertEquals(App.PROMPT_1 + App.PROMPT_2 +
        String.format(App.ANSWER, "A", "B", 0, 5),
        out.toString());

    out.reset();
    App.runApp(new Scanner("A\nD\n"), 
        printStream, DataTestUtils.getSampleData(), 
        new Config("sampleData.csv", false, true, false));
    assertEquals(App.PROMPT_1 + App.PROMPT_2 +
        String.format(App.ANSWER, "A", "D", 0, 15) +
        String.format(App.SHOW_PATHS, "A -> B -> C -> D (17)\n" + 
            "A -> D (15)\n"),
        out.toString());

    out.reset();
    App.runApp(new Scanner("A\nD\n"), 
        printStream, DataTestUtils.getSampleData(), 
        new Config("sampleData.csv", false, false, true));
    assertEquals(App.PROMPT_1 + App.PROMPT_2 +
        String.format(App.ANSWER, "A", "D", 0, 15) +
        String.format(App.SHOW_QUICKEST, "A -> D"),
        out.toString());
    
    out.reset();
    App.runApp(new Scanner("A\nD\n"), 
        printStream, DataTestUtils.getSampleData(), 
        new Config("sampleData.csv", false, true, true));
    assertEquals(App.PROMPT_1 + App.PROMPT_2 +
        String.format(App.ANSWER, "A", "D", 0, 15) +
        String.format(App.SHOW_QUICKEST, "A -> D") +
        String.format(App.SHOW_PATHS, "A -> B -> C -> D (17)\n" + 
            "A -> D (15)\n"),
        out.toString());
  }
}
/*


*/