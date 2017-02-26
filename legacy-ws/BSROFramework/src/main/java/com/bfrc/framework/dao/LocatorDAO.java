package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.Config;
import com.bfrc.pojo.store.Store;
import com.bfrc.framework.dao.StoreDAO;

/**
 * @author Ed Knutson
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface LocatorDAO {
	public Store[] getStores(long[][] storeNumber);
	
	public Store[] getStoresLight(long[][] storeNumber);

	public String getStoreType(Store store);
	
	public void setHolidayHours(List<Store> stores);
	
	public List getHolidayHours(Store store);
	
	public List getStoreHour(long storeNumber);
	
	public Store getStore(long storeNumber);
	
	public String getNameForStoreNumber(long number);	
	
	public long getNextId();

	public void log(long id, String app, String remoteIP, String type, long interval);

	public void log(long id, String app, String remoteIP, String type, long interval, String street, String city, String state, String zip);
	
	public void log(long id, String app, String remoteIP, String type, long interval, String street, String city, String state, String zip, long storeNumber);

	public List getClosestStores(Float[] location, String app);
	
	public List getClosestStores(Float[] location, String app, boolean licensee);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full);

	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p, boolean isDefaultStore);
	
	public List getClosestStores(Float[] location, String app, boolean licensee, int radius, boolean pricing, String state, boolean full, String licenseeType, int count, boolean ignoreActiveFlag, String p, boolean isDefaultStore, boolean ignoreStoreTypes);

	public Config getConfig();
	public StoreDAO getStoreDAO();
}
