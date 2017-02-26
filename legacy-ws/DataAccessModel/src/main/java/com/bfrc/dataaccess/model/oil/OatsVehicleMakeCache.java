package com.bfrc.dataaccess.model.oil;
// Generated Oct 11, 2012 12:44:56 PM by Hibernate Tools 3.1.0.beta4

import java.util.Calendar;


/**
 * OatsVehicleMakeCache generated by hbm2java
 */

public class OatsVehicleMakeCache  implements java.io.Serializable {


    // Fields    

     private String makeGuid;
     private String name;
     private Calendar retrievalDate;


    // Constructors

    /** default constructor */
    public OatsVehicleMakeCache() {
    }

    
    /** full constructor */
    public OatsVehicleMakeCache(String makeGuid, String name, Calendar retrievalDate) {
        this.makeGuid = makeGuid;
        this.name = name;
        this.retrievalDate = retrievalDate;
    }
    

   
    // Property accessors

    public String getMakeGuid() {
        return this.makeGuid;
    }
    
    public void setMakeGuid(String makeGuid) {
        this.makeGuid = makeGuid;
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