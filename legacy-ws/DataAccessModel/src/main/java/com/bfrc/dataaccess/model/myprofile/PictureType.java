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
public enum PictureType {
	
	VEHICLE("Vehicle"),
	DEVICE("Device"),
	DRIVER("Driver"),
	PRODUCT("Product");
	
	private String type;
	 
	private PictureType(String s) {
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
	public static PictureType getEnum(String s) {
		PictureType[] pictures = PictureType.values();
        for (PictureType picture : pictures) {
            if (picture.getType().equals(s)) {
                return picture;
            }
        }
        return VEHICLE;
	} 

}
