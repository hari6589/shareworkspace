package com.bridgestone.bsro.aws.email.model.tire;

import org.codehaus.jackson.annotate.JsonIgnore;

public class StoreHoliday  implements java.io.Serializable {

	private StoreHolidayId id;
	private Long holidayId;
	private String description;
	private String status;
	
	private int year;
	private int month;
	private int day;
	
	@JsonIgnore
    public String getStatus() {
		return status;
	}
	
	@JsonIgnore
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

}