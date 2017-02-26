package com.bfrc.dataaccess.svc.gas;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.bsro.model.gas.stations.StationPrice;
import app.bsro.model.gas.stations.StationPrices;
import app.bsro.model.gas.stations.comparator.DieselPriceComparator;
import app.bsro.model.gas.stations.comparator.DistanceComparator;
import app.bsro.model.gas.stations.comparator.MidgradePriceComparator;
import app.bsro.model.gas.stations.comparator.PremiumPriceComparator;
import app.bsro.model.gas.stations.comparator.UnleadedPriceComparator;

import com.bfrc.dataaccess.exception.InvalidGeoLocationException;
import com.bfrc.dataaccess.svc.gas.OpisNetGasStationFinderServiceImpl.Sort;
import com.bfrc.dataaccess.svc.geocode.GeoAddress;
import com.bfrc.dataaccess.svc.geocode.GeoLatLong;
import com.bfrc.dataaccess.svc.geocode.GeocodeService;

@Service
public class GasStationLocationsServiceImpl implements GasLocationsService {

	private Logger logger = Logger.getLogger(getClass().getName());
	@Autowired
	@Qualifier("geocodeService")
	private GeocodeService geocodeExecutors;
	@Autowired
	private GasStationFinderService opisNetGasStationFinder;
	
	private final int MAX_CACHE_LIFE_SECONDS = (60*60*24*7);
	final int MAX_ENTRIES = 500;
	ConcurrentMap<String, GeoLatLong> GEO_CACHE = new ConcurrentHashMap<String, GeoLatLong>();
	public GasStationLocationsServiceImpl() {

		ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
		try {
			executorService.scheduleWithFixedDelay(new Runnable() {
				public void run() {
					Calendar expireTime = GregorianCalendar.getInstance();
					expireTime.add(Calendar.SECOND, (MAX_CACHE_LIFE_SECONDS)*-1);
			
					for(Map.Entry<String, GeoLatLong> entry : GEO_CACHE.entrySet()) {
						try {
							String key = entry.getKey();
							GeoLatLong o = entry.getValue();
							if (o.getCreated().before(expireTime)) {
								GEO_CACHE.remove(key);
							}
							
						}catch(ConcurrentModificationException e) {
							e.printStackTrace();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					
				}
			}, 60, this.MAX_CACHE_LIFE_SECONDS, TimeUnit.SECONDS);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void addToCache(GeoAddress geoAddress, GeoLatLong geoLatLong) {
		try {
		if(GEO_CACHE.size() > (MAX_ENTRIES-1)) {
			do {
				try {
					GEO_CACHE.remove(GEO_CACHE.entrySet().iterator().next().getKey());
				}catch(Exception e) {logger.warning("Error removing from cache: "+e.getMessage());}
			} while (GEO_CACHE.size() > MAX_ENTRIES - 1);
		}
		}catch(Exception e) {
			logger.warning("Error cleaning cache: "+e.getMessage());
		}
		GEO_CACHE.putIfAbsent(geoAddress.getCompositeString(), geoLatLong);
	}
		
	public StationPrices findCheapGasLocations(String address, String geoPoint, Map<StationPrices.Params,Object> params) throws InvalidGeoLocationException {
		
		address = StringUtils.trimToNull(address);
		geoPoint = StringUtils.trimToNull(geoPoint);
		
		if (address == null && geoPoint == null) {
			String msg = "Both address and goePoint are null";
			logger.warning(msg);
			throw new InvalidGeoLocationException(msg);
		}

		GeoLatLong geoLatLong = new GeoLatLong();
		
		//If we were passed a geoPoint then we'll just use that directly.
		if(geoPoint != null && geoPoint.indexOf(",") > -1) {
			String[] ll = geoPoint.split(",");
			if(ll != null && ll.length == 2) {
				try {
					geoLatLong.setLatitude((ll[0]));
					geoLatLong.setLongitude((ll[1]));
				}catch(Exception e) {
					String msg = "Unable to parse geoPoint ["+geoPoint+"] into lat/long values";
					logger.warning(msg);
					throw new InvalidGeoLocationException(msg);
				}
			}
		} else if(address != null){
			//Use the address value passed in
			GeoAddress geoAddress = new GeoAddress(address);
			geoLatLong = GEO_CACHE.get(geoAddress.getCompositeString());
			if(geoLatLong == null)
				geoLatLong = geocodeExecutors.geocode(geoAddress, params);
			
			if(geoLatLong == null || geoLatLong.getLatitude() == null || geoLatLong.getLongitude() == null) {
				String errorMsg = "Invalid latitude/longitude from geoPoint["+geoPoint+"] or address["+address+"]";
				logger.warning(errorMsg);
				throw new InvalidGeoLocationException(errorMsg);
			}
			
			addToCache(geoAddress, geoLatLong);			
		}
		
		String sortCd = (String)params.get(StationPrices.Params.GRADE);
		Integer radius = (Integer)params.get(StationPrices.Params.SEARCH_RADIUS);
		Sort sort = Sort.getByCode(sortCd);
		
		StationPrices prices = new StationPrices();
		prices.setLatitude(geoLatLong.getLatitude());
		prices.setLongitude(geoLatLong.getLongitude());
		prices.setGrade(sort.getSort());
		
		List<StationPrice> stationPrices = opisNetGasStationFinder.findByGeoLocation(geoLatLong, sort, radius);
		
		//No stations were found.
		if(stationPrices == null) return null;
				
		DecimalFormat df7 = new DecimalFormat("00000.00");
		for(StationPrice p : stationPrices) {
			
			double distance = 0;
			try {
				double lat = Double.valueOf(geoLatLong.getLatitude());
				double lon = Double.valueOf(geoLatLong.getLongitude());
				distance = opisNetGasStationFinder.distance(lat, lon, p.getLatitude(), p.getLongitude(), null);
				
				p.setDistance(new Double(df7.format(distance)));
			}catch(Exception e) {
				logger.warning("Error getting distance between points: "+e.getMessage());
			}
			
			prices.addStation(p);
		}
		
		ComparatorChain chain = new ComparatorChain();
		
		if(sortCd.equals(Sort.MIDGRADE.getSortCd())) {
			chain.addComparator(new MidgradePriceComparator());
		} else if(sortCd.equals(Sort.PREMIUM.getSortCd())) {
			chain.addComparator(new PremiumPriceComparator());
		} else if(sortCd.equals(Sort.DIESEL.getSortCd())) {
			chain.addComparator(new DieselPriceComparator());
		} else {
			chain.addComparator(new UnleadedPriceComparator());
		}
		
		chain.addComparator(new DistanceComparator());

		Collections.sort(stationPrices, chain);
		prices.setStations(stationPrices);
		
		return prices;
	}

}
