package asia.crea.adc.train.adc_train;

import java.io.PrintStream;
import java.util.Scanner;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Multimap;

public class App {
  @VisibleForTesting
  static final String FLAG_PREFIX = "--";
  static final String PARAM_DELIM = "=";
  static final String PARAM_HELP = "help";
  static final String PARAM_FILE = "file";
  static final String PARAM_AUTO_GEN_BIDRECTIONS = "autogen-bidirections";
  static final String HELP = 
      "Usage: java -cp adc-train-1.0.jar asia.crea.adc.train.adc_train.App [args...]\n" +
      "where options include:\n" +
      "    --file                     Required. Path to data file\n" +
      "    --autogen-bidirections     T to auto-generate bidirectional data\n\n" +
      "Example: java -cp adc-train-1.0.jar asia.crea.adc.train.adc_train.App --file=/path/to/data.csv --autogen-bidirections=T\n\n";
  
  static final String ERROR_NO_FILE_SPECIFIED = "No file specified. Please use --help for help";
  
  public static void main(String... args ) {
    Config config = parseArgs(args);
    if (config == null) return;
    
    Multimap<String, Route> data = readFile(config);
    if (data == null) return;
    
    runApp(data);
  }
  
  private static final Config parseArgs(String...args) {
    return parseArgs(System.out, args);
  }
  
  @VisibleForTesting  
  static final Config parseArgs(PrintStream out, String...args) {
    boolean showHelp = false;
    boolean canStart = false;
    String dataFile = "";
    
    for (String arg : args) {
      String param = FLAG_PREFIX + PARAM_HELP;
      if (param.equals(arg)) {
        showHelp = true;
        break;
      }
      param = FLAG_PREFIX + PARAM_FILE + PARAM_DELIM;
      if (arg.startsWith(param)) {
        canStart = true;
        dataFile = param.replace(param, "");
      }
      
    }

    // If help is specified, ignore all other arguments and show help
    if (showHelp || !canStart) {
      out.println(HELP);
      return null;
    }
    
    return null;
  }

  private static final Multimap<String, Route> readFile(Config config) {
    return readFile(System.out, config);
  }
  
  @VisibleForTesting
  static final Multimap<String, Route> readFile(PrintStream out, Config config) {
    
    return null;
  }

  private static final void runApp(Multimap<String, Route> data) {
    runApp(new Scanner(System.in), System.out, data);
  }
  
  @VisibleForTesting
  static final void runApp(Scanner scanner, PrintStream out, Multimap<String, Route> data) {
    
  }
  
  @VisibleForTesting
  class Config {
    private String dataFile;
    private boolean generateBidirectional;
    
    public Config(String dataFile) {
      this(dataFile, false);
    }
    
    public Config(String dataFile, boolean generateBidirectional) {
      this.dataFile = dataFile;
      this.generateBidirectional = generateBidirectional;
    }

    public String getDataFile() {
      return dataFile;
    }

    public boolean generateBidirectional() {
      return generateBidirectional;
    }
  }
}
