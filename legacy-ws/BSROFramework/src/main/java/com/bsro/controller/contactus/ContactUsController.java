package com.bsro.controller.contactus;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.bfrc.Config;
import com.bfrc.UserSessionData;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.CustomerDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ContactUsUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.contact.Feedback;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.customer.CustomerContactusemailLog;
import com.bfrc.pojo.store.Store;
import com.bfrc.security.Encode;
import com.bsro.pojo.contactus.ContactUs;
import com.bsro.pojo.form.ContactUsForm;
import com.bsro.service.contactus.ContactUsService;
import com.bsro.springframework.propertyeditor.ReplaceAllEditor;

@Controller
public class ContactUsController {
	
	@Autowired
	@Qualifier("contactUsService")
	ContactUsService contactUsService;
	
	@Autowired
	MailManager mailManager;
	
	@Autowired
	ContactDAO contactDAO;
	@Autowired
	StoreDAO storeDAO;
	@Autowired
	CustomerDAO customerDAO;
	
	@Autowired
	Config config;
	
	protected String contactView;
	
	public String getContactView() {
		return contactView;
	}

	public void setContactView(String contactView) {
		this.contactView = contactView;
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(String.class, new ReplaceAllEditor(false, "&#39;", "'"));		
	}
	@ModelAttribute("contactUsForm")
	public ContactUsForm getDisplayMapForm() {
		return new ContactUsForm();
	}
	@RequestMapping(value="/contact_us/thanks.htm", method=RequestMethod.GET)
	public String doContactUs(HttpServletRequest request, HttpSession session, HttpServletResponse response, 
			    @RequestParam(value="storeNumber", required=false) String storeNumber, Model model) {	
		WebApplicationContext context =
				WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
		
		UserSessionData userData = (UserSessionData)session.getAttribute(UserSessionData._USER_SESSION_DATA);
		
		if(userData == null) {
			session.setAttribute("invalidSession", true);
			return "redirect:/contact_us/";
		}
		
		userData.setStoreNumber(storeNumber);
		ContactUs contactUs = contactUsService.createContactUsBean(userData);
		contactUs.setUserAgent(request.getHeader("User-Agent"));
		
		Mail mail = contactUsService.createContactUsMail(contactUs, context, mailManager);
		
		try {
			mailManager.sendMail(mail);
		} catch (Exception e) {
			System.err.println("Error trying to send email in ContactUsController.doContactUs: " + e.getMessage());
			System.err.println(e.getStackTrace());
		}
		
		boolean isCompliment = Config.isComplimentFeedBack(contactUs.getNature());
		session.setAttribute("isCompliment", isCompliment);
		session.setAttribute("storeNumber", contactUs.getStrStoreNumber());
		
		return "redirect:thank_you.htm";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/contact_us/thank_you.htm")
	public String confirmation(HttpServletRequest request, HttpSession session, Model model) {
		
		UserSessionData userData = (UserSessionData)session.getAttribute(UserSessionData._USER_SESSION_DATA);
		
		if(userData == null) {
			session.setAttribute("invalidSession", true);
			return "redirect:/contact_us/";
		}
		
	    String[] hiddenNames = new String[]{  "nature",
										      "firstName",
										      "lastName",
										      "address",
										      "address2",
										      "city",
										      "state",
										      "zip",
										      "phone",
										      "make",
										      "year",
										      "model",
										      "submodel",
										      "emailAddress",
										      "javaScript",
										      "message"};
		for(int i=0; i< hiddenNames.length; i++ ){
			if(userData.get("contactus."+hiddenNames[i]) != null) {
				userData.remove("contactus."+hiddenNames[i]);
			}
		}
		
		model.addAttribute("isCompliment", session.getAttribute("isCompliment"));
		model.addAttribute("storeNumber", session.getAttribute("storeNumber"));
		
		session.removeAttribute("isCompliment");
		session.removeAttribute("storeNumber");
		session.removeAttribute("invalidSession");
		
		return "contact_us/thank_you";
	}	
	@RequestMapping(value="/contact_us/getcontact.htm", method=RequestMethod.GET)
	public String getContactInfo(HttpServletRequest request, HttpSession session, HttpServletResponse response,Model model) {
		List subjects = contactDAO.getSubjects();
		model.addAttribute("subjects", subjects);

		List states = contactDAO.getStates();
		model.addAttribute("states", states);

		List mflist = contactDAO.getMainFeedbacks();
		model.addAttribute("mainFeedbacks", mflist);			
		return "contact/getContact";
		
	}
	@RequestMapping(value="/contact_us/tp_thanks.htm", method=RequestMethod.POST)
	public String doContactUsTP(HttpServletRequest request, HttpSession session, HttpServletResponse response,ContactUsForm contactUsForm,
			    @RequestParam(value="storeNumber", required=false) String storeNumber, Model model) {
		
		//request = getRequest();
		//--- 20101231 contact us use ID and Log Contact Us to DB ---//
		List feedbacks = contactDAO.getAllFeedBacks();
		java.text.DecimalFormat df8 = new java.text.DecimalFormat("00000000");
    	//Set up commonly used variables in e-mail and database log
		StringBuffer invalid = new StringBuffer();
		
		// store info is needed for 3 inquiries below only
    	if (!Config.isCustomerServiceFeedback(feedbacks, config, contactUsForm.getNature())
				&& !Config.isComplimentFeedback(feedbacks, config, contactUsForm.getNature())) {
    		contactUsForm.setStoreNumber(null);
    	}
	    // vehicle info is needed for 1 inquiry below only
    	if (!Config.isQuestionAboutTireFeedback(feedbacks, config, contactUsForm.getNature())) {
    		contactUsForm.setVehicle(null);
    	}
		
		Mail mail = new Mail();
		
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
		
		String from = contactUsForm.getFirstName() + " " + contactUsForm.getLastName() + "<" + contactUsForm.getEmailAddress() + ">";
		if (Config.isCustomerServiceFeedback(feedbacks, config, contactUsForm.getNature())
		        || Config.isComplimentFeedback(feedbacks, config, contactUsForm.getNature())){
			from = contactDAO.getFrom();
		}

		Store store = null;
		if (contactUsForm.getStoreNumber()!= null && contactUsForm.getStoreNumber().longValue()!=0) {
			store = storeDAO.findStoreById(contactUsForm.getStoreNumber());
		}

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
			contactLog.setDaytimePhone(contactUsForm.getDayPhone());
			contactLog.setEveningPhone(contactUsForm.getEveningPhone());
			contactLog.setMobilePhone(contactUsForm.getCellPhone());

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

		//Send out contact e-mail
		mail.setFrom(from);
		String storeBody = "", storeSuffix = "";
		if (store != null) {
			storeBody = "Store Visited = " + store.getStoreName() + "\r\n" + store.getAddress() + "\r\n" + store.getCity() + ", " + store.getState() + "  " + store.getZip() + "\r\n";
			if(store.getStoreName().indexOf("Licensee") > -1)
				storeSuffix = " Licensee";

		}

		String subject = config.getSiteFullName() + storeSuffix + " Feedback - " + contactUsForm.getFirstName() + " " + contactUsForm.getLastName();
		if(Config.isComplimentFeedback(feedbacks, config, contactUsForm.getNature())){
			subject = "Compliment - "+subject;
		}
		mail.setSubject(subject);

		StringBuffer body = new StringBuffer();
		body.append("The individual below has entered feedback on " + contactDAO.getUrl() + " website.\r\n\r\n");

		if (referenceNumber.longValue() != -1){
			body.append("Reference # = " + df8.format(referenceNumber) + "\r\n");
		}

		body.append("Nature of Inquiry = " + contactUsForm.getNature() + "\r\n");
		body.append("First Name = " + contactUsForm.getFirstName() + "\r\n");
		body.append("Last Name = " + contactUsForm.getLastName() + "\r\n");

		String address = contactUsForm.getAddress();
		if(address != null && !address.equals(""))
			body.append(address + "\r\n");

		String address2 = contactUsForm.getAddress2();
		if(address2 != null && !address2.equals(""))
			body.append(address2 + "\r\n");

		String city = contactUsForm.getCity();
		if(city != null && !city.equals(""))
			body.append(city);

		String state = contactUsForm.getState();
		if(state != null && !state.equals(""))
			body.append(", " + state + "  ");

		String zip = contactUsForm.getZip();
		if(zip != null && !zip.equals(""))
			body.append(zip);
		body.append("\r\n");
		
		// 1/5/2012 track phone numbers in LW tags
		StringBuffer phoneContacts = new StringBuffer(); // day,evening,cellphones
		if(!"--".equals(contactUsForm.getDayPhone())) {
			body.append("Daytime Phone = " + contactUsForm.getDayPhone() + "\r\n");
			phoneContacts.append(contactUsForm.getDayPhone());
		}
		if(!"--".equals(contactUsForm.getEveningPhone())) {
			body.append("Evening Phone = " + contactUsForm.getEveningPhone() + "\r\n");
			if (phoneContacts.length() > 0) phoneContacts.append(",");
			phoneContacts.append(contactUsForm.getEveningPhone());
		}
		if(!"--".equals(contactUsForm.getCellPhone())) {
			body.append("Mobile Phone = " + contactUsForm.getCellPhone() + "\r\n");
			if (phoneContacts.length() > 0) phoneContacts.append(",");
			phoneContacts.append(contactUsForm.getCellPhone());
		}

		request.getSession().setAttribute("phoneContacts", Encode.html(phoneContacts.toString()));

		body.append("E-mail Address = " + contactUsForm.getEmailAddress() + "\r\n");
		body.append(storeBody);

		if(v != null && v.getSubmodel() != null)
			body.append("Vehicle = " + v.getYear() + " " + v.getMake() + " " + v.getModel() + " " + v.getSubmodel() + "\r\n");

		body.append("Message = " + contactUsForm.getMessage() + "\r\n");
		body.append("\r\nUser Agent: " + request.getHeader("User-Agent") + "\r\n");
		
		body.append(ContactUsUtils.printSessionData(request));
		
		mail.setBody(body.toString());
		if(invalid!=null && invalid.length()>0)
		{
			ServerUtil.sendErrorEmail(mail, invalid.toString(), mailManager,Config.getCtx(request.getSession().getServletContext()));
		}
		try {
			mailManager.sendMail(mail);
		} catch (Exception e) {
			//do nothing
		}
		//getRequest().getSession().invalidate();
		return "contact/doContactUs";
		
	}
	
}
