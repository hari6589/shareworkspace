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
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.bfrc.pojo.promotion.AbstractPromotion;
import com.bsro.service.promotion.PromotionService;
import com.bsro.springframework.web.servlet.view.PermanentRedirectView;;

@Controller
public class BlackFridayController {

	private final Log logger = LogFactory.getLog(BlackFridayController.class);
	
	private Date odometerEndDate; //11/15/2012 @ 11:59 PM.
	private Date cutoffDate; //11/27/2012 @ 12:00 AM
	
	private String expectedDateFormat = "y:MM:d:H:m:s";
	
	private List<String> promotionNames;
	
	
	@Autowired
	private PromotionService promotionService;
	
	@Resource(name="environmentPropertiesMap")
	private Map<String, String> environmentProperties;
	
	private String trackingDateFormat = "MM/dd/yyyy";
	private SimpleDateFormat sdf_tracking = new java.text.SimpleDateFormat("MM/dd/yyyy");
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}
	
	@PostConstruct
	public void initializeController(){
		try {
			String blackFridayOdometerEndDate = environmentProperties.get("blackFridayOdometerEndDate");
			if(logger.isInfoEnabled()){logger.info("Black Friday Odometer End Date: " + blackFridayOdometerEndDate);}
			SimpleDateFormat sdf = new SimpleDateFormat(expectedDateFormat);
			odometerEndDate = sdf.parse(blackFridayOdometerEndDate); // this is the last possible second to display the odometer.
		} catch (ParseException e) {
			logger.error("An Error occurred parsing the odometer end dates", e);
			odometerEndDate = new Date();
		}
		
		try {
			String blackFridayRedirectDate = environmentProperties.get("blackFridayRedirectDate");
			if(logger.isInfoEnabled()){ logger.info("Black Friday Redirect Date: " + blackFridayRedirectDate); }
			SimpleDateFormat sdf = new SimpleDateFormat(expectedDateFormat);
			cutoffDate = sdf.parse(blackFridayRedirectDate); // this is the date redirect starts to offers page
		} catch (ParseException e) {
			logger.error("An Error occurred parsing the cutoff date", e);
			cutoffDate = null;
		}
		
		promotionNames = new ArrayList<String>();
		
		
		//change to black friday promo prefx from prop files
		//first, determine site name to prefix promo with
		String promoPrefix = environmentProperties.get("blackFridayPromoPrefix");
		
		if(logger.isInfoEnabled()){
			logger.info("Promotion Prefix: " + promoPrefix);
		}
		
		for(int i = 0; i < 15; i++){
			StringBuffer promoName = new StringBuffer();
	    	//pad with zeroes.
			promoName.append(promoPrefix);
			promoName.append("_black_friday_");
			promoName.append(String.format("%02d", i));
			String promoNameAsString = promoName.toString();
			if(logger.isInfoEnabled()){
				logger.info("adding: " + promoNameAsString);
			}
	    	promotionNames.add(promoNameAsString);	    	
		}
		
	}
	
	@RequestMapping(value = "/black-friday/offer.htm", method = RequestMethod.GET)
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
		
		return "offers/black-friday/offers";
	}
	
	@RequestMapping(value = "/black-friday.htm", method = RequestMethod.GET)
	public String displayBlackFridayOffers(HttpServletRequest request, HttpSession session, Model model, 
			@RequestParam(value="testDate", required=false) String testDate) throws ModelAndViewDefiningException{
		
		Date targetDate = new Date();
		
		//add property to turn this off per environment
		boolean allowBlackFridayDateOverride = Boolean.parseBoolean(environmentProperties.get("allowBlackFridayDateOverride"));
		if(logger.isInfoEnabled()){
			logger.info("allowDateOverride = "+allowBlackFridayDateOverride);
		}
		if (allowBlackFridayDateOverride) {			
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
		
		//redirect after black friday cutoff date
		if(targetDate.after(cutoffDate)){
			String redirectLocation = environmentProperties.get("blackFridayRedirectLocation");
			throw new ModelAndViewDefiningException(new ModelAndView(new PermanentRedirectView(redirectLocation)));
		}
				
		boolean showOdometer = false;				
		if(targetDate.before(odometerEndDate)){
			showOdometer = true;
		}
		
		model.addAttribute("showOdometer", showOdometer);
		
		//now load promotions, only if we don't show odometer
		if(!showOdometer){
			//allowing the targetDate to be controlled by the parameter.
			//this way the effective date of the promo can be controlled by the admin
			
			int size = promotionNames.size();
			int defaultStartIndex1 = 0;
			int defaultEndIndex1 = 6; //6 promos
			int defaultEndIndex2 = 10; // 4 promos
			int defaultEndIndex3 = 11; // 1 promos
			int defaultEndIndex4 = 14; // 3 promos
			
			if (size > defaultStartIndex1) {
				int endIndex = defaultEndIndex1;
				if (size < (endIndex - 1)) {
					// if we fall short, just get what we can
					endIndex = size - 1;
				}
				List<AbstractPromotion> promos1 = promotionService.getPromotionsByFriendlyIds(promotionNames.subList(defaultStartIndex1, endIndex), targetDate);
				model.addAttribute("blackFridayPromosSet1",promos1);
			}
			
			if (size > defaultEndIndex1) {
				int endIndex = defaultEndIndex2;
				if (size < (endIndex - 1)) {
					// if we fall short, just get what we can
					endIndex = size - 1;
				}
				List<AbstractPromotion> promos2 = promotionService.getPromotionsByFriendlyIds(promotionNames.subList(defaultEndIndex1, endIndex), targetDate);
				model.addAttribute("blackFridayPromosSet2",promos2);
			}
			
			if (size > defaultEndIndex2) {
				int endIndex = defaultEndIndex3;
				if (size < (endIndex - 1)) {
					// if we fall short, just get what we can
					endIndex = size - 1;
				}
				List<AbstractPromotion> promos3 = promotionService.getPromotionsByFriendlyIds(promotionNames.subList(defaultEndIndex2, endIndex), targetDate);
				model.addAttribute("blackFridayPromosSet3",promos3);
			}
			
			if (size > defaultEndIndex3) {
				int endIndex = defaultEndIndex4;
				if (size < (endIndex - 1)) {
					// if we fall short, just get what we can
					endIndex = size - 1;
				}
				List<AbstractPromotion> promos4 = promotionService.getPromotionsByFriendlyIds(promotionNames.subList(defaultEndIndex3, endIndex), targetDate);
				model.addAttribute("blackFridayPromosSet4",promos4);
			}
			

		}
		
		//add tracking formats		
		model.addAttribute("trackingDateFormatString", trackingDateFormat);		
		model.addAttribute("trackingDateFormatter", sdf_tracking);
		
		
		return "offers/blackFriday";		
	}	

	public void setPromotionService(PromotionService promotionService) {
		this.promotionService = promotionService;
	}

	public void setEnvironmentProperties(Map<String, String> environmentProperties) {
		this.environmentProperties = environmentProperties;
	}

}
