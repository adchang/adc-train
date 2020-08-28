package asia.crea.adc.train.adc_train;

import static asia.crea.adc.train.adc_train.StringUtils.isNullOrEmpty;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

public class Route {
  private static final String DELIM = ";";
  
  private String fromStation;
  private String toStation;
  private Integer duration;
  private Integer numStops;
  private List<Route> path;
  
  public Route(String fromStation, String toStation, Integer duration) {
    this(fromStation, toStation, duration, 0, null);
  }
    
  public Route(String fromStation, String toStation, Integer duration, Integer numStops,
      @Nullable List<Route> path) {
    Preconditions.checkArgument(!isNullOrEmpty(fromStation));
    Preconditions.checkArgument(!isNullOrEmpty(toStation));
    Preconditions.checkNotNull(duration);
    Preconditions.checkNotNull(numStops);

    this.fromStation = fromStation;
    this.toStation = toStation;
    this.duration = duration;
    this.numStops = numStops;
    this.path = path;
  }
  
  public String getFromStation() {
    return fromStation;
  }
  
  public String getToStation() {
    return toStation;
  }
  
  public Integer getDuration() {
    return duration;
  }
  
  public Integer getNumStops() {
    return numStops;
  }
  
  public List<Route> getPath() {
    return path;
  }
  
  @Override
  public String toString() {
    return fromStation + DELIM + toStation + DELIM + duration + DELIM + numStops;
  }
  
  @Override
  public boolean equals(Object obj) {
    return this.toString().equals(obj.toString());
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(fromStation, toStation, duration, numStops);
  }
}
