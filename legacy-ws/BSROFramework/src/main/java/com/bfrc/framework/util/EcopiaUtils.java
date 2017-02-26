package com.bfrc.framework.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.bfrc.pojo.tire.Ecopia;

public class EcopiaUtils {

	public static Ecopia calculateEcopiaSavings(String inCountry, String inMileage, String inPrice, String inVehicleType){
		// formulas
		// US/EP422
		// ((65000/mileage)*price)-(65000/(mileage+(mileage*0.05))*price)
		// US/Dueller HL422 
		// ((65000/mileage)*price)-(65000/(mileage+(mileage*0.06))*price)
		// Canada/EP422
		// ((105000/100)*mileage*price)-((105000/(100*1.05))*mileage*price)
		// Canada/Dueller HL422
		// ((105000/100)*mileage*price)-((105000/(100*1.06))*mileage*price)
	
	//Util.debug("inCountry = "+inCountry);
	//Util.debug("inMileage = "+inMileage);
	//Util.debug("inPrice = "+inPrice);
	//Util.debug("inVehicleType = "+inVehicleType);
		
		Ecopia bean = new Ecopia();
		// must be US or Canada
		if ("CA".equalsIgnoreCase(inCountry) || "US".equalsIgnoreCase(inCountry)){
			bean.setCountry(inCountry.toUpperCase());
		} else 
			return null;
		// mileage must be numeric 1 or 2 digit
		if (inMileage.matches("^([0-9]{1,2})$")){
			bean.setMileage(inMileage);
		} else 
			return null;
		// price should be n.nn, n.n or n
		if (inPrice.matches("(^[0-9]\\.[0-9][0-9]$)+|(^[0-9]\\.[0-9]$)+|(^[0-9]$)+")){
			bean.setPrice(inPrice);
		} else 
			return null;
		if ("pickup".equalsIgnoreCase(inVehicleType)
			|| "suv".equalsIgnoreCase(inVehicleType)
			|| "passenger-car".equalsIgnoreCase(inVehicleType)
			|| "minivan".equalsIgnoreCase(inVehicleType) ){
			bean.setVehicleType(inVehicleType);
		} else 
			return null;
	
		// passenger car and minivan use EP422; pickup and SUV use Dueller HL422
		// default to US passenger car/minivan settings
		boolean US = true, Canada = false, ecopia422 = true, dueller422 = false;
		BigDecimal factor1 = new BigDecimal("65000");
		BigDecimal factor2 = new BigDecimal("0.05");
		BigDecimal factor3 = new BigDecimal("100");
	
		if ("CA".equals(bean.getCountry())){
			Canada = true;
			US = false;
		}
		
		if ("pickup".equalsIgnoreCase(bean.getVehicleType())
			|| "suv".equalsIgnoreCase(bean.getVehicleType()) ){
			ecopia422 = false;
			dueller422 = true;
		}
		// set the constant factors
		if (US && dueller422){
			factor2 = new BigDecimal("0.06");
		}
		if (Canada){
			factor1 = new BigDecimal("105000");
			if (ecopia422){
				factor2 = new BigDecimal("1.05");
			} else {
				factor2 = new BigDecimal("1.06");
			}
		}
		BigDecimal mileage = new BigDecimal(bean.getMileage());
		BigDecimal price = new BigDecimal(bean.getPrice());
		BigDecimal savings, value1, value2, interim;
		if (mileage.compareTo(BigDecimal.ZERO) <= 0) {
			return null;
		}
		// do the calculation
		if (US){
			interim = factor1.divide(mileage, 2, RoundingMode.HALF_UP);
			value1 = interim.multiply(price);
			interim = mileage.multiply(factor2);
			interim = interim.add(mileage);
			interim = factor1.divide(interim, 2, RoundingMode.HALF_UP);
			value2 = interim.multiply(price);
			savings = value1.subtract(value2);
	// 		Util.debug("value1="+value1);
	// 		Util.debug("value2="+value2);
	// 		Util.debug("savings="+savings);
		} else {
			interim = factor1.divide(factor3, 2, RoundingMode.HALF_UP);
			interim = interim.multiply(mileage);
			value1 = interim.multiply(price);
			interim = factor3.multiply(factor2);
			interim = factor1.divide(interim, 2, RoundingMode.HALF_UP);
			interim = interim.multiply(mileage);
			value2 = interim.multiply(price);
			savings = value1.subtract(value2);
	// 		Util.debug("value1="+value1);
	// 		Util.debug("value2="+value2);
	// 		Util.debug("savings="+savings);
		}
		savings = savings.setScale(2, RoundingMode.HALF_UP);
		bean.setSavings(savings.toString());
		return bean;
	}

}
