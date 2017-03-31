package samples;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.AWSLambdaClient;
import com.amazonaws.services.lambda.model.InvokeRequest;

public class CallALambdaFunction {

	private static final Log logger = LogFactory.getLog(CallALambdaFunction.class);
    private static final String awsAccessKeyId = "AKIAIO6KVA4ID6VFURAA";
    private static final String awsSecretAccessKey = "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";
    private static final String functionName = "BSROScheduleAppointmentFunction";
    private static AWSCredentials credentials;
    private static AWSLambdaClient lambdaClient;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    
	public static void main(String[] args) {
		System.out.println("Class Invokation begins : " + sdf.format(new Date()));
		credentials = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);

		lambdaClient = (credentials == null) ? new AWSLambdaClient() : new AWSLambdaClient(credentials);
		String output = "";
		
		try {
		    InvokeRequest invokeRequest = new InvokeRequest();
		    invokeRequest.setFunctionName(functionName);
		    invokeRequest.setPayload("{\"resourcePath\":\"/ws2/appointment/services\","
		    		+ "\"appName\" : \"FCAC\","
		    		+ "\"tokenId\" : \"1f04ad80-b947-fe80-32bf4a78a69d54acb\""
		    		+ "}");
		    System.out.println("Invoking a Lambda function : " + sdf.format(new Date()));
		    output = byteBufferToString(lambdaClient.invoke(invokeRequest).getPayload(),Charset.forName("UTF-8"));
		    System.out.println("Lambda invoked : " + sdf.format(new Date()));
		    System.out.println("Output : " + output);
		} catch (Exception e) {
		    logger.error(e.getMessage());
		    System.out.println("Exception : " + e.getMessage());
		}
        
        System.out.println("Class Invokation End : " + sdf.format(new Date()));
	}
	
	public static String byteBufferToString(ByteBuffer buffer, Charset charset) {
        byte[] bytes;
        if (buffer.hasArray()) {
            bytes = buffer.array();
        } else {
            bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
        }
        return new String(bytes, charset);
    }

}
