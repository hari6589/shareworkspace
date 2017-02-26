package com.bsro.service.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bfrc.Config;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bsro.databean.store.StoreDataBean;

public class StoreServiceUtils {

	@SuppressWarnings({ "rawtypes"})
	public static StoreDataBean searchStoresByAddress(HttpServletRequest request, Map param, String address, String city, String state, String zip, Object radius) {
		return searchStoresByAddress(request, param, address, city, state, zip, radius, 100);
	}

	@SuppressWarnings({ "rawtypes"})
	public static StoreDataBean searchStoresByAddress(HttpServletRequest request, Map param, String address, String city, String state, String zip, Object radius, int count) {
		StoreDataBean bean = new StoreDataBean();
		LocatorOperator locator = (LocatorOperator) Config.locate(request.getSession().getServletContext(), "locator");
		if (param == null) {
			param = new HashMap();
		}

		Boolean storesWithTirePricingOnly = param.containsKey("pricing");

		Boolean ignoreRadius = (Boolean) param.get("full");
		if (ignoreRadius == null) {
			ignoreRadius = new Boolean(false);
		}

		Boolean includeLicensees = (Boolean) param.get("licensee");
		if (includeLicensees == null) {
			includeLicensees = new Boolean(true);
		}

		Boolean partner = param.get("partner") != null;

		Boolean fiveStarPrimary = param.get("5starPrimary") != null;

		StoreService storeService = (StoreService) Config.locate(request.getSession().getServletContext(), "storeSearchService");

		Integer myRadius = null;
		try {
			myRadius = Integer.valueOf(radius.toString());
		} catch (Exception ex) {
			myRadius = null;
		}

		bean = storeService.searchStoresByAddress(locator.getConfig().getSiteName(), address, city, state, zip, myRadius, request.getRemoteAddr(), request.getParameter("geoPoint"), ignoreRadius,
				storesWithTirePricingOnly, includeLicensees, (String) param.get("licenseeType"), partner, fiveStarPrimary, count);

		return bean;
	}
}
