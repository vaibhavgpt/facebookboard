package org.pihen.facebook.exporters.friends;

import java.io.File;
import java.util.List;

import com.google.code.facebookapi.schema.User;

public interface IUserExporter {

	public void export(User u,File f);
	public void exports(List<User> list,File f);
	public List<User> restore();
}
