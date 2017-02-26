/**
 * 
 */
package com.bfrc.dataaccess.svc.bing;

import java.math.BigDecimal;

/**
 * @author schowdhu
 *
 */
public interface BingMapsAPI {
	
	public BigDecimal getDistanceBetweenAddress(
			String fromAddress, String fromCity, String fromState, String fromZip, 
			String toAddress, String toCity, String toState, String toZip);
	
	public BigDecimal getDistanceFromStore(
			String fromAddress, String fromCity, String fromState, String fromZip, 
			String storeLat, String storeLong);

}
