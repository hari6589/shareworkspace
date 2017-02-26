/**
 * MaterialsAccepted.java
 *
 * This file was auto-generated from WSDL
 * by the IBM Web services WSDL2Java emitter.
 * o0444.10 v11404193627
 */

package com.bfrc.pojo.appointment;

public class AppointmentSentStatus  {
    private long appointmentId;
    private java.lang.String status;
    private java.util.Date updateDate;
    private String emailTrackingNumber;
    private String emailStatusMessage;
    private String bookingConfirmationId;

	/**
	 * @return Returns the appointmentId.
	 */
	public long getAppointmentId() {
		return appointmentId;
	}
	/**
	 * @param appointmentId The appointmentId to set.
	 */
	public void setAppointmentId(long appointmentId) {
		this.appointmentId = appointmentId;
	}
	/**
	 * @return Returns the status.
	 */
	public java.lang.String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	/**
	 * @return Returns the updateDate.
	 */
	public java.util.Date getUpdateDate() {
		return updateDate;
	}
	/**
	 * @param updateDate The updateDate to set.
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	
	/**
	 * @return the emailTrackingNumber
	 */
	public String getEmailTrackingNumber() {
		return emailTrackingNumber;
	}
	/**
	 * @param emailTrackingNumber the emailTrackingNumber to set
	 */
	public void setEmailTrackingNumber(String emailTrackingNumber) {
		this.emailTrackingNumber = emailTrackingNumber;
	}

	/**
	 * @return the emailStatusMessage
	 */
	public String getEmailStatusMessage() {
		return emailStatusMessage;
	}
	/**
	 * @param emailStatusMessage the emailStatusMessage to set
	 */
	public void setEmailStatusMessage(String emailStatusMessage) {
		this.emailStatusMessage = emailStatusMessage;
	}
	/**
	 * @return the bookingConfirmationId
	 */
	public String getBookingConfirmationId() {
		return bookingConfirmationId;
	}
	/**
	 * @param bookingConfirmationId the bookingConfirmationId to set
	 */
	public void setBookingConfirmationId(String bookingConfirmationId) {
		this.bookingConfirmationId = bookingConfirmationId;
	}
	
}
