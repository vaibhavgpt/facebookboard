package org.pihen.facebook.ui;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import org.jdesktop.swingx.JXBusyLabel;
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
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.decorator.PatternMatcher;
import org.pihen.facebook.exporters.CSVExporter;
import org.pihen.facebook.exporters.TxtExporter;
import org.pihen.facebook.services.FacebookServiceImpl;
import org.pihen.facebook.services.IFacebookService;
import org.pihen.facebook.ui.chat.JXFBChatWindow;
import org.pihen.facebook.ui.models.AlbumsCacheModel;
import org.pihen.facebook.ui.models.FriendsTableCacheModel;
import org.pihen.facebook.ui.photos.JXPhotoBrowser;
import org.pihen.facebook.ui.photos.JXPhotoPanel;
import org.pihen.facebook.util.CacheFileGestionnaire;
import org.pihen.facebook.util.PropertiesFileManager;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.User;


public class FacebookSwingWindow extends JXFrame {

	private static final long serialVersionUID = 1L;
	private IFacebookService service;

	private JXList listeAlbums;
	private JXPhotoPanel jxImagePanel;
	private JXBusyLabel lblWaiting = new JXBusyLabel();;
	private JTabbedPane panneauOnglets;
	
	private JXSearchPanel panneauRecherche;
	private JXPhotoBrowser jxPicturesBrowserPanel;
	private JXInfoPanel panneauInfo;
	private JXFBChatWindow chatwindow;
	
	private JXTable tableFriends;
	private PatternMatcher filter;
	private static FacebookSwingWindow instance;
	private FriendsSwingWorker worker;
	
	
	
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
	
	private FacebookSwingWindow() {
		
		service = new FacebookServiceImpl();
		PropertiesFileManager fm = new PropertiesFileManager();
		try
		{
			if(fm.getProperty("first_connect").equals("0")){
				JXFbLoginDialog login = new JXFbLoginDialog();
				login.setVisible(true);
				service.connection(login.getTxtLogin(), login.getTxtPassword());
	
			}
			else
			{
				if(service.connectionByBrowser())
					{
					fm.setProperty("first_connect", "0");
					fm.save();
					}
			}
			this.setTitle("FaceBoard : " + service.getLoggedUser().getName());
		
		
		this.setDefaultCloseOperation(JXFrame.EXIT_ON_CLOSE);
		this.setSize(950, 750);
		this.setLayout(new BorderLayout());
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/677166248.png")));
		this.setLocationRelativeTo(null);
		
		new FaceBoardSystemTray();
		
			//creation du label d'attente
			lblWaiting = new JXBusyLabel();
			lblWaiting.setBorder(new DropShadowBorder());
			getContentPane().add(lblWaiting, BorderLayout.SOUTH);
			
			
			
//////////////////////////////////////////////////////////////////////////////////////////////////panneaux separation entre le taskpanel et le reste
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
							   //filter = new PatternMatcher(".*",Pattern.CASE_INSENSITIVE, 1);
							  // panneauRecherche.setPatternFilter(filter);
							   searchPane.getContentPane().add(panneauRecherche);
						

				//AJOUT DE TOUT LES PANNEAUX DANS LE CONTAINER					
				taskPaneContainer.add(infoGeneralTaskPane);

				taskPaneContainer.add(friendsTaskPane);
				taskPaneContainer.add(searchPane);
							
				panneauGauche.add(taskPaneContainer, BorderLayout.CENTER);
				
				
				
				
				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 1 : JXTABLE CONTENANT LES FRIENDS
				JScrollPane jxTableFriendsScrollerPane = new JScrollPane();
				
				final FriendsTableCacheModel friendsModele = new FriendsTableCacheModel();

						tableFriends= new JXTable();					
						//tableFriends.setFilters(new FilterPipeline(filter));
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
				
				
////////////////////////////////////////////////////////////////////////////////////////////////ONGLET 3 : AFFICHAGE DU WALL DE l'UTILISATEUR (impossible car non supporte par l'api)
				JXWallPanel wall = new JXWallPanel();
					panneauOnglets.addTab("Wall", null, wall, null);

		
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
				
				JMenuItem mnuConfig = new JMenuItem();
					  mnuConfig.setText("Configurer");
					  mnuConfig.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								new ConfigurationWindow().setVisible(true);
							}
						});
					  mnuFichier.add(mnuConfig);
					
				JMenu mnuExport = new JMenu("Exporter");
						  JMenuItem menuCSVExporter = new JMenuItem("CSV");
						  		    menuCSVExporter.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent evt) 
									{
										JFileChooser f = new JFileChooser(".");
										f.setFileFilter(new FileFilter(){
											public boolean accept(File f) {
												return f.getName().endsWith(".csv");
											}
	
											public String getDescription() {
												return "(*.csv) Fichiers CSV";
											}
										});
										f.showSaveDialog(null);
										try {
											File file = f.getSelectedFile();
											new CSVExporter().exports(((FriendsTableCacheModel)getTableFriends().getModel()).getFriends(), file);
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								});
						  mnuExport.add(menuCSVExporter);
						   
						  
						  mnuFichier.add(mnuExport);
						  
						  mnuFichier.add(new JSeparator());
				JMenuItem mnuQuit = new JMenuItem();
							mnuQuit.setText("Quitter");
							mnuQuit.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									System.exit(0);
								}
							});
							mnuFichier.add(mnuQuit);
							
			menuBar.add(mnuFichier);
				
			
			JMenu mnuTool =new JMenu("Outils");
			
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
			
				mnuTool.add(mnuLook);
				
				JMenu mnuDelete = new JMenu("Who remove Me ?");
					
					File[] friendsCache = new CacheFileGestionnaire().getCacheFile().listFiles(new java.io.FileFilter(){
						public boolean accept(File pathname) {
							if(pathname.isDirectory())
								return false;
							if(!pathname.getName().endsWith(".txt"))
								return false;
							
							if(!pathname.getName().startsWith("friendsList-"))
								return false;
							
							return true;
						}
						
					});
					
					for(File f : friendsCache){
						JMenuItem ut = new JMenuItem(f.getName());
						ut.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								String file = ((JMenuItem) evt.getSource()).getText();
								try {
									List<User> deleted = service.compare(friendsModele.getFriends(),new TxtExporter().restore(new File(new CacheFileGestionnaire().getCacheFile().getAbsolutePath()+"/"+file)));
									
									StringBuffer temp = new StringBuffer("Les utilisateurs suivants ne font plus parti de votre liste d'amis :\n");
									for(User u : deleted)
									{
										temp.append(u.getName()).append("\n");
									}
									if(deleted.size()>0)
										JOptionPane.showMessageDialog(null, temp.toString(),"Not Your Friends anymore ;)",JOptionPane.ERROR_MESSAGE);
									
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						});
						
						mnuDelete.add(ut);
					}
				
				mnuTool.add(mnuDelete);
				
				
				menuBar.add(mnuTool);
				
				JMenu mnuAbout = new JMenu();
					mnuAbout.setText("?");
						JMenuItem item = new JMenuItem("About");
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
		
		
		}
		catch(FacebookException e)
		{
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur Facebook",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur de Fichier introuvable",JOptionPane.ERROR_MESSAGE);
			
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur de Fichier",JOptionPane.ERROR_MESSAGE);
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur de process",JOptionPane.ERROR_MESSAGE);
		} catch (URISyntaxException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur de configuration",JOptionPane.ERROR_MESSAGE);
		}
		
	}

	
	private String getHtmlBeanPresentation(User loggedUser) {
		StringBuffer temp = new StringBuffer();
		temp.append("<html>");
		temp.append("<img src='").append(loggedUser.getPicSquare()).append("'/> ");
		temp.append("<b>").append(loggedUser.getLastName()).append(" ").append(loggedUser.getFirstName()).append("</b><br/> (");
		temp.append("<i>").append(loggedUser.getStatus().getMessage()).append("  </i> ) <br/>");
		temp.append(loggedUser.getWallCount()).append(" elements dans le wall<br/>");
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
			// Cr�ation d'un JPopupMenu
			JPopupMenu labelPopupMenu = new JPopupMenu();
			JMenuItem item = new JMenuItem("Exporter");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					new JXAlbumExporterWindow(a);
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
