package com.bsro.service.time;

import java.util.Date;

import com.bfrc.pojo.zipcode.ZipCodeData;


public interface TimeZoneDstService {

	public boolean isInDayLightSavings(Date date);
	public ZipCodeData getZipCodeDataByZip(String zip);
	public String getStateByZip(String zip);
	
}
