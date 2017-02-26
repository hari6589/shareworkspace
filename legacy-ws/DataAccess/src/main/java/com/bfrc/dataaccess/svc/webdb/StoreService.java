package com.bfrc.dataaccess.svc.webdb;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.store.StoreFlags;
import com.bfrc.dataaccess.model.store.StoreHoliday;
import com.bfrc.dataaccess.model.store.StoreHolidayHour;
import com.bfrc.dataaccess.model.store.StoreHour;
import com.bfrc.dataaccess.model.store.StoreLocation;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotionViewObject;

/**
 * @author smoorthy
 *
 */
public interface StoreService {
	
	public Store findStoreById(Long storeNumber);
	
	public Store findStoreLightById(Long storeNumber);
	
	public List<Store> findStoresByType(String storeType);
	
	public List<StoreLocation> findClosestStoresByGeoPoints(String lat, String lng, String siteName, Integer storeCount);
	
	public List<StoreLocation> findClosestStoresByState(String state, String siteName);
	
	public List<StoreLocation> findClosestStoresByZip(String zip, String siteName, Integer storeCount, String remoteIP);
	
	public List<StoreLocation> findClosestStoresByAddress(String address, String city, String state, String zip, String siteName, Integer storeCount, String remoteIP);
	
	public boolean isMilitaryStore(Long storeNumber);
	
	public Set<StoreHour> getSortedStoreHours(Set<StoreHour> storeHours);
	
	public String getStoreHourHTML(Collection<StoreHour> storeHours);
	
	public String getStoreHourHTML(Collection<StoreHour> storeHours, boolean isFormatted);
	
	public Map<Long, List<String>> getStoreHolidayHours(List<Store> stores);
	
	public List<String> getStoreHolidayHours(Store store);
	
	public List<StoreHoliday> getStoreLocatorHolidays();
	
	public List<StoreHoliday> getOrderedStoreHolidaysBetweenDates(Date start,Date end);
	
	public StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId);
	
	public List<String> getHolidayHoursMessages(Store store, List<StoreHoliday> holidayList);
	
	public String getStoreHolidayMessage(StoreHoliday holiday, StoreHolidayHour hours);
	
	public List<StoreAdminAnnouncement> getAnnouncements(Store store);
	
	public Map<Long, List<StoreAdminAnnouncement>> getAnnouncementsByStoreType(List<String> stype);
	
	public List<StoreAdminPromotionViewObject> getLocalPromotions(Store store);
	
	public Map<Long, List<StoreAdminPromotionViewObject>> getLocalPromotionsByStoreType(List<String> stype);
	
	public String getAbbreviateStateName(String state);
	
	public List<StoreFlags> findStoreFlagsByType(String storeType);

	public StoreFlags findStoreFlagsByNumber(String storeNumber);
}