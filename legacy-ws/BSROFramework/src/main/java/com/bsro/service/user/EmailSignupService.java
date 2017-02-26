package com.bsro.service.user;

import com.bfrc.pojo.email.EmailSignup;

public interface EmailSignupService {
	public void saveAndSendConfirmation(EmailSignup signup) throws Exception;
}
