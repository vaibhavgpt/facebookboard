package org.pihen.facebook.ui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXImagePanel;
import org.pihen.facebook.services.IFacebookService;
import org.pihen.facebook.util.PropertiesFileManager;

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
public class JXFbLoginDialog extends JDialog {
	private JTextField txtLogin;
	private JPasswordField txtPassword;
	private JXImagePanel panneauLogo;
	private JButton btnConnexion;
	private JLabel jLabel1;
	private JLabel lblLogin;
	private PropertiesFileManager fm;

	public JXFbLoginDialog()
	{
		fm=new PropertiesFileManager();
		initGUI();
		
	}
	
	
	private void initGUI() {
		try {
			{
				this.setResizable(false);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				getContentPane().setLayout(null);
				this.setTitle("Connexion à Faceboard");
				this.setPreferredSize(new java.awt.Dimension(413, 241));
				{
					txtLogin = new JTextField();
					getContentPane().add(getBtnConnexion());
					getContentPane().add(txtLogin);
					txtLogin.setBounds(159, 101, 201, 21);
				}
				{
					txtPassword = new JPasswordField();
					getContentPane().add(txtPassword);
					txtPassword.setBounds(159, 130, 201, 21);
					txtPassword.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnConnexionActionPerformed(evt);
						}
					});
				}
				{
					lblLogin = new JLabel();
					getContentPane().add(lblLogin);
					lblLogin.setText("Email :");
					lblLogin.setBounds(60, 104, 87, 14);
				}
				{
					jLabel1 = new JLabel();
					getContentPane().add(jLabel1);
					jLabel1.setText("Mot de passe : ");
					jLabel1.setBounds(60, 137, 87, 14);
				}
				{
					panneauLogo = new JXImagePanel();
					getContentPane().add(panneauLogo);
					panneauLogo.setBounds(0, 0, 409, 89);
					panneauLogo.setImage(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/login.png"))).getImage());
				}
			}
			
			if(fm.getProperty("last_login")!=null)
			{
				txtLogin.setText(fm.getProperty("last_login"));
				txtPassword.requestFocusInWindow();
			}
			Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(
			        (screenSize.width-getWidth())/3,
			        (screenSize.height-getHeight())/3
			        );

			this.setSize(413, 241);
			this.setModal(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	* This method should return an instance of this class which does 
	* NOT initialize it's GUI elements. This method is ONLY required by
	* Jigloo if the superclass of this class is abstract or non-public. It 
	* is not needed in any other situation.
	 */
	public static Object getGUIBuilderInstance() {
		return new JXFbLoginDialog(Boolean.FALSE);
	}
	
	/**
	 * This constructor is used by the getGUIBuilderInstance method to
	 * provide an instance of this class which has not had it's GUI elements
	 * initialized (ie, initGUI is not called in this constructor).
	 */
	public JXFbLoginDialog(Boolean initGUI) {
		super();
	}
	
	public String getTxtLogin() {
		return txtLogin.getText();
	}
	
	public String getTxtPassword() {
		return new String(txtPassword.getPassword());
	}
	
	private JButton getBtnConnexion() {
		if(btnConnexion == null) {
			btnConnexion = new JButton();
			btnConnexion.setText("Connexion");
			btnConnexion.setBounds(153, 181, 107, 21);
			btnConnexion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					btnConnexionActionPerformed(evt);
				}
			});
		}
		return btnConnexion;
	}
	
	private void btnConnexionActionPerformed(ActionEvent evt) {
		fm.setProperty("last_login", txtLogin.getText());
		try {
			fm.save();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		}
		this.dispose();
	}


	public boolean authenticate(IFacebookService service) {
		try {
			return service.connection(getTxtLogin(), getTxtPassword());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
			return false;
		} 
	}

}
