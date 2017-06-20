package com.bridgestone.bsro.aws.appointment.util;

import com.bridgestone.bsro.aws.appointment.util.SecurityAuthentication;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class AuthenticateService {
	public boolean authenticateService(String tokenId, String appName){
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
		//AWSCredentials credential = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
    	DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
    	
		if(tokenId!=null && appName!=null && !tokenId.equals("") && !appName.equals("")) {
			SecurityAuthentication securityObject = dynamoDBMapper.load(SecurityAuthentication.class, tokenId, 
	                new DynamoDBMapperConfig(DynamoDBMapperConfig.ConsistentReads.CONSISTENT));
			if(securityObject!=null && appName.equalsIgnoreCase(securityObject.getAppName())){
				return true;
			}
		}
		return false;
	}
}
