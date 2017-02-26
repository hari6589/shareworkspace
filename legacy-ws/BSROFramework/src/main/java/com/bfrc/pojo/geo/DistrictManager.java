package com.bfrc.pojo.geo;



/**
 * DistrictManager generated by hbm2java
 */

public class DistrictManager  implements java.io.Serializable {


    // Fields    

     private DistrictManagerId id;
     private String emailAddress;
     private HrDistricts district;
     private String name;


    // Constructors

	/** default constructor */
    public DistrictManager() {
    }
    
    /** constructor with id */
    public DistrictManager(DistrictManagerId id) {
        this.id = id;
    }

    

   
    // Property accessors

    public DistrictManagerId getId() {
        return this.id;
    }
    
    public void setId(DistrictManagerId id) {
        this.id = id;
    }
    
    public String getEmailAddress() {
        return this.emailAddress;
    }
    
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

	public HrDistricts getDistrict() {
		return this.district;
	}

	public void setDistrict(HrDistricts district) {
		this.district = district;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
   








}