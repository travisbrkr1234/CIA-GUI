package org.supdev;

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
import org.apache.commons.csv.*;


interface Imports {

    //  wheel revolutions per minute
    ArrayList<Map> getAllContacts();
    Integer createContact(Map contactData);
    ArrayList<Map> getContactsAndFK();

    ArrayList<Map> getCSVData(String pathToCSV);
    void saveCSVData(ArrayList<Map> datum);

}
