package asia.crea.adc.train.adc_train;

import java.util.List;

import com.google.common.collect.Multimap;

public class RouteResult {
  private Multimap<String, List<Route>> paths;
  private long duration;
  private List<Long> durations;
  
  RouteResult(Multimap<String, List<Route>> paths, long duration, List<Long> durations) {
    this.paths = paths;
    this.duration = duration;
    this.durations = durations;
  }

  public Multimap<String, List<Route>> getPaths() {
    return paths;
  }

  public long getDuration() {
    return duration;
  }

  public List<Long> getDurations() {
    return durations;
  }
}
