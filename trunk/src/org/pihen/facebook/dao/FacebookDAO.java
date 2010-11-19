package org.pihen.facebook.dao;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Event;
import com.google.code.facebookapi.schema.Group;
import com.google.code.facebookapi.schema.Notifications;
import com.google.code.facebookapi.schema.Page;
import com.google.code.facebookapi.schema.Photo;
import com.google.code.facebookapi.schema.PhotoTag;
import com.google.code.facebookapi.schema.StreamData;
import com.google.code.facebookapi.schema.User;

public interface FacebookDAO {

	public User getUserById(long id,boolean fromCache) throws FacebookException, IOException;
	public boolean connect(String mail,String pass) throws HttpException, IOException, FacebookException;
	public boolean connectByBrowser() throws HttpException, IOException, FacebookException, InterruptedException;
	public User getLoggedUser() throws FacebookException, IOException;
	public List<User> getFriends(User u) throws FacebookException, IOException;
	public int getNbFriends(User u) throws FacebookException, IOException;
	public IFacebookRestClient<?> getClient();
	public List<Album> getAlbums(User u) throws FacebookException, IOException;
	public List<Photo> getPhotos(Album a) throws FacebookException, IOException;
	public List<Group> getGroups(User u) throws FacebookException, IOException;
	public List<Page> getPages(User u) throws FacebookException, IOException;
	public List<Event> getEvenements(User u) throws FacebookException,IOException;
	public List<PhotoTag> getTags(Photo p) throws FacebookException,IOException;
	public List<User> getOnlineFriends();
	public boolean changeStatut(String st) throws FacebookException;
	public boolean sendMessage(User u,String message) throws JSONException, FacebookException, IOException;
	public StreamData getNews(User u,List<Long> source_ids,Date start_time,Date end_time) throws FacebookException, IOException;
}
