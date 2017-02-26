/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author schowdhu
 *
 */
public enum SourceType {
	USER("User"),
	NCD("NCD"),
	OTHER("");
	
	private String value;
	
	private SourceType(String s) {
		value = s;
	}
	 
	@JsonValue
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}
}
