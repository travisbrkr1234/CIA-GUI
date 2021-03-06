/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textfiles;

import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
/**
 *
 * @author carlos.ochoa
 */
public class ReadFile {
    
    private String path;
    
    public ReadFile(String file_path) {
        path = file_path;
    }
    
    public String[] OpenFile() throws IOException {
        
        FileReader fr = new FileReader(path);
        BufferedReader textReader = new BufferedReader(fr);
        
        int numberOfLines = readLines();
        String[] textData = new String[numberOfLines];
        
        int i;
        
        for (i=0; i < numberOfLines; i++) {
            textData[i] = textReader.readLine();
        }
        
        textReader.close();
        return textData;
    }
    int readLines() throws IOException {
        FileReader gile = new FileReader(path);
        BufferedReader bf = new BufferedReader(gile);
        
        String aLine;
        int numberOfLines = 0;
        
        while ((aLine = bf.readLine()) != null) {
            numberOfLines++;
        }
        bf.close();
        
        return numberOfLines;
    }
    
    public static void main(String[] args) throws IOException {
        String file_name = System.getProperty("user.home") + "\\Desktop\\projectSettings.csv";
        
        try {
            ReadFile file = new ReadFile(file_name);
            String[] aryLines = file.OpenFile();
            
            int i;
            for (i=0; i<aryLines.length; i++) {
                System.out.println(aryLines[i]);
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
       }    
    }
}