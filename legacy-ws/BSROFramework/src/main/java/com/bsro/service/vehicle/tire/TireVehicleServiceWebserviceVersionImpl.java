package com.bsro.service.vehicle.tire;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.rebrand.BSROWebserviceServiceImpl;

//@Service - not actually being used for now
public class TireVehicleServiceWebserviceVersionImpl extends BSROWebserviceServiceImpl {
	
	private static final String VEHICLE_TIRE_OPTIONS_YEAR_MAKE_MODEL_SUBMODEL_PATH =  PATH_WEBSERVICE_BASE + "/vehicle/tire/options/year-make-model-submodel";
	
	private static final String YEARS_OPTIONS_PATH = VEHICLE_TIRE_OPTIONS_YEAR_MAKE_MODEL_SUBMODEL_PATH + "/years";
	private static final String MAKES_OPTIONS_PATH = VEHICLE_TIRE_OPTIONS_YEAR_MAKE_MODEL_SUBMODEL_PATH + "/makes";
	private static final String MODELS_OPTIONS_PATH = VEHICLE_TIRE_OPTIONS_YEAR_MAKE_MODEL_SUBMODEL_PATH + "/models";
	private static final String SUBMODELS_OPTIONS_PATH = VEHICLE_TIRE_OPTIONS_YEAR_MAKE_MODEL_SUBMODEL_PATH + "/submodels";
	
	
	private static final String PARAM_YEAR = "year";
	private static final String PARAM_MAKE = "make";
	private static final String PARAM_MODEL = "model";
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}
	
	//@Override
	public Map<String, String> getYearOptions() throws Exception {
		return (Map<String, String>) getWebservice(YEARS_OPTIONS_PATH, null, new TypeReference<LinkedHashMap<String, String>>() {}, null);
	}

	//@Override
	public Map<String, String> getMakeOptionsByYear(String year) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_YEAR, year);
		return (Map<String, String>) getWebservice(MAKES_OPTIONS_PATH, parameters, new TypeReference<LinkedHashMap<String, String>>() {}, null);
	}

	//@Override
	public Map<String, String> getModelOptionsByYearAndMakeName(String year, String makeName) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_YEAR, year);
		parameters.put(PARAM_MAKE, makeName);
		return (Map<String, String>) getWebservice(MODELS_OPTIONS_PATH, parameters, new TypeReference<LinkedHashMap<String, String>>() {}, null);
	}

	//@Override
	public Map<String, String> getSubModelOptionsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PARAM_YEAR, year);
		parameters.put(PARAM_MAKE, makeName);
		parameters.put(PARAM_MODEL, modelName);
		return (Map<String, String>) getWebservice(SUBMODELS_OPTIONS_PATH, parameters, new TypeReference<LinkedHashMap<String, String>>() {}, null);
	}

	//@Override
	public List<TireVehicle> getVehiclesByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception {
		throw new Exception("Not implemented yet");
	}
	
}
