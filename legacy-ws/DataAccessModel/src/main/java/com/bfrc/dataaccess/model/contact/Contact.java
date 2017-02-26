package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Contact implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer contactId;
	private String contactName;
	private String contactEmail;
	private Set<FeedbackContact> bfrcWebSiteFeedbackContacts = new HashSet<FeedbackContact>(0);

	public Contact() {}

	public Contact(int contactId) {
		this.contactId = contactId;
	}


	public Contact(Integer contactId, String contactName,
			String contactEmail, Set<FeedbackContact> bfrcWebSiteFeedbackContacts) {
		this.contactId = contactId;
		this.contactName = contactName;
		this.contactEmail = contactEmail;
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getName() {
		return this.contactName;
	}

	public void setName(String contactName) {
		this.contactName = contactName;
	}

	public String getEmail() {
		return this.contactEmail;
	}

	public void setEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public Set<FeedbackContact> getFeedbackContacts() {
		return this.bfrcWebSiteFeedbackContacts;
	}

	public void setFeedbackContacts(Set<FeedbackContact> bfrcWebSiteFeedbackContacts) {
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

}
