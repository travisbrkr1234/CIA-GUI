
public abstract class basicInterface{
  String appname = new String();
  public abstract String getAppName();
  public abstract String getApiKey();
  // public abstract void displayTableData();
  public void generalRun(){
    this.appname = getAppName();

  }
}
