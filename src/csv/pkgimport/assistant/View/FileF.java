/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csv.pkgimport.assistant.View;

import java.io.File;
import javax.swing.filechooser.*;

/**
 *
 * @author carlos.ochoa
 */
public class FileF extends FileFilter{
    private String imageFormat = "csv";
    private char dotIndex = '.';
    
    public boolean accept(File F)
    {
        if(F.isDirectory())
        {
            return true;
        }
        if(extension(F).equalsIgnoreCase(imageFormat))
        {
            return true;
        }else 
            return false;
        }
    public String getDescription()
    {
        return"CSV Format Only";
    }
    public String extension (File F)
    {
        String FileName = F.getName();
        int IndexFile = FileName.lastIndexOf(dotIndex);
        if(IndexFile > 0 && IndexFile < FileName.length()-1)
        {
            return FileName.substring(IndexFile+1);
        }else
        {
            return" ";
        }
    }
}