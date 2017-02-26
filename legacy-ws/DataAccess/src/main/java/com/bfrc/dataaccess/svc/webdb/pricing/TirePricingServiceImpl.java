package com.bfrc.dataaccess.svc.webdb.pricing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.tire.MindshareTiresurveyDetails;
import app.bsro.model.tire.SeoVehicleData;
import app.bsro.model.tire.TireItems;
import app.bsro.model.tire.TirePromotion;
import app.bsro.model.tire.TireSearchResults;
import app.bsro.model.tire.TireSize;
import app.bsro.model.tire.VehicleFitment;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.model.promotion.TirePromotionEvent;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.svc.webdb.PromotionsService;
import com.bfrc.dataaccess.svc.webdb.TirePricingService;
import com.bfrc.dataaccess.util.AddressUtils;
import com.bfrc.dataaccess.util.IPUtils;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.SurveyDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.service.tire.TireSizeService;

/**
 * @author stallin sevugamoorthy
 *
 */

@Service
public class TirePricingServiceImpl implements TirePricingService {
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	private VehicleDAO vehicleDAO;
	
	@Autowired
	private PricingDAO pricingDAO;
	
	@Autowired
	SurveyDAO surveyDAO;
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	private TireSizeService tireSizeService;
	
	@Autowired
	private PromotionsService promotionsService;
		
	@Autowired
	private LocatorOperator locator;
	
	@Autowired
	private PricingLocatorOperator pricingLocator;
	
	@Autowired
	TiresUtil tiresUtil;
	
	@Autowired
	private JDA2CatalogDAO jda2CatalogDAO;
	
	private static DecimalFormat decimaValueFormatter = new DecimalFormat("##.00");
	
	private static String TIRESPLUS_LICENSEE_STORE = "TPL";

	@SuppressWarnings({ "unchecked" })
	public BSROWebServiceResponse getTirePricingResults(String siteName, Long storeNumber, Long acesVehicleId, 
			Integer tpms, String cross, String aspect, String rim) {
		
		List<Tire> tires = null;
		List<Tire> pricedTires = null;
		List<Fitment> fitments = null;
		Store store = null;
		TireVehicle tireVehicle = null;
		Long regularStoreNumber = null;
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		String zip = null;
				
		/*
		 * 		Set the 'siteName' in Config for later use by services & dao's in BSRO Framework 
		 */
		if (!StringUtils.isNullOrEmpty(siteName)) {
			locator.getConfig().setSiteName(siteName);
			pricingLocator.getConfig().setSiteName(siteName);
		}
		
		/*
		 * 		Load the store for the input store number.
		 */
		store = storeDAO.findStoreById(storeNumber);
		if (store == null) {
			response.setMessage("Invalid store number.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
		
		if (TIRESPLUS_LICENSEE_STORE.equalsIgnoreCase(store.getStoreType().trim())) {
			zip = store.getZip().substring(0, 5);
			if(StringUtils.isNullOrEmpty(zip) || !AddressUtils.isValidZipCode(zip)) {
				response.setMessage("Invalid zip code: "+zip);
				response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
				return response;
			}
		}
		
		
		regularStoreNumber = getRegularStoreNumber(siteName, store.getStoreType(), zip);
		/*
		 * 		Get the tire results.
		 */
		if (!StringUtils.isNullOrEmpty(acesVehicleId)) {
			tireVehicle = tireVehicleService.fetchVehicleByAcesVehicleId(acesVehicleId);
			fitments = (List<Fitment>) vehicleDAO.getFitments(acesVehicleId);
			tires = vehicleDAO.searchTiresByVehicle(siteName, storeNumber, acesVehicleId);
		} else {
			tires = vehicleDAO.searchTiresBySize(siteName, storeNumber, cross, aspect, rim);
		}
		
		if (tires == null || tires.isEmpty()) {
			response.setMessage("No tires found for given acesVehicleId.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			return response;
		}
		
		/*
		 * 		Apply suppression logic to remove suppressed tires.
		 */
		removeSuppressedTires(tires,storeNumber);
		removeDriveGuardTiresFromNonTPMSVehicles(tireVehicle, tires, tpms);
		
		/*
		 * 		Populate tire pricing.
		 */
		pricedTires = populateTirePricing(siteName, storeNumber, regularStoreNumber, tires);
		return createTireSearchResultsResponse(siteName, store, tireVehicle, fitments, pricedTires);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Long getRegularStoreNumber(String siteName, String storeType, String zip) {
		Store regularStore = null;
		if("TP".equalsIgnoreCase(siteName)){
			java.util.Map secondaryMap = storeDAO.getSecondaryStoreMap(siteName);
			Iterator i = secondaryMap.keySet().iterator();
			while(i.hasNext()) {
				String secondaryType = (String)i.next();
				if(secondaryType.equals(storeType.trim())) {
					HashMap m = new HashMap();
					try {
						m.put("zip", zip);
						m.put("licensee", new Boolean(false));
						m.put("pricing", "y");
						pricingLocator.operate(m);
						Store[] regularStores = (Store[])m.get("result");
						regularStore = regularStores[0];
					} catch(Exception ex) {
						try {
							m.put("full", new Boolean(true));
							pricingLocator.operate(m);
							Store[] regularStores = (Store[])m.get("result");
							regularStore = regularStores[0];							
						} catch(Exception fex) {
							return null;
						}
					}
				}
			}
		}
		return (regularStore != null) ? regularStore.getStoreNumber() : null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void removeSuppressedTires(List<Tire> tires, Long storeNumber) {
		if(storeNumber == null)
			return;
		List globals = pricingDAO.getGlobalSppressedTireArticles();
		Map storeNumberTires = pricingDAO.getStoreNumberMappedSppressedTireArticles();
		Map<Long, List<String>> skuVehType = pricingDAO.getGlobalVehTypeDisplayIDSuppressedTireArticles();
		Long sn = Long.valueOf(storeNumber.toString());
		List snTires = (List)storeNumberTires.get(sn);
		if(snTires != null && snTires.size() > 0){
			globals.addAll(snTires);
		}
		if(tires == null)
			return;
		List suppressedTires = new ArrayList();
		for(int i=0; tires != null && i<tires.size(); i++){
			Tire tire = (Tire)tires.get(i);
			if(globals.contains(Long.valueOf(tire.getArticle()))){
				suppressedTires.add(tire);
			} else {
				List vehTypes = skuVehType.get(Long.valueOf(tire.getArticle()));
				if (vehTypes != null && vehTypes.size() > 0) {
					if (vehTypes.contains(tire.getVehtype())) {
						suppressedTires.add(tire);
					}
				}
			}
		}
		for(int i=0; i<suppressedTires.size(); i++){
			Tire tire = (Tire)suppressedTires.get(i);
			tires.remove(tire);
		}
	}
	
	private void removeDriveGuardTiresFromNonTPMSVehicles(TireVehicle tireVehicle, List<Tire> tires, Integer tpms) {
		if (tireVehicle != null) {
			/*
			 * 	Only vehicles WITHOUT TPMS by default will have the capability to switch to TPMS 'yes' or 'no'.
			 *  Vehicle with TPMS should not be switched to NO TPMS.
			 *  - understanding from current FCAC design -   
			 * */
			boolean hasTPMS = ((tireVehicle.getHasTpms() != null && tireVehicle.getHasTpms().intValue() == 1) || (tpms != null && tpms.intValue() == 1));
			if (!hasTPMS) {
				for (Iterator<Tire> itr = tires.iterator(); itr.hasNext(); ) {
					Tire tire = itr.next();
					if ("DRIVEGUARD".equalsIgnoreCase(tire.getTireName()))
						itr.remove();
				}
			}
		}
	}
	
	private List<Tire> populateTirePricing(String siteName, Long storeNumber, Long regularStoreNumber,
			List<Tire> tires) {
		List<Tire> pricedtires = null;
		String articles = getArticles(tires);
		if (articles != null && !articles.isEmpty()) {
			String regStoreNumber = (regularStoreNumber != null) ? regularStoreNumber.toString() : null;
			pricedtires = vehicleDAO.populateTiresPricing(siteName, articles, storeNumber.toString(), regStoreNumber, tires);
		}
		
		if (pricedtires != null && !pricedtires.isEmpty())
			return pricedtires;
		else
			return tires;
	}

	private String getArticles(List<Tire> tires) {
		StringBuffer sb = new StringBuffer();
		for(Tire tire: tires){
			sb.append(String.valueOf(tire.getArticle())+"|");
		}
		return sb.toString();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BSROWebServiceResponse createTireSearchResultsResponse(String siteName, Store store, TireVehicle tireVehicle, 
			List<Fitment> fitments, List<Tire> tires) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		VehicleFitment vehicleFitment = null;
		TireSize tireSize = null;
		app.bsro.model.tire.Tire tire = null;
		StoreInventory storeInventory = null;
		MindshareTiresurveyDetails tiresurveyDetail = null;
		FriendlyVehicleDataBean friendlyVehicleDataBean = null;
		SeoVehicleData seoVehicleData = null;
		boolean isMatchedSetTires = false;
		boolean isBestInClassTire = false;
		boolean hasTireDiscountSpecialOffer = false;
		boolean hasTireRebateSpecialOffer = false;
		boolean hasStandardSizeTires = false;
		boolean hasOptionalSizeTires = false;
		int quantity = 4;
		String articleParam = null;
		
		List<Tire> matchedSetsTires = null;
		List<Tire> frontTires = null;
		List<Tire> rearTires = null;
		
		List<String> allArticles = new ArrayList<String>();
		List<String> standardArticles = new ArrayList<String>();
		List<String> optionalArticles = new ArrayList<String>();
		List<String> frontArticles = new ArrayList<String>();
		List<String> rearArticles = new ArrayList<String>();
		Map<String, List<String>> matchedSetArticles = new HashMap<String, List<String>>();
		Map<Long,TireItems> tireItems = new HashMap<Long,TireItems>();
		Map mappedTireGrouping = new HashMap();
		
		List<String> brandFilter = new ArrayList<String>();
		List<String> priceFilter = new ArrayList<String>();
		List<String> tireTypeFilter = new ArrayList<String>();
		List<String> tireRatingFilter = new ArrayList<String>();
		List<String> sizeFilter = new ArrayList<String>();
		List<String> stdoptFilter = new ArrayList<String>();
		List<String> matchedsetFilter = new ArrayList<String>();
		List<String> mileageFilter = new ArrayList<String>();
		Map<String, List<String>> filters = new HashMap<String, List<String>>();
		TireSearchResults results = new TireSearchResults();
		
		Map offersData = promotionsService.loadTirePromotionData(siteName, Long.parseLong(store.getStoreNumber().toString()), null);
		Map mappedBestInClassTires = tiresUtil.getMappedBestInClassTires();
		Map mappedSurveyData = surveyDAO.getMappedMindshareTiresurveyDetails();
		hasOptionalSizeTires = tiresUtil.hasOptionalSizeTires(tires);
		hasStandardSizeTires = tiresUtil.hasStandardSizeTires(tires);
		isMatchedSetTires = tiresUtil.isMatchedSet(tires);
		if (isMatchedSetTires) {
			quantity = 2;
			matchedSetsTires = tiresUtil.getMatchedSetTires(tires, fitments, getArticles(tires));
			frontTires = tiresUtil.getFrontTires(tires);
			rearTires = tiresUtil.getRearTires(tires);
				
			tires = new ArrayList();
			tires.addAll(matchedSetsTires);
			tires.addAll(frontTires);
			tires.addAll(rearTires);			
		}
		
		/*
		 * 		Add vehicle details to response.
		 * 		If tireVehicle is not null we are searching by vehicle and ensure populating std/opt & front/rear/matchedset articles
		 */
		if (tireVehicle != null) {
			vehicleFitment = new VehicleFitment();
			vehicleFitment.setYear(tireVehicle.getYear());
			vehicleFitment.setMake(tireVehicle.getMakeName());
			vehicleFitment.setModel(tireVehicle.getModelName());
			vehicleFitment.setSubmodel(tireVehicle.getSubmodelName());
			vehicleFitment.setAcesVehicleId(tireVehicle.getAcesVehicleId());
			vehicleFitment.setTpmsInd(tireVehicle.getHasTpms() != null && tireVehicle.getHasTpms().intValue() == 1);
			
			if (matchedSetsTires != null && !matchedSetsTires.isEmpty()) {
				for (Tire t : matchedSetsTires) {
					if (t != null) {
						List<String> tmpList = matchedSetArticles.get(String.valueOf(t.getArticle()));
						if (tmpList == null) {
							tmpList = new ArrayList<String>();
						}
						if (!tmpList.contains(String.valueOf(t.getRearArticle()))) {
							tmpList.add(String.valueOf(t.getRearArticle()));
						}
						matchedSetArticles.put(String.valueOf(t.getArticle()), tmpList);
					}
				}
			}
			friendlyVehicleDataBean = tireVehicleService.getFriendlyVehicleDataBean(tireVehicle.getAcesVehicleId(), siteName);
			seoVehicleData = getSeoVehicleData(friendlyVehicleDataBean);
		}
		  List groupings = jda2CatalogDAO.getAllTireGrouping();
		  for(int i=0; i< groupings.size();i++){
	         TireGrouping tireGrouping = (TireGrouping)groupings.get(i);
	         mappedTireGrouping.put(tireGrouping.getDisplayId(), tireGrouping);
	       }
		for (Tire t : tires) {
			if (t != null) {
				TireItems tireItem = new TireItems();
				Object loadIndexPounds = TireSearchUtils.getMappedLoadIndexAndLoadIndexValue().get(""+t.getLoadIndex());
				Object speedRatingMPH = TireSearchUtils.getMappedSpeedRatingAndSpeedValue().get(t.getSpeedRating());
				if (tireVehicle == null) {
					tireSize = new TireSize();
					tireSize.setCrossSection(t.getCrossSection());
					tireSize.setAspectRation(t.getAspect());
					tireSize.setRimSize(t.getRimSize());
					seoVehicleData = getSeoVehicleData(tireSizeService.getSeoVehicleData(siteName, "SIZE_TIRE", "/size/"+t.getCrossSection()+"-"+t.getAspect()+"-r"+t.getRimSize()+"/"));
				}
			
				/*
				 * 		Add tire details to response
				 */
				tire = new app.bsro.model.tire.Tire();
				articleParam = String.valueOf(t.getArticle());
				isBestInClassTire = TireCatalogUtils.isBestInClassTire(mappedBestInClassTires, t.getDisplayId());
				tire.setArticle(Long.valueOf(t.getArticle()));
				if (t.getRearArticle() > 0l) {
					tire.setRearArticle(Long.valueOf(t.getRearArticle()));
				}
				tire.setBrand(t.getBrand());
				tire.setTireName(t.getTireName());
				tire.setTireGroupName(t.getTireGroupName());
				tire.setTireClassName(t.getTireClassName());
				tire.setTireSegmentName(t.getTireSegmentName());
				tire.setTireSize(t.getTireSize());
				TireGrouping tireGrouping = (TireGrouping)mappedTireGrouping.get(t.getDisplayId());
				tire.setTireType(tireGrouping == null ? t.getTireGroupName() : tireGrouping.getTireVehicle());
				tire.setLoadIndex(Long.valueOf(t.getLoadIndex()));
				if (loadIndexPounds != null)
					tire.setLoadIndexPounds(Long.valueOf(loadIndexPounds.toString()));
				tire.setVehType(t.getVehtype());
				tire.setSpeedRating(t.getSpeedRating());
				if (speedRatingMPH != null)
					tire.setSpeedRatingMPH(speedRatingMPH.toString());
				tire.setSidewallDescription(t.getSidewallDescription());
				try {
					tire.setMileage(Long.valueOf(t.getMileage()));
				} catch (Exception e) {					
				}			
				tire.setTechnology(t.getTechnology());
				tire.setWarrantyName(t.getWarrantyName());
				tire.setDescription(t.getDescription());
				tire.setRetailPrice(Double.valueOf(t.getRetailPrice()));
				tire.setBestInClass(new Boolean(isBestInClassTire));
				tire.setStandardOptional(t.getStandardOptional());
				tire.setFrb(t.getFrontRearBoth());
				tire.setGenerateCatalogPage(t.getGenerateCatalogPage());
				tire.setOemFlag(t.getOemFlag());
				tire.setDiscontinued(t.getDiscontinued());
				tire.setNotBrandedProduct(new Boolean(t.isNotBrandedProduct()));
				tire.setTireBrandName(tiresUtil.getTireBrandName(t));
				tire.setTireBrandImage(tiresUtil.getTireBrandImage(t));
				tire.setTireImage(tiresUtil.getTireImage(t));				
				/*
				 * 		Add store inventory details to response
				 */
				storeInventory = tiresUtil.getStoreInventoryByArticle(store.getStoreNumber(), Long.valueOf(tire.getArticle()));
				if (storeInventory != null) {
					tireItem.setStoreInventory(storeInventory);
				}
				/*
				 * 		Add Mindshare tire survey details to response
				 */
				tiresurveyDetail = getTireSurveyDetail(mappedSurveyData, t.getDisplayId());
				if (tiresurveyDetail != null) {
					tireItem.setTireSurveyDetails(tiresurveyDetail);
				}
				if (offersData.containsKey(articleParam)) {
					Map offerData = promotionsService.getOfferData(siteName, articleParam, null, store.getStoreNumber().toString(), ""+quantity, offersData);
					List discountOffers = (List) offerData.get("discountOffers");
					List rebateOffers = (List) offerData.get("rebateOffers");
					List allOffers = new ArrayList();
					TirePromotion tirePromotion = null;
					if ((discountOffers != null && discountOffers.size() > 0) || (rebateOffers != null && rebateOffers.size() > 0)) {
						tirePromotion = new TirePromotion();
						if (rebateOffers != null && rebateOffers.size() > 0) {
							tirePromotion.setRebateOffers(rebateOffers);
							hasTireRebateSpecialOffer = true;
							for (int i = 0; i < rebateOffers.size(); i++) {
								TirePromotionEvent promoEvent = (TirePromotionEvent) rebateOffers.get(i);
								allOffers.add(promoEvent);
							}
						}
						if (discountOffers != null && discountOffers.size() > 0) {
							tirePromotion.setDiscountOffers(discountOffers);
							tirePromotion.setDiscountAmount(Double.parseDouble(offerData.get("discountAmount").toString()));
							tirePromotion.setFrontDiscountAmount(Double.parseDouble(offerData.get("frontDiscountAmount").toString()));
							tirePromotion.setFrontDiscountAmountTotal(Double.parseDouble(offerData.get("frontDiscountAmountTotal").toString()));
							tirePromotion.setRearDiscountAmount(Double.parseDouble(offerData.get("rearDiscountAmount").toString()));
							tirePromotion.setRearDiscountAmountTotal(Double.parseDouble(offerData.get("rearDiscountAmountTotal").toString()));
							
							TirePromotionEvent promo = (TirePromotionEvent) discountOffers.iterator().next();
							tirePromotion.setPromoId(promo.getPromoId());
							tirePromotion.setPromoName(promo.getPromoName());
							tirePromotion.setPromoDisplayName(promo.getPromoDisplayName());
							
							if(tirePromotion.getDiscountAmount() > 0){
								double salePrice = Double.valueOf(decimaValueFormatter.format(tire.getRetailPrice().doubleValue() - tirePromotion.getDiscountAmount()/(quantity)));
			                    tire.setSalePrice(salePrice);
			                    hasTireDiscountSpecialOffer = true;
			                }
							
							for (int i = 0; i < discountOffers.size(); i++) {
								TirePromotionEvent promoEvent = (TirePromotionEvent) discountOffers.get(i);
								allOffers.add(promoEvent);
							}
						}
					}
					if (!allOffers.isEmpty() && allOffers.size() > 0) {						
						tirePromotion.setOffers(allOffers);
					}
					if (tirePromotion != null) {
						tireItem.setTirePromotion(tirePromotion);
					}
				}
				/*
				 * 		populate the std/opt & matched set articles
				 * */
				if (!allArticles.contains(String.valueOf(t.getArticle()))) {
					allArticles.add(String.valueOf(t.getArticle()));
				}
				if (tireVehicle != null) {
					if (hasOptionalSizeTires && hasStandardSizeTires) {
						if("Standard".equalsIgnoreCase(t.getStandardOptionalDisplay())){
							if (!standardArticles.contains(String.valueOf(t.getArticle()))) {
								standardArticles.add(String.valueOf(t.getArticle()));
							}
			    		}
						if("Optional".equalsIgnoreCase(t.getStandardOptionalDisplay())){
							if (!optionalArticles.contains(String.valueOf(t.getArticle()))) {
								optionalArticles.add(String.valueOf(t.getArticle()));
							}
			    		}
					}
					if (isMatchedSetTires) {
						if("F".equalsIgnoreCase(t.getFrontRearBoth()) ||StringUtils.isNullOrEmpty(t.getFrontRearBoth())){
							if (!frontArticles.contains(String.valueOf(t.getArticle()))) {
								frontArticles.add(String.valueOf(t.getArticle()));
							}
						}
						if("R".equalsIgnoreCase(t.getFrontRearBoth())){
							if (!rearArticles.contains(String.valueOf(t.getArticle()))) {
								rearArticles.add(String.valueOf(t.getArticle()));
							}
						}
					}
				}
				/*
				 * 		populate the filters data
				 * */
				
				// 1. brand filter
				if(!brandFilter.contains(t.getBrand()))
					brandFilter.add(t.getBrand());
				
				// 2. price filter
				if(t.getRetailPrice()>0 && t.getRetailPrice()<100) {
					if(!priceFilter.contains(0+""))
						priceFilter.add(0+"");
				} else if(t.getRetailPrice()>=100 && t.getRetailPrice()<200) {
					if(!priceFilter.contains(1+""))
						priceFilter.add(1+"");
				} else if(tire.getRetailPrice()>=200 && tire.getRetailPrice()<300){
					if(!priceFilter.contains(2+""))
						priceFilter.add(2+"");
				} else if(tire.getRetailPrice()>=300){
					if(!priceFilter.contains(3+""))
						priceFilter.add(3+"");
				}
				
				if (hasTireDiscountSpecialOffer || hasTireRebateSpecialOffer) {
					if(!priceFilter.contains("Sale Only")) {
						priceFilter.add("Sale Only");
					}
				}
				
				// 3. tire type filter
				String newTireType = TireSearchUtils.getNewFilteredName(tire.getTireType());
				if(newTireType != null && !tireTypeFilter.contains(newTireType)){
					tireTypeFilter.add(newTireType);
				}
				
				// 4. tire rating
				if(isBestInClassTire) {
					if (!tireRatingFilter.contains("Best in Class")) {
						tireRatingFilter.add("Best in Class");
					}
				}
				
				// 5. tire size
				String size = tire.getTireSize();
				if(size != null && !sizeFilter.contains(size.replaceAll(" ", "")))
					sizeFilter.add(size.replaceAll(" ", ""));
				
				// 6. std/opt filter
				if (tireVehicle != null && !optionalArticles.isEmpty() && !standardArticles.isEmpty() && stdoptFilter.isEmpty()) {
					stdoptFilter.add("Standard");
					stdoptFilter.add("Optional");
				}
				
				// 7. matched sets
				if (isMatchedSetTires && matchedsetFilter.isEmpty()) {
					matchedsetFilter.add("Matched Set");
					matchedsetFilter.add("Front Tires");
					matchedsetFilter.add("Rear Tires");
				}
				
				// 8. mileage filter
				if (tire.getMileage() != null && tire.getMileage().longValue() > 0) {
					if(tire.getMileage().longValue() >= 50000 && !mileageFilter.contains(2+"")){
						mileageFilter.add(2+"");
					} else if (tire.getMileage().longValue() >= 30000 && tire.getMileage().longValue() < 50000 && !mileageFilter.contains(1+"")) {
						mileageFilter.add(1+"");
					} else if (tire.getMileage().longValue() > 0 && tire.getMileage().longValue() < 30000 && !mileageFilter.contains(0+"")) {
						mileageFilter.add(0+"");
					}
				}
				tireItem.setTire(tire);
				tireItems.put(Long.valueOf(t.getArticle()), tireItem);
			}
		}
		if (!brandFilter.isEmpty())
			filters.put("brand", brandFilter);
		if (!priceFilter.isEmpty())
			filters.put("price", priceFilter);
		if (!tireTypeFilter.isEmpty())
			filters.put("tireType", tireTypeFilter);
		if (!tireRatingFilter.isEmpty())
			filters.put("tireRating", tireRatingFilter);
		if (!sizeFilter.isEmpty() && (tireVehicle == null || (!tires.isEmpty() && optionalArticles.isEmpty()) || (standardArticles.isEmpty())))
			filters.put("size", sizeFilter);
		if (!stdoptFilter.isEmpty())
			filters.put("stdopt", stdoptFilter);
		if (!matchedsetFilter.isEmpty())
			filters.put("matchedset", matchedsetFilter);
		if (!mileageFilter.isEmpty())
			filters.put("mileage", mileageFilter);
		
		if (!allArticles.isEmpty())
			results.setAllArticles(allArticles);
		if (!standardArticles.isEmpty())
			results.setStandardArticles(standardArticles);
		if (!optionalArticles.isEmpty())
			results.setOptionalArticles(optionalArticles);
		if (!frontArticles.isEmpty())
			results.setFrontArticles(frontArticles);
		if (!rearArticles.isEmpty())
			results.setRearArticles(rearArticles);
		if (!matchedSetArticles.isEmpty())
			results.setMatchedSetArticles(matchedSetArticles);		
		if (!filters.isEmpty())
			results.setFilters(filters);
		if (vehicleFitment != null) {
			results.setVehicleFitment(vehicleFitment);
		} else {
			results.setTireSize(tireSize);
		}
		results.setTires(tireItems);
		results.setQuantity(Short.valueOf(String.valueOf(quantity)));
		results.setStoreNumber(store.getStoreNumber());
		results.setSeoVehicleData(seoVehicleData);
		
		response.setPayload(results);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	private MindshareTiresurveyDetails getTireSurveyDetail(Map mappedSurveyData, long displayId) {
		com.bfrc.pojo.survey.MindshareTiresurveyDetails mst = (com.bfrc.pojo.survey.MindshareTiresurveyDetails)mappedSurveyData.get(displayId);
		if (mst != null) {
			return new MindshareTiresurveyDetails(mst.getSalesLine(), mst.getBrandName(), mst.getPercentWillPurchaseAgain(), mst.getDryTraction(), mst.getWetTraction(), mst.getTractionInSnowIce(),
					mst.getTireStability(), mst.getNoiseLevel(), mst.getRideComfort(), mst.getTireWear(), mst.getNumberOfComments(), mst.getNumberOfSurveys());
		}
		return null;
	}

	private SeoVehicleData getSeoVehicleData(FriendlyVehicleDataBean friendlyVehicleDataBean) {
		SeoVehicleData seoVehicleData = null;
		if (friendlyVehicleDataBean != null) {
			seoVehicleData = getSeoVehicleData(friendlyVehicleDataBean.getSeoVehicleData());
		}
		return seoVehicleData;
	}
	
	private SeoVehicleData getSeoVehicleData(com.bfrc.pojo.lookup.SeoVehicleData seoVehicleData) {
		if (seoVehicleData == null) 
			return null;
		
		SeoVehicleData seoData = new SeoVehicleData();
		seoData.setGlobalId(seoVehicleData.getGlobalId());
		seoData.setFileId(seoVehicleData.getFileId());
		seoData.setRecordId(seoVehicleData.getRecordId());
		seoData.setTitle(seoVehicleData.getTitle());
		seoData.setDescription(seoVehicleData.getDescription());
		seoData.setHero(seoVehicleData.getHero());
		seoData.setCta(seoVehicleData.getCta());
		seoData.setHeader1(seoVehicleData.getHeader1());
		seoData.setContent1(seoVehicleData.getContent1());
		seoData.setHeader2(seoVehicleData.getHeader2());
		seoData.setContent2(seoVehicleData.getContent2());
		seoData.setHeader3(seoVehicleData.getHeader3());
		seoData.setContent3(seoVehicleData.getContent3());
		
		return seoData;
	}

}
