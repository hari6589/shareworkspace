/**
 * 
 */
package app.bsro.model.appointment;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * @author schowdhu
 *
 */
public class AppointmentMetaData {
	
	private String locationId;
	private String serviceId;
	private String statusId;
	private String statusDescription;
	private String employeeId;
	private String additionalServices;
	
	/**
	 * 
	 */
	public AppointmentMetaData() {
	}
	

	/**
	 * @return the locationId
	 */
	@JsonProperty("locationId")
	public String getLocationId() {
		return locationId;
	}

	/**
	 * @param locationId the locationId to set
	 */
	@JsonProperty("c_id")
	public void setC_Id(String locationId) {
		this.locationId = locationId;
	}

	/**
	 * @return the serviceId
	 */
	@JsonProperty("serviceId")
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	@JsonProperty("service_id")
	public void setService_Id(String serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the statusId
	 */
	@JsonProperty("statusId")
	public String getStatusId() {
		return statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	@JsonProperty("status_id")
	public void setStatus_Id(String statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the statusDescription
	 */
	@JsonProperty("statusDescription")
	public String getStatusDescription() {
		return statusDescription;
	}


	/**
	 * @param statusDescription the statusDescription to set
	 */
	@JsonProperty("status_description")
	public void setStatus_Description(String statusDescription) {
		this.statusDescription = statusDescription;
	}


	/**
	 * @return the employeeId
	 */
	@JsonProperty("employeeId")
	public String getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	@JsonProperty("employee_id")
	public void setEmployee_Id(String employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the additionalServices
	 */
	@JsonProperty("additionalServices")
	public String getAdditionalServices() {
		return additionalServices;
	}

	/**
	 * @param addons the additionalServices to set
	 */
	@JsonProperty("addons")
	public void setAddons(String additionalServices) {
		if(additionalServices !=null &&additionalServices.length()>0)
			this.additionalServices = additionalServices;
		else
			this.additionalServices = "";
	}


	@Override
	public String toString() {
		return "AppointmentMetaData [locationId=" + locationId + ", serviceId="
				+ serviceId + ", statusId=" + statusId + ", statusDescription="
				+ statusDescription + ", employeeId=" + employeeId
				+ ", additionalServices=" + additionalServices + "]";
	}

}
