package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;
import java.util.List;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotion;

/**
 * @author smoorthy
 *
 */
public interface StoreAdminPromotionDAO extends IGenericOrmDAO<StoreAdminPromotion, Long> {
	
	public Collection<StoreAdminPromotion> findZoneManagerActionItemsByEmail(String email);
	
	public Collection<StoreAdminPromotion> findZoneManagerActionItemsByZoneId(Object id);
	
	public Collection<StoreAdminPromotion> findNationalActionItems();
	
	public Collection<StoreAdminPromotion> findZoneManagerCurrentPromotionsByEmail(String email);
	
	public Collection<StoreAdminPromotion> findZoneManagerCurrentPromotionsByZoneId(Object zoneId);
	
	public Collection<StoreAdminPromotion> findDistrictManagerCurrentPromotionsByEmail(String email);
	
	public Collection<StoreAdminPromotion> findDistrictManagerCurrentPromotionsByDistrictId(String districtId);
	
	public Collection<StoreAdminPromotion> findNationalCurrentPromotionsByBrand(List<String> brand);
	
	public Collection<StoreAdminPromotion> findNationalCurrentPromotions();
	
	public Collection<StoreAdminPromotion> findNationalExpiredPromotions();
	
	public Collection<StoreAdminPromotion> findZoneManagerExpiredPromotionsByEmail(String email);
	
	public Collection<StoreAdminPromotion> findZoneManagerExpiredPromotionsByZoneId(Object id);
	
	public Collection<StoreAdminPromotion> findDistrictManagerExpiredPromotionsByEmail(String email);
	
	public Collection<StoreAdminPromotion> findDistrictManagerExpiredPromotionsByDistrictId(String districtId);
	
	public Collection<StoreAdminPromotion> findPromotionsByStoreId(Long storeNumber);
	
	public Collection<StoreAdminPromotion> findPromotionsByStoreIdAndBrand(Long storeNumber, String brand);
	
	public Collection<StoreAdminPromotion> findPromotionsByStoreIdAndDate(String startDate, String endDate, Long storeNumber);
	
	public Collection<Store> findDistrictManagerPromotionStoresByPromoId(String promoId, String email);
	
	public Collection<Store>  findZoneManagerPromotionStoresByPromoId(String promoId, String email);
	
	public Collection<Store>  findNationalPromotionStoresByPromoId(String promoId);
	
	public Collection<Long>  findNationalPromotionStoresByPromoIdAndStoreType(Object promoId, String storeType);

}
