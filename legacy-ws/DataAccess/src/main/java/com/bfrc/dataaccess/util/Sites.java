package com.bfrc.dataaccess.util;

import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
 
public class Sites {

	public enum SiteTypes {
		BFRC("BFRC"),
		ET("ET"),
		ETIRE("ETIRE"),
		FANEUIL("fan"),
		FCAC("FCAC"),
		FCFC("FFC"),
		PPS("PPS"),
		TP("TP"),
		WWT("WWT"),
		HT("HT"),
		FIVESTAR("5STR");
		
		private String siteType;
		private SiteTypes(String type) {
			this.siteType = type;
		}
		public String getSiteType() {
			return this.siteType;
		}
	}
	
	public static String getWebSiteAppRoot(String siteType) {
		String approot = null;
		siteType = (siteType != null) ? siteType.trim() : siteType;
		if (!StringUtils.isNullOrEmpty(siteType)) {
			approot = "http://";
			
			if (ServerUtil.isProduction()) {
				approot += "www.";
			} else if (ServerUtil.isBSROQa()) {
				approot += "cwh-qa.";
			} else {
				approot += "cwh-uat.";
			}
			
			if ("FCAC".equalsIgnoreCase(siteType)) {
				approot += "firestonecompleteautocare.com";
			} else if ("TP".equalsIgnoreCase(siteType)) {
				approot += "tiresplus.com";
			} else if ("HT".equalsIgnoreCase(siteType)) {
				approot += "hibdontire.com";
			} else if ("WWT".equalsIgnoreCase(siteType)) {
				approot += "wheelworks.net";
			}
		}
		return approot;
	}
	
	public static String getAssetsRoot(String siteType) {
		String approot = null;
		siteType = (siteType != null) ? siteType.trim() : siteType;
		if (!StringUtils.isNullOrEmpty(siteType)) {
			approot = "http://";
			
			if (ServerUtil.isProduction()) {
				approot += "assets.";
			} else if (ServerUtil.isBSROQa()) {
				approot += "qa01-assets.";
			} else {
				approot += "dev01-assets.";
			}
			
			approot += "firestonecompleteautocare.com";
		}
		return approot;
	}
	
	public static String getSiteFullName(String siteType) {
		siteType = (siteType != null) ? siteType.trim() : siteType;
		String siteFullName = "";
		if ("FCAC".equalsIgnoreCase(siteType)) {
			siteFullName = "Firestone Complete Auto Care";
		} else if ("TP".equalsIgnoreCase(siteType)) {
			siteFullName = "Tires Plus";
		} else if ("HT".equalsIgnoreCase(siteType)) {
			siteFullName = "Hibdon Tires Plus";
		} else if ("WWT".equalsIgnoreCase(siteType)) {
			siteFullName = "Wheel Works";
		}
		return siteFullName;
	}
	
	public static String getWebSiteTireImageDir(String siteType) {
		String imgUri = null;
		siteType = (siteType != null) ? siteType.trim() : siteType;
		if (!StringUtils.isNullOrEmpty(siteType)) {
			if ("FCAC".equalsIgnoreCase(siteType)) {
				imgUri = "/static-fcac/images/tires/";
			} else if ("TP".equalsIgnoreCase(siteType)) {
				imgUri = "/img/tires/";
			} else if ("HTP".equalsIgnoreCase(siteType)) {
				imgUri = "/static-ht/images/tires/";
			} else if ("WW".equalsIgnoreCase(siteType)) {
				imgUri = "/static-wwt/images/tires/";
			}
		}
		return imgUri;
	}
}
