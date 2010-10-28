package org.pihen.facebook.ui;
import java.awt.BorderLayout;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXImageView;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;
import org.pihen.facebook.ui.models.GroupsCacheModel;
import org.pihen.facebook.ui.models.SchoolCacheModel;

import com.google.code.facebookapi.schema.User;
import com.google.code.facebookapi.schema.WorkInfo;



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
public class JXInfoPanel extends JXPanel {
	private JXImageView imgAvatar;
	private JXList jxListeSchool;
	private JXEditorPane professionnalEditorPan;
	private JSplitPane splitPaneGauche;
	private JSplitPane splitPaneDroite;
	private JXList groupsJXList;
	private JScrollPane groupeScrollPanel;
	private JXTitledPanel groupsPanel;
	private JScrollPane ecoleScrollPane;
	private JScrollPane professionnelScrollPane;
	private JXTitledPanel panneauCentral;
	private JXTitledPanel ecolePanneau;
	private JXTitledPanel workPanel;
	private JPanel panneaudroit;
	private JPanel panneauGauche;
	private JXTitledPanel panneauGeneral;
	private User user;
	private GroupsCacheModel groupsAlbumsModel ;
	private SchoolCacheModel schoolCacheModel;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public JXTitledPanel getPanneauGeneral() {
		return panneauGeneral;
	}

	public void setImgAvatar(JXImageView imgAvatar) {
		this.imgAvatar = imgAvatar;
	}


	
	public JXInfoPanel()
	{
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(801, 477));
				{
					{
						
						splitPaneGauche = new JSplitPane();
						splitPaneGauche.setContinuousLayout(true);
						{
							panneauGauche = new JPanel();
							splitPaneGauche.add(panneauGauche, JSplitPane.LEFT);
							BoxLayout panneauGaucheLayout = new BoxLayout(panneauGauche, BoxLayout.Y_AXIS);
							panneauGauche.setLayout(panneauGaucheLayout);
							{
								imgAvatar = new JXImageView();
								panneauGauche.add(imgAvatar);
								imgAvatar.setPreferredSize(new java.awt.Dimension(179, 126));
							}
							{
								panneauGeneral = new JXTitledPanel();
								panneauGauche.add(panneauGeneral);
								panneauGeneral.setTitle("Informations Generales");
								
							}
							splitPaneDroite = new JSplitPane();
							splitPaneGauche.add(splitPaneDroite, JSplitPane.RIGHT);
							{
								panneaudroit = new JPanel();
								splitPaneDroite.add(panneaudroit, JSplitPane.RIGHT);
								splitPaneDroite.setContinuousLayout(true);
								BoxLayout panneaudroitLayout = new BoxLayout(panneaudroit, BoxLayout.Y_AXIS);
								panneaudroit.setLayout(panneaudroitLayout);
								panneaudroit.setPreferredSize(new java.awt.Dimension(167, 473));
								{
									workPanel = new JXTitledPanel();
									workPanel.setTitle("Professionnel");
									panneaudroit.add(workPanel);

									{
										professionnelScrollPane = new JScrollPane();
										workPanel.add(professionnelScrollPane, BorderLayout.CENTER);
										{
											professionnalEditorPan = new JXEditorPane();
											professionnelScrollPane.setViewportView(professionnalEditorPan);
										}
									}
								}
								{
									ecolePanneau = new JXTitledPanel();
									panneaudroit.add(ecolePanneau);
									ecolePanneau.setTitle("Crusus scolaire");
									{
										schoolCacheModel = new SchoolCacheModel(); 
										//schoolCacheModel.setListSchool(user.getEducationHistory());
										ecoleScrollPane = new JScrollPane();
										ecolePanneau.add(ecoleScrollPane, BorderLayout.CENTER);
										{
											jxListeSchool = new JXList();
											ecoleScrollPane.setViewportView(jxListeSchool);
									//		jxListeSchool.setModel(schoolCacheModel);
										}
									}
								}
								{
									groupsPanel = new JXTitledPanel();
									panneaudroit.add(groupsPanel);
									groupsPanel.setTitle("Groupes");
									{
										groupeScrollPanel = new JScrollPane();
										groupsPanel.add(groupeScrollPanel, BorderLayout.CENTER);
										{
											//partie du haut (albums et diaporama)
											groupsAlbumsModel = new GroupsCacheModel(); 
											groupsJXList = new JXList();
											groupeScrollPanel.setViewportView(groupsJXList);
											groupsJXList.setModel(groupsAlbumsModel);
										}
									}
								}
							}
							{
								panneauCentral = new JXTitledPanel();
								panneauCentral.getContentContainer().setPreferredSize(new java.awt.Dimension(436, 500));
								splitPaneDroite.add(panneauCentral, JSplitPane.LEFT);
								panneauCentral.setTitleFont(new java.awt.Font("Tahoma",1,14));
							}
						}
						{
						}
						this.add(splitPaneGauche, BorderLayout.CENTER);
					}

				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public GroupsCacheModel getGroupsAlbumsModel() {
		return groupsAlbumsModel;
	}

	public void refresh()
	{
		try {
			panneauCentral.setTitle(user.getName());
			
			String url =user.getPicBig(); 
			if(url.equals(""))
			{
				url="http://static.ak.fbcdn.net/pics/d_silhouette.gif";
			}
			Image image = ImageIO.read(new URL(url));
			imgAvatar.setImage(image);
			
			professionnalEditorPan.setContentType("text/html");
			professionnalEditorPan.setText(getHtmlWorkInfo());
			groupsJXList.updateUI();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	//TODO COMPLETER CETTE FONCTION
	private String getHtmlWorkInfo() {
		
		StringBuffer temp = new StringBuffer("");
		for(WorkInfo wi : user.getWorkHistory().getWorkInfo())
		{
		temp.append("<b>").append(wi.getCompanyName())
			 .append("</b><br/> (")
			 .append(wi.getStartDate()).append(") / ")
			 .append(wi.getEndDate())
			 .append(")<br/>");
			 
		temp.append("<i>").append(wi.getDescription()).append("</i><br/>");
		}
		return temp.toString();
		
		
	}

}
