package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;


public class FeedbackContact implements Serializable {

	private static final long serialVersionUID = 1L;
	private FeedbackContactId id;
	private Contact bfrcWebSiteContact;
	private Feedback bfrcWebSiteFeedback;
	private ContactType bfrcWebSiteContactType;

	public FeedbackContact() {}

	public FeedbackContact(FeedbackContactId id) {
		this.id = id;
	}
	
	public FeedbackContact(FeedbackContactId id,
			Contact bfrcWebSiteContact,
			Feedback bfrcWebSiteFeedback,
			ContactType bfrcWebSiteContactType) {
		this.id = id;
		this.bfrcWebSiteContact = bfrcWebSiteContact;
		this.bfrcWebSiteFeedback = bfrcWebSiteFeedback;
		this.bfrcWebSiteContactType = bfrcWebSiteContactType;
	}

	public FeedbackContactId getId() {
		return this.id;
	}

	public void setId(FeedbackContactId id) {
		this.id = id;
	}

	public Contact getContact() {
		return this.bfrcWebSiteContact;
	}

	public void setContact(Contact bfrcWebSiteContact) {
		this.bfrcWebSiteContact = bfrcWebSiteContact;
	}

	public Feedback getFeedback() {
		return this.bfrcWebSiteFeedback;
	}

	public void setFeedback(Feedback bfrcWebSiteFeedback) {
		this.bfrcWebSiteFeedback = bfrcWebSiteFeedback;
	}

	public ContactType getContactType() {
		return this.bfrcWebSiteContactType;
	}

	public void setContactType(
			ContactType bfrcWebSiteContactType) {
		this.bfrcWebSiteContactType = bfrcWebSiteContactType;
	}

}
