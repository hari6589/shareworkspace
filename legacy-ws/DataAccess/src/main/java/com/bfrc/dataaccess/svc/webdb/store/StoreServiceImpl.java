package com.bfrc.dataaccess.svc.webdb.store;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import map.States;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;
import com.bfrc.dataaccess.dao.generic.StoreAdminAnnouncementDAO;
import com.bfrc.dataaccess.dao.generic.StoreAdminPromotionDAO;
import com.bfrc.dataaccess.dao.generic.StoreDAO;
import com.bfrc.dataaccess.dao.generic.StoreHolidayDAO;
import com.bfrc.dataaccess.dao.generic.StoreHolidayHourDAO;
import com.bfrc.dataaccess.dao.generic.StoreHourDAO;
import com.bfrc.dataaccess.dao.generic.StoreMilitaryDAO;
import com.bfrc.dataaccess.dao.store.StoreBusinessRulesDAO;
import com.bfrc.dataaccess.model.store.HrDistricts;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.store.StoreFlags;
import com.bfrc.dataaccess.model.store.StoreHierarchy;
import com.bfrc.dataaccess.model.store.StoreHoliday;
import com.bfrc.dataaccess.model.store.StoreHolidayHour;
import com.bfrc.dataaccess.model.store.StoreHolidayId;
import com.bfrc.dataaccess.model.store.StoreHour;
import com.bfrc.dataaccess.model.store.StoreLocation;
import com.bfrc.dataaccess.model.store.StoreMilitary;
import com.bfrc.dataaccess.model.store.WeekDayComparator;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminAnnouncement;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOffer;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferTemplateImages;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminOfferViewObject;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotion;
import com.bfrc.dataaccess.model.storeadmin.StoreAdminPromotionViewObject;
import com.bfrc.dataaccess.svc.webdb.StoreService;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.security.Encode;
import com.bfrc.storelocator.util.LocatorUtil;

/**
 * @author smoorthy
 *
 */

@Service
public class StoreServiceImpl implements StoreService {
	
	@Autowired
	StoreDAO storeDAO;
	
	@Autowired
	StoreHourDAO storeHourDAO;
	
	@Autowired
	StoreMilitaryDAO storeMilitaryDAO;
	
	@Autowired
	StoreHolidayDAO storeHolidayDAO;
	
	@Autowired
	StoreHolidayHourDAO storeHolidayHourDAO;
	
	@Autowired
	StoreBusinessRulesDAO storeBusinessRulesDAO;
	
	@Autowired
	StoreAdminAnnouncementDAO storeAdminAnnouncementDAO;
	
	@Autowired
	StoreAdminPromotionDAO storeAdminPromotionDAO;
	
	@Autowired
	private PropertyAccessor propertyAccessor;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	private States statesMap;
	
	private final int DAYS_PRIOR_TO_DISPLAY_HOLIDAY = 30;
	private final int MAX_HOLIDAYS_TO_DISPLAY = 2;
	private final int STORE_DISPLAY_COUNT = 10;
			
	public Store findStoreById(Long storeNumber) {
		Store store = storeDAO.get(storeNumber);
		if (store == null) {
			return null;
		}
		
		if (store.getHours() != null && !store.getHours().isEmpty()) {
			store.setHours(getSortedStoreHours(store.getHours()));
		}
		
		setStoreHierarchy(store);
		store.setReasonDesc(storeBusinessRulesDAO.checkStoreReasonCode(storeNumber));
		store.setHolidayHourMessages(getStoreHolidayHours(store));
		store.setMilitaryStore(isMilitaryStore(storeNumber));
		store.setLocalPromos(getLocalPromotions(store));
		store.setAnnouncements(getAnnouncements(store));
		return store;
	}
	
	public Store findStoreLightById(Long storeNumber) {
		Store store = storeDAO.get(storeNumber);
		if (store == null) {
			return null;
		}
		
		if (store.getHours() != null && !store.getHours().isEmpty()) {
			store.setHours(getSortedStoreHours(store.getHours()));
		}
		
		store.setReasonDesc(storeBusinessRulesDAO.checkStoreReasonCode(storeNumber));
		store.setHolidayHourMessages(getStoreHolidayHours(store));
		store.setMilitaryStore(isMilitaryStore(storeNumber));
		return store;
	}
	
	public List<Store> findStoresByType(String storeType) {
		List<Store> stores = null;
		if (StringUtils.isNullOrEmpty(storeType)) {
			return null; 
		}
		storeType = storeType.toUpperCase();
		
		/*
		 * 		Get valid store types by parsing the input
		 * */
		List<String> stype = getValidStoreTypes(storeType);
		if (StringUtils.isNullOrEmpty(stype) || stype.isEmpty()) {
			return null;
		}
		
		/*
		 * 		Get all the stores by store type
		 * */
		stores = storeBusinessRulesDAO.findStoreByStoreType(stype);
		if (StringUtils.isNullOrEmpty(stores) || stores.isEmpty()) {
			return null;
		}
		
		/*
		 * 		Get all the store hours by store type
		 * */
		List<StoreHour> storeHours = storeBusinessRulesDAO.findStoreHoursByStoreType(stype);
		Map<Long, Set<StoreHour>> storeHoursMap = new HashMap<Long, Set<StoreHour>>();
		for (StoreHour storeHour : storeHours) {
			Set<StoreHour> shr = null;
			if (storeHoursMap.containsKey(storeHour.getId().getStoreNumber())) {
				storeHoursMap.get(storeHour.getId().getStoreNumber()).add(storeHour);
			} else {
				shr = new HashSet<StoreHour>();
				shr.add(storeHour);
				storeHoursMap.put(storeHour.getId().getStoreNumber(), shr);
			}			
		}
		
		/*
		 * 		Get all the store holiday hours & messages by store type
		 * */
		List<StoreHoliday> holidayList = getStoreLocatorHolidays();
		Map<Long, List<String>> storeHolidayHoursMap = getStoreHolidayHoursByStoretype(stores, stype, holidayList);
		
		/*
		 * 		Get all the military stores
		 * */
		List<StoreMilitary> militaryStores = new ArrayList<StoreMilitary>(storeMilitaryDAO.listAll());
		
		/*
		 * 		Get all the local store promotions
		 * */
		Map<Long, List<StoreAdminPromotionViewObject>> storePromotions = getLocalPromotionsByStoreType(stype);
		
		/*
		 * 		Get all the store announcements
		 * */
		Map<Long, List<StoreAdminAnnouncement>> storeAnnouncements = getAnnouncementsByStoreType(stype);
				
		for (Store store : stores) {
			Set<StoreHour> shrs = storeHoursMap.get(store.getStoreNumber());
			if (shrs != null && !shrs.isEmpty()) {
				store.setHours(getSortedStoreHours(shrs));
			}
			store.setHolidayHourMessages(storeHolidayHoursMap.get(store.getStoreNumber()));
			store.setMilitaryStore(militaryStores.contains(new StoreMilitary(store.getStoreNumber())));
			store.setLocalPromos(storePromotions.get(store.getStoreNumber()));
			store.setAnnouncements(storeAnnouncements.get(store.getStoreNumber()));
		}
		return stores;
	}
	
	public List<StoreLocation> findClosestStoresByGeoPoints(String lat, String lng, String siteName, Integer storeCount) {
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		List<StoreLocation> storeLocations = null;
	    Float[] location = new Float[2];
		location[1] = new Float(lat.trim());
		location[0] = new Float(lng.trim());
		
		String app = locator.getConfig().getSiteName();
		int storesToShow = STORE_DISPLAY_COUNT;
		if(!ServerUtil.isNullOrEmpty(storeCount)) {
			storesToShow = storeCount.intValue();
		}
		
		long[][] storeArray = locator.getClosestStores(location, app, true, 5, false, null, false, null, storesToShow, true);
		if (storeArray != null && storeArray.length > 0) {
			//Go get the actual stores
			storeLocations = new ArrayList<StoreLocation>();
			for (int idx = 0; idx < storeArray.length; idx++) {
				StoreLocation storeLocation = new StoreLocation();
				storeLocation.setStore(findStoreLightById(storeArray[idx][0]));
				storeLocation.setDistance(Long.valueOf(storeArray[idx][1]));
				
				storeLocations.add(storeLocation);
			}			
		}
		
		return storeLocations;	
	}
	
	public List<StoreLocation> findClosestStoresByState(String state, String siteName) {
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		List<StoreLocation> storeLocations = null;	    
	    String app = locator.getConfig().getSiteName();
		long[][] storeArray = locator.getClosestStores(null, app, true, 5, false, state.toUpperCase());
		
		if (storeArray != null && storeArray.length > 0) {
			//Go get the actual stores
			storeLocations = new ArrayList<StoreLocation>();
			for (int idx = 0; idx < storeArray.length; idx++) {
				StoreLocation storeLocation = new StoreLocation();
				storeLocation.setStore(findStoreLightById(storeArray[idx][0]));
								
				storeLocations.add(storeLocation);
			}			
		}
		
		return storeLocations;
	}
	
	public List<StoreLocation> findClosestStoresByZip(String zip, String siteName, Integer storeCount, String remoteIP) {
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		List<StoreLocation> storeLocations = null;	    
	    int storesToShow = STORE_DISPLAY_COUNT;
		if(!ServerUtil.isNullOrEmpty(storeCount)) {
			storesToShow = storeCount.intValue();
		}
	    long requestId = locator.getRequestId();
	    String app = locator.getConfig().getSiteName();
	    Float[] location = locator.geoLocationWithBing(requestId, app, null, null, null, zip, remoteIP);
	    long[][] storeArray = locator.getClosestStores(location, app, true, 5, false, null, false, null, storesToShow, true);
	    
	    if (storeArray != null && storeArray.length > 0) {
			//Go get the actual stores
			storeLocations = new ArrayList<StoreLocation>();
			for (int idx = 0; idx < storeArray.length; idx++) {
				StoreLocation storeLocation = new StoreLocation();
				storeLocation.setStore(findStoreLightById(storeArray[idx][0]));
				storeLocation.setDistance(Long.valueOf(storeArray[idx][1]));
				
				storeLocations.add(storeLocation);
			}			
		}
		
		return storeLocations;
	}
	
	public List<StoreLocation> findClosestStoresByAddress(String address, String city, String state, String zip, String siteName, Integer storeCount, String remoteIP) {
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		List<StoreLocation> storeLocations = null;	    
	    int storesToShow = STORE_DISPLAY_COUNT;
		if(!ServerUtil.isNullOrEmpty(storeCount)) {
			storesToShow = storeCount.intValue();
		}
	    long requestId = locator.getRequestId();
	    String app = locator.getConfig().getSiteName();
	    Float[] location = locator.geoLocationWithBing(requestId, app, address, city, state, zip, remoteIP);
	    long[][] storeArray = locator.getClosestStores(location, app, true, 5, false, null, false, null, storesToShow, true);
	    
	    if (storeArray != null && storeArray.length > 0) {
			//Go get the actual stores
			storeLocations = new ArrayList<StoreLocation>();
			for (int idx = 0; idx < storeArray.length; idx++) {
				StoreLocation storeLocation = new StoreLocation();
				storeLocation.setStore(findStoreLightById(storeArray[idx][0]));
				storeLocation.setDistance(Long.valueOf(storeArray[idx][1]));
				
				storeLocations.add(storeLocation);
			}			
		}
		
		return storeLocations;
	}
	
	public List<StoreAdminPromotionViewObject> getLocalPromotions(Store store) {
		Collection<StoreAdminPromotion> storeAdminPromotions = storeAdminPromotionDAO.findPromotionsByStoreIdAndBrand(store.getStoreNumber(), store.getStoreType().trim());
		List<StoreAdminPromotionViewObject> filteredPromos = new ArrayList<StoreAdminPromotionViewObject>();
		if (storeAdminPromotions != null && storeAdminPromotions.size() > 0) {
			Map<Long, StoreAdminOfferTemplate> offerTemplates = new HashMap<Long, StoreAdminOfferTemplate>();
			Map<Long, StoreAdminOfferTemplateImages> offerTemplateImages = new HashMap<Long, StoreAdminOfferTemplateImages>();
			for (StoreAdminPromotion adminPromotion : storeAdminPromotions) {
				if (adminPromotion.getOffers() != null && adminPromotion.getOffers().size() > 0) {
					offerLoop: for (StoreAdminOffer offer : (Set<StoreAdminOffer>) adminPromotion.getOffers()) {
						try {
							if (offer == null)
								continue;
							StoreAdminOfferTemplate template = storeBusinessRulesDAO.findOfferTemplateByIdAndBrand(offer.getTemplateId(), adminPromotion.getBrand().trim());
							if (template == null)
								continue;
							
							if (!offerTemplates.containsKey(offer.getTemplateId()))
								offerTemplates.put(offer.getTemplateId(), template);
							
							StoreAdminOfferTemplateImages templateImages = storeBusinessRulesDAO.findOfferTemplateImagesByIdAndBrand(offer.getTemplateId(), adminPromotion.getBrand().trim());
							if (!offerTemplateImages.containsKey(offer.getTemplateId()))
								offerTemplateImages.put(offer.getTemplateId(), templateImages);
						} catch (Exception e) {
							e.printStackTrace();
							continue offerLoop;
						}
					}
				}
			}
			for (StoreAdminPromotion storeAdminPromotion : storeAdminPromotions) {
				StoreAdminPromotionViewObject storeAdminPromotionViewObject = getStoreAdminPromotionViewObject(store.getStoreNumber(), storeAdminPromotion, offerTemplates, offerTemplateImages);
				if (!filteredPromos.contains(storeAdminPromotionViewObject) && "normal".equalsIgnoreCase(storeAdminPromotionViewObject.getStorePromo().getPromotionType())) {
					filteredPromos.add(storeAdminPromotionViewObject);
				}				
			}
		}
		
		return filteredPromos.isEmpty() ? null : filteredPromos;
	}
	
	public Map<Long, List<StoreAdminPromotionViewObject>> getLocalPromotionsByStoreType(List<String> stype) {
		List<StoreAdminPromotion> storeAdminPromotions = storeBusinessRulesDAO.findNationalCurrentPromotionsByBrand(stype);
		Map<Long, List<StoreAdminPromotionViewObject>> localStorePromotions = new HashMap<Long, List<StoreAdminPromotionViewObject>>();
		if (storeAdminPromotions != null && storeAdminPromotions.size() > 0) {
			Map<Long, StoreAdminOfferTemplate> offerTemplates = new HashMap<Long, StoreAdminOfferTemplate>();
			Map<Long, StoreAdminOfferTemplateImages> offerTemplateImages = new HashMap<Long, StoreAdminOfferTemplateImages>();
			for (StoreAdminPromotion adminPromotion : storeAdminPromotions) {
				if (adminPromotion.getOffers() != null && adminPromotion.getOffers().size() > 0) {
					offerLoop: for (StoreAdminOffer offer : (Set<StoreAdminOffer>) adminPromotion.getOffers()) {
						try {
							if (offer == null)
								continue;
							StoreAdminOfferTemplate template = storeBusinessRulesDAO.findOfferTemplateByIdAndBrand(offer.getTemplateId(), adminPromotion.getBrand().trim());
							if (template == null)
								continue;
							
							if (!offerTemplates.containsKey(offer.getTemplateId()))
								offerTemplates.put(offer.getTemplateId(), template);
							
							StoreAdminOfferTemplateImages templateImages = storeBusinessRulesDAO.findOfferTemplateImagesByIdAndBrand(offer.getTemplateId(), adminPromotion.getBrand().trim());
							if (!offerTemplateImages.containsKey(offer.getTemplateId()))
								offerTemplateImages.put(offer.getTemplateId(), templateImages);
						} catch (Exception e) {
							e.printStackTrace();
							continue offerLoop;
						}
					}
				}
			}
			for (StoreAdminPromotion storeAdminPromotion : storeAdminPromotions) {
				List<Long> promoStoreNumbers = new ArrayList<Long>(storeAdminPromotionDAO.findNationalPromotionStoresByPromoIdAndStoreType(Long.valueOf(storeAdminPromotion.getPromotionId()), storeAdminPromotion.getBrand()));
				if (promoStoreNumbers != null && !promoStoreNumbers.isEmpty()) {
					for (Long storeNumber : promoStoreNumbers) {
						StoreAdminPromotionViewObject storeAdminPromotionViewObject = getStoreAdminPromotionViewObject(storeNumber, storeAdminPromotion, offerTemplates, offerTemplateImages);
						if ("normal".equalsIgnoreCase(storeAdminPromotionViewObject.getStorePromo().getPromotionType())) {
							if (localStorePromotions.containsKey(storeNumber)) {
								localStorePromotions.get(storeNumber).add(storeAdminPromotionViewObject);
							} else {
								List<StoreAdminPromotionViewObject> pvobj = null;
								pvobj = new ArrayList<StoreAdminPromotionViewObject>();
								pvobj.add(storeAdminPromotionViewObject);
								localStorePromotions.put(storeNumber, pvobj);
							}
						}
					}
				}
			}
		}
		return localStorePromotions;
	}
	
	private StoreAdminPromotionViewObject getStoreAdminPromotionViewObject(Long storeNumber, StoreAdminPromotion storeAdminPromotion, 
			Map<Long, StoreAdminOfferTemplate> offerTemplates, Map<Long, StoreAdminOfferTemplateImages> offerTemplateImages) {
		String desc = storeAdminPromotion.getDescription() == null ? "" : Encode.html(storeAdminPromotion.getDescription().replace("'","\\'").replace("%", "percent"));
		StoreAdminPromotionViewObject storeAdminPromotionViewObject = new StoreAdminPromotionViewObject(storeAdminPromotion, null);
		storeAdminPromotionViewObject.setFormattedDateRange(StringUtils.formatDateRangeShortMonth(storeAdminPromotion.getStartDate(), storeAdminPromotion.getEndDate()));
				
		if (storeAdminPromotion.getImageId() != null)
			storeAdminPromotion.setPromoBannerURL(getWebSiteAppRoot(storeAdminPromotion.getBrand().trim())+propertyAccessor.getStringProperty("specialOffersLibraryImageHandlerUrl")+storeAdminPromotion.getImageId());
			
		if (storeAdminPromotion.getOffers() != null && storeAdminPromotion.getOffers().size() > 0) {
			// Create 2 lists so that we can order the offers properly,
			// first it's based on the "priority" field, then if there
			// are duplicate priorities, those get added to the end.
			List<StoreAdminOfferViewObject> allOffers = new ArrayList<StoreAdminOfferViewObject>();
			Set<StoreAdminOfferViewObject> sortedOffers = new TreeSet<StoreAdminOfferViewObject>();

			offerLoop: for (StoreAdminOffer offer : (Set<StoreAdminOffer>) storeAdminPromotion.getOffers()) {
				try {
					if (offer == null)
						continue;
					StoreAdminOfferTemplate template = offerTemplates.get(offer.getTemplateId());
					if (template == null)
						continue;
					
					StoreAdminOfferTemplateImages tImages = offerTemplateImages.get(offer.getTemplateId());
					StoreAdminOfferTemplateImages templateImages = new StoreAdminOfferTemplateImages(tImages.getImageId(), tImages.getTemplateId(), tImages.getSiteName(), tImages.getCouponImage(), tImages.getBannerImage());
					templateImages.setCouponImageURL(getWebSiteAppRoot(storeAdminPromotion.getBrand().trim())+propertyAccessor.getStringProperty("specialOffersImageHandlerUrl")+"?i="+offer.getTemplateId()+"&oi="+offer.getOfferId()+"&sn="+storeNumber+"&src="+storeAdminPromotion.getBrand().trim());

					StoreAdminOfferViewObject storeAdminOfferViewObject = new StoreAdminOfferViewObject(offer, template, templateImages);					
					if (!StringUtils.isNullOrEmpty(storeAdminOfferViewObject.getTemplateNameEncoded())) {
						storeAdminOfferViewObject.setTemplateNameEncoded(Encode.html(storeAdminOfferViewObject.getTemplateNameEncoded()));
					}
					allOffers.add(storeAdminOfferViewObject);
					sortedOffers.add(storeAdminOfferViewObject);

					setDaysDisplayForPromo(storeAdminPromotionViewObject, offer);
					setEndDateForPromo(storeAdminPromotion, offer);									
				} catch (Exception e) {
					e.printStackTrace();
					continue offerLoop;
				}
			}

			// add sorted offers first to the list of offers, then add
			// the rest of the offers not in the sorted list
			storeAdminPromotionViewObject.getSortedOfferViewObjects().addAll(sortedOffers);
			allOffers.removeAll(sortedOffers);
			storeAdminPromotionViewObject.getSortedOfferViewObjects().addAll(allOffers);
		}
		
		return storeAdminPromotionViewObject;
	}
	
	public List<StoreAdminAnnouncement> getAnnouncements(Store store) {
		Collection<StoreAdminAnnouncement> c_announcements = storeAdminAnnouncementDAO.findAnnouncementsByStoreIdAndBrand(store.getStoreNumber(), store.getStoreType().trim());
		List<StoreAdminAnnouncement> announcements = null;
		if (c_announcements != null && !c_announcements.isEmpty()) {
			announcements = new ArrayList<StoreAdminAnnouncement>();
			for (StoreAdminAnnouncement storeAdminAnnouncement : c_announcements) {
				storeAdminAnnouncement.setImageSrcUrl(getWebSiteAppRoot(storeAdminAnnouncement.getBrand().trim())+propertyAccessor.getStringProperty("specialOffersLibraryImageHandlerUrl")+storeAdminAnnouncement.getImageId());
				if (storeAdminAnnouncement.getStoreAdminLibraryImage() != null && storeAdminAnnouncement.getStoreAdminLibraryImage().getUrl() != null) {
					storeAdminAnnouncement.setImageActionUrl(storeAdminAnnouncement.getStoreAdminLibraryImage().getUrl());
				}
				
				announcements.add(storeAdminAnnouncement);
			}
		}
		
		return announcements;
	}
	
	public Map<Long, List<StoreAdminAnnouncement>> getAnnouncementsByStoreType(List<String> stype) {
		Collection<StoreAdminAnnouncement> c_announcements = storeBusinessRulesDAO.findNationalCurrentApprovedAnnouncements(stype);
		Map<Long, List<StoreAdminAnnouncement>> announcements = new HashMap<Long, List<StoreAdminAnnouncement>>();
		if (c_announcements != null && !c_announcements.isEmpty()) {
			for (StoreAdminAnnouncement storeAdminAnnouncement : c_announcements) {
				storeAdminAnnouncement.setImageSrcUrl(getWebSiteAppRoot(storeAdminAnnouncement.getBrand().trim())+propertyAccessor.getStringProperty("specialOffersLibraryImageHandlerUrl")+storeAdminAnnouncement.getImageId());
				if (storeAdminAnnouncement.getStoreAdminLibraryImage() != null && storeAdminAnnouncement.getStoreAdminLibraryImage().getUrl() != null) {
					storeAdminAnnouncement.setImageActionUrl(storeAdminAnnouncement.getStoreAdminLibraryImage().getUrl());
				}
				List<Long> storeNumbers = new ArrayList<Long>(storeAdminAnnouncementDAO.findNationalAnnouncementStoresByAnnouncementIdAndStoreType(Long.valueOf(storeAdminAnnouncement.getAnnouncementId()), storeAdminAnnouncement.getBrand().trim()));
				if (storeNumbers != null && !storeNumbers.isEmpty()) {
					for (Long storeNumber : storeNumbers) {
						if (announcements.containsKey(storeNumber)) {
							announcements.get(storeNumber).add(storeAdminAnnouncement);
						} else {
							List<StoreAdminAnnouncement> an = null;
							an = new ArrayList<StoreAdminAnnouncement>();
							an.add(storeAdminAnnouncement);
							announcements.put(storeNumber, an);
						}
					}
				}
			}
		}
		return announcements;		
	}
	
	private List<String> getValidStoreTypes(String storeType) {
		List<String> types = new ArrayList<String>();
		String[] stypes = storeType.split("-");
		Collection<String> storeTypes = storeDAO.findStoreTypes();
		
		if (storeTypes != null && !storeTypes.isEmpty()) {
			Collection<String> tmpStoreTypes = new ArrayList<String>();
			for (String type: storeTypes) {
				tmpStoreTypes.add(type.trim());
			}
			storeTypes = null;
			storeTypes = tmpStoreTypes;
		}
		
		for (int i = 0; i < stypes.length; i++) {
			if (storeTypes.contains(stypes[i].trim())) {
				types.add(stypes[i].trim());
			}
		}
		
		return types;
	}
	
	private void setStoreHierarchy(Store store) {
		if (store != null) {
			StoreHierarchy sh = storeBusinessRulesDAO.getHierarchyByStoreNumber(store.getStoreNumber());
			if (sh != null) {
				store.setAreaId(sh.getDistrictId());
				store.setAreaName(sh.getDistrictName());
				store.setDistrictId(sh.getDistrictId());
				store.setDistrictName(sh.getDistrictName());
				store.setRegionId(sh.getRegionId());
				store.setRegionName(sh.getRegionName());
				store.setDivisionId(sh.getDivisionId());
				store.setDivisionName(sh.getDivisionName());
			}
		}
	}

	public boolean isMilitaryStore(Long storeNumber) {
		boolean isMilitaryStore = false;

		if (storeNumber == null) {
			return isMilitaryStore;
		}

		Collection<StoreMilitary> militaryStore = storeMilitaryDAO.findMilitaryStore(storeNumber);
		if (militaryStore == null || militaryStore.size() == 0) {
			isMilitaryStore = false;
		} else {
			Iterator<StoreMilitary> itr = militaryStore.iterator();
			StoreMilitary ms = itr.next();
			if (ms.getStoreNumber() == storeNumber.longValue())
				isMilitaryStore = true;
		}	
		return isMilitaryStore;
	}
	
	public Map<Long, List<String>> getStoreHolidayHoursByStoretype(List<Store> stores, List<String> storetype, List<StoreHoliday> holidayList) {
		Map<Long, List<String>> storeHolidayHourMessageMap = new HashMap<Long, List<String>>();
		Map<Long, List<StoreHolidayHour>> storeHolidayHourMap = new HashMap<Long, List<StoreHolidayHour>>();
		List<Long> holidayIds = null;
		
		if (holidayList != null && holidayList.size() > 0) {
			holidayIds = new ArrayList<Long>();
			for (StoreHoliday holiday : holidayList) {
				holidayIds.add(holiday.getHolidayId());
			}
		}
		
		if (holidayIds != null && !holidayIds.isEmpty()) {
			List<StoreHolidayHour> shhs = storeBusinessRulesDAO.findStoreHolidayHoursByStoreType(storetype, holidayIds);
			if (shhs != null && !shhs.isEmpty()) {
				for (StoreHolidayHour shh : shhs) {
					if (storeHolidayHourMap.containsKey(shh.getStoreHolidayHourId().getStoreNumber())) {
						storeHolidayHourMap.get(shh.getStoreHolidayHourId().getStoreNumber()).add(shh);
					} else {
						List<StoreHolidayHour> shr = null;
						shr = new ArrayList<StoreHolidayHour>();
						shr.add(shh);
						storeHolidayHourMap.put(shh.getStoreHolidayHourId().getStoreNumber(), shr);
					}
				}
			}
		}
		
		for (Store store : stores) {
			List<String> holidayHours = getHolidayHoursMessages(store, holidayList, storeHolidayHourMap);
			storeHolidayHourMessageMap.put(store.getStoreNumber(), holidayHours);
		}
		
		return storeHolidayHourMessageMap;
	}
	
	public Map<Long, List<String>> getStoreHolidayHours(List<Store> stores) {
		Map<Long, List<String>> storeHolidayHourMap = new HashMap<Long, List<String>>();

		List<StoreHoliday> holidayList = getStoreLocatorHolidays();
		if (stores != null) {
			for (Store s : stores) {
				List<String> holidayHours = getHolidayHoursMessages(s, holidayList);
				storeHolidayHourMap.put(s.getStoreNumber(), holidayHours);
			}
		}

		return storeHolidayHourMap;
	}

	public List<String> getStoreHolidayHours(Store store) {
		List<Store> stores = new ArrayList<Store>();
		stores.add(store);

		Map<Long, List<String>> storeHolidayHourMap = getStoreHolidayHours(stores);

		List<String> holidayHourMessages = storeHolidayHourMap.get(store.getStoreNumber());

		return (holidayHourMessages != null && !holidayHourMessages.isEmpty()) ? holidayHourMessages : null;
	}
	
	public List<StoreHoliday> getStoreLocatorHolidays() {
		// get holidays for next 30 days.
		Date startDate = new Date();
		Date endDate = org.apache.commons.lang.time.DateUtils.addDays(startDate, DAYS_PRIOR_TO_DISPLAY_HOLIDAY);
		// need to ensure these are in order by date
		List<StoreHoliday> holidays = getOrderedStoreHolidaysBetweenDates(startDate, endDate);

		// only show 2 per reqs.
		List<StoreHoliday> finalHolidays = new ArrayList<StoreHoliday>();
		try {
			if (holidays != null) {
				for (int i = 0; (i < MAX_HOLIDAYS_TO_DISPLAY && i < holidays.size()); i++) {
					finalHolidays.add(holidays.get(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return finalHolidays;
	}
	
	public List<StoreHoliday> getOrderedStoreHolidaysBetweenDates(Date start,Date end) {
		//TODO figure out how to fetch these by month, day, year strings
		if(start == null || end==null)
			return null;
		String startYear,endYear;
		SimpleDateFormat yearSDF = new SimpleDateFormat("yyyy");
		startYear = yearSDF.format(start);
		endYear = yearSDF.format(end);

		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		GregorianCalendar gcStart = new GregorianCalendar();
		gcStart.setTime(start);
		gcStart.add(Calendar.DATE, -1);
		
		GregorianCalendar gcEnd = new GregorianCalendar();
		gcEnd.setTime(end);
		//12AM end day
		gcEnd.set(Calendar.HOUR_OF_DAY, 0);
		gcEnd.set(Calendar.MINUTE, 0);
		gcEnd.set(Calendar.SECOND, 0);
		gcEnd.set(Calendar.MILLISECOND, 0);
		gcEnd.set(Calendar.AM_PM, Calendar.AM);
		
		Collection<StoreHoliday> l = storeHolidayDAO.findStoreHolidaysBetweenStartAndEndDates(startYear, endYear);
		try{
			//doing this to get to all zeros.
			Date gcEndDate = dateFormat.parse( (gcEnd.get(Calendar.MONTH) + 1) +"/"+(gcEnd.get(Calendar.DAY_OF_MONTH))+"/"+gcEnd.get(Calendar.YEAR));
			List<StoreHoliday> storeHolidaysToReturn = new ArrayList<StoreHoliday>();
			if(l!=null){
				for(StoreHoliday sh:l){
					Date middleDate = dateFormat.parse(sh.getId().getMonth() +"/"+sh.getId().getDay()+"/"+sh.getId().getYear());
					if(middleDate.after(gcStart.getTime()) && middleDate.before(gcEndDate)){
						storeHolidaysToReturn.add(sh);
					}
				}		
			}
		
			return storeHolidaysToReturn;		
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public StoreHolidayHour getStoreHolidayHour(Long storeNumber, Long holidayId) {
		Collection<StoreHolidayHour> storeHolidayHours = storeHolidayHourDAO.findStoreHolidayHour(storeNumber, holidayId);
		if (storeHolidayHours == null || storeHolidayHours.size() == 0) {
			return null;
		}
		
		Iterator<StoreHolidayHour> itr = storeHolidayHours.iterator();
		StoreHolidayHour storeHolidayHour = itr.next();
		return storeHolidayHour;
	}
	
	public List<String> getHolidayHoursMessages(Store store, List<StoreHoliday> holidayList) {
		List<String> holidayHours = new ArrayList<String>();
		List<StoreHolidayHour> storeHolidayHours = new ArrayList<StoreHolidayHour>();
		if (holidayList != null && holidayList.size() > 0) {
			for (StoreHoliday holiday : holidayList) {
				StoreHolidayHour holidayHour = getStoreHolidayHour(store.getStoreNumber(), holiday.getHolidayId());
				if (holidayHour != null) { 
					storeHolidayHours.add(holidayHour);
				}
				holidayHours.add(getStoreHolidayMessage(holiday, holidayHour));
			}
		}
		
		if (holidayList != null && !holidayList.isEmpty())
			store.setHolidays(holidayList);
		
		if (storeHolidayHours != null && !storeHolidayHours.isEmpty())
			store.setHolidayHours(storeHolidayHours);
		
		return holidayHours;
	}
	
	public List<String> getHolidayHoursMessages(Store store, List<StoreHoliday> holidayList, Map<Long, List<StoreHolidayHour>> storeHolidayHourMap) {
		List<String> holidayHours = new ArrayList<String>();
		List<StoreHolidayHour> storeHolidayHours = null;
		if (storeHolidayHourMap != null && !storeHolidayHourMap.isEmpty()) {
			storeHolidayHours = storeHolidayHourMap.get(store.getStoreNumber());
			if (storeHolidayHours != null && !storeHolidayHours.isEmpty()) {
				for (StoreHoliday sh : holidayList) {
					StoreHolidayHour shh = null;
					for (StoreHolidayHour tmpshh : storeHolidayHours) {
						if (tmpshh.getStoreHolidayHourId().getHolidayId().longValue() == sh.getHolidayId().longValue()) {
							shh = tmpshh;
						}					
					}
					holidayHours.add(getStoreHolidayMessage(sh, shh));
				}
			}
		}
		
		store.setHolidays(holidayList);
		store.setHolidayHours(storeHolidayHours);
		return holidayHours;
	}
	
	public String getStoreHolidayMessage(StoreHoliday holiday, StoreHolidayHour hours) {
		StringBuffer message = new StringBuffer();
		// means we have no hours, the store is closed.
		if (hours == null) {
			message.append(holiday.getDescription()+": ");
			message.append("Closed: ");
			message.append(getMonthName(holiday.getId()));
			message.append(". ");
			message.append(holiday.getId().getDay());
			message.append(", ");
			message.append(holiday.getId().getYear());
		} else {
			// store is open.
			message.append(holiday.getDescription()+": ");
			message.append(getMonthName(holiday.getId()));
			message.append(". ");
			message.append(holiday.getId().getDay());
			message.append(", ");
			message.append(holiday.getId().getYear());
			// message.append(" Hours: ");
			message.append(" ");
			message.append(convertToStandardTime(hours.getOpenTime()));
			message.append(" - ");
			message.append(convertToStandardTime(hours.getCloseTime()));
		}
		return message.toString();
	}
	
	private String convertToStandardTime(String time) {
		// get colon index
		int indexOfColon = time.indexOf(":");
		// get Hours
		int beginTimeHour = Integer.parseInt(time.substring(0, indexOfColon).trim());
		// get Minutes
		int beginTimeMinute = Integer.parseInt(time.substring(indexOfColon + 1).trim());

		Calendar startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.HOUR_OF_DAY, beginTimeHour);
		startCalendar.set(Calendar.MINUTE, beginTimeMinute);

		String formattedTime = new SimpleDateFormat("h:mma").format(startCalendar.getTime()).toLowerCase();

		return formattedTime;
	}

	private String getMonthName(StoreHolidayId holidayId) {
		LocalDate date = new LocalDate(holidayId.getYear(), holidayId.getMonth(), holidayId.getDay());
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MMM");
        String monthName = formatter.print(date);

		return monthName;
	}
	
	public Set<StoreHour> getSortedStoreHours(Set<StoreHour> storeHours) {
		Set<StoreHour> sortedHours = null;
		if (storeHours != null && !storeHours.isEmpty()) {
			List<StoreHour> listHours = new ArrayList<StoreHour>(storeHours);
			Collections.sort(listHours, new WeekDayComparator());
			sortedHours = new LinkedHashSet<StoreHour>(listHours);
		}
		return sortedHours;
	}
	
	public StoreHour getStoreHour(Collection<StoreHour> l, String day) {	
		StoreHour hour = null;		
		for (StoreHour temp: l) {
			if (temp != null && temp.getId().getWeekDay().equals(day)) {
				hour = temp;
				break;
			}				
		}
		return hour;
	}
	
	public String getStoreHourHTML(Collection<StoreHour> storeHours) {
		return getStoreHourHTML(storeHours, false);
	}
	
	public String getStoreHourHTML(Collection<StoreHour> storeHours, boolean isFormatted) {
		String out = "";
		if (storeHours == null || storeHours.isEmpty())
			return out;
		for (int i=0; i<7; i++) {
			StoreHour curr = getStoreHour(storeHours, DAY_ABBREV[i]);
			if (curr == null)
				continue;
			for (int j=i+1; j<=7; j++) {
				StoreHour test = null;
				if (j<7)
					test = getStoreHour(storeHours, DAY_ABBREV[j]);
				if (test != null && test.equals(curr))
                    continue;
				if (isFormatted)
					out = out + getHourFormattedHTML(i, j - 1, curr);
				else
					out = out + getHourHTML(i, j - 1, curr);
				i = j - 1;
				break;
			}		
		}
		return out;		
	}
	
	public String getHourHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + " " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public String getHourFormattedHTML(int begin, int end, StoreHour hour) {
		if(hour == null || hour.getOpenTime() == null || hour.getCloseTime() == null)
            return "";
        String out = "<b>" + DAY_ABBREV[begin];
        if(end > begin)
        {
            out = out + "&#45;" + DAY_ABBREV[end];            
        }
        return out + ":</b> " + format(hour.getOpenTime()) + "&#45;" + format(hour.getCloseTime()) + "<br />";
	}
	
	public static String format(String time) {
        String temp = pad(time);
        int hour = Integer.valueOf(temp.substring(0, 2)).intValue();
        String m = "am";
        if(hour > 11)
        {
            m = "pm";
            if(hour > 12)
                hour -= 12;
        }
        String out = Integer.toString(hour) + ":" + temp.substring(3, 5);
        return out + m;
    }
	
	public static String pad(String param) {
        if(param == null)
            return null;
        String temp = param.trim();
        if(temp.length() < 5)
            return "0" + temp;
        return temp;
    }
	
	public static final String DAY_ABBREV[] = {
        "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"
    };
	
	private void setDaysDisplayForPromo(StoreAdminPromotionViewObject storeAdminPromotionData, StoreAdminOffer offer) {
		String daysDisplay = "";
		String days = offer.getValidWeekDays();
		if (days != null && !"1234567".equals(days)) {
			daysDisplay = days.replace("2", "Mon/").replace("3", "Tue/").replace("4", "Wed/").replace("5", "Thu/").replace("6", "Fri/").replace("7", "Sat/").replace("1", "Sun/");
			daysDisplay = daysDisplay.substring(0, daysDisplay.length() - 1) + " Only";
		}
		storeAdminPromotionData.setDaysDisplay(daysDisplay);
	}
	
	private void setEndDateForPromo(StoreAdminPromotion storeAdminPromotion, StoreAdminOffer offer) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// -- check rolling date --//
		if (offer.getRollingDatePeriod() != null && offer.getRollingDatePeriod().intValue() > 0) {
			Date nextRollDate = getOfferEndDate(sdf, storeAdminPromotion, offer);
			String rollDate = sdf.format(nextRollDate);
			String endDate = sdf.format(storeAdminPromotion.getEndDate());
			// -- if end date is ealier than the next
			// rolled date then use the end date --//
			if (endDate.compareTo(rollDate) > 0)
				storeAdminPromotion.setEndDate(nextRollDate);
		}
	}
	
	private Date getOfferEndDate(java.text.SimpleDateFormat sdf, StoreAdminPromotion storePromo, StoreAdminOffer offer) {
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		String strNow = sdf.format(now);
		Date startDate = storePromo.getStartDate();
		String strStartDate = sdf.format(startDate);
		// if start date is NOT furture date then
		// use today
		if (strStartDate.compareTo(strNow) <= 0) {
			storePromo.setStartDate(new Date());
		} else {
			// -- set the roller date to be furture
			// date plus rolling period --//
			calendar.setTime(startDate);
		}
		calendar.add(Calendar.DAY_OF_MONTH, offer.getRollingDatePeriod().intValue());
		Date nextRollDate = calendar.getTime();
		return nextRollDate;
	}
	
	public String getWebSiteAppRoot(String storeType) {
		String approot = null;
		storeType = (storeType != null) ? storeType.trim() : storeType;
		if (!StringUtils.isNullOrEmpty(storeType)) {
			approot = "http://";
			
			if (ServerUtil.isProduction()) {
				approot += "www.";
			} else if (ServerUtil.isBSROQa()) {
				approot += "qa01.";
			} else {
				approot += "dev01.";
			}
			
			if ("FCAC".equalsIgnoreCase(storeType)) {
				approot += "firestonecompleteautocare.com";
			} else if ("TP".equalsIgnoreCase(storeType)) {
				approot += "tiresplus.com";
			} else if ("HTP".equalsIgnoreCase(storeType)) {
				approot += "hibdontire.com";
			} else if ("WW".equalsIgnoreCase(storeType)) {
				approot += "wheelworks.net";
			}
		}
		return approot;
	}

	public String getAbbreviateStateName(String state) {
		String myState = null;
		if (!StringUtils.isNullOrEmpty(state)) {
			state = state.trim().toUpperCase();
			if (state.length() == 2 && statesMap.containsKey(state)) {
				myState = state;
			} else if (state.length() > 2) {
				for (String stateAbbr : statesMap.keySet()) {
					if (state.equalsIgnoreCase(statesMap.get(stateAbbr))) {
						myState = stateAbbr;
						break;
					}
				}
			}
		}
		return myState;
	}

	public List<StoreFlags> findStoreFlagsByType(String storeType) {		
		if (StringUtils.isNullOrEmpty(storeType)) {
			return null; 
		}		
		storeType = storeType.toUpperCase();				
		List<StoreFlags> storeFlagsList = storeBusinessRulesDAO.findStoreFlagsByType(storeType);		
		return storeFlagsList;
	}

	public StoreFlags findStoreFlagsByNumber(String storeNumber) {
		StoreFlags storeFlags = storeBusinessRulesDAO.findStoreFlagsByNumber(storeNumber);
		return storeFlags;
	}
	
}
