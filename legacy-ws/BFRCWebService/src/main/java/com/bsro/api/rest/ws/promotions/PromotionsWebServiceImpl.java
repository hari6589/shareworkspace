package com.bsro.api.rest.ws.promotions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.ESAPI;
import org.owasp.esapi.ValidationErrorList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.model.promotions.SpecialOffer;
import app.bsro.model.promotions.SpecialOffers;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;
import com.bfrc.dataaccess.model.promotion.Promotion;
import com.bfrc.dataaccess.model.promotion.TirePromotionEvent;
import com.bfrc.dataaccess.svc.webdb.DisclaimerService;
import com.bfrc.dataaccess.svc.webdb.PromotionsService;
import com.bfrc.dataaccess.util.Sites;
import com.bsro.core.exception.ws.InvalidArgumentException;
import com.bsro.core.security.RequireValidToken;

/**
 * Implementation of the Promotion Web Service.
 * @author Brad Balmer
 *
 */
@Component
public class PromotionsWebServiceImpl implements PromotionsWebService {

	@Autowired
	private PromotionsService promotionService;
	
	@Autowired
	private DisclaimerService disclaimerService;
	
	@Autowired
	private PropertyAccessor propertyAccessor;
	
	private String offerDateDisclaimerFormat = "MMMM d, yyyy";
	
	private static final String ILLEGAL_ARGUMENT = "Illegal Argument passed";
	private static final String NO_DATA_FOUND = "No Data found";
	private static final String TIRE_OFFERS = "Tire Offers";
	private static final String SERVICE_OFFERS = "Service Offers";
	private static final String MAINTENANCE_OFFERS = "Maintenance Offers";
	private static final String REPAIR_OFFERS = "Repair Offers";
	private static final String ALLSTATE_OFFERS = "All State";
	private static final String BRAKE_OFFERS = "Brake Offers";
	private static final long DEFAULT_ORDER_ID = 25;
	
	/*
	private String couponUrl;
	private String bannerUrl;
	private String tirePromoUrl;
	*/
	private Logger log = Logger.getLogger(getClass().getName());
	
	/**
	 * Returns the special offers for the store number (with site name)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getSpecialOffers(String siteName, Long storeNumber, String friendlyId, String promoType, String promoCategory, HttpHeaders headers) {
		
		log.fine("Returning special offers for "+siteName+" and store "+storeNumber);
		ValidationErrorList errors = new ValidationErrorList();
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		if(storeNumber != null)
			ESAPI.validator().isValidInteger("storeNumber", storeNumber.toString(), 1, Integer.MAX_VALUE, true, errors);
		
		ESAPI.validator().isValidInput("siteName", siteName, "HTTPParameterValue", 10, false, true, errors);
		
		if(errors.size() > 0) {
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}
		
		Calendar now = GregorianCalendar.getInstance();
		Map<String, List> tirePromos = promotionService.getSpecialOffers(siteName, storeNumber, now.getTime(), friendlyId, promoType, promoCategory);
		List<TirePromotionEvent> tirePromotions = tirePromos.get("TIRES");
		if("TP".equals(siteName)) tirePromotions = null; //don't show tire promotions for TP (to be consistent with web site)
		List<Promotion> coupons = tirePromos.get("COUPONS");
		List<Promotion> promotions = tirePromos.get("PROMOTIONS");
		
		tirePromotions = tirePromotions == null ? new ArrayList<TirePromotionEvent>():tirePromotions;
		coupons = coupons == null ? new ArrayList<Promotion>():coupons;
		promotions = promotions == null ? new ArrayList<Promotion>():promotions;
		List<SpecialOffer> tireSo = new ArrayList<SpecialOffer>();
		List<SpecialOffer> couponSo = new ArrayList<SpecialOffer>();
		List<SpecialOffer> promotionSo = new ArrayList<SpecialOffer>();
		for(TirePromotionEvent t : tirePromotions) {
			SpecialOffer so = new SpecialOffer();
			so.setId(t.getPromoId());			
//			so.setType("tire promotions");
			so.setCategory("tire offers");
			so.setImageUrl(Sites.getAssetsRoot(siteName)+propertyAccessor.getStringProperty("specialOffersTirePromoUrl")+t.getPromoName());
			
			SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
			if(t.getOfferStartDate() != null) so.setOfferStartDate(sdf.format(t.getOfferStartDate()));
			if(t.getOfferEndDate() != null) so.setOfferEndDate(sdf.format(t.getOfferEndDate()));
			
			
			if (t.getImageFileId() != null && !t.getImageFileId().isEmpty()) {
				so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/"+t.getImageFileId()+".png");
			} else {
				so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/fcac-tire.png");
			}
				
			if (t.getPromoDescription() != null && !t.getPromoDescription().isEmpty()) {
				so.setDescription(t.getPromoDescription());
			} else {
				so.setDescription(t.getPromoDisplayName());
			}
			if (t.getPrice() != null && !t.getPrice().isEmpty()) {
				so.setPrice(t.getPrice());
			} else {
				so.setPrice(t.getPromoDescription());
			}
			so.setBrand(t.getBrandName());
			so.setTitle(t.getTitle());
						
			// added newly //
			
			if(t.getHomePageOffer() != null)
				so.setHomePageOffer(t.getHomePageOffer());
			
			if(t.getOfferWithoutPrice() != null)
				so.setOfferWithoutPrice(t.getOfferWithoutPrice());
			
			if (t.getPromoPdfText() != null)
				so.setRebateText(t.getPromoPdfText());
			
			if (t.getRebateURL() != null)
				so.setLandingUrl(t.getRebateURL());
			
			
			/////// end ////
			
			if (t.getOrderId() != null) 
				so.setOrderId(t.getOrderId());
			else
				so.setOrderId(Long.valueOf(DEFAULT_ORDER_ID));
			
			if(t.getPromoMainText() != null)
			{
			String promoMainText = StringUtils.trimToEmpty(t.getPromoMainText());
			promoMainText = promoMainText.replaceAll("\\<[^>]*>","");
			promoMainText = promoMainText.replaceAll("&nbsp;", " ");
			so.setOfferDescription(StringUtils.trimToEmpty(promoMainText));
			}
			
			so.setFriendlyId(t.getPromoName());
			
			if(t.getPromotionType() != null) so.setPromotionType(t.getPromotionType());
			
			if(t.getPromoFooterText() != null)
			{
			String promoFooterText = StringUtils.trimToEmpty(t.getPromoFooterText());
			promoFooterText = promoFooterText.replaceAll("\\<[^>]*>","");
			promoFooterText = promoFooterText.replaceAll("&nbsp;", " ");
			so.setDisclaimer(StringUtils.trimToEmpty(promoFooterText));
			}
			
			tireSo.add(so);
		}
		
		for(Promotion c : coupons) {
			SpecialOffer so = new SpecialOffer();
			so.setId(c.getPromotionId());
//			so.setType("coupons");
			if (!"O".equalsIgnoreCase(c.getPromoType())) {
				so.setCategory(c.getTireOffer().booleanValue() ? "tire offers" : c.getRepairOffer().booleanValue() ? REPAIR_OFFERS : c.getMaintOffer().booleanValue() ? MAINTENANCE_OFFERS : "");
			}
			so.setImageUrl(Sites.getAssetsRoot(siteName)+propertyAccessor.getStringProperty("specialOffersCouponUrl")+c.getFriendlyId());
			if(c.getFlashIconBlob() != null)
				so.setBannerUrl(propertyAccessor.getStringProperty("specialOffersBannerUrl")+so.getId());
			so.setDisclaimer(StringUtils.trimToEmpty(c.getDisclaimer()));
			if (!"O".equalsIgnoreCase(c.getPromoType())) {
				if (c.getImageFileId() != null && !c.getImageFileId().isEmpty()) {
					so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/"+c.getImageFileId()+".png");
				} else if (c.getTireOffer().booleanValue()) {
					so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/fcac-tire.png");
				} else {
					so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/fcac-service.png");
				}
			}
				
			if (c.getPromoDescription() != null && !c.getPromoDescription().isEmpty()) {
				so.setDescription(c.getPromoDescription());
			} else {
				so.setDescription(c.getDescription());
			}
			if (c.getPrice() != null && !c.getPrice().isEmpty()) {
				so.setPrice(c.getPrice());
			} else {
				so.setPrice(c.getPromoDescription());
			}
			so.setTitle(c.getTitle());
			if (c.getTireOffer() != null && c.getTireOffer().booleanValue()) {
				so.setBrand(c.getBrand());
			}
						
			// added newly //
			
			if(c.getHomePageOffer() != null)
				so.setHomePageOffer(c.getHomePageOffer());
			
			if(c.getOfferWithoutPrice() != null)
				so.setOfferWithoutPrice(c.getOfferWithoutPrice());
			
			if(c.getOfferDescription() != null)
				so.setOfferDescription(StringUtils.trimToEmpty(c.getOfferDescription()));
			
			if (c.getUrlText() != null && !c.getUrlText().isEmpty())
				so.setRebateText(c.getUrlText());
			
			if (c.getUrl() != null && !c.getUrl().isEmpty())
				so.setLandingUrl(c.getUrl());
			
			/////// end //////
			
			if (c.getOrderId() != null) 
				so.setOrderId(c.getOrderId());
			else
				so.setOrderId(Long.valueOf(DEFAULT_ORDER_ID));
			
			SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
			if(c.getOfferStartDate() != null) so.setOfferStartDate(sdf.format(c.getOfferStartDate()));
			if(c.getOfferEndDate() != null) so.setOfferEndDate(sdf.format(c.getOfferEndDate()));	
			so.setFriendlyId(c.getFriendlyId());
			if(c.getPromotionType() != null) so.setPromotionType(c.getPromotionType());
			if (c.getTireOffer() != null && c.getTireOffer().booleanValue())
				tireSo.add(so);
			else
				couponSo.add(so);
		}
				
		for(Promotion p : promotions) {
			SpecialOffer so = new SpecialOffer();
			so.setId(p.getPromotionId());
//			so.setType("promotions");
			so.setCategory(p.getTireOffer().booleanValue() ? TIRE_OFFERS : p.getRepairOffer().booleanValue() ? REPAIR_OFFERS : p.getMaintOffer().booleanValue() ? MAINTENANCE_OFFERS : "");
			
			if (p.getImageFileId() != null && !p.getImageFileId().isEmpty()) {
				so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/"+p.getImageFileId()+".png");
			} else if (p.getTireOffer().booleanValue()) {
				so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/fcac-tire.png");
			} else {
				so.setSectionImageUrl(Sites.getWebSiteAppRoot(siteName)+"/static-fcac/images/promotion/fcac-service.png");
			}
				
			if (p.getPromoDescription() != null && !p.getPromoDescription().isEmpty()) {
				so.setDescription(p.getPromoDescription());
			} else {
				so.setDescription(p.getDescription());
			}
			if (p.getPrice() != null && !p.getPrice().isEmpty()) {
				so.setPrice(p.getPrice());
			} else {
				so.setPrice(p.getPromoDescription());
			}
			so.setTitle(p.getTitle());
			if (p.getTireOffer().booleanValue()) {
				so.setBrand(p.getBrand());
			}
			
			// added newly //
			
			if(p.getHomePageOffer() != null)
				so.setHomePageOffer(p.getHomePageOffer());
						
			if(p.getOfferWithoutPrice() != null)
				so.setOfferWithoutPrice(p.getOfferWithoutPrice());
						
			if(p.getOfferDescription() != null)
				so.setOfferDescription(StringUtils.trimToEmpty(p.getOfferDescription()));
			
			if (p.getUrlText() != null && !p.getUrlText().isEmpty())
				so.setRebateText(p.getUrlText());
			
			if (p.getUrl() != null && !p.getUrl().isEmpty())
				so.setLandingUrl(p.getUrl());
			
			/////// end //////
			
			if (p.getOrderId() != null) 
				so.setOrderId(p.getOrderId());
			else
				so.setOrderId(Long.valueOf(DEFAULT_ORDER_ID));
			
			SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
			if(p.getOfferStartDate() != null) so.setOfferStartDate(sdf.format(p.getOfferStartDate()));
			if(p.getOfferEndDate() != null) so.setOfferEndDate(sdf.format(p.getOfferEndDate()));

			so.setImageUrl(Sites.getAssetsRoot(siteName)+propertyAccessor.getStringProperty("specialOffersCouponUrl")+p.getFriendlyId());
			if(p.getFlashIconBlob() != null)
				so.setBannerUrl(propertyAccessor.getStringProperty("specialOffersBannerUrl")+so.getId());
			so.setDisclaimer(StringUtils.trimToEmpty(p.getDisclaimer()));
			so.setFriendlyId(p.getFriendlyId());
			if(p.getPromotionType() != null) so.setPromotionType(p.getPromotionType());
			if (p.getTireOffer().booleanValue())
				tireSo.add(so);
			else
				promotionSo.add(so);
		}
		
		Collections.sort(tireSo);
		log.fine("Found "+tireSo.size()+" Tire Promotions");
		
		Collections.sort(couponSo);
		log.fine("Found "+couponSo.size()+" Coupon Special Offers");
		
		Collections.sort(promotionSo);
		log.fine("Found "+promotionSo.size()+" Promotion Special Offers");
		
		if(tireSo.isEmpty() && couponSo.isEmpty() && promotionSo.isEmpty())
		{
			response.setMessage(NO_DATA_FOUND);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		if (tireSo.isEmpty()) tireSo = null;
		if (couponSo.isEmpty()) couponSo = null;
		if (promotionSo.isEmpty()) promotionSo = null;
		
		SpecialOffers specialOffers= new SpecialOffers(tireSo, couponSo, promotionSo);
		response.setPayload(specialOffers);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}
	
	@Override
	@RequireValidToken
	public BSROWebServiceResponse getPromotionsbyLandingPage(final String siteName, 
			final String landingPageId, final String friendlyId, final String source, @Context HttpHeaders headers){
		
		log.fine("Returning offers for "+siteName+" and landing page = "+landingPageId);
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		ValidationErrorList errors = new ValidationErrorList();
		
		ESAPI.validator().isValidInput("siteName", siteName, "HTTPParameterValue", 10, false, true, errors);
		if(errors.size() > 0) {
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			response.setMessage(ILLEGAL_ARGUMENT);
			return response;
		}

		List <Promotion> promoList = promotionService.getOffersByLandingPage(siteName, new Date(), landingPageId, friendlyId, null);
		log.log(Level.INFO, "promotions size = "+ promoList.size());
		
		if(promoList == null || promoList.isEmpty()){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			String disclaimerMsg = disclaimerService.getDisclaimerDescription(siteName, landingPageId);
			
			if (disclaimerMsg != null && disclaimerMsg.trim().length() > 0) {
				response.setMessage(disclaimerMsg);
			} else {
				response.setMessage(NO_DATA_FOUND);
			}
			
			return response;
		}
		List<SpecialOffer> offers = new ArrayList<SpecialOffer>();
		for(Promotion p : promoList) {
			SpecialOffer so = new SpecialOffer();
			so.setId(p.getPromotionId());
			so.setDescription(p.getDescription());
			if(p.getPrice() != null && !p.getPrice().isEmpty()) {
				so.setPrice(p.getPrice());
			}
			if(p.getTitle() != null && !p.getTitle().isEmpty()) {
				so.setTitle(p.getTitle());
			}
			String category = (p.getTireOffer() != null && p.getTireOffer().booleanValue()) ? TIRE_OFFERS : 
				(p.getRepairOffer() != null && p.getRepairOffer().booleanValue()) ? REPAIR_OFFERS :
						(p.getMaintOffer() != null && p.getMaintOffer().booleanValue()) ? MAINTENANCE_OFFERS : "";
			if(category.isEmpty()){
				if(landingPageId.toLowerCase().contains("brake"))
					so.setCategory(BRAKE_OFFERS);
				else if (landingPageId.toLowerCase().contains("allstate"))
					so.setCategory(ALLSTATE_OFFERS);
			}					
			so.setCategory(category);
			so.setImageUrl(Sites.getAssetsRoot(siteName)+propertyAccessor.getStringProperty("specialOffersCouponUrl")+p.getFriendlyId());
			if(p.getFlashIconBlob() != null)
				so.setBannerUrl(propertyAccessor.getStringProperty("specialOffersBannerUrl")+so.getId());
			
			if (p.getUrlText() != null && !p.getUrlText().isEmpty())
				so.setRebateText(p.getUrlText());
			
			if (p.getUrl() != null && !p.getUrl().isEmpty()) {
				so.setLandingUrl(p.getUrl());
			}
			so.setDisclaimer(StringUtils.trimToEmpty(p.getDisclaimer()));
			SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
			if(p.getOfferStartDate() != null) 
				so.setOfferStartDate(sdf.format(p.getOfferStartDate()));
			if(p.getOfferEndDate() != null) 
				so.setOfferEndDate(sdf.format(p.getOfferEndDate()));
			so.setFriendlyId(p.getFriendlyId());
			offers.add(so);
		}
		
		response.setPayload(offers);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

}
