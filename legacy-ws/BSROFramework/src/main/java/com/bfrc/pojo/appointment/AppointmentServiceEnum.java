/**
 * 
 */
package com.bfrc.pojo.appointment;

/**
 * @author schowdhu
 *
 */
public enum AppointmentServiceEnum {
	COMPLETE_VEHICLE_INSPECTION("1"),
	OIL_AND_FILTER_CHANGE("2"),
	TIRE_REPLACEMENT("3"),
	TIRE_ROTATION("4"),
	BALANCING("5"),
	TIRE_REPAIR("6"),
	ALIGNMENT("7"),
	BATTERY_CHARGING("8"),
	BRAKES("9"),
	FLUSH_BRAKE_FLUID("10"),
	AIR_CONDITIONING_SERVICE("11"),
	ENGINE_TUNE_UP("12"),
	FLUSH_COOLANT_RADIATOR("13"),
	CHANGE_TRANSMISSION_FLUID("14"),
	BELTS_HOSES("15"),
	WIPER_BLADES("16"),
	SHOCKS_STRUTS("17"),
	MANUFACTURER_SCHEDULED_MAINTENANCE("18"),
	OTHER("19");
	
	String id;
	String value;
	
	private AppointmentServiceEnum(String id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

}
