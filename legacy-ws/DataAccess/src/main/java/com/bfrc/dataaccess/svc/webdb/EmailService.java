package com.bfrc.dataaccess.svc.webdb;

import java.util.List;

import com.bfrc.dataaccess.exception.InvalidEmailException;
import com.bfrc.dataaccess.model.email.EmailSignup;

public interface EmailService {

	public void subscribe(EmailSignup signup) throws InvalidEmailException;

	public List<String> getToEmail(String siteName, String feedbackType, Integer feedbackId);
	public List<String> getToEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd);

	public List<String> getCcEmail(String siteName, String feedbackType, Integer feedbackId);
	public List<String> getCcEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd);

	public List<String> getBccEmail(String siteName, String feedbackType, Integer feedbackId);
	public List<String> getBccEmail(String siteName, String feedbackType,
			Integer feedbackId, Long storeNumber, String stateCd);
	
	public String getDoNotReply(String siteName);
}
