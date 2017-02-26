package com.bfrc.framework.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.bfrc.pojo.email.EmailSignup;
import com.bfrc.pojo.reward.RewardsLog;

public interface EmailSignupDAO {
	public boolean subscribe(EmailSignup visitor) throws DataAccessException;
	public boolean unsubscribe(Long signUpId) throws DataAccessException;
	public boolean unsubscribe(String email, String siteName) throws DataAccessException;
	public EmailSignup getEmailSignup(Long signUpId) throws DataAccessException;
	public Collection getEmailSignup(String email) throws DataAccessException;
	public Collection getEmailSignup(String email, String siteName) throws DataAccessException;
	public void confirm(String optinCode);
	public List getDelayedSignups(String soure);
	public List getDelayedSignups();
	public long update(EmailSignup item);
	public void addRewardsLog(RewardsLog rewardsLog);
}
