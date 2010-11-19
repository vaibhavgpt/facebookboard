package org.pihen.facebook.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.pihen.facebook.exporters.IUserExporter;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Group;
import com.google.code.facebookapi.schema.Notifications;
import com.google.code.facebookapi.schema.Page;
import com.google.code.facebookapi.schema.Photo;
import com.google.code.facebookapi.schema.PhotoTag;
import com.google.code.facebookapi.schema.User;

public interface IFacebookService {

	public abstract User getLoggedUser() throws FacebookException, IOException;

	public boolean connection(String mail,String pass) throws FacebookException, IOException;

	public abstract User getUserById(long id,boolean fromCache) throws FacebookException, IOException;

	public abstract int getNbFriends(User u) throws FacebookException, IOException;

	public abstract List<User> getFriends(User u) throws FacebookException, IOException;

	public abstract IFacebookRestClient getClient() throws FacebookException, IOException;

	public abstract List<Album> getAlbums(User u) throws FacebookException, IOException;

	public abstract List<Photo> getPhotos(Album album) throws FacebookException, IOException;

	public abstract List<Group> getGroups(User u) throws FacebookException, IOException;
	
	public abstract List<Page> getPages(User u) throws FacebookException, IOException;
	
	public abstract List<PhotoTag> getTags(Photo p) throws FacebookException, IOException;

	public abstract List<User> getOnlineFriends() throws FacebookException, IOException;
	
	public abstract boolean sendMessage(User u,String message) throws FacebookException, IOException, JSONException;

	public abstract boolean connectionByBrowser() throws FacebookException, IOException, InterruptedException;
	
	public abstract List<User> compare(List<User> listFriends,List<User> cache);
	
	public abstract List<User> getCachedUser(Date d,IUserExporter exporter) throws FileNotFoundException, IOException, ClassNotFoundException ;
	
	public abstract boolean changeStatut(String st) throws FacebookException;
}