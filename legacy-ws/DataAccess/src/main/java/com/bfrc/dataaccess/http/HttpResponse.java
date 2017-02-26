package com.bfrc.dataaccess.http;

/**
 * Internal HTTP response Object to hold the status code as well as the message body.
 * @author Brad Balmer
 *
 */
public class HttpResponse {
	private Integer statusCode;
	private String responseBody;
	
	public HttpResponse(){}
	public HttpResponse(Integer status, String response) {
		this.statusCode = status;
		this.responseBody = response;
		
		//Default the status code to a value to ensure that it is not null
		if(this.statusCode == null) this.statusCode = 999;
	}
	
	public String getResponseBody() {
		return responseBody;
	}
	public Integer getStatusCode() {
		return statusCode;
	}
	
	public boolean isHttpValidResponse() {
		return(statusCode > 199 && statusCode < 300);
	}
	
	@Override
	public String toString() {
		return "HttpResponse [statusCode=" + statusCode + ", responseBody="
				+ responseBody + "]";
	}
	
	
}
