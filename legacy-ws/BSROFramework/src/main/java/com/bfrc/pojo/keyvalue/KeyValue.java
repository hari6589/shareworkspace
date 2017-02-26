package com.bfrc.pojo.keyvalue;

public class KeyValue {

	private Long keyValueId;
	private Long parentKeyValueId;
	private Long keyValueCategoryId;
	private String key;
	private String value;
	private String alternateValue1;
	private Long displayOrder;
	
	public Long getKeyValueId() {
		return keyValueId;
	}
	public void setKeyValueId(Long keyValueId) {
		this.keyValueId = keyValueId;
	}
	public Long getParentKeyValueId() {
		return parentKeyValueId;
	}
	public void setParentKeyValueId(Long parentKeyValueId) {
		this.parentKeyValueId = parentKeyValueId;
	}
	public Long getKeyValueCategoryId() {
		return keyValueCategoryId;
	}
	public void setKeyValueCategoryId(Long keyValueCategoryId) {
		this.keyValueCategoryId = keyValueCategoryId;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getAlternateValue1() {
		return alternateValue1;
	}
	public void setAlternateValue1(String alternateValue1) {
		this.alternateValue1 = alternateValue1;
	}
	public Long getDisplayOrder() {
		return displayOrder;
	}
	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}
}