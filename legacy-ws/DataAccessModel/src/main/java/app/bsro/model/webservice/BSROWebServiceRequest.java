package app.bsro.model.webservice;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BSROWebServiceRequest implements Serializable{


	@Override
	public String toString() {
		return "BSROWebServiceRequest [requestPayload=" + requestPayload
				+ ", requestType=" + requestType + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//POJO to be translated to JSON.
	private Object requestPayload;
	private String requestType;
	private Map<String, String> requestParameters = new HashMap<String,String>();
	
	public Object getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(Object requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public void removeParameter(String key) {
		 requestParameters.remove(key);
	}

	public String getParameter(String key) {
		 return requestParameters.get(key);
	}
	public void putParameter(String key, String value) {
		requestParameters.put(key, value);
	}
	public Map<String,String> getParameterMap() {
		return	requestParameters;
	}
	
}
