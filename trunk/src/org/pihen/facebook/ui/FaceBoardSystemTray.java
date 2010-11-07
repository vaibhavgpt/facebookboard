package org.pihen.facebook.ui;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class FaceBoardSystemTray {

    TrayIcon trayIcon;

    public FaceBoardSystemTray() {

        // Si le systeme d'exploitation supporte
        // les icones systemes
        if (SystemTray.isSupported()) {
        	 try {
        	// On creer la "SystemTray"
            SystemTray tray = SystemTray.getSystemTray();

            // On lui ajoute l'image qui correspond a l'icone
            
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("img/677166248.png"));
            trayIcon = new TrayIcon(image, "FaceBoard", null);
            
            trayIcon.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if(FacebookSwingWindow.getInstance().isVisible())
                    	FacebookSwingWindow.getInstance().setVisible(false);
                    else
                    	FacebookSwingWindow.getInstance().setVisible(true);
                }
            });


            trayIcon.setImageAutoSize(true);
          
                tray.add(trayIcon);
                displayMessage("Cliquer ici pour afficher / masquer faceboard");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void displayMessage(String message)
    {
    	 if (SystemTray.isSupported()) {
    		 trayIcon.displayMessage("FaceBoard",message,TrayIcon.MessageType.INFO);
    	 }
    }
}