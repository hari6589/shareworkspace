package com.bfrc.framework.util;

import com.bfrc.framework.util.*;
public class Util {
   public static void debug(Object msg){
	   if (!ServerUtil.isBSRODev()
	           && !ServerUtil.isBSROQa()
	           && !ServerUtil.isProduction()           ) {
		   if(msg instanceof Throwable){
			   System.out.println("[DEBUG]\t\t");
			   ((Throwable)msg).printStackTrace(System.out);
		   }else
		       System.out.println("[DEBUG]\t\t"+msg);
	   }
   }
   public static boolean isStanddardLoadRange(String loadRange){
	   return (ServerUtil.isNullOrEmpty(loadRange) || "SL".equalsIgnoreCase(loadRange));
   }
   
   public static String getLoadRange(String loadRange){
	   if (ServerUtil.isNullOrEmpty(loadRange))
		   return "SL";
	   return loadRange;

   }
   
   public static String getVehicleLoadRange(String loadRange){
	   if(ServerUtil.isNullOrEmpty(loadRange)){
		   return "SL";
	   }
	   return loadRange;
   }
 //--**TIRE SELECTOR RULES(2)**--//
   public static String getVehicleMetric(String tireSize){
	   if(tireSize != null){
		   String ucTireSize = tireSize.toUpperCase();
		   if(ucTireSize.startsWith("P")){
			   return "P-Metric";	
		   }else if(ucTireSize.startsWith("LT")){
			   return "LT-Metric";	
		   }else if(ucTireSize.indexOf(".") > 0){
			   return "Flotation";	
		   }else{
			   return "Euro-Metric";	
		   }
	   }
	   return "P-Metric";
   }
   
   public static boolean isPMetric(String tireSize){
	   return "P-Metric".equalsIgnoreCase(getVehicleMetric(tireSize));
   }
   public static boolean isLTMetric(String tireSize){
	   return "LT-Metric".equalsIgnoreCase(getVehicleMetric(tireSize));
   }
   public static boolean isFlotationMetric(String tireSize){
	   return "Flotation".equalsIgnoreCase(getVehicleMetric(tireSize));
   }
   public static boolean isEuroMetric(String tireSize){
	   return "Euro-Metric".equalsIgnoreCase(getVehicleMetric(tireSize));
   }
   public static boolean isValidZipCode(String str) {
	   if(str == null) return false;
   	   String zipCodePattern = "\\d{5}(-\\d{4})?";
       return str.matches(zipCodePattern);
   }
   public static boolean isVehicleEmpty(Object year, String  make, String model, String submodel)
   {
	   if(StringUtils.isNullOrEmpty(year) || StringUtils.isNullOrEmpty(make) || StringUtils.isNullOrEmpty(model) || StringUtils.isNullOrEmpty(submodel))
		   return true;
		try{
			Integer.parseInt(year.toString());
		}catch(Exception ex){
		    return true;
		}
		return false;
	}
   
   public static boolean isValidVehicleIds(Object year, Object  makeId, Object modelId)
   {
	   if(StringUtils.isNullOrEmpty(year) || StringUtils.isNullOrEmpty(makeId) || StringUtils.isNullOrEmpty(modelId))
		   return false;
		try{
			Integer.parseInt(year.toString());
		}catch(Exception ex){
		    return false;
		}
		return true;
	}
}
