package com.bfrc.framework.dao;

import com.bfrc.pojo.geodata.BsroCityStateGeoData;
import com.bfrc.pojo.geodata.BsroStateGeoData;
import com.bfrc.pojo.geodata.BsroZipGeoData;

public interface GeoDataDAO {

	
	

	boolean isCacheExpired(java.util.Date date);
	BsroZipGeoData findBsroZipGeoData(Object id);
	void createBsroZipGeoData(BsroZipGeoData item);
	void updateBsroZipGeoData(BsroZipGeoData item);
	void deleteBsroZipGeoData(BsroZipGeoData item);
	void deleteBsroZipGeoData(Object id);
	
	
	BsroStateGeoData findBsroStateGeoData(Object id);
	void createBsroStateGeoData(BsroStateGeoData item);
	void updateBsroStateGeoData(BsroStateGeoData item);
	void deleteBsroStateGeoData(BsroStateGeoData item);
	void deleteBsroStateGeoData(Object id);
	
	BsroCityStateGeoData findBsroCityStateGeoData(String state, String city);
	BsroCityStateGeoData findBsroCityStateGeoData(Object id);
	void createBsroCityStateGeoData(BsroCityStateGeoData item);
	void updateBsroCityStateGeoData(BsroCityStateGeoData item);
	void deleteBsroCityStateGeoData(BsroCityStateGeoData item);
	void deleteBsroCityStateGeoData(Object id);
	
	String getBsroIpGeoCF(Object id);
}
