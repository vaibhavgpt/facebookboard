package org.pihen.facebook.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

public class PropertiesFileManager {
	public static final String CONFIG_FILE = "/conf/settings.conf";
	
	private static Properties props;
		
	public void chargerParametres()
	{
		try {
		InputStream fis = getClass().getResourceAsStream(CONFIG_FILE);
	    	props = new Properties();
			props.load(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PropertiesFileManager()
	{
		chargerParametres();
	}
	
	public String getProperty(String string) {
		if(props.getProperty(string)!=null)
			return props.getProperty(string).trim();
		else
			return null;
	}
	
	public void setProperty(String key,String value) {
		props.setProperty(key, value.trim());
	}
	
	public void save() throws FileNotFoundException, IOException,URISyntaxException
	{
		props.store(new FileOutputStream(new File(getClass().getResource(CONFIG_FILE).toURI())), "");
	}
	
}
