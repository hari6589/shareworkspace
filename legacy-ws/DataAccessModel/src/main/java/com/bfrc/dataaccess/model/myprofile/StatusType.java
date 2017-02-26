/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author schowdhu
 *
 */
public enum StatusType {
	
	ACTIVE("A"),
	LOCKED("L"),
	INACTIVE("I");
	
	private String value;
	
	private StatusType(String s) {
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
	

	
	@JsonCreator
	public static StatusType getEnum(String s) {
		StatusType[] statuses = StatusType.values();
        for (StatusType status : statuses) {
            if (status.getValue().equals(s)) {
                return status;
            }
        }
        return ACTIVE;
	}

}
