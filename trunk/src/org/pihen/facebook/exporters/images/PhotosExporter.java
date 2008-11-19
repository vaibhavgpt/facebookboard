package org.pihen.facebook.exporters.images;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.pihen.facebook.ui.FacebookSwingWindow;

import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Photo;

public class PhotosExporter{

	public void exporter(Album a, File dir,String type) {
		
		
	}

	public void exporter(Photo p, File f,String type) {
		try {
			BufferedImage img = ImageIO.read(new URL(p.getSrcBig()));
			File dest = new File(f.getAbsolutePath()+File.pathSeparator+p.getPid()+"."+type);
			ImageIO.write(img, type, dest);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

}