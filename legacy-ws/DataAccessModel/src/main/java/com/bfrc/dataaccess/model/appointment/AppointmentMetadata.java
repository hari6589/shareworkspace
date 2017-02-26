package com.bfrc.dataaccess.model.appointment;

import java.util.Date;

public class AppointmentMetadata {
	
    private Long appointmentId;

    private Long locationId;

    private Long employeeId;

    private String servicesCSV;

    private Long appointmentStatusId;

    private String otherDetails;

    private Date createdDate;

	/**
	 * @return the appointmentId
	 */
	public Long getAppointmentId() {
		return appointmentId;
	}

	/**
	 * @param appointmentId the appointmentId to set
	 */
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}

	/**
	 * @return the locationId
	 */
	public Long getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	public void setLocationId(Long locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the servicesCSV
	 */
	public String getServicesCSV() {
		return servicesCSV;
	}

	/**
	 * @param servicesCSV the servicesCSV to set
	 */
	public void setServicesCSV(String servicesCSV) {
		this.servicesCSV = servicesCSV;
	}

	/**
	 * @return the appointmentStatusId
	 */
	public Long getAppointmentStatusId() {
		return appointmentStatusId;
	}

	/**
	 * @param appointmentStatusId the appointmentStatusId to set
	 */
	public void setAppointmentStatusId(Long appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	/**
	 * @return the otherDetails
	 */
	public String getOtherDetails() {
		return otherDetails;
	}

	/**
	 * @param otherDetails the otherDetails to set
	 */
	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
