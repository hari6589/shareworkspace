package com.bfrc.pojo.contact;

// Generated Jan 9, 2007 10:55:22 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Feedback generated by hbm2java
 */
@JsonIgnoreProperties({"webSite", "bfrcWebSite", "feedbackTypeId", "order", "feedbackOrder", "mainFeedbackId", "contacts", "bfrcWebSiteFeedbackContacts"})
public class Feedback implements java.io.Serializable {

	// Fields    

	private int feedbackId;

	private WebSite bfrcWebSite;

	private Integer feedbackTypeId;

	private Integer feedbackOrder;

	private String feedbackSubject;
	
	private Integer mainFeedbackId;

	private Set bfrcWebSiteFeedbackContacts = new HashSet(0);

	// Constructors

	/** default constructor */
	public Feedback() {
	}

	/** minimal constructor */
	public Feedback(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	/** full constructor */
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

	// Property accessors
	@JsonSerialize(using=ToStringSerializer.class)
	@JsonProperty("id")
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

	public Integer getMainFeedbackId() {
		return mainFeedbackId;
	}

	public void setMainFeedbackId(Integer mainFeedbackId) {
		this.mainFeedbackId = mainFeedbackId;
	}

	public Set getContacts() {
		return this.bfrcWebSiteFeedbackContacts;
	}

	public void setContacts(Set bfrcWebSiteFeedbackContacts) {
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

}
