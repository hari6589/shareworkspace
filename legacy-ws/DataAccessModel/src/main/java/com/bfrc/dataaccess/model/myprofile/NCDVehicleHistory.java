/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.List;

/**
 * @author schowdhu
 *
 */
public class NCDVehicleHistory {
	
	private Long acesVehicleId;
	private List<MyServiceHistoryVehicle> vehicles;

	public NCDVehicleHistory() {
	}

	/**
	 * @param acesVehicleId
	 * @param vehicles
	 */
	public NCDVehicleHistory(Long acesVehicleId,
			List<MyServiceHistoryVehicle> vehicles) {
		this.acesVehicleId = acesVehicleId;
		this.vehicles = vehicles;
	}
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}

	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}

	/**
	 * @return the vehicles
	 */
	public List<MyServiceHistoryVehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicles the vehicles to set
	 */
	public void setVehicles(List<MyServiceHistoryVehicle> vehicles) {
		this.vehicles = vehicles;
	}

	@Override
	public String toString() {
		return "NCDVehicleHistory [acesVehicleId=" + acesVehicleId
				+ ", vehicles=" + vehicles + "]";
	}
}
