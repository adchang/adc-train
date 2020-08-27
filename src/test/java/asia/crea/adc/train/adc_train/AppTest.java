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
  Config config = new Config("sampleData.csv", false);
  Config configBi = new Config("sampleData.csv", true);

  ByteArrayOutputStream out = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(out);
  
  @Test
  public void testParseArgs()  {
    out.reset();
    Config config = App.parseArgs(printStream, "");
    assertEquals(App.HELP + "\n", out.toString());
    assertTrue(config == null);

    out.reset();
    config = App.parseArgs(printStream, "--file=/asf/as.csv", "--help");
    assertEquals(App.HELP + "\n", out.toString());
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
        new Config("missing.csv", false));
    assertEquals(App.ERROR_FILE_INVALID, out.toString());
    assertTrue(data == null);

    out.reset();
    data = App.loadDataFromFile(printStream, config);
    assertEquals("", out.toString());
    
    /*
    Multimap<String, Route> expected = DataTestUtils.getSampleData();
    for (String key : expected.keys()) {
      assertEquals(expected.get(key).size(), data.get(key).size());
      for (Route route : expected.get(key)) {
        assertTrue(data.get(key).contains(route));
      }
    }*/
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
    
  }
  
  @Test
  public void testRunApp() {
    out.reset();
    App.runApp(new Scanner("\n\na\n\na\na\n\nB\n"), 
        printStream, DataTestUtils.getSampleData());
    assertEquals(App.PROMPT_1 + App.ERROR_ENTER_STATION_NAME + "\n" +
        App.PROMPT_1 + App.ERROR_ENTER_STATION_NAME + "\n" +
        App.PROMPT_1 + App.PROMPT_2 + App.ERROR_ENTER_STATION_NAME + "\n" +
        App.PROMPT_2 + App.ERROR_ENTER_DIFFERENT_STATION_NAME + "\n" +
        App.PROMPT_2 + App.ERROR_ENTER_DIFFERENT_STATION_NAME + "\n" +
        App.PROMPT_2 + App.ERROR_ENTER_STATION_NAME + "\n" +
        App.PROMPT_2 + String.format(App.ERROR_NO_ROUTES, "a", "B"),
        out.toString());

    out.reset();
    App.runApp(new Scanner("A\nB\n"), 
        printStream, DataTestUtils.getSampleData());
    assertEquals(App.PROMPT_1 + App.PROMPT_2 +
        String.format(App.ANSWER, "A", "B", 0, 5),
        out.toString());
  }
}
