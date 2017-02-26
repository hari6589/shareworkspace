package com.bsro.service.keyvalue;

import java.util.List;

import com.bfrc.pojo.keyvalue.KeyValueData;

public interface KeyValueService {
	public List<KeyValueData> getHierarchicalKeyValueDataForChildrenOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey);
	public KeyValueData lookupHierarchicalKeyValueDataForSpecificDescendantOfSpecifiedSiteAndKeyCategoryAndContainerKey(String siteName, String keyValueCategoryName, String containerKey, String parentKey, String childKey);
}
