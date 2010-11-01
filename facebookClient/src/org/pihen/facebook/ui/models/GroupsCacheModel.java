package org.pihen.facebook.ui.models;

import java.util.List;

import javax.swing.AbstractListModel;

import com.google.code.facebookapi.schema.Group;

public class GroupsCacheModel extends AbstractListModel{


	private static final long serialVersionUID = 1L;

	private List<Group> groups;
	
	public Object getElementAt(int i) {
		return groups.get(i).getName();
	}

	public int getSize() {
		if(groups==null)
			return 0;
		
		return groups.size();
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
