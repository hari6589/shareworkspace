/**
 * 
 */
package com.bfrc.dataaccess.model.mws;

/**
 * @author schowdhu
 *
 */
public enum MwsUserStatus {
	
	ACTIVE("1"),
	LOCKED("0");
	
	private String statusCode;
	 
	private MwsUserStatus(String s) {
		statusCode = s;
	}
 
	public String getStatusCode() {
		return statusCode;
	}

}
