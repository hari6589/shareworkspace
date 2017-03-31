package com.scheduleappointment;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduleappointment.model.Appointment;
import com.scheduleappointment.model.AppointmentData;

public class AppointmentWorker {

	public static void main(String[] args) {
		System.out.println("Starting...");
		String queueName = "MyFifoQueue.fifo";
		String queueUrl = "";
		String messageBody = "";
		try {
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
			
			AmazonSQSClient sqs = new AmazonSQSClient(credential);
			//sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
			sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
			//sqs.setSignerRegionOverride("us-west-2");
			try {
				queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
			} catch (QueueDoesNotExistException e) {
				System.out.println("FIFO Queue not found!");
			}
			System.out.println("1");
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
			System.out.println("1.5");
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
			System.out.println("2 : " + messages.size());
	        for (Message message : messages) {
	        	
	        	messageBody = message.getBody();
	        	
	        	ObjectMapper mapper = new ObjectMapper();
	        	AppointmentData appointmentData = new AppointmentData();
				
				appointmentData = mapper.readValue(messageBody, AppointmentData.class);
				System.out.println("3");
				scheduleAppointment(appointmentData); 
	            
	            //String messageReceiptHandle = message.getReceiptHandle();
	            //sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
	            //sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(messageReceiptHandle));;
	        }
		} catch (JsonParseException e) {
			System.out.println("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (JsonMappingException e) {
			System.out.println("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (Throwable ex) {
	        System.err.println("Uncaught exception - " + ex.getMessage());
	        ex.printStackTrace(System.err);
	    }
	}
	
	public static void scheduleAppointment(AppointmentData appointmentData) {
		System.out.println("Scheduling Appointment..");
		AppointmentWorkerService appointmentWorkerService = new AppointmentWorkerService();
		Appointment appointment = new Appointment();
		
		try {
			// Get Customer ID
			Long customerId = appointmentWorkerService.getCustomer(appointmentData.getCustomerDayTimePhone(), appointmentData.getCustomerEmailAddress(), appointmentData.getCustomerLastName());
			System.out.println("Customer Id : " + customerId);
			if(customerId == 0L) {
				// Create a Customer and get ID
				customerId = appointmentWorkerService.createCustomer(appointmentData.getLocationId(), appointmentData.getCustomerFirstName(), 
						appointmentData.getCustomerLastName(), appointmentData.getCustomerEmailAddress(), 
						appointmentData.getCustomerDayTimePhone());
			}
			
			appointment = appointmentWorkerService.bookAppointment(appointmentData, customerId);
			
			System.out.println("Saving data into DynamoDB..");
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
	    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
	    	amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	    	
	    	appointment.setAppointmentServices(appointmentData.toString());
	    	
	        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
			dynamoDBMapper.save(appointment, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
		
		} catch (JSONException ase) {
        	System.out.println("JSONException Occured while saving Appointment : " + ase.getMessage());
		} catch (ClientProtocolException ase) {
        	System.out.println("ClientProtocolException Occured while saving Appointment : " + ase.getMessage());
		} catch (ConnectTimeoutException ase) {
        	System.out.println("ConnectTimeoutException Occured while saving Appointment : " + ase.getMessage());
        } catch (AmazonServiceException ase) {
        	System.out.println("Exception Occured while saving Appointment : " + ase.getMessage());
		} catch (AmazonClientException ace) {
			System.out.println("JSONException Occured while saving Appointment : " + ace.getMessage());
		} catch (Exception e) {
			System.out.println("Exception Occured while saving Appointment : " + e.getMessage());
		}
	}

}
