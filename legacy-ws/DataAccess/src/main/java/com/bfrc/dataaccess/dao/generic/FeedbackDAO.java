package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.contact.Feedback;

public interface FeedbackDAO extends IGenericOrmDAO<Feedback, Integer> {

	public Collection<Object[]> findContactUsSubjects(String siteType);
	
}
