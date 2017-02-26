package com.bfrc.framework.util;

import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;


public class GasPriceLookupUtils {
   private static final String TOKEN="auBZ2RoP9XcFUIgEdrgbYQ==";
   private static final String OPIS_URL="https://services.opisnet.com/RealtimePriceService/RealtimePriceService.asmx/";
   private static String userTicket;
   public static final String SORT_UNLEADED="Unleaded";
   public static final String SORT_MIDGRADE="Midgrade";
   public static final String SORT_PREMIUM="Premium";
   public static final String SORT_DIESEL="Diesel";
   public static String getUserTicket(){
	   //if(userTicket == null){
		   try{
		   String xml = ServerUtil.grabPage(OPIS_URL+"Authenticate?CustomerToken="+TOKEN);
		   if(StringUtils.isNullOrEmpty(xml))
			   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   userTicket =  ((org.json.JSONObject)jo.get("string")).getString("content");
			   return userTicket;
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   userTicket =  "N6v30PETGnb9uMlfoNtRdkH30WlHxQCUheOh8n92Q/56N13Lkqo4t8Dp2cWcNaKB";
		   
	   //}
	   return userTicket;
   }

	/**
	 * Search a zip code for retail station prices using default settings.
	 */
	public static JSONArray searchZipCodeSimple(java.lang.String zipCode){
		   getUserTicket();
		   try{
			   String url=OPIS_URL+"GetZipCodeResults?UserTicket="+java.net.URLEncoder.encode(userTicket,"utf-8")+"&ZipCode="+java.net.URLEncoder.encode(zipCode,"utf-8");
			   String xml = ServerUtil.grabPage(url);
			   if(StringUtils.isNullOrEmpty(xml))
				   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   try{
			       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
			   }catch(JSONException ex){
				   //no elements were found in the above gets, means no data was found in web service call.
				   //do nothing here, to avoid logging messages.
			   }catch(Exception ex){
				   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
				   if(jobj != null){
				      JSONArray arr = new JSONArray();
				      arr.put(jobj);
				      return arr;
				   }
			   }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return null;
	}
	
	/**
	 * Search a zip code for retail station prices using default settings.
	 */
	public static JSONArray searchZipCode(java.lang.String zipCode, String sortByProduct){
		   getUserTicket();
		   try{
			   String url=OPIS_URL+"GetZipCodeSortedResults?UserTicket="+java.net.URLEncoder.encode(userTicket,"utf-8")+"&ZipCode="+java.net.URLEncoder.encode(zipCode,"utf-8")+"&SortByProduct="+java.net.URLEncoder.encode(sortByProduct,"utf-8");;
			   String xml = ServerUtil.grabPage(url);
			   if(StringUtils.isNullOrEmpty(xml))
				   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   try{
			       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
			   }catch(JSONException ex){
				   //no elements were found in the above gets, means no data was found in web service call.
				   //do nothing here, to avoid logging messages.
			   }catch(Exception ex){
				   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
				   if(jobj != null){
				      JSONArray arr = new JSONArray();
				      arr.put(jobj);
				      return arr;
				   }
			   }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return null;
	}
	
	/**
	 * Search a City, State for retail station prices using default
	 * settings.
	 */
	public static JSONArray searchCityStateSimple(java.lang.String city, java.lang.String state,String sortByProduct){
		   getUserTicket();
		   try{
	                if(sortByProduct == null)
	                    sortByProduct="Unleaded";
			   String url=OPIS_URL+"GetCityStateSortedResults?UserTicket="+java.net.URLEncoder.encode(userTicket,"utf-8")+"&City="+java.net.URLEncoder.encode(city,"utf-8")+"&State="+java.net.URLEncoder.encode(state,"utf-8")+"&SortByProduct="+java.net.URLEncoder.encode(sortByProduct,"utf-8");
	                //System.out.println(url);
			   String xml = ServerUtil.grabPage(url);
			   if(StringUtils.isNullOrEmpty(xml))
				   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   try{
			       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
			   }catch(JSONException ex){
				   //no elements were found in the above gets, means no data was found in web service call.
				   //do nothing here, to avoid logging messages.
			   }catch(Exception ex){
				   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
				   if(jobj != null){
				      JSONArray arr = new JSONArray();
				      arr.put(jobj);
				      return arr;
				   }
			   }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return null;
	}
	
	/**
     * Get the search results for a latitude and longitude using default
     * settings.
     */
	public static JSONArray searchLatLongSimple(java.lang.String latitude, java.lang.String longitude){
		   
		   return searchLatLong(latitude,longitude,null);
	}
	
	/**
	 * Search latitude and longitude for retail station prices using default
	 * settings.
	 */
	public static JSONArray searchLatLong(java.lang.String latitude, java.lang.String longitude,String sortByProduct){
		   getUserTicket();
		   try{
	                if(sortByProduct == null)
	                    sortByProduct="Unleaded";
			   String url=OPIS_URL+"GetLatLongSortedResults?UserTicket="+java.net.URLEncoder.encode(userTicket,"utf-8")+"&Latitude="+java.net.URLEncoder.encode(latitude,"utf-8")+"&Longitude="+java.net.URLEncoder.encode(longitude,"utf-8")+"&SortByProduct="+java.net.URLEncoder.encode(sortByProduct,"utf-8");
	                //System.out.println(url);
			   String xml = ServerUtil.grabPage(url);
			   if(StringUtils.isNullOrEmpty(xml))
				   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   try{
			       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
			   }catch(JSONException ex){
				   //no elements were found in the above gets, means no data was found in web service call.
				   //do nothing here, to avoid logging messages.			   				   
			   }catch(Exception ex){
				   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPrices");
				   if(jobj != null){
				      JSONArray arr = new JSONArray();
				      arr.put(jobj);
				      return arr;
				   }
			   }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return null;
	}
	/**
	 * Get prices by station for a latitude and longitude. 
	 * Allows the user to specify a search radius limit in miles, relative to the geographic position 
	 * specified by latitude and longitude. 
	 * Also allows the user to pass in the coordinates of the user's current position to 
	 * calculate station distance from the user's position. 
	 * You can also toggle whether you want to return stations closest to the user's position or 
	 * cheapest within the overall search radius.
	 */
	public static JSONArray searchLatLong(java.lang.String latitude, java.lang.String longitude,
            String sortByProduct, String distance){
		return searchLatLong(latitude,longitude,sortByProduct,distance,null,null,null);
		
	}
	public static JSONArray searchLatLong(java.lang.String latitude, java.lang.String longitude,
			                              String sortByProduct, String distance, String isFilteredByDistance, 
			                              String UserLatitude, String UserLongitude){
		   getUserTicket();
		   if(StringUtils.isNullOrEmpty(sortByProduct))
               sortByProduct="Unleaded";
		   if(StringUtils.isNullOrEmpty(distance))
			   distance="10";
		   if(StringUtils.isNullOrEmpty(isFilteredByDistance))
			   isFilteredByDistance="false";
		   if(StringUtils.isNullOrEmpty(UserLatitude))
			   UserLatitude=latitude;
		   if(StringUtils.isNullOrEmpty(UserLongitude))
			   UserLongitude=longitude;
		   try{
	                if(sortByProduct == null)
	                    sortByProduct="Unleaded";
			   String url=OPIS_URL+"GetLatLongSortedWithDistanceResults?UserTicket="+java.net.URLEncoder.encode(userTicket,"utf-8")
			              +"&Latitude="+java.net.URLEncoder.encode(latitude,"utf-8")
			              +"&Longitude="+java.net.URLEncoder.encode(longitude,"utf-8")
			              +"&distance="+java.net.URLEncoder.encode(distance,"utf-8")
			              +"&isFilteredByDistance="+java.net.URLEncoder.encode(isFilteredByDistance,"utf-8")
			              +"&UserLatitude="+java.net.URLEncoder.encode(UserLatitude,"utf-8")
			              +"&UserLongitude="+java.net.URLEncoder.encode(UserLongitude,"utf-8")
			              +"&SortByProduct="+java.net.URLEncoder.encode(sortByProduct,"utf-8");
	                //System.out.println(url);
			   String xml = ServerUtil.grabPage(url);
			   if(StringUtils.isNullOrEmpty(xml))
				   return null;
			   JSONObject jo = org.json.XML.toJSONObject(xml);
			   try{
			       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesByLatLongDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPricesByLatLong");
			   }catch(JSONException ex){
				   //no elements were found in the above gets, means no data was found in web service call.
				   //do nothing here, to avoid logging messages.
			   }catch(Exception ex){
				   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("StationPricesByLatLongDataTable")).get("diffgr:diffgram")).get("DocumentElement")).get("StationPricesByLatLong");
				   if(jobj != null){
				      JSONArray arr = new JSONArray();
				      arr.put(jobj);
				      return arr;
				   }
			   }
		   }catch(Exception ex){
			   ex.printStackTrace();
		   }
		   return null;
	}


	public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
		  double theta = lon1 - lon2;
	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
	  dist = Math.acos(dist);
	  dist = rad2deg(dist);
	  dist = dist * 60 * 1.1515;
	  if (unit == "K") {
	    dist = dist * 1.609344;
	  } else if (unit == "N") {
	  	dist = dist * 0.8684;
	    }
	  return (dist);
	}

		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts decimal degrees to radians             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	static private double deg2rad(double deg) {
	  return (deg * Math.PI / 180.0);
	}

		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
		/*::  This function converts radians to decimal degrees             :*/
		/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	static private double rad2deg(double rad) {
	  return (rad * 180.0 / Math.PI);
	}

}
