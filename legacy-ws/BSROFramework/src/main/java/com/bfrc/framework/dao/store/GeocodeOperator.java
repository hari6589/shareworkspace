package com.bfrc.framework.dao.store;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.GeoDataDAO;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.geodata.BsroCityStateGeoData;
import com.bfrc.pojo.geodata.BsroCityStateGeoDataId;
import com.bfrc.pojo.geodata.BsroStateGeoData;
import com.bfrc.pojo.geodata.BsroZipGeoData;
import com.bfrc.storelocator.util.GeocodingException;
import com.bfrc.storelocator.util.Heartbeat;

public class GeocodeOperator extends BusinessOperatorSupport {
	Log logger = LogFactory.getLog(GeocodeOperator.class);
	
	private Heartbeat heartbeat;
	private LocatorDAO locatorDAO;
	private map.States states;
	private GeoDataDAO geoDataDAO;
	private MailManager mailManager;
	private String geocodingQuotaExceededNotificationEmailAddresses;
	private final String NULL_TEXT = "null";
	private String bingmapsApiKey;
	private String bingmapsGeoLocationApiUrl;
	private String bingmapsGeoLocationOutputType;
	private String bingmapsGeoLocationApiVersion;
	
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_OK 		= "200";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_BAD 		= "400";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_NOTFOUND = "404";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_UNAUTHORIZED = "401";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_VALID = "ValidCredentials";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_INVALID = "InvalidCredentials";
	private static final String BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_NONE = "NoCredentials";
    
	public LocatorDAO getLocatorDAO() {
		return this.locatorDAO;
	}

	public void setLocatorDAO(LocatorDAO locatorDAO) {
		this.locatorDAO = locatorDAO;
	}

	public void setStates(map.States states) {
		this.states = states;
	}

	public map.States getStates() {
		return this.states;
	}

	public GeoDataDAO getGeoDataDAO() {
		return this.geoDataDAO;
	}

	public void setGeoDataDAO(GeoDataDAO geoDataDAO) {
		this.geoDataDAO = geoDataDAO;
	}

	public MailManager getMailManager() {
		return mailManager;
	}

	public void setMailManager(MailManager mailManager) {
		this.mailManager = mailManager;
	}
	
	public String getGeocodingQuotaExceededNotificationEmailAddresses() {
		return geocodingQuotaExceededNotificationEmailAddresses;
	}

	public void setGeocodingQuotaExceededNotificationEmailAddresses(
			String geocodingQuotaExceededNotificationEmailAddresses) {
		this.geocodingQuotaExceededNotificationEmailAddresses = geocodingQuotaExceededNotificationEmailAddresses;
	}


	public String getBingmapsApiKey() {
		return bingmapsApiKey;
	}

	public void setBingmapsApiKey(String bingmapsApiKey) {
		this.bingmapsApiKey = bingmapsApiKey;
	}

	public String getBingmapsGeoLocationApiUrl() {
		return bingmapsGeoLocationApiUrl;
	}

	public void setBingmapsGeoLocationApiUrl(String bingmapsGeoLocationApiUrl) {
		this.bingmapsGeoLocationApiUrl = bingmapsGeoLocationApiUrl;
	}

	public String getBingmapsGeoLocationOutputType() {
		return bingmapsGeoLocationOutputType;
	}

	public void setBingmapsGeoLocationOutputType(
			String bingmapsGeoLocationOutputType) {
		this.bingmapsGeoLocationOutputType = bingmapsGeoLocationOutputType;
	}

	public String getBingmapsGeoLocationApiVersion() {
		return bingmapsGeoLocationApiVersion;
	}

	public void setBingmapsGeoLocationApiVersion(
			String bingmapsGeoLocationApiVersion) {
		this.bingmapsGeoLocationApiVersion = bingmapsGeoLocationApiVersion;
	}

	public Object operate(Object o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * <p>
	 * Gets the Geo-location of an address using Bing
	 * </p>
	 * @param id
	 * @param app
	 * @param address
	 * @param city
	 * @param state
	 * @param zip
	 * @param remoteIP
	 * @return
	 * @throws GeocodingException
	 */
	public Float[] geoLocationWithBing(long id, String app, String address,
			String city, String state, String zip, String remoteIP)
			throws GeocodingException {
		address = address == null ?  null : address.trim();
		city = city == null ?  null : city.trim();
		state = state == null ?  null : state.trim();
		zip = zip == null ?  null : zip.trim();
		//---  do validation here before it hits saving data to locator_log 20100707 cs.---//
		//--- check if a zip code entry so route it to the Zip code cache checking --//
        if(Util.isValidZipCode(address) && ServerUtil.isNullOrEmpty(zip)) {
			zip = address;
			address = null;
		}
		if(!ServerUtil.isNullOrEmpty(address) && address.trim().length() > 50)
			throw new  GeocodingException("Address is too long","");
		if(!ServerUtil.isNullOrEmpty(city) && city.trim().length() > 50)
			throw new  GeocodingException("City is too long","");
		if(!ServerUtil.isNullOrEmpty(state) && state.trim().length() > 2)
			throw new  GeocodingException("State is too long","");
		if(!ServerUtil.isNullOrEmpty(zip) && zip.trim().length() > 10)
			throw new  GeocodingException("Zip code is too long","");
		if(!ServerUtil.isNullOrEmpty(zip) && !Util.isValidZipCode(zip))
			throw new  GeocodingException("Not a Valid Zip Code","");
		
		
		String srcString = "-go";
		long startTime = System.currentTimeMillis();
		Float position[] = null;
		boolean bingRestCallFailed = false;
		try {
			//Bing Maps REST Location API v1 call
			Map gdata = null;
			if (ServerUtil.isNullOrEmpty(address)) {
				if (!ServerUtil.isNullOrEmpty(zip)) {
					String lookupId = zip;
					if (zip.length() > 5) // --- use the five digits only
						lookupId = zip.substring(0, 5);
					// System.out.println("\t\t\t============="+lookupId);
					BsroZipGeoData cachedata = geoDataDAO.findBsroZipGeoData(lookupId);
					if (cachedata != null) {
						// System.out.println("\t\t\t============= GET FROM CACHE"+lookupId);
						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
							// System.out.println("\t\t\t============= GET FROM CACHE EXPIRED "+lookupId);
							gdata = getGeoData(address, city, state, zip);
							position = (Float[])gdata.get("point");
							//position = checkGoogle(address, city, state, zip);
							if (position != null
									&& position[0].floatValue() != 0
									&& position[1].floatValue() != 0) {
								cachedata.setLatitude(position[1]);
								cachedata.setLongitude(position[0]);
								cachedata.setSource((String)gdata.get("source"));
								geoDataDAO.updateBsroZipGeoData(cachedata);
							} else {
								bingRestCallFailed = true;
							}
						} else {
							// System.out.println("\t\t\t============= GET FROM CACHE EXPIRED "+lookupId);
							position = new Float[2];
							position[1] = cachedata.getLatitude();
							position[0] = cachedata.getLongitude();
						}
					} else {
						// System.out.println("\t\t\t============= GET FROM GOOGLE "+lookupId);
						gdata = getGeoData(address, city, state, zip);
						position = (Float[])gdata.get("point");
						//position = checkGoogle(address, city, state, zip);
						// System.out.println(" -----------------   GOt FROM GOOGLE + "+position[0]);
						if (position != null && position[0].floatValue() != 0
								&& position[1].floatValue() != 0) {
							cachedata = new BsroZipGeoData();
							cachedata.setZip(lookupId);
							cachedata.setLatitude(position[1]);
							cachedata.setLongitude(position[0]);
							cachedata.setSource((String)gdata.get("source"));
							try {
								geoDataDAO.createBsroZipGeoData(cachedata);
							} catch (Exception ex) {
								printCacheDataErrorMessage("GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").geocodeWithBing call to createBsroZipGeoData ", cachedata, ex);								
								// -- ignore error
							}
						} else {
							bingRestCallFailed = true;
						}
					}

				} else if (!ServerUtil.isNullOrEmpty(city)
						&& !ServerUtil.isNullOrEmpty(state)) {
					BsroCityStateGeoDataId lookupId = new BsroCityStateGeoDataId(
							city, state);
					BsroCityStateGeoData cachedata = geoDataDAO
							.findBsroCityStateGeoData(lookupId);
					if (cachedata != null) {
						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
							gdata = getGeoData(address, city, state, zip);
							position = (Float[])gdata.get("point");
							//position = checkGoogle(address, city, state, zip);
							if (position != null) {
								cachedata.setLatitude(position[1]);
								cachedata.setLongitude(position[0]);
								geoDataDAO
										.updateBsroCityStateGeoData(cachedata);
							} else {
								bingRestCallFailed = true;
							}
						} else {
							position = new Float[2];
							position[1] = cachedata.getLatitude();
							position[0] = cachedata.getLongitude();
						}
					} else {
						gdata = getGeoData(address, city, state, zip);
						position = (Float[])gdata.get("point");
						//position = checkGoogle(address, city, state, zip);
						if (position != null && position[0].floatValue() != 0
								&& position[1].floatValue() != 0) {
							cachedata = new BsroCityStateGeoData();
							cachedata.setId(lookupId);
							cachedata.setLatitude(position[1]);
							cachedata.setLongitude(position[0]);
							try {
								geoDataDAO
										.createBsroCityStateGeoData(cachedata);
							} catch (Exception ex) {
								printCacheDataErrorMessage("GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").geocodeWithBing call to createBsroCityStateGeoData ", cachedata, ex);								
								// -- ignore error
							}
						} else {
							bingRestCallFailed = true;
						}
					}
				} else {
					Object lookupId = state;
					BsroStateGeoData cachedata = geoDataDAO
							.findBsroStateGeoData(lookupId);
					if (cachedata != null) {
						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
							gdata = getGeoData(address, city, state, zip);
							position = (Float[])gdata.get("point");
							//position = checkGoogle(address, city, state, zip);
							if (position != null) {
								cachedata.setLatitude(position[1]);
								cachedata.setLongitude(position[0]);
								geoDataDAO.updateBsroStateGeoData(cachedata);
							} else {
								bingRestCallFailed = true;
							}
						} else {
							position = new Float[2];
							position[1] = cachedata.getLatitude();
							position[0] = cachedata.getLongitude();
						}
					} else {
						gdata = getGeoData(address, city, state, zip);
						position = (Float[])gdata.get("point");
						//position = checkGoogle(address, city, state, zip);
						if (position != null && position[0].floatValue() != 0
								&& position[1].floatValue() != 0) {
						    if(!ServerUtil.isNullOrEmpty(state)){//add validation for state
								cachedata = new BsroStateGeoData();
								cachedata.setState(lookupId.toString());
								cachedata.setLatitude(position[1]);
								cachedata.setLongitude(position[0]);
								try {
									geoDataDAO.createBsroStateGeoData(cachedata);
								} catch (Exception ex) {
									printCacheDataErrorMessage("GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").geocodeWithBing call to createBsroStateGeoData ", cachedata, ex);
									// -- ignore error
								}
						    }
						} else {
							bingRestCallFailed = true;
						}
					}
				}
			} else {// --- will NOT check cache if have address data
				gdata = getGeoData(address, city, state, zip);
				position = (Float[])gdata.get("point");
				//position = checkGoogle(address, city, state, zip);
			}
			if (position !=null && position[0].floatValue() == 0 && position[1].floatValue() == 0)
				bingRestCallFailed = true;
			// position = checkGoogle(address, city, (String)states.get(state),
			// zip);
		} catch (Exception bingEx) {
			StringBuilder sb = new StringBuilder();
			sb.append("\n===========================");
			sb.append("\nError occurred in GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").geocodeWithBing ");
			sb.append("\n\t city: ");sb.append(city == null ? this.NULL_TEXT : city);
			sb.append("\n\t state: ");sb.append(state == null ? this.NULL_TEXT : state);
			sb.append("\n\t zip: ");sb.append(zip == null ? this.NULL_TEXT : zip);
			sb.append("\n\t address: ");sb.append(address == null ? this.NULL_TEXT : address);
			if(position != null){
				sb.append("\n\tposition[0] latitude:");sb.append(position[0].floatValue());
				sb.append("\n\tposition[1] longitude:");sb.append(position[1].floatValue());
			}else{
				sb.append("\n\tposition was null");
			}
			sb.append(getStackTraceAsString(bingEx));
			sb.append("===========================\n");
			System.err.println(sb.toString());
			bingRestCallFailed = true;
			
		}
		
		return position;
	}
	// This function geocodes and address using Google as a primary source and
	// first logic as a backup.
//	public Float[] geocodeWithGoogle(long id, String app, String address,
//			String city, String state, String zip, String remoteIP)
//			throws GeocodingException {
//		address = address == null ?  null : address.trim();
//		city = city == null ?  null : city.trim();
//		state = state == null ?  null : state.trim();
//		zip = zip == null ?  null : zip.trim();
//		//---  do validation here before it hits saving data to locator_log 20100707 cs.---//
//		//--- check if a zip code entry so route it to the Zip code cache checking --//
//        if(Util.isValidZipCode(address) && ServerUtil.isNullOrEmpty(zip)) {
//			zip = address;
//			address = null;
//		}
//		if(!ServerUtil.isNullOrEmpty(address) && address.trim().length() > 50)
//			throw new  GeocodingException("Address is too long","");
//		if(!ServerUtil.isNullOrEmpty(city) && city.trim().length() > 50)
//			throw new  GeocodingException("City is too long","");
//		if(!ServerUtil.isNullOrEmpty(state) && state.trim().length() > 2)
//			throw new  GeocodingException("State is too long","");
//		if(!ServerUtil.isNullOrEmpty(zip) && zip.trim().length() > 10)
//			throw new  GeocodingException("Zip code is too long","");
//		if(!ServerUtil.isNullOrEmpty(zip) && !Util.isValidZipCode(zip))
//			throw new  GeocodingException("Not a Valid Zip Code","");
//		
//		
//		String srcString = "-go";
//		long startTime = System.currentTimeMillis();
//		Float position[] = null;
//		boolean googleFailed = false;
//		try {
//			// --- 20100506 releases: GOOGLE API UPDATES, CACHING GEO DATA FROM
//			// GOOGLE by CS ---//
//			// -- if address is empty check address in the order of of zip, city
//			// state, state
//			Map gdata = null;
//			if (ServerUtil.isNullOrEmpty(address)) {
//				if (!ServerUtil.isNullOrEmpty(zip)) {
//					String lookupId = zip;
//					if (zip.length() > 5) // --- use the five digits only
//						lookupId = zip.substring(0, 5);
//					// System.out.println("\t\t\t============="+lookupId);
//					BsroZipGeoData cachedata = geoDataDAO
//							.findBsroZipGeoData(lookupId);
//					if (cachedata != null) {
//						// System.out.println("\t\t\t============= GET FROM CACHE"+lookupId);
//						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
//							// System.out.println("\t\t\t============= GET FROM CACHE EXPIRED "+lookupId);
//							gdata = getGeoData(address, city, state, zip);
//							position = (Float[])gdata.get("point");
//							//position = checkGoogle(address, city, state, zip);
//							if (position != null
//									&& position[0].floatValue() != 0
//									&& position[1].floatValue() != 0) {
//								cachedata.setLatitude(position[1]);
//								cachedata.setLongitude(position[0]);
//								cachedata.setSource((String)gdata.get("source"));
//								geoDataDAO.updateBsroZipGeoData(cachedata);
//							} else {
//								googleFailed = true;
//							}
//						} else {
//							// System.out.println("\t\t\t============= GET FROM CACHE EXPIRED "+lookupId);
//							position = new Float[2];
//							position[1] = cachedata.getLatitude();
//							position[0] = cachedata.getLongitude();
//						}
//					} else {
//						// System.out.println("\t\t\t============= GET FROM GOOGLE "+lookupId);
//						gdata = getGeoData(address, city, state, zip);
//						position = (Float[])gdata.get("point");
//						//position = checkGoogle(address, city, state, zip);
//						// System.out.println(" -----------------   GOt FROM GOOGLE + "+position[0]);
//						if (position != null && position[0].floatValue() != 0
//								&& position[1].floatValue() != 0) {
//							cachedata = new BsroZipGeoData();
//							cachedata.setZip(lookupId);
//							cachedata.setLatitude(position[1]);
//							cachedata.setLongitude(position[0]);
//							cachedata.setSource((String)gdata.get("source"));
//							try {
//								geoDataDAO.createBsroZipGeoData(cachedata);
//							} catch (Exception ex) {
//								printCacheDataErrorMessage("GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+").geocodeWithGoogle call to createBsroZipGeoData ", cachedata, ex);								
//								// -- ignore error
//							}
//						} else {
//							googleFailed = true;
//						}
//					}
//
//				} else if (!ServerUtil.isNullOrEmpty(city)
//						&& !ServerUtil.isNullOrEmpty(state)) {
//					BsroCityStateGeoDataId lookupId = new BsroCityStateGeoDataId(
//							city, state);
//					BsroCityStateGeoData cachedata = geoDataDAO
//							.findBsroCityStateGeoData(lookupId);
//					if (cachedata != null) {
//						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
//							gdata = getGeoData(address, city, state, zip);
//							position = (Float[])gdata.get("point");
//							//position = checkGoogle(address, city, state, zip);
//							if (position != null) {
//								cachedata.setLatitude(position[1]);
//								cachedata.setLongitude(position[0]);
//								geoDataDAO
//										.updateBsroCityStateGeoData(cachedata);
//							} else {
//								googleFailed = true;
//							}
//						} else {
//							position = new Float[2];
//							position[1] = cachedata.getLatitude();
//							position[0] = cachedata.getLongitude();
//						}
//					} else {
//						gdata = getGeoData(address, city, state, zip);
//						position = (Float[])gdata.get("point");
//						//position = checkGoogle(address, city, state, zip);
//						if (position != null && position[0].floatValue() != 0
//								&& position[1].floatValue() != 0) {
//							cachedata = new BsroCityStateGeoData();
//							cachedata.setId(lookupId);
//							cachedata.setLatitude(position[1]);
//							cachedata.setLongitude(position[0]);
//							try {
//								geoDataDAO
//										.createBsroCityStateGeoData(cachedata);
//							} catch (Exception ex) {
//								printCacheDataErrorMessage("GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+").geocodeWithGoogle call to createBsroCityStateGeoData ", cachedata, ex);								
//								// -- ignore error
//							}
//						} else {
//							googleFailed = true;
//						}
//					}
//				} else {
//					Object lookupId = state;
//					BsroStateGeoData cachedata = geoDataDAO
//							.findBsroStateGeoData(lookupId);
//					if (cachedata != null) {
//						if (geoDataDAO.isCacheExpired(cachedata.getCheckDate())) {
//							gdata = getGeoData(address, city, state, zip);
//							position = (Float[])gdata.get("point");
//							//position = checkGoogle(address, city, state, zip);
//							if (position != null) {
//								cachedata.setLatitude(position[1]);
//								cachedata.setLongitude(position[0]);
//								geoDataDAO.updateBsroStateGeoData(cachedata);
//							} else {
//								googleFailed = true;
//							}
//						} else {
//							position = new Float[2];
//							position[1] = cachedata.getLatitude();
//							position[0] = cachedata.getLongitude();
//						}
//					} else {
//						gdata = getGeoData(address, city, state, zip);
//						position = (Float[])gdata.get("point");
//						//position = checkGoogle(address, city, state, zip);
//						if (position != null && position[0].floatValue() != 0
//								&& position[1].floatValue() != 0) {
//						    if(!ServerUtil.isNullOrEmpty(state)){//add validation for state
//								cachedata = new BsroStateGeoData();
//								cachedata.setState(lookupId.toString());
//								cachedata.setLatitude(position[1]);
//								cachedata.setLongitude(position[0]);
//								try {
//									geoDataDAO.createBsroStateGeoData(cachedata);
//								} catch (Exception ex) {
//									printCacheDataErrorMessage("GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+").geocodeWithGoogle call to createBsroStateGeoData ", cachedata, ex);
//									// -- ignore error
//								}
//						    }
//						} else {
//							googleFailed = true;
//						}
//					}
//				}
//			} else {// --- will NOT check cache if have address data
//				gdata = getGeoData(address, city, state, zip);
//				position = (Float[])gdata.get("point");
//				//position = checkGoogle(address, city, state, zip);
//			}
//			if (position !=null && position[0].floatValue() == 0 && position[1].floatValue() == 0)
//				googleFailed = true;
//			// position = checkGoogle(address, city, (String)states.get(state),
//			// zip);
//		} catch (Exception googleEx) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("\n===========================");
//			sb.append("\nError occurred in GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+").geocodeWithGoogle ");
//			sb.append("\n\t city: ");sb.append(city == null ? this.NULL_TEXT : city);
//			sb.append("\n\t state: ");sb.append(state == null ? this.NULL_TEXT : state);
//			sb.append("\n\t zip: ");sb.append(zip == null ? this.NULL_TEXT : zip);
//			sb.append("\n\t address: ");sb.append(address == null ? this.NULL_TEXT : address);
//			if(position != null){
//				sb.append("\n\tposition[0] latitude:");sb.append(position[0].floatValue());
//				sb.append("\n\tposition[1] longitude:");sb.append(position[1].floatValue());
//			}else{
//				sb.append("\n\tposition was null");
//			}
//			sb.append(getStackTraceAsString(googleEx));
//			sb.append("===========================\n");
//			System.err.println(sb.toString());
//			googleFailed = true;
//			
//		}
//		/* remove First Logic 
//		if (googleFailed) {
//			try {
//				if (this.heartbeat.isFirstLogicUp()) {
//					position = checkFirstLogic(address, city, state, zip);
//					srcString = "-fl";
//				} else
//					throw new GeocodingException(
//							"Google failed:  -- First Logic Down", "");
//			} catch (Exception flex) {
//				if (!(flex instanceof FirstlogicDownException || flex instanceof GeocodingException)) {
//					System.err
//							.println("Store Locator Firstlogic Geocoding Exception:"
//									+ flex.getMessage());
//					System.err
//							.println("caused by " + flex.getClass().getName());
//				}
//				//
//				//if (flex instanceof GeocodingException) {
//				//	String flError = ErrorUtil
//				//			.getMessageForError(((GeocodingException) flex)
//				//					.getFaultCode());
//				//	if (flError != null)
//				//		throw (GeocodingException) flex;
//				//}
//			}
//		}
//*/
//		try {
//			this.locatorDAO.log(id, app, remoteIP, "geocode" + srcString,
//					System.currentTimeMillis() - startTime, address, city,
//					state, zip);
//		} catch (Exception ex) {
//			StringBuilder sb = new StringBuilder();
//			sb.append("\n============= Error in GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+").geocodeWithGoogle locatorDao.log ");
//			sb.append("\n\tid=");sb.append(id);
//			sb.append("\n\tapp=");sb.append(app == null ? this.NULL_TEXT : app);
//			sb.append("\n\tremoteIP=");sb.append(remoteIP == null ? this.NULL_TEXT : remoteIP);
//			sb.append("\n\tsrcString=");sb.append(srcString == null ? this.NULL_TEXT : srcString);
//			sb.append("\n\tADDRESS=");sb.append(address == null ? this.NULL_TEXT : address);
//			sb.append("\n\tCITY=");sb.append(city == null ? this.NULL_TEXT : city);
//			sb.append("\n\tSTATE=");sb.append(state == null ? this.NULL_TEXT : state);
//			sb.append("\n\tZIP CODE=");sb.append(zip == null ? this.NULL_TEXT : zip);
//			sb.append("===========================\n");
//			sb.append(getStackTraceAsString(ex));
//			System.err.println(sb.toString());
//			
//		}
//
//		return position;
//	}
	
	private void printCacheDataErrorMessage(String source,
			BsroStateGeoData cachedata, Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("============");
		sb.append(source);
		sb.append("\nCacheData:");
		sb.append("\n\tstate:");sb.append(cachedata.getState());
		sb.append("\tcheckDate:");sb.append(cachedata.getCheckDate());
		sb.append("\tlatitude:");sb.append(cachedata.getLatitude());
		sb.append("\tlongitude:");sb.append(cachedata.getLongitude());
		sb.append("\nEXCEPTION:");
		sb.append(getStackTraceAsString(ex));
		sb.append("============\n");
		System.err.println(sb.toString());
		
	}

	private void printCacheDataErrorMessage(String source,
			BsroCityStateGeoData cachedata, Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("============");
		sb.append(source);
		sb.append("\n CacheData:");
		sb.append("\n\tcity:");sb.append(cachedata.getId().getCity());
		sb.append("\tstate:");sb.append(cachedata.getId().getState());
		sb.append("\tcheckDate:");sb.append(cachedata.getCheckDate());
		sb.append("\tlatitude:");sb.append(cachedata.getLatitude());
		sb.append("\tlongitude:");sb.append(cachedata.getLongitude());
		sb.append("\nEXCEPTION:");
		sb.append(getStackTraceAsString(ex));
		sb.append("============\n");
		System.err.println(sb.toString());
		
	}

	private void printCacheDataErrorMessage(String source, BsroZipGeoData cachedata, Exception ex) {
		StringBuilder sb = new StringBuilder();
		sb.append("============");
		sb.append(source);
		sb.append("\n CacheData:");
		sb.append("\n\tsource:");sb.append(cachedata.getSource());
		sb.append("\tzip:");sb.append(cachedata.getZip());
		sb.append("\tcheckDate:");sb.append(cachedata.getCheckDate());
		sb.append("\tlatitude:");sb.append(cachedata.getLatitude());
		sb.append("\tlongitude:");sb.append(cachedata.getLongitude());
		sb.append("\nEXCEPTION:");
		sb.append(getStackTraceAsString(ex));
		sb.append("============\n");
		System.err.println(sb.toString());
	}

	public Float[] geocode(long id, String app, String address, String city,
			String state, String zip, String remoteIP, boolean forceEsri)
			throws GeocodingException {
		//return geocodeWithGoogle(id, app, address, city, state, zip, remoteIP);
		return geoLocationWithBing(id, app, address, city, state, zip, remoteIP);
	}
/*
	public Float[] geocode(long id, String app, String address, String city,
			String state, String zip, String remoteIP, boolean forceEsri)
			throws GeocodingException {
		return geocode(id, app, address, city, state, zip, remoteIP, forceEsri,
				false);
	}
	*/
/*
	public Float[] geocode(long id, String app, String address, String city,
			String state, String zip, String remoteIP, boolean forceEsri,
			boolean triedAddressCorrection) throws GeocodingException {
		String srcString = "";
		long startTime = System.currentTimeMillis();
		Float position[] = null;
		// System.err.println("GEOCODE: Starting geocode for address=" + address
		// + ", city=" + city
		// + ", state=" + state + ", zip=" + zip + ", forceESRI=" + forceEsri);
		try {
			if (!forceEsri && this.heartbeat.isFirstLogicUp()) {
				// System.err.println("GEOCODE: Trying FL");
				position = checkFirstLogic(address, city, state, zip);
				// System.err.println("GEOCODE: Response from FL: " + position);
				srcString = "-fl";
			} else {
				// System.err.println("GEOCODE: Trying ESRI since heartbeat says down");
				position = checkEsri(id, app, address, city, state, zip,
						remoteIP, triedAddressCorrection);
			}
			// FIXME find way to deal with geocoding exception / make sure error
			// messages are displayed properly
		} catch (Exception flex) {
			// System.err.println("GEOCODE: Trying ESRI, exception was " +
			// flex);
			if (!(flex instanceof FirstlogicDownException || flex instanceof GeocodingException)) {
				System.err
						.println("Store Locator Firstlogic Geocoding Exception:"
								+ flex.getMessage());
				System.err.println("caused by " + flex.getClass().getName());
				// flex.printStackTrace(System.err);
			}
			
			//if (flex instanceof GeocodingException) {
			//	String flError = ErrorUtil
			//			.getMessageForError(((GeocodingException) flex)
			//					.getFaultCode());
			//	// System.err.println("GEOCODE: FL error code: " + flError);
			//	if (flError != null)
			//		throw (GeocodingException) flex;
			//}
			
			position = checkEsri(id, app, address, city, state, zip, remoteIP,
					triedAddressCorrection);
			// System.err.println("GEOCODE: Position from failover: " +
			// position);
		}
		try {
			this.locatorDAO.log(id, app, remoteIP, "geocode" + srcString,
					System.currentTimeMillis() - startTime, address, city,
					state, zip);
		} catch (Exception ex) {
			System.err.println("ADDRESS=" + address);
			System.err.println("CITY=" + city);
			System.err.println("STATE=" + state);
			System.err.println("ZIP CODE=" + zip);
		}
		return position;
	}
*/
	
public boolean isZipCodeInCache(String zip) {
	if (zip == null) {
		return false;
	} else {
		try {
			BsroZipGeoData cachedata = geoDataDAO.findBsroZipGeoData(zip);
			if (cachedata != null && cachedata.getZip() != null && cachedata.getZip().equals(zip)) {
				return true;
			}
		} catch (Throwable throwable) {
			throwable.printStackTrace();
			return false;
		}
	}
	return false;
}

public Map getBingGeoLocationData(String addressString, String cityString, String stateString, String zipString) throws Exception {

	Map resultMap = new HashMap();

	String address = "";
	String country="";
	String statusCode="";
	Float[] longitudeAndLatitude = new Float[2];

	if (addressString != null) {
		address += addressString + " ";
	}
	if (cityString != null) {
		address += cityString + " ";
	}
	if (stateString != null) {
		address += stateString + " ";
	}
	if (zipString != null) {
		address += zipString;
	}

	// Format the url correctly, and make the call to Bing.
	address = java.net.URLEncoder.encode(address.trim(), "UTF-8");
	if(StringUtils.isNullOrEmpty(address)) {
		return resultMap;
	}

	boolean isAddressAZipCode = Util.isValidZipCode(address);

	StringBuilder urlBuilder = new StringBuilder();
	urlBuilder.append(bingmapsGeoLocationApiUrl+"?q=");
	urlBuilder.append(address);
	urlBuilder.append("&o="+java.net.URLEncoder.encode(bingmapsGeoLocationOutputType, "UTF-8"));
	urlBuilder.append("&key="+bingmapsApiKey);

	String url = urlBuilder.toString();

	String response = null;

	try {
		if (logger.isInfoEnabled()) {
			logger.info("GeocodeOperator(Bing Rest Api v"+bingmapsGeoLocationApiVersion+":"+bingmapsGeoLocationApiUrl);
		}
		response = com.bfrc.framework.util.ServerUtil.grabPage(url);

		JSONObject json = new JSONObject(response);

		JSONArray resourceSets = null;
		JSONObject resource = null;
		JSONArray geocodePoints = null;
		String lat = null;
		String lng = null;

		statusCode = String.valueOf(json.getInt("statusCode"));
		//System.out.println("statusCode="+statusCode);

		String authResultsCode = json.getString("authenticationResultCode");

		if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_VALID.equalsIgnoreCase(authResultsCode)){

			if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_OK.equalsIgnoreCase(statusCode)) {

				resourceSets = json.getJSONArray("resourceSets");
				if (resourceSets.length() > 0 && resourceSets.getJSONObject(0).getJSONArray("resources").length() > 0) {

					resource = resourceSets.getJSONObject(0).getJSONArray("resources").getJSONObject(0);
					if(resource != null && resource.getJSONArray("geocodePoints").length() > 0){
						geocodePoints = resource.getJSONArray("geocodePoints").getJSONObject(0).getJSONArray("coordinates");
						lat = String.valueOf(geocodePoints.get(0));
						lng = String.valueOf(geocodePoints.get(1));
						longitudeAndLatitude[0] = new Float(lng);
						longitudeAndLatitude[1] = new Float(lat);
					}

					JSONObject addressJSON = resource.getJSONObject("address"); 
					country = addressJSON.getString("countryRegion");

					if("United States".equals(country) && longitudeAndLatitude[1] != null && longitudeAndLatitude[0] != null) {
						resultMap.put("statusCode", statusCode);
						resultMap.put("position", longitudeAndLatitude);
					} else {
						if (logger.isInfoEnabled()) {
							String output = generateBingLocationGeoDataLogOutput(null, "Unable to find a US location, or missing location data", url, response);	
							logger.info(output);
						}
					}
				}

			} else {					
				if (BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_UNAUTHORIZED.equalsIgnoreCase(statusCode)) {
					// status code - 401
					if (logger.isWarnEnabled()) {
						String output = generateBingLocationGeoDataLogOutput(null, "Uuathorized Url", url, response);					
						logger.warn(output);
					}
				}else if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_BAD.equalsIgnoreCase(statusCode)) {
					// status code - 404
					if (logger.isWarnEnabled()) {
						String output = generateBingLocationGeoDataLogOutput(null, "Bad Request - check again", url, response);					
						logger.warn(output);
					}
				}else if(BINGMAPS_GEOLOCATION_API_RESPONSE_CODE_NOTFOUND.equalsIgnoreCase(statusCode)) {
					// status code - 404
					if (logger.isWarnEnabled()) {
						String output = generateBingLocationGeoDataLogOutput(null, "No Results Found", url, response);					
						logger.warn(output);
					}				
				}
			}	
		}else{
			if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_NONE.equalsIgnoreCase(authResultsCode)){
				//missing credentials
				if (logger.isWarnEnabled()) {
					String output = generateBingLocationGeoDataLogOutput(null, "No Bing maps key entered", url, response);					
					logger.warn(output);
				}	
			}
			else if(BINGMAPS_GEOLOCATION_API_RESPONSE_AUTH_INVALID.equalsIgnoreCase(authResultsCode)){
				// invalid credentials
				if (logger.isWarnEnabled()) {
					String output = generateBingLocationGeoDataLogOutput(null, "Invalid Bing maps key entered", url, response);					
					logger.warn(output);
				}	
			}else{
				//other issues with credentials
				if (logger.isWarnEnabled()) {
					String output = generateBingLocationGeoDataLogOutput(null, "Issue with Bing maps credentials", url, response);					
					logger.warn(output);
				}	
			}
		}
	} catch (Throwable throwable) {
		if (logger.isWarnEnabled()) {
			String output = generateBingLocationGeoDataLogOutput(throwable, "Exception", url, response);					
			logger.warn(output);
		}
	}

	return resultMap;	
}
	
//public Map getGoogleGeoData(String addressString, String cityString, String stateString, String zipString) throws Exception {
//		
//		Map resultMap = new HashMap();
//		
//		String address = "";
//		String country="";
//		String statusCode="";
//		Float[] longitudeAndLatitude = new Float[2];
//
//		if (addressString != null) {
//			address += addressString + " ";
//		}
//		if (cityString != null) {
//			address += cityString + " ";
//		}
//		if (stateString != null) {
//			address += stateString + " ";
//		}
//		if (zipString != null) {
//			address += zipString;
//		}
//
//		// Format the url correctly, and make the call to google.
//		address = java.net.URLEncoder.encode(address.trim(), "UTF-8");
//		if(StringUtils.isNullOrEmpty(address)) {
//			return resultMap;
//		}
// 
//		boolean isAddressAZipCode = Util.isValidZipCode(address);
//		
//		StringBuilder urlBuilder = new StringBuilder();
//		urlBuilder.append("http://maps.googleapis.com/maps/api/geocode/json?address=");
//		urlBuilder.append(address);
//		urlBuilder.append("&components=country");
//		urlBuilder.append(URL_ENCODED_COLON);
//		urlBuilder.append("US");
//		if (isAddressAZipCode) {
//			// The old Google Geocoding API would not return results for an invalid zip code, such as "00000"
//			// What this component filter does is tell the v3 API, "only return matches that ARE the given zip code"
//			// It appears to handle both 5-digit and zip+4 digit zip codes gracefully, even though it only ever returns 5-digit zip codes
//			urlBuilder.append(URL_ENCODED_PIPE);
//			urlBuilder.append("postal_code");
//			urlBuilder.append(URL_ENCODED_COLON);
//			urlBuilder.append(address);
//		}
//		urlBuilder.append("&sensor=false&client=gme-bfrc");
//		
//		String url = urlBuilder.toString();
//		
//		String signedUrl = null;
//		String response = null;
//		
//		try {
//			    signedUrl = StringUtils.signUrl(url);
//			    if (signedUrl != null) {
//			    	// encoding adds a newline to the end of the url
//			    	signedUrl = signedUrl.trim();
//			    }
//				if (logger.isInfoEnabled()) {
//					logger.info("GeocodeOperator(v"+GOOGLE_GEOCODE_API_VERSION+"): "+signedUrl);
//				}
//			    response = com.bfrc.framework.util.ServerUtil.grabPage(signedUrl);
//			    
//				JSONObject json = new JSONObject(response);
//		
//				JSONArray results = null;
//				JSONObject firstResult = null;
//				JSONObject firstResultLocation = null;
//				String lat = null;
//				String lng = null;
//	
//				statusCode = (String) json.get("status");
//				
//				if(GOOGLE_GEOCODING_API_OK_STATUS.equalsIgnoreCase(statusCode)) {
//					statusCode = "200";
//					
//					results = json.getJSONArray("results");
//					if (results.length() > 0) {
//						firstResult = results.getJSONObject(0);
//						firstResultLocation = firstResult.getJSONObject("geometry").getJSONObject("location");
//						lat = firstResultLocation.getString("lat");
//						lng = firstResultLocation.getString("lng");
//
//						longitudeAndLatitude[0] = new Float(lng);
//						longitudeAndLatitude[1] = new Float(lat);
//						
//						JSONArray addressComponents = firstResult.getJSONArray("address_components"); 
//						
//						for(int i=0; i < addressComponents.length(); i++) {
//							JSONObject addressComponent = addressComponents.getJSONObject(i);
//							String addressComponentShortName = addressComponent.getString("short_name");
//							if(addressComponentShortName.equals("US")) {
//								country = addressComponentShortName;
//							}
//						}
//						
//						if("US".equals(country) && longitudeAndLatitude[1] != null && longitudeAndLatitude[0] != null) {
//							resultMap.put("statusCode", statusCode);
//							resultMap.put("position", longitudeAndLatitude);
//						} else {
//							if (logger.isInfoEnabled()) {
//								String output = generateGetGoogleGeoDataLogOutput(null, "Unable to find a US location, or missing location data", url, signedUrl, response);	
//								logger.info(output);
//							}
//						}
//					}
//					
//				} else {					
//					if(GOOGLE_GEOCODING_API_OVER_QUERY_LIMIT_STATUS.equalsIgnoreCase(statusCode)) {
//						// If we exceed the quota, we want to log it and send an email
//						if (logger.isWarnEnabled()) {
//							String output = generateGetGoogleGeoDataLogOutput(null, "Query limit exceeded", url, signedUrl, response);		
//							logger.warn(output);
//						}
//						if (geocodingQuotaExceededNotificationEmailAddresses != null && mailManager != null) {
//							Mail mail = new Mail();
//							String[] toAddresses;
//							toAddresses = geocodingQuotaExceededNotificationEmailAddresses.split(",");
//							if (toAddresses != null) {
//								mail.setTo(toAddresses);
//								mail.setFrom("\"DO-NOT-REPLY\" <webmaster@firestonecompleteautocare.com>");
//								mail.setSubject("BFRC Website error - Google Geocode quota reached ("+ServerUtil.getHostname()+")");
//								mail.setBody("The quota for Google Geocode operations has been reached.  Issues that can contribute to this message are as follows:\n\n1. The per-second usage limit of the geocode operation has been reached.\n\n2. The per-day usage limit of the geocode operation has been reached. The current contract allows for 100,000 calls per day.");
//								mailManager.sendMail(mail);
//							}
//						}
//					} else if (!GOOGLE_GEOCODING_API_ZERO_RESULTS_STATUS.equalsIgnoreCase(statusCode)) {
//						// If there are zero results, that's not a cause for alarm
//						// Any other response should at least be logged, however
//						if (logger.isWarnEnabled()) {
//							String output = generateGetGoogleGeoDataLogOutput(null, "Unexpected status", url, signedUrl, response);					
//							logger.warn(output);
//						}
//					}
//				}				
//			} catch (Throwable throwable) {
//				if (logger.isWarnEnabled()) {
//					String output = generateGetGoogleGeoDataLogOutput(throwable, "Exception", url, signedUrl, response);					
//					logger.warn(output);
//				}
//			}
//			
//		return resultMap;
//	}
 
//	private String generateGetGoogleGeoDataLogOutput(Throwable throwable, String subject, String url, String signedUrl, String response) {
//		StringBuilder sb = new StringBuilder();
//		sb.append("\n=========================== ");
//		if (subject != null) {
//			sb.append(subject).append(": ");
//		}
//		sb.append("GeocodeOperator(v");
//		sb.append(GOOGLE_GEOCODE_API_VERSION);
//		sb.append(").getGoogleGeoData:");
//		sb.append("\n\trequestUrl:");
//		sb.append(url == null ? NULL_TEXT : url);
//		sb.append("\n\tsignedUrl:");
//		sb.append(signedUrl == null ? NULL_TEXT : signedUrl);
//		sb.append("\n");
//		sb.append("\nresponse:\n");
//		sb.append(response);
//		sb.append("\n");
//		if (throwable != null) {
//			sb.append(getStackTraceAsString(throwable));
//		}
//		return sb.toString();
//	}
	
	private String generateBingLocationGeoDataLogOutput(Throwable throwable, String subject, String url, String response) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n=========================== ");
		if (subject != null) {
			sb.append(subject).append(": ");
		}
		sb.append("GeocodeOperator(v");
		sb.append(bingmapsGeoLocationApiVersion);
		sb.append(").getGoogleGeoData:");
		sb.append("\n\trequestUrl:");
		sb.append(url == null ? NULL_TEXT : url);
		sb.append("\n");
		sb.append("\nresponse:\n");
		sb.append(response);
		sb.append("\n");
		if (throwable != null) {
			sb.append(getStackTraceAsString(throwable));
		}
		return sb.toString();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getGeoData(String addressString, String cityString,
			String stateString, String zipString) throws Exception {
		Map m = new HashMap();
		Float[] point = checkBingLocationApi(addressString, cityString, stateString, zipString);
		if(point != null){
			m.put("source", "bing");
			m.put("point", point);
			return m;
		}
		/*Float[] point = checkGoogle(addressString, cityString, stateString, zipString);
		if(point != null){
			m.put("source", "google");
			m.put("point", point);
			return m;
		}*/
		//WI-428: Remove references to Yahoo Geo-coding API call
		/*point = checkYahoo(addressString, cityString, stateString, zipString);
		if(point != null){
			m.put("source", "yahoo");
			m.put("point", point);
			return m;
		}*/
		return m;
	}
	
	public Float[] checkBingLocationApi(String addressString, String cityString,
			String stateString, String zipString) throws Exception {
		Map m = getBingGeoLocationData(addressString, cityString, stateString, zipString);
		if (m != null && "200".equals((String)m.get("statusCode"))) {
			Float[] position = (Float[]) m.get("position");
			if(position != null) {
				//System.out.println("Geo coding successfully from Bing.");
				return position;
			}
		} 
		return null;
	}
	
//	public Float[] checkGoogle(String addressString, String cityString,
//			String stateString, String zipString) throws Exception {
//		Map m = getGoogleGeoData(addressString, cityString, stateString, zipString);
//		if (m != null && "200".equals((String)m.get("statusCode"))) {
//			Float[] position = (Float[]) m.get("position");
//			if(position != null) {
//				//System.out.println("Geo coding successfully from Google.");
//				return position;
//			}
//		} 
//		//WI-428: Remove references to Yahoo Geo-coding API call
//		/*
//		Float[] position = checkYahoo(addressString, cityString, stateString, zipString);
//		if(position != null) {
//			//System.out.println("Geo coding successfully from Yahoo.");
//			return position;
//		}
//		*/
//		return null;
//	}
	
	public boolean isValidAddress(String addressString, String cityString,
	String stateString, String zipString) {
		Map m = null;
		try {
			m = getGeoData(addressString, cityString, stateString, zipString);
			if(m.get("point") != null) return true;
		} catch(Exception ex) {
			boolean nullMap = m == null ? true : false;
			StringBuilder sb = new StringBuilder();
			sb.append("\n=========================== Error occurred in GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").isValidAddress");
			if(!nullMap){
				sb.append("\n\t source: ");
				sb.append(m.get("source") == null ? this.NULL_TEXT : m.get("source"));
			}
			sb.append("\n\t city: ");sb.append(cityString == null ? this.NULL_TEXT : cityString);
			sb.append("\n\t state: ");sb.append(stateString == null ? this.NULL_TEXT : stateString);
			sb.append("\n\t zip: ");sb.append(zipString == null ? this.NULL_TEXT : zipString);
			sb.append("\n\t address: ");sb.append(addressString == null ? this.NULL_TEXT : addressString);
			if(!nullMap){
				Float[] position = (Float[])m.get("point");
				if(position != null){
					sb.append("\n\tposition[0] latitude:");sb.append(position[0].floatValue());
					sb.append("\n\tposition[1] longitud:");sb.append(position[1].floatValue());
				}else{
					sb.append("\n\tposition was null");
				}
			}
			sb.append(getStackTraceAsString(ex));
			sb.append("===========================\n");
			System.err.println(sb.toString());
			
			
		}
		
		return false;
	}
	
	private boolean isValidUSAGeoData(Float[] position) {
		float lat = position[1].floatValue();
		float lng = position[0].floatValue();
		if(((lat >= 24.52 && lat <=49.38) && (lng >= -124.77 && lng <= -66.95)) //usa continent: 48 states
		|| ((lat >= 16.91 && lat <= 23.00) && (lng >= -162.00 && lng <= -154.67)) // Hawaii
		|| ((lat >= 54.67 && lat <= 71.83) && (lng >= -172.44 && lng <= -129.96)) //Alaska
			
		) return true;
		
		return false;
	}
	
	//WI-428: Remove references to Yahoo Geo-coding API call
	/*
	public Float[] checkYahoo(String addressString, String cityString,
			String stateString, String zipString) {
		
		//String requestUrl = "http://local.yahooapis.com/MapsService/V1/geocode?appid=YD-9G7bey8_JXxQP6rxl.fBFGgCdNjoDMACQA--&location=";
		//--- Geo Code YAHOO API V2 --//
		String requestUrl = "http://where.yahooapis.com/geocode?appid=YD-9G7bey8_JXxQP6rxl.fBFGgCdNjoDMACQA--&location=";
		Float[] position = new Float[2];
		String address = "";
		String responseStr = "";
		BufferedReader in = null;
		try {

			// Build the address to send to google
			if (addressString != null)
				address += addressString + " ";
			if (cityString != null)
				address += cityString + " ";
			if (stateString != null)
				address += stateString + " ";
			if (zipString != null)
				address += zipString;

			// Format the url correctly, and make the call to google.
			address = java.net.URLEncoder.encode(address.trim(), "UTF-8");
			
			URL yahooUrl = new java.net.URL(requestUrl + address);
			URLConnection yahooConnection = yahooUrl.openConnection();
			in = new BufferedReader(new InputStreamReader(yahooConnection.getInputStream()));
			
			String line = null;
			while((line=in.readLine()) != null) {
				responseStr += line;
			}
			position[1] = extractFloatFromXmlString(responseStr, "latitude");
			position[0] = extractFloatFromXmlString(responseStr, "longitude");
			String country = extractStringFromXmlString(responseStr, "countrycode");
			if("US".equals(country) && position[1]!=null && position[0]!=null) return position;			
		} catch (Exception ex) {
//			System.err.println("YAHOO GEOCODE EXCEPTION: " + ex.getMessage());
//			System.err.println("YAHOO GEOCODE EXCEPTION: Address = " + address);
//			System.err.println("YAHOO GEOCODE EXCEPTION: URL = " + requestUrl);
			
			StringBuilder sb = new StringBuilder();
			sb.append("\n=========================== GeocodeOperator(v2).checkYahoo:");
			sb.append("\n\trequestUrl:"); sb.append(requestUrl  == null ? NULL_TEXT : requestUrl);
			sb.append("\n\taddress:"); sb.append(address == null ? NULL_TEXT : address);
			sb.append("\n\taddressString:"); sb.append(addressString == null ? NULL_TEXT : addressString);
			sb.append("\n\tcityString:"); sb.append(cityString == null ? NULL_TEXT : cityString);
			sb.append("\n\tstateString:"); sb.append(stateString == null ? NULL_TEXT : stateString);
			sb.append("\n\taddress:"); sb.append(address == null ? NULL_TEXT : address);
			sb.append("\n\tzipString:"); sb.append(zipString == null ? NULL_TEXT : zipString);
			
			sb.append("\n\tresponseStr:"); sb.append(responseStr == null ? NULL_TEXT : responseStr);
			
			sb.append("\n\tposition[1] latitude:"); sb.append(position[1] == null ? NULL_TEXT : position[1]);
			sb.append("\n\tposition[0] longitude:"); sb.append(position[0] == null ? NULL_TEXT : position[0]);
			
		    sb.append(getStackTraceAsString(ex));
			sb.append("===========================\n");
			System.err.println(sb.toString());
			
		}finally{
			try{
			if(in != null)
				in.close();
			}catch (Exception ex) {
				StringBuilder sb = new StringBuilder();
				sb.append("\n=========================== GeocodeOperator(v2).checkYahoo:");
				sb.append("\n\t error closing input stream");
				sb.append(getStackTraceAsString(ex));
				sb.append("===========================\n");
				System.err.println(sb.toString());				
			}
		}
		return null;
	}


	public Float[] checkEsri(long id, String app, String address, String city,
			String state, String zip, String remoteIP,
			boolean triedAddressCorrection) throws GeocodingException {
		Float[] position = new Float[2];
		try {
			Address addrParam = new Address();
			if (address != null && !address.equals(""))
				addrParam.setStreet(address);
			if (city != null && !city.equals(""))
				addrParam.setCity(city);
			if (state != null && !state.equals(""))
				addrParam.setStateProvince(state);
			if (zip != null && !zip.equals(""))
				addrParam.setPostalCode(zip);
			boolean partial = true;
			if (addrParam.getStateProvince() != null
					&& addrParam.getCity() != null
					&& addrParam.getStateProvince() != null
					&& addrParam.getPostalCode() != null)
				partial = false;
			AddressFinderLocator addressLocator = new AddressFinderLocator();
			IAddressFinder myAddressFinder = addressLocator
					.getAddressFinderHttpPort();
			AddressFinderOptions addrOpts = new AddressFinderOptions();
			addrOpts.setDataSource(getConfig().getLocator().getEsriGeoSource());
			addrOpts.setPartialAddress(partial);
			GeocodeInfo myLocationInfo = myAddressFinder.findLocationByAddress(
					addrParam, addrOpts, null);
			// System.err.println("ESRI: Error code: " +
			// myLocationInfo.getErrorMessage());
			String esriError = myLocationInfo.getErrorMessage();
			if (esriError != null)
				throw new GeocodingException(esriError, esriError.substring(0,
						4));
			GeocodeCandidate[] locations = myLocationInfo.getCandidates();
			// System.err.println("ESRI: GeocodeCandidate[] locations=" +
			// locations);
			if (locations != null)
				// System.err.println("ESRI: locations[0]=" + locations[0]);
				if (locations != null && locations[0] != null) {
					GeocodeCandidate location = locations[0];
					// System.err.println("ESRI: Location from ESRI: " +
					// location);
					Point point = location.getPoint();
					position[0] = new Float(point.getX());
					position[1] = new Float(point.getY());
				} else {
					// System.err.println("ESRI: Null position returned by ESRI");
					return null;
				}
		} catch (Exception ex) {
			if (ex instanceof GeocodingException) {
				String[] fields = ErrorUtil
						.getFieldsForError(((GeocodingException) ex)
								.getFaultCode());
				if (fields.length > 0) {
					for (int i = 0; i < fields.length; i++)
						if (!triedAddressCorrection
								&& "address".equals(fields[i])) {
							// System.err.println("ESRI: Geocoding error due to address; trying ZIP code: "
							// + zip);
							return geocode(id, app, null, null, null, zip,
									remoteIP, false, true);
						}
				}
				throw ((GeocodingException) ex);
			}
			System.err.println("Store Locator ESRI Geocoding Exception: "
					+ ex.getMessage());
			System.err.println("caused by " + ex.getClass().getName());
			ex.printStackTrace(System.err);
		}
		return position;
	}
    */
	public Float[] checkFirstLogic(String addressString, String cityString,
			String stateString, String zipString) throws Exception {
		/*
		 * DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		 * DocumentBuilder builder = dbf.newDocumentBuilder(); Document doc =
		 * builder.newDocument(); org.w3c.dom.Element dataSet =
		 * doc.createElement("DataSet"); org.w3c.dom.Element record =
		 * doc.createElement("Record"); org.w3c.dom.Element address1 =
		 * doc.createElement("address1");
		 * address1.appendChild(doc.createTextNode(addressString));
		 * record.appendChild(address1); org.w3c.dom.Element city =
		 * doc.createElement("city");
		 * city.appendChild(doc.createTextNode(cityString));
		 * record.appendChild(city); org.w3c.dom.Element state =
		 * doc.createElement("state");
		 * state.appendChild(doc.createTextNode(stateString));
		 * record.appendChild(state); org.w3c.dom.Element zip =
		 * doc.createElement("zip");
		 * zip.appendChild(doc.createTextNode(zipString));
		 * record.appendChild(zip); dataSet.appendChild(record); String
		 * xmlString = domWriter(dataSet, new StringBuffer());
		 * //System.err.println
		 * ("\t\t\t\t ------------------------ FIRSTLOGIC: Request to FL: " +
		 * xmlString); String host =
		 * getConfig().getLocator().getFirstlogicHostname(); IQServiceLocator
		 * locator = new IQServiceLocator();
		 * locator.setFirstlogicIQServiceSoapEndpointAddress("http://" + host +
		 * "/FirstlogicIQService/FirstlogicIQService.asmx"); IQServiceSoap soap
		 * = locator.getFirstlogicIQServiceSoap(); String responseStr =
		 * soap.runTransactionDataflowWithXmlData("localhost", 20003,
		 * "D:\\IQ8\\repository\\configuration_rules",
		 * "projects\\DBMarketing\\Store Locator\\BFRC_xml_store_locator.xml",
		 * "bfrc_substitutions.xml", true, xmlString); Float out[] = new
		 * Float[2];
		 * //System.err.println("FIRSTLOGIC: Response from First Logic:\n" +
		 * responseStr); out[1] = extractFloatFromXmlString(responseStr,
		 * "latitude"); out[0] = extractFloatFromXmlString(responseStr,
		 * "longitude"); String flError =
		 * extractStringFromXmlString(responseStr, "addrfaultcode"); if(flError
		 * != null && flError.length() == 4 && flError.charAt(0) == 'E') throw
		 * new GeocodingException("Firstlogic", flError); if(out[0] == null ||
		 * out[1] == null) throw new FirstlogicDownException(); return out;
		 */

		
		/*String xmlInput = "<WEB_Trxn_Store_Locator_IN><Address>"
				+ addressString + "</Address><City>" + cityString
				+ "</City><State>" + stateString + "</State><Zip>" + zipString
				+ "</Zip></WEB_Trxn_Store_Locator_IN>";
		String responseStr = "";
		String serviceName = "";
		if(ServerUtil.isProduction()){
			serviceName = "WEB_Trxn_Store_Locator_PRD";
			com.businessobjects.www.DataServices_Server wsServer = new com.businessobjects.www.DataServices_ServerLocator();
			com.businessobjects.www.Realtime_Service_Admin admin = wsServer
					.getRealtime_Service_Admin();
	
			com.businessobjects.www.DataServices.ServerX_xsd.RunRealtimeServiceRequest runRealtimeServiceRequest = new com.businessobjects.www.DataServices.ServerX_xsd.RunRealtimeServiceRequest();
			runRealtimeServiceRequest.setServiceName(serviceName);
			runRealtimeServiceRequest.setXmlInput(xmlInput);
			com.businessobjects.www.DataServices.ServerX_xsd.RunRealtimeServiceResponse res = admin
					.run_Realtime_Service(runRealtimeServiceRequest);
	
			responseStr = res.getXmlOutput();
		}else{
			serviceName = "WEB_Trxn_Store_Locator_DEV";
			com.businessobjects_dev.www.DataServices_Server wsServer = new com.businessobjects_dev.www.DataServices_ServerLocator();
			com.businessobjects_dev.www.Realtime_Service_Admin admin = wsServer
					.getRealtime_Service_Admin();
	
			com.businessobjects_dev.www.DataServices.ServerX_xsd.RunRealtimeServiceRequest runRealtimeServiceRequest = new com.businessobjects_dev.www.DataServices.ServerX_xsd.RunRealtimeServiceRequest();
			runRealtimeServiceRequest.setServiceName(serviceName);
			runRealtimeServiceRequest.setXmlInput(xmlInput);
			com.businessobjects_dev.www.DataServices.ServerX_xsd.RunRealtimeServiceResponse res = admin
					.run_Realtime_Service(runRealtimeServiceRequest);
	
			responseStr = res.getXmlOutput();
		}

		Float out[] = new Float[2];
		// System.err.println("FIRSTLOGIC: Response from First Logic:\n" +
		// responseStr);
		out[1] = extractFloatFromXmlString(responseStr, "Latitude");
		out[0] = extractFloatFromXmlString(responseStr, "Longitude");
		// String flError = extractStringFromXmlString(responseStr,
		// "AddrFaultStat");
		// if(flError != null && flError.length() == 4 && flError.charAt(0) ==
		// 'E')
		// throw new GeocodingException("Firstlogic", flError);
		if (out[0] == null || out[1] == null)
			throw new FirstlogicDownException();*/
		Float out[] = new Float[2];
		return out;
	}

	private Float extractFloatFromXmlString(String xml, String element) {
		if (element == null)
			throw new IllegalArgumentException("no element specified");
		if (xml == null)
			throw new IllegalArgumentException("server returned null XML");
		int pos = xml.indexOf(element);
		if (pos == -1)
			return null;
		pos += element.length();
		int len = xml.length();
		if (pos >= len)
			return null;
		String out = "";
		char c;
		while (++pos < len && (c = xml.charAt(pos)) != '<')
			out += c;
		if ("".equals(out))
			return null;
		// System.err.println("xml="+xml+", "+element+"="+out);
		Float value = null;
		try {
			value = Float.valueOf(out);
		} catch (RuntimeException ex) {
			StringBuilder sb = new StringBuilder();
			sb.append("\n===========================");
			sb.append("\nError occurred in GeocodeOperator(v"+bingmapsGeoLocationApiVersion+").extractFloatFromXmlString ");
			sb.append("\n\t inputString: ");sb.append(xml == null ? this.NULL_TEXT : xml);
			sb.append("\n\t element: ");sb.append(element == null ? this.NULL_TEXT : element);			
			sb.append(getStackTraceAsString(ex));
			sb.append("===========================\n");
			System.err.println(sb.toString());
//			System.err.println("Input string=" + xml);
			throw ex;
		}
		return value;
	}

	private String extractStringFromXmlString(String xml, String element) {
		// System.err.println("Extracting string: xml="+xml+", "+element);
		if (element == null)
			throw new IllegalArgumentException("no element specified");
		if (xml == null)
			throw new IllegalArgumentException("server returned null XML");
		int pos = xml.indexOf(element);
		if (pos == -1)
			return null;
		pos += element.length();
		int len = xml.length();
		if (pos >= len)
			return null;
		String out = "";
		char c;
		while (++pos < len && (c = xml.charAt(pos)) != '<')
			out += c;
		if ("".equals(out))
			return null;
		// System.err.println("="+out);
		return out;
	}

	public static java.lang.String domWriter(org.w3c.dom.Node node,
			java.lang.StringBuffer buffer) {
		if (node == null) {
			return "";
		}
		int type = node.getNodeType();
		switch (type) {
		case org.w3c.dom.Node.ELEMENT_NODE: {
			// System.out.println("elementName=" + node.getNodeName());
			buffer.append("<" + node.getNodeName());
			buffer.append(">");
			org.w3c.dom.NodeList children = node.getChildNodes();
			// System.out.println("childNodeCount=" + children.getLength());
			if (children != null) {
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					domWriter(children.item(i), buffer);
					// System.out.println("currentChild=" + children.item(i));
				}
			}
			buffer.append("</" + node.getNodeName() + ">");
			break;
		}
		case org.w3c.dom.Node.TEXT_NODE: {
			// System.out.println("textNode=" + node.getNodeValue());
			buffer.append(node.getNodeValue());
			break;
		}
		}
		return buffer.toString();
	}

	public Heartbeat getHeartbeat() {
		return this.heartbeat;
	}

	public void setHeartbeat(Heartbeat heartbeat) {
		this.heartbeat = heartbeat;
	}
	
	public String getStackTraceAsString(Exception e) {
	    StringWriter stackTrace = new StringWriter();
	    e.printStackTrace(new PrintWriter(stackTrace));
	    return stackTrace.toString();
	}

	private String getStackTraceAsString(Throwable throwable) {
	    StringWriter stackTrace = new StringWriter();
	    throwable.printStackTrace(new PrintWriter(stackTrace));
	    return stackTrace.toString();
	}
}