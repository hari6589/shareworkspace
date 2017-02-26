package com.bfrc.framework.dao;

import com.bfrc.pojo.lookup.SeoVehicleData;

/**
 * @author smoorthy
 *
 */
public interface SeoVehicleDataDAO {
	
	SeoVehicleData getSEOVehicleData(String siteName, String fileId, String recordId);

}
