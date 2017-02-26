package com.bfrc.dataaccess.svc.webdb.promotions;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.svc.webdb.DisclaimerService;

@Service
public class DisclaimerServiceImpl implements DisclaimerService {
	
	@Autowired
	private HibernateUtil hibernateUtil;

	public String getDisclaimerDescription(String website, String landingPageId) {
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("PromotionDisclaimer.getDisclaimerMessage");
		query.setString("website", website);
		query.setString("landingPageId", landingPageId);
		
		@SuppressWarnings("unchecked")
		List<String> messages = query.list();
		if (messages != null && messages.size() > 0) {
			return messages.get(0);
		}
		return null;
	}

}
