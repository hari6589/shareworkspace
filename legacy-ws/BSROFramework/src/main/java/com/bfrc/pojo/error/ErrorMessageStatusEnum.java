/**
 * 
 */
package com.bfrc.pojo.error;

/**
 * @author schowdhu
 *
 */
public enum ErrorMessageStatusEnum {
	
	NEW("N"),
	
	PROCESSING("P"),
	
	COMPLETE("C");
	
	private String statusCode;
	 
	private ErrorMessageStatusEnum(String s) {
		statusCode = s;
	}
 
	public String getStatusCode() {
		return statusCode;
	}


}
