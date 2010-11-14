package org.pihen.facebook.tests;

import java.util.List;

import javax.swing.JOptionPane;

import org.pihen.facebook.dao.FacebookDAO;
import org.pihen.facebook.dao.FacebookJaxBDaoImpl;

import com.google.code.facebookapi.schema.StreamData;
import com.google.code.facebookapi.schema.StreamPost;
import com.google.code.facebookapi.schema.User;
import com.google.code.facebookapi.schema.StreamData.Posts;

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
			fbDAO.connect("email@email.com","mypassword");
		
			StreamData data = fbDAO.getNews(fbDAO.getLoggedUser().getUid(), null, null, null);
			Posts p = data.getPosts();
			
			List<StreamPost> list = p.getStreamPost();
			
			for(StreamPost sp : list)
			{
				User u = fbDAO.getUserById(sp.getActorId());
				System.out.println(u.getName() + " " + sp.getMessage());
			}
			
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
