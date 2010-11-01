package org.pihen.facebook.ui.models;

import java.util.List;

import javax.swing.AbstractListModel;

import com.google.code.facebookapi.schema.User;

public class FriendsListCacheModel extends AbstractListModel{


	private static final long serialVersionUID = 1L;

	private List<User> users;
	
	public Object getElementAt(int i) {
		return users.get(i).getName();
	}

	public int getSize() {
		if(users==null)
			return 0;
		
		return users.size();
	}

	public List<User> getUsers() {
		return users;
	}
	
	public User getUserAt(int i)
	{
		return users.get(i);
	}
	
	

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
