package com.bfrc.dataaccess.dao.store;

import java.util.List;

import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.store.StoreFlags;
import com.bfrc.dataaccess.model.store.StoreHierarchy;
import com.bfrc.dataaccess.model.store.StoreHolidayHour;
import com.bfrc.dataaccess.model.store.StoreHour;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplateImages;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotion;

/**
 * @author smoorthy
 *
 */
public interface StoreBusinessRulesDAO {
	
	public StoreHierarchy getHierarchyByStoreNumber(Long storeNumber);
	
	public String checkStoreReasonCode(Long storeNumber);
	
	public List<Store> findStoreByStoreType(List<String> storeType);
	
	public List<Store> findStoreByStateCityType(String state, String city, List<String> storeType);
	
	public List<StoreHour> findStoreHoursByStoreType(List<String> storeType);
	
	public List<StoreHolidayHour> findStoreHolidayHoursByStoreType(List<String> storeType, List<Long> holidayIds);
	
	public StoreAdminOfferTemplate findOfferTemplateByIdAndBrand(Object id, String brand);
	
	public StoreAdminOfferTemplateImages findOfferTemplateImagesByIdAndBrand(Object id, String brand);
	
	public List<StoreAdminPromotion> findNationalCurrentPromotionsByBrand(List<String> brand);
	
	public List<StoreAdminAnnouncement> findNationalCurrentApprovedAnnouncements(List<String> brand);
	
	public List<StoreFlags> findStoreFlagsByType(String storeType);
	
	public StoreFlags findStoreFlagsByNumber(String storeNumber);
	
}
