package com.bsro.controller.offers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ltd.getahead.dwr.util.SwallowingHttpServletResponse;

import com.bfrc.Config;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.promotion.AbstractPromotion;
import com.bfrc.pojo.promotion.Promotion;
import com.bfrc.pojo.promotion.PromotionImages;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;
import com.bsro.service.promotion.PromotionService;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

/**
 * @author RickFrancisco
 *
 */

@Controller
public class OffersController {
	
	@Autowired
	private PromotionService promotionService;

	@Autowired
	private MailManager mailManager;

	@Autowired
	private Config config;

	@Resource(name="environmentPropertiesMap")
	private Map<String, String> environmentProperties;
	
	private final Log logger = LogFactory.getLog(OffersController.class);
	
	private final String viewAllActiveOffersPage = "offers/index";
	private final String viewPromoTypeOfferPage = "offers/offer-landing-page";
	private final String viewOffersGrid = "common/offers-grid";
	private final String viewHomePagePromotions = "common/homepage-offers";

	private final String HEADER_COUPONS= "headerCoupon";
	private final String COUPONS = "coupons";
	private final String TIRE_COUPON_EVENTS = "tireCouponEvents";
	private final String TIRE_COUPONS = "tireCoupon";
	private final String SERVICE_COUPONS = "serviceCoupons";
	private final String PROMOTION_TYPE = "promotionType";
	private final String BRAKE_INSPECTION_CALLOUT = "fcac-brake-inspection";
	private final String ALIGNMENT_CALLOUT = "fcac-alignment-check";
	private final String IS_DRIVE_KEY = "isDrive";
	private final String PARAM_CLASS = "pclass";
	
	private final int DEFAULT_DISPLAY_NUMBER = 3;
	
	private final String ALL_OFFERS_LANDING_PAGE_ID = "offers";
	
	private String expectedDateFormat = "y:MM:d:H:m:s";
	
	
	@RequestMapping(value = "/offers/offers-grid.htm", method = RequestMethod.GET)
	public String getOfferTypes(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="promotionType", required=false) String promotionType,
			@RequestParam(value="homePageOffer", required=false) boolean homePageOffer,
			@RequestParam(value="class", required=false) String paramclass){
		
		List<Promotion> coupons;
		
		if(homePageOffer) {
			coupons = promotionService.getActiveHomePageCoupons(new Date());
		}
		else {
			coupons = promotionService.getActivePromotionsByType(promotionType, new Date());
		}
		
		// moved to filterDuplicateOffers
		//for(Promotion c : coupons){
		//	promotionService.formatPromotion(c);
		//}

		//promotionService.removePromotionsWithoutImage(coupons);
		coupons = promotionService.filterDuplicateOffers(coupons);

		List<Promotion> tireCoupons = new ArrayList<Promotion>();
		List<Promotion> serviceCoupons = new ArrayList<Promotion>();
		sortTireCoupons(null, coupons, tireCoupons, serviceCoupons);
		if (!homePageOffer && !tireCoupons.isEmpty()) {
			coupons = tireCoupons;
		}		
		
		model.addAttribute(COUPONS, coupons);
		model.addAttribute(PROMOTION_TYPE, promotionService.getPromotionTypeObjectByType(promotionType));
		model.addAttribute(PARAM_CLASS, paramclass);
		
		return viewOffersGrid;
	}
	
	@RequestMapping(value = "/offers/repair.htm", method = RequestMethod.GET)
	public String getRepairOffers(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="numberOfPromos", required=false) String numberOfPromos){
		
		List<Promotion> coupons;
		
		coupons = promotionService.getActiveRepairOffers(new Date());

		
		// moved to filterDuplicateOffers
		//for(Promotion c : coupons){
		//	promotionService.formatPromotion(c);
		//}
		
		// This is to limit the number of offers that are being displayed.
		int numberDisplayed = DEFAULT_DISPLAY_NUMBER;
		if(StringUtils.isNotEmpty(numberOfPromos)) {
			numberDisplayed = Integer.valueOf(numberOfPromos);
		}
		
		//promotionService.removePromotionsWithoutImage(coupons);
		coupons = promotionService.filterDuplicateOffers(coupons, numberDisplayed);
		
		model.addAttribute(COUPONS, coupons);
		
		return viewOffersGrid;
	}
	
	@RequestMapping(value = "/offers/maintenance.htm", method = RequestMethod.GET)
	public String getMaintenanceOffers(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="numberOfPromos", required=false) String numberOfPromos){
		
		List<Promotion> coupons;
		
		coupons = promotionService.getActiveMaintenanceOffers(new Date());
		
		// moved to filterDuplicateOffers 
		//for(Promotion c : coupons){
		//	promotionService.formatPromotion(c);
		//}
		
		// This is to limit the number of offers that are being displayed.
		int numberDisplayed = DEFAULT_DISPLAY_NUMBER;
		if(StringUtils.isNotEmpty(numberOfPromos)) {
			numberDisplayed = Integer.valueOf(numberOfPromos);
		}
		
		//promotionService.removePromotionsWithoutImage(coupons);
		coupons = promotionService.filterDuplicateOffers(coupons, numberDisplayed);
		
		model.addAttribute(COUPONS, coupons);
		
		return viewOffersGrid;
	}
	
	@RequestMapping(value = "/offers/tire.htm", method = RequestMethod.GET)
	public String getTireOffers(HttpServletRequest request, HttpSession session, Model model){
		
		List<Promotion> coupons;		
		
		coupons = promotionService.getActiveTireCoupons(new Date());
		
		// moved to filterDuplicateOffers
		//for(Promotion c : coupons){
		//	promotionService.formatPromotion(c);
		//}
		
		//promotionService.removePromotionsWithoutImage(coupons);
		coupons = promotionService.filterDuplicateOffers(coupons);

		List<Promotion> tireCoupons = new ArrayList<Promotion>();
		List<Promotion> serviceCoupons = new ArrayList<Promotion>();
		sortTireCoupons(null, coupons, tireCoupons, serviceCoupons);
		if (!tireCoupons.isEmpty()) {
			coupons = tireCoupons;
		}		
		
		model.addAttribute(COUPONS, coupons);
		
		return viewOffersGrid;
	}
	
	@RequestMapping(value = "/offers/service.htm", method = RequestMethod.GET)
	public String getServiceCoupons(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="numberOfPromos", required=false) String numberOfPromos){
		
		List<Promotion> coupons;
		
		Date now = new Date();
		coupons = promotionService.getActiveRepairOffers(now);
		coupons.addAll(promotionService.getActiveMaintenanceOffers(now));
		
		
		// moved to filterDuplicateOffers
		//for(Promotion c : coupons){
		//	promotionService.formatPromotion(c);
		//}
		
		// This is to limit the number of offers that are being displayed.
		int numberDisplayed = DEFAULT_DISPLAY_NUMBER;
		if(StringUtils.isNotEmpty(numberOfPromos)) {
			numberDisplayed = Integer.valueOf(numberOfPromos);
		}
		
		//promotionService.removePromotionsWithoutImage(coupons);
		coupons = promotionService.filterDuplicateOffers(coupons, numberDisplayed);
		
		model.addAttribute(COUPONS, coupons);
		
		return viewOffersGrid;
	}
	
	/**
	 * This method pull in only the home page promotions.
	 * It's different than COUPONS
	 * @param request
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/homepage-offer/index.htm", method = RequestMethod.GET)
	public String getHomePagePromotions(HttpServletRequest request, HttpSession session, Model model){
		
		List<Promotion> coupons;
		
		coupons = promotionService.getActiveHomePageCoupons(new Date());
		
		coupons = promotionService.filterDuplicateOffers(coupons);
		
		List<Promotion> tireCoupons = new ArrayList<Promotion>();
		List<Promotion> serviceCoupons = new ArrayList<Promotion>();
		sortTireCoupons(null, coupons, tireCoupons, serviceCoupons);
		tireCoupons.clear();
		serviceCoupons.clear();
		int i=0;
		for(Promotion c : coupons){
			if (i++ % 2 == 0) {
				tireCoupons.add(c);
			} else {
				serviceCoupons.add(c);
			}
		}
		
		model.addAttribute("tireCoupon", tireCoupons);
		model.addAttribute("serviceCoupons", serviceCoupons);

		return viewHomePagePromotions;
	}
	
	@RequestMapping(value = "/drive/offers/{promoType}.htm", method = RequestMethod.GET)
    public String getOfferCouponForDrive(@PathVariable String promoType, HttpServletRequest request, HttpSession session, Model model,
                @RequestParam(value="id", required=false) String friendlyId,
                @RequestParam(value="testDate", required=false) String testDate){

          return getOffers(request, session, model, friendlyId, promotionService.getPromoTypeByName(String.valueOf(promoType)), null, testDate, true, null);
    }

	@RequestMapping(value = "/offers/thanks.htm", method = RequestMethod.POST)
	public String emailTestOffers(HttpServletRequest request, HttpServletResponse response, HttpSession session, Model model,
			//@PathVariable String testVer, 
			@RequestParam(value="friendly-id", required=false) String friendlyId,
			@RequestParam(value="offer-type", required=false) String promoType,
			@RequestParam(value="first-name", required=false) String firstName,
			@RequestParam(value="last-name", required=false) String lastName,
			@RequestParam(value="email", required=false) String email){

        Mail mail = new Mail();
        mail.setTo(new String[]{email});
        mail.setFrom("\"DO-NOT-REPLY\" <webmaster@firestonecompleteautocare.com>");
        mail.setSubject(config.getSiteFullName()+" Offer Coupon");
        
        // body
        String body = "/WEB-INF/views/offers/offer-email.jsp?id="+friendlyId;
    	StringWriter sout = new StringWriter();
        StringBuffer sbuffer = sout.getBuffer();
    	ServletResponse swallowingResponse = new SwallowingHttpServletResponse(response, sout, "utf-8");
    	RequestDispatcher rd = request.getRequestDispatcher(body);
        try {
			rd.include(request, swallowingResponse);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mail.setHtml(true);
        mail.setBody(sbuffer.toString());

        try{
        	mailManager.sendMail(mail);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return getOffers(request, session, model, friendlyId, promotionService.getPromoTypeByName(String.valueOf(promoType)), null, null, false, "thankyoupage");
        
       /* if(testVer.equals("t4") || testVer.equals("t5")|| testVer.equals("t6"))
        	return getOffers(request, session, model, friendlyId, promotionService.getPromoTypeByName(String.valueOf(promoType)), testVer, null, false, "thankyoupage");
        else
        	
        	return "offers/"+ testVer + "/thanks";*/
	}
	
	@RequestMapping(value = "/offers/{promoType}.htm", method = RequestMethod.GET)
	public String getOfferTypes(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable String promoType, 
			@RequestParam(value="id", required=false) String friendlyId,
			@RequestParam(value="testDate", required=false) String testDate){
		
		return getOffers(request, session, model, friendlyId, promotionService.getPromoTypeByName(String.valueOf(promoType)), null, testDate, false, null);
	}
	
	@RequestMapping(value = "/offers/{testVer}/{promoType}.htm", method = RequestMethod.GET)
	public String getTestOfferTypes(HttpServletRequest request, HttpSession session, Model model,
			@PathVariable String promoType, 
			@PathVariable String testVer, 
			@RequestParam(value="id", required=false) String friendlyId,
			@RequestParam(value="testDate", required=false) String testDate){
		
		return getOffers(request, session, model, friendlyId, promotionService.getPromoTypeByName(String.valueOf(promoType)), testVer, testDate, false, null);
	}
	
	public String getOffers(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="id", required=false) String friendlyId,
			@RequestParam(value="promotionType", required=false) String promotionType,
			@RequestParam(value="testVer", required=false) String testVer,
			@RequestParam(value="testDate", required=false) String testDate, 
			boolean isDrive, String fromPage){
		
		
		
		Date targetDate = getTargetDate(testDate);		
		
		if(logger.isInfoEnabled()){ 
			if(!StringUtils.isBlank(friendlyId)){
				logger.info("Received friendlyId: " + friendlyId);
			}
		}
		
		//need to do check for generic offers page, when no params are passed.
		//just check if no friendlyId, then assume that means get 
		//all offers.
		if(!StringUtils.isBlank(promotionType) && promotionType.equalsIgnoreCase("get")){

			List<Promotion> promotions = promotionService.getActivePromotionsByLandingPageId(ALL_OFFERS_LANDING_PAGE_ID, targetDate);
			promotions = promotionService.filterDuplicateOffers(promotions);
			//List<TirePromotionEvent> tireCouponEvents = promotionService.getActiveTirePromotions(targetDate);
			List<TirePromotionEvent> tireCouponEvents = null;
			
			//NEED 3 lists, one for tire COUPONS events from TIRE_PROMOTION table, one for tire coupons from PROMOTION table,
			// and one for auto service COUPONS
			
			//so, always show tire coupon events from TIRE_PROMOTION, let's just add that to the model.
			//tire coupon events - these are the actual tire promos from the TIRE_PROMOTION table
			model.addAttribute(TIRE_COUPON_EVENTS, tireCouponEvents);
			
			//remove any tire coupons from PROMOTION that is a tireoffer, has an image and the description matches the name of a tire coupon events
			//this method does that work to match against tire coupons and existing coupons.
			List<Promotion> tireCoupons = new ArrayList<Promotion>();
			List<Promotion> serviceCoupons = new ArrayList<Promotion>();
			
			//doing this here instead of looping through promos twice
			//this also does the formatting
			sortTireCoupons(tireCouponEvents, promotions, tireCoupons, serviceCoupons);
			
			//tire coupons - from PROMOTION table
			
			model.addAttribute(TIRE_COUPONS, tireCoupons);			
			
			//service promotions
			model.addAttribute(SERVICE_COUPONS, serviceCoupons);
			
			Boolean isDriveObj = Boolean.FALSE;
			if (isDrive) {
				isDriveObj = Boolean.TRUE;
			}
			model.addAttribute(IS_DRIVE_KEY, isDriveObj);
			
			if (testVer == null) {
				return "offers/index";
			} else {
				return "offers/" + testVer + "/index";
			}
			
		}else{		
			//means we received an offer with only the id and no type
			if(StringUtils.isBlank(promotionType)){
				AbstractPromotion promotion = promotionService.getPromotionByFriendlyId(friendlyId);
				if (promotion == null) {
					return "redirect:/offers/";				
				}
				promotionService.formatPromotion(promotion);
				model.addAttribute(HEADER_COUPONS , promotion);
			}else{
				//get all the promos by the promotion type
				
				List<Promotion> currentPromotions = promotionService.getActivePromotionsByType(promotionType, targetDate);

				Promotion headerPromotion = null;
				if (currentPromotions != null) {
					for(Promotion c : currentPromotions){
						promotionService.formatPromotion(c);
						//is this the header one?
						if(c.getFriendlyId().equalsIgnoreCase(friendlyId)){
							headerPromotion = c;
						}									
					}
				}
				if (headerPromotion == null) {
					return "redirect:/offers/";
				}
				promotionService.formatPromotion(headerPromotion);
				model.addAttribute(HEADER_COUPONS , headerPromotion);
				//remove the matched promo for the list so it doesn't appear twice.
				currentPromotions.remove(headerPromotion);
				
				if (promotionType.equalsIgnoreCase("special-coupons")) {
					model.addAttribute(COUPONS, currentPromotions);
					model.addAttribute(PROMOTION_TYPE, promotionService.getPromotionTypeObjectByType(promotionType));
				}
			}
		}
		
		if(fromPage != null && fromPage.equals("thankyoupage"))
		{
			return "offers/thanks-email";
		}
		else
		{
		if (testVer == null) {
			return "offers/offer-landing-page";	
		} else {
			return "offers/" + testVer + "/offer-landing-page";		
		}
		}
	}


	private void sortTireCoupons(List<TirePromotionEvent> tireCouponEvents, List<Promotion> promotions, 
			List<Promotion> tireCoupons, List<Promotion> serviceCoupons) {
		
		List<String> tirePromoNames = new ArrayList<String>();
		if(tireCouponEvents != null){
			for(TirePromotionEvent tce : tireCouponEvents){
				tirePromoNames.add(tce.getPromoDisplayName().trim());
			}
		}
		
		Long width = null;
		Long height = null;
		if(promotions != null){
			
			Map imageMap = promotionService.getAllPromotionImages();
			Map brandMap = promotionService.getAllPromotionBrands();

			for(Promotion promo : promotions){
				//clean up the description
				String description = promo.getDescription(false);
				if (description != null) description = description.trim();
				
				//taken from CouponsTag.java
				//setting a default width/height
				width = promo.getWidth();
				if (width == null || width.longValue() == 0)
					width = new Long(660);
				height = promo.getHeight();
				if (height == null || height.longValue() == 0)
					height = new Long(500);
				
				promotionService.formatPromotion(promo);
				//if it isn't a tire coupon event, then we can add it.
				if(!tirePromoNames.contains(description)){
					if(promo.getTireOffer().booleanValue()){
						if(promo.getImageFileId() != null)
						{
							try
							{
							Object brandId = imageMap.get(promo.getImageFileId());
							com.bfrc.pojo.tire.jda2.Brand brand = (com.bfrc.pojo.tire.jda2.Brand) brandMap.get(brandId+ "");
							if(brand != null)
								promo.setBrandName(brand.getValue());
							}
							catch(Exception e)
							{
								logger.error("The passed could not be parsed, BRANDID", e);
							}
						}
						tireCoupons.add(promo);
					}else if(promo.getRepairOffer().booleanValue() || promo.getMaintOffer().booleanValue()){
						serviceCoupons.add(promo);
					}
				}
			}									
		}
		
	}
	



	private Date getTargetDate(String testDate) {
		
		Date targetDate = new Date();
		boolean allowOfferDateOverride = Boolean.parseBoolean(environmentProperties.get("allowOffersDateOverride"));
		if(logger.isInfoEnabled()){
			logger.info("allowOfferDateOverride = "+allowOfferDateOverride);
		}
		if (allowOfferDateOverride) {			
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
		return targetDate;
	}
	
	public void setEnvironmentProperties(Map<String, String> environmentProperties) {
		this.environmentProperties = environmentProperties;
	}
	
	@RequestMapping(value = "/offers/enterprise.htm", method = RequestMethod.GET)
	public String getEnterprisePage(HttpServletRequest request, HttpSession session, Model model){
		
		return "offers/enterprise";
	}
	
	@RequestMapping(value = "/offers/ecopower.htm", method = RequestMethod.GET)
	public String getEcopowerPage(HttpServletRequest request, HttpSession session, Model model){
		
		return "offers/ecopower";
	}
	
	@RequestMapping(value = "/offers/coupon.htm", method = RequestMethod.GET)
	public String getOfferCoupon(HttpServletRequest request, HttpSession session, Model model, 
			@RequestParam(value="id", required=false) String id ){
		
		if(StringUtils.isEmpty(id)){ 
			model.addAttribute("offer", null);
			return "offers/coupon";		
		}
		Date now = new Date();
		boolean isNumber = StringUtils.isNumeric(id);
		PromotionImages promotion = null;
		//here, we assume we are looking up by the actual id.
		if(isNumber){
			long longId = Long.parseLong(id);
			promotion = promotionService.getActivePromotionImagesById(longId, now);
		}else{
			//we assume we are looking up by the friendly id.
			promotion = promotionService.getActivePromotionImagesByFriendlyId(id, now);
		}
		
		model.addAttribute("id", id);
		model.addAttribute("offer", promotion);
		return "offers/coupon";
	}
	
	@RequestMapping(value = "/common/callout-free-brake-inspection.htm", method = RequestMethod.GET)
	public String checkBrakeInspectionCallout(HttpServletRequest request, HttpSession session, Model model){	

		PromotionImages brakeInspectionCallout = promotionService.getActivePromotionImagesByFriendlyId(BRAKE_INSPECTION_CALLOUT, new Date());
		promotionService.formatPromotion(brakeInspectionCallout);
		
		if(logger.isInfoEnabled()){logger.info("brakeInspectionCallout:" + brakeInspectionCallout);};
		
		model.addAttribute("brakeInspectionCallout", brakeInspectionCallout);
		return "common/callout-free-brake-inspection";
	}
	
	@RequestMapping(value = "/common/callout-free-alignment-check.htm", method = RequestMethod.GET)
	public String checkAlignmentCallout(HttpServletRequest request, HttpSession session, Model model){	

		PromotionImages alignmentCallout = promotionService.getActivePromotionImagesByFriendlyId(ALIGNMENT_CALLOUT, new Date());
		promotionService.formatPromotion(alignmentCallout);
		
		model.addAttribute("alignmentCallout", alignmentCallout);
		return "common/callout-free-alignment-check";
	}
	
	@RequestMapping(value = "/specialoffers/index.htm", method = RequestMethod.GET)
	public String getSpecialOffers(HttpServletRequest request, HttpSession session, Model model,
			@RequestParam(value="testDate", required=false) String testDate){
		
		Date targetDate = getTargetDate(testDate);
		List<Promotion> promotions = promotionService.getActivePromotionsByLandingPageId("specialoffers", targetDate);

		List<Promotion> tireCoupons = new ArrayList<Promotion>();
		List<Promotion> serviceCoupons = new ArrayList<Promotion>();
		sortTireCoupons(null, promotions, tireCoupons, serviceCoupons);
		
		//tire coupons
		model.addAttribute(TIRE_COUPONS, tireCoupons);			
		//service promotions
		model.addAttribute(SERVICE_COUPONS, serviceCoupons);
		return "specialoffers/index";
	}

	@RequestMapping(value = "/specialoffers/coupon.htm", method = RequestMethod.GET)
	public String getSpecialOfferCoupon(HttpServletRequest request, HttpSession session, Model model, 
			@RequestParam(value="id", required=false) String id ){
		
		Date now = new Date();
		PromotionImages promotion = null;
		if(!StringUtils.isEmpty(id)){ 
			if(StringUtils.isNumeric(id)){
				promotion = promotionService.getActivePromotionImagesById(Long.parseLong(id), now);
			}else{
				promotion = promotionService.getActivePromotionImagesByFriendlyId(id, now);
			}
		}
		
		model.addAttribute("offer", promotion);		
		return "specialoffers/coupon";
	}
	
	@RequestMapping(value = "/brakeoffer/{friendlyId}/pdf.htm", method = RequestMethod.GET)
	public void downloadPDF(HttpServletResponse response, HttpServletRequest request,
								@PathVariable String friendlyId) throws Exception {
		Promotion promotion = (Promotion) promotionService.getPromotionByFriendlyId(friendlyId);
		promotionService.formatPromotion(promotion);
		Document document = new Document();
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
	  	try {
		  		ByteArrayOutputStream temp = new ByteArrayOutputStream();
				InputStream in = promotion.getImageBlob().getBinaryStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while ((n=in.read(buf))>=0)
				{
				   temp.write(buf, 0, n);
				
				}
				in.close();
				byte[] imgb = temp.toByteArray();
	    		PdfWriter.getInstance(document, baos);

	    		document.open();
				
	    		 String logoImageRelativePath = "";
	    		//currently this is only needed for FCAC
	    		
	    		logoImageRelativePath = "/static-fcac/images/logo-fcac-print.jpg";
	    		
	    		
	    		String logoImagePath = request.getSession().getServletContext().getRealPath(logoImageRelativePath);
	    		
	    		PdfPTable storeDetailTable = new PdfPTable(2);
	    		
	    		Image logoImage = Image.getInstance(logoImagePath);
				logoImage.scaleAbsoluteHeight(29);
				logoImage.scaleAbsoluteWidth(177);
	    		PdfPCell logoCell = new PdfPCell(logoImage, false);
	    		logoCell.setHorizontalAlignment(Element.ALIGN_LEFT);
	    		Font infoFont = new Font(FontFamily.TIMES_ROMAN, 16);  		

	      		PdfPCell storeInfoCell = new PdfPCell(new Paragraph(""));
	      		logoCell.setBorder(Rectangle.NO_BORDER);
	      		storeInfoCell.setPaddingLeft(10);
	      		storeInfoCell.setBorder(Rectangle.NO_BORDER);
	      		
				storeDetailTable.addCell(logoCell);
	      		storeDetailTable.addCell(storeInfoCell);
	    		document.add(storeDetailTable); 	
				LineSeparator sep = new LineSeparator();
	    		sep.setOffset(-3.0f);
	    		sep.setLineWidth(0.5f);
	    		document.add(sep);	
	 			document.add( Chunk.NEWLINE );
	 			Paragraph desc = new Paragraph(promotion.getDescription(),infoFont);
				document.add(desc); 
	 			document.add( Chunk.NEWLINE );
				Image offerImage = Image.getInstance(imgb);
				offerImage.scaleAbsoluteHeight(260);
				offerImage.scaleAbsoluteWidth(450);
				PdfPCell offerImageCell = new PdfPCell(offerImage, false);
				offerImageCell.setBorder(0);
				PdfPTable offerTable = new PdfPTable(1);
				offerTable.setWidthPercentage(100);
				offerTable.getDefaultCell().setPadding(1);
				offerTable.addCell(offerImageCell);
				document.add(offerTable); 
	    		    		
	    		document.close();
	    } catch (DocumentException e) {
	    	System.err.println("DocumentException in generating pdf document");
	   	}
	   	
	   	response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=brakeoffer.pdf");
	    response.setHeader("Cache-Control", "cache, must-revalidate");
	    response.setHeader("Pragma", "public");
	        
		if(baos != null && baos.size() > 0){
			response.setContentLength(baos.size());
			response.getOutputStream().write(baos.toByteArray());
			response.getOutputStream().flush();
		}	
		
	}
}
