package org.pihen.facebook.exporters;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.google.code.facebookapi.schema.User;

public class CSVExporter implements IUserExporter{
  
	public CSVExporter() { 
    	
    }

	@Override
	public void export(User u, File f) throws IOException {
		FileWriter out = new FileWriter(f);
		out.write(toDescriptiveString(u,";")+"\n");
		out.close();
		
	}

	@Override
	public void exports(List<User> list, File f) throws IOException {
		  FileWriter out = new FileWriter(f);
	        for(int i=0; i< list.size(); i++) {
	                out.write(toDescriptiveString(list.get(i),";")+"\n");
	        }
	        out.close();
		
	}


	private String toDescriptiveString(User u,String separator) {
		StringBuffer temp = new StringBuffer();
		
		temp.append(u.getUid()).append(separator);
		temp.append(u.getFirstName()).append(separator);
		temp.append(u.getLastName()).append(separator);
		//temp.append((u.getStatus()!=null)? u.getStatus().getMessage():"").append(separator);
		temp.append((u.getSex()!=null)? u.getSex():"").append(separator);
		temp.append((u.getBirthday()!=null)?u.getBirthday():"").append(separator);
		temp.append((u.getCurrentLocation()!=null)?u.getCurrentLocation().getState():"").append(separator);
		temp.append((u.getCurrentLocation()!=null)?u.getCurrentLocation().getCountry():"").append(separator);
		temp.append((u.getCurrentLocation()!=null)?u.getCurrentLocation().getCity():"").append(separator);
		
		return temp.toString();
	}

	public String getExtension() {
		return "csv";
	}

	public List<User> restore(File f) throws FileNotFoundException, IOException,ClassNotFoundException {
		return null;
	}
	
	
	
}
