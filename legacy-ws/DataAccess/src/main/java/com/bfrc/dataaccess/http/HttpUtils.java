package com.bfrc.dataaccess.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

//import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.SingleClientConnManager;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import com.bfrc.dataaccess.exception.HttpException;
import com.bfrc.dataaccess.http.HttpConstants.ContentType;
import com.bfrc.dataaccess.http.HttpConstants.MessageCharset;

/**
 * Utility class to perform HTTP calls.
 * @author Brad Balmer
 *
 */
public class HttpUtils {
	
	private static Logger log = Logger.getLogger(HttpUtils.class.getName());
	
	/**
	 * Send a POST message returning the raw text response
	 * @param url
	 * @param message
	 * @param headerParams
	 * @return {@link HttpResponse}
	 */
	public static com.bfrc.dataaccess.http.HttpResponse post(String url, String message, Map<String, String> headerParams) throws HttpException {
		return post(url, message, headerParams);
	}
	
	/**
	 * Send a POST message returning the raw text response 
	 * @param url Full URL of the endpoint
	 * @param message String body to be set
	 * @param messageMimeType ContentType of the message body (optional) {@link ContentType}
	 * @param messageCharset Characterset of the message body (optional) {@link MessageCharset}
	 * @param headerParams Any header parameters needed to be set (optional) {@link HttpHeaderParameters}
	 * @return {@link HttpResponse}
	 */
	public static com.bfrc.dataaccess.http.HttpResponse post(String url, String message, ContentType messageMimeType, MessageCharset messageCharset, Map<String, String> headerParams) throws HttpException {

		//This is the response that will be returned back to the calling function
		com.bfrc.dataaccess.http.HttpResponse httpResponse = null;
		
		//This is the response that is returned back from the HttpClient call
		org.apache.http.HttpResponse response = null;
		
		//Base, default, HttpClient
		HttpClient httpclient = new DefaultHttpClient();
        
        try {           

        	/*
        	 * Currently do not need SSL but the code is here just in case.
        	 */
//			SSLSocketFactory sf = new SSLSocketFactory(new TrustStrategy() {
//
//				public boolean isTrusted(X509Certificate[] arg0, String arg1)
//						throws CertificateException {
//					return true;
//				}
//			});
//
//			SchemeRegistry registry = new SchemeRegistry();
//			registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
//			registry.register(new Scheme("https", 443, sf));
//
//			ClientConnectionManager ccm = new ThreadSafeClientConnManager(registry);
//
//			HttpClient httpclient = new DefaultHttpClient(ccm);
        	
        	
            HttpPost httppost = new HttpPost(url);
            if(headerParams != null) {
            	Iterator<String> iter = headerParams.keySet().iterator();
            	while(iter.hasNext()) {
            		String name = iter.next();
            		String value = headerParams.get(name);
            		
            		log.fine("Setting Header parameter: "+name+"-"+value);
            		
            		if(name != null && value != null)
                        httppost.setHeader(name, value);
            	}
            }
            
            HttpEntity entity = null;
    		log.fine("SENDING: "+message);
            
            if(messageCharset != null) {
            	if(messageMimeType != null)
            		entity = new StringEntity(message, messageMimeType.getContentType(), messageCharset.getCharset());
            	else
            		entity = new StringEntity(message, messageCharset.getCharset());
            } else 
            	entity = new StringEntity(message);
            
            httppost.setEntity(entity);

            response =  httpclient.execute(httppost);
         
            HttpEntity resEntity = response.getEntity();
            String responseBody = "";
            if (resEntity != null) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line = "";
    			while ((line = reader.readLine()) != null) {
    				builder.append(line);
    			}
                responseBody = builder.toString();
            }
            EntityUtils.consume(resEntity);
          
	        responseBody = StringUtils.trimToEmpty(responseBody);
	      
	        httpResponse = new com.bfrc.dataaccess.http.HttpResponse(response.getStatusLine().getStatusCode(), responseBody);
        }catch(UnsupportedEncodingException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-UnsupportedEncodingException: "+e.getMessage());
        	throw new HttpException(e);
        } catch(ClientProtocolException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-ClientProtocolException: "+e.getMessage());
        	throw new HttpException(e);
        } catch(IOException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-IOException: "+e.getMessage());
        	throw new HttpException(e);
        }catch(Exception e) {
        	log.severe("["+HttpUtils.class.getName()+"]-Exception: "+e.getMessage());
        	throw new HttpException(e);
        } finally {
            // Shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
		log.fine("RECEIVING: "+httpResponse);
        return httpResponse;
	}
	
	
	public static com.bfrc.dataaccess.http.HttpResponse get(String uri, Map<String, String> headerParams) throws HttpException {

		com.bfrc.dataaccess.http.HttpResponse httpResponse = null;
		HttpClient httpclient = new DefaultHttpClient();
		String responseBody = "";
         
		try {
        	HttpGet get = new HttpGet(uri);
            if(headerParams != null) {
            	Iterator<String> iter = headerParams.keySet().iterator();
            	while(iter.hasNext()) {
            		String name = iter.next();
            		String value = headerParams.get(name);
            		
            		log.fine("Setting Header parameter: "+name+"-"+value);
            		
            		if(name != null && value != null)
                        get.setHeader(name, value);
            	}
            }
        	
        	
        	
        	HttpResponse response = httpclient.execute(get);
            
            HttpEntity resEntity = response.getEntity();
            
            if (resEntity != null) {
                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(resEntity.getContent()));
                String line = "";
    			while ((line = reader.readLine()) != null) {
    				builder.append(line);
    			}
                responseBody = builder.toString();
            }
            EntityUtils.consume(resEntity);
          
	        responseBody = StringUtils.trimToEmpty(responseBody);
	        httpResponse = new com.bfrc.dataaccess.http.HttpResponse(response.getStatusLine().getStatusCode(), responseBody);
	        
        }catch(UnsupportedEncodingException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-UnsupportedEncodingException: "+e.getMessage());
        	throw new HttpException(e);
        } catch(ClientProtocolException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-ClientProtocolException: "+e.getMessage());
        	throw new HttpException(e);
        } catch(IOException e) {
        	log.severe("["+HttpUtils.class.getName()+"]-IOException: "+e.getMessage());
        	throw new HttpException(e);
        }catch(Exception e) {
        	log.severe("["+HttpUtils.class.getName()+"]-Exception: "+e.getMessage());
        	throw new HttpException(e);
        } finally {
            // Shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }
        return httpResponse;
	}
	
	
}
