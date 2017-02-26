/**
 * 
 */
package com.bfrc.dataaccess.svc.webdb.appointment;

/**
 * @author schowdhu
 *
 */
@Deprecated
public class AppointmentSchedulerFault {
	
	private String detailErrorCode;
	private String detailErrorSource;
	private String detailErrorMessage;
	private String faultMessage;
	private String faultActor;
	
	public AppointmentSchedulerFault() {
	}


	public AppointmentSchedulerFault(String detailErrorCode, String faultMessage) {
		this.detailErrorCode = detailErrorCode;
		this.faultMessage = faultMessage;
	}


	public AppointmentSchedulerFault(String detailErrorCode, String detailErrorSource,
			String detailErrorMessage, String faultMessage, String faultActor) {
		this.detailErrorCode = detailErrorCode;
		this.detailErrorSource = detailErrorSource;
		this.detailErrorMessage = detailErrorMessage;
		this.faultMessage = faultMessage;
		this.faultActor = faultActor;
	}




	public String getDetailErrorCode() {
		return detailErrorCode;
	}




	public void setDetailErrorCode(String detailErrorCode) {
		this.detailErrorCode = detailErrorCode;
	}




	public String getDetailErrorSource() {
		return detailErrorSource;
	}




	public void setDetailErrorSource(String detailErrorSource) {
		this.detailErrorSource = detailErrorSource;
	}




	public String getDetailErrorMessage() {
		return detailErrorMessage;
	}




	public void setDetailErrorMessage(String detailErrorMessage) {
		this.detailErrorMessage = detailErrorMessage;
	}




	public String getFaultMessage() {
		return faultMessage;
	}


	public void setFaultMessage(String faultMessage) {
		this.faultMessage = faultMessage;
	}

	public String getFaultActor() {
		return faultActor;
	}

	public void setFaultActor(String faultActor) {
		this.faultActor = faultActor;
	}

}
