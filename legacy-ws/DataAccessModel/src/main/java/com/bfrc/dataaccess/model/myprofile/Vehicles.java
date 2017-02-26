/**
 * 
 */
package com.bfrc.dataaccess.model.myprofile;

import java.util.Collection;

/**
 * @author schowdhu
 *
 */
public class Vehicles {
	
	private Collection<MyVehicle> vehicles;
	
	
	/**
	 * 
	 */
	public Vehicles() {
	}

	/**
	 * @param vehicles
	 */
	public Vehicles(Collection<MyVehicle> vehicles) {
		this.vehicles = vehicles;
	}

	/**
	 * @return the vehicle
	 */
	public Collection<MyVehicle> getVehicles() {
		return vehicles;
	}

	/**
	 * @param vehicle the vehicle to set
	 */
	public void setVehicles(Collection<MyVehicle> vehicles) {
		this.vehicles = vehicles;
	}

}
