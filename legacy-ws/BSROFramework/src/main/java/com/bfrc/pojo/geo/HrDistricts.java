package com.bfrc.pojo.geo;

import java.util.*;

/**
 * HrDistricts generated by hbm2java
 */

public class HrDistricts  implements java.io.Serializable {


    // Fields    

	private Set stores = new HashSet();
     private String districtId;
     private String districtZone;
     private String districtName;

    // Constructors

	/** default constructor */
    public HrDistricts() {
    }
    
    /** constructor with id */
    public HrDistricts(String districtId) {
        this.districtId = districtId;
    }

    

   
    // Property accessors

    public String getDistrictId() {
        return this.districtId;
    }
    
    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictZone() {
        return this.districtZone;
    }
    
    public void setDistrictZone(String districtZone) {
        this.districtZone = districtZone;
    }

    public String getDistrictName() {
        return this.districtName;
    }
    
    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

	public Set getStores() {
		return this.stores;
	}

	public void setStores(Set stores) {
		this.stores = stores;
	}
   








}