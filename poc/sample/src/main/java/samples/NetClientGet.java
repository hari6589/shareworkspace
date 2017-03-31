package samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.net.ssl.SSLSession;


public class NetClientGet {

	// http://localhost:8080/RESTfulExample/json/product/get
	public static void main(String[] args) {

	  try {

		//URL url = new URL("https://1rw1yvhop9.execute-api.us-east-1.amazonaws.com/Dev/ws2/vehicle/battery/options/year-make-model-engine/years");
		  URL url = new URL("https://1rw1yvhop9.execute-api.us-east-1.amazonaws.com/Dev/ws2/appointment/services/-proxy-");
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("tokenId","1f04ad80-b947-fe80-32bf4a78a69d54acb");
		conn.setRequestProperty("appName","FCAC");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode() + conn.getHeaderField("Location"));
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();
		
		

	  } catch (MalformedURLException e) {

		e.printStackTrace();

	  } catch (IOException e) {

		e.printStackTrace();

	  }

	}


}