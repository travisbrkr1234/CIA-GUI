/*
* @Author: Jeremiah Marks
* @Date:   2015-07-12 15:58:13
* @Last Modified 2015-07-12
* @Last Modified time: 2015-07-12 18:43:41
*/
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class ISClient {
    private String appname;
    private String apikey;
    private URL serverAddress;
    private XmlRpcClient connection;
    private XmlRpcClientConfigImpl config;
    private List contact;
    private List dataToPass;

    public ISClient(String appname, String apikey) throws MalformedURLException, XmlRpcException {
        this.appname = appname;
        this.apikey = apikey;
        this.serverAddress = new URL("https://" + this.appname + ".infusionsoft.com:443/api/xmlrpc");
        this.config = new XmlRpcClientConfigImpl();
        this.config.setServerURL(this.serverAddress);
        this.connection = new XmlRpcClient();
        this.connection.setConfig(config);
        this.contact = new ArrayList();
        this.contact.add("AccountId");
        this.contact.add("Address1Type");
        this.contact.add("Address2Street1");
        this.contact.add("Address2Street2");
        this.contact.add("Address2Type");
        this.contact.add("Address3Street1");
        this.contact.add("Address3Street2");
        this.contact.add("Address3Type");
        this.contact.add("Anniversary");
        this.contact.add("AssistantName");
        this.contact.add("AssistantPhone");
        this.contact.add("BillingInformation");
        this.contact.add("Birthday");
        this.contact.add("City");
        this.contact.add("City2");
        this.contact.add("City3");
        this.contact.add("Company");
        this.contact.add("CompanyID");
        this.contact.add("ContactNotes");
        this.contact.add("ContactType");
        this.contact.add("Country");
        this.contact.add("Country2");
        this.contact.add("Country3");
        this.contact.add("CreatedBy");
        this.contact.add("DateCreated");
        this.contact.add("Email");
        this.contact.add("EmailAddress2");
        this.contact.add("EmailAddress3");
        this.contact.add("Fax1");
        this.contact.add("Fax1Type");
        this.contact.add("Fax2");
        this.contact.add("Fax2Type");
        this.contact.add("FirstName");
        this.contact.add("_FK");
        this.contact.add("Groups");
        this.contact.add("Id");
        this.contact.add("JobTitle");
        this.contact.add("LastName");
        this.contact.add("LastUpdated");
        this.contact.add("LastUpdatedBy");
        this.contact.add("LeadSourceId");
        this.contact.add("Leadsource");
        this.contact.add("MiddleName");
        this.contact.add("Nickname");
        this.contact.add("OwnerID");
        this.contact.add("Password");
        this.contact.add("Phone1");
        this.contact.add("Phone1Ext");
        this.contact.add("Phone1Type");
        this.contact.add("Phone2");
        this.contact.add("Phone2Ext");
        this.contact.add("Phone2Type");
        this.contact.add("Phone3");
        this.contact.add("Phone3Ext");
        this.contact.add("Phone3Type");
        this.contact.add("Phone4");
        this.contact.add("Phone4Ext");
        this.contact.add("Phone4Type");
        this.contact.add("Phone5");
        this.contact.add("Phone5Ext");
        this.contact.add("Phone5Type");
        this.contact.add("PostalCode");
        this.contact.add("PostalCode2");
        this.contact.add("PostalCode3");
        this.contact.add("ReferralCode");
        this.contact.add("SpouseName");
        this.contact.add("State");
        this.contact.add("State2");
        this.contact.add("State3");
        this.contact.add("StreetAddress1");
        this.contact.add("StreetAddress2");
        this.contact.add("Suffix");
        this.contact.add("Title");
        this.contact.add("Username");
        this.contact.add("Validated");
        this.contact.add("Website");
        this.contact.add("ZipFour1");
        this.contact.add("ZipFour2");
        this.contact.add("ZipFour3");
    }

    public void printFirstContacts() {

        Map searchData = new HashMap();
        searchData.put("FirstName", "%");
        this.dataToPass = new ArrayList();
        this.dataToPass.add(this.apikey); //Secure key
        this.dataToPass.add("Contact");  //What table we are looking in
        this.dataToPass.add(new Integer(1000)); //How many records to return
        this.dataToPass.add(new Integer(0)); //Which page of results to display
        this.dataToPass.add(searchData);
        this.dataToPass.add(this.contact); //what fields to select on return
        this.dataToPass.add("Id"); //The field we are querying on
        this.dataToPass.add(true); //THe data to query on

        //Make call - the result is an array of structs

        try {
          Object[] contacts = (Object[]) this.connection.execute("DataService.query", this.dataToPass);
          //Loop through results
          for (int i = 0; i < contacts.length; i++) {
              //Each item in the array is a struct
              Map contact = (Map) contacts[i];
              System.out.println("Contact with email address " + contact.get("Email") + " was found and has ID " +
                      contact.get("Id"));
          }
        } catch (XmlRpcException e) {
            System.err.println("XmlRpcException Ya broke it" + e.getMessage());
        }
    }
    public ArrayList<Map> getAllContacts(){
      Map searchData = new HashMap();
      Object[] results = new Object[0];
      searchData.put("Id", "%");
      Integer page=0;
      ArrayList<Map> totalContacts = new ArrayList<Map>();
      try {
        do {
          this.dataToPass = new ArrayList();
          this.dataToPass.add(this.apikey); //Secure key
          this.dataToPass.add("Contact");  //What table we are looking in
          this.dataToPass.add(new Integer(1000)); //How many records to return
          this.dataToPass.add(page); //Which page of results to display
          this.dataToPass.add(searchData);
          this.dataToPass.add(this.contact); //what fields to select on return
          this.dataToPass.add("Id"); //The field we are querying on
          this.dataToPass.add(true); //THe data to

          results =  (Object[]) this.connection.execute("DataService.query", this.dataToPass);
          //Loop through results
          for (int i = 0; i < results.length; i++) {
              //Each item in the array is a struct
              Map contact = (Map) results[i];
              totalContacts.add(contact);
          }
          page++;
        } while (results.length==1000);
      } catch (XmlRpcException e) {
          System.err.println("XmlRpcException Ya broke it" + e.getMessage());
      }
      return totalContacts;
    }
    public ArrayList<Map> getContactsAndFK(){
      Map searchData = new HashMap();
      Object[] results = new Object[0];
      searchData.put("_FK", "%");
      Integer page=0;
      ArrayList<Map> totalContacts = new ArrayList<Map>();
      try {
        do {
          this.dataToPass = new ArrayList();
          this.dataToPass.add(this.apikey); //Secure key
          this.dataToPass.add("Contact");  //What table we are looking in
          this.dataToPass.add(new Integer(1000)); //How many records to return
          this.dataToPass.add(page); //Which page of results to display
          this.dataToPass.add(searchData);
          this.dataToPass.add(this.contact); //what fields to select on return
          this.dataToPass.add("Id"); //The field we are querying on
          this.dataToPass.add(true); //THe data to

          results =  (Object[]) this.connection.execute("DataService.query", this.dataToPass);
          //Loop through results
          for (int i = 0; i < results.length; i++) {
              //Each item in the array is a struct
              Map contact = (Map) results[i];
              totalContacts.add(contact);
          }
          page++;
        } while (results.length==1000);
      } catch (XmlRpcException e) {
          System.err.println("XmlRpcException Ya broke it" + e.getMessage());
      }
      return totalContacts;
    }
}
