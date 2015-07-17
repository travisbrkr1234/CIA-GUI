import java.net.MalformedURLException;
import org.apache.xmlrpc.XmlRpcException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import org.apache.commons.csv.*;

public class ISClientDriver{
  //Delimiter used in CSV file
  private static final String NEW_LINE_SEPARATOR = "\n";
  //CSV file header
  private static final Object [] FILE_HEADER = {"id","firstName","lastName","gender","age"};
  private static final String Filename = "C:\\users\\carlos.ochoa\\Desktop\\Apple.csv";

  public static void main(String[] args) {
    Scanner input1 = new Scanner(System.in);
    System.out.println("Enter appname: ");
    String appName = input1.nextLine();

    Scanner input2 = new Scanner(System.in);
    System.out.println("Enter apiKey: ");
    String apiKey = input2.nextLine();

    try {
      ISClient thisclient = new ISClient(appName, apiKey);
      thisclient.printFirstContacts();
      System.out.println("\n\n\n\n");
      ArrayList allcontacts = thisclient.getContactsAndFK();
      for (int i = 0; i < allcontacts.size(); i++) {
          //Each item in the array is a struct
          Map contact = (Map) allcontacts.get(i);
          System.out.println("Email, " + contact.get("Email") + "," + "id, " +
                  contact.get("Id"));
/*      FileWriter fileWriter = null;
      CSVPrinter csvFilePrinter = null;

      CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
      try{
        fileWriter = new FileWriter(Filename);
        csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
        csvFilePrinter.printRecord(FILE_HEADER);
        for (Map contact : allcontacts){
          csvFilePrinter(contact);
        }
      }
      catch (Exception e){
        System.out.println("Crud, there was an error" + e.getMessage());
        e.printStackTrace();
      }
*/    }

    } catch (MalformedURLException e){
      System.err.println("MalformedURLException Ya broke it" + e.getMessage());
    } catch (XmlRpcException e) {
      System.err.println("XmlRpcException Ya broke it" + e.getMessage());
    }
  }
}
