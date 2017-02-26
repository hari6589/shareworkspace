/**
 * 
 */
package com.bsro.service.bingmaps.staticmap;

import java.util.List;

import com.bfrc.pojo.store.Store;

/**
 * @author schowdhu
 *
 */
public interface BingMapsStaticMapService {
	public static final String	SPRING_KEY	= "bing.maps.staticmap";


	/**
	 * Generates <img> element.
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &amp;zoomLevel=[zoom]
	 * &amp;size=[width]x[height]
	 * &amp;imagerySet=Road
	 * &amp;pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 */
	public String getImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoomLevel, String id, String cssClass, String alt, boolean hideTracking, boolean disableLiveMaps );


	/**
	 * Generates <div> element with the image as the background.
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &amp;zoomLevel=[zoom]
	 * &amp;mapSize=[width]x[height]
	 * &amp;imagerySet=Road
	 * &amp;pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 * 
	 * The full link is URL encoded.
	 */
	public String getDivImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoomLevel, Integer mapShiftVertical, Integer mapShiftHorizontal, String id, String cssClass, String imgFloat, Integer marginRight, 
			boolean hideTracking, boolean disableLiveMaps );


	/**
	 * Generates link to staticmap from Bing
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &zoomLevel=[zoom]
	 * &size=[width]x[height]
	 * &imagerySet=Road
	 * &pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 */
	public String getLink( Store store, Integer width, Integer height, Integer zoomLevel, boolean encodeAmpersands, boolean hideTracking, boolean disableLiveMaps );
	
	/**
	 * Generates link to a staticmap from Bing with multiple push pins(mostly used by the mobile sites)
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &zoomLevel=[zoom]
	 * &imagerySet=Road
	 * &pushPin=[store1.lat],[store1.lng]&pushPin=[store2.lat],[store2.lng]&pushPin=[store3.lat],[store3.lng]
	 */
	public String getMultiPinLink(List<Store> stores, Integer width, Integer height, Integer zoomLevel, boolean encodeAmpersands, boolean hideTracking, boolean disableLiveMaps );
}
