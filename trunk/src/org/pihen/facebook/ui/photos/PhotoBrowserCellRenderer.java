package org.pihen.facebook.ui.photos;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.jdesktop.swingx.JXImagePanel;


public class PhotoBrowserCellRenderer extends DefaultListCellRenderer
{
	private static final long	serialVersionUID	= 20070608L;

	public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus)
	{
		return (JXImagePanel)value;
	}
}
