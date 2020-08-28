package asia.crea.adc.train.adc_train;

public class Config {
  private String dataFile;
  private boolean generateBidirectional;
  private boolean showPaths;
  private boolean showQuickest;
  
  public Config(String dataFile, boolean generateBidirectional, boolean showPaths,
      boolean showQuickest) {
    this.dataFile = dataFile;
    this.generateBidirectional = generateBidirectional;
    this.showPaths = showPaths;
    this.showQuickest = showQuickest;
  }

  public String getDataFile() {
    return dataFile;
  }

  public boolean generateBidirectional() {
    return generateBidirectional;
  }
  
  public boolean showPaths() {
    return showPaths;
  }
  
  public boolean showQuickest() {
    return showQuickest;
  }
}
