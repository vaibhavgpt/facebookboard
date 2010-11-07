package org.pihen.facebook.ui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Photo;

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
public class JXAlbumExporterWindow extends JFrame {
	private JProgressBar progressionBarre;
	private JButton btnSelectionRepertoire;
	private JButton btnExporter;
	private JComboBox cboTypeExport;
	private JLabel lblTypeExport;
	private JLabel lblTitreAlbume;
	private JTextField txtRepertoire;
	private Album album;
	private JXAlbumExporterWindow instance;
	
	public Album getAlbum() {
		return album;
	}
	public JXAlbumExporterWindow(Album a)
	{
		this.album=a;
		instance = this;
		initGUI();
	}
	
	
	private void initGUI() {
		try {
			{
				getContentPane().setLayout(null);
				this.setResizable(false);
				this.setTitle("Export d'album Photo " + album.getName());
				{
					progressionBarre = new JProgressBar();
					getContentPane().add(getBtnExporter());
					getContentPane().add(progressionBarre);
					progressionBarre.setBounds(26, 116, 342, 25);
					progressionBarre.setMaximum(album.getSize());
					progressionBarre.setMinimum(0);
				}
				{
					btnSelectionRepertoire = new JButton();
					getContentPane().add(btnSelectionRepertoire);
					btnSelectionRepertoire.setText("...");
					btnSelectionRepertoire.setBounds(342, 50, 26, 21);
					btnSelectionRepertoire.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							JFileChooser f = new JFileChooser(".");
							f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
							f.showOpenDialog(null);
							if(f.getSelectedFile()!=null)
								txtRepertoire.setText(f.getSelectedFile().getAbsolutePath());
						}
					});
				}
				{
					txtRepertoire = new JTextField();
					getContentPane().add(txtRepertoire);
					txtRepertoire.setText("");
					txtRepertoire.setBounds(26, 50, 304, 21);
				}
				{
					lblTitreAlbume = new JLabel();
					getContentPane().add(lblTitreAlbume);
					lblTitreAlbume.setText("Export de l'album photo : ");
					lblTitreAlbume.setBounds(26, 24, 123, 14);
				}
				{
					lblTypeExport = new JLabel();
					getContentPane().add(lblTypeExport);
					lblTypeExport.setText("Type d'exportation :");
					lblTypeExport.setBounds(26, 83, 123, 20);
				}
				{
					ComboBoxModel cboTypeExportModel = 
						new DefaultComboBoxModel( ImageIO.getReaderFormatNames());
					cboTypeExport = new JComboBox();
					getContentPane().add(cboTypeExport);
					cboTypeExport.setModel(cboTypeExportModel);
					cboTypeExport.setBounds(226, 82, 142, 22);
				}
			}
			{
				this.setSize(402, 207);
				this.setVisible(true);
				this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				this.setLocationRelativeTo(null);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private JButton getBtnExporter() {
		if(btnExporter == null) {
			btnExporter = new JButton();
			btnExporter.setText("Exporter");
			btnExporter.setBounds(149, 147, 88, 21);
			btnExporter.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnExporterActionPerformed(evt);
				}
			});
		}
		return btnExporter;
	}
	
	private void btnExporterActionPerformed(ActionEvent evt) {
		new SwingWorker<Void, Photo>()
		{
			protected Void doInBackground() throws Exception {
				List<Photo> photos;
				List<Photo> temps = new ArrayList<Photo>();
				String type = cboTypeExport.getSelectedItem().toString();
				try {
					photos = FacebookSwingWindow.getInstance().getService().getPhotos(album);
					for(Photo p:photos)
					{
						temps.add(p);
						BufferedImage img = ImageIO.read(new URL(p.getSrcBig()));
						
						if(!new File(txtRepertoire.getText()).exists())
						{
							new File(txtRepertoire.getText()).createNewFile();
						}
						File dest = new File(txtRepertoire.getText()+"/"+p.getPid()+"."+type);
						ImageIO.write(img,type , dest);
						publish((Photo[]) temps.toArray(new Photo[temps.size()]));
					}
				}
				catch (IOException e) {
					this.cancel(true);
					JOptionPane.showMessageDialog(null, "Veuillez selectioner un repertoire valide","erreur de repertoire",JOptionPane.ERROR_MESSAGE);
				} 
				return null;
			}
			
			protected void process(List<Photo> chunks) {
				progressionBarre.setValue(chunks.size());
				
			}

			protected void done() {
				instance.dispose();
			}
		}.execute();
	}

}
