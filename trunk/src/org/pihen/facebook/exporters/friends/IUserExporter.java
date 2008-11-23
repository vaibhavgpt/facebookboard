package org.pihen.facebook.exporters.friends;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.google.code.facebookapi.schema.User;

public interface IUserExporter {

	public void export(User u,File f) throws IOException;
	public void exports(List<User> list,File f) throws IOException;
	public List<User> restore();
	public String getExtension();
}
