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
				this.setBounds(0, 0, 410, 275);
				this.setResizable(false);
				this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				getContentPane().setLayout(null);
				this.setTitle("About Faceboard");
				this.setPreferredSize(new java.awt.Dimension(413, 241));
					lblLogin = new JLabel();
					getContentPane().add(lblLogin);
					System.out.println(fm);
					lblLogin.setText("<html>Developper : Nicolas Pihen<br> mail : nicolas.pihen@gmail.com<br>Version : "+fm.getProperty("version")+"</html>");
					lblLogin.setBounds(12, 110, 380, 95);
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

			this.setSize(410, 275);
			setVisible(true);
	}
}

