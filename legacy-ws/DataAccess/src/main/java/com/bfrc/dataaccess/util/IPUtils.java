package com.bfrc.dataaccess.util;

import org.apache.http.conn.util.InetAddressUtils;

/**
 * @author stallin sevugamoorthy
 *
 */
public class IPUtils {
	
	public IPUtils(){
    	
    }
    
    public static boolean isValidIP(final String ipAddress){		  
    	return InetAddressUtils.isIPv4Address(ipAddress) || InetAddressUtils.isIPv6Address(ipAddress);
    }

}
