package com.bfrc.framework.dao.district;

import com.bfrc.framework.businessdata.BusinessOperatorSupport;

public class ListEventsOperator extends BusinessOperatorSupport {

	private com.bfrc.framework.dao.EventDAO eventDAO;
	
	public com.bfrc.framework.dao.EventDAO getEventDAO() {
		return this.eventDAO;
	}

	public void setEventDAO(com.bfrc.framework.dao.EventDAO eventDAO) {
		this.eventDAO = eventDAO;
	}

	public Object operate(Object o) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
