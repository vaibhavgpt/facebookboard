package org.pihen.facebook.ui;

import java.io.IOException;
import java.util.List;

import javax.swing.SwingWorker;

import org.pihen.facebook.ui.models.FriendsTableCacheModel;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.schema.User;

public class FriendsSwingWorker extends SwingWorker<FriendsTableCacheModel, User> {
	
	
	private FacebookSwingWindow window;
	
    public FriendsSwingWorker(FacebookSwingWindow window) {
    	this.window=window;
    }

    protected FriendsTableCacheModel doInBackground()throws Exception{
    	window.getLblWaiting().setBusy(true);
    	FriendsTableCacheModel model = (FriendsTableCacheModel)window.getTableFriends().getModel();
    	window.getLblWaiting().setText("Chargement des " + window.getService().getNbFriends(window.getService().getLoggedUser()) + " amis en cours");
    	model.setFriends(window.getService().getFriends(window.getService().getLoggedUser()));
    	return model;
    }
    
    protected void done() {
    	window.getLblWaiting().setBusy(false);
    	try {
			window.getLblWaiting().setText(window.getService().getNbFriends(window.getService().getLoggedUser()) + " amis chargés");
		} catch (Exception e) {
			e.printStackTrace();
		} 
    	window.getTableFriends().updateUI();
    	window.getTableFriends().getFilters().flush();
    	
	}

	protected void process(List<User> chunks) {
		((FriendsTableCacheModel)window.getTableFriends().getModel()).setFriends(chunks);
	}
}
