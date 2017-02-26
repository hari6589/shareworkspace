package com.bsro.service.store;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreSearch;
import com.bsro.databean.store.StoreDataBean;
import com.bsro.databean.store.StoreWidgetDataBean;

public interface StoreService {

	public Store getStoreById(Long storeNumber);
	public Store[] getStoresLight(long[][] storeNumber);
	public Store findStoreLightById(Long storeNumber);	
	public String getPipeDelimitedMilitaryStoreNumbers();
	public List getMilitaryStores();
	
	public void setMappedDistance(Map mappedDistance);
	public Map getMappedDistance();
	
	public StoreDataBean searchStoresByAddress(String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius, Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partner, Boolean fiveStarPrimary, int count);

	public Map<String, String> parseAddressString(String addressString, String siteName);
	
	public List<Store> searchStoresByStoreNumbers(String storeNumbers);
	public boolean isMilitaryStore(Long storeNumber);
	public List getStoreHours(Long storeNumber);

	public List<StoreHoliday> getStoreHolidaysBetweenDates(Date start,Date end);
	public Map<Long, List<String>> getStoreHolidayHours(List<Store> stores);
	public void setHolidayHours(List<Store> stores);
	public List<String> getStoreHolidayHours(Store store);
	public StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId);
	public List<String> getHolidayHoursMessages(Store store, List<StoreHoliday> holidayList);
	public List<StoreHoliday> getStoreLocatorHolidays();
	public String getStoreHolidayMessage(StoreHoliday holiday, StoreHolidayHour hours);
	public String checkStoreReasonCode(long number);
    public StoreWidgetDataBean initializeStoreWidget(String context, Long selectedStoreNumber, String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius, Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partner, Boolean fiveStarPrimary, int count);
	public StoreSearch createStoreSearchObject(String navZip);
	public void setMilitaryStores(List<Store> stores);
	public List<Store> getStoresEligibleForAppointments(StoreSearch storeSearch, String ipAddress);
	public List<Store> findStoresLightByIds(List<Long> storeNumbers);
	public StoreDataBean searchStoresByState(String app, String state, String remoteIP, Boolean storesWithTirePricingOnly, Boolean includeLicensees, Boolean partnerPricing, Boolean fiveStarPrimary, Boolean lightStores, String p);
	public List<Store> findStoresLightByStoreNumbers(String pipeDelimitedStoreNumbers);
	public StoreDataBean searchStoresByAddress(String siteName, String address, String city, String state, String zip, Integer radius, String remoteIP, String geoPoint, Boolean ignoreRadius, Boolean storesWithTirePricingOnly, Boolean includeLicensees, String licenseeType, Boolean partner, Boolean fiveStarPrimary, int count, boolean lightStores, boolean ignoreActiveFlag);
}
