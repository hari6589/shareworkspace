package com.bfrc.pojo.keyvalue;

public class KeyValueCategory {
	
	public static final String MAKE_MODEL_LOCALIZATION_MAPPING_LIST = "make-model-localization-mapping-list";
	public static final String STATE_CITY_LOCALIZATION_MAPPING_LIST = "state-city-localization-mapping-list";
	
	private Long keyValueCategoryId;
	private String siteName;
	private String name;
	
	public Long getKeyValueCategoryId() {
		return keyValueCategoryId;
	}
	public void setKeyValueCategoryId(Long keyValueCategoryId) {
		this.keyValueCategoryId = keyValueCategoryId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
