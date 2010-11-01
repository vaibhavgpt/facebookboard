package org.pihen.facebook.ui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
public class ConfigurationWindow extends JDialog {
	private JTextField txtBrowser;
	private JLabel lblBrowser;
	private JTextField txtSecretKey;
	private JLabel lblSecretKey;
	private JTextField txtApiKey;
	private JLabel lblAPIKEY;
	private JButton btnOk;
	private JButton btnBrowser;
	private PropertiesFileManager manager;
	
	public ConfigurationWindow()
	{
		manager = new PropertiesFileManager();
		initGUI();
	}
	
	
	
	private void initGUI() {
		try {
			setModal(true);
			GridBagLayout thisLayout = new GridBagLayout();
			thisLayout.rowWeights = new double[] {0.1, 0.1, 0.1, 0.1, 0.1};
			thisLayout.rowHeights = new int[] {7, 7, 7, 7, 20};
			thisLayout.columnWeights = new double[] {0.0, 0.0, 0.0, 0.1};
			thisLayout.columnWidths = new int[] {75, 88, 104, 7};
			getContentPane().setLayout(thisLayout);
			{
				txtBrowser = new JTextField();
				getContentPane().add(txtBrowser, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtBrowser.setText(manager.getProperty("browser"));
				
			}
			{
				lblBrowser = new JLabel();
				getContentPane().add(lblBrowser, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				lblBrowser.setText("Navigateur");
			}
			{
				btnBrowser = new JButton();
				getContentPane().add(btnBrowser, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				btnBrowser.setText("...");
				btnBrowser.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnBrowserActionPerformed(evt);
					}
				});
			}
			{
				btnOk = new JButton();
				getContentPane().add(btnOk, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				btnOk.setText("OK");
				btnOk.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						btnOkActionPerformed(evt);
					}
				});
			}
			{
				lblAPIKEY = new JLabel();
				getContentPane().add(lblAPIKEY, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				lblAPIKEY.setText("API KEY");
			}
			{
				txtApiKey = new JTextField();
				getContentPane().add(txtApiKey, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtApiKey.setText(manager.getProperty("api_key"));
			}
			{
				lblSecretKey = new JLabel();
				getContentPane().add(lblSecretKey, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
				lblSecretKey.setText("Secret Key");
				
			}
			{
				txtSecretKey = new JTextField();
				getContentPane().add(txtSecretKey, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
				txtSecretKey.setText(manager.getProperty("secret"));
			}
			{
				this.setTitle("Configuration");
				this.setSize(360, 139);
				this.setLocationRelativeTo(null);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void btnBrowserActionPerformed(ActionEvent evt) {
		JFileChooser f = new JFileChooser(".");
		f.showOpenDialog(null);
		if(f.getSelectedFile()!=null)
			txtBrowser.setText(f.getSelectedFile().getAbsolutePath());
	}
	
	private void btnOkActionPerformed(ActionEvent evt) {
		try {
			manager.setProperty("secret", txtSecretKey.getText());
			manager.setProperty("api_key", txtApiKey.getText());
			manager.setProperty("browser", txtBrowser.getText());
			manager.save();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(),"erreur",JOptionPane.ERROR_MESSAGE);
		} 
		this.dispose();
	}

}
