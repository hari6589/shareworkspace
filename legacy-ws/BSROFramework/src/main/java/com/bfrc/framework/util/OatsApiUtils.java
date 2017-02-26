package com.bfrc.framework.util;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class OatsApiUtils {
   private static final String TOKEN="token=90a4662b3914e9d55b3e42fb6be83a464b8e24c51cb4d65e5591f373231960c7";
   private static final String OATS_URL="http://api.oats.co.uk/Corvus/";
   private static final String LANGUAGE="language=en-us";
   public static final String SORT_UNLEADED="Unleaded";
   public static final String SORT_MIDGRADE="Midgrade";
   public static final String SORT_PREMIUM="Premium";
   public static final String SORT_DIESEL="Diesel";
   public static final String KENDALL_GUID = "50895293738137856";
   public static final String CLASSIFICATION_CARS = "classification=56071794481752534";
   public static final String REGION_USA = "region=56071794481697455";
   public static final String TYPE_BROWSE_YEARS = "browse/yearsavailable";
   public static final String YEARS_AVAILABLE = "yearsavailable";
   public static final String TYPE_BROWSE_MANUFACTURER = "browse/manufacturer";
   public static final String MANUFACTURER = "manufacturer";
   public static final String TYPE_BROWSE_EQUIPMENT = "browse/equipment";
   public static final String TYPE_EQUIPMENT_BRANDING = "equipmentbranding/";
   public static final String TYPE_LUBE_KENDALL = "type=lube&channelname=Kendall";

  // public static final String CLASSIFICATION = "classification=";
   //public static final String REGION = "region=";
   
   
   
   //use for help ==  http://api.oats.co.uk/Corvus/help.html?token=90a4662b3914e9d55b3e42fb6be83a464b8e24c51cb4d65e5591f373231960c7 
   
   //Cars classification
   //http://api.oats.co.uk/Corvus/browse/classification?token=90a4662b3914e9d55b3e42fb6be83a464b8e24c51cb4d65e5591f373231960c7

 public static Map<String,String> getYearsList(){
	   
	 JSONArray componentsList = getYearsJSONArray();
	 TreeMap<String,String> yearMap = new TreeMap<String,String>(Collections.reverseOrder());
	 if(componentsList!=null)
	 	for (int i = 0; i < componentsList.length(); i++) {
	 		try{
	 			JSONObject component = (JSONObject) componentsList.get(i);
	 			//Util.debug("==+=="+component.toString(2));
	 			//Util.debug("==+=="+component.getString("guid")+"==+==" +component.getString("type"));
	 			JSONArray contents = ((JSONArray)component.get("contents"));
	 			for (int k = 0; k < contents.length(); k++) {
	 				String yearString = null;
	 				String guid = null;
	 				JSONArray contentList = (JSONArray) contents.get(k);
	 				for (int j = 0; j < contentList.length(); j++) {
	 					JSONObject content = (JSONObject) contentList.get(j);
	 					//Util.debug("==NAME:=="+content.get("name")+"====");
	 					if(content!=null && "year".equals(content.get("name")))
	 					{		
	 						yearString = content.getString("content");
	 					}
	 					else if(content!=null && "guid".equals(content.get("name")))
	 					{		
	 						guid = content.getString("content");
	 					}
	 					//Util.debug("====="+yearString+"=====" +guid);
	 				}
	 				if(yearString!=null&&guid!=null)
						yearMap.put(yearString, guid);
	 			}
	 		}catch(Exception e){
	 			e.printStackTrace();
	 			continue;
	 		}
	 	}
	   return yearMap;
   }
 private static JSONArray getYearsJSONArray(){
	   try{
		   String url=OATS_URL+TYPE_BROWSE_YEARS+";"+CLASSIFICATION_CARS+";"+REGION_USA+"?"+TOKEN+"&"+LANGUAGE;
			Util.debug(url);
		   String xml = ServerUtil.grabPage(url);
		   //Util.debug("\nXML RETURNED:\n\n\n\n"+xml+"\n\n\n");
		   if(StringUtils.isNullOrEmpty(xml))
			   return null;
		   JSONObject jo = org.json.XML.toJSONObject(xml);
		   try{
		       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("components")).get("component");
		   }catch(JSONException ex){
			   //no elements were found in the above gets, means no data was found in web service call.
			   //do nothing here, to avoid logging messages.
		   }catch(Exception ex){
			   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("components")).get("component");
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
   public static Map<String,String> getManufacturersList(String yearGUID){
	   
	   JSONArray componentsList = getManufacturersJSONArray(yearGUID);
		 Map<String,String> manufacturerMap = new TreeMap<String,String>();
		 if(componentsList!=null)
		 	for (int i = 0; i < componentsList.length(); i++) {
		 		try{
		 			JSONObject component = (JSONObject) componentsList.get(i);
		 			Util.debug("==+=="+component.toString(2));
		 			//Util.debug("==+=="+component.getString("guid")+"==+==" +component.getString("type"));
		 			JSONArray contents = ((JSONArray)component.get("contents"));
		 			for (int k = 0; k < contents.length(); k++) {
		 				String manufacturerString = null;
		 				String guid = null;
		 				JSONArray contentList = (JSONArray) contents.get(k);
		 				for (int j = 0; j < contentList.length(); j++) {
		 					JSONObject content = (JSONObject) contentList.get(j);
		 					Util.debug("==NAME:=="+content.get("name")+"====");
		 					if(content!=null && "manufacturer".equals(content.get("name")))
		 					{		
		 						manufacturerString = content.getString("content");
		 					}
		 					else if(content!=null && "guid".equals(content.get("name")))
		 					{		
		 						guid = content.getString("content");
		 					}
		 					Util.debug("====="+manufacturerString+"=====" +guid);
		 				}
		 				if(manufacturerString!=null&&guid!=null)
							manufacturerMap.put(manufacturerString.replace(" (US)",""), guid);
		 			}
		 		}catch(Exception e){
		 			e.printStackTrace();
		 			continue;
		 		}
		 	}
		   return manufacturerMap;
   }
   private static JSONArray getManufacturersJSONArray(String yearGUID){
	   try{
		   String url=OATS_URL+TYPE_BROWSE_MANUFACTURER+";"+CLASSIFICATION_CARS+";"+REGION_USA+";"+YEARS_AVAILABLE+"="+yearGUID+"?"+TOKEN+"&"+LANGUAGE;
		   String xml = ServerUtil.grabPage(url);
		   //Util.debug("\nXML RETURNED:\n\n\n\n"+xml+"\n\n\n");
		   if(StringUtils.isNullOrEmpty(xml))
			   return null;
		   JSONObject jo = org.json.XML.toJSONObject(xml);
		   try{
		       return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("components")).get("component");
		   }catch(JSONException ex){
			   //no elements were found in the above gets, means no data was found in web service call.
			   //do nothing here, to avoid logging messages.
		   }catch(Exception ex){
			   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("components")).get("component");
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
 public static Map<String,String> getModelsList(String yearGUID, String manufacturerGUID){
	   
	   JSONArray equipmentList = getModelsJSONArray(yearGUID, manufacturerGUID);
		 TreeMap<String,String> equipmentMap = new TreeMap<String,String>();
		 if(equipmentList!=null)
		for (int i = 0; i < equipmentList.length(); i++) {
			try{
				JSONObject equipment = (JSONObject) equipmentList.get(i);
	 			//Util.debug("==+=="+equipment.toString(2));
				equipmentMap.put(equipment.getString("name"),equipment.getString("guid"));
					//Util.debug("====="+equipment.getString("name")+"=====" +equipment.getString("guid"));
				
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		 
	   return equipmentMap;
   }
   private static JSONArray getModelsJSONArray(String yearGUID, String manufacturerGUID){
	   try{
		   String url=OATS_URL+TYPE_BROWSE_EQUIPMENT+";"+CLASSIFICATION_CARS+";"+MANUFACTURER+"="+manufacturerGUID+";"+YEARS_AVAILABLE+"="+yearGUID+";"+REGION_USA+"?"+TOKEN+"&"+LANGUAGE;
		   String xml = ServerUtil.grabPage(url);
		   if(StringUtils.isNullOrEmpty(xml))
			   return null;
		   JSONObject jo = org.json.XML.toJSONObject(xml);
		   try{
			   return (JSONArray)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("equipment")).get("equipment");
		   }catch(JSONException ex){
			   //no elements were found in the above gets, means no data was found in web service call.
			   //do nothing here, to avoid logging messages.
		   }catch(Exception ex){
			   JSONObject jobj =  (JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("equipment")).get("equipment");
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

 public static Map<String,String> getProductsList(String equipmentGUID){
	   
	   JSONArray equipmentList = getProductsJSONArray(equipmentGUID);
	   TreeMap<String,String> productMap = new TreeMap<String,String>();
		 if(equipmentList!=null){
		for (int i = 0; i < equipmentList.length(); i++) {
			try{
				JSONObject component = (JSONObject) equipmentList.get(i);
	 			//Util.debug("==+=="+component.toString(2));
	 			//Util.debug("==+=="+component.getString("guid")+"==+==" +component.getString("type"));
	 			String name = component.getString("name");
	 			if("Engine".equals(name)){
		 			//Util.debug("==cpacity=="+capacity);
		 			JSONArray productList = new JSONArray();;
	 				try{
	 					productList= (JSONArray)((org.json.JSONObject)component.get("products")).get("product");
	 			   }catch(JSONException ex){
	 				   //ex.printStackTrace();
	 				   //no elements were found in the above gets, means no data was found in web service call.
	 				   //do nothing here, to avoid logging messages.
	 			   }catch(Exception ex){
	 				   ex.printStackTrace();
	 				   JSONObject jobj =  (JSONObject)((JSONObject)component.get("products")).get("product");
	 				   if(jobj != null){
	 					  productList.put(jobj);
	 				   }
	 			   }
	 				for (int j = 0; j < productList.length(); j++) {
		 				String productName = null;
		 				JSONObject product = (JSONObject) productList.get(j);
			 			//Util.debug("==+=="+product.toString(2));
	 					productName = product.getString("prodName");
	 					
	 					//Util.debug("====="+productName+"=====");
		 				if(productName!=null)
		 					productMap.put(productName, productName);
	 				}	
	 			}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}
		 }
		 
	   return productMap;
   }
 private static JSONArray getProductsJSONArray(String equipmentGUID){
	   try{
		   String url=OATS_URL+TYPE_EQUIPMENT_BRANDING+equipmentGUID+"?"+TYPE_LUBE_KENDALL+"&"+TOKEN+"&"+LANGUAGE;
		   String xml = ServerUtil.grabPage(url);
		   //Util.debug("\nXML RETURNED:\n\n\n\n"+xml+"\n\n\n");
		   if(StringUtils.isNullOrEmpty(xml))
			   return null;
		   JSONObject jo = org.json.XML.toJSONObject(xml);
		   try{
		       return (JSONArray)((JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("equipmentBranding")).get("applications")).get("application");
		   }catch(JSONException ex){
			   //ex.printStackTrace();
		   }catch(Exception ex){
			   ex.printStackTrace();
			   JSONObject jobj =  (JSONObject)((JSONObject)((org.json.JSONObject)((org.json.JSONObject)jo.get("corvusResponse")).get("equipmentBranding")).get("applications")).get("application");
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

}
