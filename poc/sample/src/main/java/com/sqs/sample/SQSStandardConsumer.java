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
public class SQSStandardConsumer {

	public static void main(String[] args) {
		
		String queueName = "MyStandardQueue";
		String message = "Three";

		AWSCredentials credential = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
		
        AmazonSQSClient sqs = new AmazonSQSClient(credential);
        //sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        sqs.setSignerRegionOverride("us-east-1");
        
        String queueUrl = "";
        
        try {
        	queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        } catch (QueueDoesNotExistException e) {
        	SQSStandardConsumer sqsSimpleSample = new SQSStandardConsumer();
        	queueUrl = sqsSimpleSample.createQueue(sqs, queueName);
        }
        
        SendMessageResult sendMessageResult = sqs.sendMessage(new SendMessageRequest()
        	.withQueueUrl(queueUrl)
        	.withMessageBody("This is my message text 2"));
        
        String sequenceNumber = sendMessageResult.getSequenceNumber();
        String messageId = sendMessageResult.getMessageId();
        System.out.println("SendMessage succeed with messageId " + messageId + ", sequence number " + sequenceNumber + "\n");
        
	}
        
    public String createQueue(AmazonSQSClient sqs, String queueName) {
    	Map<String, String> attributes = new HashMap<String, String>();
        //attributes.put("FifoQueue", "true");
        //attributes.put("ContentBasedDeduplication", "true");
        
        CreateQueueRequest createQueueRequest = new CreateQueueRequest(queueName).withAttributes(attributes);
        String myQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();
        
    	return myQueueUrl;
    }

}
