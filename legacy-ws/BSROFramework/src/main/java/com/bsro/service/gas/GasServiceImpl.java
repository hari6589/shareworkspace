package com.bsro.service.gas;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.gas.mpg.EpaMpgSearchResult;
import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;
import com.bsro.webservice.BSROWebserviceConfig;
import com.bsro.webservice.BSROWebserviceServiceImpl;

@Service("gasService")
public class GasServiceImpl extends BSROWebserviceServiceImpl implements GasService {

	private static final String PATH_GAS_BASE = "/gas";
	
	private static final String PATH_FIND_GAS_PRICE = "/find/price";
	
	private static final String PATH_FIND_MPG = "/mpg";
	
	private static final String PARAM_GEO_POINT = "geoPoint";
	
	private static final String PARAM_VEHICLE_YEAR = "year";
	
	private static final String PARAM_VEHICLE_MAKE = "make";
	
	private static final String PARAM_VEHICLE_MODEL = "model";
	
	private static final String LATITUDE_LONGITUDE_DELIMITER = ",";
	
	@Autowired
	public void setBSROWebserviceConfig(BSROWebserviceConfig bsroWebserviceConfig) {
		super.setBSROWebserviceConfig(bsroWebserviceConfig);
	}
	
	@Override
	public StationPrices findCheapGas(String latitude, String longitude) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put(PARAM_GEO_POINT, latitude+LATITUDE_LONGITUDE_DELIMITER+longitude);
		
		return (StationPrices) getWebserviceWithTokenOnly(PATH_WEBSERVICE_BASE+PATH_GAS_BASE+PATH_FIND_GAS_PRICE, parameters, StationPrices.class); 
	}

	@Override
	public List<BsroEpaMpgLookup> findMilesPerGallon(String vehicleYear, String vehicleMake, String vehicleModel) throws IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		
		parameters.put(PARAM_VEHICLE_YEAR, vehicleYear);
		parameters.put(PARAM_VEHICLE_MAKE, vehicleMake);
		parameters.put(PARAM_VEHICLE_MODEL, vehicleModel);
		
		EpaMpgSearchResult result = (EpaMpgSearchResult) getWebserviceWithTokenOnly(PATH_WEBSERVICE_BASE+PATH_GAS_BASE+PATH_FIND_MPG, parameters, EpaMpgSearchResult.class); 
		
		if (result != null) {
			return result.getEpaMpgs();
		}
		return null;
	}
	
}
