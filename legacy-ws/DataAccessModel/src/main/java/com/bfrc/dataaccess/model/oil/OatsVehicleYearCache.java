package com.bfrc.dataaccess.model.oil;
// Generated Oct 11, 2012 12:44:56 PM by Hibernate Tools 3.1.0.beta4

import java.util.Calendar;


/**
 * OatsVehicleYearCache generated by hbm2java
 */

public class OatsVehicleYearCache  implements java.io.Serializable {


    // Fields    

     private String yearGuid;
     private String name;
     private Calendar retrievalDate;


    // Constructors

    /** default constructor */
    public OatsVehicleYearCache() {
    }

    
    /** full constructor */
    public OatsVehicleYearCache(String yearGuid, String name, Calendar retrievalDate) {
        this.yearGuid = yearGuid;
        this.name = name;
        this.retrievalDate = retrievalDate;
    }
    

   
    // Property accessors

    public String getYearGuid() {
        return this.yearGuid;
    }
    
    public void setYearGuid(String yearGuid) {
        this.yearGuid = yearGuid;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Calendar getRetrievalDate() {
        return this.retrievalDate;
    }
    
    public void setRetrievalDate(Calendar retrievalDate) {
        this.retrievalDate = retrievalDate;
    }

}
