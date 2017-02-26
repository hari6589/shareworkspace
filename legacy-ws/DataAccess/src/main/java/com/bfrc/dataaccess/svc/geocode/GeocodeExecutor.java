package com.bfrc.dataaccess.svc.geocode;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service(value="geocodeService")
public class GeocodeExecutor implements GeocodeService {

	//WI-1977: replace all google geocode calls with Bing rest Geo location API calls
	//	@Autowired
	//	@Qualifier("googleGeocodeService")
	//	private GeocodeService googleGeocodeService;
	
	//WI-428: Remove references to Yahoo Geo-coding API call
	//	@Autowired
	//	@Qualifier("yahooGeocodeService")
	//	private GeocodeService yahooGeocodeService;
	
	@Autowired
	@Qualifier("bingRestGeocodeService")
	private GeocodeService bingRestGeocodeService;
	
	
	public GeoLatLong geocode(GeoAddress address, Map params) {
		/*if(this.googleGeocodeService == null && this.yahooGeocodeService == null) {
			return null;
		}*/
		
		if(bingRestGeocodeService == null){
			return null;
		}
		
		if (this.bingRestGeocodeService != null) {
			GeoLatLong ll = bingRestGeocodeService.geocode(address, params);
			if(ll != null) return ll;
		}
		
		//WI-1977: replace all google geocode calls with Bing rest Geo location API calls
		/*if(this.googleGeocodeService == null) {
			return null;
		}
		
		if (this.googleGeocodeService != null) {
			GeoLatLong ll = googleGeocodeService.geocode(address, params);
			if(ll != null) return ll;
		}*/
		
		//WI-428: Remove references to Yahoo Geo-coding API call
		/*
		if (this.yahooGeocodeService != null) {
			GeoLatLong ll = yahooGeocodeService.geocode(address, params);
			if(ll != null) return ll;
		}
		*/
		
		return null;
	}
}
