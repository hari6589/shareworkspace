package com.bfrc.pojo.email;

public interface IEmailSignup {

	/**
	 * Return the simple primary key value that identifies this object.
	 * @return java.lang.Long
	 */
	public java.lang.Long getSignupId();

	/**
	 * Set the simple primary key value that identifies this object.
	 * @param signupId
	 */
	public void setSignupId(java.lang.Long signupId);

	/**
	 * Return the value of the FIRST_NAME column.
	 * @return java.lang.String
	 */
	public java.lang.String getFirstName();

	/**
	 * Set the value of the FIRST_NAME column.
	 * @param firstName
	 */
	public void setFirstName(java.lang.String firstName);

	/**
	 * Return the value of the MIDDLE_INITIAL column.
	 * @return java.lang.String
	 */
	public java.lang.String getMiddleInitial();

	/**
	 * Set the value of the MIDDLE_INITIAL column.
	 * @param middleInitial
	 */
	public void setMiddleInitial(java.lang.String middleInitial);

	/**
	 * Return the value of the LAST_NAME column.
	 * @return java.lang.String
	 */
	public java.lang.String getLastName();

	/**
	 * Set the value of the LAST_NAME column.
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName);

	/**
	 * Return the value of the ADDRESS_1 column.
	 * @return java.lang.String
	 */
	public java.lang.String getAddress1();

	/**
	 * Set the value of the ADDRESS_1 column.
	 * @param address1
	 */
	public void setAddress1(java.lang.String address1);

	/**
	 * Return the value of the ADDRESS_2 column.
	 * @return java.lang.String
	 */
	public java.lang.String getAddress2();

	/**
	 * Set the value of the ADDRESS_2 column.
	 * @param address2
	 */
	public void setAddress2(java.lang.String address2);

	/**
	 * Return the value of the CITY column.
	 * @return java.lang.String
	 */
	public java.lang.String getCity();

	/**
	 * Set the value of the CITY column.
	 * @param city
	 */
	public void setCity(java.lang.String city);

	/**
	 * Return the value of the STATE column.
	 * @return java.lang.String
	 */
	public java.lang.String getState();

	/**
	 * Set the value of the STATE column.
	 * @param state
	 */
	public void setState(java.lang.String state);

	/**
	 * Return the value of the ZIP column.
	 * @return java.lang.String
	 */
	public java.lang.String getZip();

	/**
	 * Set the value of the ZIP column.
	 * @param zip
	 */
	public void setZip(java.lang.String zip);

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
	 * Return the value of the CREATED_DATE column.
	 * @return java.util.Date
	 */
	public java.util.Date getCreatedDate();

	/**
	 * Set the value of the CREATED_DATE column.
	 * @param createdDate
	 */
	public void setCreatedDate(java.util.Date createdDate);

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