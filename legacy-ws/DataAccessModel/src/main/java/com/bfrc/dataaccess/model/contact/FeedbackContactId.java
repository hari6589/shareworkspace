package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;

public class FeedbackContactId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer feedbackId;
	private Integer contactId;
	private Integer contactTypeId;

	public FeedbackContactId() {}

	public FeedbackContactId(Integer feedbackId, Integer contactId,
			Integer contactTypeId) {
		this.feedbackId = feedbackId;
		this.contactId = contactId;
		this.contactTypeId = contactTypeId;
	}


	public Integer getFeedbackId() {
		return this.feedbackId;
	}

	public void setFeedbackId(Integer feedbackId) {
		this.feedbackId = feedbackId;
	}

	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public Integer getContactTypeId() {
		return this.contactTypeId;
	}

	public void setContactTypeId(Integer contactTypeId) {
		this.contactTypeId = contactTypeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof FeedbackContactId))
			return false;
		FeedbackContactId castOther = (FeedbackContactId) other;

		return ((this.getFeedbackId() == castOther.getFeedbackId()) || (this
				.getFeedbackId() != null
				&& castOther.getFeedbackId() != null && this.getFeedbackId()
				.equals(castOther.getFeedbackId())))
				&& ((this.getContactId() == castOther.getContactId()) || (this
						.getContactId() != null
						&& castOther.getContactId() != null && this
						.getContactId().equals(castOther.getContactId())))
				&& ((this.getContactTypeId() == castOther.getContactTypeId()) || (this
						.getContactTypeId() != null
						&& castOther.getContactTypeId() != null && this
						.getContactTypeId()
						.equals(castOther.getContactTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getFeedbackId() == null ? 0 : this.getFeedbackId()
						.hashCode());
		result = 37 * result
				+ (getContactId() == null ? 0 : this.getContactId().hashCode());
		result = 37
				* result
				+ (getContactTypeId() == null ? 0 : this.getContactTypeId()
						.hashCode());
		return result;
	}

}
