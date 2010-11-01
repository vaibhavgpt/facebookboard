package org.pihen.facebook.ui.photos;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.pihen.facebook.ui.FacebookSwingWindow;
import org.pihen.facebook.ui.models.JXImageLabelCacheModel;

import com.google.code.facebookapi.schema.Photo;


public class JXPhotoBrowser extends JXList {


	private static final long serialVersionUID = 1L;

	public JXPhotoBrowser() {
		initGUI();
	}
	
	
	private void initGUI() {
		setLayoutOrientation(JXList.HORIZONTAL_WRAP);
		setFixedCellWidth(100);
		setFixedCellHeight(80);
		setVisibleRowCount(-1);
	}


	public void setPhotosToPanel(final List<Photo> photos) 
	{
		final JXImageLabelCacheModel model = new JXImageLabelCacheModel();
		this.setModel(model);
		this.setCellRenderer(new PhotoBrowserCellRenderer());
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {
				JXList t = (JXList) evt.getComponent();
				JXThumbnailLabel comp = (JXThumbnailLabel)t.getElementAt(t.getSelectedIndex());
				FacebookSwingWindow.getInstance().getJxImagePanel().showPhoto(comp.getPhoto());
			}
		});

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
								JXThumbnailLabel smallPicImage = new JXThumbnailLabel();
										smallPicImage.setPhoto(pic);
										smallPicImage.setImage(im);
										smallPicImage.setEditable(false);
 										smallPicImage.setBorder(new DropShadowBorder());
										smallPicImage.setToolTipText(pic.getCaption());
										model.getPhotos().add(smallPicImage);
										
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
	

	