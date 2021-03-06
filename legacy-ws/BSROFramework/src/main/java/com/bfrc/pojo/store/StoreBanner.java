package com.bfrc.pojo.store;


/**
 * Store generated by hbm2java
 */

public class StoreBanner implements java.io.Serializable {


	private Long storeNumber;

	private String bannerSetting;
	
	/*
	private Boolean fivestarActiveFlag;
    private String websiteName;
    */

	// Constructors

	/** default constructor */
	
	public Long getStoreNumber() {
		return this.storeNumber;
	}

	public int getNumber() {
		return this.storeNumber.intValue();
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public void setNumber(long storeNumber) {
		setStoreNumber(new Long(storeNumber));
	}


	public String getBannerSetting() {
		return bannerSetting;
	}

	public void setBannerSetting(String bannerSetting) {
		this.bannerSetting = bannerSetting;
	}
}