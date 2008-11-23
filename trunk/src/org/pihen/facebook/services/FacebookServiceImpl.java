package org.pihen.facebook.services;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;
import org.pihen.facebook.dao.FacebookDAO;
import org.pihen.facebook.dao.FacebookJaxBDaoImpl;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Group;
import com.google.code.facebookapi.schema.Notifications;
import com.google.code.facebookapi.schema.Page;
import com.google.code.facebookapi.schema.Photo;
import com.google.code.facebookapi.schema.PhotoTag;
import com.google.code.facebookapi.schema.User;

public class FacebookServiceImpl implements IFacebookService {

	private FacebookDAO dao = new FacebookJaxBDaoImpl();
	
	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getLoggedUser()
	 */
	public User getLoggedUser() throws FacebookException, IOException
	{
			return dao.getLoggedUser();
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#connection()
	 */
	public boolean connection(String mail,String password) throws FacebookException, IOException
	{
		return dao.connect(mail,password);
	}
	
	
	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getUserById(long)
	 */
	public User getUserById(long id)  throws FacebookException, IOException{
			return dao.getUserById(id);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getNbFriends(com.google.code.facebookapi.schema.User)
	 */
	public int getNbFriends(User u) throws FacebookException, IOException
	{
			return dao.getNbFriends(u);
	}
	
	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getFriends(com.google.code.facebookapi.schema.User)
	 */
	public List<User> getFriends(User u) throws FacebookException, IOException {
			return dao.getFriends(u);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getNotifications()
	 */
	public Notifications getNotifications()  throws FacebookException, IOException
	{
			return dao.getNotifications();
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getClient()
	 */
	public IFacebookRestClient getClient() throws FacebookException, IOException {
		return dao.getClient();
	}
	
	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getAlbums(com.google.code.facebookapi.schema.User)
	 */
	public List<Album> getAlbums(User u) throws FacebookException, IOException
	{
				return dao.getAlbums(u);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getPhotos(com.google.code.facebookapi.schema.Album)
	 */
	public List<Photo> getPhotos(Album album) throws FacebookException, IOException {
			return dao.getPhotos(album);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getGroups(com.google.code.facebookapi.schema.User)
	 */
	public List<Group> getGroups(User u)  throws FacebookException, IOException{
			return dao.getGroups(u);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getPages(com.google.code.facebookapi.schema.User)
	 */
	public List<Page> getPages(User u) throws FacebookException, IOException{
			return dao.getPages(u);
	}

	/* (non-Javadoc)
	 * @see org.pihen.facebook.services.IFacebookService#getTags(com.google.code.facebookapi.schema.Photo)
	 */
	public List<PhotoTag> getTags(Photo p) throws FacebookException, IOException {
			return dao.getTags(p);
	}

	@Override
	public List<User> getOnlineFriends() throws FacebookException, IOException {
		return dao.getOnlineFriends();
	}

	@Override
	public boolean sendMessage(User u, String message) throws FacebookException, IOException, JSONException{
			return dao.sendMessage(u, message);
	}

	@Override
	public boolean connectionByBrowser()  throws FacebookException, IOException, InterruptedException{
		return dao.connectByBrowser();
		
	}
	
	
}
