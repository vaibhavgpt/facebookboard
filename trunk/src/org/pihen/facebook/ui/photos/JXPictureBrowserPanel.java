package org.pihen.facebook.ui.photos;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.pihen.facebook.ui.FacebookSwingWindow;

import com.google.code.facebookapi.schema.Photo;


public class JXPictureBrowserPanel extends JXPanel {


	private static final long serialVersionUID = 1L;

	public JXPictureBrowserPanel() {
		initGUI();
	}
	
	
	private void initGUI() {
		
	
		setLayout(new FlowLayout());
	}


	public void setPhotosToPanel(final List<Photo> photos) 
	{
		FacebookSwingWindow.getInstance().getLblWaiting().setBusy(true);
		FacebookSwingWindow.getInstance().getLblWaiting().setText("Chargement des photos en cours");
		
					new SwingWorker<Void,Photo>(){
						
						private List<Photo> arrayTemp = new ArrayList<Photo>();
						
						protected Void doInBackground()  
						{
							for( final Photo pic : photos)
							{
								arrayTemp.add(pic);
								BufferedImage im=null;
								try {
									im = ImageIO.read(new URL(pic.getSrcSmall()));
								} catch (Exception e) {
									e.printStackTrace();
								} 
							JXImagePanel smallPicImage = new JXImagePanel();
										smallPicImage.setImage(im);
										smallPicImage.setEditable(false);
 										smallPicImage.setBorder(new DropShadowBorder());
										smallPicImage.setToolTipText(pic.getCaption());
										smallPicImage.setPreferredSize(new java.awt.Dimension(120, 80));
										smallPicImage.addMouseListener(new MouseAdapter() {
														public void mouseClicked(MouseEvent evt) {
																FacebookSwingWindow.getInstance().getJxImagePanel().setUrlPhoto(pic.getSrcBig());
														}
													});
							add(smallPicImage);
								
							publish((Photo[]) arrayTemp.toArray(new Photo[arrayTemp.size()]));
							
							}
							return null;
						}
						
						protected void done() {
							FacebookSwingWindow.getInstance().getLblWaiting().setText("Chargement des photos terminé");
							FacebookSwingWindow.getInstance().getLblWaiting().setBusy(false);
						}
						
						protected void process(List<Photo> chunks) {
							FacebookSwingWindow.getInstance().getLblWaiting().setText("Chargement des photos en cours " + chunks.size() +"/" + photos.size());
							updateUI();
						}
						
					}.execute();
					
			}
	}
	

	