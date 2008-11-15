package org.pihen.facebook.exporters.friends;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.table.TableModel;

import org.jdesktop.swingx.JXTable;

public class ExcelExporter  {
    public ExcelExporter() { 
    	
    }
    
    public static void exportTable(JXTable table, File file) throws IOException {
        TableModel model = table.getModel();
        FileWriter out = new FileWriter(file);
        
        for(int i=0; i < model.getColumnCount(); i++) {
            out.write(model.getColumnName(i) + ";");
        }
        out.write("\n");
        for(int i=0; i< model.getRowCount(); i++) {
            for(int j=0; j < model.getColumnCount(); j++) {
                out.write(model.getValueAt(i,j)+";");
            }
            out.write("\n");
        }
        out.close();
    }
    
    
}
