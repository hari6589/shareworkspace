package com.bridgestone.bsro.aws.appointment.handler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bridgestone.bsro.aws.appointment.model.Appointment;
import com.bridgestone.bsro.aws.appointment.model.AppointmentData;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponse;
import com.bridgestone.bsro.aws.appointment.webservice.BSROWebServiceResponseCode;
import com.bridgestone.bsro.aws.appointment.services.impl.AppointmentWorkerService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class BSROScheduleAppointmentWorkerFunctionHandler implements RequestHandler<Object, Object>  {
	
	public Object handleRequest(Object input, Context context) {
		BSROWebServiceResponse bsroWebServiceResponse = new BSROWebServiceResponse();
		String queueName = "MyFifoQueue.fifo";
		String queueUrl = "";
		String messageBody = "";
		try {
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
			
			AmazonSQSClient sqs = new AmazonSQSClient(credential);
			//sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
			sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
			//sqs.setEndpoint("https://sqs.us-east-1.amazonaws.com");
			//sqs.setSignerRegionOverride("us-west-2");
			try {
				queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
			} catch (QueueDoesNotExistException e) {
				System.out.println("FIFO Queue not found!");
			}
			
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
			
			List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	        for (Message message : messages) {
	        	
	        	messageBody = message.getBody();
	        	
	        	ObjectMapper mapper = new ObjectMapper();
	        	AppointmentData appointmentData = new AppointmentData();
				
				appointmentData = mapper.readValue(messageBody, AppointmentData.class);
				scheduleAppointment(appointmentData, context); 
	            
	            String messageReceiptHandle = message.getReceiptHandle();
	            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
	            sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(messageReceiptHandle));;
	        }
		} catch (JsonParseException e) {
			context.getLogger().log("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (JsonMappingException e) {
			context.getLogger().log("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		} catch (IOException e) {
			context.getLogger().log("Exception Occured while Schedule Appointment - Worker : " + e.getMessage());
		}
		return bsroWebServiceResponse;
	}
	
	public void scheduleAppointment(AppointmentData appointmentData, Context context) {

		AppointmentWorkerService appointmentWorkerService = new AppointmentWorkerService();
		Appointment appointment = new Appointment();
		
		try {
			// Get Customer ID
			Long customerId = appointmentWorkerService.getCustomer(appointmentData.getCustomerDayTimePhone(), appointmentData.getCustomerEmailAddress(), appointmentData.getCustomerLastName());
			
			if(customerId == 0L) {
				// Create a Customer and get ID
				customerId = appointmentWorkerService.createCustomer(appointmentData.getLocationId(), appointmentData.getCustomerFirstName(), 
						appointmentData.getCustomerLastName(), appointmentData.getCustomerEmailAddress(), 
						appointmentData.getCustomerDayTimePhone());
			}
			
			appointment = appointmentWorkerService.bookAppointment(appointmentData, customerId);
			
			AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
	    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
	    	amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
	    	//amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_EAST_1));
	    	
	        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
			dynamoDBMapper.save(appointment, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
			
        } catch (AmazonServiceException ase) {
        	context.getLogger().log("Exception Occured while saving Appointment : " + ase.getMessage());
		} catch (AmazonClientException ace) {
        	context.getLogger().log("Exception Occured while saving Appointment : " + ace.getMessage());
		} catch (ConnectTimeoutException e) {
        	context.getLogger().log("ConnectTimeoutException Occured while saving Appointment : " + e.getMessage());
		} catch (ClientProtocolException e) {
			context.getLogger().log("ClientProtocolException Occured while saving Appointment : " + e.getMessage());
		} catch (JSONException e) {
			context.getLogger().log("JSONException Occured while saving Appointment : " + e.getMessage());
		} catch (Exception e) {
			context.getLogger().log("Exception Occured while saving Appointment : " + e.getMessage());
		}
	}

}
