package com.bfrc.framework.util;

import java.util.*;


public class EpaMpgLookupUtils {
	public static String translateTransmission(String transmission){
        if(StringUtils.isNullOrEmpty(transmission))
            return "Other";
        String key = transmission;
        if(Character.isDigit(transmission.charAt(transmission.length()-1))){
            String num_speed = transmission.substring(transmission.length()-1);
            key = transmission.substring(0,transmission.length()-1);
            return getMappedTransmission().get(key)+"("+num_speed+"-speed)";
        }
        String s = (String)getMappedTransmission().get(key);
        if(s == null)
            return "Other";
        return s;

    }
    private static Map<String, String> mappedTransmissions;
    public static Map<String, String> getMappedTransmission(){
        if(mappedTransmissions == null){
            mappedTransmissions = new HashMap<String, String>();
            mappedTransmissions.put("A", "Automatic");
            mappedTransmissions.put("L", "Automatic");
            mappedTransmissions.put("M", "Manual");
            mappedTransmissions.put("S", "Semi-Automatic");
            mappedTransmissions.put("SA", "Semi-Automatic");
            mappedTransmissions.put("AM", "Automated Manual");
            mappedTransmissions.put("OT", "Other");
            mappedTransmissions.put("AV-S", "Other");
            mappedTransmissions.put("AV", "Continuously Variable");
            mappedTransmissions.put("CVT", "Continuously Variable");
        }
        return mappedTransmissions;
    }
    public static String translateDrive(String drive){
       String driveDesc = (String)getMappedDrives().get(drive);
       if(driveDesc == null)
           return "Other";
       return driveDesc;
    }
    private static Map<String, String> mappedDrives;
    public static Map<String, String> getMappedDrives(){
        if(mappedDrives == null){
            mappedDrives = new HashMap<String, String>();
            mappedDrives.put("R", "2-Wheel Drive (Rear)");
            mappedDrives.put("F", "2-Wheel Drive (Front)");
            mappedDrives.put("A", "All Wheel Drive");
            mappedDrives.put("4", "4-Wheel Drive");
        }
        return mappedDrives;
    }

    public static String translateFuel(String fuel){
       String fuelDesc = (String)getMappedFuels().get(fuel);
       if(fuelDesc == null)
           return "Other";
       return fuelDesc;
    }
    private static Map<String, String> mappedFuels;
    public static Map<String, String> getMappedFuels(){
        if(mappedFuels == null){
            mappedFuels = new HashMap<String, String>();
            mappedFuels.put("G", "Regular Unleaded");
            mappedFuels.put("R", "Regular Unleaded");
            mappedFuels.put("GP", "Premium Unleaded");
            mappedFuels.put("P", "Premium Unleaded");
            mappedFuels.put("GPR", "Premium Unleaded Required");
            mappedFuels.put("DU", "Diesel, ultra low sulfur");
            mappedFuels.put("D", "Diesel Fuel");
            mappedFuels.put("C", "Compressed Natural Gas");
            mappedFuels.put("E", "Ethanol");
            mappedFuels.put("EL", "Electricity");
        }
        return mappedFuels;
    }
    private static Map<String, String> mappedNormMakeNames;
    public static String getNormMakeName(String make){
        if(mappedNormMakeNames == null){
            mappedNormMakeNames = new HashMap<String, String>();
            mappedNormMakeNames.put("CHRYSLER GROUP LLC","CHRYSLER");

            mappedNormMakeNames.put("GM","GMC");
            mappedNormMakeNames.put("GENERAL MOTORS","GMC");
            mappedNormMakeNames.put("LINCOLN-MERCURY","LINCOLN");
            mappedNormMakeNames.put("MAZDA MOTOR CORP","MAZDA");
            mappedNormMakeNames.put("PANTHER CAR COMPANY LIMITED","PANTHER");
            mappedNormMakeNames.put("FORD MOTOR COMPANY","FORD");

            mappedNormMakeNames.put("HYUNDAI MOTOR COMPANY","HYUNDAI");
            mappedNormMakeNames.put("ISUZU MOTORS LIMITED","ISUZU");
            mappedNormMakeNames.put("JAGUAR CARS","JAGUAR");
            mappedNormMakeNames.put("JAGUAR CARS INC.","JAGUAR");
            mappedNormMakeNames.put("JEEP CORPORATION","JAGUAR");
            mappedNormMakeNames.put("KIA MOTORS CORPORATION","KIA");
            mappedNormMakeNames.put("MAZDA MOTOR","MAZDA");
            mappedNormMakeNames.put("MITSUBISHI MOTORS AUST LTD","MITSUBISHI");
            mappedNormMakeNames.put("MITSUBISHI MOTORS AUST. LTD.","MITSUBISHI");
            mappedNormMakeNames.put("MITSUBISHI MOTORS CO","MITSUBISHI");
            mappedNormMakeNames.put("MITSUBISHI MOTORS NA","MITSUBISHI");
            mappedNormMakeNames.put("NISSAN MOTOR CO., LTD.","NISSAN");
            mappedNormMakeNames.put("NISSAN MOTOR COMPANY, LTD.","NISSAN");
            mappedNormMakeNames.put("ROLLS-ROYCE MOTOR CARS LTD.","ROLLS-ROYCE");
            mappedNormMakeNames.put("ROLLS-ROYCE MOTORS LTD.","ROLLS-ROYCE");
            mappedNormMakeNames.put("SUZUKI MOTOR CO., LTD","SUZUKI");
            mappedNormMakeNames.put("SUZUKI MOTOR CO., LTD.","SUZUKI");
            mappedNormMakeNames.put("SUZUKI MOTOR CO.,LTD.","SUZUKI");
            mappedNormMakeNames.put("SUZUKI MOTOR CORP.","SUZUKI");
            mappedNormMakeNames.put("SUZUKI MOTOR CORPORATION","SUZUKI");
            mappedNormMakeNames.put("VOLVO CAR CORPORATION","VOLVO");
            mappedNormMakeNames.put("VW","VOLKSWAGEN");
        }
        return mappedNormMakeNames.get(make) == null? make  : (String)mappedNormMakeNames.get(make);
     
    }
    public static String trimAndUpper(String in){
        if(in != null){
            in = in.trim().toUpperCase();
        }
        return in;
    }
}
