package com.bfrc.framework.dao;


import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.pojo.tire.TireSearchResults;
import com.bfrc.pojo.tire.TireSubBrand;
import com.bfrc.UserSessionData;

public interface TireSearchDAO {
	
	TireSearchResults fetchTireSearchResults(UserSessionData userSessionData, String remoteIP, String geoPoint, String tireSuppressionOverrideToken);
	TireSearchResults searchTiresBySize(UserSessionData tireSearchRequest,TireSearchResults results);
	TireSearchResults searchTiresByVehicle(UserSessionData tireSearchRequest,TireSearchResults results);
	
	public List<TireSubBrand> getAllTireSubBrands();
	
	public List<TireSubBrand> getTireSubBrandsByBrandId(Long brandId);
	
	public List<TireSubBrand> getTireSubBrandsByBrandName(String brandName);
}
