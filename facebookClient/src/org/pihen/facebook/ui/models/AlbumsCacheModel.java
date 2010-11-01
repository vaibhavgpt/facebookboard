package org.pihen.facebook.ui.models;

import java.util.List;

import javax.swing.AbstractListModel;

import com.google.code.facebookapi.schema.Album;

public class AlbumsCacheModel extends AbstractListModel{


	private static final long serialVersionUID = 1L;

	private List<Album> albums;
	
	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

	public Object getElementAt(int i) {
		return albums.get(i).getName();
	}

	public int getSize() {
		return albums.size();
	}

}
