package com.bsro.webservice;

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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import app.bsro.model.error.WebServiceExceptionError;

import com.bsro.exception.errors.ErrorMessageException;
import com.bsro.exception.webservice.WebServiceExceptionErrorException;

public class BSROWebserviceServiceImpl implements BSROWebserviceService {
	Log logger = LogFactory.getLog(BSROWebserviceServiceImpl.class);
	
	private static final String WEBSERVICE_TOKEN_ID = "tokenId";
	private static final String WEBSERVICE_APP_NAME = "appName";
	private static final String WEBSERVICE_APP_SOURCE = "appSource";
	private static final String REQUEST_HEADER_TOKEN_ONLY = "tokenOnly";
	private static final String REQUEST_HEADER_TOKEN_AND_APP = "tokenAndApp";
	private static final String REQUEST_HEADER_ALL = "all";
	
	protected static final String PATH_WEBSERVICE_BASE = "/ws2";
	
	protected static final String PATH_DELIMITER = "/";
	
	private static final String QUERY_STRING_BEGIN = "?";
	private static final String QUERY_STRING_NAME_VALUE_DELIMITER = "=";
	private static final String QUERY_STRING_NAME_VALUE_PAIR_DELIMITER = "&";
	
	private static final String REQUEST_METHOD_POST = "POST";
	private static final String REQUEST_METHOD_GET = "GET";
	
	private BSROWebserviceConfig scheduledMaintenanceConfig;
	
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		if (bsroWebserviceConfig != null) {
			this.logger.info("Initializing BSRO webservice URL: "+bsroWebserviceConfig.getBaseURL());
		} else {
			this.logger.error("BSROWebserviceConfig is missing!");
		}
		this.scheduledMaintenanceConfig = bsroWebserviceConfig;
	}

	/**
	 * This defaults to GET. Should be replaced with a call to getWebservice
	 * 
	 * @param webservicePath
	 * @param parameters
	 * @param expectedReturnType
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	protected <T> T callWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	
	protected <T> T getWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	protected <T> T getWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType) throws IOException {
		return callWebserviceWithTokenOnly(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	
	protected <T> T postWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST);
	}
	protected <T> T postWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType) throws IOException {
		return callWebserviceWithTokenOnly(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST);
	}
	
	protected <T> T postJSONToWebservice(String webservicePath, Object jsonObject, Class<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, null, jsonObject, expectedReturnType, REQUEST_METHOD_POST);
	}
	
	protected <T> T getWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType, boolean expectErrorMessages) throws IOException, ErrorMessageException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET, expectErrorMessages);
	}
	
	protected <T> T postWebservice(String webservicePath, Map<String, String> parameters, Class<T> expectedReturnType, boolean expectErrorMessages) throws IOException, ErrorMessageException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST, expectErrorMessages);
	}
	
	protected <T> T postJSONToWebservice(String webservicePath, Object jsonObject, Class<T> expectedReturnType, boolean expectErrorMessages) throws IOException, ErrorMessageException {
		return callWebservice(webservicePath, null, jsonObject, expectedReturnType, REQUEST_METHOD_POST, expectErrorMessages);
	}
	
	private <T> T callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, String requestMethod) throws IOException {
		try {
			return callWebservice(webservicePath, parameters,jsonObject, expectedReturnType, requestMethod, false);
		} catch (ErrorMessageException errorMessageException) {
			throw new IOException("This webservice call has returned an unanticipated "+ErrorMessageException.class.getName()+". This IOException has been thrown for backward compatibility purposes. Please consider setting the expectErrorMessages flag to true for this call instead. The "+ErrorMessageException.class.getName()+" contains the following information:\n "+errorMessageException.printErrors());
		}
	}
	
	private <T> T callWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, String requestMethod) throws IOException {
		try {
			return callWebserviceWithTokenOnly(webservicePath, parameters,jsonObject, expectedReturnType, requestMethod, false);
		} catch (ErrorMessageException errorMessageException) {
			throw new IOException("This webservice call has returned an unanticipated "+ErrorMessageException.class.getName()+". This IOException has been thrown for backward compatibility purposes. Please consider setting the expectErrorMessages flag to true for this call instead. The "+ErrorMessageException.class.getName()+" contains the following information:\n "+errorMessageException.printErrors());
		}
	}
		
	private <T> T callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, String requestMethod, boolean expectErrorMessages) throws IOException, ErrorMessageException {
		String jsonResponse = callWebservice(webservicePath, parameters, jsonObject, requestMethod, REQUEST_HEADER_ALL);
		
		if (jsonResponse != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.readValue(jsonResponse, expectedReturnType);
			} catch (JsonMappingException jsonMappingExcepion) {
				try {
					// one of our webservices *may* return one of these in the case of a validation issue
					// we copy over to a framework-specific Errors object so it's a little more generic - services that don't use the webservice and don't have access to the data access model could use it
					app.bsro.model.error.Errors errorMessageResponse = mapper.readValue(jsonResponse, app.bsro.model.error.Errors.class);
					com.bsro.errors.Errors errors = new com.bsro.errors.Errors();
					errors.setGlobalErrors(errorMessageResponse.getGlobalErrors());
					errors.setFieldErrors(errorMessageResponse.getFieldErrors());
					throw new ErrorMessageException(errors);
				} catch (JsonMappingException jsonMappingExcepion2) {
					try {
						// one of our webservices *may* return one of these in the case of an unexpected error - it's a way of returning a meaningful response
						// it's a runtime exception so we don't have to go refactor all of the old exception calls to catch it
						WebServiceExceptionError webServiceErrorResponse = mapper.readValue(jsonResponse, WebServiceExceptionError.class);
						throw new WebServiceExceptionErrorException(webServiceErrorResponse);
					} catch (JsonMappingException jsonMappingException3) {
						throw new JsonMappingException("Unexpected response "+jsonResponse, jsonMappingException3);
					}
				}
			} catch (JsonParseException jsonParseException) {
				throw new JsonMappingException("Unexpected response "+jsonResponse, jsonParseException);
			}
		}
		return null;
	}
	
	private <T> T callWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, Object jsonObject, Class<T> expectedReturnType, String requestMethod, boolean expectErrorMessages) throws IOException, ErrorMessageException {
		String jsonResponse = callWebservice(webservicePath, parameters, jsonObject, requestMethod, REQUEST_HEADER_TOKEN_ONLY);
		
		if (jsonResponse != null) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				return mapper.readValue(jsonResponse, expectedReturnType);
			} catch (JsonMappingException jsonMappingExcepion) {
				try {
					// one of our webservices *may* return one of these in the case of a validation issue
					// we copy over to a framework-specific Errors object so it's a little more generic - services that don't use the webservice and don't have access to the data access model could use it
					app.bsro.model.error.Errors errorMessageResponse = mapper.readValue(jsonResponse, app.bsro.model.error.Errors.class);
					com.bsro.errors.Errors errors = new com.bsro.errors.Errors();
					errors.setGlobalErrors(errorMessageResponse.getGlobalErrors());
					errors.setFieldErrors(errorMessageResponse.getFieldErrors());
					throw new ErrorMessageException(errors);
				} catch (JsonMappingException jsonMappingExcepion2) {
					try {
						// one of our webservices *may* return one of these in the case of an unexpected error - it's a way of returning a meaningful response
						// it's a runtime exception so we don't have to go refactor all of the old exception calls to catch it
						WebServiceExceptionError webServiceErrorResponse = mapper.readValue(jsonResponse, WebServiceExceptionError.class);
						throw new WebServiceExceptionErrorException(webServiceErrorResponse);
					} catch (JsonMappingException jsonMappingException3) {
						throw new JsonMappingException("Unexpected response "+jsonResponse, jsonMappingException3);
					}
				}
			} catch (JsonParseException jsonParseException) {
				throw new JsonMappingException("Unexpected response "+jsonResponse, jsonParseException);
			}
		}
		return null;
	}
	
	/**
	 * This defaults to GET. Should be replaced with a call to getWebservice
	 * 
	 * @param webservicePath
	 * @param parameters
	 * @param expectedReturnType
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	protected <T> T callWebservice(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	
	protected <T> T getWebservice(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	
	protected <T> T getWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType) throws IOException {
		return callWebserviceWithTokenOnly(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_GET);
	}
	
	protected <T> T postWebservice(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST);
	}
	
	protected <T> T postWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, TypeReference<T> expectedReturnType) throws IOException {
		return callWebserviceWithTokenOnly(webservicePath, parameters, null, expectedReturnType, REQUEST_METHOD_POST);
	}
	
	protected <T> T postJSONToWebservice(String webservicePath, Object jsonObject, TypeReference<T> expectedReturnType) throws IOException {
		return callWebservice(webservicePath, null, jsonObject, expectedReturnType, REQUEST_METHOD_POST);
	}
	
	private <T> T callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, TypeReference<T> expectedReturnType, String requestMethod) throws IOException {
		String jsonResponse = callWebservice(webservicePath, parameters, jsonObject, requestMethod, REQUEST_HEADER_ALL);
		if (jsonResponse != null) {
			ObjectMapper mapper = new ObjectMapper();
			return (T)mapper.readValue(jsonResponse, expectedReturnType);
		}
		return null;
	}
	private <T> T callWebserviceWithTokenOnly(String webservicePath, Map<String, String> parameters, Object jsonObject, TypeReference<T> expectedReturnType, String requestMethod) throws IOException {
		String jsonResponse = callWebservice(webservicePath, parameters, jsonObject, requestMethod, REQUEST_HEADER_TOKEN_ONLY);
		if (jsonResponse != null) {
			ObjectMapper mapper = new ObjectMapper();
			return (T)mapper.readValue(jsonResponse, expectedReturnType);
		}
		return null;
	}
	
	/**
	 * This defaults to GET. Should be replaced with a call to getWebservice
	 * 
	 * @param webservicePath
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	@Deprecated
	protected String callWebservice(String webservicePath, Map<String, String> parameters) throws IOException {
		return callWebservice(webservicePath, parameters, null, REQUEST_METHOD_GET, REQUEST_HEADER_ALL);
	}
	
	protected String getWebservice(String webservicePath, Map<String, String> parameters) throws IOException {
		return callWebservice(webservicePath, parameters, null, REQUEST_METHOD_GET, REQUEST_HEADER_ALL);
	}
	
	protected String postWebservice(String webservicePath, Map<String, String> parameters) throws IOException {
		return callWebservice(webservicePath, parameters, null, REQUEST_METHOD_POST, REQUEST_HEADER_ALL);
	}
	
	/**
	 * Defaults to GET method
	 * 
	 * @param webservicePath
	 * @param parameters
	 * @return
	 * @throws IOException
	 */
	private String callWebservice(String webservicePath, Map<String, String> parameters, Object jsonObject, String requestMethod, String requestProperty) throws IOException {
		StringBuilder jsonResponse = new StringBuilder();
		URLConnection connection = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedInputStreamReader = null;

		StringBuilder url = new StringBuilder(scheduledMaintenanceConfig.getBaseURL()).append(webservicePath);
		String appName = scheduledMaintenanceConfig.getAppName();
		String tokenId = scheduledMaintenanceConfig.getTokenId();
		String appSource = scheduledMaintenanceConfig.getAppSource();
		
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
			connection.setConnectTimeout(60000);
			connection.setReadTimeout(60000);
			
			if(requestProperty != null && requestProperty.equalsIgnoreCase(REQUEST_HEADER_TOKEN_ONLY)){
				connection.addRequestProperty(WEBSERVICE_TOKEN_ID, tokenId);
			}else if(requestProperty != null && requestProperty.equalsIgnoreCase(REQUEST_HEADER_TOKEN_AND_APP)){
				connection.addRequestProperty(WEBSERVICE_TOKEN_ID, tokenId);
				connection.addRequestProperty(WEBSERVICE_APP_NAME, appName);
			}else{
				connection.addRequestProperty(WEBSERVICE_TOKEN_ID, tokenId);
				connection.addRequestProperty(WEBSERVICE_APP_NAME, appName);
				connection.addRequestProperty(WEBSERVICE_APP_SOURCE, appSource);
			}
			
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
					connection.setRequestProperty("content-type", "application/json");
					connection.setRequestProperty("Accept", "application/json");
					ObjectMapper mapper = new ObjectMapper();
					OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
					mapper.writeValue(out, jsonObject);
				} else if (parameters != null && !parameters.isEmpty()) {
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
			return jsonResponse.toString();
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
