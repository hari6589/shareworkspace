package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;

import com.bfrc.Bean;
import com.bfrc.pojo.tire.Tire;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;

public interface VehicleDAO extends Bean {
	@SuppressWarnings("rawtypes")
	List getModelYears(Map param);
	@SuppressWarnings("rawtypes")
	List getMakeNames(Map param);
	@SuppressWarnings("rawtypes")
	List getModelNames(Map param);
	@SuppressWarnings("rawtypes")
	List getSubmodels(Map param);
	@SuppressWarnings("rawtypes")
	List getAirPressures(String siteName, String year, String make, String model, String submodel);
	
	Long getMakeIdByModelId(Object modelId);
	@SuppressWarnings("rawtypes")
	List getFitmentsByNames(Object year, String make, String model, String submodel);
	@SuppressWarnings("rawtypes")
	List getFitmentsLightMakeModelOnly();
	
	@SuppressWarnings("rawtypes")
	List getYearList();
	@SuppressWarnings("rawtypes")
	List getMakeList(Object year);
	public List<TireVehicleMake> getAllMakes();

	@SuppressWarnings("rawtypes")
	List getModelList(Object year, Object makeId);
	public List<TireVehicleModel> getAllModels();
	public List<TireVehicleModel> getModelsByMakeId(Long makeId);
	public List<TireVehicleModel> getModelsByMakeName(String makeName);
	
	public List<String> getYearOptionsByMakeModel(String makeName, String modelName);
	
	@SuppressWarnings("rawtypes")
	List getModelListByName(Object year, String makeName);
	@SuppressWarnings("rawtypes")
	List getSubmodelList(Object year, Object makeId, Object modelId);
	//this method returns the submodelId in addition to the acesVehicleId
	@SuppressWarnings("rawtypes")
	List getSubmodelListNew(Object year, Object makeId, Object modelId);
	@SuppressWarnings("rawtypes")
	List getSubmodelListByName(Object year, String makeName, String modelName);
	//this method returns the submodelId in addition to the acesVehicleId
	@SuppressWarnings("rawtypes")
	List getSubmodelListByNameNew(Object year, String makeName, String modelName);
	
	public List<TireVehicleSubModel> getSubModelsByMakeIdAndModelId(Long makeId, Long modelId);
	public List<TireVehicleSubModel> getSubModelsByMakeNameAndModelName(String makeName, String modelName);
	
	public List<TireVehicleMake> getMakesByYear(String year);
	public List<TireVehicleModel> getModelsByYearAndMakeName(String year, String makeName);
	public List<TireVehicleSubModel> getSubModelsByYearAndMakeNameAndModelName(String year, String makeName, String modelName);
	public List<TireVehicleSubModel> getSubModelsByYearMakeAndModelNames(String year, String makeName, String modelName);
	public List<TireVehicleSubModel> getTrimByYearMakeAndModelNames(String year, String makeName, String modelName);
		
	public List<TireVehicle> getVehiclesByMakeIdModelIdSubmodelName(Long makeId, Long modelId, String submodelName);
	public List<TireVehicle> getVehiclesByMakeNameModelNameSubmodelName(String makeName, String modelName, String submodelName);
	public List<TireVehicle> getVehiclesByYearMakeNameModelName(String year, String makeName, String modelName);
	public TireVehicle fetchVehicleByAcesVehicleId(Long acesVehicleId);
	
	List<String> getSubmodelNameListByAcesVehicleId(Long acesVehicleId);
	@SuppressWarnings("rawtypes")
	List getAirPressuresById(String siteName, Object year, Object makeId, Object modelId, String submodel);
	@SuppressWarnings("rawtypes")
	List getAirPressuresById(String siteName, Object year, Object makeId, Object modelId, Object submodelId);
	@SuppressWarnings("rawtypes")
	List getFitmentsByIds(Object year, Object makeId, Object modelId, String submodel);
	@SuppressWarnings("rawtypes")
	List getFitmentsByIds(Object year, Object makeId, Object modelId, Object submodelId);
	@SuppressWarnings("rawtypes")
	List getFitments(Object acesVehicleId);
	
	
	//------------------- VERSION 2 --------------------//
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresDescription(String articleNumbersdelimited);
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresPricing(String siteName, String articleNumbersdelimited,String storeNumber,List items);
	
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresDescription(List articles);
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresPricing(String siteName, List articles,String storeNumber,List items);
	
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresPricing(String siteName, String articleNumbersdelimited,String storeNumber,String regularStoreNumber,List items);
	
	@SuppressWarnings("rawtypes")
	List<Tire> populateTiresPricing(String siteName, List articles,String storeNumber,String regularStoreNumber,List items);
	
	
	List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object acesVehicleId, boolean byAdvanced, String tireGroup, String tireClass);
	List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object regularStoreNumber,Object acesVehicleId, boolean byAdvanced, String tireGroup, String tireClass);
	
	List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object acesVehicleId);
	List<Tire> searchTiresByVehicle(String siteName, Object storeNumber,Object regularStoreNumber,Object acesVehicleId);
	
	List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, String submodel);
	List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object year, Object makeId, Object modelId, Object submodelId);
	List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, String submodel);
	List<Tire> searchTiresByVehicleIds(String siteName, Object storeNumber,Object regularStoreNumber,Object year, Object makeId, Object modelId, Object submodelId);
	List<Tire> searchTiresByVehicleNames(String siteName, Object storeNumber,Object year, String make, String model, String submodel);
	List<Tire> searchTiresByVehicleNames(String siteName, Object storeNumber,Object regularStoreNumber,Object year, String make, String model, String submodel);
	List<Tire> searchTiresBySize(String siteName, Object storeNumber,Object cross, Object aspect, Object rim);
	List<Tire> searchTiresBySize(String siteName, Object storeNumber,Object regularStoreNumber,Object cross, Object aspect, Object rim);
	
	List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber);
	List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber, String regularStoreNumber);
	List<Tire> populateTiresByArticles(String siteName, List articleNumbers,String storeNumber);
	List<Tire> populateTiresByArticles(String siteName, List articleNumbers,String storeNumber,String regularStoreNumber);
	
	String getMakeNameByMakeId(Object makeId);
	String getModelNameByModelId(Object modelId);
	String getSubmodelNameBySubmodelId(Object submodelId);
	Object getVehicleSizes(Object o);
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleCrossSections();
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleAspects(String crossSection);
	
	/**
	 * This replaces Object getVehicleSizes(Object o);
	 */
	public Map<String, String> getVehicleRims(String crossSection, String aspect);
	
	long getMakeIdByMakeName(String makeName);
	long getModelIdByModelName(String modelName);
	long getSubmodelIdBySubmodelName(String submodelName);
	List getMakeModelList();
	List getYearListByMakeAndModel(Object makeIdOrName, Object modelIdOrName);
	List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelName(Integer year, String make, String model, String submodel);
	List<Object[]> getModelListByNameCaseInsensitive(String year, String makeName);
	List<Object[]> getSubmodelListByNameCaseInsensitive(Object year, String makeName, String modelName);
	List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelNameCaseInsensitive(Integer year, String make, String model, String submodel);
	
	public Map<String, String> getVehicleRims();
	public Map<String, String> getVehicleRims(String siteName);
	public Map<String, String> getVehicleSizesByRim(String rim);
	public Map<String, String> getVehicleSizesByRim(String rim, String siteName);
	public String findToyoStoreById(Long storeNumber);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(Long acesVehicleId);
	
	public List<FriendlyVehicleDataBean> getMakeModelsByVehicleType(List<String> vehtype);
}
