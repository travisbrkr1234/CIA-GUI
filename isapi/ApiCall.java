import java.util.Map;

public interface ApiCall {

  public String getAllRecords(String TableName); //contacts,tags, extends all tables
  public Integer createContact(Map ContactDetails); //creates contact ONLY
  public Integer createRecord(String TableName, Map RecordDetails); //creates !Contacts
  public Boolean deleteRecord(String TableName, Integer RecordId); //deletes records from table
  public void call();
}
