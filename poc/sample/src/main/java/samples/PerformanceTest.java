package samples;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import samples.pojo.Metadata;

public class PerformanceTest {

	public static void main(String[] args) throws IOException {
	    final HttpGet request = new HttpGet("https://1rw1yvhop9.execute-api.us-east-1.amazonaws.com/Dev/ws2/appointment/services");
	    //https://1rw1yvhop9.execute-api.us-east-1.amazonaws.com/Dev/ws2/appointment/services
	    final int numRequests = 100;
	    final int[] histogram = new int[100];
	    final List<Integer> latencies = new ArrayList<Integer>();

	    final CloseableHttpClient client = HttpClients.createDefault();
	    
	    request.addHeader("appName" , "FCAC");
	    request.addHeader("tokenId" , "1f04ad80-b947-fe80-32bf4a78a69d54acb");
	    
	    for (int i = 0; i < numRequests; i++) {

	        long start = System.currentTimeMillis();
	        final CloseableHttpResponse resp = client.execute(request);
	        /*
	        HttpEntity responseEntity = resp.getEntity();
			
			if(responseEntity != null) {
			    String responseBody = EntityUtils.toString(responseEntity);
			    System.out.println("Response : " + responseBody);
			}
	        */
	        resp.close();
	        long millis = System.currentTimeMillis() - start;

	        if (millis > 800) {
	        	//LOG.warn
	            System.out.println(String.format("Request took %dms" + ", request ID: %s, CF request ID: %s", millis,
	                                   resp.getFirstHeader("X-Amzn-Requestid"), resp.getFirstHeader("X-Amz-Cf-Id")));
	        }

	        histogram[((int) (millis / 100))]++;
	        latencies.add((int) millis);
	    }

	    Collections.sort(latencies);

	    final int p50 = latencies.get((50 * (numRequests) / 100) - 1);
	    final int p90 = latencies.get((90 * (numRequests) / 100) - 1);
	    final int p99 = latencies.get((99 * (numRequests) / 100) - 1);
	    final int p100 = latencies.get((100 * (numRequests) / 100) - 1);

	    System.out.println(String.format("Num requests: %d P50: %dms P90: %dms P99: %dms P100: %dms", numRequests, p50, p90, p99, p100));
	    System.out.println(String.format("Distribution: "));
	    for (int i = 0; i < histogram.length; i++) {
	    	System.out.println(i*100 + "ms - " + (i+1)*100 + "ms: " + histogram[i]);
	    }
	}
}
