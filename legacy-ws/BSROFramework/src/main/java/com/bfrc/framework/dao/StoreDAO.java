package com.bfrc.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bfrc.Bean;
import com.bfrc.Config;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreMilitary;

public interface StoreDAO extends Bean {
	List getStoresInState(String state);
	List getStoresInState(String state, String pageNumber);
	List getStates(Map param);
	List getCities(Map param);
	List getStoreStates();
	List getStoreCities(String state);
	List getStores(String state, String city);
	List getStores(Map param);
	List getStoresInZip(Map param);
	Store findStoreById(Long storeNumber);
	Map getFullStoreMap(Config c);
	Map getPrimaryStoreMap(Config c);
	Map getPrimaryStoreMap(Config c, String licenseeType);
	Map getSecondaryStoreMap(Config c);
	Map getFullStoreMap(String siteName);
	Map getPrimaryStoreMap(String siteName);
	Map getPrimaryStoreMap(String siteName, String licenseeType);
	Map getSecondaryStoreMap(String siteName);
	List getStoreHours(Long storeNumber);
	String getStoreHourHTML(List storeHours);
	List findStoresByStoreNumbers(List storeNumbers);
	List findStoresByStoreNumbers(String pipeDelimitedStoreNumbers);
	List findSiteStoreList(Config thisConfig);
	List findSiteStoreList(String siteName);
	List findStoresByZoneManagerEmail(String email);
	List findStoresByZoneId(Object id);
	List findStoresLightByZone(String id, String brand) ;
	List findStoresByDistrictManagerEmail(String email);
	List findStoresByDistrictId(Object id);
	List getStoresLightByDistrict(String id);
	List getStoresLightByDistrict(String id, String brand);
	List<Store> getStoresLightByDistrict(String id, String[] brands);
	List getStoresLightByAdresses(String address, String city, String state, String zip);
	List getStoresLightCityStateOnly();
	StoreMilitary findMilitaryStore(Long storeNumber);
	List<StoreHoliday> getStoreHolidaysBetweenDates(Date start,Date end);
	List<StoreHoliday> getOrderedStoreHolidaysBetweenDates(Date start,Date end);
	StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId);
	String checkStoreReasonCode(long storeNumber);
	Store findStoreLightById(Long id);
	List<Store> findStoresLightByIds(List<Long> id);//id can be storenumber
	List<Store> findSiteStoreLightList(String siteName);
	List<Long> findMilitaryStores(List<Long> storeNumbers);
	void setStoreCount(int count);
	int getStoreCount();
	
}