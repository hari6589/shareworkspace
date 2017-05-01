package com.bridgestone.bsro.aws.mobile.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="SequenceNumber")
public class SequenceNumber {
	
	private int id;
	private Long batteryQuoteId;
	private Long tireQuoteId;
	private Long userId;
	
	@DynamoDBHashKey(attributeName="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@DynamoDBAttribute(attributeName="batteryQuoteId")  
	public Long getBatteryQuoteId() {
		return batteryQuoteId;
	}
	public void setBatteryQuoteId(Long batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}
	
	@DynamoDBAttribute(attributeName="tireQuoteId")  
	public Long getTireQuoteId() {
		return tireQuoteId;
	}
	public void setTireQuoteId(Long tireQuoteId) {
		this.tireQuoteId = tireQuoteId;
	}
	
	@DynamoDBAttribute(attributeName="userId")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
