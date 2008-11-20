package org.pihen.facebook.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXBusyLabel;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXFrame;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXSearchPanel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.decorator.FilterPipeline;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternFilter;
import org.pihen.facebook.exporters.friends.ExcelExporter;
import org.pihen.facebook.services.FacebookServiceImpl;
import org.pihen.facebook.services.IFacebookService;
import org.pihen.facebook.ui.chat.JXFBChatWindow;
import org.pihen.facebook.ui.models.AlbumsCacheModel;
import org.pihen.facebook.ui.models.FriendsTableCacheModel;
import org.pihen.facebook.ui.photos.JXPhotoBrowser;
import org.pihen.facebook.ui.photos.JXPhotoPanel;
import org.pihen.facebook.ui.photos.PhotosExporter;
import org.pihen.facebook.util.PropertiesFileManager;

import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Notifications;
import com.google.code.facebookapi.schema.User;


public class FacebookSwingWindow extends JXFrame {

	private static final long serialVersionUID = 1L;
	private IFacebookService service;

	private JXList listeAlbums;
	private JXPhotoPanel jxImagePanel;
	private JXBusyLabel lblWaiting = new JXBusyLabel();;
	private JTabbedPane panneauOnglets;
	private JXTaskPane notificationsTaskPane;
	private JXSearchPanel panneauRecherche;
	private JXPhotoBrowser jxPicturesBrowserPanel;
	private JXInfoPanel panneauInfo;
	private JXFBChatWindow chatwindow;
	
	private JXTable tableFriends;
	private PatternFilter filter;
	private FaceBoardSystemTray systray;
	private Timer timer;
	private static FacebookSwingWindow instance;
	private FriendsSwingWorker worker;
	private JXLabel labCourrier;
	private JXLabel labPokes;
	private JXLabel labFriends;
	private JXLabel labEvent;
	private JXLabel labGroup ;
	
	
	
	public static FacebookSwingWindow getInstance()
	{
		if(instance==null)
			try {
				instance = new FacebookSwingWindow();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return instance;
	}
	
	private FacebookSwingWindow() throws Exception {
		
		service = new FacebookServiceImpl();
		PropertiesFileManager fm = new PropertiesFileManager();
		
		if(fm.getProperty("first_connect").equals("0")){
			JXFbLoginDialog login = new JXFbLoginDialog();
			login.setVisible(true);
			login.authenticate(service);
		}
		else
		{
			if(service.connectionByBrowser())
				{
				fm.setProperty("first_connect", "0");
				fm.save();
				}
		}
		
		this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
		this.setSize(950, 750);
		this.setLayout(new BorderLayout());
		this.setTitle("FaceBoard : " + service.getLoggedUser().getName());
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/677166248.png")));
		this.setLocationRelativeTo(null);
		
		
			//creation du label d'attente
			lblWaiting = new JXBusyLabel();
			lblWaiting.setBorder(new DropShadowBorder());
			getContentPane().add(lblWaiting, BorderLayout.SOUTH);
			
			
			
//////////////////////////////////////////////////////////////////////////////////////////////////panneaux séparation entre le taskpanel et le reste
			JSplitPane taskSeperatorPane = new JSplitPane();
						taskSeperatorPane.setContinuousLayout(true);
			
			getContentPane().add(taskSeperatorPane, BorderLayout.CENTER);
			
			JXTitledPanel panneauGauche = new JXTitledPanel();
					panneauGauche.setBorder(new DropShadowBorder());
					panneauGauche.setTitle("Menu");
			taskSeperatorPane.add(panneauGauche, JSplitPane.LEFT);
					
			
			JXTitledPanel panneauDroite = new JXTitledPanel();
							panneauDroite.setBorder(new DropShadowBorder());
							panneauDroite.setPreferredSize(new java.awt.Dimension(459,512));
							panneauDroite.setTitle("Tableau de bord");
							panneauOnglets = new JTabbedPane();
							panneauDroite.add(panneauOnglets, BorderLayout.CENTER);
							
			taskSeperatorPane.add(panneauDroite, JSplitPane.RIGHT);
			
			
////////////////////////////////////////////////////////////////////////////////////////////////PANNEAU DE GAUCHE : CONTIENT TOUTES LES TACHES
			JXTaskPaneContainer taskPaneContainer = new JXTaskPaneContainer();
			taskPaneContainer.setFocusable(false);
			taskPaneContainer.setScrollableTracksViewportWidth(false);
					
			
					//AJOUT DU PANNEAU INFO GENERAL
					JXTaskPane infoGeneralTaskPane = new JXTaskPane();
							   infoGeneralTaskPane.setTitle("Info");
							   infoGeneralTaskPane.add(new JXLabel(getHtmlBeanPresentation(service.getLoggedUser())));
							   JXHyperlink lien = new JXHyperlink();
							   lien.setText("Mes infos");
							   lien.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent evt) {
										User u = new User();
										try {
											u = service.getLoggedUser();
										} catch (Exception e) {
											JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
										} 
										selectionTableUtilisateurMouseClicked(evt,u);
									}
								});
							   infoGeneralTaskPane.add(lien);
								
					//AJOUT DU PANNEAU DE NOTIFICATIONS
							    notificationsTaskPane = new JXTaskPane();
								notificationsTaskPane.setTitle("Notifications");
								
								Notifications notif = service.getNotifications();
								
								labCourrier = new JXLabel("Courrier non lu :"+ notif.getMessages().getUnread(),new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/mail.gif"))),JXLabel.LEFT);
								notificationsTaskPane.add(labCourrier);
								
								labPokes = new JXLabel("Vous avez été poké "+ notif.getPokes().getUnread()+ " fois",new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/poke.gif"))),JXLabel.LEFT);
								notificationsTaskPane.add(labPokes);
								
								labFriends = new JXLabel("Invitation d'ami : "+ notif.getFriendRequests().getUid().size(),new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/friend.gif"))),JXLabel.LEFT);
								notificationsTaskPane.add(labFriends);
								
								labEvent = new JXLabel("Invitation d'evenements : "+ notif.getEventInvites().getEid().size(),new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/event.gif"))),JXLabel.LEFT);
								notificationsTaskPane.add(labEvent);
								
								labGroup = new JXLabel("Invitation groupe : "+ notif.getGroupInvites().getGid().size(),new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/group.gif"))),JXLabel.LEFT);
								notificationsTaskPane.add(labGroup);
								
								initNotifications();
								//timer pour les notifications
								timer = new Timer(Integer.parseInt(new PropertiesFileManager().getProperty("refresh")),  
										new ActionListener() {
									  		public void actionPerformed(ActionEvent evt) 
									  		{
									  			initNotifications();
									  		}
									});
								timer.start(); 
								
								
								
					
					//AJOUT DU PANNEAU DU NOMBRE D'AMIS
					JXTaskPane friendsTaskPane = new JXTaskPane();
							   friendsTaskPane.setTitle("Friends");
							   friendsTaskPane.add(new JXLabel(service.getNbFriends(service.getLoggedUser())+ " ami(s)"));
					
					JXHyperlink lienRefresh = new JXHyperlink();
								lienRefresh.setText("Rafraichir");
								lienRefresh.addMouseListener(new MouseAdapter() {
									public void mouseClicked(MouseEvent evt) {
										worker=new FriendsSwingWorker(FacebookSwingWindow.getInstance());
										worker.execute();
									}
								});
								friendsTaskPane.add(lienRefresh);
								
							   
					//AJOUT DU PANNEAU DE RECHERCHE		   
					JXTaskPane searchPane = new JXTaskPane();
							   searchPane.setTitle("Recherche");
					
							   panneauRecherche = new JXSearchPanel();
							   panneauRecherche.setPreferredSize(new java.awt.Dimension(174,89));//dimension du panneau pour ne pas etre trop large
							   filter = new PatternFilter(".*",Pattern.CASE_INSENSITIVE, 1);
							   panneauRecherche.setPatternFilter(filter);
							   searchPane.getContentPane().add(panneauRecherche);
						

				//AJOUT DE TOUT LES PANNEAUX DANS LE CONTAINER					
				taskPaneContainer.add(infoGeneralTaskPane);
				taskPaneContainer.add(notificationsTaskPane);
				taskPaneContainer.add(friendsTaskPane);
				taskPaneContainer.add(searchPane);
							
				panneauGauche.add(taskPaneContainer, BorderLayout.CENTER);
				
				
				
				
				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 1 : JXTABLE CONTENANT LES FRIENDS
				JScrollPane jxTableFriendsScrollerPane = new JScrollPane();
				
				FriendsTableCacheModel friendsModele = new FriendsTableCacheModel();

						tableFriends= new JXTable();					
						tableFriends.setFilters(new FilterPipeline(filter));
						tableFriends.setSortable(true);
						tableFriends.setModel(friendsModele);
						tableFriends.setShowVerticalLines(false);
						tableFriends.setColumnControlVisible(true);
						tableFriends.setHighlighters(HighlighterFactory.createAlternateStriping());
						tableFriends.getTableHeader().setBounds(1, -3, 621, 18);
						tableFriends.addMouseListener(new MouseAdapter() {
							public void mouseClicked(MouseEvent evt) {
								JXTable t = (JXTable) evt.getComponent();
								Integer value = (Integer)t.getValueAt(t.getSelectedRow(), 0);

								User f = ((FriendsTableCacheModel) tableFriends.getModel()).getUserAt(value);

								selectionTableUtilisateurMouseClicked(evt,f);
							}
						});									   
						jxTableFriendsScrollerPane.setViewportView(tableFriends);				
						panneauOnglets.addTab("Friends", null, jxTableFriendsScrollerPane,null);						

////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 2 : PANNEAU DE PHOTOS

				JSplitPane panneauSplitHaut = new JSplitPane(JSplitPane.VERTICAL_SPLIT);// separateur entre la liste de photo et la photo
				panneauOnglets.addTab("Photos", null, panneauSplitHaut, null);
				
				//on ajoute en bas le panneau contenant la photo (en bas)
				jxImagePanel = new JXPhotoPanel();
				panneauSplitHaut.add(jxImagePanel, JSplitPane.BOTTOM);

				
				//partie du haut (albums et diaporama)
				AlbumsCacheModel listeAlbumsModel = new AlbumsCacheModel(); 
								 listeAlbumsModel.setAlbums(service.getAlbums(service.getLoggedUser()));
				
				listeAlbums = new JXList();
				listeAlbums.setModel(listeAlbumsModel);
				
				JScrollPane  jxListAlbumsScrollerPane = new JScrollPane();
							 jxListAlbumsScrollerPane.setViewportView(listeAlbums);
				
				listeAlbums.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						selectionAlbumMouseClicked(evt);
					}
				});
				
				
				JSplitPane albumsSeperatorPane = new JSplitPane();
				
				jxPicturesBrowserPanel = new JXPhotoBrowser();
				
				JScrollPane panneauScrollAlbum = new JScrollPane();
							panneauScrollAlbum.setViewportView(jxPicturesBrowserPanel);
				

							  
				albumsSeperatorPane.add(jxListAlbumsScrollerPane, JSplitPane.LEFT);							  
				albumsSeperatorPane.add(panneauScrollAlbum, JSplitPane.RIGHT);

				panneauSplitHaut.add(albumsSeperatorPane, JSplitPane.TOP);
				
				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 3 : AFFICHAGE DU WALL DE l'UTILISATEUR (impossible car non supporté par l'api)			
				panneauOnglets.addTab("Wall", null, new JXEditorPane(), null);

////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 4 : AFFICHAGE DES NEWS (impossible car non supporté par l'api)			
				panneauOnglets.addTab("News Feed", null, new JXEditorPane(), null);

				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 5 : AFFICHAGE DES INFOS DE L'UTILISATEUR SELECTIONNE			
				
				JXPanel userInfoTabPanel = new JXPanel();
					panneauOnglets.addTab("Info", null, userInfoTabPanel, null);
					
						panneauInfo = new JXInfoPanel();
						panneauInfo.setUser(service.getLoggedUser());
						panneauInfo.getGroupsAlbumsModel().setGroups(service.getGroups(service.getLoggedUser()));
						panneauInfo.refresh();
						
						userInfoTabPanel.setLayout(new BorderLayout());
				userInfoTabPanel.add(panneauInfo,BorderLayout.CENTER);

////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 6 : PANNEAU DE CHAT			
				
				chatwindow = new JXFBChatWindow(service);
				
				panneauOnglets.addTab("chat", null, chatwindow, null);
				
				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 7 : PANNEAU DE DEBUGAGE			
				
				panneauOnglets.addTab("debug", null, new Log4JFrame(), null);

		
////////////////////////////////////////////////////////////////////////////////////////////////LA BARRE DE MENU DU HAUT			
		
			JMenuBar menuBar = new JMenuBar();
			
			JMenu mnuFichier = new JMenu();
				mnuFichier.setText("Fichier");
				
				JMenuItem mnuQuit = new JMenuItem();
					mnuQuit.setText("Quitter");
					mnuQuit.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							System.exit(0);
						}
					});
					mnuFichier.add(mnuQuit);

				JMenuItem mnuConfig = new JMenuItem();
					  mnuConfig.setText("Configurer");
					  mnuConfig.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								new ConfigurationWindow().setVisible(true);
							}
						});
					  mnuFichier.add(mnuConfig);
					
				JMenuItem mnuExport = new JMenuItem();
						  mnuExport.setText("Exporter");
						  mnuExport.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									JFileChooser f = new JFileChooser(".");
									f.showSaveDialog(null);
									
									try {
										File file = f.getSelectedFile();
										ExcelExporter.exportTable(tableFriends, file);
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							});
						  mnuFichier.add(mnuExport);
						  
			menuBar.add(mnuFichier);
				
				JMenu mnuLook = new JMenu();
					  mnuLook.setText("Looks");
						for (int i = 0; i < UIManager.getInstalledLookAndFeels().length; i++) {
							JMenuItem item = new JMenuItem(UIManager.getInstalledLookAndFeels()[i].getClassName());
							item.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									changerDeSkin(evt);
								}
							});
							mnuLook.add(item);
						}
				JMenuItem plasticPlafItem = new JMenuItem("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
						  plasticPlafItem.addActionListener(new ActionListener() {
																public void actionPerformed(ActionEvent evt) {
																	changerDeSkin(evt);
																}
															});
				mnuLook.add(plasticPlafItem);
			
				menuBar.add(mnuLook);
				
				
				JMenu mnuAbout = new JMenu();
					mnuAbout.setText("?");
						JMenuItem item = new JMenuItem("about");
						item.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								new JXAboutDialog();
							}
						});
						mnuAbout.add(item);
						menuBar.add(mnuAbout);
						
						
				setJMenuBar(menuBar);
				
//////////////////////////////////////////////////////////////////////////////////LANCEMENT DU CHARGEMENT DES FRIENDS ET AFFICHAGE				
		
		worker = new FriendsSwingWorker(this);
		worker.execute();	
				

		this.setVisible(true);
		
		systray = new FaceBoardSystemTray();
		
	}

	private void initNotifications() {

		Notifications notif = null;
		try{
			notif = service.getNotifications();
			StringBuffer temp = new StringBuffer();
			boolean print = false;
			
			
			labCourrier.setText("Courrier non lu :"+ notif.getMessages().getUnread());
			if(notif.getMessages().getUnread()>0)
			{
				temp.append(notif.getMessages().getUnread()).append(" nouveau(x) courrier");
				print=true;
			}
			
			labPokes.setText("Vous avez été poké "+ notif.getPokes().getUnread()+ " fois");
			if(notif.getPokes().getUnread()>0)
			{
				temp.append("Vous venez d'etre poké");
				print=true;
			}
			
			labFriends.setText("Invitation d'ami : "+ notif.getFriendRequests().getUid().size());
			if(notif.getFriendRequests().getUid().size()>0)
			{
				for(long uid:notif.getFriendRequests().getUid())
					temp.append(service.getUserById(uid).getName()).append(" vous demande d'etre son ami\n");
				
				print=true;
			}
			
			labEvent.setText("Invitation d'evenements : "+ notif.getEventInvites().getEid().size());
			
			if(notif.getEventInvites().getEid().size()>0)
			{
				temp.append("vous avez été invité a ").append(notif.getEventInvites().getEid().size()).append(" nouveau(x) evenement(s)\n");
				print=true;
			}
			labGroup.setText("Invitation groupe : "+ notif.getGroupInvites().getGid().size());
			if(notif.getGroupInvites().getGid().size()>0)
			{
				temp.append("vous avez été invité a ").append(notif.getGroupInvites().getGid().size()).append(" nouveau(x) groupe(s)\n");
				print=true;
			}
			
			if(print)
				systray.displayMessage(temp.toString());
			
			
			
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		}
		
	}

	private String getHtmlBeanPresentation(User loggedUser) {
		StringBuffer temp = new StringBuffer();
		temp.append("<html>");
		temp.append("<img src='").append(loggedUser.getPicSquare().getValue()).append("'/> ");
		temp.append("<b>").append(loggedUser.getLastName()).append(" ").append(loggedUser.getFirstName()).append("</b><br/> (");
		temp.append("<i>").append(loggedUser.getStatus().getValue().getMessage()).append(" ) </i><br/>");
		temp.append(loggedUser.getWallCount().getValue()).append(" elements dans le wall<br/>");
		temp.append("</html>");
		return temp.toString();
	}

	private void changerDeSkin(ActionEvent evt) {
		try {
			UIManager.setLookAndFeel(((JMenuItem) evt.getSource()).getText());
			SwingUtilities.updateComponentTreeUI(this);
		} catch (Exception e) {
		}
	}
	
	private void selectionTableUtilisateurMouseClicked(MouseEvent evt,User f) {
		
		panneauOnglets.setTitleAt(4, "Infos de " + f.getName());
		
		List<Album> albums;
		try {
			albums = service.getAlbums(f);
		
		((AlbumsCacheModel)listeAlbums.getModel()).setAlbums(albums);
		
		panneauOnglets.setTitleAt(1, "Photos (" + albums.size()+")");
		
		
	
		panneauInfo.setUser(f);
		panneauInfo.getGroupsAlbumsModel().setGroups(service.getGroups(f));

		listeAlbums.updateUI();
		panneauInfo.refresh();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		}

	}

	private void selectionAlbumMouseClicked(MouseEvent evt) {
		
		final Album a = ((AlbumsCacheModel) listeAlbums.getModel()).getAlbums().get(((JXList)evt.getComponent()).getSelectedIndex());
		
		
		if(evt.getButton() == MouseEvent.BUTTON3) {
			// Création d'un JPopupMenu
			JPopupMenu labelPopupMenu = new JPopupMenu();
			JMenuItem item = new JMenuItem("Exporter");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					new JXExporterWindow(a);
				}
			});
			labelPopupMenu.add(item);
			labelPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
		}
		else
		{
				jxPicturesBrowserPanel.removeAll();
				try {
					jxPicturesBrowserPanel.setPhotosToPanel(service.getPhotos(a));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
				}
		}
	}

	public JXPhotoPanel getJxImagePanel() {
		return jxImagePanel;
	}

	public JXBusyLabel getLblWaiting() {
		return lblWaiting;
	}

	public JXTable getTableFriends() {
		return tableFriends;
	}
	
	public JXSearchPanel getPanneauRecherche() {
		return panneauRecherche;
	}
	
	public IFacebookService getService() {
		return service;
	}
	public JXFBChatWindow getChatwindow() {
		return chatwindow;
	}


}
