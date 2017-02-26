package com.bsro.filter.store;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import com.bfrc.UserSessionData;
import com.bfrc.framework.util.CacheDataUtils;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.UserProfileUtils;
import com.bfrc.pojo.store.Store;
import com.bfrc.security.Encode;
import com.bsro.constants.CookieConstants;
import com.bsro.constants.SessionConstants;
import com.bsro.service.geoip.IpInfoService;
import com.bsro.service.store.StoreService;

public class StoreFilter implements Filter {

	private FilterConfig filterConfig;

	private List<String> batteryUrls;
	private List<String> alignmentUrls;
	private List<String> tireUrls;
	private List<String> appointmentUrls;
	private String batteryDestinationUrl;
	private String alignmentDestinationUrl;
	private String tireDestinationUrl;
	private String appointmentDestinationUrl;
	private String defaultDestinationUrl;
	private String errorDestinationUrl;

	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public List<String> getBatteryUrls() {
		return batteryUrls;
	}

	public void setBatteryUrls(List<String> batteryUrls) {
		this.batteryUrls = batteryUrls;
	}

	public List<String> getAlignmentUrls() {
		return alignmentUrls;
	}

	public void setAlignmentUrls(List<String> alignmentUrls) {
		this.alignmentUrls = alignmentUrls;
	}

	public List<String> getTireUrls() {
		return tireUrls;
	}

	public void setTireUrls(List<String> tireUrls) {
		this.tireUrls = tireUrls;
	}

	public List<String> getAppointmentUrls() {
		return appointmentUrls;
	}

	public void setAppointmentUrls(List<String> appointmentUrls) {
		this.appointmentUrls = appointmentUrls;
	}

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	private final Log logger = LogFactory.getLog(StoreFilter.class);

	@Autowired
	private StoreService storeService;
	@Autowired
	IpInfoService ipInfoService;

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		try {

			HttpServletRequest httpRequest = (HttpServletRequest) req;
			HttpServletResponse httpResponse = (HttpServletResponse) res;

			if (logger.isInfoEnabled())
				logger.info("In the StoreFilter Referer:" + httpRequest.getHeader("Referer"));

			loadStoreData(httpRequest, httpResponse);
			setChangeStore(httpRequest, httpResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
		chain.doFilter(req, res);
	}

	private void setChangeStore(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		try {
			Store store = (Store) WebUtils.getSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE);
			String zipToUse = CacheDataUtils.getCachedZip(httpRequest);
			if ((zipToUse == null || zipToUse.length() == 0) && store != null && store.getZip() != null && store.getZip().length() >= 5) {
				zipToUse = store.getZip().substring(0, 5);
				// if there isn't already a cached zip for use in the funnels,
				// set it to this
				CacheDataUtils.setCachedZip(httpRequest, zipToUse);
			}
			httpRequest.setAttribute("cachedZip", zipToUse);
			httpRequest.setAttribute("changeStoreUrl", errorDestinationUrl + (store == null ? "" : "?navZip=" + zipToUse + "&amp;storeNumber=" + store.getNumber()));

			String navZipSTring = "";
			if (store != null && store.getZip() != null) {
				navZipSTring = "?zip=" + zipToUse + "&storeNumber=" + store.getNumber();
			}
			String queryString = navZipSTring;
			if (httpRequest.getQueryString() != null) {
				queryString = "?" + httpRequest.getQueryString();
				queryString = StringUtils.replaceParameterInUrl("zip", zipToUse, queryString, false);
				queryString = StringUtils.replaceParameterInUrl("navZip", zipToUse, queryString, false);
			}

			String url = httpRequest.getRequestURL().toString().toLowerCase();
			String changeStoreUrl = defaultDestinationUrl;
			boolean found = false;
			if (url != null) {
				if (batteryUrls != null) {
					for (String batteryURL : batteryUrls) {
						if (url.contains(batteryURL)) {
							changeStoreUrl = batteryDestinationUrl;
							found = true;
						}
					}
				}
				if (alignmentUrls != null && !found) {
					for (String alignmentUrl : alignmentUrls) {
						if (url.contains(alignmentUrl)) {
							changeStoreUrl = alignmentDestinationUrl;
							queryString += "&mode=alignment";
							found = true;
						}
					}
				}
				if (appointmentUrls != null && !found) {
					for (String appointmentUrl : appointmentUrls) {
						if (url.contains(appointmentUrl)) {
							// we dont need a change store Url or query string
							// if in the funnel
							// we'll set the index page here and just use that
							// one.
							if (url.contains("index")) {
								changeStoreUrl = appointmentDestinationUrl;
							} else {
								changeStoreUrl = "";
								queryString = "";
								httpRequest.setAttribute("isFromAppointmentFunnel", true);
							}
							found = true;
						}
					}
				}
				if (tireUrls != null && !found) {
					for (String tireUrl : tireUrls) {
						if (url.contains(tireUrl)) {
							changeStoreUrl = tireDestinationUrl;
							found = true;
						}
					}
				}
				queryString = java.net.URLDecoder.decode(queryString.toString(),"UTF-8");
				String str = "";
				if(queryString.length() > 1){
					str = queryString.substring(1);
				}
				
				StringBuffer queryStr = new StringBuffer();
				StringTokenizer st  = new StringTokenizer(str, "&");
				//System.out.println(st);
				String token = "";
				String[] strArray = new String[2];
				while(st.hasMoreTokens()){
					token = st.nextToken();
					strArray = token.split("=");	
					if(strArray.length>0){
						queryStr.append(Encode.encodeForJavaScript(strArray[0])+"=");
					}					
					if(strArray.length>1){
						queryStr.append(Encode.encodeForJavaScript(strArray[1])+"&");
					}else if(strArray.length !=0){
						queryStr.append("&");
					}				
				}
				if(queryStr.length() > 1){
					queryString = "?"+ queryStr.toString().substring(0,queryStr.length()-1);
				}
				String replacedLink = (changeStoreUrl + queryString).replace("&", "&amp;");
				// Util.debug("changeStoreUrl " + replacedLink);
				httpRequest.setAttribute("changeStoreUrl", replacedLink);

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public String getBatteryDestinationUrl() {
		return batteryDestinationUrl;
	}

	public void setBatteryDestinationUrl(String batteryDestinationUrl) {
		this.batteryDestinationUrl = batteryDestinationUrl;
	}

	public String getAlignmentDestinationUrl() {
		return alignmentDestinationUrl;
	}

	public void setAlignmentDestinationUrl(String alignmentDestinationUrl) {
		this.alignmentDestinationUrl = alignmentDestinationUrl;
	}

	public String getTireDestinationUrl() {
		return tireDestinationUrl;
	}

	public void setTireDestinationUrl(String tireDestinationUrl) {
		this.tireDestinationUrl = tireDestinationUrl;
	}

	public String getAppointmentDestinationUrl() {
		return appointmentDestinationUrl;
	}

	public void setAppointmentDestinationUrl(String appointmentDestinationUrl) {
		this.appointmentDestinationUrl = appointmentDestinationUrl;
	}

	public String getDefaultDestinationUrl() {
		return defaultDestinationUrl;
	}

	public void setDefaultDestinationUrl(String defaultDestinationUrl) {
		this.defaultDestinationUrl = defaultDestinationUrl;
	}

	public String getErrorDestinationUrl() {
		return errorDestinationUrl;
	}

	public void setErrorDestinationUrl(String errorDestinationUrl) {
		this.errorDestinationUrl = errorDestinationUrl;
	}
	
	private void setStoreCookie (HttpServletResponse httpResponse, String cookieValue) {
		Cookie cookie = new Cookie(CookieConstants.PREFERRED_STORE_NUMBER, cookieValue);
		cookie.setPath("/");
		cookie.setMaxAge(CookieConstants.EXPIRE_TIME);
		httpResponse.addCookie(cookie);
	}

	private void loadStoreData(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

		Store store = (Store) WebUtils.getSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE);
		
		UserSessionData usd = UserProfileUtils.getUserSessionData(httpRequest.getSession());

		if (store == null) {
			try {
				String storeNumber = (String) WebUtils.getSessionAttribute(httpRequest, SessionConstants.STORE_NUMBER);
				if (storeNumber == null) {
					Cookie preferredStoreCookie = WebUtils.getCookie(httpRequest, CookieConstants.PREFERRED_STORE_NUMBER);
					if (preferredStoreCookie != null && preferredStoreCookie.getValue() != null && preferredStoreCookie.getValue().length() > 0) {
						storeNumber = preferredStoreCookie.getValue();
					}
					if (storeNumber == null) {
						ipInfoService.setPreferredStoreByIp(httpRequest.getRemoteAddr(), httpRequest.getSession());
						store = (Store) WebUtils.getSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE);
						if (store != null) {
							storeNumber = store.getStoreNumber().toString();
						}
					}
				}
				if (storeNumber == null)
					return;

				store = storeService.findStoreLightById(Long.parseLong(storeNumber));

				if(store.getActive() != 0) {
					WebUtils.setSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE, store);
					// Util.debug("store set in storeFilter");
					setStoreCookie(httpResponse, store.getStoreNumber().toString());
				}
				else{
					WebUtils.setSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE, null);
					usd.setStoreNumber(null);
					usd.setStoreNumbers(null);
					setStoreCookie(httpResponse, null);
				}
			} catch (Exception e) {
				// e.printStackTrace(); //Commenting out so if the store number
				// is not real, we just return
				setStoreCookie(httpResponse, null);
			}
		} else {
			//check store for temporaryClosed
			store = storeService.findStoreLightById(store.getStoreNumber());
			if(store.getActive() == 0) {
				//current store is temporary closed
				WebUtils.setSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE, null);
				setStoreCookie(httpResponse, null);
				usd.setStoreNumber(null);
				usd.setStoreNumbers(null);
				return;
			}
			
			boolean setSessionStore = false;
			
			boolean militaryBool = storeService.isMilitaryStore(store.getStoreNumber());
			if(militaryBool != store.getIsMilitaryStore()){
				store.setMilitaryStore(militaryBool);
				setSessionStore = true;
			}
			
			List<String> holidayHours = storeService.getStoreHolidayHours(store);
			if(holidayHours!=null && 
					(store.getHolidayHourMessages()==null ||
					 store.getHolidayHourMessages().size()!=holidayHours.size())){
				store.setHolidayHourMessages(holidayHours);
				setSessionStore = true;
			}
			
			if(setSessionStore){
				WebUtils.setSessionAttribute(httpRequest, SessionConstants.PREFERRED_STORE, store);
			}
			
			boolean cookieAlreadySet = false;
			
			Cookie[] cookies = httpRequest.getCookies();

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(CookieConstants.PREFERRED_STORE_NUMBER)) {
						String cookieStoreNumber = cookie.getValue();
						if (cookieStoreNumber != null && cookieStoreNumber.equals(store.getStoreNumber().toString())) {
							cookieAlreadySet = true;
						}
					}
				}
			}
			
			
			if (!cookieAlreadySet) {
				logger.info("Store number cookie is not already set, so set it");
				setStoreCookie(httpResponse, store.getStoreNumber().toString());
			} else {
				logger.info("Store number cookie is already set, do nothing");
			}
			// Util.debug("store set in storeFilter");

			if (logger.isInfoEnabled())
				logger.info("a store was found in the session: " + store.getStoreNumber());
		}
	}

}
