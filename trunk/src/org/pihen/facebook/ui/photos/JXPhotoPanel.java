package org.pihen.facebook.ui.photos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;



public class JXPhotoPanel extends JXPanel {
	
	
	
	private static final long serialVersionUID = 1L;

	private BufferedImage origImg=null;
	ReflectionRenderer renderer;
	
	
	public JXPhotoPanel()
	{
		initGUI();
	}
	
	

	public void setUrlPhoto(String urlPhoto) {
		
		try {
			origImg = ImageIO.read(new URL(urlPhoto));
		} catch (Exception e) {
			e.printStackTrace();
		} 
		repaint();
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
		//this.setPreferredSize(new java.awt.Dimension(518, 364));
		setBackgroundPainter(new MattePainter(PaintUtils.NIGHT_GRAY,true));
	}


}
