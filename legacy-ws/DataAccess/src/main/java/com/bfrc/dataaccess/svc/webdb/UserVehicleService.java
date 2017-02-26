package com.bfrc.dataaccess.svc.webdb;

import com.bfrc.dataaccess.model.VehicleSearchResult;
import com.bfrc.dataaccess.model.vehicle.UserVehicle;

public interface UserVehicleService {

	public VehicleSearchResult listModelYears();
	public VehicleSearchResult listMakes(String year);
	public VehicleSearchResult listModels(Long makeId, String year);
	public VehicleSearchResult listSubModels(Long makeId, Long modelId, String year);
	public UserVehicle search(String year, Long makeId, Long modelId, String subModelId);

}
