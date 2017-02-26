package com.bsro.service.keyvalue;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.KeyValueDAO;
import com.bfrc.pojo.keyvalue.KeyValueData;

@Service
public class KeyValueServiceImpl implements KeyValueService {
	
	@Autowired
	private KeyValueDAO keyValueDAO;
	
	public List<KeyValueData> getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey) {
		List<KeyValueData> keyValueList = keyValueDAO.getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, keyValueCategoryName, containerKey);
		
		return keyValueList;		
	}
	
	public KeyValueData lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey, String parentKey, String childKey) {
		KeyValueData keyValueData = keyValueDAO.lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(siteName, keyValueCategoryName, containerKey, parentKey, childKey);
		
		return keyValueData;
	}
}
