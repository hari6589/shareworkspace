package com.bfrc.pojo.email;

public interface IEmailUnsubscribe {

	/**
	 * Return the simple primary key value that identifies this object.
	 * @return java.lang.Long
	 */
	public java.lang.Long getUnsubscribeId();

	/**
	 * Set the simple primary key value that identifies this object.
	 * @param unsubscribeId
	 */
	public void setUnsubscribeId(java.lang.Long unsubscribeId);

	/**
	 * Return the value of the EMAIL_ADDRESS column.
	 * @return java.lang.String
	 */
	public java.lang.String getEmailAddress();

	/**
	 * Set the value of the EMAIL_ADDRESS column.
	 * @param emailAddress
	 */
	public void setEmailAddress(java.lang.String emailAddress);

	/**
	 * Return the value of the UNSUBSCRIBE_DATE column.
	 * @return java.util.Date
	 */
	public java.util.Date getUnsubscribeDate();

	/**
	 * Set the value of the UNSUBSCRIBE_DATE column.
	 * @param unsubscribeDate
	 */
	public void setUnsubscribeDate(java.util.Date unsubscribeDate);

	/**
	 * Return the value of the SOURCE column.
	 * @return java.lang.String
	 */
	public java.lang.String getSource();

	/**
	 * Set the value of the SOURCE column.
	 * @param source
	 */
	public void setSource(java.lang.String source);

}