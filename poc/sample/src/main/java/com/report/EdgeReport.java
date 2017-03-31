package com.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.appointmentplus.model.Service;
import com.scheduleappointment.model.Appointment;

public class EdgeReport {

	public static DynamoDBMapper dynamoDBMapper = null;
	public static AmazonS3Client amazonS3Client = null;
	public static Map<Integer, String> serviceDesc = null;
	
	static {
		try {
			AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
			dynamoDBMapper = new DynamoDBMapper(new AmazonDynamoDBClient(awsCredentials));
			amazonS3Client = (awsCredentials == null) ? new AmazonS3Client() : new AmazonS3Client(awsCredentials);
		} catch (Exception e) {
			System.out.println("Exception  in EdgeReport Constructor!");
		}
	}
	
	public static void main(String[] args) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		String fileName = "bsro_ig_appointments_data."+dateFormat.format(new Date())+".txt";
		String bucketName = "bridgestone-edge-report";
		List<Appointment> appointment = new ArrayList<Appointment>();
		
		try {
			
			prepareAppointmentServiceDesc();
			
			String startDate = "2017-03-21 00:00:00";
		    String endDate = "2017-03-21 23:59:59";
		    
		    Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		    eav.put(":val2", new AttributeValue().withS(startDate));
		    eav.put(":val3", new AttributeValue().withS(endDate));

	    	DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression()
	    		.withFilterExpression("createdDate between :val2 and :val3")
		    	.withExpressionAttributeValues(eav);
	    	appointment = new ArrayList<Appointment>(dynamoDBMapper.scan(Appointment.class, dynamoDBScanExpression));
	    	System.out.println("Appointment Count: " + appointment.size());
	    	
	    	
	    	File file = new File(fileName);
	    	FileWriter fw = new FileWriter(file.getAbsoluteFile());
	    	BufferedWriter bw = new BufferedWriter(fw);
	    	
            if (!file.exists()) {
                file.createNewFile();
            }
            
	    	for(int i=0; i<appointment.size(); i++) {
	    		bw.append(objectToFormatedString(appointment.get(i)));
	    	}
	    	
            bw.close(); // Close connection
            
            //amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, file));
            
        } catch (AmazonClientException ace) {
			System.out.println("AmazonClientException occured while generating Appointment Edge Report : " + ace.getMessage());
		} catch (IOException e) {
        	System.out.println("IOException: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception occured while generating Appointment Edge Report  : " + e.getMessage());
		}
	}
	
	public static String objectToFormatedString(Appointment appointment) {
		String tempAppointmentData = "";
		tempAppointmentData += appointment.getAppointmentId() + "|";
		tempAppointmentData += appointment.getStoreNumber() != null ? appointment.getStoreNumber() + "|" : "|";
		tempAppointmentData += appointment.getVehicleYear() != null ? appointment.getVehicleYear() + "|" : "|";
		tempAppointmentData += appointment.getVehicleMake() != null ? appointment.getVehicleMake() + "|" : "|";
		tempAppointmentData += appointment.getVehicleModel() != null ? appointment.getVehicleModel() + "|" : "|";
		tempAppointmentData += appointment.getVehicleSubmodel() != null ? appointment.getVehicleSubmodel() + "|" : "|";
		tempAppointmentData += "0|";
		tempAppointmentData += "none|";
		tempAppointmentData += appointment.getFirstName() != null ? appointment.getFirstName() + "|" : "|";
		tempAppointmentData += appointment.getLastName() != null ? appointment.getLastName() + "|" : "|";
		tempAppointmentData += appointment.getAddress1() != null ? appointment.getAddress1() + "|" : "|";
		tempAppointmentData += appointment.getAddress2() != null ? appointment.getAddress2() + "|" : "|";
		tempAppointmentData += appointment.getCity() != null ? appointment.getCity() + "|" : "|";
		tempAppointmentData += appointment.getState() != null ? appointment.getState() + "|" : "|";
		tempAppointmentData += appointment.getZip() != null ? appointment.getZip() + "|" : "|";
		tempAppointmentData += appointment.getDaytimePhone() != null ? appointment.getDaytimePhone() + "|" : "|";
		tempAppointmentData += appointment.getEveningPhone() != null ? appointment.getEveningPhone() + "|" : "|";
		tempAppointmentData += appointment.getCellPhone() != null ? appointment.getCellPhone() + "|" : "|";
		tempAppointmentData += appointment.getEmailAddress() != null ? appointment.getEmailAddress() + "|" : "|";
		tempAppointmentData += appointment.getEmailSignup() != null ? appointment.getEmailSignup() + "|" : "|";
		tempAppointmentData += appointment.getCreatedDate() != null ? appointment.getCreatedDate() + "|" : "|";
		tempAppointmentData += appointment.getWebSite() != null ? appointment.getWebSite() + "|" : "|";
		tempAppointmentData += appointment.getAppointmentServices() != null ? constructAppointmentServiceDesc(appointment.getAppointmentServices()) + "|" : "|";
		//tempAppointmentData += appointment.getAppointmentChoice() != null ? appointment.getAppointmentChoice() + "|" : "|";
		tempAppointmentData += appointment.getEcommRefNumber() != null ? appointment.getEcommRefNumber() + "|" : "|";
		tempAppointmentData += "\n";
		return tempAppointmentData;
	}
	
	public static void prepareAppointmentServiceDesc() {
		serviceDesc = new HashMap<Integer, String>();
		DynamoDBScanExpression dynamoDBScanExpression = new DynamoDBScanExpression();
    	ArrayList<Service> services = new ArrayList<Service>(dynamoDBMapper.scan(Service.class, dynamoDBScanExpression));
    	for(int i=0; i < services.size(); i++) {
    		Service service = services.get(i);
    		serviceDesc.put(service.getValue(), service.getText());
    	}
	}
	
	public static String constructAppointmentServiceDesc(String serviceIds) {
		String[] serviceSplit = serviceIds.split(",");
		String appointmentServiceDescStr = "";
		int key;
		for(int i=0; i<serviceSplit.length; i++) {
			try {
				key = Integer.parseInt(serviceSplit[i].toString());
				appointmentServiceDescStr += serviceDesc.get(key);
				appointmentServiceDescStr += serviceSplit.length != i+1 ? ";" : "";
			} catch (NumberFormatException ne) {
				System.out.println("NumberFormatException: " + ne.getMessage());
			}
		}
		return appointmentServiceDescStr;
	}
	
}
