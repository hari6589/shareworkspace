package com.bfrc.dataaccess.dao.oil;

import java.util.List;

import com.bfrc.dataaccess.model.oil.OatsOilRecommendationCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleMakeCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearCache;
import com.bfrc.dataaccess.model.oil.OatsVehicleYearToMake;
import com.bfrc.dataaccess.model.oil.Oil;
import com.bfrc.dataaccess.model.oil.OilChange;
import com.bfrc.dataaccess.model.oil.OilFilter;
import com.bfrc.dataaccess.model.oil.OilType;

public interface OilDAO {
	public Oil get(Long articleNumber);
	
	public OilType getOilType(Long oilTypeId);
	
	public List<Oil> findOilByOATSName(String oatsName);
	
	public Oil findAdditionalOilByType(Long oilTypeId);
	
	public OilChange findOilChangeByOilType(Long oilTypeId);
	
	public OilFilter getOilFilter();
	
	public OilChange getOilChange(Long articleNumber);
	
	public Oil findHighMileageOilByViscosity(String viscosity);
	
	public void saveOatsVehicleYearCache(OatsVehicleYearCache oatsVehicleYearCache);
	
	public void saveOatsVehicleYearToMake(OatsVehicleYearToMake oatsVehicleYearToMake);
	
	public void saveOatsVehicleMakeCache(OatsVehicleMakeCache oatsVehicleMakeCache);
	
	public void saveOatsVehicleCache(OatsVehicleCache oatsVehicleCache);
	
	public void saveOatsOilRecommendationCache(OatsOilRecommendationCache oatsOilRecommendationCache);
}
