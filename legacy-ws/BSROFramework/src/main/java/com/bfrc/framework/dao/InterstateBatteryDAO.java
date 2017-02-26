package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;

import com.bfrc.pojo.interstatebattery.*;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;

public interface InterstateBatteryDAO {

	
	/**
	 * @return List<Long>
	 */
	List getYearList();
	List getMakeList(Object year);
	List getModelList(Object year,Object make);
	List getEngineList(Object year, Object make, Object model);
	List getBatteryList(Object year, Object make, Object model, Object engine);
	List getBatteryListOptional(Object year, Object make, Object model, Object engine);
	List getBatteryListLightMakeModelOnly();
	
	
	InterstateBatteryMaster getInterstateBatteryMasterById(Object id);
	InterstateBatteryWebPrices getInterstateBatteryWebPricesById(Object id);
	
	void updateInterstateBatteryWebPrices(InterstateBatteryWebPrices item) throws Exception;
	
	Map getMappedBatteryMaster();
	
	Map getMappedBatteryWebPrices();
	
	InterstateBatteryQuote getInterstateBatteryQuote(Object id);
	void createInterstateBatteryQuote(InterstateBatteryQuote item) throws Exception;;
	void updateInterstateBatteryQuote(InterstateBatteryQuote item) throws Exception;;
	void deleteInterstateBatteryQuote(Object id);
	
	BatteryLifeDuration getBatteryLifeDuration(Object id);
	void createBatteryLifeDuration(BatteryLifeDuration item) throws Exception;;
	void updateBatteryLifeDuration(BatteryLifeDuration item) throws Exception;;
	void deleteBatteryLifeDuration(Object id);
	
	BatteryLifeRegion getBatteryLifeRegion(Object id);
	void createBatteryLifeRegion(BatteryLifeRegion item) throws Exception;;
	void updateBatteryLifeRegion(BatteryLifeRegion item) throws Exception;;
	void deleteBatteryLifeRegion(Object id);
	
	BatteryLifeDuration getBatteryLifeDurationByZipCode(Object id);
	public InterstateAutomobile fetchInterstateAutomobile(Long interstateAutomobileId);
	List<InterstateAutomobile> getBatteryListIgnoreCase(String year, String make, String model, String engine);
	List<String> getEngineListIgnoreCase(String year, String make, String model);
	List<String> getModelListIgnoreCase(String year, String make);
	
	
}
