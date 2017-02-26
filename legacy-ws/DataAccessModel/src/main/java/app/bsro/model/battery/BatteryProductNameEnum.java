/**
 * 
 */
package app.bsro.model.battery;

import org.codehaus.jackson.annotate.JsonValue;

/**
 * @author schowdhu
 *
 */
public enum BatteryProductNameEnum {
			
	PF("Interstate PowerFast"),
	MT("Mega-Tron II"),
	MTP("Mega-Tron Plus");
	
	private BatteryProductNameEnum(String s) {
		value = s;
	}
	
	private String value;
	
	@JsonValue
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(){
		return value;
	}
	
}
