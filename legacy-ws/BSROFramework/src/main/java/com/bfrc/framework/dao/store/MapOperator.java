package com.bfrc.framework.dao.store;

import java.util.Vector;

import esri.aws.v2006_1.*;
import esri.aws.v2006_1.dto.geom.*;
import esri.aws.v2006_1.dto.mapimage.*;
import esri.aws.v2006_1.dto.routefinder.*;

import com.bfrc.LocatorConfig;
import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.pojo.store.*;
import com.bfrc.storelocator.util.GeocodingException;

public class MapOperator extends BusinessOperatorSupport {
	private GeocodeOperator geocodeOperator;

	public GeocodeOperator getGeocodeOperator() {
		return this.geocodeOperator;
	}

	public void setGeocodeOperator(GeocodeOperator geocodeOperator) {
		this.geocodeOperator = geocodeOperator;
	}

	public Object operate(Object o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public Map getMap(long id, String app, String street, String city,
			String state, String zip, Float[] location, Store[] mapStores,
			int offset, int storesPerPage, String remoteIP, String fetchMap,
			int theZoomLevel, int radius, Map currMap, String direction,
			Integer zoomStore) {
		LocatorConfig locatorConfig = getConfig().getLocator();
		boolean storeMap = zoomStore != null;
		Store[] stores = mapStores;
		if(storeMap) {
			if(stores.length > 1)
				stores = new Store[] { stores[zoomStore.intValue()] };
			offset = 0;
		}
		int zoomLevel = theZoomLevel;
		long startTime = System.currentTimeMillis();
		Map out = new Map(app, getConfig());
		out.setStores(stores);
		out.setLocation(location);
		out.setStoreCount(stores.length);
		out.setOffset(offset);
		out.setStreet(street);
		out.setCity(city);
		out.setState(state);
		out.setZip(zip);
		out.setDisplayCount(storesPerPage);
		boolean changeZoom = false;
		if(currMap != null) {
			out.setOriginalInfo(currMap.getOriginalInfo());
			if(zoomLevel != currMap.getZoomLevel().intValue())
				changeZoom = true;
		}
		MapImageInfo mapInfo = null;
		String operation = "store list";
		if ("1".equals(fetchMap) || currMap != null) {
			operation = "store map";
			out.setMapHeight(locatorConfig.getMapHeight().intValue());
			out.setMapWidth(locatorConfig.getMapWidth().intValue());
			double miles = locatorConfig.getZoomLevels()[zoomLevel].doubleValue();
			if(direction == null && !changeZoom && storeMap)
				miles = locatorConfig.getZoomLevels()[locatorConfig.getStoreZoomLevel().intValue()].doubleValue();
			MapArea mapExtent = null;
			try {
				MapImageLocator mapImageLocator = new MapImageLocator();
				IMapImage myMapImage = mapImageLocator.getMapImageHttpPort();
				MapImageOptions mapOptions = getMapImageOptions(stores, offset, storesPerPage);
				Point center = new Point();
				if(direction != null) {
					mapInfo = changeDirection(currMap, center, direction);
					mapInfo = myMapImage.getMaps(new MapArea[] { mapInfo.getMapArea() },
							new MapImageOptions[] { mapOptions }, null)[0];
				} else if(changeZoom || storeMap) {
					if(changeZoom) {
						mapInfo = currMap.getInfo();
						Envelope env = mapInfo.getMapArea().getExtent();
						double midX = (env.getMinX() + env.getMaxX()) / 2.0;
						double midY = (env.getMinY() + env.getMaxY()) / 2.0;
						center.setX(midX);
						center.setY(midY);
					} else {
						Store store = stores[0];
						center.setX(store.getLongitude().doubleValue());
						center.setY(store.getLatitude().doubleValue());
						out.setZoomLevel(locatorConfig.getStoreZoomLevel());
					}
					CircleDesc[] circles = getCircles(center, miles);
					mapOptions.setCircles(circles);
// handle stores not appearing on map
					if(changeZoom) {
						MapImageOptions zoomOptions = getMapImageOptions(null, 0, 0);
						zoomOptions.setCircles(circles);
						mapExtent = myMapImage.getBestMapArea(zoomOptions, 100, null);
					}
					else mapExtent = myMapImage.getBestMapArea(mapOptions, 100, null);
					mapInfo = myMapImage.getMaps(new MapArea[] { mapExtent },
							new MapImageOptions[] { mapOptions }, null)[0];
				} else {
// ensure map is as large as zoom area
					mapExtent = myMapImage.getBestMapArea(mapOptions, 100, null);
					mapOptions.setCircles(getCircles(mapExtent.getCenter(), miles));
					mapInfo = myMapImage.getBestMap(mapOptions, 100, null);
				}
				out.setMapURL(mapInfo.getMapURL());
			} catch (Exception ex) {
				System.err.println("Store Locator ESRI Map Image Exception: "
						+ ex.getMessage());
				System.err.println("caused by " + ex.getClass().getName());
				ex.printStackTrace(System.err);
			}

		}
		out.setInfo(mapInfo);
		if(currMap == null)
			out.setOriginalInfo(mapInfo);
		out.setRadius(new Integer(radius));
		out.setZoomLevel(new Integer(zoomLevel));
		out.setDirty(false);
		return out;
	}

	public MapImageInfo changeDirection(Map currMap, Point center, String direction) {
		MapImageInfo mapInfo = currMap.getInfo();
		Envelope env = mapInfo.getMapArea().getExtent();
		double width = env.getMaxX() - env.getMinX();
		double height = env.getMaxY() - env.getMinY();
		if("center".equals(direction)) {
			mapInfo = currMap.getOriginalInfo();
			env = mapInfo.getMapArea().getExtent();
			double midX = (env.getMinX() + env.getMaxX()) / 2.0;
			double midY = (env.getMinY() + env.getMaxY()) / 2.0;
			center.setX(midX);
			center.setY(midY);
			mapInfo = currMap.getInfo();
			mapInfo.getMapArea().setCenter(center);
			env = mapInfo.getMapArea().getExtent();
			env.setMinX(midX - width / 2.0);
			env.setMaxX(midX + width / 2.0);
			env.setMinY(midY - height / 2.0);
			env.setMaxY(midY + height / 2.0);
		} else {
			if("up".equals(direction)) {
				env.setMaxY(env.getMaxY() + height);
				env.setMinY(env.getMinY() + height);
			} else if("down".equals(direction)) {
				env.setMaxY(env.getMaxY() - height);
				env.setMinY(env.getMinY() - height);
			} else if("left".equals(direction)) {
				env.setMaxX(env.getMaxX() - width);
				env.setMinX(env.getMinX() - width);
			} else if("right".equals(direction)) {
				env.setMaxX(env.getMaxX() + width);
				env.setMinX(env.getMinX() + width);
			}
			double midX = (env.getMinX() + env.getMaxX()) / 2.0;
			double midY = (env.getMinY() + env.getMaxY()) / 2.0;
			center.setX(midX);
			center.setY(midY);
			mapInfo.getMapArea().setCenter(center);
		}
		return mapInfo;
	}
	
	public CircleDesc[] getCircles(Point center, double miles) {
		CircleDesc bound = new CircleDesc();
		Circle circle = new Circle();
		circle.setCenter(center);
		circle.setRadius(miles);
		bound.setFillTransparency(0.000000001);
		bound.setBoundaryTransparency(0.000000001);
		bound.setCircle(circle);
		return new CircleDesc[] { bound };
	}

	public Map getDirections(long id, String app, Map mapIn, Store store, String remoteIP) throws GeocodingException {
		Map out = new Map(app, getConfig());
		RouteStop[] points = new RouteStop[2];
		RouteStop stop = new RouteStop();
		Point point = new Point();
		Float[] location = mapIn.getLocation();
		location = this.geocodeOperator.geocode(id, app, mapIn.getStreet(), mapIn.getCity(), mapIn.getState(), mapIn.getZip(), remoteIP, false);
		if(location == null || location[0] == null || location[1] == null)
			throw new GeocodingException(null);
		long startTime = System.currentTimeMillis();
		point.setX(location[0].doubleValue());
		point.setY(location[1].doubleValue());
		stop.setPoint(point);
		stop.setDesc(mapIn.getDescription());
		points[0] = stop;
		stop = new RouteStop();
		point = new Point();
		long storeNumber = -1L;
		try {
			storeNumber = store.getNumber();
			point.setX(store.getLongitude().doubleValue());
			point.setY(store.getLatitude().doubleValue());
			stop.setPoint(point);
			String addressLabel = this.geocodeOperator.getLocatorDAO().getNameForStoreNumber(store.getNumber()) + ", " + store.getAddress() + ", " 
				+ store.getCity() + ", " + store.getState() + " " + store.getZip();
			stop.setDesc(addressLabel);
			points[1] = stop;
			RouteFinderOptions routeFinderOptions = new RouteFinderOptions();
			routeFinderOptions.setDataSource(getConfig().getLocator().getEsriGeoSource());
			routeFinderOptions.setReturnDirections(true);
			routeFinderOptions.setReturnMap(true);
			RouteOptions routeOptions = new RouteOptions();
//			routeOptions.routeType = "shortest";
			routeFinderOptions.setRouteOptions(routeOptions);
			RouteDisplayOptions routeDisplayOptions = new RouteDisplayOptions();
			routeFinderOptions.setRouteDisplayOptions(routeDisplayOptions);
			Store[] storeArray = new Store[1];
			storeArray[0] = store;
			out.setStreet(store.getAddress());
			out.setCity(store.getCity());
			out.setState(store.getState());
			out.setZip(store.getZip());
			out.setStoreType(this.geocodeOperator.getLocatorDAO().getNameForStoreNumber(storeNumber));
			routeFinderOptions.setRouteMapOptions(getMapImageOptions(storeArray, 0, 1));
			RouteInfo myRouteInfo = null;
			RouteFinderLocator routeFinderLocator = new RouteFinderLocator();
			IRouteFinder myRouteFinder = routeFinderLocator.getRouteFinderHttpPort();
			myRouteInfo = myRouteFinder.findRoute(points, routeFinderOptions, null);
			MapImageInfo mapInfo = myRouteInfo.getRouteMap();
			out.setMapURL(mapInfo.getMapURL());
			out.setMapHeight(getConfig().getLocator().getMapHeight().intValue());
			out.setMapWidth(getConfig().getLocator().getMapWidth().intValue());
			SegmentDesc[] route = myRouteInfo.getSegmentDescs();
			Vector v = new Vector();
			int count = 0;
			for(int i=0; i<route.length; i++) {
				SegmentDesc segment = route[i];
				if(segment.getDescriptiveDirections() != null) {
					String directions = "<td>" + ++count + ". " + segment.getDescriptiveDirections() + "</td><td>";
					if(segment.getDescriptiveDistance() != null)
						directions += segment.getDescriptiveDistance();
					directions += "</td>";
					v.addElement(directions);
				}
			}
			int len = v.size();
			String[] directions = new String[len];
			for(int i=0; i<len; i++)
				directions[i] = (String)v.elementAt(i);
			out.setDirections(directions);
		} catch(Exception ex) {
			System.err.println("Store Locator ESRI Directions Exception: " + ex.getMessage());
			System.err.println("caused by " + ex.getClass().getName());
			ex.printStackTrace(System.err);
		}
		return out;
	}

	public MapImageOptions getMapImageOptions(Store[] stores, int offset, int storesPerPage) {
		MapImageOptions mapOptions = new MapImageOptions();
		mapOptions.setMapImageFormat("gif");
		mapOptions.setDataSource(getConfig().getLocator().getEsriMapSource());
		MapImageSize mapSize = new MapImageSize();
		mapSize.setHeight(getConfig().getLocator().getMapHeight().intValue());
		mapSize.setWidth(getConfig().getLocator().getMapWidth().intValue());
		mapOptions.setMapImageSize(mapSize);
		if(stores != null) {
			Vector v = new Vector();
			int len = stores.length;
			int maxIndex = offset + (storesPerPage - 1);
			if(maxIndex >= len)
				maxIndex = len - 1;
			for(int i=offset; i<=maxIndex; i++) {
				Store currentStore = stores[i];
				MarkerDesc storeMarker = new MarkerDesc();
				Point storeLocation = new Point();
				storeLocation.setX(currentStore.getLongitude().doubleValue());
				storeLocation.setY(currentStore.getLatitude().doubleValue());
				storeMarker.setLocation(storeLocation);
				storeMarker.setIconDataSource(getConfig().getLocator().getEsriUserIconSource());
				storeMarker.setName(getConfig().getLocator().getEsriUserIconPrefix() + String.valueOf(i - offset + 1) + ".gif");
				v.addElement(storeMarker);
			}
			int markerLen = v.size();
			MarkerDesc[] markers = new MarkerDesc[markerLen];
			for(int i=0; i<markerLen; i++) {
				markers[i] = (MarkerDesc)v.elementAt(i);
			}
			mapOptions.setMarkers(markers);
		}
		ScaleBarDesc[] scaleBars = new ScaleBarDesc[1];
		ScaleBarDesc scaleBarDesc = new ScaleBarDesc();
		scaleBars[0] = scaleBarDesc;
		mapOptions.setScaleBars(scaleBars);
		return mapOptions;
	}

}
