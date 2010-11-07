package org.pihen.facebook.main;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.pihen.facebook.ui.FacebookSwingWindow;

public class Launcher {
 
  public static void main(String[] args) throws Exception {
	  	  SwingUtilities.invokeLater(new Runnable(){
		  
		  public void run(){
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					FacebookSwingWindow.getInstance();
				} catch (Exception e) {
					e.printStackTrace();
				} 
		  }
		  });
  }
}
