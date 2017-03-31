package com.appointmentplus.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="AppointmentService")
public class Service implements Comparable<Service> {
	
	private int value;
	private String text;
	private String serviceCategory;
	private String vehicleRequiredInd;
	private int serviceType;
	private int sortOrder;
	
	@DynamoDBHashKey(attributeName="ServiceId")
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	@DynamoDBAttribute(attributeName="ServiceDesc")
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	@DynamoDBIgnore
	@DynamoDBAttribute(attributeName="ServiceCategory")
	public String getServiceCategory() {
		return serviceCategory;
	}
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}
	
	@DynamoDBIgnore
	@DynamoDBAttribute(attributeName="VehicleRequiredInd")
	public String getVehicleRequiredInd() {
		return vehicleRequiredInd;
	}
	public void setVehicleRequiredInd(String vehicleRequiredInd) {
		this.vehicleRequiredInd = vehicleRequiredInd;
	}
	
	@DynamoDBIgnore
	@DynamoDBAttribute(attributeName="ServiceType")
	public int getServiceType() {
		return serviceType;
	}
	public void setServiceType(int serviceType) {
		this.serviceType = serviceType;
	}
	
	@DynamoDBAttribute(attributeName="SortOrder")
	public int getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	//@Override
	public int compareTo(Service service) {
		int sortOrder = ((Service) service).getSortOrder();
		return this.sortOrder - sortOrder; //ascending order
		//return sortOrder - this.sortOrder; //descending order
	}
	
}
