/**
 * 
 */
package com.bfrc.pojo.pricing;

/**
 * @author smoorthy
 *
 */
public class TpSuppressionDisplayVehId implements java.io.Serializable {
	
	private Long displayId;
	private String vehicleType;
	
	public TpSuppressionDisplayVehId() {
		
	}
	
	public TpSuppressionDisplayVehId(Long displayId, String vehicleType) {
		this.displayId = displayId;
		this.vehicleType = vehicleType;
	}
	
	public Long getDisplayId() {
		return this.displayId;
	}

	public void setDisplayId(Long displayId) {
		this.displayId = displayId;
	}
	
	public String getVehicleType() {
		return this.vehicleType;
	}
	
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

}
