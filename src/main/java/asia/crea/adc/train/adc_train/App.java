package asia.crea.adc.train.adc_train;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Splitter;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class App {
  @VisibleForTesting
  static final String FLAG_PREFIX = "--";
  static final String PARAM_DELIM = "=";
  static final String PARAM_HELP = "help";
  static final String PARAM_FILE = "file";
  static final String PARAM_AUTO_GEN_BIDRECTIONS = "autogen-bidirections";
  static final String VALUE_TRUE = "T";
  
  static final String PROMPT_1 = "What station are you getting on the train?: ";
  static final String PROMPT_2 = "What station are you getting off the train?: ";

  static final String ANSWER = "\nYour trip from %s to %s includes %d stops and will take %d minutes.\n\n";
  
  static final String DATA_FILE_DELIM = ",";
  static final Splitter splitter = Splitter.on(DATA_FILE_DELIM);
  static final int DATA_FILE_FROM_STATION_POS = 0;
  static final int DATA_FILE_TO_STATION_POS = 1;
  static final int DATA_FILE_DURATION_POS = 2;
  
  static final String HELP = 
      "Usage: java -cp adc-train-1.0-jar-with-dependencies.jar asia.crea.adc.train.adc_train.App [args...]\n" +
      "where options include:\n" +
      "    --file                     Required. Path to data file\n" +
      "    --autogen-bidirections     T to auto-generate bidirectional data\n\n" +
      "Example: java -cp adc-train-1.0-jar-with-dependencies.jar asia.crea.adc.train.adc_train.App --file=/path/to/data.csv --autogen-bidirections=T\n\n";
  
  static final String ERROR_ENTER_STATION_NAME = "You must enter a station name\n";
  static final String ERROR_ENTER_DIFFERENT_STATION_NAME = "You must enter a different station name\n";
  static final String ERROR_FILE_INVALID = "Cannot find specified file. Please enter a valid path to your data file.\n\n";
  static final String ERROR_FILE_BAD = "File contains invalid data.\n\n";
  static final String ERROR_NO_ROUTES = "\nNo routes from %s to %s.\n\n";
  
  public static void main(String... args ) {
    Config config = parseArgs(args);
    if (config == null) return;
    
    Multimap<String, Route> data = loadData(config);
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
    boolean generateBidirectional = false;
    
    for (String arg : args) {
      String param = FLAG_PREFIX + PARAM_HELP;
      if (param.equals(arg)) {
        showHelp = true;
        break;
      }
      param = FLAG_PREFIX + PARAM_FILE + PARAM_DELIM;
      if (arg.startsWith(param)) {
        canStart = true;
        dataFile = arg.replace(param, "");
      }
      param = FLAG_PREFIX + PARAM_AUTO_GEN_BIDRECTIONS + PARAM_DELIM + VALUE_TRUE;
      if (param.equals(arg)) {
        generateBidirectional = true;
      }
    }

    // If help is specified, ignore all other arguments and show help
    if (showHelp || !canStart) {
      out.print(HELP);
      return null;
    }
    
    return new Config(dataFile, generateBidirectional);
  }

  private static final Multimap<String, Route> loadData(Config config) {
    return loadDataFromFile(System.out, config);
  }
  
  @VisibleForTesting
  static final Multimap<String, Route> loadDataFromFile(PrintStream out, Config config) {
    List<String> lines = Collections.emptyList();
    try {
      lines = Files.readAllLines(Paths.get(config.getDataFile()));
    } catch (IOException e) { 
      out.print(ERROR_FILE_INVALID);
      return null;
    }
    
    return loadData(out, config, lines);
  }
  
  @VisibleForTesting
  static final Multimap<String, Route> loadData(PrintStream out, Config config,
      List<String> fileData) {
    Multimap<String, Route> data = HashMultimap.create();
    
    // Remove duplicate lines
    List<String> noDupes = fileData.stream().distinct().collect(Collectors.toList());
    
    for (String line : noDupes) {
      List<String> values = splitter.splitToList(line);
      if (values.size() != 3) {
        out.print(ERROR_FILE_BAD);
        return null;
      }
      Integer duration;
      try {
        duration = Integer.parseInt(values.get(DATA_FILE_DURATION_POS));
      } catch (Exception e) {
        out.print(ERROR_FILE_BAD);
        return null;
      }
      String from = values.get(DATA_FILE_FROM_STATION_POS);
      String to = values.get(DATA_FILE_TO_STATION_POS);
      boolean exists = false;
      for (Route route : data.get(from)) {
        exists = route.getFromStation().equals(from) && route.getToStation().equals(to);
        if (exists) break;
      }
      if (!exists) {
        data.put(from, new Route(from, to, duration));
        if (config.generateBidirectional()) {
          data.put(to, new Route(to, from, duration));
        }
      }
    }

    return data;
  }

  private static final void runApp(Multimap<String, Route> data) {
    runApp(new Scanner(System.in), System.out, data);
  }
  
  @VisibleForTesting
  static final void runApp(Scanner scanner, PrintStream out, Multimap<String, Route> data) {
    boolean hasErrors = true;
    String startStation = "";
    String endStation = "";
    
    while (hasErrors) {
      out.print(PROMPT_1);
      startStation = scanner.nextLine().trim();
      hasErrors = StringUtils.isNullOrEmpty(startStation);
      if (hasErrors) out.print(ERROR_ENTER_STATION_NAME);
    }
    
    hasErrors = true;
    while (hasErrors) {
      out.print(PROMPT_2);
      endStation = scanner.nextLine().trim();
      hasErrors = StringUtils.isNullOrEmpty(endStation);
      if (hasErrors) {
        out.print(ERROR_ENTER_STATION_NAME);
        continue;
      }
      hasErrors = startStation.equals(endStation);
      if (hasErrors) out.print(ERROR_ENTER_DIFFERENT_STATION_NAME);
    }
    
    Set<List<Route>> paths = RouteUtils.buildPaths(data, startStation, endStation);
    if (paths.isEmpty()) {
      out.print(String.format(ERROR_NO_ROUTES, startStation, endStation));
      return;
    }
    Route answer = RouteUtils.getQuickestPath(paths);
    out.print(String.format(ANSWER, answer.getFromStation(), answer.getToStation(),
        answer.getNumStops(), answer.getDuration()));
  }
}
