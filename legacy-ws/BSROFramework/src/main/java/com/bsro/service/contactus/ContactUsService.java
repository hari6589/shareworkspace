package com.bsro.service.contactus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;

import com.bfrc.UserSessionData;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bsro.pojo.contactus.ContactUs;
import com.bsro.pojo.form.ContactUsForm;

public interface ContactUsService {
	public ContactUs createContactUsBean(UserSessionData userData);
	public Mail createContactUsMail(ContactUs contactUs, WebApplicationContext appContext, MailManager mailManager);
	public String doContactUs(HttpServletRequest request, HttpSession session,
			ContactUsForm contactUsForm,MailManager mailManager) throws Exception;
}
