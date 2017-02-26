package com.bfrc.pojo.store;

// Generated Sep 5, 2007 12:38:01 PM by Hibernate Tools 3.2.0.beta8

/**
 * BfrcStoreMap generated by hbm2java
 */
public class BfrcStoreMap implements java.io.Serializable {

	// Fields    

	private String siteName;

	private String storeType;

	private int mapId;

	private String parentType;

	private String disclaimerDescription;

	// Constructors

	/** default constructor */
	public BfrcStoreMap() {
	}

	/** minimal constructor */
	public BfrcStoreMap(int mapId) {
		this.mapId = mapId;
	}

	/** full constructor */
	public BfrcStoreMap(int mapId, String parentType,
			String disclaimerDescription) {
		this.mapId = mapId;
		this.parentType = parentType;
		this.disclaimerDescription = disclaimerDescription;
	}

	// Property accessors
	public int getMapId() {
		return this.mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public String getParentType() {
		return this.parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public String getDisclaimerDescription() {
		return this.disclaimerDescription;
	}

	public void setDisclaimerDescription(String disclaimerDescription) {
		this.disclaimerDescription = disclaimerDescription;
	}

	public String getSiteName() {
		return this.siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getStoreType() {
		return this.storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

}
