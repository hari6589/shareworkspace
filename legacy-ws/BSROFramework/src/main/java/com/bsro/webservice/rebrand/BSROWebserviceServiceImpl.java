package com.bsro.webservice.rebrand;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import app.bsro.model.webservice.BSROWebServiceRequest;
import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.framework.util.Util;
import com.bsro.errors.Errors;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceService;

public class BSROWebserviceServiceImpl implements BSROWebserviceService {
	Log logger = LogFactory.getLog(BSROWebserviceServiceImpl.class);
	
	private static final String WEBSERVICE_TOKEN_ID = "tokenId";
	private static final String WEBSERVICE_APP_NAME = "appName";
	
	protected static final String PATH_WEBSERVICE_BASE = "/ws2";
	
	protected static final String PATH_DELIMITER = "/";
	
	private static final String QUERY_STRING_BEGIN = "?";
	private static final String QUERY_STRING_NAME_VALUE_DELIMITER = "=";
	private static final String QUERY_STRING_NAME_VALUE_PAIR_DELIMITER = "&";
	
	private static final String REQUEST_METHOD_POST = "POST";
	private static final String REQUEST_METHOD_GET = "GET";
	
	private BSROWebserviceConfig webServiceConfig;
	
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		if (bsroWebserviceConfig != null) {
			this.logger.info("Initializing BSRO webservice URL: "+bsroWebserviceConfig.getBaseURL());
		} else {
			this.logger.error("BSROWebserviceConfig is missing!");
		}
		this.webServiceConfig = bsroWebserviceConfig;
	}
	
	protected <T> T getWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET, errors);
	}
	
	protected <T> T postWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST, errors);
	}
	
	protected <T> T postJSONToWebservice(String webservicePath, Object jsonObject, Class<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, null, jsonObject, expectedReturnType, REQUEST_METHOD_POST, errors);
	}
	protected <T> T postJSONToWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, parameters, jsonObject, expectedReturnType, REQUEST_METHOD_POST, errors);
	}
	
	private <T> T callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, String requestMethod, Errors errors) throws IOException {		
		
		BSROWebServiceResponse response = callWebservice(webservicePath, parameters, jsonObject, requestMethod);
		if (response != null) {
			if(logger.isInfoEnabled()) logger.info("Received a response! : " + response);
			ObjectMapper mapper = new ObjectMapper();
			if (errors != null && response.getErrors() != null) {
				errors.getGlobalErrors().addAll(response.getErrors().getGlobalErrors());
				errors.getFieldErrors().putAll(response.getErrors().getFieldErrors());
			}
			return (T) mapper.convertValue(response.getPayload(), expectedReturnType);
		}
		return null;
	}
		
	protected <T> T getWebservice(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET, errors);
	}
	
	protected <T> T postWebservice(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST, errors);
	}
	
	protected <T> T postJSONToWebservice(String webservicePath, Object jsonObject, TypeReference<T> expectedReturnType, Errors errors) throws IOException {
		return callWebservice(webservicePath, null, jsonObject, expectedReturnType, REQUEST_METHOD_POST, errors);
	}
	
	private <T> T callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, TypeReference<T> expectedReturnType, String requestMethod, Errors errors) throws IOException {
		BSROWebServiceResponse response = callWebservice(webservicePath, parameters, jsonObject, requestMethod);
		if (errors != null && response.getErrors() != null) {
			errors.getGlobalErrors().addAll(response.getErrors().getGlobalErrors());
			errors.getFieldErrors().putAll(response.getErrors().getFieldErrors());
		}
		ObjectMapper mapper = new ObjectMapper();
		return (T) mapper.convertValue(response.getPayload(), expectedReturnType);
		
	}	
	
	protected String getWebservice(String webservicePath, Map<String, String> parameters, Errors errors) throws IOException {
		BSROWebServiceResponse response = callWebservice(webservicePath, parameters, null, REQUEST_METHOD_GET);
		if (errors != null && response.getErrors() != null) {
			errors.getGlobalErrors().addAll(response.getErrors().getGlobalErrors());
			errors.getFieldErrors().putAll(response.getErrors().getFieldErrors());
		}
		return response.getStatusCode();
	}
	
	protected String postWebservice(String webservicePath, Map<String, String> parameters, Errors errors) throws IOException {
		BSROWebServiceResponse response = callWebservice(webservicePath, parameters, null, REQUEST_METHOD_POST);
		if (errors != null && response.getErrors() != null) {
			errors.getGlobalErrors().addAll(response.getErrors().getGlobalErrors());
			errors.getFieldErrors().putAll(response.getErrors().getFieldErrors());
		}
		return response.getStatusCode();
	}
	
	/**
	 * Defaults to GET method
	 * 
	 * @param webservicePath
	 * @param parameters
	 * @return
	 * @throws IOException
	 */	
	
	private BSROWebServiceResponse callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, String requestMethod) throws IOException {
		StringBuilder jsonResponse = new StringBuilder();
		URLConnection connection = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedInputStreamReader = null;

		StringBuilder url = new StringBuilder(webServiceConfig.getBaseURL()).append(webservicePath);
		String appName = webServiceConfig.getAppName();
		String tokenId = webServiceConfig.getTokenId();
		
		try {
			if (REQUEST_METHOD_GET.equals(requestMethod)) {
				if (parameters != null && !parameters.isEmpty()) {
					url.append(QUERY_STRING_BEGIN);
					boolean isFirst = true;
					for (String parameterName : parameters.keySet()) {
						if (!isFirst) {
							url.append(QUERY_STRING_NAME_VALUE_PAIR_DELIMITER);
						}
						url.append(parameterName).append(QUERY_STRING_NAME_VALUE_DELIMITER).append(URLEncoder.encode(parameters.get(parameterName), "UTF-8"));
						isFirst = false;
					}
				}
			}
			
			URL service = new URL(url.toString());

			connection = service.openConnection();
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			
			connection.addRequestProperty(WEBSERVICE_TOKEN_ID, tokenId);
			connection.addRequestProperty(WEBSERVICE_APP_NAME, appName);
			
			if (connection instanceof HttpURLConnection) {
		        ((HttpURLConnection)connection).setRequestMethod(requestMethod);
			} else if  (connection instanceof HttpsURLConnection) {
				((HttpsURLConnection)connection).setRequestMethod(requestMethod);
			}
			
			logger.debug("Sending request to: "+url.toString());		
			
			if (REQUEST_METHOD_POST.equals(requestMethod)) {	

				connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setUseCaches (false);
				connection.setRequestProperty("Accept", "application/json");
			
				if (jsonObject != null) {
					BSROWebServiceRequest request = new BSROWebServiceRequest();
					request.setRequestPayload(jsonObject);
					connection.setRequestProperty("Content-Type", "application/json");
					ObjectMapper mapper = new ObjectMapper();
					OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());

					if (parameters != null && !parameters.isEmpty()) {
						for(String key: parameters.keySet())
							request.putParameter(key, parameters.get(key));
					}
					mapper.writeValue(out, request);
				}else if (parameters != null && !parameters.isEmpty()) {

					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					StringBuilder postParameters = appendParameters(parameters);
					DataOutputStream  out = new DataOutputStream (connection.getOutputStream());
					//Util.debug(postParameters.toString());
					out.writeBytes(postParameters.toString());
					out.flush ();
					out.close ();
				}else{
					connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				}
			}
			
			inputStreamReader = new InputStreamReader(connection.getInputStream());
			bufferedInputStreamReader = new BufferedReader(inputStreamReader);

			String inputLine;

			while ((inputLine = bufferedInputStreamReader.readLine()) != null) {
				jsonResponse.append(inputLine);
			}
			
			if (jsonResponse != null) {
				logger.debug("Response: "+jsonResponse);
			}
			
		} catch (Throwable myThrowable) {
			Exception myException = new Exception("Application "+appName+" failed to read from url "+url+" using tokenId "+tokenId, myThrowable);
			myException.printStackTrace();
		} finally {
			if (bufferedInputStreamReader != null) {
				bufferedInputStreamReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (connection != null && connection.getInputStream() != null) {
				connection.getInputStream().close();
			}
		}

		if (jsonResponse.length() > 0) {
			ObjectMapper mapper = new ObjectMapper();
			BSROWebServiceResponse response = mapper.readValue(jsonResponse.toString(), BSROWebServiceResponse.class);
			return response;
		}
		
		return null;
	}

	private StringBuilder appendParameters(Map<String, String> parameters) throws UnsupportedEncodingException {
		StringBuilder postParameters = new StringBuilder(); 
		boolean isFirst = true;
		for (String parameterName : parameters.keySet()) {
			if (!isFirst) {
				postParameters.append(QUERY_STRING_NAME_VALUE_PAIR_DELIMITER);
			}
			postParameters.append(parameterName).append(QUERY_STRING_NAME_VALUE_DELIMITER).append(URLEncoder.encode(parameters.get(parameterName),"UTF-8"));
			isFirst = false;
		}
		return postParameters;
	}
}
