
package com.bsro.controller.oil;

/**
 *
 * @author
 *
 */

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import app.bsro.model.oil.OilChangePackage;
import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;

import com.bfrc.Config;
import com.bfrc.dataaccess.model.promotion.PromotionBusinessRules;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.store.Store;
import com.bfrc.security.Encode;
import com.bsro.controller.appointment.SlickcomboTime;
import com.bsro.controller.appointment.SlickcomboTimeData;
import com.bsro.databean.store.StoreWidgetDataBean;
import com.bsro.exception.errors.ErrorMessageException;
import com.bsro.service.oil.OilService;
import com.bsro.service.store.StoreService;
import com.bsro.springframework.propertyeditor.ReplaceAllEditor;
import com.bsro.springframework.web.servlet.view.PermanentRedirectView;
import com.bsro.util.encryption.EncryptionUtil;

public class OilBaseController {

	private Log logger = LogFactory.getLog(OilBaseController.class);

	private static final String dateFormatString = "MMMM d, yyyy"; // was sdf
	private static final String decimalFormatString = "000000"; // was df
	private static final String moneyWithCommasFormatString = "#,##0.00"; // was mf
	private static final String moneyFormatString = "##.00"; // was pattern
	private static final String dateFullFormatString = "MMMMMMMM dd, yyyy";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX = "oil.cache.non.user.input.";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_YEARS = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX+"oil.years";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MAKES = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX+"makes.";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MODELS = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX+"models.";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MILEAGES = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX+"mileages";

	private static final String OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_SEARCH_RESULTS = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX+"searchResults.";

	private static final Integer QUOTE_DAYS_UTIL_EXPIRATION = 30;

	private static java.text.DecimalFormat decimalFormatter = new java.text.DecimalFormat(decimalFormatString);
	
	private static java.text.DecimalFormat decimaValueFormatter = new java.text.DecimalFormat(moneyFormatString);

	protected OilService oilService;

	protected PromotionDAO promotionDAO;

	public PromotionDAO getPromotionDAO() {
		return promotionDAO;
	}

	public void setPromotionDAO(PromotionDAO promotionDAO) {
		this.promotionDAO = promotionDAO;
	}


	protected StoreService storeService;

	protected LocatorOperator locator;

	protected String indexView;

	protected String searchView;

	protected String quoteView;
	protected String quoteEmailView;
	protected String changeStoreView;
	protected String quoteNotFoundURL;
	protected String quoteNotFoundView;
	protected String quoteExpiredView;
	protected String noStoreFoundView;
	protected Config thisConfig;
	protected Boolean displayNationalOffers;
	protected String processDataForMakeView;

	public String getProcessDataForMakeView() {
		return processDataForMakeView;
	}

	public void setProcessDataForMakeView(String processDataForMakeView) {
		this.processDataForMakeView = processDataForMakeView;
	}

	public String getProcessDataForModelView() {
		return processDataForModelView;
	}

	public void setProcessDataForModelView(String processDataForModelView) {
		this.processDataForModelView = processDataForModelView;
	}

	protected String processDataForModelView;
	public String getChangeStoreView() {
		return changeStoreView;
	}

	public void setChangeStoreView(String changeStoreView) {
		this.changeStoreView = changeStoreView;
	}

	public OilService getOilService() {
		return oilService;
	}

	public void setOilService(OilService oilService) {
		this.oilService = oilService;
	}

	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	public LocatorOperator getLocator() {
		return locator;
	}

	public void setLocator(LocatorOperator locator) {
		this.locator = locator;
	}

	public String getIndexView() {
		return indexView;
	}

	public void setIndexView(String indexPage) {
		this.indexView = indexPage;
	}

	public String getSearchView() {
		return searchView;
	}

	public void setSearchView(String searchPage) {
		this.searchView = searchPage;
	}

	public String getQuoteEmailView() {
		return quoteEmailView;
	}

	public void setQuoteEmailView(String quoteEmailView) {
		this.quoteEmailView = quoteEmailView;
	}

	public String getQuoteView() {
		return quoteView;
	}

	public void setQuoteView(String quotePage) {
		this.quoteView = quotePage;
	}

	public String getQuoteNotFoundURL() {
		return quoteNotFoundURL;
	}

	public void setQuoteNotFoundURL(String quoteNotFoundPage) {
		this.quoteNotFoundURL = quoteNotFoundPage;
	}

	public String getQuoteNotFoundView() {
		return quoteNotFoundView;
	}

	public void setQuoteNotFoundView(String quoteNotFoundView) {
		this.quoteNotFoundView = quoteNotFoundView;
	}

	public String getQuoteExpiredView() {
		return quoteExpiredView;
	}

	public void setQuoteExpiredView(String quoteExpiredPage) {
		this.quoteExpiredView = quoteExpiredPage;
	}

	public String getNoStoreFoundView() {
		return noStoreFoundView;
	}

	public void setNoStoreFoundView(String noStoreFoundView) {
		this.noStoreFoundView = noStoreFoundView;
	}

	public Config getThisConfig() {
		return thisConfig;
	}

	public void setThisConfig(Config thisConfig) {
		this.thisConfig = thisConfig;
	}


	public Boolean getDisplayNationalOffers() {
		return displayNationalOffers;
	}

	public void setDisplayNationalOffers(Boolean displayNationalOffers) {
		this.displayNationalOffers = displayNationalOffers;
	}

	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
		binder.registerCustomEditor(String.class, new ReplaceAllEditor(false, "&#39;", "'"));
	}
	public String getNewView(HttpServletRequest request, HttpSession session, Model model) {
		initializeFormatStrings(request);
		// get the list of appointment services - now with categories
		initializeOilVehicleWidget(request, session);
		addOffersForSlider(request, getDisplayNationalOffers(), null);
		addLandingCoupon(request);
		// TODO: The items put on the model won't be there after the redirect, need to fix this logic
		if(model.asMap().get("vehicleError")!=null && ((String)model.asMap().get("vehicleError")).length()>0) {
			return "redirect:/"+getIndexView();
		}
		return getIndexView();
	}

	/**
	 * These format strings are used by the JSP to format dates and numbers and such
	 * @param request
	 */
	private void initializeFormatStrings(HttpServletRequest request) {
		request.setAttribute("dateFormatString", dateFormatString);
		request.setAttribute("decimalFormatString", decimalFormatString);
		request.setAttribute("moneyWithCommasFormatString", moneyWithCommasFormatString);
		request.setAttribute("moneyFormatString", moneyFormatString);
		request.setAttribute("dateFullFormatString", dateFullFormatString);
	}

	public String doOilSearch(HttpServletRequest request, HttpSession session, HttpServletResponse response,  Model model) {


		initializeFormatStrings(request);

		String zip = request.getParameter("zip");
		String yearId = request.getParameter("oilChangeVehicleYearId");
		String makeId = request.getParameter("oilChangeVehicleMakeId");
		String modelId = request.getParameter("oilChangeVehicleModelId");
		String mileageId = request.getParameter("oilChangeVehicleMileageId");

		if(ServerUtil.isNullOrEmpty(yearId)){
			model.addAttribute("vehicleError", "Year is not set");
			return getNewView(request,session,model);
		}if(ServerUtil.isNullOrEmpty(makeId)){
			model.addAttribute("vehicleError", "Make is not set");
			return getNewView(request,session,model);
		}if(ServerUtil.isNullOrEmpty(modelId)){
			model.addAttribute("vehicleError", "Model is not set");
			return getNewView(request,session,model);
		}if(ServerUtil.isNullOrEmpty(mileageId)){
			model.addAttribute("vehicleError", "Mileage Range is not set");
			return getNewView(request,session,model);
		}if(zip==null ||zip.length()==0 || "ZIP Code".equals(zip)){
			// TODO: Check for zip code validity
			model.addAttribute("vehicleError", "ZIP Code is not set");
			return getNewView(request,session,model);
		}

		String highMileage = request.getParameter("oilChangeVehicleMileageId");
		String selectedCarId = request.getParameter("oilChangeVehicleModelId");
		StoreWidgetDataBean storeWidgetDataBean = null;
		Store store = null;
		Boolean highMileageBool = null;

		try {
			Map<String, String> years = getYears(session);
			Map<String, String> makes = getManufacturers(session, yearId);
			Map<String, String> models = getModels(session, yearId, makeId);
			Map<String, String> mileages = getMileages(session);


			highMileageBool = highMileage!=null&&highMileage.equals("1");
			model.addAttribute("oilChangeVehicleYearId", yearId);
			model.addAttribute("oilChangeVehicleMakeId", makeId);
			model.addAttribute("oilChangeVehicleModelId", modelId);
			model.addAttribute("oilChangeVehicleMileageId", mileageId);
			model.addAttribute("oilChangeVehicleYearName", years.get(yearId));
			model.addAttribute("oilChangeVehicleMakeName", makes.get(makeId));
			model.addAttribute("oilChangeVehicleModelName", models.get(modelId));
			model.addAttribute("oilChangeVehicleMileageName", mileages.get(mileageId));
			model.addAttribute("isHighMileage", highMileageBool );
			Util.debug(" in oil search : zip= " + zip + " highmiles= "+highMileageBool +" car= " + selectedCarId);
			Util.debug(" in oil search : vehicleYearId= " + yearId + " vehicleMakeString= "+makes.get(makeId) +" vehicleModelString= " + models.get(modelId));


			String geoPoint = request.getParameter("geoPoint");
			String selectedStoreNumberString = request.getParameter("storeNumber");
			initializeOilChangeStoreWidget(request, session, selectedStoreNumberString, zip, geoPoint);
			storeWidgetDataBean = (StoreWidgetDataBean) request.getAttribute("storeWidget");
			store = storeWidgetDataBean.getSelectedStore();
			request.setAttribute("store", store);
		} catch (Throwable throwable) {
			//TODO return "redirect:/auto-services/oil";
			throwable.printStackTrace();
		}


		if (storeWidgetDataBean == null || storeWidgetDataBean.getStoreList() == null || storeWidgetDataBean.getStoreList().isEmpty()) {
			return getNoStoreFoundView();
		}


		OilChangeSearchResult results = null;

		if (selectedCarId!=null&&selectedCarId.length()>0) {
			try{
				results = getOilChangeSearchResult(session, selectedCarId, new Long(store.getStoreNumber()),highMileageBool);

				if(results!=null){
					Util.debug(" oilProducts : not null") ;
					if(results.getOilChangePackages()!=null){
						Util.debug(" results.getOilChangePackages()!=null size:" + results.getOilChangePackages().size());
						//Util.debug(" results.getOilChangePackages()!=null");
					}
				}
				model.addAttribute("oilProducts", results);
			}catch(Exception e){
				//TODO return "redirect: /auto-services/oil/index";
				e.printStackTrace();
			}
		}else{
			//TODO return "redirect: /auto-services/oil/index";
		}

		cacheVehicleWidgetParameters(session, request);
		addLandingCoupon(request);


		return getSearchView();
	}

	public String displayOilQuoteNotFoundPage(HttpServletRequest request, HttpSession session, HttpServletResponse response, Model model) {
		return getQuoteNotFoundView();
	}

	public String displayOilQuoteEmail(HttpServletRequest request, HttpSession session, HttpServletResponse response, Model model) throws Exception {
		// Due to the nature of the RequestDispatcher include used to send emails, we can't use Spring Binding to get the parameters...
		String quoteId = request.getParameter("code");

		String emailNote = null;
		if (request.getParameter("note") != null) {
			emailNote = request.getParameter("note");
		}
		String emailFromAddress = null;
		if (request.getParameter("efrom") != null) {
			emailFromAddress = request.getParameter("efrom");
		}

		request.setAttribute("emailNote", emailNote);
		request.setAttribute("emailFromAddress", emailFromAddress);

		displayOilQuotePage(request, session, response, quoteId, model);
		return getQuoteEmailView();
	}

	private void interruptAndRedirectToQuoteNotFoundPage(String quoteId) throws ModelAndViewDefiningException {
		PermanentRedirectView myView = new PermanentRedirectView(getQuoteNotFoundURL()+"?code="+quoteId);
		ModelAndView quoteNotFound = new ModelAndView(myView);
		throw new ModelAndViewDefiningException(quoteNotFound);
	}

	public String displayOilQuotePage(HttpServletRequest request, HttpSession session, HttpServletResponse response, String quoteId, Model model) throws Exception {
		initializeFormatStrings(request);
		Long quoteIdLong = null;
		String decryptedQuoteId = null;

		try {
		    decryptedQuoteId = EncryptionUtil.decryptWithSymmetricKeyEncryption(quoteId);
		} catch (Throwable throwable) {
			logger.error("Encrypted quote id - "+quoteId+" could not be decrypted", throwable);
			interruptAndRedirectToQuoteNotFoundPage(quoteId);
		}

		try {
			quoteIdLong = Long.valueOf(decryptedQuoteId);
		} catch (Throwable throwable) {
			logger.error("Decrypted quote id - "+decryptedQuoteId+" is not valid", throwable);
			interruptAndRedirectToQuoteNotFoundPage(quoteId);
		}

		OilChangeQuote oilChangeQuote = null;
		Store store = null;

		try {
			oilChangeQuote = oilService.getOilChangeQuote(quoteIdLong, thisConfig.getSiteName());
		} catch (Throwable throwable) {
			logger.error("Oil quote "+quoteIdLong+" was not retrieved", throwable);
			interruptAndRedirectToQuoteNotFoundPage(quoteId);
		}

		try {
			/*  Change Store Number from Integer to Long as per current TP application
			 *
			 * store = storeService.getStoreById(oilChangeQuote.getStoreNumber().intValue());
			 */
				store = storeService.getStoreById(oilChangeQuote.getStoreNumber());
		} catch (Throwable throwable) {
			logger.error("Could not retreive store "+oilChangeQuote.getStoreNumber()+" from quote "+quoteIdLong+", "+thisConfig.getSiteName(), throwable);
			interruptAndRedirectToQuoteNotFoundPage(quoteId);
		}

		model.addAttribute("quote", oilChangeQuote);

		initializeOilChangeStoreWidget(request, session, store.getStoreNumber().toString(), null, null);

		model.addAttribute("store", store);
		
		// The total value is not formed correctly in ADD A MESSAGE(OPTIONAL) textarea.
		// Replaced the 'oilChangeQuote.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal() to getTotal()' to get the correct total value.
		String comments = "oil change quote id:  " + decimalFormatter.format(oilChangeQuote.getOilChangeQuoteId()) + ",  oil article number:  " + oilChangeQuote.getBaseOil().getArticleNumber() + ",  oil description: " + oilChangeQuote.getOilType().getName() + ",  oil change total price: $" + decimaValueFormatter.format(oilChangeQuote.getTotal()) + ",  oil change quote End;"; 

		request.setAttribute("comments", comments);

		if (session != null) {
					session.setAttribute("APPOINTMENT_COMMENTS", comments);
		}

		request.setAttribute("disableChangeStore", "1");
		request.setAttribute("disableChangeVehicle", "1");
		addLandingCoupon(request);

		Calendar quoteExpirationDate = (Calendar)oilChangeQuote.getCreatedDate().clone();
		quoteExpirationDate.add(Calendar.DATE, QUOTE_DAYS_UTIL_EXPIRATION+1);
		quoteExpirationDate.set(Calendar.HOUR_OF_DAY, 0);
		quoteExpirationDate.set(Calendar.MINUTE, 0);
		quoteExpirationDate.set(Calendar.SECOND, 0);
		quoteExpirationDate.set(Calendar.MILLISECOND, 0);

		Calendar currentDate = Calendar.getInstance();

		if (currentDate.after(quoteExpirationDate) || currentDate.equals(quoteExpirationDate)) {
			model.addAttribute("quoteExpirationDate", quoteExpirationDate);
			return getQuoteExpiredView();
		}

		return getQuoteView();
	}
	public String createOilQuote(HttpServletRequest request, HttpSession session, HttpServletResponse response, String articleId, Long storeNumber, String zip, Boolean isHighMileage, String vehicleId) throws Exception {

		try{

			//Store store =(Store)storeService.getStoreById(storeNumber.intValue());
			Store store =(Store)storeService.getStoreById(storeNumber);

			String webSite = getThisConfig().getSiteName();
			Long oilArticleNumber = Long.parseLong(articleId);

			String yearId = request.getParameter("oilChangeVehicleYearId");
			String makeId = request.getParameter("oilChangeVehicleMakeId");
			String modelId = request.getParameter("oilChangeVehicleModelId");

			Map<String, String> years = getYears(session);
			Map<String, String> makes = getManufacturers(session, yearId);
			Map<String, String> models = getModels(session, yearId, makeId);

			Integer vehicleYear = Integer.parseInt(years.get(yearId));

			OilChangeSearchResult oilChangeSearchResults = getOilChangeSearchResult(session, vehicleId, storeNumber, isHighMileage);

			try{
				Long code = oilService.createOilChangeQuote(oilArticleNumber, storeNumber, oilChangeSearchResults.getVehicleOilCapacity(), vehicleId, vehicleYear, makes.get(makeId), models.get(modelId), store.getZip(), webSite, isHighMileage);
				return EncryptionUtil.encryptWithSymmetricKeyEncryptionV01(Long.toString(code));
			}catch(Exception e){
				Util.debug("Error in service call createOilChangeQuote");
				e.printStackTrace();
			}

		}catch(Exception e){
			Util.debug("Error parsing parameters in createOilQuote");
			e.printStackTrace();
		}
		return null;

		}

	private void initializeOilChangeStoreWidget(HttpServletRequest request, HttpSession session, String selectedStoreNumberString, String zip, String geoPoint) throws Exception {

		/*
		 * Changed the selectedStoreNumber from Integer to Long
		 * Modified Date: 08/13/2013
		 */
		//Integer selectedStoreNumber = null;
		Long selectedStoreNumber = null;
		String context = "oil";
		String contextType = Encode.html(request.getParameter("contextType"));
		if(!ServerUtil.isNullOrEmpty(contextType) && "oil".equals(contextType))
		{
			context = "oil_changeStore";
		}

		if (selectedStoreNumberString != null) {
			//selectedStoreNumber = Integer.valueOf(selectedStoreNumberString);
			selectedStoreNumber = Long.valueOf(selectedStoreNumberString);
		}
//		StoreWidgetDataBean storeWidgetDataBean = getStoreWidgetDataBean(request, session, selectedStoreNumber, zip, geoPoint);
		StoreWidgetDataBean storeWidgetDataBean = storeService.initializeStoreWidget(context, selectedStoreNumber, thisConfig.getSiteName(), null, null, null, zip, null, request.getRemoteAddr(), geoPoint, false, true, true, null, true, false, 10);

		session.setAttribute("oilchange.storeWidget", storeWidgetDataBean);
		request.setAttribute("storeWidget", storeWidgetDataBean);
	}

	/**
	 * Returns a json representation of the given object, or the string "false" if it cannot do so.
	 *
	 * @param object
	 * @return
	 */
	private String generateAjaxResponse(Object object) {
		ObjectMapper JSONMapper = new ObjectMapper();
		String jsonResponse = Boolean.FALSE.toString();

		try {
			jsonResponse = JSONMapper.writeValueAsString(object);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		return jsonResponse;
	}

	public String updateOilChangeQuoteFirstNameAjax(String oilChangeQuoteId, String firstName, Model model) {
		String jsonResponse = Boolean.FALSE.toString();

		try {
			String decryptedQuoteId = EncryptionUtil.decryptWithSymmetricKeyEncryption(oilChangeQuoteId);
			Long quoteIdLong = Long.valueOf(decryptedQuoteId);
			Boolean wasSuccessful = oilService.updateOilChangeQuoteFirstName(quoteIdLong, thisConfig.getSiteName(), firstName);
			jsonResponse = generateAjaxResponse(wasSuccessful.toString());
		} catch (ErrorMessageException errorMessageException) {
			jsonResponse = generateAjaxResponse(errorMessageException.getErrors());
		} catch (Throwable throwable) {
			logger.error("Could not update oil change quote with quote id "+oilChangeQuoteId, throwable);
		}

		model.addAttribute("jsonData", jsonResponse);

		return "common/jsonData";
	}

	public String updateOilChangeQuoteLastNameAjax(String oilChangeQuoteId, String lastName, Model model) {
		String jsonResponse = Boolean.FALSE.toString();

		try {
			String decryptedQuoteId = EncryptionUtil.decryptWithSymmetricKeyEncryption(oilChangeQuoteId);
			Long quoteIdLong = Long.valueOf(decryptedQuoteId);
			Boolean wasSuccessful = oilService.updateOilChangeQuoteLastName(quoteIdLong, thisConfig.getSiteName(), lastName);
			jsonResponse = generateAjaxResponse(wasSuccessful.toString());
		} catch (ErrorMessageException errorMessageException) {
			jsonResponse = generateAjaxResponse(errorMessageException.getErrors());
		} catch (Throwable throwable) {
			logger.error("Could not update oil change quote with quote id "+oilChangeQuoteId, throwable);
		}

		model.addAttribute("jsonData", jsonResponse);

		return "common/jsonData";
	}

	public void addLandingCoupon(HttpServletRequest request){



		String app = Config.locate(request.getSession().getServletContext()).getSiteName();
		List<String> ids = new ArrayList<String>();
		ids.add(app.toLowerCase().trim()+"_landing_lof" );



		List coupons = promotionDAO.findPromotionsByFriendlyIds(ids,true);



		if(coupons!=null&&coupons.size()>0) {

			request.setAttribute("landingOffer", coupons.get(0));
		}



	}
	public void addOffersForSlider(HttpServletRequest request, Boolean nationalOffers, String storeNumber){

		String app = Config.locate(request.getSession().getServletContext()).getSiteName();
		if(nationalOffers!=null && nationalOffers){
			List<String> ids = new ArrayList<String>();
			java.text.NumberFormat nf = new java.text.DecimalFormat("00");

			for(int i=1; i<=10; i++)
				ids.add(app.toLowerCase().trim()+"_lof_offer_" + nf.format(i));

			List coupons = promotionDAO.findPromotionsByFriendlyIds(ids,true);

			request.setAttribute("nationalOffers", coupons);
		}

		if(storeNumber!=null&&storeNumber.length()>0){
			//TODO GET STORE OIL OFFERS/PROMOTIONS for app, not for phase 1
			request.setAttribute("storeOffers", null);
		}

	}
	public String displaySelectStoreForOilChangeSearch(HttpServletRequest request, HttpSession session){
		StoreWidgetDataBean storeWidgetDataBean = null;

		String zip = Encode.html(request.getParameter("zip"));
		String geoPoint = Encode.html(request.getParameter("geoPoint"));
		String selectedStoreNumberString = Encode.html(request.getParameter("storeNumber"));

		boolean refreshData = true;

		if (session.getAttribute("oilchange.storeWidget") != null) {
			refreshData = false;
			storeWidgetDataBean = (StoreWidgetDataBean) session.getAttribute("oilchange.storeWidget");
			if (zip != null) {
				if (storeWidgetDataBean.getZip() == null || !storeWidgetDataBean.getZip().equals(zip)) {
					refreshData = true;
				}
			}
			if (geoPoint != null) {
				if (storeWidgetDataBean.getGeoPoint() == null || !storeWidgetDataBean.getGeoPoint().equals(geoPoint)) {
					refreshData = true;
				}
			}
			if (selectedStoreNumberString != null) {
				if (storeWidgetDataBean.getSelectedStore() == null || !storeWidgetDataBean.getSelectedStore().getStoreNumber().equals(Integer.valueOf(selectedStoreNumberString))) {
					refreshData = true;
				}
			}
		}

		if (refreshData) {
			try {
				initializeOilChangeStoreWidget(request, session, selectedStoreNumberString, zip, geoPoint);
				storeWidgetDataBean = (StoreWidgetDataBean) session.getAttribute("oilchange.storeWidget");
			} catch (Exception exception) {
				// TODO: Need some kind of ajax error handling!
			}
		}

		if (storeWidgetDataBean != null) {
			storeWidgetDataBean.moveSelectedStoreToFront();
		}

		request.setAttribute("storeWidget", storeWidgetDataBean);

		return getChangeStoreView();
	}

	protected void initializeOilVehicleWidget(HttpServletRequest request, HttpSession session) {
		try{
		Boolean displayCacheFields = true;
		if (!ServerUtil.isNullOrEmpty(request.getParameter("displayCache"))) {
			Boolean.valueOf(request.getParameter("displayCache"));
		}

		Boolean includeTracking = true;
		if (!ServerUtil.isNullOrEmpty(request.getParameter("includeTracking"))) {
			includeTracking = Boolean.valueOf(request.getParameter("includeTracking"));
		}

		String zipCache		= normalizeCache(session, request,	"zip",				"cache.oilChangeZip");
		String yearCache	= normalizeCache(session, request,	"oilChangeVehicleYearId",		"cache.oilChangeVehicleYearId");
		String makeCache	= normalizeCache(session, request,	"oilChangeVehicleMakeId",		"cache.oilChangeVehicleMakeId");
		String modelCache	= normalizeCache(session, request,	"oilChangeVehicleModelId",	"cache.oilChangeVehicleModelId");
		String mileageCache	= normalizeCache(session, request,	"oilChangeVehicleMileageId",	"cache.oilChangeVehicleMileageId");

		Map<String,String> yearOptions = new LinkedHashMap<String,String>();
		Map<String,String> makeOptions = new LinkedHashMap<String,String>();
		Map<String,String> modelOptions = new LinkedHashMap<String,String>();
		Map<String,String> mileageOptions = new LinkedHashMap<String,String>();

		mileageOptions = getMileages(session);
		request.setAttribute("mileageOptions", mileageOptions);

		yearOptions = getYears(session);
		request.setAttribute("yearOptions", yearOptions);

		//year = 2010, manufacturer = honda, model = Civic DX (get Ids for webservice from the maps)
		boolean useCache = !"".equalsIgnoreCase(yearCache) && !"".equalsIgnoreCase(makeCache) && !"".equalsIgnoreCase(modelCache);
		if (useCache) {
			// Make sure that everything works

			String selectedYear = null;
			String selectedMake = null;
			String selectedModel = null;
			String selectedMileage = null;

			if (!"".equalsIgnoreCase(yearCache)) {
				if (yearOptions != null && yearOptions.get(yearCache) != null) {
					selectedYear = yearCache;
				}
			}

			if (!"".equalsIgnoreCase(yearCache)) {
				makeOptions = getManufacturers(session, yearCache);
				if (makeOptions!=null && makeOptions.get(makeCache) != null) {
					selectedMake = makeCache;
				}
			}

			if (!"".equalsIgnoreCase(makeCache)) {
				modelOptions = getModels(session, yearCache, makeCache);
				if (makeOptions!=null && modelOptions.get(modelCache) != null) {
					selectedModel = modelCache;
				}
			}

			if (!"".equalsIgnoreCase(mileageCache)) {
				if (mileageOptions!=null && mileageOptions.get(mileageCache) != null) {
					selectedMileage = mileageCache;
				}
			}

			useCache = !"".equalsIgnoreCase(selectedYear) && !"".equalsIgnoreCase(selectedMake) && !"".equalsIgnoreCase(selectedModel) && !"".equalsIgnoreCase(selectedMileage);
		}

		request.setAttribute("displayCacheFields", displayCacheFields);
		request.setAttribute("includeTracking", includeTracking);
		request.setAttribute("useCache", useCache);

		if (useCache) {
			request.setAttribute("zipCache", zipCache);
			request.setAttribute("yearCache", yearCache);
			request.setAttribute("makeCache", makeCache);
			request.setAttribute("modelCache", modelCache);
			request.setAttribute("mileageCache", mileageCache);

			request.setAttribute("makeOptions", makeOptions);
			request.setAttribute("modelOptions", modelOptions);
		}

		}catch(IOException e){
			e.printStackTrace();
			return;
		}
	}

	public SlickcomboTimeData processData(HttpServletRequest request, HttpSession session, HttpServletResponse response,
			 String yearId,	String makeId, String modelId) throws IOException {

		Util.debug("======processing data yearId:"+yearId+" makeId "+makeId+" modelId "+modelId );

		if(yearId==null||yearId.length()==0){

			Util.debug("======processing year" );
			Map<String,String> yearOptions = new LinkedHashMap<String,String>();
			yearOptions = getYears(session);

			List<SlickcomboTime> yearsData = new ArrayList<SlickcomboTime>();
			SlickcomboTimeData data = new SlickcomboTimeData();

			for(String year : yearOptions.keySet()){
				SlickcomboTime slickComboTime = new SlickcomboTime();
				//String sortedDateNoLeadingZeroes = org.apache.commons.lang.StringUtils.stripStart(sortedDate, "0");
				slickComboTime.setText(yearOptions.get(year));
				slickComboTime.setValue(year);
				yearsData.add(slickComboTime);
			}

			data.setData(yearsData);

			return data;
		}else if((yearId!=null&&yearId.length()>0)&&(makeId==null||makeId.length()==0)){

			Util.debug("======processing make" );
			Map<String,String> makeOptions = new LinkedHashMap<String,String>();
			makeOptions = getManufacturers(session, yearId);

			List<SlickcomboTime> makesData = new ArrayList<SlickcomboTime>();
			SlickcomboTimeData data = new SlickcomboTimeData();

			for(String make : makeOptions.keySet()){
				SlickcomboTime slickComboTime = new SlickcomboTime();
				//String sortedDateNoLeadingZeroes = org.apache.commons.lang.StringUtils.stripStart(sortedDate, "0");
				slickComboTime.setText(makeOptions.get(make));
				slickComboTime.setValue(make);
				makesData.add(slickComboTime);
			}

			data.setData(makesData);

			return data;
		}else if((yearId!=null&&yearId.length()>0&&makeId!=null&&makeId.length()>0)&&(modelId==null||modelId.length()==0)){

			Util.debug("======processing model" );
			Map<String,String> modelOptions = new LinkedHashMap<String,String>();
			modelOptions = getModels(session, yearId, makeId);

			List<SlickcomboTime> modelData = new ArrayList<SlickcomboTime>();
			SlickcomboTimeData data = new SlickcomboTimeData();

			for(String model : modelOptions.keySet()){
				SlickcomboTime slickComboTime = new SlickcomboTime();
				//String sortedDateNoLeadingZeroes = org.apache.commons.lang.StringUtils.stripStart(sortedDate, "0");
				slickComboTime.setText(modelOptions.get(model));
				slickComboTime.setValue(model);
				modelData.add(slickComboTime);
			}

			data.setData(modelData);

			return data;

		}
		else if(modelId!=null&&modelId.length()>0){

		}

		return new SlickcomboTimeData();
	}

	private String normalizeCache(HttpSession session, HttpServletRequest request, String parameterName, String sessionAttributeName) {
		String localCache = Encode.html(request.getParameter(parameterName));

		if (ServerUtil.isNullOrEmpty(localCache)) {
			localCache = (String)session.getAttribute(sessionAttributeName);
		}

		if (ServerUtil.isNullOrEmpty(localCache)) {
			localCache = "";
		}

		if (!localCache.equals((String)session.getAttribute(sessionAttributeName))) {
			session.setAttribute(sessionAttributeName, localCache);
		}

		return localCache;
	}

	private void cacheVehicleWidgetParameters(HttpSession session, HttpServletRequest request) {
		if (!ServerUtil.isNullOrEmpty(request.getParameter("zip"))) {
			session.setAttribute("cache.oilChangeZip", request.getParameter("zip"));
		}
		if (!ServerUtil.isNullOrEmpty(request.getParameter("oilChangeVehicleYearId"))) {
			session.setAttribute("cache.oilChangeVehicleYearId", request.getParameter("oilChangeVehicleYearId"));
		}
		if (!ServerUtil.isNullOrEmpty(request.getParameter("oilChangeVehicleMakeId"))) {
			session.setAttribute("cache.oilChangeVehicleMakeId", request.getParameter("oilChangeVehicleMakeId"));
		}
		if (!ServerUtil.isNullOrEmpty(request.getParameter("oilChangeVehicleModelId"))) {
			session.setAttribute("cache.oilChangeVehicleModelId", request.getParameter("oilChangeVehicleModelId"));
		}
		if (!ServerUtil.isNullOrEmpty(request.getParameter("oilChangeVehicleMileageId"))) {
			session.setAttribute("cache.oilChangeVehicleMileageId", request.getParameter("oilChangeVehicleMileageId"));
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> getYears(HttpSession session) throws IOException {
		String cacheKey = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_YEARS;

		 if (session.getAttribute(cacheKey) != null && session.getAttribute(cacheKey) instanceof Map<?, ?>) {
			 return (Map<String, String>)session.getAttribute(cacheKey);
		 }
		 cleanCachedNonUserInputFromSession(session, cacheKey);

		 Map<String, String> years = oilService.getYears();
		 if (years != null && !years.isEmpty()) {
			 session.setAttribute(cacheKey, years);
		 }
		 return years;
	 }
	 private Map<String,String> getManufacturers(HttpSession session, String yearId) throws IOException {
		 String myPrefix = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MAKES;
		 String cacheKey = myPrefix+yearId;

		 if (session.getAttribute(cacheKey) != null && session.getAttribute(cacheKey) instanceof Map<?, ?>) {
			 return (Map<String, String>)session.getAttribute(cacheKey);
		 }
		 cleanCachedNonUserInputFromSession(session, myPrefix);

		 Map<String, String> makes = oilService.getManufacturers(yearId);
		 if (makes != null && !makes.isEmpty()) {
			 session.setAttribute(cacheKey, makes);
		 } else {
			 // This is mostly here because of how the mobile sites (Netbiscuits) respond to our error handlers..there's no way to handle the error nicely, so it just reloads the page. By wiping the year
			 // cache, we can ensure display of an appropriate message.
			 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_YEARS);
		 }

		 return makes;
	 }
	 private Map<String,String> getModels(HttpSession session, String yearId, String manufacturerId) throws IOException {
		 String myPrefix = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MODELS;
		 String cacheKey = myPrefix+yearId+"."+manufacturerId;

		 if (session.getAttribute(cacheKey) != null && session.getAttribute(cacheKey) instanceof Map<?, ?>) {
			 return (Map<String, String>)session.getAttribute(cacheKey);
		 }
		 cleanCachedNonUserInputFromSession(session, myPrefix);

		 Map<String, String> models = oilService.getModels(yearId, manufacturerId);
		 if (models != null && !models.isEmpty()) {
			 session.setAttribute(cacheKey, models);
		 } else {
			 // This is mostly here because of how the mobile sites (Netbiscuits) respond to our error handlers..there's no way to handle the error nicely, so it just reloads the page. By wiping the year
			 // cache, we can ensure display of an appropriate message.
			 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_YEARS);
			 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MAKES);
		 }

		 return models;
	 }
	 private Map<String,String> getMileages(HttpSession session) {
		String cacheKey = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MILEAGES;

		 if (session.getAttribute(cacheKey) != null && session.getAttribute(cacheKey) instanceof Map<?, ?>) {
			 return (Map<String, String>)session.getAttribute(cacheKey);
		 }
		 cleanCachedNonUserInputFromSession(session, cacheKey);

		 Map<String, String> mileages = oilService.getMileages();
		 if (mileages != null && !mileages.isEmpty()) {
			 session.setAttribute(cacheKey, mileages);
		 } else {
				 // This is mostly here because of how the mobile sites (Netbiscuits) respond to our error handlers..there's no way to handle the error nicely, so it just reloads the page. By wiping the year
				 // cache, we can ensure display of an appropriate message.
				 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_YEARS);
				 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MAKES);
				 cleanCachedNonUserInputFromSession(session, OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_MODELS);
			 }
		 return mileages;
	 }
	 private OilChangeSearchResult getOilChangeSearchResult(HttpSession session, String equipmentId, Long storeNumber, Boolean isHighMileageVehicle) throws IOException {
		 String myPrefix = OIL_SEARCH_NON_USER_INPUT_CACHE_PREFIX_SEARCH_RESULTS;
		 String cacheKey = myPrefix+equipmentId+"."+storeNumber+"."+isHighMileageVehicle;

		 if (session.getAttribute(cacheKey) != null && session.getAttribute(cacheKey) instanceof OilChangeSearchResult) {
			 return (OilChangeSearchResult) session.getAttribute(cacheKey);
		 }
		 cleanCachedNonUserInputFromSession(session, myPrefix);

		 OilChangeSearchResult oilChangeSearchResult = oilService.getProducts(equipmentId, storeNumber, isHighMileageVehicle);
		 
		 
		 PromotionBusinessRules promotionBusinessRules = null;
		 for (OilChangePackage oilChangePackage : oilChangeSearchResult.getOilChangePackages()) {
			 if( oilChangePackage != null && oilChangePackage.getOilType() != null)
			 {
			 Object[] obj = promotionDAO.findOilPromotionByPriceType(oilChangePackage.getOilType().getOilTypeFriendlyId());
			 BigDecimal subTotal = oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal();
			 
			 BigDecimal percentDiscount;
			 BigDecimal amountDiscount;
			 BigDecimal specificPrice;
		     BigDecimal localBigDecimal1;
		     BigDecimal localBigDecimal2;
		     promotionBusinessRules = new PromotionBusinessRules();
		     
			 if(obj != null){
				 
				 promotionBusinessRules.setProductType((String)obj[0]);
				 promotionBusinessRules.setProductSubType((String)obj[1]);
				 promotionBusinessRules.setPriceModificationType((String)obj[2]);
				 promotionBusinessRules.setSpecificPrice((BigDecimal)obj[3]);
				 promotionBusinessRules.setPercentDiscount((BigDecimal)obj[4]);
				 promotionBusinessRules.setAmountDiscount((BigDecimal)obj[5]);
				 promotionBusinessRules.setIsRebate((BigDecimal)obj[5]);
				 
				 if ("PERCENT_DISCOUNT".equalsIgnoreCase(promotionBusinessRules.getPriceModificationType()))
			      {
					 percentDiscount = promotionBusinessRules.getPercentDiscount();
			        if (percentDiscount != null)
			        {
			        	localBigDecimal1 = subTotal.subtract(subTotal.multiply(percentDiscount).divide(new BigDecimal(100)));
			        	oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(localBigDecimal1);
			        	localBigDecimal2 = subTotal.subtract(localBigDecimal1);
			        	oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion(localBigDecimal2);
			        	oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion(percentDiscount);		        	
			          
			        }
			      }
				  else
			      {
			        BigDecimal localBigDecimal3;
			        BigDecimal localBigDecimal4;
			        if ("AMOUNT_DISCOUNT".equalsIgnoreCase(promotionBusinessRules.getPriceModificationType()))
			        {
			        	amountDiscount = promotionBusinessRules.getAmountDiscount();
			          if (amountDiscount != null)
			          {
			        	  oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(amountDiscount));			        	  
			        	  localBigDecimal1 = oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion());			        	  
			        	  localBigDecimal2 = oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion().divide(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(), RoundingMode.HALF_DOWN);
			        	  
			        	  localBigDecimal3 = localBigDecimal2.multiply(new BigDecimal(100));			        	  
			        	  localBigDecimal4 = new BigDecimal(100).subtract(localBigDecimal3);			        	  
			        	  oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion(localBigDecimal1);
			        	  oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion(localBigDecimal4);
			          }
			        }
			        else if ("SPECIFIC_PRICE".equalsIgnoreCase(promotionBusinessRules.getPriceModificationType()))
			        {
			        	specificPrice = promotionBusinessRules.getSpecificPrice();
			          if (specificPrice != null)
			          {
			        	  oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(specificPrice));
			              localBigDecimal1 = oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal().subtract(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion());
			              localBigDecimal2 = oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion().divide(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilSubtotal(), RoundingMode.HALF_DOWN);
			              localBigDecimal3 = localBigDecimal2.multiply(new BigDecimal(100));
			              localBigDecimal4 = new BigDecimal(100).subtract(localBigDecimal3);
			              oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilAmountSavedFromPromotion(localBigDecimal1);
			              oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilPercentSavedFromPromotion(localBigDecimal4);
			          }
			        }		        
			        
			      }
				 
			 }
			 else
			 {
				 oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(subTotal);
			 }
			 if (oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion() != null)
				 oilChangePackage.setBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion());
			// oilChangePackage.setTotal(oilChangePackage.getBaseOilOilChangeOilFilterAndAdditionalOilTotalWithPromotion());
			
		 }
			 
		 } 
		 
		 
		 session.setAttribute(cacheKey, oilChangeSearchResult);

		 return oilChangeSearchResult;
	 }

	 /**
	  * This exists so we don't keep unreferenced items in the session...basically, if there's something meeting the criteria, and it's not the thing
	  * we want, we clean it up.
	  *
	  * @param session
	  * @param prefix
	  */
	 private void cleanCachedNonUserInputFromSession(HttpSession session, String prefix) {
		 Enumeration names = session.getAttributeNames();

		 if (names != null) {
			 while (names.hasMoreElements()) {
				 String name = (String)names.nextElement();

				 if (name.startsWith(prefix)) {

					 Util.debug("** clearing cache  " + name );
					 session.removeAttribute(name);
				 }
			 }
		 }
	 }
}
