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
public enum ProductType {
	
	TIRE("Tire"),
	BATTERY("Battery"),
	ALIGNMENT("Alignment"),
	OIL_CHANGE("Oil"),
	OTHERS("Others");
	
	private String type;
	 
	private ProductType(String s) {
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
	public static ProductType getEnum(String s) {
		ProductType[] products = ProductType.values();
        for (ProductType product : products) {
            if (product.getType().equals(s)) {
                return product;
            }
        }
        return TIRE;
	} 
}
