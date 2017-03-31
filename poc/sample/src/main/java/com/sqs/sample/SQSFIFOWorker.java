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

public class SQSFIFOWorker {

	public static void main(String[] args) {
		String queueName = "MyFifoQueue.fifo";
		
		AWSCredentials credential = new BasicAWSCredentials("AKIAJN6CNV7NFM3MJM3Q", "cTls28RIJdrw3gBAsm8u/FUR9Jo6VA7iFcH+u1ck");
		
        AmazonSQSClient sqs = new AmazonSQSClient(credential);
        //sqs.setRegion(Region.getRegion(Regions.US_WEST_2));
        sqs.setEndpoint("https://sqs.us-west-2.amazonaws.com");
        //sqs.setSignerRegionOverride("us-west-2");
        
        String queueUrl = "";
        
        try {
        	queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
        } catch (QueueDoesNotExistException e) {
        	System.out.println("FIFO Queue not found!");
        }
        
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        
        System.out.println("FIFO Queue URL : " + queueUrl);
        
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
            System.out.println("Message");
            System.out.println("MessageId:     " + message.getMessageId());
            System.out.println("ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
          String messageReceiptHandle = message.getReceiptHandle();
          sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
          sqs.deleteMessage(new DeleteMessageRequest().withQueueUrl(queueUrl).withReceiptHandle(messageReceiptHandle));;
        }
        
//      String messageReceiptHandle = messages.get(0).getReceiptHandle();
//      sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageReceiptHandle));
        
	}

}
