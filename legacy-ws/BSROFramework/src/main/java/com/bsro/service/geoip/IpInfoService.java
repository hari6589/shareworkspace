package com.bsro.service.geoip;

import javax.servlet.http.HttpSession;

import com.bfrc.pojo.store.Store;

public interface IpInfoService {

	public void setPreferredStoreByIp(String remoteIP, HttpSession session);
	
	public String findZipfromIPAddress(String remoteIP);
	
	public Store getStoreByZip(String navZip, String remoteIP) ;

}

