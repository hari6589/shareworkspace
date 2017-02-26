/**
 * 
 */
package com.bsro.service.bingmaps.staticmap;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bfrc.pojo.store.Store;


/**
 * @author schowdhu
 *
 */
public class BingMapsStaticMapServiceImpl implements BingMapsStaticMapService {
	
	// Internal state
	private static final Log		log			= LogFactory.getLog( BingMapsStaticMapServiceImpl.class );

	// Caches
	private Map<Integer, String>	imgCache		= new HashMap<Integer, String>();
	private Map<Integer, String>	divCache		= new HashMap<Integer, String>();
	private Map<Integer, String>	linkCache		= new HashMap<Integer, String>();

	// Bean fields
	private String					baseUrl;
	private String					secureCall;
	private Integer					smallPushpinIconId;
	private Integer					largePushpinIconId;
	private Integer					defaultZoomLevel;
	private String					imagerySet;
	private String					imageFormat;
	private String					mapMetadata;
	private String					mapLayer;
	
	//Internal fields
	private String					pushPinLocation;
	private String					centerPoint;

	// Bean fields for key parameter support
	private boolean					includeApiKey;
	private String					apiKey;
	
	/**
	 * Generates <img> element.
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &amp;zoom=13
	 * &amp;size=[width]x[height]
	 * &amp;imagerySet=Road
	 * &amp;pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 */
	public String getImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, String id, String cssClass, String alt, boolean hideTracking, boolean disableLiveMaps ) {

		if( store == null ) {
			log.error( "buildImgElement was called with a null store. Empty map URL was generated." );
			return "";
		}

		int cacheKey = buildImgCacheKey( store, width, height, sourceImageWidth, sourceImageHeight, zoom, id, cssClass, alt, hideTracking, disableLiveMaps );

		String element = imgCache.get( cacheKey );

		if( element == null ) {
			element = generateImgElement( store, width, height, sourceImageWidth, sourceImageHeight, zoom, id, cssClass, alt, hideTracking, disableLiveMaps );
			imgCache.put( cacheKey, element );
		}
		log.info("ImgElement="+element);
		return element;
	}
	
	/**
	 * Generates <div> element with the image as the background.
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &amp;zoom=13
	 * &amp;mapSize=[width]x[height]
	 * &amp;imagerySet=Road
	 * &amp;pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 * 
	 * The full link is URL encoded.
	 */
	public String getDivImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, Integer mapShiftVertical, Integer mapShiftHorizontal, String id, String cssClass,
			String imgFloat, Integer marginRight, boolean hideTracking, boolean disableLiveMaps ) {

		if( store == null ) {
			log.error( "buildDivImgElement was called with a null store. Empty map URL was generated." );
			return "";
		}

		int cacheKey = buildDivImgCacheKey( store, width, height, sourceImageWidth, sourceImageHeight, zoom, mapShiftVertical, mapShiftHorizontal, id, cssClass, imgFloat, marginRight, hideTracking, disableLiveMaps );
		String element = divCache.get( cacheKey );

		if( element == null ) {
			element = generateDivImgElement( store, width, height, sourceImageWidth, sourceImageHeight, zoom, mapShiftVertical, mapShiftHorizontal, id, cssClass, imgFloat, marginRight, hideTracking, disableLiveMaps );
			divCache.put( cacheKey, element );
		}
		log.info("DivImgElement = "+ element);
		return element;
	}
	
	/**
	 * Rapidly generates the key for use with this cache. NPE safe.
	 * 
	 * @param store
	 * @param width
	 * @param height
	 * @param sourceImageWidth
	 * @param sourceImageHeight
	 * @param zoom
	 * @param id
	 * @param cssClass
	 * @param alt
	 * @param hideTracking
	 * @param disableLiveMaps
	 * @return
	 */
	private int buildDivImgCacheKey( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, Integer mapShiftVertical, Integer mapShiftHorizontal, String id, String cssClass,
			String imgFloat, Integer marginRight, boolean hideTracking, boolean disableLiveMaps ) {

		StringBuilder key = new StringBuilder( 200 );

		key.append( (store != null && store.getStoreNumber() != null) ? store.getStoreNumber() : "null" );
		key.append( (width != null) ? width : "null" );
		key.append( (height != null) ? height : "null" );
		key.append( (sourceImageWidth != null) ? sourceImageWidth : "null" );
		key.append( (sourceImageHeight != null) ? sourceImageHeight : "null" );
		key.append( (mapShiftHorizontal != null) ? mapShiftHorizontal : "null" );
		key.append( (mapShiftVertical != null) ? mapShiftVertical : "null" );
		key.append( (zoom != null) ? zoom : "null" );
		key.append( (id != null) ? id : "null" );
		key.append( (cssClass != null) ? cssClass : "null" );
		key.append( (imgFloat != null) ? imgFloat : "null" );
		key.append( (marginRight != null) ? marginRight : "null" );

		key.append( hideTracking );
		key.append( disableLiveMaps );

		return key.toString().hashCode();
	}


	/**
	 * Builds the actual <div> element, with the background set to the image.
	 * 
	 * <div id="[id]" class="[cssClass]" alt="[alt]"
	 * style="background-image:url('http://dev.virtualearth.net/REST/v1/Imagery/Map/[See getLink code]'); width: 150px; height: 90px;"></div>
	 * 
	 * @param store
	 * @param height
	 * @param width
	 * @param zoom
	 * @param id
	 * @param cssClass
	 * @param alt
	 * @return
	 */
	private String generateDivImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, Integer mapShiftVertical, Integer mapShiftHorizontal, String id, String cssClass,
			String imgFloat, Integer marginRight, boolean hideTracking, boolean disableLiveMaps ) {

		// Set default values

		if( width == null || width < 1 ) {
			log.warn( "Width was " + width + ". Defaulting to 100." );
			width = 100;
		}
		if( height == null || height < 1 ) {
			log.warn( "Height was " + height + ". Defaulting to 100." );
			height = 100;
		}

		if( sourceImageWidth == null || sourceImageWidth < 1 ) {
			sourceImageWidth = width;
		}

		if( sourceImageHeight == null || sourceImageHeight < 1 ) {
			sourceImageHeight = height;
		}

		// Calculate Offsets
		int positionHorizontal = calculatePosition( sourceImageWidth, width, mapShiftHorizontal );
		int positionVertical = calculatePosition( sourceImageHeight, height, mapShiftVertical );

		// Build the String
		StringBuilder builder = new StringBuilder( 500 );

		builder.append( "<div" );

		if( notEmptyString( id ) ) {
			builder.append( " id=\"" ).append( id ).append( "\"" );
		}

		if( notEmptyString( cssClass ) ) {
			builder.append( " class=\"" ).append( cssClass ).append( "\"" );
		}

		builder.append( " style=\"" );
		builder.append( "background-image:url('" ).append( getLink( store, sourceImageWidth, sourceImageHeight, zoom, true, hideTracking, disableLiveMaps ) ).append( "');" );
		builder.append( " width:" ).append( width ).append( "px;" );
		builder.append( " height:" ).append( height ).append( "px;" );
		
		if( notEmptyString( imgFloat ) ) {
			builder.append( " float:" ).append( imgFloat ).append( ";" );
		}
		
		if (marginRight != null) {
			builder.append( " margin-right:" ).append( marginRight ).append( "px;" );
		}

		if( positionHorizontal != 0 || positionVertical != 0 ) {
			builder.append( " background-position:" );
			builder.append( positionHorizontal ).append( "px " );
			builder.append( positionVertical ).append( "px;" );
		}

		builder.append( "\"></div>" );

		return builder.toString();
	}

	
	/**
	 * Builds the actual <img> element, per specs.
	 * 
	 * @param store
	 * @param height
	 * @param width
	 * @param zoom
	 * @param id
	 * @param cssClass
	 * @param alt
	 * @return
	 */
	private String generateImgElement( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, String id, String cssClass, String alt, boolean hideTracking, boolean disableLiveMaps ) {

		StringBuilder builder = new StringBuilder( 500 );

		builder.append( "<img" );

		if( notEmptyString( id ) ) {
			builder.append( " id=\"" ).append( id ).append( "\"" );
		}

		if( notEmptyString( cssClass ) ) {
			builder.append( " class=\"" ).append( cssClass ).append( "\"" );
		}

		if( width == null || width < 1 ) {
			log.warn( "Width was " + width + ". Defaulting to 100." );
			width = 100;
		}
		builder.append( " width=\"" ).append( width ).append( "\"" );

		if( height == null || height < 1 ) {
			log.warn( "Height was " + height + ". Defaulting to 100." );
			height = 100;
		}
		builder.append( " height=\"" ).append( height ).append( "\"" );

		if( sourceImageWidth == null || sourceImageWidth < 1 ) {
			sourceImageWidth = width;
		}

		if( sourceImageHeight == null || sourceImageHeight < 1 ) {
			sourceImageHeight = height;
		}

		builder.append( " src=\"" ).append( getLink( store, sourceImageWidth, sourceImageHeight, zoom, true, hideTracking, disableLiveMaps ) ).append( "\"" );

		if( notEmptyString( alt ) ) {
			builder.append( " alt=\"" ).append( alt ).append( "\"" );
		}

		builder.append( " />" );

		return builder.toString();
	}
	
	/** 
	 * Rapidly generates the key for use with this cache. NPE safe.
	 * 
	 * @param store
	 * @param width
	 * @param height
	 * @param zoom
	 * @param encodeAmpersands
	 * @param hideTracking
	 * @return
	 */
	private int buildLinkCacheKey( Store store, Integer width, Integer height, Integer zoom, boolean encodeAmpersands, boolean hideTracking ) {

		StringBuilder key = new StringBuilder( 200 );

		key.append( (store != null && store.getStoreNumber() != null) ? store.getStoreNumber() : "null" );
		key.append( (width != null) ? width : "null" );
		key.append( (height != null) ? height : "null" );
		key.append( (zoom != null) ? zoom : "null" );

		key.append( encodeAmpersands );
		key.append( hideTracking );

		return key.toString().hashCode();
	}
	
	/**
	 * Rapidly generates the key for use with this cache. NPE safe. 
	 * No longer used
	 * 
	 * @param store
	 * @param width
	 * @param height
	 * @param sourceImageWidth
	 * @param sourceImageHeight
	 * @param zoom
	 * @param id
	 * @param cssClass
	 * @param alt
	 * @param hideTracking
	 * @param disableLiveMaps
	 * @return
	 */
	private int buildImgCacheKey( Store store, Integer width, Integer height, Integer sourceImageWidth, Integer sourceImageHeight, Integer zoom, String id, String cssClass, String alt, boolean hideTracking, boolean disableLiveMaps ) {

		StringBuilder key = new StringBuilder( 200 );

		key.append( (store != null && store.getStoreNumber() != null) ? store.getStoreNumber() : "null" );
		key.append( (width != null) ? width : "null" );
		key.append( (height != null) ? height : "null" );
		key.append( (sourceImageWidth != null) ? sourceImageWidth : "null" );
		key.append( (sourceImageHeight != null) ? sourceImageHeight : "null" );
		key.append( (zoom != null) ? zoom : "null" );
		key.append( (id != null) ? id : "null" );
		key.append( (cssClass != null) ? cssClass : "null" );
		key.append( (alt != null) ? alt : "null" );

		key.append( hideTracking );
		key.append( disableLiveMaps );

		return key.toString().hashCode();
	}
	
	/**
	 * Builds the actual URL, based on the supplied parameters and the defined logic of the service.
	 * 
	 * @param store
	 * @param width
	 * @param height
	 * @return
	 */
	private String generateLink( Store store, Integer width, Integer height, Integer zoom, boolean encodeAmpersands, boolean hideTracking ) {

		StringBuilder urlBuilder = new StringBuilder( 500 );

		if( notEmptyString( getBaseUrl() ) ) {
			urlBuilder.append( getBaseUrl() );
		} else {
			log.warn( "Base URL was not defined, therefore default was used." );
			urlBuilder.append( "http://dev.virtualearth.net/REST/v1/Imagery/Map" );
		}

		urlBuilder.append( "/" ).append( getImagerySet() ).append( "/" );
		appendUrlEncodedPart( urlBuilder, generateCenterWithLatLng( store ) );

		urlBuilder.append( "/" );
		if( zoom == null || zoom < 0 ) {
			urlBuilder.append( getDefaultZoomLevel() );
		} else {
			urlBuilder.append( zoom );
		}

		urlBuilder.append( "?ms=" ).append( width ).append( "," ).append( height );

		urlBuilder.append( "&pp=" );
		urlBuilder.append( generatePushPin( store, height ) );

		urlBuilder.append( "&ml=" );
		if( notEmptyString( getMapLayer()) ) {
			urlBuilder.append( getMapLayer() );
		} else {
			urlBuilder.append( "TrafficFlow" );
		}

		urlBuilder.append( "&fmt=" );
		if( notEmptyString( getImageFormat()) ) {
			urlBuilder.append( getImageFormat() );
		} else {
			urlBuilder.append( "png" );
		}
		
		urlBuilder.append( "&mmd=" );
		if( notEmptyString( getMapMetadata()) ) {
			urlBuilder.append( getMapMetadata() );
		} else {
			urlBuilder.append( "0" );
		}

		if( !hideTracking ) {

			 if( isIncludeApiKey() && notEmptyString( getApiKey() ) ) {
				urlBuilder.append( "&key=" ).append( getApiKey().trim() );
			}
		}

		if( encodeAmpersands ) {
			return urlBuilder.toString().replace( "&", "&amp;" );
		} else {
			return urlBuilder.toString();
		}
	}

	
	/**
	 * Generates link to staticmap from Google
	 * 
	 * Here is the logic for the link itself based on the following format:
	 * 
	 * http://dev.virtualearth.net/REST/v1/Imagery/Map
	 * center=[store.lat],[store.lng]
	 * &zoom=[zoom]
	 * &size=[width]x[height]
	 * &imagerySet=Road
	 * &pushPin=icon:[URL to pin icon]|[store.lat],[store.lng]
	 */
	public String getLink( Store store, Integer width, Integer height, Integer zoom, boolean encodeAmpersands, boolean hideTracking, boolean disableLiveMaps ) {

		if( store == null ) {
			log.error( "buildLink was called with a null store. Empty map URL was generated." );
			return "";
		}

		int cacheKey = buildLinkCacheKey( store, width, height, zoom, encodeAmpersands, hideTracking );

		String link = linkCache.get( cacheKey );

		if( link == null ) {
			link = generateLink( store, width, height, zoom, encodeAmpersands, hideTracking );
			linkCache.put( cacheKey, link );
		}

		return link;
	}
	
	/**
	 * Builds the actual URL, based on the supplied parameters and the defined logic of the service.
	 * 
	 * @param store
	 * @param width
	 * @param height
	 * @return
	 */
	private String generateMultiPinLink( List<Store> stores, Integer width, Integer height, Integer zoom, boolean encodeAmpersands, boolean hideTracking ) {

		StringBuilder urlBuilder = new StringBuilder( 500 );

		if( notEmptyString( getBaseUrl() ) ) {
			urlBuilder.append( getBaseUrl() );
		} else {
			log.warn( "Base URL was not defined, therefore default was used." );
			urlBuilder.append( "http://dev.virtualearth.net/REST/v1/Imagery/Map" );
		}

		urlBuilder.append( "/" ).append( getImagerySet() ).append( "?" );
		
		urlBuilder.append( "dcl=1" );
		
		if(width != null && height != null){
			urlBuilder.append( "&ms=" ).append( width ).append( "," ).append( height );
		}
		
		//creates all the marker pins with large icons appropriately numbered
		for(int i =0; i < stores.size(); i++){
			urlBuilder.append( "&pp=" );
			urlBuilder.append( generatePushPinWithIndex( stores.get(i), i+1) );
		}

		urlBuilder.append( "&ml=" );
		if( notEmptyString( getMapLayer()) ) {
			urlBuilder.append( getMapLayer() );
		} else {
			urlBuilder.append( "TrafficFlow" );
		}

		urlBuilder.append( "&fmt=" );
		if( notEmptyString( getImageFormat()) ) {
			urlBuilder.append( getImageFormat() );
		} else {
			urlBuilder.append( "png" );
		}
		
		urlBuilder.append( "&mmd=" );
		if( notEmptyString( getMapMetadata()) ) {
			urlBuilder.append( getMapMetadata() );
		} else {
			urlBuilder.append( "0" );
		}
		urlBuilder.append( "&zl=");
		if( zoom == null || zoom < 0 ) {
			urlBuilder.append( getDefaultZoomLevel() );
		} else {
			urlBuilder.append( zoom );
		}
		
		if( !hideTracking ) {
			 if( isIncludeApiKey() && notEmptyString( getApiKey() ) ) {
				urlBuilder.append( "&key=" ).append( getApiKey().trim() );
			}
		}

		if( encodeAmpersands ) {
			return urlBuilder.toString().replace( "&", "&amp;" );
		} else {
			return urlBuilder.toString();
		}

	}
	
	@Override
	public String getMultiPinLink(List<Store> stores, Integer width, Integer height, Integer zoomLevel, 
			boolean encodeAmpersands, boolean hideTracking, boolean disableLiveMaps) {
		
		if( stores == null ) {
			log.error( "buildLink was called with a null stores. Empty map URL was generated." );
			return "";
		}

		return generateMultiPinLink(stores, width, height, zoomLevel, encodeAmpersands, hideTracking);
	}

	
	
	
	/**
	 * Calculate the background position offset.
	 * 
	 * @param largerSize
	 * @param smallerSize
	 * @param additionalOffset
	 * @return
	 */
	private int calculatePosition( Integer largerSize, Integer smallerSize, Integer offset ) {

		int position = 0;

		if( largerSize != smallerSize ) {
			position = (largerSize - smallerSize) / 2 * -1;
		}

		if( offset != null ) {
			position += offset;
		}

		return position;
	}

	/**
	 * Builds the pushpin portion of the URL.
	 * All the push pin icon images are available at the msdn site
	 * <link>http://msdn.microsoft.com/en-us/library/ff701719.aspx</link>
	 * 
	 * @param store
	 *            , of which we'll use the lat/lng
	 * @return URL string part
	 */
	private String generatePushPin( Store store, Integer height ) {

		if( store == null ) {
			return "";
		}

		Integer iconId = determinePinIconId( height );

		if( iconId != null ) {

			StringBuilder builder = new StringBuilder( 150 );
			builder.append( store.getLatitude() );
			builder.append( "," );
			builder.append( store.getLongitude() );
			builder.append(";");
			builder.append(String.valueOf(iconId));

			return builder.toString();

		} else {
			return "";
		}

	}
	
	
	/**
	 * Builds the push pin portion of the URL. This is used by the mobile sites
	 * where the static maps have the multiple push pins.
	 * All the push pin icon images are available at the MSDN site
	 * <link>http://msdn.microsoft.com/en-us/library/ff701719.aspx</link>
	 * 
	 * @param store
	 *            , of which we'll use the lat/lng
	 * @return URL string part
	 */
	private String generatePushPinWithIndex( Store store, Integer index) {

		if( store == null ) {
			return "";
		}

		Integer iconId = getLargePushpinIconId();

		if( iconId != null ) {

			StringBuilder builder = new StringBuilder( 150 );
			builder.append( store.getLatitude() );
			builder.append( "," );
			builder.append( store.getLongitude() );
			builder.append(";");
			builder.append(String.valueOf(iconId));
			builder.append(";");
			builder.append(String.valueOf(index));
			
			return builder.toString();

		} else {
			return "";
		}

	}
	

	/**
	 * Determine which pin iconId to use using the following logic:
	 * if the height is less than 100 px, then use the small icon id.
	 * otherwise, use the large icon id.
	 * 
	 * @param height
	 * @return
	 */
	private Integer determinePinIconId( Integer height ) {

		if( height == null ) {
			return getSmallPushpinIconId();
		}

		if( height < 100 && getSmallPushpinIconId() != null ) {
			return getSmallPushpinIconId();
		} else {
			return getLargePushpinIconId();
		}

	}
	/**
	 * Builds the center definition based on the store address.
	 * 
	 * @param store
	 *            , of which we'll use the address fields
	 * @return URL string part
	 */
	private String generateCenterWithAddress( Store store ) {

		if( store == null ) {
			return "";
		}

		StringBuilder builder = new StringBuilder( 150 );

		if( notEmptyString( store.getAddress() ) ) {
			builder.append( store.getAddress() ).append( "," );
		}
		if( notEmptyString( store.getCity() ) ) {
			builder.append( store.getCity() ).append( "," );
		}
		if( notEmptyString( store.getState() ) ) {
			builder.append( store.getState() );
		}
		if( notEmptyString( store.getZip() ) ) {
			builder.append( store.getZip() );
		}

		return builder.toString();
	}


	/**
	 * Builds the center definition based on lat/lng.
	 * 
	 * @param store
	 *            , of which we'll use the address fields
	 * @return URL string part
	 */
	private String generateCenterWithLatLng( Store store ) {

		if( store == null ) {
			return "";
		}

		if( !validLatitude( store.getLatitude() ) || !validLongitude( store.getLongitude() ) ) {
			log.error( "Invalid lat/lng supplied, therefore full address was used. Supplied lat: " + store.getLatitude() + " lng: " + store.getLongitude() );
			return generateCenterWithAddress( store );
		}

		StringBuilder builder = new StringBuilder( 30 );

		builder.append( store.getLatitude() );
		builder.append( "," );
		builder.append( store.getLongitude() );

		return builder.toString();
	}

	
	
	/**
	 * Returns true if the supplied latitude is not null and between -90 and 90.
	 * 
	 * @param latitude
	 * @return
	 */
	private boolean validLatitude( Float latitude ) {

		if( latitude == null ) {
			return false;
		}

		if( latitude < -90 ) {
			return false;
		}

		if( latitude > 90 ) {
			return false;
		}

		return true;
	}


	/**
	 * Returns true if the supplied longitude is not null and between -180 and 180
	 * 
	 * @param longitude
	 * @return
	 */
	private boolean validLongitude( Float longitude ) {

		if( longitude == null ) {
			return false;
		}

		if( longitude < -180 ) {
			return false;
		}

		if( longitude > 180 ) {
			return false;
		}

		return true;
	}


	/**
	 * Internal convenience method to handle duplicate logic building the link.
	 * 
	 * @param buffer
	 *            to append to.
	 * @param part
	 *            part to be appended (lower case, and spaces are exchanged for dashes)
	 */
	private void appendUrlEncodedPart( StringBuilder builder, String part ) {

		if( part != null && part.trim().length() > 0 ) {
			try {
				builder.append( URLEncoder.encode( part, "UTF-8" ) );
			} catch (UnsupportedEncodingException e) {
				log.warn( "Unable to encode URL parameters for Google Maps StaticMap Link, therefore it was generated without encoding.", e );
				builder.append( part );
			}
		}
	}
	
	/**
	 * Simple convenience method, which returns true if the supplied String is not null and has a length greater then zero after it's trimmed.
	 * 
	 * @param str
	 * @return
	 */
	private static boolean notEmptyString( String str ) {
		return (str != null && str.trim().length() > 0);
	}

	/**
	 * @return the includeApiKey
	 */
	public boolean isIncludeApiKey() {
		return includeApiKey;
	}
	
	/**
	 * @param includeApiKey the includeApiKey to set
	 */
	public void setIncludeApiKey(boolean includeApiKey) {
		this.includeApiKey = includeApiKey;
	}

	/**
	 * @return the baseUrl
	 */
	public String getBaseUrl() {
		return baseUrl;
	}
	/**
	 * @param baseUrl the baseUrl to set
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	/**
	 * @return the secureCall
	 */
	public String getSecureCall() {
		return secureCall;
	}
	/**
	 * @param secureCall the secureCall to set
	 */
	public void setSecureCall(String secureCall) {
		this.secureCall = secureCall;
	}
	/**
	 * @return the defaultZoomLevel
	 */
	public Integer getDefaultZoomLevel() {
		return defaultZoomLevel;
	}
	/**
	 * @param defaultZoomLevel the defaultZoomLevel to set
	 */
	public void setDefaultZoomLevel(Integer defaultZoomLevel) {
		this.defaultZoomLevel = defaultZoomLevel;
	}

	/**
	 * @return the imagerySet
	 */
	public String getImagerySet() {
		return imagerySet;
	}

	/**
	 * @param imagerySet the imagerySet to set
	 */
	public void setImagerySet(String imagerySet) {
		this.imagerySet = imagerySet;
	}

	/**
	 * @return the imageFormat
	 */
	public String getImageFormat() {
		return imageFormat;
	}
	/**
	 * @param imageFormat the imageFormat to set
	 */
	public void setImageFormat(String imageFormat) {
		this.imageFormat = imageFormat;
	}
	/**
	 * @return the mapMetadata
	 */
	public String getMapMetadata() {
		return mapMetadata;
	}
	/**
	 * @param mapMetadata the mapMetadata to set
	 */
	public void setMapMetadata(String mapMetadata) {
		this.mapMetadata = mapMetadata;
	}
	/**
	 * @return the mapLayer
	 */
	public String getMapLayer() {
		return mapLayer;
	}
	/**
	 * @param mapLayer the mapLayer to set
	 */
	public void setMapLayer(String mapLayer) {
		this.mapLayer = mapLayer;
	}


	/**
	 * @return the apiKey
	 */
	public String getApiKey() {
		return apiKey;
	}


	/**
	 * @param apiKey the apiKey to set
	 */
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


	/**
	 * @return the pushPinLocation
	 */
	public String getPushPinLocation() {
		return pushPinLocation;
	}
	/**
	 * @param pushPinLocation the pushPinLocation to set
	 */
	public void setPushPinLocation(String pushPinLocation) {
		this.pushPinLocation = pushPinLocation;
	}
	/**
	 * @return the centerPoint
	 */
	public String getCenterPoint() {
		return centerPoint;
	}
	/**
	 * @param centerPoint the centerPoint to set
	 */
	public void setCenterPoint(String centerPoint) {
		this.centerPoint = centerPoint;
	}

	/**
	 * @return the smallPushpinIconId
	 */
	public Integer getSmallPushpinIconId() {
		return smallPushpinIconId;
	}

	/**
	 * @param smallPushpinIconId the smallPushpinIconId to set
	 */
	public void setSmallPushpinIconId(Integer smallPushpinIconId) {
		this.smallPushpinIconId = smallPushpinIconId;
	}

	/**
	 * @return the largePushpinIconId
	 */
	public Integer getLargePushpinIconId() {
		return largePushpinIconId;
	}

	/**
	 * @param largePushpinIconId the largePushpinIconId to set
	 */
	public void setLargePushpinIconId(Integer largePushpinIconId) {
		this.largePushpinIconId = largePushpinIconId;
	}
	

}
