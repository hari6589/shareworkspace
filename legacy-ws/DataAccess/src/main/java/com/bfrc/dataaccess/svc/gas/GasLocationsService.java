package com.bfrc.dataaccess.svc.gas;

import java.util.Map;

import com.bfrc.dataaccess.exception.InvalidGeoLocationException;

import app.bsro.model.gas.stations.StationPrices;

public interface GasLocationsService {
	
	public StationPrices findCheapGasLocations(String address, String geoPoint, Map<StationPrices.Params, Object> params) throws InvalidGeoLocationException; 
	
}
