package com.bridgestone.bsro.aws.appointment.util;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="SecurityAuthentication")
public class SecurityAuthentication {
	
	private String tokenId;
	private String appName;
	
	@DynamoDBHashKey(attributeName="tokenId")  
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	} 
	
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
}
