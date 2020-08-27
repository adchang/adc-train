package asia.crea.adc.train.adc_train;

import static asia.crea.adc.train.adc_train.StringUtils.isNullOrEmpty;

import java.util.Objects;

import com.google.common.base.Preconditions;

public class Route {
  private static final String DELIM = ";";
  
  private String fromStation;
  private String toStation;
  private Integer duration;
  private Integer numStops;
  
  public Route(String fromStation, String toStation, Integer duration) {
    this(fromStation, toStation, duration, 0);
  }
    
  public Route(String fromStation, String toStation, Integer duration, Integer numStops) {
    Preconditions.checkArgument(!isNullOrEmpty(fromStation));
    Preconditions.checkArgument(!isNullOrEmpty(toStation));
    Preconditions.checkNotNull(duration);
    Preconditions.checkNotNull(numStops);
    
    StringUtils.isNullOrEmpty("asdf");
    
    this.fromStation = fromStation;
    this.toStation = toStation;
    this.duration = duration;
    this.numStops = numStops;
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
