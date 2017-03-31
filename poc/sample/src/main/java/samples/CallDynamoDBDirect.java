package samples;

import samples.pojo.User;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

public class CallDynamoDBDirect {

	public static void main(String[] args) {
		try {
			AWSCredentials awsCredentials = new BasicAWSCredentials("AKIAIO6KVA4ID6VFURAA", "aaSNiXJVyTafsNBJfuLXp0+/KeiPbUUeel54XcKF");
	    	AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(awsCredentials);
	    	
	    	User user = new User(); 
	    	user.setId(1);
	    	user.setName("Aravind");
	    	
	        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDBClient);
			dynamoDBMapper.save(user, new DynamoDBMapperConfig(DynamoDBMapperConfig.SaveBehavior.CLOBBER));
        }  catch (Exception e) {
			System.out.println("Exception Occured while saving Appointment : " + e.getMessage());
		}
	}

}
