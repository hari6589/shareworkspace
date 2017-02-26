package samples;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import samples.pojo.Metadata;

public class HTTPWSPost {

	public static void main(String[] args) {
		
		Metadata mdatamdata = null;
		HttpClient httpClient = null;
		HttpPost httpPost = null;
		ObjectMapper objectMapper = new ObjectMapper();
		Metadata metadata = null;
		
		String responseBody="";
		String storeNumber = "11940";
		String serviceDesc = "Tire%20Replacement";
		
		String siteId = "appointplus846/776";
		String devApiKey = "b18371f34b0931963f62add253820169cfa05cf7";
		String liveApiKey = "123ba713955f286356423d59d03618db7ceecfc7";
		String responseType = "JSON";
		
		String queryDeli = "?";
		String nameValueDeli = "&";
		String nameValuePairDeli = "=";
		
		
		//String url = "https://sandbox-ws.appointment-plus.com/Bridgestone/Rules?store_number=011940&services=Tire%20Replacement&site_id=appointplus846/776&api_key=b18371f34b0931963f62add253820169cfa05cf7&response_type=JSON";
		String url = "https://sandbox-ws.appointment-plus.com/Bridgestone/Rules";
		
		try {
			
			httpPost = new HttpPost(url);
			
			final HttpParams httpParams = new BasicHttpParams();
			httpClient = new DefaultHttpClient(httpParams);
			
			String paddedStoreNumber = ("000000" + storeNumber);
			StringBuilder paramBuilder = new StringBuilder(); 
			paramBuilder.append(queryDeli).append("store_number").append(nameValuePairDeli).append(paddedStoreNumber.substring(paddedStoreNumber.length()-6));
			paramBuilder.append(nameValueDeli).append("services").append(nameValuePairDeli).append(serviceDesc);
			paramBuilder.append(nameValueDeli).append("site_id").append(nameValuePairDeli).append(siteId);
			paramBuilder.append(nameValueDeli).append("api_key").append(nameValuePairDeli).append(URLEncoder.encode(devApiKey,"UTF-8"));
			paramBuilder.append(nameValueDeli).append("response_type").append(nameValuePairDeli).append(URLEncoder.encode(responseType,"UTF-8"));
			
			String finalUrl = httpPost.getURI().toString() + paramBuilder.toString();
			System.out.println("finalUrl : " + finalUrl);
			
			try {
				httpPost.setURI(new URI(finalUrl));
			} catch (URISyntaxException e1) {
				System.err.println("Malformed URL while trying to build ");
			}
			
			HttpResponse response;
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			
			try {
				if(responseEntity != null) {
				    responseBody = EntityUtils.toString(responseEntity);
				    
				    JSONObject json = new JSONObject(responseBody);
					if(json.getString("data") != null) {
						responseBody = json.get("data").toString();
					}
					System.out.println("Response : " + responseBody);
				    metadata = objectMapper.readValue(responseBody, Metadata.class);
				    String str = objectMapper.writeValueAsString(metadata);
				    System.out.println("str : " + str);
				    System.out.println("mdatamdata : " + metadata.toString());
					System.out.println("jsonData : " + json.get("data").toString());
					
					httpClient.getConnectionManager().shutdown();
				}
			} catch (Exception e) {
				System.err.println("JSON Parsing Exception in parsing response from Subscribe API : " + e);
				e.printStackTrace();
			}
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
