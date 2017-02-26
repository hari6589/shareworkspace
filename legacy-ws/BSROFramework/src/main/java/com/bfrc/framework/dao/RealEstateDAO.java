package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;

import com.bfrc.pojo.realestate.*;

public interface RealEstateDAO {

	
	
	List getAllRealestatePages();
	RealestatePage findRealestatePage(Object id);
	void createRealestatePage(RealestatePage item);
	void updateRealestatePage(RealestatePage item);
	void deleteRealestatePage(RealestatePage item);
	void deleteRealestatePage(Object id);
	
	List getAllRealestateRegionContacts();
	RealestateRegionContact findRealestateRegionContact(Object id);
	List getRealestateRegionContactsByWebSite(String webSite);
	Map getMappedRealestateRegionContactsByWebSite(String webSite);
	void createRealestateRegionContact(RealestateRegionContact item);
	void updateRealestateRegionContact(RealestateRegionContact item);
	void deleteRealestateRegionContact(RealestateRegionContact item);
	void deleteRealestateRegionContact(Object id);
	
	List getAllRealestateStoreGalleries();
	RealestateStoreGallery findRealestateStoreGallery(Object id);
	List getRealestateStoreGalleriesByYearAndWebSite(Object year, String webSite);
	List getRealestateStoreGalleryYearsByWebSite(String webSite);
	void createRealestateStoreGallery(RealestateStoreGallery item);
	void updateRealestateStoreGallery(RealestateStoreGallery item);
	void deleteRealestateStoreGallery(RealestateStoreGallery item);
	void deleteRealestateStoreGallery(Object id);
	
	List getAllRealestateSurplusProperties();
	RealestateSurplusProperty findRealestateSurplusProperty(Object id);
	List getActiveRealestateSurplusPropertiesByWebSite(String webSite);
	List getRealestateSurplusPropertiesByWebSite(String webSite);
	void createRealestateSurplusProperty(RealestateSurplusProperty item);
	void updateRealestateSurplusProperty(RealestateSurplusProperty item);
	void deleteRealestateSurplusProperty(RealestateSurplusProperty item);
	void deleteRealestateSurplusProperty(Object id);
	
	
	
}
