package com.bfrc.dataaccess.svc.contact;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import app.bsro.model.contact.ContactUs;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.dao.generic.CustomerContactUsEmailLogDAO;
import com.bfrc.dataaccess.dao.generic.FeedbackDAO;
import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.customer.CustomerContactUsEmailLog;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.svc.webdb.website.WebsiteService;
import com.bfrc.dataaccess.util.Sites.SiteTypes;
import com.bfrc.dataaccess.util.StringUtils;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.pojo.contact.Feedback;
import com.bsro.databean.vehicle.TireVehicle;

@Service
public class ContactUsServiceImpl implements ContactUsService {

	private Logger logger = Logger.getLogger(getClass().getName());
	
	@Autowired
	private FeedbackDAO feedbackDao;
	@Autowired
	private CustomerContactUsEmailLogDAO customerContactUsEmailLogDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private StoreDAO storeDao;
	@Autowired
	private WebsiteService websiteService;
	@Resource(name="emailSender")
	private JavaMailSender mailSender;
	
	@Autowired
	private HibernateUtil hibernateUtil;
	
	@Autowired
	private ContactDAO contactDAO;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	public List<ValueTextBean> listAllSubjects(String siteType) {
		List<ValueTextBean> loResult = new ArrayList<ValueTextBean>();
		Collection<Object[]> data = feedbackDao.findContactUsSubjects(siteType);
		for(Object[] o : data) {
			loResult.add(new ValueTextBean((String)o[0], (String)o[1]));
		}
		
		return loResult;
	}
	
	public void sendContactUs(ContactUs contactUs, String siteType, Long storeId, Long acesVehicleId, String userAgent) {
		Integer feedbackId = contactUs.getFeedbackId();
		List<String> to = emailService.getToEmail(siteType, "CONTACT US", feedbackId);
		List<String> cc = emailService.getCcEmail(siteType, "CONTACT US", feedbackId);
		List<String> bcc = emailService.getBccEmail(siteType, "CONTACT US", feedbackId);
		
		String from = "";
		if(isCustomerServiceFeedback(siteType, feedbackId) || isComplimentFeedback(siteType, feedbackId)) {
			from = emailService.getDoNotReply(siteType);
		} else {
			from = contactUs.getFirstName() + " " + contactUs.getLastName() + "<" + contactUs.getEmail() + ">";
		}
		
		String storeBody = "";
		String storeSuffix = "";
		Store store = null;
		TireVehicle tireVehicle = null;
		if (storeId!=null && storeId.longValue()!=0) {
			store = storeDao.get(storeId.longValue());
			storeBody = "Store Visited:\r\n#"
				+ store.getStoreNumber().longValue() + "\r\n" + store.getStoreName() + "\r\n" + store.getAddress() + "\r\n" + store.getCity() + ", " + store.getState() + "  " + store.getZip() + "\r\n";
			if(store.getStoreName().indexOf("Licensee") > -1)
				storeSuffix = " Licensee";
		}
		String subject = siteType + storeSuffix + " Contact Feedback - " + contactUs.getFirstName() + " " + contactUs.getLastName();
		if(isComplimentFeedback(siteType, feedbackId)){
			subject = "Compliment - "+subject;
		}
		
		if (isQuestionAboutTireFeedback(siteType, feedbackId)) {
			if (!com.bfrc.framework.util.StringUtils.isNullOrEmpty(acesVehicleId)) {
				tireVehicle = tireVehicleService.fetchVehicleByAcesVehicleId(acesVehicleId);
			}
		}
		Long saveEmailId = saveEmailLog(contactUs, siteType, store, tireVehicle, feedbackId, to, cc, bcc, from, userAgent);
		String body = generateBody(contactUs, saveEmailId, siteType, feedbackId, storeBody, tireVehicle, userAgent);
		
		sendEmail(to, cc, bcc, from, body);
	}
	
	private String generateBody(ContactUs contactUs, Long referenceNumber, String siteType, Integer feedbackId, String storeBody, TireVehicle tireVehicle, String userAgent) {
		java.text.DecimalFormat df8 = new java.text.DecimalFormat("00000000");
		StringBuffer body = new StringBuffer();
		body.append("The individual below has entered feedback online.\r\n\r\n");
		if (referenceNumber.longValue() != -1){
			body.append("Reference # = " + df8.format(referenceNumber) + "\r\n");
		}
		body.append("Nature of Inquiry = " + getNatureString(siteType, feedbackId) + "\r\n");
		body.append("First Name = " + contactUs.getFirstName() + "\r\n");
		body.append("Last Name = " + contactUs.getLastName() + "\r\n");
		String address = contactUs.getAddress();
		if(address != null && !address.equals(""))
			body.append(address + "\r\n");
		String address2 = contactUs.getAddress2();
		if(address2 != null && !address2.equals(""))
			body.append(address2 + "\r\n");
		String city = contactUs.getCity();
		if(city != null && !city.equals(""))
			body.append(city);
		String state = contactUs.getState();
		if(state != null && !state.equals(""))
			body.append(", " + state + "  ");
		String zip = contactUs.getZip();
		if(zip != null && !zip.equals(""))
			body.append(zip);
		body.append("\r\n");
		if(contactUs.getPhone() != null && !"".equals(contactUs.getPhone())){
			//body.append("Phone Number = " + getDayPhone() + "\r\n");
			body.append("Phone Number = " + contactUs.getPhone() + "\r\n");
		}

		body.append("E-mail Address = " + contactUs.getEmail() + "\r\n");
		body.append(storeBody);
		if(tireVehicle != null && tireVehicle.getSubmodelName() != null)
			body.append("Vehicle = " + tireVehicle.getYear() + " " + tireVehicle.getMakeName() + " " + tireVehicle.getModelName() + " " + tireVehicle.getSubmodelName() + "\r\n");
		body.append("Message = " + contactUs.getMessage() + "\r\n");
		
		if (userAgent != null && !userAgent.isEmpty())
			body.append("\r\nUser Agent: " + userAgent + "\r\n");

		return body.toString();
	}
	
	private Long saveEmailLog(ContactUs contactUs, String siteType, Store store, TireVehicle tireVehicle, Integer feedbackId, List<String> to, List<String> cc, List<String> bcc, String from, String userAgent) {
		Long loResult = null;
		//Log contact us information to the database
		CustomerContactUsEmailLog contactLog = new CustomerContactUsEmailLog();
		try {

			contactLog.setComments(contactUs.getMessage());

			contactLog.setEmailTo(StringUtils.listToString(to, ";"));
			contactLog.setEmailCc(StringUtils.listToString(cc, ";"));
			contactLog.setEmailBcc(StringUtils.listToString(bcc, ";"));
			contactLog.setEmailFrom(from);

			contactLog.setFirstName(contactUs.getFirstName());
			contactLog.setLastName(contactUs.getLastName());

			contactLog.setEmailAddress(contactUs.getEmail());
			contactLog.setDaytimePhone(contactUs.getPhone());
			contactLog.setEveningPhone(contactUs.getEveningPhone());
			contactLog.setMobilePhone(contactUs.getCellPhone());

			contactLog.setStreetAddress1(contactUs.getAddress());
			contactLog.setStreetAddress2(contactUs.getAddress2());
			contactLog.setCity(contactUs.getCity());
			contactLog.setState(contactUs.getState());
			contactLog.setZipCode(contactUs.getZip());

			contactLog.setFeedbackId(feedbackId);
			contactLog.setSiteId(new Integer(websiteService.getBySiteName(siteType).getSiteId()));
			contactLog.setUserAgentInfo(userAgent);

			if (store != null) {
				contactLog.setStoreNumber(store.getStoreNumber());
				contactLog.setStoreName(store.getStoreName());
				contactLog.setStoreAddress(store.getAddress());
				contactLog.setStoreCity(store.getCity());
				contactLog.setStoreState(store.getState());
				contactLog.setStoreZip(store.getZip());
			}

			if (tireVehicle != null) {
				contactLog.setVehicleMake(tireVehicle.getMakeName());
				contactLog.setVehicleModel(tireVehicle.getModelName());
				contactLog.setVehicleSubmodel(tireVehicle.getSubmodelName());
				contactLog.setVehicleYear(Short.valueOf(tireVehicle.getYear()));
			}
			contactLog.setCreationDate(new Date());

			loResult = customerContactUsEmailLogDao.save(contactLog);
		}
		catch (Exception ex) {
			logger.severe("Error saving customerContactUsEmail to log: "+contactLog.toString()+": "+ex.getMessage());
		}
		
		return loResult;
	}
	
	private void sendEmail(List<String> to, List<String> cc, List<String> bcc,
			String from, String body) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(StringUtils.listToStringArray(to));
			helper.setFrom(from);
			if (cc != null && cc.size() > 0)
				helper.setCc(StringUtils.listToStringArray(cc));
			if (bcc != null && bcc.size() > 0)
				helper.setBcc(StringUtils.listToStringArray(bcc));

			helper.setText(body, true);

			this.mailSender.send(message);
		} catch (Exception ex) {
			// simply log it and go on...
			logger.severe("Error sending email: " + ex.getMessage());
		}
	}
	
	private boolean isCustomerServiceFeedback(String siteType, Integer feedbackId) {
		if (feedbackId == 1 && SiteTypes.FCAC.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 21 && SiteTypes.ET.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 44 && SiteTypes.FCFC.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 71 && SiteTypes.TP.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 121 && SiteTypes.HT.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 141 && SiteTypes.ETIRE.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 161 && SiteTypes.FIVESTAR.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		}
		
		return false;
	}
	private boolean isComplimentFeedback(String siteType, Integer feedbackId) {
		if (feedbackId == 19
				&& SiteTypes.FCAC.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 39
				&& SiteTypes.ET.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 89
				&& SiteTypes.TP.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 139
				&& SiteTypes.HT.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 149
				&& SiteTypes.ETIRE.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 169
				&& SiteTypes.FIVESTAR.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		}
		return false;
	}
	
	private boolean isQuestionAboutTireFeedback(String siteType, Integer feedbackId) {
		if (feedbackId == 3 && SiteTypes.FCAC.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 23 && SiteTypes.ET.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 47 && SiteTypes.FCFC.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 73 && SiteTypes.TP.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 123 && SiteTypes.HT.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 143 && SiteTypes.ETIRE.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		} else if (feedbackId == 163 && SiteTypes.FIVESTAR.getSiteType().equalsIgnoreCase(siteType)) {
			return true;
		}
		
		return false;
	}
	
	private String getNatureString(String siteType, Integer natureId) {
		List<ValueTextBean> values = listAllSubjects(siteType);
		if(values == null) return "";
		for(ValueTextBean b : values) {
			if(b.getValue().equals(natureId.toString()))
				return b.getText();
		}
		return "";
	}
	
	public Long createEmailSignup(EmailSignup signup)
	{
		try
		{
			if(signup != null && !signup.equals(""))
    		{
				hibernateUtil.save(signup);
    			return  new Long(signup.getSignupId());
    		}else
    		{
    		return null;
    		}
			
		}catch(Exception e)
		{
			return null;
		}
	}
	
	public EmailSignup getEmailSignup(Long signUpId)
	{
		EmailSignup emailSignup = null;
		try
		{
			String hql = "from com.bfrc.dataaccess.model.email.EmailSignup email where email.signupId=:signUpId";
			Query query = hibernateUtil.getSessionFactory().getCurrentSession().createQuery(hql);
			query.setLong("signUpId", signUpId);
			query.setMaxResults(1);
			emailSignup = (EmailSignup) query.uniqueResult();
			
			
		}catch(Exception e)
		{
			System.out.println(" Exception Occured in  getEmailSignup :"+e.getMessage());
		}
		return emailSignup;
	}

	@SuppressWarnings("rawtypes")
	public List listAllCategories(String siteName) {
		locator.getConfig().setSiteName(siteName);
		return contactDAO.getMainFeedbacks();
	}
	
	@SuppressWarnings("unchecked")
	public List<Feedback> listEnquiries(String siteName, Integer categoryId) {
		locator.getConfig().setSiteName(siteName);
		List<Feedback> feedbacks = contactDAO.getFeedbacksByMain(categoryId);
		
		return feedbacks;
	}
	
}
