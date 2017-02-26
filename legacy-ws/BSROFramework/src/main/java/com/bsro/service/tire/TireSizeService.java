package com.bsro.service.tire;

import java.util.Map;
import java.util.List;

import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bsro.databean.vehicle.FriendlySizeDataBean;
import com.bsro.databean.vehicle.TireSize;
import com.bsro.databean.vehicle.RimDiameter;

public interface TireSizeService {
	public Map<String, String> getCrossSectionOptions() throws Exception;
	
	public Map<String, String> getAspectOptionsByCrossSection(String crossSection) throws Exception;
	
	public Map<String, String> getRimOptionsByCrossSectionAndAspect(String crossSection, String aspect) throws Exception;
	
	public Map<String, String> getRimOptions() throws Exception;
	
	public List<RimDiameter> getRimDiameters() throws Exception;
	
	public List<RimDiameter> getRimDiameters(String siteName) throws Exception;
	
	public Map<String, String> getVehicleSizesByRim(String rim) throws Exception;
	
	public FriendlySizeDataBean getVehicleSizesByRimSize(String rim, String siteName) throws Exception;
	
	public SeoVehicleData getSeoVehicleData(String siteName, String fileId, String recordId);
}
