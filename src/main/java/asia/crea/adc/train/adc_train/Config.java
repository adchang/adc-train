package asia.crea.adc.train.adc_train;

public class Config {
  private String dataFile;
  private boolean generateBidirectional;
  
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
