package com.bfrc.dataaccess.svc.webdb.email;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.dao.generic.EmailSignupDAO;
import com.bfrc.dataaccess.exception.InvalidEmailException;
import com.bfrc.dataaccess.model.contact.WebSite;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.svc.webdb.website.WebsiteService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private EmailSignupDAO emailSignupDAO;
	@Autowired
	private WebsiteService websiteService;
	@Autowired
	private HibernateUtil hibernateUtil;	
	
	public void subscribe(EmailSignup signup) throws InvalidEmailException {
		signup.setCreatedDate(new Date());
		try {
			emailSignupDAO.save(signup);
		}catch(Exception E) {
			throw new InvalidEmailException(E);
		}
	}
	
	
	public List<String> getToEmail(String siteName, String feedbackType,
			Integer feedbackId) {
		return getToEmail(siteName, feedbackType, feedbackId, 0L, null);
	}
	
	public List<String> getToEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd) {

		return getEmailAddresses(siteName, feedbackType, feedbackId, storeNumber, StringUtils.trimToEmpty(stateCd), "TO");
	}	
	
	public List<String> getCcEmail(String siteName, String feedbackType,
			Integer feedbackId) {
		return getCcEmail(siteName, feedbackType, feedbackId, 0L, null);
	}
	
	public List<String> getCcEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd) {

		return getEmailAddresses(siteName, feedbackType, feedbackId, storeNumber, stateCd, "CC");
	}
	
	public List<String> getBccEmail(String siteName, String feedbackType,
			Integer feedbackId) {
		return getBccEmail(siteName, feedbackType, feedbackId, 0L, null);
	}
	
	public List<String> getBccEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd) {

		return getEmailAddresses(siteName, feedbackType, feedbackId, storeNumber, stateCd, "BCC");
	}
	
	public String getDoNotReply(String siteName) {

		WebSite site = websiteService.getBySiteName(siteName);
		if(site != null) {
			String from = "DO-NOT-REPLY<" + site.getWebmaster() + ">";
			return from;
		}
		return "";
	}
	
	private List<String> getEmailAddresses(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd, String contactType) {
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("getEmailAddressesByFeedbackSiteContact");
		query.setString("siteName", siteName);
		query.setString("feedbackType", feedbackType);
		query.setInteger("feedbackId", feedbackId);
		query.setString("contactType", contactType);
		query.setLong("storeNumber", storeNumber);
		query.setString("stateCd", stateCd);
		
		List<String> emails = query.list();
		return emails;
	}
}
