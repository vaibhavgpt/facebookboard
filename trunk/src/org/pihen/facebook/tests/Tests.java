package org.pihen.facebook.tests;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.pihen.facebook.dao.FacebookDAO;
import org.pihen.facebook.dao.FacebookJaxBDaoImpl;

import com.google.code.facebookapi.schema.StreamData;
import com.google.code.facebookapi.schema.StreamData.Posts;
import com.google.code.facebookapi.schema.StreamPost;
import com.google.code.facebookapi.schema.User;

public class Tests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FacebookDAO fbDAO = new FacebookJaxBDaoImpl();
		/**
		 * 
		 * http://www.facebook.com/authorize.php?api_key=68ba8eff30abde9cc78b442f59606970&ext_perm=status_update,read_stream,publish_stream
		 * 
		 */
		
		
		try {
			
			fbDAO.connect("login","password");
		
			Calendar c = new GregorianCalendar();
			
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.MONTH,1);
			c.set(Calendar.YEAR, 2009);
			
			
			StreamData data = fbDAO.getNews(fbDAO.getLoggedUser(), null, c.getTime(), null);
			Posts p = data.getPosts();
			
			List<StreamPost> list = p.getStreamPost();
			List<User> friends = fbDAO.getFriends(fbDAO.getLoggedUser());
			for(StreamPost sp : list)
			{
				User u = fbDAO.getUserById(sp.getActorId(),true);
				System.out.println(u.getName() + " " + sp.getMessage() + " le " + new Date(sp.getCreatedTime()));
				System.out.println(sp.getAttachment().getIcon()+"\n");
			}
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
