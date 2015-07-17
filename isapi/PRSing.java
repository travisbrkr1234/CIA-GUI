import java.io.*;
import org.apache.commons.csv.*;

public class PRSing {
  public static void main(String[] args) throws IOException {
    Reader in = new InputStreamReader(PRSing.class.getClassLoader().getResourceAsStream("./student.csv"), "UTF-8");
    Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
    for (CSVRecord record : records) {
        String lastName = record.get("lastName");
        String firstName = record.get("firstName");
        System.out.println(firstName + " " + lastName);
    }
  }
}
