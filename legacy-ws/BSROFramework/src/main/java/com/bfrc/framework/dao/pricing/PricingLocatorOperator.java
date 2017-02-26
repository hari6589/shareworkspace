package com.bfrc.framework.dao.pricing;

import java.util.HashMap;
import java.util.Map;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.pojo.store.Store;
import com.bfrc.storelocator.util.AddressNotFoundException;
import com.bfrc.storelocator.util.NoStoresFoundException;
import com.bsro.service.store.StoreService;

public class PricingLocatorOperator extends BusinessOperatorSupport {
	
	private LocatorDAO locatorDAO;
	private StoreDAO storeDAO;
	private StoreService storeService;
	public StoreService getStoreService() {
		return storeService;
	}
	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}
	private com.bfrc.framework.businessdata.BusinessOperator bizOp;
	
	public Object operate(Object o) throws Exception {
		Map m = (Map)o;
		m.put("pricing", "");
//		List l = null;
//		l = this.storeDAO.getStoresInZip(m);
		String result = SUCCESS;
		
		// if there is exactly one store in the zip code, use that
/*		if(l != null && l.size() == 1) {
			Store match = (Store)l.get(0);
			Store[] out = new Store[1];
			out[0] = match;
			m.put(RESULT, out);
			return result;
		}
*/		
		// else use the store locator to find the 3 closest stores
		HashMap param = new HashMap();
		param.put(OPERATION, "getRequestId");
		result = this.bizOp.operate(param);
		
		String geoPoint = (String)m.get("geoPoint");
		Float[] location;
		
		long id = ((Long)param.get(RESULT)).longValue();
		param.put("id", new Long(id));
		param.put("app", getConfig().getSiteName().toUpperCase()+"_PRICING");
		param.put("zip", m.get("zip"));
		param.put("remoteIP", m.get("remoteIP"));
		param.put("full", m.get("full"));
		param.put("licensee", m.get("licensee"));
		if(m.get("ignoreActiveFlag") != null && (Boolean)m.get("ignoreActiveFlag") == true)
		{
			param.put("ignoreActiveFlag", new Boolean(true));		
		}
		else
		{
			param.put("ignoreActiveFlag", new Boolean(false));	
		}		
		if (geoPoint == null || "".equals(geoPoint))
		{
			param.put(OPERATION, "geocode");
			result = this.bizOp.operate(param);
			location = (Float[])param.get(RESULT);
			if(location == null)
				throw new AddressNotFoundException();
        }
        else
        {
        	location = new Float[2];
        	String[] pointString = geoPoint.split(",");
        	location[1] = new Float(pointString[0]);
        	location[0] = new Float(pointString[1]);	        	
        }		
		
		param.put("location", location);
		Object radius = m.get("radius");
		if(radius != null)
			param.put("radius", radius);
		param.put("licenseeType", m.get("licenseeType"));
		param.put(OPERATION, "getClosestStores");
		if(m.get("partner") != null)
			param.put("partner", "");
		if(m.get("5starPrimary") != null)
			param.put("5starPrimary", "");
		if(m.get("partner") != null)
			param.put("partner", "");
		result = this.bizOp.operate(param);
		long[][] storeArray = (long[][])param.get(RESULT);
		if(storeArray == null || storeArray.length == 0)
			throw new NoStoresFoundException();
		param.put("interval", new Long(storeArray[0][2]));
		param.put(OPERATION, "logStoreQuery");
		result = this.bizOp.operate(param);
		Store[] stores = storeService.getStoresLight(storeArray);
		HashMap<Long,Long> distances = new HashMap<Long,Long>();
		if (stores != null && stores.length > 0) {
			for(int i = 0 ; i < stores.length ; i++) {
				distances.put(stores[i].getStoreNumber(), storeArray[i][1]);
			}
			m.put("distances",distances);
		}
		m.put(RESULT, stores);
		
		return result;
	}
	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}
	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}
	public com.bfrc.framework.businessdata.BusinessOperator getBizOp() {
		return this.bizOp;
	}
	public void setBizOp(com.bfrc.framework.businessdata.BusinessOperator bizOp) {
		this.bizOp = bizOp;
	}
	public LocatorDAO getLocatorDAO() {
		return this.locatorDAO;
	}
	public void setLocatorDAO(LocatorDAO locatorDAO) {
		this.locatorDAO = locatorDAO;
	}

}
