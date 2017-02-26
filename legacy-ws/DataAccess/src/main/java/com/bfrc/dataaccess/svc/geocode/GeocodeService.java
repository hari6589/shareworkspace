package com.bfrc.dataaccess.svc.geocode;

import java.util.Map;

import app.bsro.model.gas.stations.StationPrices;

public interface GeocodeService {

	public GeoLatLong geocode(GeoAddress address, Map<StationPrices.Params, Object> params);
	
}
