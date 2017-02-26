package com.bfrc.framework.util;

import java.util.*;
import java.util.concurrent.*;

public class CacheService {

	static private CacheService _theInstance;
	@SuppressWarnings("rawtypes")
	static private Map _caches;

	/*********
	 * 
	 * Cahes in the form
	 * 
	 * id - Map
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private CacheService() {
		if (_caches == null)
			_caches = new ConcurrentHashMap();
	}

	public static CacheService getInstance() {
		if (_theInstance == null) {
			synchronized (CacheService.class) {
				if (_theInstance == null) {
					_theInstance = new CacheService();
				}
			}
		}
		return _theInstance;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void add(String id, Object key, Object val) {
		synchronized (_caches) {
			if (id == null) {
				_caches.put(key, val);
			} else {
				Map m = (Map) _caches.get(id.toUpperCase());
				if (m == null)
					m = new ConcurrentHashMap();
				m.put(key, val);
				_caches.put(id.toUpperCase(), m);
			}
		}
	}

	public void add(Object key, Object val) {
		add(null, key, val);
	}

	public Object get(Object key) {
		return get(null, key);
	}

	@SuppressWarnings("rawtypes")
	public Object get(String id, Object key) {
		Object obj = null;
		synchronized (_caches) {
			if (id == null) {
				obj = _caches.get(key);
			} else {
				Map m = (Map) _caches.get(id.toUpperCase());
				if (m == null)
					return null;
				obj = m.get(key);
				return obj;
			}
		}
		return obj;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void remove(String id, Object key) {
		synchronized (_caches) {
			if (id == null) {
				_caches.remove(key);
			} else {
				Map m = (Map) _caches.get(id.toUpperCase());
				if (m == null)
					return;
				m.remove(key);
				_caches.put(id.toUpperCase(), m);
				return;
			}
		}
		//
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void clear(String id) {
		synchronized (_caches) {
			if (id == null) {
				_caches.clear();
				return;
			} else {
				Map m = (Map) _caches.get(id.toUpperCase());
				if (m == null)
					return;
				m.clear();
				_caches.put(id.toUpperCase(), m);
				return;

			}
		}
	}

	public void clear() {
		//
		clear(null);
	}

	@SuppressWarnings("rawtypes")
	public Set getIds() {
		return _caches.keySet();
	}
	
	public boolean needResetCache(String key, long timeToCheck){
		if(timeToCheck <= 0) //0 or -1 will no need check cache
			return false;
		//add cache here for performance improvement
		Date lastCacheDate = (Date)getInstance().get(key+"_DATE_");
		Object l = getInstance().get(key);
		boolean resetCache = false;
		if(lastCacheDate == null || l == null)
			resetCache = true;
		if(!resetCache){
			Date now = new Date();
			if(now.getTime() - lastCacheDate.getTime() > timeToCheck)
				resetCache = true;
		}
		if(resetCache)//reset the date
			getInstance().add(key+"_DATE_", new Date());
		return resetCache;
	}

}
