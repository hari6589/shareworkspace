package samples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class HttpWSGet {

	public static void main(String[] args) {
		//String url = "http://dev01-api.bsro.com/ws2/promotions/specialoffers/FCAC";
		String url = "http://dev01-api-aem.bsro.com/ws2/appointment/store-services?storeNumber=23817";
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("Accept", "application/json");
		request.addHeader("tokenId", "1f04ad80-b947-fe80-32bf4a78a69d54acb");
		//request.addHeader("Authorization", authString);
		//request.addHeader("Proxy-Authorization", "http://pac.ad.csscorp.com:8080");
		
		HttpResponse response;
		try {
			response = client.execute(request);
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + 
	                       response.getStatusLine().getStatusCode());
			BufferedReader rd = new BufferedReader(
	                       new InputStreamReader(response.getEntity().getContent()));
	
			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			System.out.println("Result :: "+result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
