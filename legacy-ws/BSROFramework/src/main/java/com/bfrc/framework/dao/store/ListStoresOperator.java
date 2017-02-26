package com.bfrc.framework.dao.store;

import java.util.List;
import java.util.Map;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;
import com.bfrc.framework.dao.StoreDAO;
import com.bsro.service.store.StoreService;

public class ListStoresOperator extends BusinessOperatorSupport {
	
	private StoreDAO storeDAO;
	private StoreService storeService;
	
	public Object operate(Object o) throws Exception {
		Map m = (Map)o;
		List l = null;
		String result = "dropdown";
		if(m.containsKey("city")) {
//System.out.println("running getStores");
			l = this.storeDAO.getStores(m);
			this.storeService.setHolidayHours(l);
			result = "radio";
		} else if(m.containsKey("state")) {
//System.out.println("running getCities");
			l = this.storeDAO.getCities(m);
		} else {
//System.out.println("running getStates");
			l = this.storeDAO.getStates(m);
		}
		m.put(RESULT, l);
		return result;
	}
	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}
	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}
	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

}
