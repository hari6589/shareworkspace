package com.bfrc.pojo.tire;

public class TireSubBrand {
	private Long tireSubBrandId;
	private Long brandId;
	private String value;
	
	public Long getTireSubBrandId() {
		return tireSubBrandId;
	}
	public void setTireSubBrandId(Long tireSubBrandId) {
		this.tireSubBrandId = tireSubBrandId;
	}

	public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
