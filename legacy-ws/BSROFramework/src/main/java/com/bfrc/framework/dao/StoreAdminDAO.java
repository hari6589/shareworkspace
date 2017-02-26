package com.bfrc.framework.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.bfrc.pojo.storeadmin.*;

public interface StoreAdminDAO {
	StoreAdminOfferTemplate findOfferTemplateById(Object id);
	StoreAdminOfferTemplate findOfferTemplateByIdAndBrand(Object id, String brand);
	Long createOfferTemplate(StoreAdminOfferTemplate item);
	void updateOfferTemplate(StoreAdminOfferTemplate item);
	void deleteOfferTemplate(StoreAdminOfferTemplate item);
	void deleteOfferTemplate(StoreAdminOfferTemplate item, boolean deleteImages);
	void deleteOfferTemplateById(Object id);
	
	StoreAdminOfferTemplateImages findOfferTemplateImagesByIdAndBrand(Object id, String brand);
	Long createOfferTemplateImages(StoreAdminOfferTemplateImages item);
	void deleteTemplateImagesByTemplateId(Object id);
	void deleteTemplateImagesByImageId(Object id);
	
	StoreAdminOffer findOfferById(Object id);
	List findOffersByPromotion(Object id);
	Long createOffer(StoreAdminOffer item);
	void updateOffer(StoreAdminOffer item);
	void deleteOffer(StoreAdminOffer item);
	void deleteOfferById(Object id);
	
	StoreAdminPromotion findPromotionById(Object id);
	Long createPromotion(StoreAdminPromotion item);
	void updatePromotion(StoreAdminPromotion item);
	void deletePromotion(StoreAdminPromotion item);
	void deletePromotionById(Object id);
	
	StoreAdminOfferCategory findOfferCategoryById(Object id);
	Long createOfferCategory(StoreAdminOfferCategory item);
	void updateOfferCategory(StoreAdminOfferCategory item);
	void deleteOfferCategory(StoreAdminOfferCategory item);
	void deleteOfferCategoryById(Object id);

	StoreAdminPromotionStoreJoin findAdminPromotionStoreJoinById(Object id);
	StoreAdminPromotionStoreJoin findAdminPromotionStoreJoinById(Object promoId, Object storeNumber);
	List findAdminPromotionStoreJoinByPromotion(Object id);
	StoreAdminPromotionStoreJoinId createPromotionStoreJoin(StoreAdminPromotionStoreJoin item);
	void updatePromotionStoreJoin(StoreAdminPromotionStoreJoin item);
	void deletePromotionStoreJoin(StoreAdminPromotionStoreJoin item);
	void deletePromotionStoreJoinById(Object id);
	
	List getAllWebsites();	
	List getAllOfferCategories();
	List getAllOffers();
	List getAllOfferTemplates();
	List getAllOfferTemplatesImages();
	List getAllOfferTemplatesImagesById(Object id);
	StoreAdminOfferTemplateImages getBannerTemplateImages(Long id, Object siteName, List storeAdminOfferTmplImages);
	StoreAdminOfferTemplateImages getCouponTemplateImages(Long id, Object siteName, List storeAdminOfferTmplImages);
	public List getAllActiveOfferTemplates(Long categoryId);
	public Boolean checkOffersByTemplate(Object id); 
	public List findAdminOfferStoreJoinByOffer(Object id);
	
	List findNationalActionItems();
	List findZoneManagerActionItemsByEmail(String email);
	List findZoneManagerActionItemsByZoneId(Object id);
	
	
	List findZoneManagerCurrentPromotionsByEmail(String email);
	List findZoneManagerCurrentPromotionsByZoneId(Object zoneId);
	List findDistrictManagerCurrentPromotionsByEmail(String email);
	List findDistrictManagerCurrentPromotionsByDistrictId(String districtId);
	List findNationalCurrentPromotions();
	List findNationalCurrentPromotions(String brand);
	
	StoreAdminAnnouncement findAnnouncementById(Object id);
	List findAdminAnnouncementStoreJoinById(Object announcementId, Object storeNumber);
	List findAdminAnnouncementStoreJoinById(Object id);
	
	List findAdminAnnouncementStoreJoinByAnnouncement(Object id);
	List findZoneManagerCurrentAnnouncementsByEmail(String email);
	List findZoneManagerCurrentAnnouncementsByZoneId(Object zoneId);
	List findDistrictManagerCurrentAnnouncementsByEmail(String email);
	List findDistrictManagerCurrentAnnouncementsByDistrictId(String districtId);
	List findNationalCurrentAnnouncements();
	
	List findNationalExpiredPromotions();
	List findZoneManagerExpiredPromotionsByEmail(String email);
	List findZoneManagerExpiredPromotionsByZoneId(Object id);
	List findDistrictManagerExpiredPromotionsByEmail(String email);
	List findDistrictManagerExpiredPromotionsByDistrictId(String districtId);
	
	List findDistrictManagerOfferStoresByOfferId(String offerId, String email);
	List findZoneManagerOfferStoresByOfferId(String offerId, String email);
	List findNationalOfferStoresByOfferId(String offerId);
	
	List findDistrictManagerPromotionStoresByPromoId(String promoId, String email);
	List findZoneManagerPromotionStoresByPromoId(String promoId, String email);
	List findNationalPromotionStoresByPromoId(String promoId);
	
	List findDistrictManagerAnnouncementStoresByAnnouncementId(String announcementId, String email);
	List findZoneManagerAnnouncementStoresByAnnouncementId(String announcementId, String email);
	List findNationalAnnouncementStoresByAnnouncementId(String announcementId);

	Map getMappedOfferCategories();
	Map getMappedTemplates();
	Map getMappedCategoriesWithTemplateId();
	
	public StoreAdminStoreImage findWorkingStoreAdminStoreImageByStoreNumber(Object id);
	public StoreAdminStoreImage findPublishedStoreAdminStoreImageByStoreNumber(Object id);
	public StoreAdminStoreImage findStoreAdminStoreImageById(Object imageId);
	public List findAllStoreAdminLibraryImages();
	public StoreAdminLibraryImage findStoreAdminLibraryImageById(Object imageId);
	
	public List fetchOtherStoresInConflict(String promoId, List storeNumbers,
			List categoriesUsed, Date startDate, Date endDate); 
	public Long createAnnouncement(StoreAdminAnnouncement item);
	public void updateAnnouncement(StoreAdminAnnouncement item);
	public void deleteAnnouncement(StoreAdminAnnouncement item);
	public void deleteAnnouncementById(Object id);
	
	public StoreAdminAnnouncementStoreJoinId createAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item);
	public void updateAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item);
	public void deleteAnnouncementStoreJoin(StoreAdminAnnouncementStoreJoin item);
	public void deleteAnnouncementStoreJoinById(Object id);
	public void deleteAnnouncementStoreJoinById(Object announcementId, Object storeNumber);

	public void createStoreAdminStoreImage(StoreAdminStoreImage item);
	public void updateStoreAdminStoreImage(StoreAdminStoreImage item);
	public void deleteStoreAdminStoreImage(StoreAdminStoreImage item);
	public Long createStoreAdminLibraryImage(StoreAdminLibraryImage item);
	public void updateStoreAdminLibraryImage(StoreAdminLibraryImage item);
	public void deleteStoreAdminLibraryImage(StoreAdminLibraryImage item);
	
	public List findAnnouncementsByStoreId(String storeId);
	public List findAnnouncementsByStoreIdAndDate(String storeId, Date selDate);
	public List findPromotionsByStoreId(String storeNum);
	public List findPromotionsByStoreIdAndDate(String storeNum, Date selDate);
	

	public void updateBannerSettingOnStores(List<Long> storeNumbers, String bannerSetting);


}
