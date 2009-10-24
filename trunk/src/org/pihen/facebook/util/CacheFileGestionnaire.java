package org.pihen.facebook.util;

import java.io.File;
import java.io.IOException;

public class CacheFileGestionnaire {

	
	public File getCacheFile() throws IOException
	{
		String dir = new PropertiesFileManager().getProperty("cache_friends_directory");
		String userHome = System.getProperty("user.home"); 
		File cachedir = new File(userHome + File.separator+dir+File.separator);
	
		if(!cachedir.exists())
			cachedir.mkdir();
		
		return cachedir;
	}
	
}
