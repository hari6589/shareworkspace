package com.bfrc.framework.dao;

import java.util.List;
import java.util.Map;

import com.bfrc.*;
import com.bfrc.pojo.store.Store;

public interface SurveyDAO extends Bean {
	Map getMappedMindshareTiresurveyDetails();
	Map getValidMappedMindshareTiresurveyDetails();
	
}