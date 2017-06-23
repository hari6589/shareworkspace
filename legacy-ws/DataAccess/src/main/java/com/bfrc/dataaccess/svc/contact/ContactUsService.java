package com.bfrc.dataaccess.svc.contact;

import java.util.List;

import app.bsro.model.contact.ContactUs;

import com.bfrc.dataaccess.model.ValueTextBean;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.pojo.contact.Feedback;

public interface ContactUsService {

	public List<ValueTextBean> listAllSubjects(String siteType);
	public void sendContactUs(ContactUs contactUs, String siteType, Long storeId, Long acesVehicleId, String userAgent);
	public String sendFleetCareContactUs(ContactUs contactUs, String siteName, Long storeNumber);
	public Long createEmailSignup(EmailSignup emailSignup);
	public EmailSignup getEmailSignup(Long signUpId);
	
	@SuppressWarnings("rawtypes")
	public List listAllCategories(String siteName);
	public List<Feedback> listEnquiries(String siteName, Integer categoryId);
	
	
}
