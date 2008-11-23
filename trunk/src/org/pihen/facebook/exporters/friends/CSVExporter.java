package org.pihen.facebook.exporters.friends;

import java.io.File;
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

	@Override
	public List<User> restore() {
		// TODO Auto-generated method stub
		return null;
	}
    

	private String toDescriptiveString(User u,String separator) {
		StringBuffer temp = new StringBuffer();
		
		temp.append(u.getUid()).append(separator);
		temp.append(u.getFirstName()).append(separator);
		temp.append(u.getLastName()).append(separator);
		temp.append((u.getStatus().getValue()!=null)? u.getStatus().getValue().getMessage():"").append(separator);
		temp.append((u.getSex().getValue()!=null)? u.getSex().getValue():"").append(separator);
		temp.append((u.getBirthday().getValue()!=null)?u.getBirthday().getValue():"").append(separator);
		temp.append((u.getCurrentLocation().getValue()!=null)?u.getCurrentLocation().getValue().getState():"").append(separator);
		temp.append((u.getCurrentLocation().getValue()!=null)?u.getCurrentLocation().getValue().getCountry():"").append(separator);
		temp.append((u.getCurrentLocation().getValue()!=null)?u.getCurrentLocation().getValue().getCity():"").append(separator);
		
		return temp.toString();
	}

	@Override
	public String getExtension() {
		return "csv";
	}
	
	
	
}
