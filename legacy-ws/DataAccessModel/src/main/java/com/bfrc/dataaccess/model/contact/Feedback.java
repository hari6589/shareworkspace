package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Feedback implements Serializable {

	private static final long serialVersionUID = 1L;
	private int feedbackId;
	private WebSite bfrcWebSite;
	private Integer feedbackTypeId;
	private Integer feedbackOrder;
	private String feedbackSubject;
	private Set bfrcWebSiteFeedbackContacts = new HashSet(0);

	public Feedback() {}

	public Feedback(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Feedback(int feedbackId, WebSite bfrcWebSite,
			Integer feedbackTypeId, Integer feedbackOrder,
			String feedbackSubject, Set bfrcWebSiteFeedbackContacts) {
		this.feedbackId = feedbackId;
		this.bfrcWebSite = bfrcWebSite;
		this.feedbackTypeId = feedbackTypeId;
		this.feedbackOrder = feedbackOrder;
		this.feedbackSubject = feedbackSubject;
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

	public int getFeedbackId() {
		return this.feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public WebSite getWebSite() {
		return this.bfrcWebSite;
	}

	public void setWebSite(WebSite bfrcWebSite) {
		this.bfrcWebSite = bfrcWebSite;
	}

	public Integer getFeedbackTypeId() {
		return this.feedbackTypeId;
	}

	public void setFeedbackTypeId(Integer feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}

	public Integer getOrder() {
		return this.feedbackOrder;
	}

	public void setOrder(Integer feedbackOrder) {
		this.feedbackOrder = feedbackOrder;
	}

	public String getSubject() {
		return this.feedbackSubject;
	}

	public void setSubject(String feedbackSubject) {
		this.feedbackSubject = feedbackSubject;
	}

	public Set getContacts() {
		return this.bfrcWebSiteFeedbackContacts;
	}

	public void setContacts(Set bfrcWebSiteFeedbackContacts) {
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

}
