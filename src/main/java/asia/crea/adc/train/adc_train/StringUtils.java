package asia.crea.adc.train.adc_train;

public class StringUtils {
  public static final boolean isNullOrEmpty(String data) {
    // Guava's isNullOrEmpty treats white spaces as not empty
    if (data == null) return true;

    return data.trim().isEmpty();
  }
  
  private StringUtils() {
    // Prevent instantiation
  }
}
