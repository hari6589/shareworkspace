package com.bsro.controller.store;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import map.States;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.bfrc.Config;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.StoreAdminDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.framework.dao.store.GeocodeOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.User;
import com.bfrc.pojo.grandopening.BsroGrandOpening;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.store.StoreHoliday;
import com.bfrc.pojo.store.StoreHolidayHour;
import com.bfrc.pojo.store.StoreHour;
import com.bfrc.pojo.store.StoreHourViewObject;
import com.bfrc.pojo.store.StoreSearch;
import com.bfrc.pojo.storeadmin.StoreAdminAnnouncement;
import com.bfrc.pojo.storeadmin.StoreAdminOffer;
import com.bfrc.pojo.storeadmin.StoreAdminOfferTemplate;
import com.bfrc.pojo.storeadmin.StoreAdminOfferTemplateImages;
import com.bfrc.pojo.storeadmin.StoreAdminOfferViewObject;
import com.bfrc.pojo.storeadmin.StoreAdminPromotion;
import com.bfrc.pojo.storeadmin.StoreAdminPromotionViewObject;
import com.bfrc.pojo.storeadmin.StoreAdminStoreImage;
import com.bfrc.security.Encode;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.constants.SessionConstants;
import com.bsro.databean.store.StoreDataBean;
import com.bsro.databean.store.StoreWidgetDataBean;
import com.bsro.service.geoip.IpInfoService;
import com.bsro.service.location.LocationService;
import com.bsro.service.store.StoreService;
import com.bsro.service.time.TimeZoneDstService;
import com.bsro.springframework.web.servlet.view.PermanentRedirectView;
import com.bsro.taglib.common.StoreDetailLinkTag;
import com.hibernate.dao.base.BaseDao;

@Controller
//@RequestMapping("/locate")
public class StoreController {

	private final Log logger = LogFactory.getLog(StoreController.class);

	public final String STORE_LOCATOR_PREFIX = "com.bfrc.storelocator.";
	public final static String SESSION_TIMEOUT_KEY = "com.bfrc.struts2.locator.sessionTimeoutKey";
	private final String lwTrackingTag = "var s=s_gi('lwingbridgestonedev');s.linkTrackVars='eVar43,prop43,events';s.linkTrackEvents='event35';s.events='event35';s.eVar43='_DESCRIPTION_';s.prop43=s.eVar43;s.tl(this,'o','Store Offer Click');";
	@Autowired
	StoreService storeService;

	@Autowired
	StoreAdminDAO storeAdminDAO;
	@Autowired
	GeocodeOperator geocodeOperator;
	@Autowired
	TirePromotionDAO tirePromotionDAO;
	@Autowired
	BaseDao baseGrandOpeningDAO;
	@Autowired
	LocatorOperator locator;
	@Autowired
	LocationService locationService;
	@Autowired
	LocatorDAO locatorDAO;
	@Autowired
	StoreDAO storeDAO;
	@Autowired
	TimeZoneDstService timeZoneDstService;
	@Autowired
	private States statesMap;
	@Autowired
	Config thisConfig;
	@Autowired
	IpInfoService ipInfoService;
	@Resource(name = "environmentPropertiesMap")
	private Map<String, String> environmentProperties;

	@PostConstruct
	public void initializeController() {
	}

	@InitBinder
	public void initBinder(WebDataBinder binder, HttpSession session) {

		// session.setAttribute(SESSION_TIMEOUT_KEY,"");
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
		binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
		binder.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class, true));
	}

	@ModelAttribute("statesMap")
	public Map<String, String> getStatesMap() {
		return statesMap;
	}
	
	public String getNitrogenStores(){
		return "|5886|25194|19658|19127|19860|28703|336230|326853|";
	}

	// TODO this was working as a catch all, but setStore was getting caught
	// here instead of in the setStore method
	@RequestMapping(value = "/")
	public String catchAll(Model model) throws Exception {

		return "redirect:/locate/";
	}

	// @ModelAttribute("isSessionAvailable")
	// public Boolean checkSession(HttpSession session) throws
	// ModelAndViewDefiningException {
	//
	// Util.debug("CHECKING THE SESSION");
	// if(session==null||session.getAttribute(SESSION_TIMEOUT_KEY) == null) {
	// Util.debug("session dissappeared: sending back to /locate/index");
	// throw new ModelAndViewDefiningException(new ModelAndView(new
	// RedirectView("/locate/index.jsp")));
	// }
	// return true;
	// }

	// @RequestMapping(value = "/locate/index.htm", method = RequestMethod.GET)
	// public String locateIndex( ) throws Exception {
	//
	// return "redirect:/locate/";
	// }
	@RequestMapping(value = "/locate/index.htm", method = RequestMethod.GET)
	public String locateIndex(Model model,HttpServletRequest request, @RequestParam(value = "zip", required = false) String zip, @RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "state", required = false) String state, @RequestParam(value = "address", required = false) String address) throws Exception {

		zip = Encode.encodeForJavaScript(zip);
		state = Encode.encodeForJavaScript(state);
		
		model.addAttribute("zip", zip != null ? zip : "");
		model.addAttribute("city", city != null ? city : "");
		model.addAttribute("state", state != null ? state : "");
		model.addAttribute("address", address != null ? address : "");
		
		List states = storeDAO.getStoreStates();
		request.setAttribute("_states_", states);
		return "locate/index";
	}

	// @RequestMapping(value = "/locate/display-map.htm", method =
	// RequestMethod.GET)
	// public String locateDisplayMapGet(Model model, @RequestParam(value="zip",
	// required=false) String zip, @RequestParam(value="city", required=false)
	// String city, @RequestParam(value="state", required=false) String state,
	// @RequestParam(value="address", required=false) String address ) throws
	// Exception {
	//
	// model.addAttribute("zip",zip!=null?zip:"");
	// model.addAttribute("city",city!=null?city:"");
	// model.addAttribute("state",state!=null?state:"");
	// model.addAttribute("address",address!=null?address:"");
	// return "locate/index";
	// }

	@RequestMapping(value = "/locate/set-store.htm", method = RequestMethod.GET)
	public String setPreferredStoreOnly(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "newStore", required = false) Long storeNumber, @RequestParam(value = "returnUrl", required = false) String returnUrl, @RequestParam(value = "cacheZip", required = false) String cacheZip) throws Exception {

		//Util.debug("returnUrl: " + returnUrl);
		
 		if(storeNumber==null){
			return "redirect:/locate/";
		}
		if (storeNumber != null) {
			Store store = storeService.findStoreLightById(storeNumber);
			if (store == null || store.getActive() == 0)
				return "redirect:/locate/";// TODO what should we do if the store
										// isn't found? or if it's closed
			session.setAttribute(SessionConstants.PREFERRED_STORE, store);
			if(!StringUtils.isNullOrEmpty(cacheZip))
				CacheDataUtils.setCachedZip(request, cacheZip);
			else
				CacheDataUtils.setCachedZip(request, store.getZip());
		}
		if(returnUrl.indexOf("storeNumber")>-1)
			returnUrl = StringUtils.replaceParameterInUrl("storeNumber",storeNumber.toString(), returnUrl,true);
		
		if(isValidUrl(returnUrl)){
			if(onWhiteList(returnUrl)){
				return "redirect:" + returnUrl;
			}else{
				return "redirect:/locate/";
			}
		}else{
			return "redirect:" + returnUrl ;
		}
	}

	@RequestMapping(value = "/locate/set-store.htm", method = RequestMethod.POST)
	public String setPreferedStore(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "newStore", required = false) Long storeNumber, @RequestParam(value = "returnUrl", required = false) String returnUrl,
			@RequestParam(value = "navZip", required = false) String navZip, @RequestParam(value = "fromPage", required = false) String fromPage, @RequestParam(value = "cacheZip", required = false) String cacheZip)  throws Exception {

		//Util.debug("returnUrl: " + returnUrl);
		// Util.debug("navZip "+navZip +" returnURL "+returnUrl );
		String storeTracking="";
		if (storeNumber != null) {
			Store store = storeService.findStoreLightById(storeNumber);
			if (store == null){
				if (!StringUtils.isNullOrEmpty(navZip)) 
					return "redirect:/locate/?navZip="+navZip;
				else
					return "redirect:/locate/";
			}// TODO what should we do if the store
										// isn't found? or if it's closed
			session.setAttribute(SessionConstants.PREFERRED_STORE, store);
			if(!StringUtils.isNullOrEmpty(cacheZip))
				CacheDataUtils.setCachedZip(request, cacheZip);
			else
				CacheDataUtils.setCachedZip(request, store.getZip());
		} else if (navZip != null) {
			try {
				StoreSearch storeSearch = storeService.createStoreSearchObject(navZip);
				Float[] location = new Float[2];
				long[][] storeArray = null;
				String app = thisConfig.getSiteName();
				String remoteIP = request.getRemoteAddr();
				long requestId = locator.getRequestId();
				location = locator.geoLocationWithBing(requestId, app, null, storeSearch.getCity(), storeSearch.getState(), storeSearch.getZip(), remoteIP);
				storeArray = locator.getClosestStores(location, app, true, 5, false, storeSearch.getState(), false, null, 1, false, null, true);
				Store[] stores = null;
				// Did we get a list of store IDs back?
				if (storeArray != null && storeArray.length > 0) {
					// Go get the actual stores
					stores = locator.getLocatorDAO().getStores(storeArray);
					// Build the distances array and set the store hours
					if (stores != null && stores.length > 0) {
						Store store = stores[0];
//						store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(stores[0].getNumber()), true));
						storeNumber = store.getNumber();
						store = storeService.findStoreLightById(storeNumber);
						session.setAttribute(SessionConstants.PREFERRED_STORE, store);
						if(!StringUtils.isNullOrEmpty(storeSearch.getZip()))
							CacheDataUtils.setCachedZip(request, storeSearch.getZip());
						else
							CacheDataUtils.setCachedZip(request, store.getZip());
					}
					if(fromPage!=null){
						storeTracking = "trackPage="+fromPage +"&searchSuccess=true";
					}
				}else{
					storeTracking = "trackPage="+fromPage +"&searchSuccess=false";
				}
			} catch (Exception e) {
				e.printStackTrace();
				if (!StringUtils.isNullOrEmpty(navZip)) 
					return "redirect:/locate/?navZip="+navZip +"&searchSuccess=false";
				else
					return "redirect:/locate/";
			}
		}else{
				return "redirect:/locate/";
		}
		// Util.debug(returnUrl);
		if (storeNumber == null || StringUtils.isNullOrEmpty(returnUrl))// TODO
																		// where
																		// should
																		// we go
																		// if we
																		// don't
																		// know
																		// where
																		// they
																		// came
																		// from?
			if (!StringUtils.isNullOrEmpty(navZip)) 
				return "redirect:/locate/?navZip="+navZip + (ServerUtil.isNullOrEmpty(storeTracking)?"":("&"+storeTracking));
			else
				return "redirect:/locate/";
		
		returnUrl = URLDecoder.decode(returnUrl, "UTF-8");
		returnUrl = StringUtils.replaceParameterInUrl("storeNumber",storeNumber.toString(), returnUrl,true);
		if(ServerUtil.isNullOrEmpty(storeTracking)){
			if(isValidUrl(returnUrl)){
				// Util.debug("Check whitelist");
				if(onWhiteList(returnUrl)){
					return "redirect:" + returnUrl;
				}else{
					return "redirect:/locate/";
				}
			}else{
				return "redirect:" + returnUrl ;
			}
		}else
			return "redirect:" + returnUrl +(returnUrl.contains("?")?"":"?") + "&"+storeTracking;
	}
	
	// returnUrl should be http://www.firestonecompleteautocare.com
	private boolean onWhiteList(String returnUrl) {
		Util.debug("checking white list");
		
		Map <String,String> allSites = ServerUtil.getBSROSites();
		String approvedUrl = allSites.get(thisConfig.getSiteName().toUpperCase());
		
		if(returnUrl.toLowerCase().startsWith("http://") && approvedUrl != null){
			returnUrl = returnUrl.split("http://")[1];
			
			if(!ServerUtil.isNullOrEmpty(returnUrl) && returnUrl.toLowerCase().startsWith(approvedUrl)){
				return true;
			}else
				return false;
		}else
			return false;
	}

	private boolean isValidUrl(String returnUrl) {
		String[] schemes = {"http","https", "ftp", "ftps"};
	    UrlValidator urlValidator = new UrlValidator(schemes);
	    boolean isValid = false;
	    
	    if (urlValidator.isValid(returnUrl)) {
	        isValid = true;
	     } else {
	        isValid = false;
	     }
	    
	    return isValid;
	}

	
	@RequestMapping(value = "/store-detail/{storeNumber}", method = RequestMethod.GET)
	public String storeNumberOnly(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber, 
			@RequestParam(value = "cacheZip", required = false) String cacheZip, @RequestParam(value = "bvrrp", required = false) String bvrrp,
			@RequestParam(value = "lpage", required = false) String lpage) throws Exception {		
		String searchSuccess = request.getParameter("searchSuccess");
		StringBuffer errors = new StringBuffer();
		Store store = checkStore(storeNumber, errors);
		if(store==null){
			return "redirect:/locate/";
		}
		
		String types = getTypesForApp();
		if (types != null) {
			String[] arr = types.split(",");
			boolean matched = false;
			for (int idx = 0; idx < arr.length; idx++) {
				if (store.getStoreType().trim().equalsIgnoreCase(arr[idx].trim())) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				return "redirect:/locate/";
			}
		}
		
		if(!"true".equals(searchSuccess)){
			if(store.getActive() != 0){
				session.setAttribute(SessionConstants.PREFERRED_STORE, store);
				CacheDataUtils.setCachedZip(request, store.getZip());
			}else{
				session.setAttribute(SessionConstants.PREFERRED_STORE, null);
			}
		}
		
		model.addAttribute("nitrogenStores", getNitrogenStores());
		
		return storeDetail(model, request, storeNumber, null, cacheZip, bvrrp, lpage);
	}
	
	// links of this sort have been coming from bing, redirect to correct
	@RequestMapping(value = "/store-detail/{storeNumber}/www.firestonecompleteautocare.com/locate/route_request.jsp")
	public String storeNumberWithBrokenBingLink1(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber) throws Exception {
		return "redirect:/store-detail/"+storeNumber;
	}
	
	
	// links of this sort have been coming from bing, redirect to correct
	@RequestMapping(value = "/store-detail/{storeNumber}/www.firestonecompleteautocare.com/locate/www.firestonecompleteautocare.com/locate/route_request.jsp")
	public String storeNumberWithBrokenBingLink2(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber) throws Exception {
		return "redirect:/store-detail/"+storeNumber;
	}
	
	private void checkStoreAddress(String address, Store store) throws Exception {
		String storeAddress = StoreDetailLinkTag.generateStoreDetailLink(store);
		if(!storeAddress.equalsIgnoreCase(address)) {
			throw new ModelAndViewDefiningException(new ModelAndView(new PermanentRedirectView(storeAddress)));
		}
	}
	
	/**
	 * Some addresses have slashes in them. Slashes don't seem to be escapable in the usual fashion, so we need to handle it like so in order for Spring to recognize it.
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @param storeNumber
	 * @param address1
	 * @param address2
	 * @param cacheZip
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/store-detail/{storeNumber}/{address1}/{address2}", method = RequestMethod.GET)
	public String storeDetailGetStoreWithSlashInAddress(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber, @PathVariable final String address1, @PathVariable final String address2, @RequestParam(value = "cacheZip", required = false) String cacheZip, @RequestParam(value = "bvrrp", required = false) String bvrrp,
			@RequestParam(value = "lpage", required = false) String lpage) throws Exception {
		// we need to use an escaped slash here
		return storeDetailGet(model, request, response, session, storeNumber, address1+"%2F"+address2, cacheZip, bvrrp, lpage);
	}
	
	/**
	 * Thinking ahead, in case we ever see an address with two slashes in it. Slashes are an unusual case in general, hopefully it will not exceed two.
	 * @param model
	 * @param request
	 * @param response
	 * @param session
	 * @param storeNumber
	 * @param address1
	 * @param address2
	 * @param cacheZip
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/store-detail/{storeNumber}/{address1}/{address2}/{address3}", method = RequestMethod.GET)
	public String storeDetailGetStoreWithTwoSlashesInAddress(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber, @PathVariable final String address1, @PathVariable final String address2, @PathVariable final String address3, @RequestParam(value = "cacheZip", required = false) String cacheZip, 
			@RequestParam(value = "bvrrp", required = false) String bvrrp, @RequestParam(value = "lpage", required = false) String lpage) throws Exception {
		// we need to use an escaped slash here
		return storeDetailGet(model, request, response, session, storeNumber, address1+"%2F"+address2+"%2F"+address3, cacheZip, bvrrp, lpage);
	}	

	@RequestMapping(value = "/store-detail/{storeNumber}/{address}", method = RequestMethod.GET)
	public String storeDetailGet(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @PathVariable final String storeNumber, @PathVariable final String address, @RequestParam(value = "cacheZip", required = false) String cacheZip, 
			@RequestParam(value = "bvrrp", required = false) String bvrrp, @RequestParam(value = "lpage", required = false) String lpage)
			throws Exception {
		StringBuffer errors = new StringBuffer();
		Store store = checkStore(storeNumber, errors);
		if(store==null){
			return "redirect:/locate/";
		}
		String types = getTypesForApp();
		if (types != null) {
			String[] arr = types.split(",");
			boolean matched = false;
			for (int idx = 0; idx < arr.length; idx++) {
				if (store.getStoreType().trim().equalsIgnoreCase(arr[idx].trim())) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				return "redirect:/locate/";
			}
		}
		checkStoreAddress("/store-detail/"+storeNumber+"/"+URLEncoder.encode(URLDecoder.decode(address,"UTF-8"), "UTF-8"), store);

			if(store.getActive() != 0){
				session.setAttribute(SessionConstants.PREFERRED_STORE, store);
				CacheDataUtils.setCachedZip(request, store.getZip());
			}else{
				session.setAttribute(SessionConstants.PREFERRED_STORE, null);
			}
		
		model.addAttribute("nitrogenStores", getNitrogenStores());
		
		return storeDetail(model, request, storeNumber, null, cacheZip, bvrrp, lpage);
	}

	@RequestMapping(value = "/store-detail.htm", method = RequestMethod.GET)
	public String storeDetailGetHtm(Model model, HttpServletRequest request, HttpServletResponse response, HttpSession session, @RequestParam(value="storeNumber", required=false) String storeNumber,
			@RequestParam(value = "dayOfWeek", required = false) String dayOfWeek, @RequestParam(value = "cacheZip", required = false) String cacheZip, 
			@RequestParam(value = "bvrrp", required = false) String bvrrp, @RequestParam(value = "lpage", required = false) String lpage) throws Exception {
		
		if(ServerUtil.isNullOrEmpty(storeNumber))
			return "redirect:/store-list";
		
		return storeDetail(model, request, storeNumber, dayOfWeek, cacheZip, bvrrp, lpage);
	}
	
	private String getTypesForApp() {
		String types = "";		
		Map storeMap = storeDAO.getPrimaryStoreMap(thisConfig, null);
		Iterator i = storeMap.keySet().iterator();
		while(i.hasNext()) {
			String type = (String)i.next();
			types += type + ",";
		}
		Map secondaryMap = storeDAO.getSecondaryStoreMap(thisConfig);
		i = secondaryMap.keySet().iterator();
		while(i.hasNext()) {
			String type = (String)i.next();
			types += type + ",";
		}
		
		if (types.endsWith(",")) {
			types = types.substring(0, types.length()-1);
		}
		return types;
	}

	@SuppressWarnings("unchecked")
	private String storeDetail(Model model, HttpServletRequest request, String storeNumber, String dayOfWeek, String cacheZip, String bvrrp, String lpage) {
		StringBuffer errors = new StringBuffer();
		Store store = checkStore(storeNumber, errors);
		String pageURL = null;
		String baseURL = null;
		String uri = null;
		
		String types = getTypesForApp();
		if (types != null) {
			String[] arr = types.split(",");
			boolean matched = false;
			for (int idx = 0; idx < arr.length; idx++) {
				if (store.getStoreType().trim().equalsIgnoreCase(arr[idx].trim())) {
					matched = true;
					break;
				}
			}
			if (!matched) {
				return "redirect:/locate/";
			}
		}
		
		if (!ServerUtil.isNullOrEmpty(errors)) {
			model.addAttribute("errors", errors);
			// System.out.println("<p style='left:10px;margin:20px;'></p>");
			// TODO fix this
			return "locate/store-detail";
		}
		
		pageURL = "http://" + request.getHeader("Host");
		uri = (bvrrp == null) ? request.getRequestURI() : request.getRequestURI() + "/?bvrrp=" + bvrrp;
		int sindex = uri.indexOf("/store-detail");
		if (sindex != -1) {
			uri = uri.substring(sindex);
		}
		pageURL += uri;
		baseURL = pageURL;
		int qindex = baseURL.indexOf("?");
		if (qindex > 0) {
			baseURL = baseURL.substring(0, qindex);
		}
		model.addAttribute("bv.baseURL", baseURL);
		model.addAttribute("bv.pageURL", pageURL);
		
		store.setStoreHour(LocatorUtil.getStoreHourHTML(locatorDAO.getStoreHour(store.getNumber()), true));
		List<String> holidayHourMessages = createHolidayHoursMessage(store);
		// -- list of annoucements --//
		List<StoreAdminAnnouncement> announcements = storeAdminDAO.findAnnouncementsByStoreId(String.valueOf(store.getStoreNumber()));

		List<StoreAdminPromotionViewObject> filteredPromos = new ArrayList<StoreAdminPromotionViewObject>();
		List<StoreAdminPromotionViewObject> filteredBannerPromos = new ArrayList<StoreAdminPromotionViewObject>();
		List<StoreAdminPromotionViewObject> filteredPriorityPromos = new ArrayList<StoreAdminPromotionViewObject>();

		populatePromoLists(store, filteredPromos, filteredBannerPromos, filteredPriorityPromos);

		StoreAdminStoreImage storeImage = storeAdminDAO.findPublishedStoreAdminStoreImageByStoreNumber(storeNumber);

		// ------------------------- Set Omniture Tags Dynamically 20110622
		// -----------------------------//
		// --- omniture tags on 20110622 ---//
		model.addAttribute("s.pageName", "fcac:stores:" + store.getStoreNumber());
		model.addAttribute("ogtitle", store.getAddress() + ", " + store.getCity() + ", " + store.getState() + " " + store.getZip());
		model.addAttribute("ogtype", "company");
		model.addAttribute("ogimage", "/images-fcac/fcac-store-banner-fb.jpg");
		model.addAttribute("ogurl", request.getServerName() + "/store-" + request.getParameter("input"));

		model.addAttribute("store", store);
		model.addAttribute("storeImage", storeImage);

		model.addAttribute("announcements", announcements);

		model.addAttribute("filteredPromos", filteredPromos);
		model.addAttribute("filteredPriorityPromos", filteredPriorityPromos);
		model.addAttribute("filteredBannerPromos", filteredBannerPromos);
		model.addAttribute("holidayHourMessages", holidayHourMessages);
		model.addAttribute("offerType", PromotionDAO.TIRE);
		GregorianCalendar calendar = new GregorianCalendar();
		Set<StoreHourViewObject> storehours = createStoreHoursViewObjects(locatorDAO.getStoreHour(store.getNumber()));
		model.addAttribute("storeHours", storehours);
		String today = StoreHourViewObject.CALENDAR_DAY_ABBREV.get(calendar.get(Calendar.DAY_OF_WEEK));
		if (Boolean.parseBoolean(environmentProperties.get("allowBlackFridayDateOverride")) && !StringUtils.isNullOrEmpty(dayOfWeek)) {
			today = dayOfWeek;
		}
		model.addAttribute("today", today);
		if(cacheZip != null && Util.isValidZipCode(cacheZip))
			model.addAttribute("cacheZip", cacheZip);

		if ("MVAN".equalsIgnoreCase(store.getStoreType().trim()) && "MVAN".equalsIgnoreCase(lpage))
			return "locate/store-detail-mvan"; 
		else
			return "locate/store-detail";
	}

	public static class DayBean {
		private final int year;
		private final int month;
		private final int day;
		private final String dayName;
		
		public DayBean(Calendar cl, String dayName){
			this.year = cl.get(Calendar.YEAR);
			this.month = cl.get(Calendar.MONTH)+1;
			this.day = cl.get(Calendar.DAY_OF_MONTH);
			this.dayName = dayName;
		}

		public int getYear() {
			return year;
		}

		public int getMonth() {
			return month;
		}

		public int getDay() {
			return day;
		}
		
		public String getDayName() {
			return dayName;
		}

		@Override
		public String toString() {
			return String.format("DayBean [year=%s, month=%s, day=%s, dayName=%s]", year, month, day, dayName);
		}
	}

	private List<DayBean> getDayBeansForCurrentWeek() {
		List<DayBean> result = new ArrayList<DayBean>();
		Calendar cl = Calendar.getInstance();
		cl.setTimeInMillis(System.currentTimeMillis());
		int currentDay = cl.get(Calendar.DAY_OF_WEEK);
		cl.add(Calendar.DAY_OF_MONTH, 1-currentDay);
		for (int i = 1; i <= 7; i++) {
			result.add(new DayBean(cl, LocatorUtil.DAY_ABBREV[i-1]));
			cl.add(Calendar.DAY_OF_MONTH, 1);
		}
		return result;
	}
	
	private Set<StoreHourViewObject> createStoreHoursViewObjects(List<StoreHour> storeHours) {
		List<DayBean> dayBeans = getDayBeansForCurrentWeek();
		List<StoreHoliday> holidays = storeService.getStoreLocatorHolidays();
		boolean[] days = { false, false, false, false, false, false, false };
		Set<StoreHourViewObject> storeHourViewObjects = new TreeSet<StoreHourViewObject>();
		
		if(thisConfig.getUserType() == User.FCAC) {
			for(int i=0;i<storeHours.size();i++) {
				StoreHour storeHour = storeHours.get(i);
				if (!"Closed".equals(storeHour.getOpenTime()) && !"Closed".equals(storeHour.getCloseTime())) {
				StoreHourViewObject storeHourViewObject = new StoreHourViewObject(storeHour);
				
				int dayIndex = StoreHourViewObject.DAY_ORDER.get(storeHourViewObject.getDayOfWeek());
				DayBean dayBean = dayBeans.get(dayIndex);
				
				//Override working hours if it is necessary
				if (holidays != null && !holidays.isEmpty()) {
					for (StoreHoliday holiday : holidays) {
						if (dayBean.getYear() == holiday.getId().getYear().intValue() && 
							dayBean.getMonth() == holiday.getId().getMonth().intValue() && 
							dayBean.getDay() == holiday.getId().getDay().intValue()) {
							
							storeHourViewObject.setHoliday(holiday.getDescription());

							StoreHolidayHour hour = storeService.getStoreHolidayHour(storeHour.getId().getStoreNumber(), holiday.getHolidayId());
							if(hour != null) {
								storeHourViewObject.setOpenTime(LocatorUtil.format(hour.getOpenTime()));
								storeHourViewObject.setCloseTime(LocatorUtil.format(hour.getCloseTime()));
							}
							else{
								storeHourViewObject.setClosed(true);
							}
						}
					}
				}			
				//
				storeHourViewObjects.add(storeHourViewObject);
				days[dayIndex] = true;
				}
			}
		}
		else {
			for (StoreHour storeHour : storeHours) {
				StoreHourViewObject storeHourViewObject = new StoreHourViewObject(storeHour);
				storeHourViewObjects.add(storeHourViewObject);
				days[StoreHourViewObject.DAY_ORDER.get(storeHourViewObject.getDayOfWeek())] = true;
			}
		}
		
		int i = 1;
		for (boolean hasDay : days) {
			if (!hasDay) {
				StoreHourViewObject storeHourViewObject = new StoreHourViewObject(StoreHourViewObject.CALENDAR_DAY_ABBREV.get(i));
				storeHourViewObjects.add(storeHourViewObject);
			}
			i++;
		}
		return storeHourViewObjects;
	}

	@SuppressWarnings("unchecked")
	private void populatePromoLists(Store store, List<StoreAdminPromotionViewObject> filteredPromos, List<StoreAdminPromotionViewObject> filteredBannerPromos,
			List<StoreAdminPromotionViewObject> filteredPriorityPromos) {
		// List tirePromos =
		// tirePromotionDAO.getTirePromotionsBySitesAndStoreNumber(thisConfig.getSiteName(),
		// (long)store.getNumber());
		// -- list of promos --//
		List<StoreAdminPromotion> storeAdminPromotions = storeAdminDAO.findPromotionsByStoreId(String.valueOf(store.getStoreNumber()));

		List<Long> allCategoryIds = new ArrayList<Long>();
		if (storeAdminPromotions != null && storeAdminPromotions.size() > 0) {
			for (StoreAdminPromotion storeAdminPromotion : storeAdminPromotions) {
				StoreAdminPromotionViewObject storeAdminPromotionViewObject = new StoreAdminPromotionViewObject(storeAdminPromotion, lwTrackingTag);
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
							StoreAdminOfferTemplate template = storeAdminDAO.findOfferTemplateByIdAndBrand(offer.getTemplateId(), store.getStoreType().trim());
							if (template == null)
								continue;
							
							StoreAdminOfferTemplateImages templateImages = storeAdminDAO.findOfferTemplateImagesByIdAndBrand(offer.getTemplateId(), store.getStoreType().trim());

							StoreAdminOfferViewObject storeAdminOfferViewObject = new StoreAdminOfferViewObject(offer, template, templateImages);
							allOffers.add(storeAdminOfferViewObject);
							sortedOffers.add(storeAdminOfferViewObject);

							// this category is like oil change, tire, etc
							Long categoryId = template.getCategoryId();

							if (!allCategoryIds.contains(categoryId)) {
								allCategoryIds.add(categoryId);
								setDaysDisplayForPromo(storeAdminPromotionViewObject, offer);
								setEndDateForPromo(storeAdminPromotion, offer);

								if (!filteredPromos.contains(storeAdminPromotionViewObject) && "normal".equalsIgnoreCase(storeAdminPromotionViewObject.getStorePromo().getPromotionType())) {
									filteredPromos.add(storeAdminPromotionViewObject);
								} else if (!filteredBannerPromos.contains(storeAdminPromotionViewObject) && "banner".equalsIgnoreCase(storeAdminPromotionViewObject.getStorePromo().getPromotionType())) {
									filteredBannerPromos.add(storeAdminPromotionViewObject);
								} else if (!filteredPriorityPromos.contains(storeAdminPromotionViewObject)
										&& "priority".equalsIgnoreCase(storeAdminPromotionViewObject.getStorePromo().getPromotionType())) {
									filteredPriorityPromos.add(storeAdminPromotionViewObject);
								}
							}
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
			}
		}
	}

	private List<String> createHolidayHoursMessage(Store store) {
		List<String> holidayHourMessages = new ArrayList<String>();
		List<StoreHoliday> holidays = storeService.getStoreLocatorHolidays();
		if (holidays != null && !holidays.isEmpty()) {
			for (StoreHoliday holiday : holidays) {
				StoreHolidayHour hour = storeService.getStoreHolidayHour(store.getStoreNumber(), holiday.getHolidayId());
				String holidayMessage = storeService.getStoreHolidayMessage(holiday, hour);
				if(thisConfig.getUserType() == User.FCAC) {
					holidayHourMessages.add("<b>"+holiday.getDescription()+": </b>"+holidayMessage);
				}
				else{
					holidayHourMessages.add(holidayMessage);
				}
				
			}
		}
		return holidayHourMessages;
	}

	private Store checkStore(String storeNumber, StringBuffer error) {
		if (ServerUtil.isNullOrEmpty(storeNumber))
			error.append("No store found, please try again.");
		Store store = null;
		try {
			store = storeService.findStoreLightById(Long.valueOf(storeNumber));
		} catch (Exception e) {
		}

		if (store == null)
			error.append("Invalid store number, please try again.");

		if(thisConfig.getUserType() == User.FCAC) {
			//ignore temporary close error for store for FCAC application
		}
		else{
			if (store != null && store.getActiveFlag().intValue() == 0) {
				String reason = storeService.checkStoreReasonCode(store.getNumber());
				if (reason != null && reason.length() > 0)
					error.append(Encode.html(reason) + " Please check the <a href='/locate/'>Store Locator</a> for the nearest available location.");
				else
					error.append("Sorry, this location is closed for business. Please check the <a href='/locate/'>Store Locator</a> for the nearest available location.");
			}
		}
		return store;
	}

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

	@RequestMapping(value = "/store-list", method = RequestMethod.GET)
	public String listStoresGet(Model model, HttpServletRequest request, HttpSession session) throws Exception {
		return listStores(model, request);
	}

	@RequestMapping(value = "/locate/stores_all.htm", method = RequestMethod.GET)
	public String listStoresGetHtm(Model model, HttpServletRequest request, HttpSession session) throws Exception {
		return listStores(model, request);
	}

	private String listStores(Model model, HttpServletRequest request) {
		List<Store> stores = storeDAO.findSiteStoreLightList(thisConfig.getSiteName());
		if (stores != null && stores.size() > 0) {
			for (int i = 0; i < stores.size(); i++) {
				Store store = (Store)stores.get(i);
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locatorDAO.getStoreHour(store.getNumber()), true));
			}
		}
		model.addAttribute("stores", stores);
		return "locate/stores_all";
	}

	@RequestMapping(value = "/store-near/{cityState}", method = RequestMethod.GET)
	public String locateDisplayMapGet(Model model, HttpServletRequest request, HttpSession session, @PathVariable final String cityState) throws Exception {

		int index = cityState.lastIndexOf("-");
		if (index < 0) {
			return "redirect:/locate/";
		}

		String city = null;
		String state = null;
		try {
			city = cityState.substring(0, index).replace("-", " ");
			state = cityState.substring(index + 1);
		} catch (Exception e) {
			return "redirect:/locate/";
		}

		model.addAttribute("storeNear", "true");
		return displayMapExecute(model, request, session, null, city, state, null, null, null, null, null, null, null, null, null, null, null);
	}
	@RequestMapping(value = "/locate/{state}", method = RequestMethod.GET)
	public String locateStoresInState(Model model, HttpServletRequest request, HttpSession session, @PathVariable String state) throws Exception {

		String myState = null;
		if (!StringUtils.isNullOrEmpty(state)) {
			state = state.replace("-", " ");
			StoreSearch storeSearch = storeService.createStoreSearchObject(StringEscapeUtils.escapeHtml(state));
			myState = storeSearch.getState();
			model.addAttribute("state", StringEscapeUtils.escapeHtml(myState));
			model.addAttribute("navZip", StringEscapeUtils.escapeHtml(state));
		}
		return displayMapExecute(model, request, session, null, null, myState, null, null, null, null, null, null, null, null, null, null, null);
	}
	@RequestMapping(value = "/locate/display-map.htm", method = RequestMethod.GET)
	public String locateDisplayMapGet(Model model, HttpServletRequest request, HttpSession session, @RequestParam(value = "navZip", required = false) String navZip,
			@RequestParam(value = "zip", required = false) String zip, @RequestParam(value = "city", required = false) String city, @RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "address", required = false) String address, @RequestParam(value = "print", required = false) String print, @RequestParam(value = "p", required = false) String p,
			@RequestParam(value = "centerLat", required = false) String centerLat, @RequestParam(value = "centerLng", required = false) String centerLng,
			@RequestParam(value = "zoom", required = false) String zoom, @RequestParam(value = "popUp", required = false) String popUp,
			@RequestParam(value = "pIndex", required = false) String pIndex, @RequestParam(value = "maptype", required = false) String maptype,
			@RequestParam(value = "fromUrl", required = false) String fromUrl, @RequestParam(value = "fromPage", required = false) String fromPage)  throws Exception {
		//to get state List for index page
		List states = storeDAO.getStoreStates();
		request.setAttribute("_states_", states);
		
		if (!StringUtils.isNullOrEmpty(navZip)) {
			StoreSearch storeSearch = storeService.createStoreSearchObject(StringEscapeUtils.escapeHtml(navZip));
			zip = storeSearch.getZip();
			city = storeSearch.getCity();
			state = storeSearch.getState();
			model.addAttribute("navZip", StringEscapeUtils.escapeHtml(navZip));
			model.addAttribute("city", city);
			model.addAttribute("state", state);
		}

		// Util.debug("After STATE and Zip "+state+" "+zip);
		if ((state == null || state.length() == 0) && (zip == null || zip.length() == 0) && (navZip == null || navZip.length() == 0)) {
			Store store = (Store) session.getAttribute(SessionConstants.PREFERRED_STORE);
			if (store == null) {
				ipInfoService.setPreferredStoreByIp(request.getRemoteAddr(), session);
				store = (Store) session.getAttribute(SessionConstants.PREFERRED_STORE);
			}
			if (store != null) {
				zip = store.getZip();
				if (!ServerUtil.isNullOrEmpty(zip)) {
					if (zip.length() > 5)
						zip = zip.substring(0, 5);
				}
			}
		}
		if ((zip == null || zip.length() == 0) && (state == null || state.length() == 0)) {
			model.addAttribute("errors", "Please enter a valid Zip Code and State");
			model.addAttribute("zip", zip != null ? zip : "");
			model.addAttribute("city", city != null ? city : "");
			model.addAttribute("state", state != null ? state : "");
			model.addAttribute("address", address != null ? address : "");
			return "locate/index";
		} else if ((state == null || state.length() == 0) && zip != null && !Util.isValidZipCode(zip)) {
			model.addAttribute("zip", zip != null ? zip : "");
			model.addAttribute("city", city != null ? city : "");
			model.addAttribute("state", state != null ? state : "");
			model.addAttribute("address", address != null ? address : "");
			model.addAttribute("errors", "Please enter a valid Zip Code and State");
			return "locate/index";
		}
		return displayMapExecute(model, request, session, zip, city, state, address, print, p, centerLat, centerLng, zoom, popUp, pIndex, maptype, fromUrl, fromPage);

	}

	@RequestMapping(value = "/locate/display-map.htm", method = RequestMethod.POST)
	public String locateDisplayMapPost(Model model, HttpServletRequest request, HttpSession session, @RequestParam(value = "navZip", required = false) String navZip,
			@RequestParam(value = "zip", required = false) String zip, @RequestParam(value = "city", required = false) String city, @RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "address", required = false) String address, @RequestParam(value = "print", required = false) String print, @RequestParam(value = "p", required = false) String p,
			@RequestParam(value = "centerLat", required = false) String centerLat, @RequestParam(value = "centerLng", required = false) String centerLng,
			@RequestParam(value = "zoom", required = false) String zoom, @RequestParam(value = "popUp", required = false) String popUp,
			@RequestParam(value = "pIndex", required = false) String pIndex, @RequestParam(value = "maptype", required = false) String maptype,
			@RequestParam(value = "fromUrl", required = false) String fromUrl, @RequestParam(value = "fromPage", required = false) String fromPage) throws Exception {
		//to get state List for index page
		List states = storeDAO.getStoreStates();
		request.setAttribute("_states_", states);
		if (!StringUtils.isNullOrEmpty(navZip)) {
			StoreSearch storeSearch = storeService.createStoreSearchObject(StringEscapeUtils.escapeHtml(navZip));
			zip = storeSearch.getZip();
			city = storeSearch.getCity();
			state = storeSearch.getState();
			model.addAttribute("navZip", StringEscapeUtils.escapeHtml(navZip));
		}

		if ((zip == null || zip.length() == 0) && (state == null || state.length() == 0)) {
			model.addAttribute("errors", "Please enter a valid Zip Code and State");
			model.addAttribute("zip", zip != null ? zip : "");
			model.addAttribute("city", city != null ? city : "");
			model.addAttribute("state", state != null ? state : "");
			model.addAttribute("address", address != null ? address : "");
			return "locate/index";
		} else if ((state == null || state.length() == 0) && zip != null && !Util.isValidZipCode(zip)) {
			model.addAttribute("zip", zip != null ? zip : "");
			model.addAttribute("city", city != null ? city : "");
			model.addAttribute("state", state != null ? state : "");
			model.addAttribute("address", address != null ? address : "");
			model.addAttribute("errors", "Please enter a valid Zip Code and State");
			return "locate/index";
		}
		return displayMapExecute(model, request, session, zip, city, state, address, print, p, centerLat, centerLng, zoom, popUp, pIndex, maptype, fromUrl, fromPage);

	}

	private String displayMapExecute(Model model, HttpServletRequest request, HttpSession session, String zip, String city, String state, String address, String print, String p, String centerLat,
			String centerLng, String zoom, String popUp, String pIndex, String maptype, String fromUrl, String fromPage) {
		try {
			long startTime = System.currentTimeMillis();

			String app = thisConfig.getSiteName();
			String remoteIP = request.getRemoteAddr();
			long requestId = locator.getRequestId();

			// String navZip = (String)session.getAttribute("zip");
			// if((zip == null || zip.equals("")) && navZip != null &&
			// isValidZipCode(navZip))
			// zip = navZip;

			if (state == null || state.equals("")) {
				state = locationService.lookupStateWithStoresByZipCode(zip);
			}
			String geoPoint = (String) session.getAttribute("geoPoint");

			// If the only input value is the state, perform a state only lookup
			boolean isByState = false;
			if ((city == null || city.equals("")) && (address == null || address.equals("")) && (zip == null || zip.equals("")))
				isByState = true;
			
			// Create address description text
			String addressDescription = "";
			if (address != null && !address.equals(""))
				addressDescription += address + ", ";
			if (city != null && !city.equals(""))
				addressDescription += city + ", ";
			if (state != null && !state.equals(""))
				addressDescription += state + " ";
			if (zip != null && !zip.equals(""))
				addressDescription += zip;

			StoreDataBean storeDataBean = null;

			if (isByState) {
				storeDataBean = storeService.searchStoresByState(app, state, remoteIP, false, true, false, false, true, p);
			} else {
				storeDataBean = storeService.searchStoresByAddress(app, address, city, state, zip, 5, remoteIP, geoPoint, false, false, true, null, false, false, 30);
			}

			Float[] location = { storeDataBean.getLongitude(), storeDataBean.getLatitude() };
			
			model.addAttribute("isByState", isByState);
			// Util.debug("store size "+stores.length);
			model.addAttribute("stores", storeDataBean.getStoreList());
			model.addAttribute("location", location);
			model.addAttribute("distances", storeDataBean.getMappedDistance());
			model.addAttribute("addressDescription", addressDescription);
			model.addAttribute("state", state);
			model.addAttribute("zip", zip);

			// Load the values into the locator action base
			model.addAttribute("address", address);
			model.addAttribute("city", city);

			model.addAttribute("fromUrl", fromUrl);
			model.addAttribute("storesCount", storeDataBean.getStoresCount());

			return getMapPage(model, session, zip, (List<Store>) storeDataBean.getStoreList(), isByState, print, location, address, city, state, p, centerLat, centerLng, zoom, popUp, pIndex, maptype, fromPage, storeDataBean.getStoresCount());

		} catch (Exception ex) {
			model.addAttribute("errors", "An exception occured, please try again.");
			
			if(fromPage != null ){
				return "redirect:/locate/?searchSuccess=false&trackPage="+fromPage;
			}
			return "redirect:/locate/?searchSuccess=false";
		}
		// setSessionAttribute(SESSION_TIMEOUT_KEY, "");
	}

	private List<Store> searchStores(Model model, String state, String app, Float[] location, boolean isByState) {

		// Get the list of stores
		long[][] storeArray = null;
		if (isByState) {
			// location, app, licensee, radius, pricing, state
			// Util.debug("GETTING BY STATE");
			storeArray = locator.getClosestStores(null, app, true, 5, false, state);
		} else
			// location, app, licensee, radius, pricing, state, full,
			// licenseeType, count
			storeArray = locator.getClosestStores(location, app, true, 5, false, null, false, null, 30);

		List<Long> storeIds = new ArrayList<Long>();
		for (int i = 0; i < storeArray.length; i++)
			storeIds.add(storeArray[i][0]);

		List<Store> stores = null;
		// Did we get a list of store IDs back?
		if (storeArray != null && storeArray.length > 0) {

			// Go get the actual stores
			stores = storeService.findStoresLightByIds(storeIds);

			// Build the distances array and set the store hours
			if (stores != null && stores.size() > 0) {
				long[] distances = new long[0];
				distances = new long[stores.size()];
				int i = 0;
				for (Store store : stores) {
					distances[i] = storeArray[i][1];
					i++;
				}
			}
		}
		return stores;
	}

	private String getMapPage(Model model, HttpSession session, String zip, List<Store> stores, Boolean isByState, String print, Float[] location, String address, String city, String state, String p,
			String centerLat, String centerLng, String zoom, String popUp, String pIndex, String maptype, String fromPage, int storesCount) {

		try {
			addMilitaryStoreData(model, session);

			// if(checkVegasStores(model, session, zip))
			// return "redirect:/locations/las-vegas";


			boolean displayStores = false;
			boolean isLargeListMode = false;
			boolean isPrintMode = "1".equalsIgnoreCase(print);
			int offset = 0;
			int imageOffset = 0;
			int pageNumber = 1;
			int displayCount = 30;
			int loopBound = 30;
			int maxPages = 1;
			String parameters = "";

			if (stores != null && stores.size() > 0) {
				displayStores = true;
				if (isByState.booleanValue() && storesCount > 30)
					isLargeListMode = true;
				// --- 20100804 ERROR FIX---//
				if (location == null) {
					if(fromPage != null ){
						return "redirect:/locate/?searchSuccess=false&trackPage="+fromPage;
					}
					return "redirect:/locate/?searchSuccess=false";
				}
				maxPages = (int) Math.ceil((double) storesCount / (double) displayCount);
				if (maxPages == 0)
					maxPages = 1;
				if (address != null)
					parameters += "address=" + address;
				if (city != null)
					parameters += (parameters == "" ? "" : "&") + "city=" + city;
				if (state != null)
					parameters += (parameters == "" ? "" : "&") + "state=" + state;
				if (zip != null)
					parameters += (parameters == "" ? "" : "&") + "zip=" + zip;
				// Util.debug("display store before numbers :  p "+p+" max:"+
				// maxPages + " maptype:"+maptype
				// +" pIndex:"+pIndex+" zoom:"+zoom+" popUp:"+popUp
				// +" pageNumber:"+pageNumber +" displayCount:"+displayCount
				// +" offset:"+offset +" loopbound:"+loopBound
				// +" offset:"+offset);

				if (isLargeListMode) {
					// Util.debug("param p being set:"+ p);
					String pageParam = p;
					if (pageParam != null)
						pageNumber = Integer.valueOf(pageParam).intValue();
					if (pageNumber < 1)
						pageNumber = 1;
				} else {
					loopBound = 30;
					offset = 0;
				}
			}

			model.addAttribute("stores", stores);
			model.addAttribute("address", address);
			model.addAttribute("city", city);
			model.addAttribute("state", state);
			model.addAttribute("zip", zip);
			model.addAttribute("location", location);
			model.addAttribute("offset", offset);
			model.addAttribute("maxPages", maxPages);
			model.addAttribute("parameters", parameters);
			model.addAttribute("imageOffset", imageOffset);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("displayCount", displayCount);
			model.addAttribute("loopBound", loopBound);
			model.addAttribute("displayStores", displayStores);
			model.addAttribute("isLargeListMode", isLargeListMode);
			model.addAttribute("isPrintMode", isPrintMode);
			model.addAttribute("centerLat", centerLat);
			model.addAttribute("centerLng", centerLng);
			model.addAttribute("zoom", zoom);
			model.addAttribute("popUp", popUp);
			model.addAttribute("pIndex", pIndex);
			model.addAttribute("maptype", maptype);
			model.addAttribute("storesCount", storesCount);

			if(fromPage != null){
				model.addAttribute("trackPage", fromPage);
				model.addAttribute("searchSuccess", "true");
			}
			
			// Util.debug("display store after numbers :  p "+p+" max:"+
			// maxPages + " maptype:"+maptype
			// +" pIndex:"+pIndex+" zoom:"+zoom+" popUp:"+popUp
			// +" pageNumber:"+pageNumber +" displayCount:"+displayCount
			// +" offset:"+offset +" loopbound:"+loopBound +" offset:"+offset);
			// TODO drag items from map page in
			boolean validZipCode = true; // put a user friendly message
			if (!displayStores && !com.bfrc.framework.util.ServerUtil.isNullOrEmpty(zip)) {
				try {
					//WI-1977: replace google geocoding calls with bing api calls
					//Map geoData = geocodeOperator.getGoogleGeoData(null, null, null, zip);
					Map geoData = geocodeOperator.getBingGeoLocationData(null, null, null, zip);
					if (!"200".equals(geoData.get("statusCode"))) {
						validZipCode = false;
					}
				} catch (Exception ex) {

				}
			}
			model.addAttribute("validZipCode", validZipCode);
			
			//Assign the first valid promotion found for a store in its currentPromotionTitle field. (WI - 1753)
			for (Store store:stores) {
				store.setCurrentPromotionTitle(getFirstPromoTitle(store));
			}
		} catch (Exception e) {
			e.printStackTrace();
			if(fromPage != null ){
				return "redirect:/locate/?searchSuccess=false&trackPage="+fromPage;
			}
			return "redirect:/locate/?searchSuccess=false";
		}
		return "locate/map";
	}

	private void addMilitaryStoreData(Model model, HttpSession session) {
		model.addAttribute("storeMilitaryNumbers", storeService.getPipeDelimitedMilitaryStoreNumbers());
		model.addAttribute("storeMilitary_Disclaimer", "This location is only available to customers with military exchange privileges.");
		model.addAttribute("storeMilitaryDisclaimer", "This location only serves customers with military exchange privileges.");
	}

	private boolean checkVegasStores(Model model, HttpSession session, String zip) {
		// String
		// zipCodes="89002|89005|89011|89012|89015|89018|89019|89030|89031|89032|89033|89044|89052|89054|89074|89081|89084|89085|89086|89101|89102|89103|89104|89106|89107|89108|89109|89110|89113|89115|89117|89118|89119|89120|89121|89122|89123|89124|89128|89129|89130|89131|89134|89135|89138|89139|89141|89142|89144|89145|89146|89147|89148|89149|89156|89161|89165|89166|89169|89183|89191";

		BsroGrandOpening item = (BsroGrandOpening) baseGrandOpeningDAO.findUniqueBy("friendlyId", "lasvegas_store_zipcode_info");
		String zipCodes = item.getDetailContent();

		zipCodes = zipCodes.replace("&nbsp;", "");
		zipCodes = zipCodes.replaceAll("\\<.*?\\>", "");

		// com.bfrc.framework.util.Util.debug("=========zipCodes ================================"+zipCodes);
		return (zip != null && !zip.equals("") && zipCodes.contains(zip));
	}

	// @RequestMapping(value = "/mapFrame.htm", method = RequestMethod.GET)
	// public String redrawMapFrameAction(Model model, HttpServletRequest
	// request,HttpServletResponse response, HttpSession session,
	// @RequestParam(value="zoom", required=false) String zoomParam,
	// @RequestParam(value="move", required=false) String directionParam,
	// @RequestParam(value="displayCount", required=false) String storesPerPage
	// ) throws Exception {
	//
	// String app =
	// Config.locate(request.getSession().getServletContext()).getSiteName();
	// com.bfrc.pojo.store.Map map =
	// (com.bfrc.pojo.store.Map)session.getAttribute(STORE_LOCATOR_PREFIX +
	// "map.LatestMap");
	// String remoteIP = request.getRemoteAddr();
	// String street = map.getStreet();
	// String city = map.getCity();
	// String state = map.getState();
	// String zip = map.getZip();
	// int displayCount = map.getDisplayCount();
	// if(storesPerPage != null)
	// displayCount = Integer.valueOf(storesPerPage).intValue();
	// String[] storeHTML = map.getStoreHTML();
	// int[] distance = map.getDistance();
	// try {
	// HashMap param = new HashMap();
	// param.put("operation", "getRequestId");
	// locator.operate(param);
	// long id = ((Long)param.get("result")).longValue();
	// param.put("id", new Long(id));
	// if(directionParam != null)
	// param.put("direction", directionParam);
	// else if(zoomParam != null)
	// param.put("zoom", Integer.valueOf(zoomParam));
	// param.put("app", app);
	// param.put("currMap", map);
	// param.put("street", street);
	// param.put("city", city);
	// param.put("state", state);
	// param.put("zip", zip);
	// param.put("location", map.getLocation());
	// param.put("stores", map.getStores());
	// param.put("offset", new Integer(map.getOffset()));
	// param.put("displayCount", new Integer(displayCount));
	// param.put("remoteIP", remoteIP);
	// param.put("operation", "getMap");
	// locator.operate(param);
	// map = (com.bfrc.pojo.store.Map)param.get("result");
	// } catch(Exception ex) {
	// ex.printStackTrace();
	// model.addAttribute("errors",
	// "there was an issue adding values to param map");
	// return "redirect:/locate/mapTimeout.htm";
	// }
	// map.setStoreHTML(storeHTML);
	// map.setDistance(distance);
	// setResponseCache(response);
	// session.setAttribute(STORE_LOCATOR_PREFIX + "map.LatestMap", map);
	// model.addAttribute(STORE_LOCATOR_PREFIX + "map.LatestMap", map);
	// return "locate/mapImageTag";
	// }

	private void setResponseCache(HttpServletResponse response) {
		response.setHeader("Cache-Control", "no-store"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0); // prevents caching at the proxy
												// server
	}

	// @RequestMapping(value = "/redrawMap.htm", method = RequestMethod.GET)
	// public String redrawMapAction(Model model, HttpServletRequest
	// request,HttpServletResponse response, HttpSession session,
	// @RequestParam(value="print", required=false) String print,
	// @RequestParam(value="offset", required=false) String offsetParam,
	// @RequestParam(value="fetchMap", required=false) String fetchMap,
	// @RequestParam(value="zoom", required=false) String zoomParam,
	// @RequestParam(value="displayCount", required=false) String storesPerPage,
	// @RequestParam(value="p", required=false) String p,
	// @RequestParam(value="centerLat", required=false) String centerLat,
	// @RequestParam(value="centerLng", required=false) String centerLng,
	// @RequestParam(value="popUp", required=false) String popUp,
	// @RequestParam(value="pIndex", required=false) String pIndex ,
	// @RequestParam(value="maptype", required=false) String maptype ) throws
	// Exception {
	//
	// try{
	// String app =
	// Config.locate(request.getSession().getServletContext()).getSiteName();
	// com.bfrc.pojo.store.Map map =
	// (com.bfrc.pojo.store.Map)session.getAttribute(STORE_LOCATOR_PREFIX +
	// "map.Map");
	// if(map == null)
	// return "redirect:/locate/index.htm";
	// int offset = 0;
	// if(offsetParam != null)
	// offset = Integer.valueOf(offsetParam).intValue();
	// if(offset < 0)
	// offset = 0;
	// int zoom = -1;
	// HashMap param = new HashMap();
	// if(zoomParam != null) {
	// zoom = Integer.valueOf(zoomParam).intValue();
	// if(zoom > -1) {
	// param.put("zoomStore", new Integer(zoom));
	// fetchMap = "1";
	// }
	// }
	// Float[] location = map.getLocation();
	// Store[] stores = map.getStores();
	// String[] storeHTML = map.getStoreHTML();
	// int[] distance = map.getDistance();
	// String remoteIP = request.getRemoteAddr();
	// String street = map.getStreet();
	// String city = map.getCity();
	// String state = map.getState();
	// String zip = map.getZip();
	// int displayCount = map.getDisplayCount();
	// if(storesPerPage != null)
	// displayCount = Integer.valueOf(storesPerPage).intValue();
	// try {
	// param.put("operation", "getRequestId");
	// locator.operate(param);
	// long id = ((Long)param.get("result")).longValue();
	// param.put("id", new Long(id));
	// param.put("app", app);
	// param.put("street", street);
	// param.put("city", city);
	// param.put("state", state);
	// param.put("zip", zip);
	// param.put("location", location);
	// param.put("stores", stores);
	// param.put("offset", new Integer(offset));
	// param.put("displayCount", new Integer(displayCount));
	// param.put("remoteIP", remoteIP);
	// param.put("fetchMap", fetchMap);
	// param.put("operation", "getMap");
	// locator.operate(param);
	// map = (com.bfrc.pojo.store.Map)param.get("result");
	// } catch(Exception ex) {
	// ex.printStackTrace();
	// model.addAttribute("errors",
	// "there was an issue adding values to param map");
	// return "redirect:/locate/timeout.htm";
	// }
	// map.setStoreHTML(storeHTML);
	// map.setDistance(distance);
	// if(zoom > -1)
	// model.addAttribute(STORE_LOCATOR_PREFIX + "map.StoreMap", map);
	// else {
	// session.setAttribute(STORE_LOCATOR_PREFIX + "map.Map", map);
	// model.addAttribute(STORE_LOCATOR_PREFIX + "map.Map", map);
	// }
	// boolean isByState = false;
	// if ((city == null || city.equals("")) && (street == null ||
	// street.equals("")) && (zip == null || zip.equals("")))
	// isByState = true;
	//
	// return getMapPage(model,session, zip, stores, isByState, print, location,
	// street, city, state, p, centerLat, centerLng, zoomParam,popUp, pIndex,
	// maptype);
	// }catch(Exception e){
	// e.printStackTrace();
	// return "redirect:/locate/index.htm";
	// }
	// }
	//
	@RequestMapping(value = "/locate/get-directions.htm", method = RequestMethod.GET)
	public String getDirections(Model model, HttpServletRequest request, HttpSession session, @RequestParam(value = "storeNumber", required = false) Long storeNumber,
			@RequestParam(value = "navZip", required = false) String navZip, @RequestParam(value = "directionsFromZip", required = false) String zip, @RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "state", required = false) String state, @RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "print", required = false) String print, @RequestParam(value = "zoom", required = false) String zoom,
			@RequestParam(value = "maptype", required = false) String maptype) throws Exception {

		if (storeNumber == null)
			return "redirect:/locate/";

		if(request.getParameter("zip")!=null){
			zip=request.getParameter("zip");
		}
		if(zip==null||zip.isEmpty()){
			try{
				if(navZip!=null&&!navZip.isEmpty()){	
					StoreSearch storeSearch = storeService.createStoreSearchObject(navZip);
					zip = storeSearch.getZip();
				}
				if(zip==null||zip.isEmpty()){
					if(CacheDataUtils.getCachedZip(request)!=null){
						zip=CacheDataUtils.getCachedZip(request);
					}
				}
			}catch(Exception e)
			{
				/*do nothing*/
			}
		}
		city = Encode.encodeForJavaScript(city);
		address = Encode.encodeForJavaScript(address);
		if(zip==null||zip.isEmpty()){
			if(session.getAttribute(SessionConstants.DIRECTIONS_FROM_ZIP)!=null){
				zip=(String)session.getAttribute(SessionConstants.DIRECTIONS_FROM_ZIP);
			}
		}else{
			if(zip!=null&&!zip.isEmpty()&&Util.isValidZipCode(zip)){
				zip = Encode.encodeForJavaScript(zip);
				session.setAttribute(SessionConstants.DIRECTIONS_FROM_ZIP, zip);
			}
		}		
		zip = Encode.encodeForJavaScript(zip);
		if(zip!=null&&!zip.isEmpty()){
			String tempState=null;
			tempState = timeZoneDstService.getStateByZip(zip);
			if(tempState!=null&&!tempState.isEmpty())
				state=tempState;
		}
		if(state==null||state.isEmpty()){
			if(session.getAttribute(SessionConstants.DIRECTIONS_FROM_STATE)!=null)
				state=(String)session.getAttribute(SessionConstants.DIRECTIONS_FROM_STATE);
		}else{
			session.setAttribute(SessionConstants.DIRECTIONS_FROM_STATE, state);
		}
//removing caching for city and address as there are none in prod.
//		if(city==null||city.isEmpty()){
//			if(session.getAttribute(SessionConstants.DIRECTIONS_FROM_CITY)!=null)
//				city=(String)session.getAttribute(SessionConstants.DIRECTIONS_FROM_CITY);
//		}else{
//			session.setAttribute(SessionConstants.DIRECTIONS_FROM_CITY, city);
//		}
//		if(address==null||address.isEmpty()){
//			if(session.getAttribute(SessionConstants.DIRECTIONS_FROM_ADDRESS)!=null)
//				address=(String)session.getAttribute(SessionConstants.DIRECTIONS_FROM_ADDRESS);
//		}else{
//			session.setAttribute(SessionConstants.DIRECTIONS_FROM_ADDRESS, address);
//		}
		if ((city == null || city.equals("")) && (state == null || state.equals("")) && (zip == null || zip.equals("")) && (address == null || address.equals(""))) {
			model.addAttribute("errors", "Please enter your full address.");
		}

		model.addAttribute("directionsFromZip", zip != null ? zip : "");
		model.addAttribute("city", city != null ? city : "");
		model.addAttribute("state", state != null ? state : "");
		model.addAttribute("address", address != null ? address : "");
		model.addAttribute("nitrogenStores", getNitrogenStores());

		addMilitaryStoreData(model, session);

		long startTime = System.currentTimeMillis();
		String app = thisConfig.getSiteName();
		String remoteIP = request.getRemoteAddr();
		long requestId = locator.getRequestId();

		Store store = null;
		try {
			store = storeService.findStoreLightById(storeNumber);
			//store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
			// Util.debug("store : " + store);
			// long id, String app, String remoteIP, String type, long interval,
			// String street, String city, String state, String zip, int
			// storeNumber			
		} catch (Exception ex) {
			ex.printStackTrace();
			model.addAttribute("errors", "Please enter your full address.");
			model.addAttribute("storeNumber", storeNumber);
			return "locate/directions";
		}
		model.addAttribute("destStore", store);
		//session.setAttribute(SessionConstants.PREFERRED_STORE, store);
		if(!StringUtils.isNullOrEmpty(zip))
			CacheDataUtils.setCachedZip(request, zip);
		else
			CacheDataUtils.setCachedZip(request, store.getZip());
		model.addAttribute("storeNumber", storeNumber);
		model.addAttribute("isPrintMode", "1".equals(print));
		String parameters = "";
		String fullAddress = "";
		if (address != null){
			parameters += "address=" + java.net.URLEncoder.encode(address, "UTF-8");
			fullAddress += address + " ";
		}
		if (city != null){
			parameters += (parameters == "" ? "" : "&") + "city=" + java.net.URLEncoder.encode(city, "UTF-8");
			fullAddress += city + " ";
		}
		if (state != null){
			parameters += (parameters == "" ? "" : "&") + "state=" + java.net.URLEncoder.encode(state, "UTF-8");
			fullAddress += state + " ";
		}
		if (zip != null || navZip != null){
			parameters += (parameters == "" ? "" : "&") + "zip=" + java.net.URLEncoder.encode(zip == null ? navZip : zip, "UTF-8");
			fullAddress += (zip == null ? navZip : zip);
		}
		if (storeNumber != null)
			parameters += (parameters == "" ? "" : "&") + "storeNumber=" + storeNumber;

		fullAddress = fullAddress.trim();

		// Create address description text
		String addressDescription = "";
		if (address != null && !address.equals(""))
			addressDescription += address + ", ";
		if (city != null && !city.equals(""))
			addressDescription += city + ", ";
		if (state != null && !state.equals(""))
			addressDescription += state + " ";
		if (zip != null && !zip.equals(""))
			addressDescription += zip;

		model.addAttribute("fullAddress", fullAddress);
		model.addAttribute("addressDescription", addressDescription);
		model.addAttribute("parameters", parameters);
		return "locate/directions";
	}

	@RequestMapping("/locate/change-store.htm")
	public String displaySelectStoreForStoreSearch(HttpServletRequest request, HttpSession session) {
		StoreWidgetDataBean storeWidgetDataBean = null;

		String zip = CacheDataUtils.getCachedZip(request, "zip");
		String geoPoint = Encode.html(request.getParameter("geoPoint"));
		String event = Encode.html(request.getParameter("event"));
		request.setAttribute("s.events", event);
		String selectedStoreNumberString = Encode.html(request.getParameter("storeNumber"));

		boolean refreshData = true;

		if (session.getAttribute("store.storeWidget") != null) {
			refreshData = false;
			storeWidgetDataBean = (StoreWidgetDataBean) session.getAttribute("store.storeWidget");
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
		}

		if (refreshData) {
			try {
				// Dont pass the selected store number as it will only return a
				// single store, we want the full list.
				// We set the selected store bean after that list comes back.
				initializeStoreStoreWidget(request, session, null, zip, geoPoint);
				storeWidgetDataBean = (StoreWidgetDataBean) session.getAttribute("store.storeWidget");
			} catch (Exception exception) {
			}
		}

		if (storeWidgetDataBean != null) {
			try {
				if (selectedStoreNumberString != null) {
					Store selectedStore = storeDAO.findStoreLightById(Long.valueOf(selectedStoreNumberString));
					// Util.debug(" stores found: " + selectedStore);
					if (selectedStore != null) {
						selectedStore.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(selectedStore.getStoreNumber()), true));
						storeWidgetDataBean.setSelectedStore(selectedStore);
					}
				}
				storeWidgetDataBean.moveSelectedStoreToFront();
			} catch (Exception e) {
			}
		}

		request.setAttribute("storeWidget", storeWidgetDataBean);

		return "common/modals/select-store";
	}

	private void initializeStoreStoreWidget(HttpServletRequest request, HttpSession session, String selectedStoreNumberString, String zip, String geoPoint) throws Exception {
		Long selectedStoreNumber = null;

		if (selectedStoreNumberString != null) {
			selectedStoreNumber = Long.valueOf(selectedStoreNumberString);
		}

		StoreWidgetDataBean storeWidgetDataBean = storeService.initializeStoreWidget("stores", selectedStoreNumber, thisConfig.getSiteName(), null, null, null, zip, null, request.getRemoteAddr(),
				geoPoint, false, true, true, null, true, false, 10);

		session.setAttribute("store.storeWidget", storeWidgetDataBean);
		request.setAttribute("storeWidget", storeWidgetDataBean);
	}
	public String[] createGoogleTrackingEntry(String category, String action, String label){
		String[] trackingEvent = new String[5];
		trackingEvent[0]=category;
		trackingEvent[1]=action;
		trackingEvent[2]=label;
		return trackingEvent;
	}
	
	// Get the valid first promotion for a specific store
	public String getFirstPromoTitle(Store store) {
		List<StoreAdminPromotion> storeAdminPromotions = storeAdminDAO.findPromotionsByStoreId(String.valueOf(store.getStoreNumber()));

		if (storeAdminPromotions != null && storeAdminPromotions.size() > 0) {
			for (StoreAdminPromotion storeAdminPromotion : storeAdminPromotions) {
				
				if (storeAdminPromotion.getOffers() != null && storeAdminPromotion.getOffers().size() > 0) {
					String storeAdminPromotionTitle = storeAdminPromotion.getTitle();

					if ("normal".equalsIgnoreCase(storeAdminPromotion.getPromotionType())) {
						return storeAdminPromotionTitle;
					}
				}
			}
		}
		return null;
	}

}
