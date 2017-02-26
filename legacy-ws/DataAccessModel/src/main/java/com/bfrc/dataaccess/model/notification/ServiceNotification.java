/**
 * 
 */
package com.bfrc.dataaccess.model.notification;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author schowdhu
 *
 */
public class ServiceNotification implements Serializable{

	private static final long serialVersionUID = -2571240601750318507L;
	
	private Long notificationId;
	private String notification;
	private Date startDate;
	private Date endDate;
	private String title;
	private String subject;
	private Long userId;
	private Date lastUpdateDate;
	
	public ServiceNotification() {
	}
	
	/**
	 * 
	 * @param notificationId
	 * @param notification
	 */
	public ServiceNotification(Long notificationId, String notification) {
		this.notificationId = notificationId;
		this.notification = notification;
		this.startDate = new Date();
	}
	
	/**
	 * @param notificationId
	 * @param notification
	 * @param startDate
	 */
	public ServiceNotification(Long notificationId, String notification,
			Date startDate) {
		this.notificationId = notificationId;
		this.notification = notification;
		this.startDate = startDate;
	}
	
	/**
	 * @param notificationId
	 * @param notification
	 * @param startDate
	 */
	public ServiceNotification(Long notificationId, String notification,
			Date startDate, Date endDate) {
		this.notificationId = notificationId;
		this.notification = notification;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getNotificationId() {
		return notificationId;
	}
	public void setNotificationId(Long notificationId) {
		this.notificationId = notificationId;
	}
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@JsonIgnore
	public Long getUserId() {
		return userId;
	}
	@JsonIgnore
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@JsonIgnore
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	@JsonIgnore
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result
				+ ((notification == null) ? 0 : notification.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceNotification other = (ServiceNotification) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (notification == null) {
			if (other.notification != null)
				return false;
		} else if (!notification.equals(other.notification))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceNotification [notificationId=" + notificationId
				+ ", notification=" + notification + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", title=" + title + ", subject="
				+ subject + ", userId=" + userId + "]";
	}
}
