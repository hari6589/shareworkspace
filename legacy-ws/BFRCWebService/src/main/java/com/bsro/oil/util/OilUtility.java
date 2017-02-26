package com.bsro.oil.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.owasp.esapi.Logger;

import com.bsro.oil.exception.OilDataException;




/**
 * Miscellaneous utility class to address various items necessary to perform Oil Funnel related items
 * 
 * @author Ramana
 *
 */
public class OilUtility {
	
	private static final Log logger = LogFactory.getLog(OilUtility.class);

	public static String getWebServiceResponse(String url) throws IOException
	{
		java.net.URL url1 = null;
		java.net.URLConnection conn = null;
		BufferedReader dis = null;
		
		try {
			
			if (url == null || url.equalsIgnoreCase(""))
			{
				// Problem...malformed URL.  Throw error here
				throw new OilDataException("URL is not a valid URL.");
				
			}

			url1 = new java.net.URL(url);
			conn = url1.openConnection();
			
			if (conn == null ){
				throw new OilDataException("Unable to connect LOF Web Service");
			}
			
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			conn.setDoOutput(true);
			conn.setRequestProperty("content-type", "application/xml");
			conn.setRequestProperty("Accept", "application/xml");

			StringBuffer xmlResponse = new StringBuffer();
			
			String inputLine;
			dis = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((inputLine = dis.readLine()) != null) {
				xmlResponse.append(inputLine + "\n");
			}
			if (xmlResponse != null) {
				 // Remove special character(&) from xml response file 
				return xmlResponse.toString().replaceAll("&", "&amp;");
				
			} else {
				// Received no data from the webservice and throw an exception.
				throw new OilDataException("No Data retrieved from LOF Backend Web Service");
			}
			
		}  catch(OilDataException oilDataException){
			logger.error("Unable to establish connection to Web Service or No Data is returned from the Web Service",oilDataException );
			oilDataException.printStackTrace();
			
		}  finally {
			
			try {
				if (dis != null)
					dis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}
}
