/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author schowdhu
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties({"myVehicle"})
@JsonPropertyOrder({"id", "customerId", "vehicleId", "lastDownloadDate"})
public class MyServiceHistoryVehicle  {
	
	private Long myServiceHistVehicleId;	
	private MyVehicle myVehicle;
	private String year;
	private String make;
	private String model;
	private String submodel;
	private Long customerId;
	private Long vehicleId;
	private Date lastDownloadDate;
	
	private List<MyServiceHistoryInvoice> invoices = new ArrayList<MyServiceHistoryInvoice>();
	
	/**
	 * 
	 */
	public MyServiceHistoryVehicle() {
	}
	/**
	 * @return the myServiceHistVehicleId
	 */
	@JsonProperty("id")
	public Long getMyServiceHistVehicleId() {
		return myServiceHistVehicleId;
	}
	/**
	 * @param myServiceHistVehicleId the myServiceHistVehicleId to set
	 */
	@JsonProperty("id")
	public void setMyServiceHistVehicleId(Long myServiceHistVehicleId) {
		this.myServiceHistVehicleId = myServiceHistVehicleId;
	}
	/**
	 * @return the myVehicle
	 */
	public MyVehicle getMyVehicle() {
		return myVehicle;
	}
	/**
	 * @param myVehicle the myVehicle to set
	 */
	public void setMyVehicle(MyVehicle myVehicle) {
		this.myVehicle = myVehicle;
	}
	/**
	 * @return the customerId
	 */
	public Long getCustomerId() {
		return customerId;
	}
	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * @return the vehicleId
	 */
	public Long getVehicleId() {
		return vehicleId;
	}
	/**
	 * @param vehicleId the vehicleId to set
	 */
	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}
	/**
	 * @return the lastDownloadDate
	 */
	public Date getLastDownloadDate() {
		return lastDownloadDate;
	}
	/**
	 * @param lastDownloadDate the lastDownloadDate to set
	 */
	public void setLastDownloadDate(Date lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}
	
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMake() {
		return make;
	}
	public void setMake(String make) {
		this.make = make;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSubmodel() {
		return submodel;
	}
	@JsonProperty("subModel")
	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}
	/**
	 * @return the invoices
	 */
	public List<MyServiceHistoryInvoice> getInvoices() {
		return invoices;
	}
	/**
	 * @param invoices the invoices to set
	 */
	public void setInvoices(List<MyServiceHistoryInvoice> invoices) {
		this.invoices = invoices;
	}
	@Override
	public String toString() {
		return "MyServiceHistoryVehicle [myServiceHistVehicleId="
				+ myServiceHistVehicleId + ", year=" + year + ", make=" + make
				+ ", model=" + model + ", submodel=" + submodel
				+ ", customerId=" + customerId + ", vehicleId=" + vehicleId
				+ ", lastDownloadDate=" + lastDownloadDate + ", invoices="
				+ invoices + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((customerId == null) ? 0 : customerId.hashCode());
		result = prime
				* result
				+ ((lastDownloadDate == null) ? 0 : lastDownloadDate.hashCode());
		result = prime * result
				+ ((vehicleId == null) ? 0 : vehicleId.hashCode());
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
		MyServiceHistoryVehicle other = (MyServiceHistoryVehicle) obj;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (lastDownloadDate == null) {
			if (other.lastDownloadDate != null)
				return false;
		} else if (!lastDownloadDate.equals(other.lastDownloadDate))
			return false;
		if (vehicleId == null) {
			if (other.vehicleId != null)
				return false;
		} else if (!vehicleId.equals(other.vehicleId))
			return false;
		return true;
	}
	
	
}
