package com.bsro.service.vehicle.tire;

import java.util.List;
import java.util.Map;

import com.bsro.databean.vehicle.TireVehicle;

public interface TireVehicleService {
	
	public Map<String, String> getYearOptions() throws Exception;
	
	public Map<String, String> getMakeOptionsByYear(String year) throws Exception;
	
	public Map<String, String> getModelOptionsByYearAndMakeName(String year, String makeName) throws Exception;
	
	public Map<String, String> getSubModelOptionsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception;
	
	public List<TireVehicle> getVehiclesByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception;
	
	// For "reverse widget" with make/model first
	public Map<String, String> getMakeOptions() throws Exception;
	
	public Map<String, String> getModelOptionsByMakeName(String makeName) throws Exception;
	
	public Map<String, String> getSubModelOptionsByMakeNameAndModelName(String makeName, String modelName) throws Exception;
	
	public List<TireVehicle> getVehiclesByMakeNameModelNameSubmodelName(String makeName, String modelName, String submodelName) throws Exception;
}
