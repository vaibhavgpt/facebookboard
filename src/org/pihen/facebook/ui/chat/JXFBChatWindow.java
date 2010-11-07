package org.pihen.facebook.ui.chat;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDesktopPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.pihen.facebook.services.IFacebookService;
import org.pihen.facebook.ui.models.FriendsListCacheModel;

import com.google.code.facebookapi.schema.User;



public class JXFBChatWindow extends JXPanel {
	
	private IFacebookService service;
	private JXList jxListeFriends;
	private JSplitPane panneauSplit;
	private JDesktopPane panneauDialogs;
	private JScrollPane panneauScrollFriends;
	private FriendsListCacheModel onlineFriendsModel;
	private Timer timer;
	public JXFBChatWindow() {
	
	}
	
	public JXFBChatWindow(IFacebookService service)
	{
		this.service = service;
		initGUI();
	}
	
	private void initGUI() {
		try {
			{
				this.setLayout(new BorderLayout());
				this.setPreferredSize(new java.awt.Dimension(618, 336));
				{
					panneauSplit = new JSplitPane();
					this.add(panneauSplit, BorderLayout.CENTER);
					{
						panneauScrollFriends = new JScrollPane();
						panneauSplit.add(panneauScrollFriends, JSplitPane.LEFT);
						{
							
							onlineFriendsModel = new FriendsListCacheModel();
							onlineFriendsModel.setUsers(service.getOnlineFriends());
							jxListeFriends = new JXList();
							panneauScrollFriends.setViewportView(jxListeFriends);
							jxListeFriends.setModel(onlineFriendsModel);
							jxListeFriends.setPreferredSize(new java.awt.Dimension(92, 333));
							jxListeFriends.addMouseListener(new MouseAdapter() {
								
								public void mouseClicked(MouseEvent evt) {
									if (evt.getClickCount() == 2) {     
										User f = ((FriendsListCacheModel) jxListeFriends.getModel()).getUserAt(((JXList)evt.getSource()).getSelectedIndex());
										jxListeFriendsMouseClicked(f);
									}
								}
							});
						}
					}
					{
						panneauDialogs = new JDesktopPane();
						panneauSplit.add(panneauDialogs, JSplitPane.RIGHT);
						panneauDialogs.setBackground(new java.awt.Color(109,159,201));

					}
				}

				//timer pour les notifications
				timer = new Timer(2000,  
						new ActionListener() {
					  		public void actionPerformed(ActionEvent evt) 
					  		{
					  			new SwingWorker<Void, Void>()
					  			{

									protected Void doInBackground()throws Exception {
										onlineFriendsModel.setUsers(service.getOnlineFriends());
							  			jxListeFriends.updateUI();
							  			return null;
									}
					  				
					  			}.execute();
					  			
					  		}
					});
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	private void jxListeFriendsMouseClicked(User f) {
		panneauDialogs.add(new JxDialogWindow(f));
	}

}
