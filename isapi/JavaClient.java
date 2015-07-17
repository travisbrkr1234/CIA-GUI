/* 
* @Author: Jeremiah Marks
* @Date:   2015-07-12 15:58:13
* @Last Modified 2015-07-12
* @Last Modified time: 2015-07-12 17:04:58
*/
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.*;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Vector;


public class JavaClient {
    private final static String server_url =
        "https://marty.infusionsoft.com:443/api/xmlrpc";
    public static void main (String [] args) {
      System.out.println("Hello World!");
      try {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(server_url));
        XmlRpcClient server = new XmlRpcClient(); 
        server.setConfig(config);
        Vector params = new Vector();
        
        params.addElement(new Integer(17));
        params.addElement(new Integer(13));

        Object result = server.execute("sample.sum", params);

        int sum = ((Integer) result).intValue();
        System.out.println("The sum is: "+ sum);

      } catch (Exception exception) {
        System.err.println("JavaClient: " + exception);
      }
   }
}