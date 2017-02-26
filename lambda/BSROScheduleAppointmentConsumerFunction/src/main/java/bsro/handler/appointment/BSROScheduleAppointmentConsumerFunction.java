package bsro.handler.appointment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class BSROScheduleAppointmentConsumerFunction implements RequestHandler<Object, Object> {

	static final String GET_SERVICE = "services";
	static final String GET_METADATA = "metadata";
	static final String GET_DAY = "days";
	static final String GET_TIME = "times";
	static final String CREATE_BOOK = "book";
	
	public Object handleRequest(Object input, Context context) {
		
		backup1();
		
		HashMap<String, String> params = (HashMap<String, String>) input;
		String resourcePathParam = params.get("resource_path").toString();
		
		String serviceFunction = resourcePathParam.substring(resourcePathParam.lastIndexOf("/") + 1);
				
		switch(serviceFunction) {
			case GET_SERVICE:
				return GET_SERVICE;
			case GET_METADATA:
				return GET_METADATA;
			case GET_DAY:
				return GET_DAY;	
			case GET_TIME:
				return GET_TIME;
			case CREATE_BOOK:
				return CREATE_BOOK;
			default:
				return "Method not matched with the services";
		}
	}
	
	public static void backup2() {
		Appointment appointment = new Appointment();
		
		
		appointment.setAppointmentId(12346L);
		appointment.setCustomerId(1235L);
		appointment.setStoreNumber(11940L);
		appointment.setVehicleYear(2017L);
		appointment.setVehicleMake("vehicle_make");
		appointment.setVehicleModel("vehicle_model");
		appointment.setVehicleSubmodel("vehicle_sub_model");
		appointment.setMileage(105);
		appointment.setComments("comments");
		appointment.setFirstName("first_name");
		appointment.setLastName("last_name");
		appointment.setEmailAddress("email_address");
		appointment.setAddress1("address_1");
		appointment.setAddress2("address_2");
		appointment.setState("state");
		appointment.setCity("city");
		appointment.setDaytimePhone("day_time_phone");
		appointment.setEveningPhone("evening_phone");
		appointment.setCellPhone("cell_phone");
		appointment.setEmailAddress("email_address");
		appointment.setEmailSignup("email_signup");
		appointment.setCreatedDate(new Date());
		appointment.setWebSite("web_site");
		appointment.setWebSiteSource("");
		appointment.setAppointmentChoiceConfirmed(2);
		appointment.setBatteryQuoteId(104L);
		appointment.setPhoneReminderInd("phone_reminder_ind");
		appointment.setEmailReminderInd("email_reminder_ind");
		appointment.setChoice(1);
		appointment.setDateTime(new Date());
		appointment.setDropWaitOption("drop_wait_options");
		appointment.setPickupTime(new Date());
		appointment.setDropOffTime(new Date());
		appointment.setAppointmentServices("2047,2548");
		appointment.setEmployeeId(103L);
		appointment.setRoomId(102L);
		appointment.setLocationId(101L);
		appointment.setAppointmentStatusId(100L);
		appointment.setOtherDetails("other_detail");
		appointment.setUpdateDate(new Date());
		appointment.setBookingConfirmationId("booking_confirmation_id");
		appointment.setEmailStatusMessage("email_status_message");
		appointment.setEmailTrackingNumber("email_tracking_number");
		
		
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credential);
    	amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
    	
        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
		
		try {
			dynamoDBMapper.save(appointment, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
        } catch (AmazonServiceException ase) {
        	//context.getLogger().log("Exception Occured while saving Appointment : " + ase.getMessage());
			//return "AmazonServiceException";
        } catch (AmazonClientException ace) {
        	//context.getLogger().log("Exception Occured while saving Appointment : " + ace.getMessage());
			//return "AmazonClientException";
        }
	}
	
	public static void backup1() {
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
    	
        AmazonSQSClient sqs = new AmazonSQSClient(credential);
        sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        //sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
        
        String queueUrl = sqs.getQueueUrl("MyFifoQueue.fifo").getQueueUrl();
        
        Map<String, String> attributes = new HashMap<String, String>();
        // A FIFO queue must have the FifoQueue attribute set to True
        attributes.put("FifoQueue", "true");
        // Generate a MessageDeduplicationId based on the content, if the user doesn't provide a MessageDeduplicationId
        attributes.put("ContentBasedDeduplication", "true");
        sqs.setQueueAttributes(queueUrl, attributes);
        
/*        SendMessageRequest request = new SendMessageRequest();
        request.withMessageBody("A test message body.");
        request.withQueueUrl("MyQueueUrlStringHere");
        //request.withMessageAttributes(messageAttributes);
        SendMessageResult sendMessageResult = sqs.sendMessage(request);*/
        
     // Send a message
        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, "This is my message text.");
        // You must provide a non-empty MessageGroupId when sending messages to a FIFO queue
        sendMessageRequest.setMessageGroupId("messageGroup1");
        sendMessageRequest.setMessageBody("Message Body Content");
        // Uncomment the following to provide the MessageDeduplicationId
        //sendMessageRequest.setMessageDeduplicationId("1");
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
        //String sequenceNumber = sendMessageResult.getSequenceNumber();
        String messageId = sendMessageResult.getMessageId();
		//return sendMessageResult.getMD5OfMessageBody();
	}

}
