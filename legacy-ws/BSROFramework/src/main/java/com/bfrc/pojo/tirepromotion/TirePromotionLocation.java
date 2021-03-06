package com.bfrc.pojo.tirepromotion;
// Generated Jun 12, 2009 11:31:56 AM by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * TirePromotionLocation generated by hbm2java
 */
public class TirePromotionLocation  implements java.io.Serializable {


     private TirePromotionLocationId id;
     private Date startDate;
     private Date endDate;

    public TirePromotionLocation() {
    }

    public TirePromotionLocation(TirePromotionLocationId id, Date startDate, Date endDate) {
       this.id = id;
       this.startDate = startDate;
       this.endDate = endDate;
    }
   
    public TirePromotionLocationId getId() {
        return this.id;
    }
    
    public void setId(TirePromotionLocationId id) {
        this.id = id;
    }
    public Date getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }




}


