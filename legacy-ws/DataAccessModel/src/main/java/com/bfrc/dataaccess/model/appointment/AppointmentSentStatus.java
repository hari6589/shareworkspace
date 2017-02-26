package com.bfrc.dataaccess.model.appointment;

import java.util.Date;

public class AppointmentSentStatus {

	private Long appointmentId;
	private String status;
	private Date updateDate;
	private String bookingConfirmationId;
	private String appointmentStatus;
	private String otherDetails;

	public Long getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Long appointmentId) {
		this.appointmentId = appointmentId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBookingConfirmationId() {
		return bookingConfirmationId;
	}

	public void setBookingConfirmationId(String bookingConfirmationId) {
		this.bookingConfirmationId = bookingConfirmationId;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}

	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}

	public String getOtherDetails() {
		return otherDetails;
	}

	public void setOtherDetails(String otherDetails) {
		this.otherDetails = otherDetails;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AppointmentSentStatus [appointmentId=" + appointmentId
				+ ", status=" + status + ", updateDate=" + updateDate
				+ ", bookingConfirmationId=" + bookingConfirmationId + ", appointmentStatus="
				+ appointmentStatus + ", otherDetails=" + otherDetails + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((appointmentStatus == null) ? 0 : appointmentStatus
						.hashCode());
		result = prime * result
				+ ((bookingConfirmationId == null) ? 0 : bookingConfirmationId.hashCode());
		result = prime * result
				+ ((otherDetails == null) ? 0 : otherDetails.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((updateDate == null) ? 0 : updateDate.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppointmentSentStatus other = (AppointmentSentStatus) obj;
		if (appointmentStatus == null) {
			if (other.appointmentStatus != null)
				return false;
		} else if (!appointmentStatus.equals(other.appointmentStatus))
			return false;
		if (bookingConfirmationId == null) {
			if (other.bookingConfirmationId != null)
				return false;
		} else if (!bookingConfirmationId.equals(other.bookingConfirmationId))
			return false;
		if (otherDetails == null) {
			if (other.otherDetails != null)
				return false;
		} else if (!otherDetails.equals(other.otherDetails))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		return true;
	}
	
}
