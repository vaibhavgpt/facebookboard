package org.pihen.facebook.main;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;


public class Setup extends JDialog{
	private JPanel panneauConteneurEndorsedDir;
	private JEditorPane editorPane;
	private JScrollPane scrollPane;
	private JLabel lblEndorsed;
	private JTextField txtEndorsedDir;
	private File dirEndorsed;
	private File libFile;
	
	private void initGUI() {
		try {
			
				this.setSize(551, 235);
				this.setTitle("Assistant installation");
				this.setLocationRelativeTo(null);
				this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				this.setResizable(false);
				getContentPane().setLayout( new BorderLayout());
				
					panneauConteneurEndorsedDir = new JPanel();

					scrollPane = new JScrollPane();
					editorPane = new JEditorPane();
					scrollPane.setViewportView(editorPane);
					
					
					getContentPane().add(panneauConteneurEndorsedDir, BorderLayout.NORTH);
					getContentPane().add(scrollPane, BorderLayout.CENTER);
					panneauConteneurEndorsedDir.setPreferredSize(new java.awt.Dimension(549, 35));
					{
						lblEndorsed = new JLabel();
						panneauConteneurEndorsedDir.add(lblEndorsed);
						lblEndorsed.setText("Repertoire endorsed JVM :");
					}
					{
						txtEndorsedDir = new JTextField();
						txtEndorsedDir.setText(System.getProperty("java.endorsed.dirs"));
						panneauConteneurEndorsedDir.add(txtEndorsedDir);
						txtEndorsedDir.setPreferredSize(new java.awt.Dimension(352, 21));
					}
					
					
				

		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Setup()
	{
		initGUI();
		setVisible(true);
		
		
		addText("initialisation du repertoire endorsed========================");
		dirEndorsed = new File(txtEndorsedDir.getText());
		libFile = new File("../lib/jaxb-api-2.1.jar");
		
		addText("Fichier endorsed =" +dirEndorsed.getAbsolutePath()+ " isDirectory : " + dirEndorsed.isDirectory()+ " existe : " + dirEndorsed.exists());
		if(!dirEndorsed.exists())
		{
			addText("Repertoire inexistant il sera cree");
			if(dirEndorsed.mkdir())
				addText("Repertoire cree " + dirEndorsed.exists());
		}
		else
		{
			addText("le repertoire endorsed existe deja");
		}

		addText("Verification de la librairie JAXB==========================");
		addText("Fichier lib JAXB =" +libFile.getAbsolutePath()+ " isFile : " + libFile.isFile()+ " existe : " + libFile.exists());
		addText("===========================================================");
		String filename= dirEndorsed+"/"+libFile.getName();
		addText("creation du fichier " + filename);
		try {
			InputStream in = new FileInputStream(libFile.getAbsolutePath());
			OutputStream out = new FileOutputStream(filename); 
			int len;
			byte[] buf = new byte[1024];
			
			while ((len = in.read(buf)) > 0){
		        out.write(buf, 0, len);
		      }
		      in.close();
		      out.close();			
			addText("creation du fichier : OK");
		} catch (IOException e) {
			addText("ERREUR " + e.getMessage());
		}
		
		
	}
	
	
	
	public static void main(String[] args) {
	
		new Setup();
	}
	
	public void addText(String message)
	{
		editorPane.setText(editorPane.getText()+"\n"+message);
	}
	
}
