package com.bfrc.framework.dao.district;

import java.util.List;
import java.util.Map;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;

import com.bfrc.pojo.geo.*;

public class DistrictInfoOperator extends BusinessOperatorSupport {

	private com.bfrc.framework.dao.DistrictDAO districtDAO;
	
	public com.bfrc.framework.dao.DistrictDAO getDistrictDAO() {
		return this.districtDAO;
	}

	public void setDistrictDAO(com.bfrc.framework.dao.DistrictDAO districtDAO) {
		this.districtDAO = districtDAO;
	}

	public Object operate(Object o) throws Exception {
		Map m = (Map)o;
		String result = "district";
		String id = (String)m.get("district");
		HrDistricts d = this.districtDAO.getDistrict(id); 
		m.put(RESULT, d);
		List l = this.districtDAO.getStores(id);
		m.put("stores", l);
		DistrictManager dm = this.districtDAO.getDistrictManager(id);
		m.put("dm", dm);
		return result;
	}

}
