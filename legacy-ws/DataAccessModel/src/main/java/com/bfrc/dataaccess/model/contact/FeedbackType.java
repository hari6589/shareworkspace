package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;

public class FeedbackType implements Serializable {

	private static final long serialVersionUID = 1L;
	private int feedbackTypeId;

	private String feedbackTypeName;

	public FeedbackType() {}

	public FeedbackType(int feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}

	public FeedbackType(int feedbackTypeId, String feedbackTypeName) {
		this.feedbackTypeId = feedbackTypeId;
		this.feedbackTypeName = feedbackTypeName;
	}

	public int getFeedbackTypeId() {
		return this.feedbackTypeId;
	}

	public void setFeedbackTypeId(int feedbackTypeId) {
		this.feedbackTypeId = feedbackTypeId;
	}

	public String getName() {
		return this.feedbackTypeName;
	}

	public void setName(String feedbackTypeName) {
		this.feedbackTypeName = feedbackTypeName;
	}

}
