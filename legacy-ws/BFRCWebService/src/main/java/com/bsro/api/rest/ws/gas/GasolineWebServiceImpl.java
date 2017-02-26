package com.bsro.api.rest.ws.gas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.gas.mpg.EpaMpgSearchResult;
import app.bsro.model.gas.stations.StationPrice;
import app.bsro.model.gas.stations.StationPrices;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;
import com.bfrc.dataaccess.exception.InvalidGeoLocationException;
import com.bfrc.dataaccess.model.gas.BsroEpaMpgLookup;
import com.bfrc.dataaccess.svc.gas.GasLocationsService;
import com.bfrc.dataaccess.svc.webdb.EpaMpgService;
import com.bsro.core.exception.ws.EmptyDataSetException;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.security.RequireValidToken;

@Component
public class GasolineWebServiceImpl implements GasolineWebService {
	
	@Autowired
	private EpaMpgService epaMpgServiceImpl;
	@Autowired
	private GasLocationsService gasLocationsServiceImpl;

	@Autowired
	private PropertyAccessor propertyAccessor;
	
	
	@Override
	@RequireValidToken
	public StationPrices findCheapGas(HttpHeaders headers, String address,
			String geoPoint, String strRadius, String maxCount, String grade) {
		
		Integer radius = propertyAccessor.getIntegerProperty("gasDefaultSearchRadius");
		if(StringUtils.trimToNull(strRadius) != null) {
			try {
				radius = new Integer(strRadius);
			}catch(NumberFormatException e) {
				throw new InvalidArgumentException("Invalid radius");
			}
		}

		
		//Check to see if either we've been sent an address or a geoPoint
		if(StringUtils.trimToNull(geoPoint) == null && StringUtils.trimToNull(address) == null)
			throw new InvalidArgumentException("A valid geoPoint or address was NOT passed");
	
		Map<StationPrices.Params,Object> geoParams = new HashMap<StationPrices.Params,Object>();
		geoParams.put(StationPrices.Params.GRADE, grade);
		geoParams.put(StationPrices.Params.SEARCH_RADIUS, radius);
		StationPrices stationPrices = null;
		try {
			stationPrices = gasLocationsServiceImpl.findCheapGasLocations(address, geoPoint, geoParams);
		} catch (InvalidGeoLocationException e) {
			throw new InvalidArgumentException(e.getMessage());
		}
		if(stationPrices == null || 
				stationPrices.getStations() == null || 
				stationPrices.getStations().size() == 0)
			throw new EmptyDataSetException();
		
		//filter out those stations that has no price info ($0.00)
		List<StationPrice> stations = stationPrices.getStations();
		if(stations != null && stations.size() > 0) {
			List<StationPrice> filteredStationList = new ArrayList<StationPrice>();
			for(int i=0; i<stations.size(); i++) {
				StationPrice sp = (StationPrice)stations.get(i);
				if("U".equals(grade) && sp.getUnleadedPrice() > 0.0d) {
					filteredStationList.add(sp);
				} else if("M".equals(grade) && sp.getMidGradePrice() > 0.0d) {
					filteredStationList.add(sp);
				} else if("P".equals(grade) && sp.getPremiumPrice() > 0.0d) {
					filteredStationList.add(sp);
				} else if("D".equals(grade) && sp.getDieselPrice() > 0.0d) {
					filteredStationList.add(sp);
				}
			}
			stationPrices.setStations(filteredStationList);
		}
		
		//Do we only want to return a certain number of results?
		if(maxCount != null) {
			int max = 0;
			try { max = new Integer(maxCount); }catch(Exception e){max = 0;}
			if(max > 0) {
				
				List<StationPrice> ps = stationPrices.getStations();
				if(ps != null && ps.size() > max) {

					List<StationPrice> finalStationList = new ArrayList<StationPrice>();
					for(int i=0;i<max;i++) {
						finalStationList.add(ps.get(i));
					}
					stationPrices.setStations(finalStationList);

				}
			}
		}
		
		return stationPrices;
	}

}
