package org.pihen.facebook.ui.photos;

import org.jdesktop.swingx.JXImagePanel;

import com.google.code.facebookapi.schema.Photo;

public class JXThumbnailLabel extends JXImagePanel {

	private Photo photo;

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	
}
