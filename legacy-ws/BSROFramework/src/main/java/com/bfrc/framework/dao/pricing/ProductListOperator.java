package com.bfrc.framework.dao.pricing;

import java.util.*;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;

public class ProductListOperator extends BusinessOperatorSupport {

	private com.bfrc.framework.dao.CatalogDAO catalogDAO;
	private com.bfrc.framework.dao.PricingDAO pricingDAO;
	
	public com.bfrc.framework.dao.CatalogDAO getCatalogDAO() {
		return this.catalogDAO;
	}

	public void setCatalogDAO(com.bfrc.framework.dao.CatalogDAO catalogDAO) {
		this.catalogDAO = catalogDAO;
	}

	public Object operate(Object o) throws Exception {
		return this.catalogDAO.getVehicleSizes(o);
	}
	
	public com.bfrc.framework.dao.PricingDAO getPricingDAO() {
		return this.pricingDAO;
	}

	public void setPricingDAO(com.bfrc.framework.dao.PricingDAO pricingDAO) {
		this.pricingDAO = pricingDAO;
	}

}
