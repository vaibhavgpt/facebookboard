package org.pihen.facebook.ui.photos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;
import org.pihen.facebook.ui.FacebookSwingWindow;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.schema.Photo;
import com.google.code.facebookapi.schema.PhotoTag;



public class JXPhotoPanel extends JXPanel {
	
	
	
	private static final long serialVersionUID = 1L;

	private BufferedImage origImg=null;
	ReflectionRenderer renderer;
	private Photo p;
	
	public JXPhotoPanel()
	{
		initGUI();
	}
	
	

	public void setUrlPhoto(Photo photo) {
		this.p=photo;
		try {
			origImg = ImageIO.read(new URL(p.getSrcBig()));
			printInfo();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		repaint();
	}

	public void printInfo()
	{
		try {
			List<PhotoTag> tags= FacebookSwingWindow.getInstance().getService().getTags(p);
			for(PhotoTag tag : tags)
			{
				System.out.println("x="+tag.getXcoord()+",y="+tag.getYcoord());
				System.out.println(FacebookSwingWindow.getInstance().getService().getUserById(tag.getSubject()).getName());
			}
			
		} catch (FacebookException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		if(origImg !=null)
		{
			BufferedImage newImg =renderer.appendReflection(origImg);
			 int w = getWidth();
			    int h = getHeight();
			    int x = (w - newImg.getWidth())/2;
			    int y = (h - newImg.getHeight())/2;
	
			g.drawImage(newImg,x, y,null);
		}
	}



	private void initGUI() {
		renderer = new ReflectionRenderer();
		setBackgroundPainter(new MattePainter(PaintUtils.NIGHT_GRAY,true));
		this.setPreferredSize(new java.awt.Dimension(529, 349));
	}


}
