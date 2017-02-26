package com.bfrc.framework.dao.hibernate3;

import java.util.List;

import com.bfrc.framework.dao.BsroCorpUsersDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.storeadmin.BsroCorpUsers;

public class BsroCorpUsersDAOImpl extends HibernateDAOImpl implements BsroCorpUsersDAO {

	
	public BsroCorpUsers getCorpUserByEmail(String email) {
		String hql = "from BsroCorpUsers bcu where bcu.emailAddress=?";
		Object[] args = new Object[] { email };
		List l = getHibernateTemplate().find(hql, args);
		if (l == null || l.size() == 0)
			return null;
		return (BsroCorpUsers) l.get(0);
	}
}
