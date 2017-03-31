package com.sqs.sample;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
public class SQSFIFOConsumer {

	public static void main(String[] args) {
		
		String queueName = "MyQueue.fifo";
		String message = "Three";
		
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
		
        AmazonSQSClient sqs = new AmazonSQSClient(credential);
        sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        //sqs.setSignerRegionOverride("us-west-2");
        
        String queueUrl = "";
        
        try {
        	queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        } catch (QueueDoesNotExistException e) {
        	SQSFIFOConsumer sqsSimpleSample = new SQSFIFOConsumer();
        	queueUrl = sqsSimpleSample.createQueue(sqs, queueName);
        }
        
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("FifoQueue", "true");
        attributes.put("ContentBasedDeduplication", "true");
        sqs.setQueueAttributes(queueUrl, attributes);

        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody("MESSAGE : " + message);
        sendMessageRequest.setMessageGroupId("ScheduleAppointmentRequest");
        
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
        
        String sequenceNumber = sendMessageResult.getSequenceNumber();
        String messageId = sendMessageResult.getMessageId();
        System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber + "\n");
        
	}
        
    public String createQueue(AmazonSQSClient sqs, String queueName) {
    	Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("FifoQueue", "true");
        attributes.put("ContentBasedDeduplication", "true");
        
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName).withAttributes(attributes);
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
    	return myQueueUrl;
    }

}
