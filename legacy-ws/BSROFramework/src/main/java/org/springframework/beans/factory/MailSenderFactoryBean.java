package org.springframework.beans.factory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.mail.MailSender;

public class MailSenderFactoryBean implements FactoryBean<MailSender> {
	private static final Log log = LogFactory.getLog(MailSenderFactoryBean.class);
	
	private Map<String, MailSender> mailSenderMap = new HashMap<String, MailSender>();
	
	private String mailSender = null;

	@Override
	public MailSender getObject() throws Exception {
		log.info("Injecting mail sender \""+mailSender+"\"");
		return mailSenderMap.get(mailSender);
	}

	@Override
	public Class<MailSender> getObjectType() {
		return MailSender.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public Map<String, MailSender> getMailSenderMap() {
		return mailSenderMap;
	}

	public void setMailSenderMap(Map<String, MailSender> mailSenderMap) {
		this.mailSenderMap = mailSenderMap;
	}

	public String getMailSender() {
		return mailSender;
	}

	public void setMailSender(String mailSender) {
		this.mailSender = mailSender;
	}

}
