/**
 * 
 */
package com.bfrc.pojo.error;

import java.io.Serializable;
import java.util.Date;

import com.bfrc.pojo.contact.WebSite;


/**
 * @author schowdhu
 *
 */
public class ErrorMessage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long errorId;
	
	private String errorMessage;
	
	private WebSite site;
	
	private Date insertDate;
	
	private String status;
	
	
	public ErrorMessage() {
	}

	public ErrorMessage(String errorMessage, String status) {
		this.errorMessage = errorMessage;
		this.status = status;
	}


	public ErrorMessage(String errorMessage, WebSite site) {
		this.errorMessage = errorMessage;
		this.site = site;
		this.insertDate = new Date();
		this.status = ErrorMessageStatusEnum.NEW.getStatusCode();
	}
	
	/**
	 * @return the errorId
	 */
	public Long getErrorId() {
		return errorId;
	}

	/**
	 * @param errorId the errorId to set
	 */
	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the site
	 */
	public WebSite getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(WebSite site) {
		this.site = site;
	}

	/**
	 * @return the insertDate
	 */
	public Date getInsertDate() {
		return insertDate;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}


}
