package org.pihen.facebook.ui.photos;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.ReflectionRenderer;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.util.PaintUtils;




/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
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
		setBackgroundPainter(new MattePainter(PaintUtils.NIGHT_GRAY,true));
		this.setPreferredSize(new java.awt.Dimension(529, 349));
	}


}
