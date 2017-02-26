/**
 * 
 */
package com.bfrc.framework.util;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.bfrc.framework.dao.error.ErrorMessageDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.error.ErrorMessage;
import com.bfrc.pojo.error.ErrorMessageStatusEnum;

/**
 * @author schowdhu
 *
 */
public class ErrorHandlerManager {
	
	private static final long serialVersionUID = 02L;
	private final Log logger = LogFactory.getLog(ErrorHandlerManager.class);
	 
	private ErrorMessageDAO errorMessageDAO;
	private MailManager mailManager;
	private String emailGroup;
	
	public ErrorHandlerManager(ApplicationContext applicationContext, String emailGroup) {
		if(applicationContext != null && applicationContext.containsBean("errorMessageDAO")) {
			errorMessageDAO = (ErrorMessageDAO) applicationContext.getBean("errorMessageDAO");
			logger.info("loaded ErrorMessage DAO");
		} 

		if(applicationContext != null && applicationContext.containsBean("mailManager")) {
			mailManager = (MailManager) applicationContext.getBean("mailManager");
			logger.info("loaded MailManager");
		} 
		this.emailGroup = emailGroup;
	}

	public void emailErrorMessages(){
		StringBuffer sb = new StringBuffer();		
		List<ErrorMessage> messageList = errorMessageDAO.getNewErrorMessages();
		logger.info("Inside emailErrorMessages size = "+ messageList.size());
		if(!messageList.isEmpty()){
			int i = 0;
			for (ErrorMessage message : messageList){
				i++;
				sb.append("\n-------------------------------------------------------");
				sb.append("\nException # "+ i);
				sb.append("\n-------------------------------------------------------");
				sb.append(message.getErrorMessage());
			}
			
			Mail mail = new Mail();
			String[] toEmails = new String[]{emailGroup};
			mail.setTo(toEmails);

			mail.setFrom("webmaster@firestonecompleteautocare.com");
			mail.setSubject("BFRC WebSite Errors - Periodic Comprehensive Report");
			mail.setHtml(true);
			mail.setBody(org.apache.commons.lang.StringEscapeUtils.unescapeHtml(sb.toString()));

			logger.info(sb.toString());

			try {
				mailManager.sendMail(mail);
			} catch (Exception e) {
				e.printStackTrace();
				errorMessageDAO.updateMessageStatuses(ErrorMessageStatusEnum.NEW.getStatusCode(), 
						ErrorMessageStatusEnum.PROCESSING.getStatusCode());
			}

			errorMessageDAO.updateMessageStatuses(ErrorMessageStatusEnum.PROCESSING.getStatusCode(), 
					ErrorMessageStatusEnum.COMPLETE.getStatusCode());
			
		}
	}

	/**
	 * @return the emailGroup
	 */
	public String getEmailGroup() {
		return emailGroup;
	}

	/**
	 * @param emailGroup the emailGroup to set
	 */
	public void setEmailGroup(String emailGroup) {
		this.emailGroup = emailGroup;
	}
}
