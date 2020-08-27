package asia.crea.adc.train.adc_train;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class AppTest {
  ByteArrayOutputStream out = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(out);
  
  @Test
  public void testParseArgs()  {
    App.Config config = App.parseArgs(printStream, "");
    assertEquals(App.HELP + "\n", out.toString());
    assertTrue(config == null);

    out.reset();
    config = App.parseArgs(printStream, "--file=/asf/as.csv", "--help");
    assertEquals(App.HELP + "\n", out.toString());
    assertTrue(config == null);

    out.reset();
    config = App.parseArgs(printStream, "--file=/asf/as.csv");
    assertEquals("", out.toString());
    assertTrue(config == null);
  }
}
