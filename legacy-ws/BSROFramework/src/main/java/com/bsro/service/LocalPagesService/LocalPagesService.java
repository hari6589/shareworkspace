package com.bsro.service.LocalPagesService;

import java.util.List;

import com.bsro.databean.location.LocationDataBean;
import com.bsro.databean.vehicle.VehicleDataBean;

public interface LocalPagesService {
	public List<LocationDataBean> getSpecifiedLocationList(String siteName, String listName);
	
	public LocationDataBean findSpecifiedLocationStateAndCityInList(String siteName, String listName, String state, String city);
	
	public List<VehicleDataBean> getSpecifiedVehicleList(String siteName, String listName);
	
	public VehicleDataBean findSpecifiedVehicleMakeAndModelInList(String siteName, String listName, String make, String model);
}
