/**
 * 
 */
package com.bfrc.dataaccess.dao.generic;

import java.util.Collection;

import com.bfrc.dataaccess.core.orm.IGenericOrmDAO;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;

/**
 * @author smoorthy
 *
 */
public interface StoreAdminAnnouncementDAO extends IGenericOrmDAO<StoreAdminAnnouncement, Long> {
	
	public Collection<StoreAdminAnnouncement> findZoneManagerCurrentAnnouncementsByEmail(String email);
	
	public Collection<StoreAdminAnnouncement> findZoneManagerCurrentAnnouncementsByZoneId(Object ZoneId);
	
	public Collection<StoreAdminAnnouncement> findDistrictManagerCurrentAnnouncementsByEmail(String email);
	
	public Collection<StoreAdminAnnouncement> findDistrictManagerCurrentAnnouncementsByDistrictId(String districtId);
	
	public Collection<StoreAdminAnnouncement> findNationalCurrentAnnouncements();
	
	public Collection<StoreAdminAnnouncement> findNationalCurrentApprovedAnnouncements();
	
	public Collection<StoreAdminAnnouncement> findNationalExpiredAnnouncements();
	
	public Collection<StoreAdminAnnouncement> findZoneManagerExpiredAnnouncementsByEmail(String email);
	
	public Collection<StoreAdminAnnouncement> findZoneManagerExpiredAnnouncementsByZoneId(Object ZoneId);
	
	public Collection<StoreAdminAnnouncement> findDistrictManagerExpiredAnnouncementsByEmail(String email);
	
	public Collection<StoreAdminAnnouncement> findDistrictManagerExpiredAnnouncementsByDistrictId(String districtId);
	
	public Collection<StoreAdminAnnouncement> findNationalActionAnnouncementItems();
	
	public Collection<StoreAdminAnnouncement> findZoneManagerActionAnnouncementsByEmail(String email);
	
	public Collection<StoreAdminAnnouncement> findZoneManagerActionAnnouncementsByZoneId(Object ZoneId);
	
	public Collection<Store> findDistrictManagerAnnouncementStoresByAnnouncementId(Object announcementId, String email);
	
	public Collection<Store> findZoneManagerAnnouncementStoresByAnnouncementId(Object announcementId, String email);
	
	public Collection<Store> findNationalAnnouncementStoresByAnnouncementId(Object announcementId);
	
	public Collection<Long> findNationalAnnouncementStoresByAnnouncementIdAndStoreType(Object announcementId, String storeType);
	
	public Collection<StoreAdminAnnouncement> findAnnouncementsByStoreId(Object storeId);
	
	public Collection<StoreAdminAnnouncement> findAnnouncementsByStoreIdAndDate(String startDate, String expirationDate, Object storeId);
	
	public Collection<StoreAdminAnnouncement> findAnnouncementsByStoreIdAndBrand(Object storeNumber, String brand);

}
