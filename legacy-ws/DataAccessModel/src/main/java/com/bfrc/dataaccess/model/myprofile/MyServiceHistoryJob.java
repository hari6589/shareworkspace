/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.math.BigDecimal;
import java.util.ArrayList;
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
@JsonIgnoreProperties({"serviceHistoryInvoice"})
@JsonPropertyOrder({"id", "authorized", "total", "description"})
public class MyServiceHistoryJob {
	
	private Long myServiceHistJobId;
	
	private MyServiceHistoryInvoice serviceHistoryInvoice;
	
	private String authorized;
	
	private BigDecimal total;
	
	private String description;
	
	private List<MyServiceHistoryJobDetail> details = new ArrayList<MyServiceHistoryJobDetail>(100);
	

	public MyServiceHistoryJob() {
	}
	/**
	 * @return the myServiceHistJobId
	 */
	@JsonProperty("id")
	public Long getMyServiceHistJobId() {
		return myServiceHistJobId;
	}
	/**
	 * @param myServiceHistJobId the myServiceHistJobId to set
	 */
	@JsonProperty("id")
	public void setMyServiceHistJobId(Long myServiceHistJobId) {
		this.myServiceHistJobId = myServiceHistJobId;
	}
	/**
	 * @return the serviceHistoryInvoice
	 */
	public MyServiceHistoryInvoice getServiceHistoryInvoice() {
		return serviceHistoryInvoice;
	}
	/**
	 * @param serviceHistoryInvoice the serviceHistoryInvoice to set
	 */
	public void setServiceHistoryInvoice(
			MyServiceHistoryInvoice serviceHistoryInvoice) {
		this.serviceHistoryInvoice = serviceHistoryInvoice;
	}

	/**
	 * @return the authorized
	 */
	public String getAuthorized() {
		return authorized;
	}
	/**
	 * @param authorized the authorized to set
	 */
	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}
	/**
	 * @return the total
	 */
	public BigDecimal getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the details
	 */
	public List<MyServiceHistoryJobDetail> getDetails() {
		return details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(List<MyServiceHistoryJobDetail> details) {
		this.details = details;
	}
	@Override
	public String toString() {
		return "MyServiceHistoryJob [myServiceHistJobId=" + myServiceHistJobId
				+ ", authorized=" + authorized + ", total=" + total
				+ ", description=" + description + ", details=" + details + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorized == null) ? 0 : authorized.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		MyServiceHistoryJob other = (MyServiceHistoryJob) obj;
		if (authorized == null) {
			if (other.authorized != null)
				return false;
		} else if (!authorized.equals(other.authorized))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
	

}
