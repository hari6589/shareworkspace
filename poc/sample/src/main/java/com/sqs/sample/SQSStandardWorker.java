package com.sqs.sample;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.QueueDoesNotExistException;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;

public class SQSStandardWorker {

	public static void main(String[] args) {
		String queueName = "MyStandardQueue";
		
		AWSCredentials credential = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
		
        AmazonSQSClient sqs = new AmazonSQSClient(credential);
        //sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        //sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
        sqs.setSignerRegionOverride("us-east-1");
        
        String queueUrl = "";
        
        try {
        	queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        } catch (QueueDoesNotExistException e) {
        	System.out.println("Standard Queue not found!");
        }
        
        System.out.println("Standard Queue URL : " + queueUrl);
        
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl).withAttributeNames("All");
        receiveMessageRequest.setMaxNumberOfMessages(10);
        for(int i=0; i < 10; i++) {
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        System.out.println("Messages Count : " + messages.size());
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
        }
        }
        
        /*String messageReceiptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest()
            .withQueueUrl(queueUrl)
            .withReceiptHandle(messageReceiptHandle));
            */
        
	}

}
