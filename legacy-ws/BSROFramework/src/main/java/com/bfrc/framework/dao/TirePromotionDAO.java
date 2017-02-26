package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;
import java.util.Date;

import com.bfrc.pojo.tirepromotion.*;

public interface TirePromotionDAO {
	public static final String[] SITES = new String[]{"FCAC","TP","ET","PPS","ETIRE", "HT"};
	TirePromotionEvent getTirePromotion(Object id);
	
	List getAllTirePromotions();
	List getAllTirePromotionsWithoutBlobs();
	List getAllTirePromotionsBySiteAndStatus(String siteName, String statusFlag);	
	List getPublishedTirePromotionsBySite(String siteName);
	List<TirePromotionEvent> getPublishedTirePromotionsBySite(String siteName, Date startDate);
	
	List<TirePromotionDisplay> getTirePromotionDisplayInfo();
	List<TirePromotionDisplay> getTirePromotionDisplayInfo(final int start,final int maxResults);
	
	void createTirePromotion(TirePromotionEvent p) throws Exception;
	void deleteTirePromotion(Object id);
	void updateTirePromotion(TirePromotionEvent p) throws Exception;
	
	
	byte[] resizeImage(byte[] in, int width, int height) throws Exception;
	
	List getTirePromotionTypes();
	List getTirePromotionArticles(Object id);
	
	SourcePromotionType getTirePromotionType(Object id);
	List getTirePromotionStores(Object id);
	List getTirePromotionSites(Object id);
	
	void createTirePromotionSite(TirePromotionSite p) throws Exception;
	void createTirePromotionSites(List ps) throws Exception;
	void deleteTirePromotionSite(Object id);
	void updateTirePromotionSite(TirePromotionSite p) throws Exception;
	TirePromotionSite getTirePromotionSite(Object id);
	
	Map getTirePromotionStatusFlags();
	
	double getDiscountAmountByStoreNumberMinQtyPromoId(long article, long storeNumber, byte minQty, long promoId);
	
	List getTirePromotionsBySitesAndStoreNumber(String siteName, long storeNumber);
	List getTirePromotionsBySitesAndStoreNumberWithoutBlobs(String siteName, long storeNumber);
	
	Map loadTirePromotionData(String siteName, long storeNumber);
	List<TirePromotionEvent> getSpecialTirePromotionEvent(String siteName, long storeNumber, List<Long> articleIds);

	TirePromotionEvent getTirePromotionByFriendlyId(String promoName);
	int updateTirePromotionStatus(String promoName, char statusFlag);
}
