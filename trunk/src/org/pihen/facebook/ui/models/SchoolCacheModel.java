package org.pihen.facebook.ui.models;

import javax.swing.AbstractListModel;

import com.google.code.facebookapi.schema.User.EducationHistory;

public class SchoolCacheModel extends AbstractListModel{
	
	private EducationHistory educationHistory;
	
	public Object getElementAt(int i) {
		return educationHistory.getEducationInfo().get(i).getName() + "  " + educationHistory.getEducationInfo().get(i).getYear();
		
	}

	public int getSize() {
		return educationHistory.getEducationInfo().size();
	}

	
	public void setListSchool(EducationHistory educationHistory)
	{
		this.educationHistory=educationHistory;
	}
	
}
