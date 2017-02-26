package com.bfrc.dataaccess.svc.gas;

import java.util.List;

import app.bsro.model.gas.stations.StationPrice;

import com.bfrc.dataaccess.svc.gas.OpisNetGasStationFinderServiceImpl.Sort;
import com.bfrc.dataaccess.svc.geocode.GeoLatLong;

public interface GasStationFinderService {

	/**
	 * Find a listing of gas stations by the Latitude/Longitude
	 * @param geoLatLong
	 * @return
	 */
	public List<StationPrice> findByGeoLocation(GeoLatLong geoLatLong, Sort sort);
	public List<StationPrice> findByGeoLocation(GeoLatLong geoLatLong, Sort sort, int searchRadius);
	public double distance(double lat1, double lon1, double lat2, double lon2, String unit);
}
