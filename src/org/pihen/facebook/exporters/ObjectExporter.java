package org.pihen.facebook.exporters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.code.facebookapi.schema.User;


//waiting for serialization of facebook-api-schema model object

public class ObjectExporter implements IUserExporter{
 	
	public ObjectExporter() { 
    	
    }

	@Override
	public void export(User u, File f) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(f));
		oos.writeObject(u);
		oos.close();
	}

	@Override
	public void exports(List<User> list, File f) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream( new FileOutputStream(f));
		for(User u:list)
			oos.writeObject(u);
		oos.close();
	}

	@Override
	public List<User> restore(File f) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream( new FileInputStream(f));
		List<User> list = new ArrayList<User>();
		Object readed=ois.readObject();
		while(readed!=null)
		{
			User u = (User)readed;
			list.add(u);
			readed = ois.readObject();
		}
		ois.close();
		return list;
	}
    
	@Override
	public String getExtension() {
		return "fbu";
	}
	
	
	
}
