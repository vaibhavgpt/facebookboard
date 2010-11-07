package org.pihen.facebook.ui.models;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import org.pihen.facebook.ui.photos.JXThumbnailLabel;

public class JXImageLabelCacheModel extends AbstractListModel{


	private static final long serialVersionUID = 1L;

	private List<JXThumbnailLabel> photos;
	
	public JXImageLabelCacheModel()
	{
		photos=new ArrayList<JXThumbnailLabel>();
	}

	public Object getElementAt(int i) {
		return photos.get(i);
	}

	public List<JXThumbnailLabel> getPhotos() {
		return photos;
	}

	public void setPhotos(List<JXThumbnailLabel> photos) {
		this.photos = photos;
	}

	public int getSize() {
		return photos.size();
	}

}
