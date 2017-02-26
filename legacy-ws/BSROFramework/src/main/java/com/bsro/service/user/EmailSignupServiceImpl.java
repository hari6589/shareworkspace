package com.bsro.service.user;

import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.email.EmailSignup;

@Service
public class EmailSignupServiceImpl implements EmailSignupService {
	@Autowired
	private EmailSignupDAO emailSignupDAO;
	
	@Autowired
	private ContactDAO contactDAO;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MailManager mailManager;
	
	public static String generateEmailSignupOptinCode() {
		String signupCode = String.valueOf((int)(Math.random()*10000000));
		while(signupCode.length() < 10)
			signupCode += String.valueOf((int)(Math.random()*10000000));
		if(signupCode.length() > 10)
			signupCode = signupCode.substring(0, 10);
		return signupCode;
	}
	
	public void saveAndSendConfirmation(EmailSignup signup) throws Exception {
		signup.setOptinCode(generateEmailSignupOptinCode());
		signup.setCreatedDate(new Date());
		if(emailSignupDAO.subscribe(signup)) {
			sendConfirmation(signup);
		}
	}
	
	private void sendConfirmation(EmailSignup e) throws Exception {
		Mail mail = new Mail();
		mail.setHtml(true);
		mail.setFrom(contactDAO.getFrom());
		String subject = contactDAO.getSubject(new Integer(16));
		mail.setSubject(subject);
		mail.setTo(new String[] {e.getEmailAddress()});
		
		Object[] messageParameters = new Object[] {subject, e.getFirstName(), e.getLastName(), e.getOptinCode(), e.getOptinCode()};
		
		String body =  messageSource.getMessage("email.signup.confirmation.email.html", messageParameters, Locale.getDefault());
		
		mail.setBody(body);
		
		mailManager.sendMail(mail);
	}
}
