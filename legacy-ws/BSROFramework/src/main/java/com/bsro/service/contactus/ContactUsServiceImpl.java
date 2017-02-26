package com.bsro.service.contactus;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.bfrc.Config;
import com.bfrc.UserSessionData;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.CustomerDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ContactUsUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.contact.Feedback;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.customer.CustomerContactusemailLog;
import com.bfrc.pojo.store.Store;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.pojo.contactus.ContactUs;
import com.bsro.pojo.form.ContactUsForm;

@Service("contactUsService")
public class ContactUsServiceImpl implements ContactUsService {

	@Autowired
	ContactDAO contactDAO;
	
	@Autowired
	CustomerDAO customerDAO;
	
	@Autowired
	VehicleDAO vechicleDAO;
	
	@Autowired
	StoreDAO storeDAO;
	
	@Autowired
	Config config;

	@Override
	public ContactUs createContactUsBean(UserSessionData userData) {
		
		ContactUs contactUs = new ContactUs();
		
	    contactUs.setFirstName((String)userData.get("contactus.firstName"));
	    contactUs.setLastName((String)userData.get("contactus.lastName"));
	    contactUs.setAddress((String)userData.get("contactus.address"));
	    contactUs.setAddress2((String)userData.get("contactus.address2"));
	    contactUs.setCity((String)userData.get("contactus.city"));
	    contactUs.setState((String)userData.get("contactus.state"));
	    contactUs.setZip((String)userData.get("contactus.zip"));
	    contactUs.setPhone((String)userData.get("contactus.phone"));
	    contactUs.setEmailAddress((String)userData.get("contactus.emailAddress"));
	    contactUs.setMessage((String)userData.get("contactus.message"));
	    contactUs.setYear((String)userData.get("contactus.year"));
	    contactUs.setMake((String)userData.get("contactus.make"));
	    contactUs.setStrStoreNumber(userData.getStoreNumber());

	    if(!ServerUtil.isNullOrEmpty(contactUs.getMake())) {
	    	contactUs.setMakeName(vechicleDAO.getMakeNameByMakeId(contactUs.getMake()));
	    }
	    
	    contactUs.setModel((String)userData.get("contactus.model"));
	    
	    if(!ServerUtil.isNullOrEmpty(contactUs.getModel())) {
	    	contactUs.setModelName(vechicleDAO.getModelNameByModelId(contactUs.getModel()));
	    }
	    
	    contactUs.setSubmodel((String)userData.get("contactus.submodel"));
	    contactUs.setAcesVehicleId((String)userData.get("contactus.acesVehicleId"));
	    contactUs.setNature((String)userData.get("contactus.nature"));
		
		return contactUs;
	}

	@Override
	public Mail createContactUsMail(ContactUs contactUs, WebApplicationContext appContext, MailManager mailManager) {
		Config thisConfig = (Config)Config.locate(appContext, "config");
		Long storeNumber = 0L;
		
	    try{
	        storeNumber = Long.parseLong(contactUs.getStrStoreNumber());
	    }catch(NumberFormatException ex){
	    	
	    }
	    Store store = null;
	    if (storeNumber != 0) 
			store = storeDAO.findStoreById(storeNumber);
		
	    Mail mail = new Mail();
	    StringBuffer invalid = new StringBuffer();
	    
		String[] to = contactDAO.getTo(contactUs.getNature(), storeNumber);
		String[] validTo = {};
		if(to!=null||to.length>0) //{
			validTo = ServerUtil.sanitizeInvalidEmails(to, invalid, "TO");
		mail.setTo(validTo);
		
		String[] cc = contactDAO.getCc(contactUs.getNature(), storeNumber);
		String[] validCc = {};
		if(cc!=null||cc.length>0) //{
			validCc = ServerUtil.sanitizeInvalidEmails(cc, invalid, "CC");
		mail.setCc(validCc);
		
		String[] bcc = contactDAO.getBcc(contactUs.getNature(), storeNumber);
		String[] validBcc = {};
		if(bcc!=null||bcc.length>0) //{
			validBcc = ServerUtil.sanitizeInvalidEmails(bcc, invalid, "BCC");
		mail.setBcc(validBcc);
		
		String from = contactUs.getFirstName() + " " + contactUs.getLastName() + "<" + contactUs.getEmailAddress() + ">";
		//--- 20101231 contact us by Id ---//
		List feedbacks = contactDAO.getAllFeedBacks();
		if (Config.isCustomerServiceFeedback(feedbacks, thisConfig, contactUs.getNature())
		        || Config.isComplimentFeedback(feedbacks, thisConfig, contactUs.getNature())){
			from = contactDAO.getFrom();
		}
		mail.setFrom(from);
		String storeSuffix = "";
		
		if(store != null) {
			if(store.getStoreName().indexOf("Licensee") > -1)
				storeSuffix = " Licensee";
		}
		
		String subject = thisConfig.getSiteFullName() + storeSuffix + " Contact Feedback - " + contactUs.getFirstName() + " " + contactUs.getLastName()+" - Mobile";
		if(Config.isComplimentFeedBack(contactUs.getNature())){
			subject = "Compliment - "+subject;
		}
		mail.setSubject(subject);
		
		Long refNumber = logContactUsInfo(mail, contactUs, store);
		contactUs.setReferenceNumber(refNumber);
		
		StringBuffer body = createMailBody(contactUs, store);
		mail.setBody(body.toString());
		
		if (invalid != null && invalid.length() > 0) {
			ServerUtil.sendErrorEmail(mail, invalid.toString(), mailManager, appContext);
		}
		
		return mail;
	}
	
	private StringBuffer createMailBody(ContactUs contactUs, Store store) {
		java.text.DecimalFormat df8 = new java.text.DecimalFormat("00000000");
		StringBuffer body = new StringBuffer();
		body.append("The individual below has entered feedback on " + contactDAO.getUrl() + " website.\r\n\r\n");
		//--- 20101231 contact us by Id ---//
		if (contactUs.getReferenceNumber().longValue() != -1){
			body.append("Reference # = " + df8.format(contactUs.getReferenceNumber()) + "\r\n");
		}
		body.append("Nature of Inquiry = " + contactUs.getNature() + "\r\n");
		body.append("First Name = " + contactUs.getFirstName() + "\r\n");
		body.append("Last Name = " + contactUs.getLastName() + "\r\n");
		if(!ServerUtil.isNullOrEmpty(contactUs.getAddress())) 
			body.append(contactUs.getAddress() + "\r\n");
		//if(address2 != null && !address2.equals(""))
		//	body.append(address2 + "\r\n");
		if(!ServerUtil.isNullOrEmpty(contactUs.getCity())) 
			body.append(contactUs.getCity());
		if(contactUs.getState() != null && !contactUs.getState().equals(""))
			body.append(", " + contactUs.getState() + "  ");
		if(!ServerUtil.isNullOrEmpty(contactUs.getZip())) 
			body.append(contactUs.getZip());
		body.append("\r\n");
		if(!StringUtils.isNullOrEmpty(contactUs.getPhone())){
			body.append("Phone Number = " + contactUs.getPhone() + "\r\n");
		}

		String storeBody = "";
		if(store != null) {
			storeBody = "Store Visited:\r\n#"
					+ LocatorUtil.padStoreNumber(store.getStoreNumber().intValue()) + "\r\n" + store.getStoreName() + "\r\n" + store.getAddress() + "\r\n" + store.getCity() + ", " + store.getState() + "  " + store.getZip() + "\r\n";
		}
		
		body.append("Email Address = " + contactUs.getEmailAddress() + "\r\n");
		body.append(storeBody);

		if(!StringUtils.isNullOrEmpty(contactUs.getSubmodel())){
	         body.append("Vehicle = " + contactUs.getYear() + " " + contactUs.getMake() + " " + contactUs.getModel() + 
	        		 " " + contactUs.getSubmodel() + "\r\n");
	         }
		body.append("Message = " + contactUs.getMessage() + "\r\n");
		body.append("\r\nUser Agent: " + contactUs.getUserAgent() + "\r\n");
		body.append("JavaScript detected: Yes" ); // this was hardcoded to always say yes
		
		return body;
	}

	
	private Long logContactUsInfo(Mail mail, ContactUs contactUs, Store store) {
		com.bfrc.pojo.customer.CustomerContactusemailLog contactLog = new com.bfrc.pojo.customer.CustomerContactusemailLog();
		
		contactLog.setComments(contactUs.getMessage());
		
		contactLog.setEmailTo(ServerUtil.arrayToString(mail.getTo(), ";"));
		contactLog.setEmailCc(ServerUtil.arrayToString(mail.getCc(), ";"));
		contactLog.setEmailBcc(ServerUtil.arrayToString(mail.getBcc(), ";"));
		contactLog.setEmailFrom(mail.getFrom());
					
		contactLog.setFirstName(contactUs.getFirstName());
		contactLog.setLastName(contactUs.getLastName());

		contactLog.setEmailAddress(contactUs.getEmailAddress());
		contactLog.setDaytimePhone(contactUs.getPhone());
		/*
		contactLog.setEveningPhone(eveningPhone);
		contactLog.setMobilePhone(cellPhone);
		*/
		contactLog.setStreetAddress1(contactUs.getAddress());
		contactLog.setStreetAddress2(contactUs.getAddress2());
		contactLog.setCity(contactUs.getCity());
		contactLog.setState(contactUs.getState());
		contactLog.setZipCode(contactUs.getZip());
		
		//TODO//No company name asked for on Tires Plus
		//contactLog.setCompanyName(getComanyName());
		
		//feedbackId
		List subjects = contactDAO.getSubjects();
		Iterator it = subjects.iterator();
		Integer feedbackId = new Integer(-1);
		while  (it.hasNext()) {  
			Feedback feedback = (Feedback)it.next(); 
			if(contactUs.getNature() != null && feedback.getSubject()!=null){
				if (contactUs.getNature().equalsIgnoreCase(feedback.getSubject())) {
					feedbackId = new Integer(feedback.getFeedbackId());
					break;
				}
			}
		}
		contactLog.setFeedbackId(feedbackId);
		contactLog.setSiteId(new Integer(contactDAO.getSite().getSiteId()));
		contactLog.setUserAgentInfo(contactUs.getUserAgent());
		
		if (store != null) {
			contactLog.setStoreNumber(store.getStoreNumber());
			contactLog.setStoreName(store.getStoreName());
			contactLog.setStoreAddress(store.getAddress());
			contactLog.setStoreCity(store.getCity());
			contactLog.setStoreState(store.getState());
			contactLog.setStoreZip(store.getZip());
		}
		
		if(!StringUtils.isNullOrEmpty(contactUs.getSubmodel())){
			contactLog.setVehicleMake(contactUs.getMakeName());
			contactLog.setVehicleModel(contactUs.getModelName());
			contactLog.setVehicleSubmodel(contactUs.getSubmodel());
			contactLog.setVehicleYear(Short.valueOf(contactUs.getYear()));
		}
		
		Long referenceNumber = Long.valueOf(-1);
		
		try {
			referenceNumber = customerDAO.createCustomerContactusemailLog(contactLog);
		}
		catch(Exception e) {
			System.err.println("Error saving contact log to the database ContactUsService.logContactUsInfo: " + e.getMessage());
			System.err.println(e.getStackTrace());			
		}
		
		return referenceNumber;
	}
	@Override
	public String doContactUs(HttpServletRequest request, HttpSession session,
			ContactUsForm contactUsForm,MailManager mailManager) throws Exception{

		//--- 20101231 contact us use ID and Log Contact Us to DB ---//
		List feedbacks = contactDAO.getAllFeedBacks();
		java.text.DecimalFormat df8 = new java.text.DecimalFormat("00000000");
		Mail mail = new Mail();
		StringBuffer invalid = new StringBuffer();
		
		String[] to = contactDAO.getTo(contactUsForm.getNature(),contactUsForm.getStoreNumber());
		String[] validTo = {};
		if(to!=null||to.length>0) //{
			validTo = ServerUtil.sanitizeInvalidEmails(to, invalid, "TO");
		mail.setTo(validTo);
		
		String[] cc = contactDAO.getCc(contactUsForm.getNature(), contactUsForm.getStoreNumber());
		String[] validCc = {};
		if(cc!=null||cc.length>0) //{
			validCc = ServerUtil.sanitizeInvalidEmails(cc, invalid, "CC");
		mail.setCc(validCc);
		
		String[] bcc = contactDAO.getBcc(contactUsForm.getNature(), contactUsForm.getStoreNumber());
		String[] validBcc = {};
		if(bcc!=null||bcc.length>0) //{
			validBcc = ServerUtil.sanitizeInvalidEmails(bcc, invalid, "BCC");
		mail.setBcc(validBcc);
		
		String from = contactDAO.getFrom();
		mail.setFrom(from);
		Store store = null;
		if (contactUsForm.getStoreNumber()!=null && contactUsForm.getStoreNumber().longValue()!=0) {
			store = storeDAO.findStoreById(contactUsForm.getStoreNumber());
		}
		
		String subject = config.getSiteFullName() + " Contact Feedback - " + contactUsForm.getFirstName() + " " + contactUsForm.getLastName();
		if(Config.isComplimentFeedback(feedbacks, config, contactUsForm.getNature())){
			subject = "Compliment - "+subject;
		}
		mail.setSubject(subject);
		
		
		//--- Start Log Contact Us to Database 20101231 ---//
		UserVehicle v = contactUsForm.getVehicle();
		Long referenceNumber = new Long(-1);
		
		//Log contact us information to the database
		try {
			CustomerContactusemailLog contactLog = new CustomerContactusemailLog();
			
			contactLog.setComments(contactUsForm.getMessage());
			
			contactLog.setEmailTo(ServerUtil.arrayToString(to, ";"));
			contactLog.setEmailCc(ServerUtil.arrayToString(cc, ";"));
			contactLog.setEmailBcc(ServerUtil.arrayToString(bcc, ";"));
			contactLog.setEmailFrom(from);
						
			contactLog.setFirstName(contactUsForm.getFirstName());
			contactLog.setLastName(contactUsForm.getLastName());

			contactLog.setEmailAddress(contactUsForm.getEmailAddress());
			
			contactLog.setStreetAddress1(contactUsForm.getAddress());
			contactLog.setStreetAddress2(contactUsForm.getAddress2());
			contactLog.setCity(contactUsForm.getCity());
			contactLog.setState(contactUsForm.getState());
			contactLog.setZipCode(contactUsForm.getZip());
			
			//No company name asked for on Tires Plus
			//contactLog.setCompanyName(getComanyName());
			
			//feedbackId
			List subjects = contactDAO.getSubjects();
			Iterator it = subjects.iterator();
			Integer feedbackId = new Integer(-1);
			while  (it.hasNext()) {  
				Feedback feedback = (Feedback)it.next(); 
				if (contactUsForm.getNature().equalsIgnoreCase(feedback.getSubject())) {
					feedbackId = new Integer(feedback.getFeedbackId());
					break;
				}
			}
			contactLog.setFeedbackId(feedbackId);
			contactLog.setSiteId(new Integer(contactDAO.getSite().getSiteId()));
			contactLog.setUserAgentInfo(request.getHeader("User-Agent"));
			
			if (store != null) {
				contactLog.setStoreNumber(store.getStoreNumber());
				contactLog.setStoreName(store.getStoreName());
				contactLog.setStoreAddress(store.getAddress());
				contactLog.setStoreCity(store.getCity());
				contactLog.setStoreState(store.getState());
				contactLog.setStoreZip(store.getZip());
			}
			
			if (v != null) {
				contactLog.setVehicleMake(v.getMake());
				contactLog.setVehicleModel(v.getModel());
				contactLog.setVehicleSubmodel(v.getSubmodel());
				contactLog.setVehicleYear(Short.valueOf(v.getYear()));
			}
			
			referenceNumber = customerDAO.createCustomerContactusemailLog(contactLog);
		}
		catch (Exception ex) {
			
			System.err.println("Error saving contact log to the database:" + ex.getMessage());
			System.err.println(ex.getStackTrace());			
		}
		//--- End Log Contact Us to Database ---//
		
		StringBuffer body = new StringBuffer();
		body.append("The individual below has entered feedback on " + contactDAO.getUrl() + " website.\n\n");

		if (referenceNumber.longValue() != -1){
			body.append("Reference # = " + df8.format(referenceNumber) + "\r\n");
		}
		
		body.append("Nature of Inqury = " + contactUsForm.getNature() + "\n");
		body.append("First Name = " + contactUsForm.getFirstName() + "\n");
		body.append("Last Name = " + contactUsForm.getLastName() + "\n");
		/* is there any address info? */
		if( !StringUtils.isNullOrEmpty(contactUsForm.getAddress()) || !StringUtils.isNullOrEmpty(contactUsForm.getAddress2()) ||
			!StringUtils.isNullOrEmpty(contactUsForm.getCity()) || !StringUtils.isNullOrEmpty(contactUsForm.getState()) || !StringUtils.isNullOrEmpty(contactUsForm.getZip())) {
			if(!StringUtils.isNullOrEmpty(contactUsForm.getAddress()))
				body.append("Address = " + contactUsForm.getAddress() + "\n");
			if(!StringUtils.isNullOrEmpty(contactUsForm.getAddress2()))
				body.append(contactUsForm.getAddress2() + "\n");
			/* city, state, zip line */
			if (!StringUtils.isNullOrEmpty(contactUsForm.getCity()))
				body.append(contactUsForm.getCity() + ", ");
			if (!StringUtils.isNullOrEmpty(contactUsForm.getState()))
				body.append(contactUsForm.getState() + "  ");
			if (!StringUtils.isNullOrEmpty(contactUsForm.getZip()))
				body.append(contactUsForm.getZip());
			if(!StringUtils.isNullOrEmpty(contactUsForm.getCity()) || !StringUtils.isNullOrEmpty(contactUsForm.getState()) || !StringUtils.isNullOrEmpty(contactUsForm.getZip()))		 
				body.append("\n");
			}
		String num = contactUsForm.getPhone();
		if(!StringUtils.isNullOrEmpty(num) && !num.equals("--"))
			body.append("Phone = " + num + "\n");
		body.append("E-mail Address = " + contactUsForm.getEmailAddress() + "\n");
		if (store != null) {
			body.append("Store Visited = "
					+ store.getStoreName() + "\n" + store.getAddress() + "\n" + store.getCity() + ", " + store.getState() + "  " + store.getZip() + "\n");
		}
		if(v != null && v.getSubmodel() != null)
			body.append("Vehicle = " + v.getYear() + " " + v.getMake() + " " + v.getModel() + " " + v.getSubmodel() + "\r\n");
		body.append("Message = " + contactUsForm.getMessage() + "\n");
		body.append("\r\nUser Agent: " + request.getHeader("User-Agent") + "\r\n");
		
		body.append(ContactUsUtils.printSessionData(request));
		
		mail.setBody(body.toString());
		if(invalid!=null && invalid.length()>0)
		{
			ServerUtil.sendErrorEmail(mail, invalid.toString(), mailManager,Config.getCtx(session.getServletContext()));
		}	
		mailManager.sendMail(mail);
		
		session.invalidate();
		return "contact/contactus_thankyou";		
		
	}
			
}
