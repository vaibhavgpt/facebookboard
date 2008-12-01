package org.pihen.facebook.ui.photos;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import org.jdesktop.swingx.JXImagePanel;


public class PhotoBrowserCellRenderer extends DefaultListCellRenderer
{

	public Component getListCellRendererComponent(JList list, Object value,int index, boolean isSelected, boolean cellHasFocus)
	{
		return (JXImagePanel)value;
	}
}
