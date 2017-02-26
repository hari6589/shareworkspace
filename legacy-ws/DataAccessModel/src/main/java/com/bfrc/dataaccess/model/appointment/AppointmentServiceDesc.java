package com.bfrc.dataaccess.model.appointment;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;


@JsonIgnoreProperties({"categories"})
@JsonPropertyOrder({"serviceId","serviceDesc","serviceCategory","sortOrder"})
public class AppointmentServiceDesc {

	private static final long serialVersionUID = 1L;
	private Integer serviceId;
	private String serviceDesc;
	private Integer serviceType;
	private Integer sortOrder;
	
	private String assignedCategory;
	
	private List<AppointmentServiceCategory> categories;

	public AppointmentServiceDesc() {
	}

	public AppointmentServiceDesc(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public AppointmentServiceDesc(Integer serviceId, String serviceDesc) {
		this.serviceId = serviceId;
		this.serviceDesc = serviceDesc;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Integer getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceDesc() {
		return this.serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	@JsonProperty("serviceCategory")
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_EMPTY)
	public String getAssignedCategory() {
		return assignedCategory;
	}
	@JsonProperty("serviceCategory")
	public void setAssignedCategory(String assignedCategory) {
		this.assignedCategory = assignedCategory;
	}

	public List<AppointmentServiceCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<AppointmentServiceCategory> categories) {
		this.categories = categories;
	}

}
