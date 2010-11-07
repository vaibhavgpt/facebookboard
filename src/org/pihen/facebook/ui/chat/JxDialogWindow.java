package org.pihen.facebook.ui.chat;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.pihen.facebook.ui.FacebookSwingWindow;

import com.google.code.facebookapi.schema.User;




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
public class JxDialogWindow extends JInternalFrame {
	private JEditorPane conversationEditorPane;
	private JTextField txtMessage;
	private JScrollPane jScrollPane1;
	private JXLabel jxLabelUserChat;
	private User user;
	
	
	public JxDialogWindow()
	{
		initGUI();
	}
	
	
	public JxDialogWindow(User f) {
		this.user=f;
		initGUI();
	}


	private void initGUI() {
		try {
			{
				this.setPreferredSize(new java.awt.Dimension(321, 324));
				BorderLayout thisLayout = new BorderLayout();
				this.setBounds(0, 0, 321, 324);
				getContentPane().setLayout(thisLayout);
				{
					jScrollPane1 = new JScrollPane();
					getContentPane().add(jScrollPane1, BorderLayout.CENTER);
					jScrollPane1.setPreferredSize(new java.awt.Dimension(278, 275));
					{
						conversationEditorPane = new JEditorPane();
						jScrollPane1.setViewportView(conversationEditorPane);
						conversationEditorPane.setBorder(new DropShadowBorder());
						{
							jxLabelUserChat = new JXLabel();
							
							conversationEditorPane.add(jxLabelUserChat);
							if(user.getStatus()!=null)
								jxLabelUserChat.setText(user.getStatus().getMessage());
							
							if(user.getPicSquare().equals(""));
								jxLabelUserChat.setIcon(new ImageIcon(new URL(user.getPicSquare())));
							
							jxLabelUserChat.setPreferredSize(new java.awt.Dimension(319, 49));
							getContentPane().add(jxLabelUserChat, BorderLayout.NORTH);
						}
					}
				}
				{
					txtMessage = new JTextField();
					
					getContentPane().add(txtMessage, BorderLayout.SOUTH);
					txtMessage.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							txtMessageActionPerformed(evt);
						}
					});
					setTitle(user.getName());
					setResizable(true);
					setVisible(true);
					setClosable(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void txtMessageActionPerformed(ActionEvent evt) {
		
		conversationEditorPane.setText(conversationEditorPane.getText()+"\n"+txtMessage.getText());
		try {
			FacebookSwingWindow.getInstance().getService().sendMessage(user, txtMessage.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		}
		txtMessage.setText("");
	}

}
