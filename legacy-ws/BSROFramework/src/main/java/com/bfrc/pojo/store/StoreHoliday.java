package com.bfrc.pojo.store;



/**
 * StoreHoliday generated by hbm2java
 */

public class StoreHoliday  implements java.io.Serializable {


    // Fields    

     private StoreHolidayId id;

     private Long holidayId;

	private String description;
	
	private String status;
    // Constructors

	/** default constructor */
    
    
    public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public StoreHoliday() {
    }
	/** constructor with id */
    public StoreHoliday(StoreHolidayId id) {
        this.id = id;
    }
    
    // Property accessors

    public StoreHolidayId getId() {
        return this.id;
    }
    
    public void setId(StoreHolidayId id) {
        this.id = id;
    }
   
    public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}