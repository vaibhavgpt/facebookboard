package org.pihen.facebook.ui.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.google.code.facebookapi.schema.User;

public class FriendsTableCacheModel extends AbstractTableModel {


	private static final long serialVersionUID = 1L;
	private String[] columnName = {"id", "Nom","Prenom","Statut","Sexe","Date Naissance","Relation","Région","Ville","Pays","derniere mise a jour"};
	
	
	public List<User> friends = new ArrayList<User>();
	
	public User getUserAt(int id)
	{
		return friends.get(id);
	}

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public String getColumnName(int col) {
		return columnName[col];
	}

	public int getColumnCount() {
		return columnName.length;
	}

	public int getRowCount() {
		if(friends==null)
			return 0;
		return friends.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		
		switch (columnIndex) 
		{
			case 0: return rowIndex;
			case 1: return friends.get(rowIndex).getLastName();
			case 2: return friends.get(rowIndex).getFirstName();
			case 3: return (friends.get(rowIndex).getStatus().getValue() != null ) ?friends.get(rowIndex).getStatus().getValue().getMessage():"";
			case 4: return friends.get(rowIndex).getSex().getValue();
			case 5: return friends.get(rowIndex).getBirthday().getValue();
			case 6: return friends.get(rowIndex).getRelationshipStatus().getValue();
			case 7: return (friends.get(rowIndex).getCurrentLocation().getValue() != null ) ? friends.get(rowIndex).getCurrentLocation().getValue().getState():"";
			case 8: return (friends.get(rowIndex).getCurrentLocation().getValue() != null ) ? friends.get(rowIndex).getCurrentLocation().getValue().getCity():"";
			case 9: return (friends.get(rowIndex).getCurrentLocation().getValue() != null ) ? friends.get(rowIndex).getCurrentLocation().getValue().getCountry():"";
			case 10: return (friends.get(rowIndex).getStatus().getValue() != null ) ? formatDate(friends.get(rowIndex).getStatus().getValue().getTime()):"";
		}
		return columnIndex;
	}

	private String formatDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm.s");
		
		
		return format.format(new Date(time));
	}
	
	

}
