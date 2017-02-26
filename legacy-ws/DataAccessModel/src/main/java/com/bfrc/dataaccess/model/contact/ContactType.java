package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ContactType implements Serializable {

	private static final long serialVersionUID = 1L;
	private int contactTypeId;
	private String contactTypeName;
	private Set bfrcWebSiteFeedbackContacts = new HashSet(0);

	public ContactType() {}

	public ContactType(int contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public ContactType(int contactTypeId, String contactTypeName,
			Set bfrcWebSiteFeedbackContacts) {
		this.contactTypeId = contactTypeId;
		this.contactTypeName = contactTypeName;
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

	public int getContactTypeId() {
		return this.contactTypeId;
	}

	public void setContactTypeId(int contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public String getName() {
		return this.contactTypeName;
	}

	public void setName(String contactTypeName) {
		this.contactTypeName = contactTypeName;
	}

	public Set getContacts() {
		return this.bfrcWebSiteFeedbackContacts;
	}

	public void setContacts(Set bfrcWebSiteFeedbackContacts) {
		this.bfrcWebSiteFeedbackContacts = bfrcWebSiteFeedbackContacts;
	}

}
