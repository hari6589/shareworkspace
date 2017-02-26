package com.bsro.controller.offers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.bfrc.pojo.promotion.AbstractPromotion;
import com.bsro.service.promotion.PromotionService;
import com.bsro.springframework.web.servlet.view.PermanentRedirectView;

@Controller
public class DecemberOffersController {

	private final Log logger = LogFactory.getLog(DecemberOffersController.class);
	
	private String expectedDateFormat = "y:MM:d:H:m:s";
	
	private List<String> promotionNames;
	
	
	@Autowired
	private PromotionService promotionService;
	
	@Resource(name="environmentPropertiesMap")
	private Map<String, String> environmentProperties;
	
	private String trackingDateFormat = "MM/dd/yyyy";
	private SimpleDateFormat sdf_tracking = new java.text.SimpleDateFormat("MM/dd/yyyy");
	
	@PostConstruct
	public void initializeController(){
		promotionNames = new ArrayList<String>();
		
		//change to december offers promo prefx from prop files
		//first, determine site name to prefix promo with
		String promoPrefix = environmentProperties.get("decemberOffersPromoPrefix");
		
		if(logger.isInfoEnabled()){
			logger.info("Promotion Prefix: " + promoPrefix);
		}
		
		for(int i = 0; i < 15; i++){
			StringBuffer promoName = new StringBuffer();
	    	//pad with zeroes.
			promoName.append(promoPrefix);
			promoName.append("_december_offers_");
			promoName.append(String.format("%02d", i));
			String promoNameAsString = promoName.toString();
			if(logger.isInfoEnabled()){
				logger.info("adding: " + promoNameAsString);
			}
	    	promotionNames.add(promoNameAsString);	    	
		}
		
	}
	
	
	@RequestMapping(value = "/december-offers/offer.htm", method = RequestMethod.GET)
	public String getBlackFridayOffer(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="friendlyId", required=false) String friendlyId,
			@RequestParam(value="description", required=false) String description){
		
		if(StringUtils.isNotBlank(friendlyId)){
			if(logger.isInfoEnabled()){logger.info("friendlyId: " + friendlyId);}
			model.addAttribute("friendlyId",friendlyId);
		}
		
		if(StringUtils.isNotBlank(description)){
			if(logger.isInfoEnabled()){logger.info("description: " + description);}
			model.addAttribute("description",description);
		}
		
		return "offers/december-offers/offers";
	}
	
	
	@RequestMapping(value = "/december-offers.htm", method = RequestMethod.GET)
	public String displayDecemberOffers(HttpServletRequest request, HttpSession session, Model model, 
			@RequestParam(value="testDate", required=false) String testDate) throws ModelAndViewDefiningException{
		
		Date targetDate = new Date();
		
		//add property to turn this off per environment
		boolean allowDecemberOfferDateOverride = Boolean.parseBoolean(environmentProperties.get("allowDecemberOffersDateOverride"));
		if(logger.isInfoEnabled()){
			logger.info("allowDateOverride = "+allowDecemberOfferDateOverride);
		}
		if (allowDecemberOfferDateOverride) {			
			if(StringUtils.isNotBlank(testDate)){
				try {
					if(logger.isInfoEnabled()){
						logger.info("Found a testDate passed: " + testDate);
					}
					SimpleDateFormat sdf = new SimpleDateFormat(expectedDateFormat);
					targetDate = sdf.parse(testDate);
				} catch (ParseException e) {
					logger.error("The testDate passed could not be parsed, therefore using today's date", e);
				}
				
			}
		}
		
		//allowing the targetDate to be controlled by the parameter.
		//this way the effective date of the promo can be controlled by the admin
			
		int size = promotionNames.size();
		int defaultStartIndex = 0;
		int defaultEndIndex = 20; 

			
		if (size > defaultStartIndex) {
			int endIndex = defaultEndIndex;
			if (size < (endIndex - 1)) {
				// if we fall short, just get what we can
				endIndex = size - 1;
			}
			List<AbstractPromotion> promos = 
					promotionService.getPromotionsByFriendlyIds(promotionNames.subList(defaultStartIndex, endIndex), targetDate);
			model.addAttribute("decemberOffersPromosSet",promos);
		}
			
		
		//add tracking formats		
		model.addAttribute("trackingDateFormatString", trackingDateFormat);		
		model.addAttribute("trackingDateFormatter", sdf_tracking);
		
		return "offers/decemberOffers";		
	}	

	
	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	public void setEnvironmentProperties(Map<String, String> environmentProperties) {
		this.environmentProperties = environmentProperties;
	}
}
