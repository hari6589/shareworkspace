package com.bfrc.dataaccess.model.store;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * StoreHour generated by hbm2java
 */

public class StoreHolidayHourId  implements java.io.Serializable {

    // Fields    
     private Long holidayId;
     private Long storeNumber;
    
    @JsonIgnore
	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	@JsonSerialize(using=ToStringSerializer.class)
	public Long getHolidayId() {
		return holidayId;
	}

	public void setHolidayId(Long holidayId) {
		this.holidayId = holidayId;
	}

}