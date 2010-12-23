package org.pihen.facebook.ui;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.pihen.facebook.exporters.TxtExporter;
import org.pihen.facebook.ui.models.FriendsTableCacheModel;
import org.pihen.facebook.util.CacheFileGestionnaire;
import org.pihen.facebook.util.PropertiesFileManager;

import com.google.code.facebookapi.schema.User;

public class FriendsSwingWorker extends SwingWorker<FriendsTableCacheModel, User> {
	
	
	private FacebookSwingWindow window;
	private FriendsTableCacheModel model; 
	
    public FriendsSwingWorker(FacebookSwingWindow window) {
    	this.window=window;
    }

    protected FriendsTableCacheModel doInBackground()throws Exception{
    	window.getLblWaiting().setBusy(true);
    	model = (FriendsTableCacheModel)window.getTableFriends().getModel();
    	window.getLblWaiting().setText("Chargement des " + window.getService().getNbFriends(window.getService().getLoggedUser()) + " amis en cours");
    	model.setFriends(window.getService().getFriends(window.getService().getLoggedUser()));
    	return model;
    }
    
    protected void done() {
    	window.getLblWaiting().setBusy(false);
    	try {
			window.getLblWaiting().setText(window.getService().getNbFriends(window.getService().getLoggedUser()) + " amis charg�s");
			window.getTableFriends().updateUI(); //update the JXTable
			//window.getTableFriends().getFilters().flush(); //initialize de searchEngine
			window.getChatwindow().getTimer().start(); //start the get online friends timer
			new TxtExporter().exports(model.getFriends(),new File(new CacheFileGestionnaire().getCacheFile().getAbsolutePath()+"/friendsList-"+new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date())+".txt"));
			
    	} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	protected void process(List<User> chunks) {
		((FriendsTableCacheModel)window.getTableFriends().getModel()).setFriends(chunks);
	}
}
