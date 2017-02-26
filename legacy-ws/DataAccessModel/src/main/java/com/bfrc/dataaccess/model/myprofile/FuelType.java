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
public enum FuelType {
	
	REGULAR("Regular"),
	PLUS("Plus"),
	PREMIUM("Premium"),
	DIESEL("Diesel");
	
	
	private String type;
	 
	private FuelType(String s) {
		type = s;
	}
 
	
	@JsonValue
	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return type;
	}
	
	@JsonCreator
	public static FuelType getEnum(String s) {
		FuelType[] fuelTypes = FuelType.values();
        for (FuelType fuelType : fuelTypes) {
            if (fuelType.getType().equals(s)) {
                return fuelType;
            }
        }
        return REGULAR;
	} 
	
}
