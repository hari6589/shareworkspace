package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.keyvalue.KeyValueData;

public interface KeyValueDAO {
	public List<KeyValueData> getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey);
	public KeyValueData lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey, String parentKey, String childKey);
}
