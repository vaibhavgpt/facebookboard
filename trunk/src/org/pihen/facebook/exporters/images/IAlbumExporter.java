package org.pihen.facebook.exporters.images;

import java.io.File;

import com.google.code.facebookapi.schema.Album;
import com.google.code.facebookapi.schema.Photo;

public interface IAlbumExporter {

	public void exporter(Album a,File dir);
	public void exporter(Photo p,File f);
}
