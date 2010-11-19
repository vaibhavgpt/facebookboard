package org.pihen.facebook.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.pihen.facebook.util.PropertiesFileManager;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookJaxbRestClient;
import com.google.code.facebookapi.FacebookXmlRestClient;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.ProfileField;
import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Event;
import com.google.code.facebookapi.schema.EventsGetResponse;
import com.google.code.facebookapi.schema.FriendsGetResponse;
import com.google.code.facebookapi.schema.Group;
import com.google.code.facebookapi.schema.GroupsGetResponse;
import com.google.code.facebookapi.schema.Page;
import com.google.code.facebookapi.schema.PagesGetInfoResponse;
import com.google.code.facebookapi.schema.Photo;
import com.google.code.facebookapi.schema.PhotoTag;
import com.google.code.facebookapi.schema.PhotosGetAlbumsResponse;
import com.google.code.facebookapi.schema.PhotosGetResponse;
import com.google.code.facebookapi.schema.PhotosGetTagsResponse;
import com.google.code.facebookapi.schema.StreamData;
import com.google.code.facebookapi.schema.User;
import com.google.code.facebookapi.schema.UsersGetInfoResponse;

 
public class FacebookJaxBDaoImpl implements FacebookDAO{

	private IFacebookRestClient client;
	private FacebookXmlRestClient xmlClient;
	private User loggedUser=null;
	private List<User> friendsCache=null;
	public static EnumSet<ProfileField> fields =EnumSet.allOf(ProfileField.class);
	private static Logger logger = Logger.getLogger(FacebookJaxBDaoImpl.class);
	private PropertiesFileManager propertiesManager ;
	private boolean isConnected=false;
	private String session;
	
	public boolean connect(String login,String password) throws HttpException, IOException, FacebookException
	{
		propertiesManager = new PropertiesFileManager();	
		logger.debug("Connexion en cours");
		client =new FacebookJaxbRestClient(propertiesManager.getProperty("api_key"), propertiesManager.getProperty("secret"));
		String token = client.auth_createToken();
		logger.debug("token=" + token);

        HttpClient http = new HttpClient();
        		   http.setParams(new HttpClientParams());
        		   http.setState(new HttpState());

        GetMethod get = new GetMethod(propertiesManager.getProperty("login_url")+"?api_key=" + propertiesManager.getProperty("api_key")+ "&v=1.0&auth_token=" + token);

        http.executeMethod(get);

        PostMethod post = new PostMethod(propertiesManager.getProperty("login_url"));
	                post.addParameter(new NameValuePair("api_key", propertiesManager.getProperty("api_key")));
	                post.addParameter(new NameValuePair("v", "1.0"));
	                post.addParameter(new NameValuePair("auth_token", token));
	                post.addParameter(new NameValuePair("email",login));
	                post.addParameter(new NameValuePair("pass", password));

        http.executeMethod(post);
        
        // fetch session key
        session = client.auth_getSession(token);
	
        //Client pour le chat
        xmlClient= new FacebookXmlRestClient(propertiesManager.getProperty("api_key"),propertiesManager.getProperty("secret"),session);
		
        logger.debug("Session key is " + session);
        
        isConnected= true;
        logger.debug("Connexion en cours : " + isConnected);
        
		return isConnected; 
	}
	
	public boolean connectByBrowser() throws HttpException, IOException, FacebookException, InterruptedException{
		String auth ;
		logger.debug("Connexion a faceboook");
		if(isConnected==false)
		{
			logger.debug("Pas encore connecte, connexion en cours");
			
				propertiesManager = new PropertiesFileManager();
				logger.debug("initialisation du client");
				client =new FacebookJaxbRestClient(propertiesManager.getProperty("api_key"), propertiesManager.getProperty("secret"));
			    auth = client.auth_createToken();
			    logger.debug("Token OK " + auth);
			    logger.debug("Lancement du browser");
			    Process p = Runtime.getRuntime().exec(propertiesManager.getProperty("browser") + " " + propertiesManager.getProperty("login_url") + "?api_key=" + propertiesManager.getProperty("api_key") + "&auth_token=" + auth);
			    p.waitFor(); 
			    logger.debug("Browser ferme");			    		    
			    session = client.auth_getSession(auth);
			    
			    //initialisation du client pour le chat'
			    xmlClient= new FacebookXmlRestClient(propertiesManager.getProperty("api_key"),propertiesManager.getProperty("secret"),session);
			    logger.debug("Initialisation du client terminé secret=" + propertiesManager.getProperty("secret") + " session=" + session);
			    isConnected = true;
		}
		return isConnected;
		
	}
	
	public IFacebookRestClient getClient() {
		return client;
	}

	public User getLoggedUser() throws FacebookException, IOException {
		
		if(loggedUser != null)
			return loggedUser;
		
		long id = client.users_getLoggedInUser();
		Collection<Long> users = new ArrayList<Long>();
		users.add(id);
		client.users_getInfo(users, fields);
		UsersGetInfoResponse u =(UsersGetInfoResponse)client.users_getInfo(users, fields);
		loggedUser = u.getUser().get(0);
		return loggedUser;
	}

	public List<User> getFriends(User u) throws FacebookException, IOException {
		
		if(friendsCache==null)
		{
			logger.info("Recuperation de la liste d'amis pour " + u.getName() );
			FriendsGetResponse friendsResp = (FriendsGetResponse)client.friends_get(u.getUid());
			client.users_getInfo(friendsResp.getUid(),fields);
			UsersGetInfoResponse uresp =(UsersGetInfoResponse)client.users_getInfo(friendsResp.getUid(), fields);
			friendsCache= uresp.getUser();	
		}
		return friendsCache;
			
		
	}

	public int getNbFriends(User u) throws FacebookException, IOException {
		if(friendsCache==null)
		{
			FriendsGetResponse friendsResp = (FriendsGetResponse)client.friends_get(u.getUid());
			return friendsResp.getUid().size();
		}
		else
		{
			return friendsCache.size();
		}
	}

	public User getUserById(long id, boolean fromCache) throws FacebookException, IOException {
	
		if(fromCache)
		{
			if(friendsCache ==null)
				throw new IOException("Le cache est vide");
			
			for (User u: friendsCache)
			{
				if(u.getUid()==id)
					return u;
			}
		}
		else
		{
			Collection<Long> users = new ArrayList<Long>();
			users.add(id);
			client.users_getInfo(users, fields);
			UsersGetInfoResponse u =(UsersGetInfoResponse)client.users_getInfo(users, fields);
			return u.getUser().get(0);
		}
		return null;
	}
	
	public List<Album> getAlbums(User u) throws FacebookException, IOException {
		logger.info("Recuperation des albums de" +u.getName() );
		PhotosGetAlbumsResponse alb = (PhotosGetAlbumsResponse)client.photos_getAlbums(u.getUid());
		return alb.getAlbum();
	}

	public List<Photo> getPhotos(Album a) throws FacebookException, IOException {
		logger.info("Recuperation de l'album "+a.getName() );
		PhotosGetResponse photos = (PhotosGetResponse)client.photos_get(null,a.getAid());
		return photos.getPhoto();
	}

	public List<Group> getGroups(User u) throws FacebookException, IOException {
		logger.info("recuperation des groups pour l'utilisateur " + u.getName());
		GroupsGetResponse groups = (GroupsGetResponse)client.groups_get(u.getUid(), null);
		logger.info("recuperation " + groups.getGroup().size() + " groups pour l'utilisateur " + u.getName());
		return groups.getGroup();
	}
	
	public List<Page> getPages(User u) throws FacebookException, IOException
	{
		logger.info("recuperation des pages pour l'utilisateur " + u.getName());
		PagesGetInfoResponse pagesResp = (PagesGetInfoResponse)client.pages_getInfo(u.getUid(), null);
		logger.info("recuperation " + pagesResp.getPage().size() +" pages pour l'utilisateur " + u.getName());
		return pagesResp.getPage();
	}
	
	public List<Event> getEvenements(User u) throws FacebookException,IOException
	{
		logger.info("recuperation des evenements de l'utilisateur " + u.getName());
		EventsGetResponse eventResp = (EventsGetResponse)client.events_get(u.getUid(), null,null,null);
		logger.info("recuperation " + eventResp.getEvent().size() +" evenements pour l'utilisateur " + u.getName());
		return eventResp.getEvent();
	}
	
	public List<PhotoTag> getTags(Photo p) throws FacebookException,IOException
	{
		logger.info("recuperation des tags de la photo " + p.getPid());
		Collection<Long> col = new ArrayList<Long>();
		col.add(Long.parseLong(p.getPid()));
		PhotosGetTagsResponse tagsResp = (PhotosGetTagsResponse)client.photos_getTags(col);
		return tagsResp.getPhotoTag();
	}

	public List<User> getOnlineFriends()
	{
		try {
			  Document d =(Document)xmlClient.fql_query("SELECT uid FROM user WHERE uid IN (SELECT uid2 FROM friend WHERE uid1="+ loggedUser.getUid()+") AND 'active' IN online_presence");
			  logger.debug("recuperation des friends online");
			  
			  NodeList nlists = d.getElementsByTagName("uid");
			  Collection<Long> col = new ArrayList<Long>();
			  for(int i = 0;i<nlists.getLength();i++)
			  {
				  col.add(Long.parseLong(nlists.item(i).getChildNodes().item(0).getNodeValue()));
			  }
			  client.users_getInfo(col, fields);
			  
			  return ((UsersGetInfoResponse)client.users_getInfo(col, fields)).getUser();
			  
		} catch (Exception e) {
			logger.error(e);
		}
		return null;
	}
	
	public boolean changeStatut(String st) throws FacebookException
	{
		return client.users_setStatus(st);
	}
	
	public StreamData getNews(User idUser,List<Long> source_ids,Date start_time,Date end_time) throws FacebookException, IOException
	{
		return (StreamData)client.stream_get(idUser.getUid(), source_ids, start_time, end_time, null, null,null);		
	}
	
	
	public boolean sendMessage(User u,String message) throws JSONException, FacebookException, IOException
	{
		logger.info("envoi du message <  " + message + "  > a " + u.getName());
		JSONObject obj = new JSONObject();
				   obj.put("message", message);
		return xmlClient.liveMessage_send(u.getUid(), "test", obj);
	}
		
	
	
	
}

	

