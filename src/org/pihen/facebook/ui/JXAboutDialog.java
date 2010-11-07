package org.pihen.facebook.ui;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXImagePanel;
import org.pihen.facebook.util.PropertiesFileManager;


public class JXAboutDialog extends JDialog {
	
	private JXImagePanel panneauLogo;
	private JLabel lblLogin;
	private PropertiesFileManager fm;

	public JXAboutDialog()
	{
		fm=new PropertiesFileManager();
		initGUI();
	}
	
	
	private void initGUI() {
				
				this.setPreferredSize(new java.awt.Dimension(410, 275));
				this.setBounds(0, 0, 412, 194);
				this.setResizable(false);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				getContentPane().setLayout(null);
				this.setTitle("About Faceboard");
				this.setPreferredSize(new java.awt.Dimension(412, 194));
					lblLogin = new JLabel();
					getContentPane().add(lblLogin);
					lblLogin.setText("<html>Developper : Nicolas Pihen<br> mail : nicolas.pihen@gmail.com<br>Version : "+fm.getProperty("version")+"</html>");
					lblLogin.setBounds(12, 110, 380, 54);
					lblLogin.setVerticalAlignment(SwingConstants.TOP);
					panneauLogo = new JXImagePanel();
					getContentPane().add(panneauLogo);
					panneauLogo.setBounds(0, 0, 409, 91);
					panneauLogo.setImage(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/about.png"))).getImage());
			Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
			setLocation(
			        (screenSize.width-getWidth())/3,
			        (screenSize.height-getHeight())/3
			        );

			this.setSize(412, 194);
			setVisible(true);
	}
}

