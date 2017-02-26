package com.bfrc.framework.dao;

import java.util.*;

import com.bfrc.pojo.customer.*;

public interface CustomerDAO {
	
	CustomerContactusemailLog findCustomerContactusemailLog(Object id);
	List getAllCustomerContactusemailLogs();
	void updateCustomerContactusemailLog(CustomerContactusemailLog t);
	Long createCustomerContactusemailLog(CustomerContactusemailLog t);
	void deleteCustomerContactusemailLog(CustomerContactusemailLog t);
	
}
