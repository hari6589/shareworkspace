package samples.pojo;

import org.codehaus.jackson.annotate.JsonProperty;

public class Metadata {

	private String locationId;
	private String serviceId;
	private String statusId;
	private String statusDescription;
	private String employeeId;
	private String additionalServices;
	
	@JsonProperty("c_id")
	public String getLocationId() {
		return locationId;
	}
	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}
	@JsonProperty("service_id")
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	@JsonProperty("status_id")
	public String getStatusId() {
		return statusId;
	}
	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}
	@JsonProperty("status_description")
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	@JsonProperty("employee_id")
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	@JsonProperty("addons")
	public String getAdditionalServices() {
		return additionalServices;
	}
	public void setAdditionalServices(String additionalServices) {
		this.additionalServices = additionalServices;
	}
	
	@Override
	public String toString() {
		return "Metadata [locationId=" + locationId + ", serviceId="
				+ serviceId + ", statusId=" + statusId + ", statusDescription="
				+ statusDescription + ", employeeId=" + employeeId
				+ ", additionalServices=" + additionalServices + "]";
	}
	
	
//	private String c_id;
//	private String service_id;
//	private String status_id;
//	private String status_description;
//	private String employee_id;
//	private String addons;
	
		
}
