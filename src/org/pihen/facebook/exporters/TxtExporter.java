package org.pihen.facebook.exporters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.code.facebookapi.schema.User;

public class TxtExporter implements IUserExporter{
  
	public TxtExporter() { 
    	
    }

	public void export(User u, File f) throws IOException {
		FileWriter out = new FileWriter(f);
		out.write(toDescriptiveString(u,";")+"\n");
		out.close();
		
	}

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
		return "txt";
	}

	public List<User> restore(File f) throws FileNotFoundException, IOException,ClassNotFoundException {
		
		BufferedReader lecteur = new BufferedReader(new FileReader(f));
		String ligne = lecteur.readLine();
		List<User> liste = new ArrayList<User>();
		
		while(ligne!=null)
		{
			String[] elements = ligne.split(";");
			//TODO completer la les elements du friends
			User u = new User();
			u.setUid(Long.parseLong(elements[0]));
			u.setFirstName(elements[1]);
			u.setLastName(elements[2]);
			u.setName(u.getFirstName() + " " + u.getLastName());
			liste.add(u);
			ligne=lecteur.readLine();
		}
		return liste;
	}

	
}
