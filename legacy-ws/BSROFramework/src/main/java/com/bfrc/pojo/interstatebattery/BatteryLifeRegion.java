package com.bfrc.pojo.interstatebattery;
// Generated Jan 27, 2010 2:32:09 PM by Hibernate Tools 3.2.1.GA



/**
 * BatteryLifeRegion generated by hbm2java
 */
public class BatteryLifeRegion  implements java.io.Serializable {


     private String batteryLifeZip3;
     private Long batteryLifeRegionId;

    public BatteryLifeRegion() {
    }

	
    public BatteryLifeRegion(String batteryLifeZip3) {
        this.batteryLifeZip3 = batteryLifeZip3;
    }
    public BatteryLifeRegion(String batteryLifeZip3, Long batteryLifeRegionId) {
       this.batteryLifeZip3 = batteryLifeZip3;
       this.batteryLifeRegionId = batteryLifeRegionId;
    }
   
    public String getBatteryLifeZip3() {
        return this.batteryLifeZip3;
    }
    
    public void setBatteryLifeZip3(String batteryLifeZip3) {
        this.batteryLifeZip3 = batteryLifeZip3;
    }
    public Long getBatteryLifeRegionId() {
        return this.batteryLifeRegionId;
    }
    
    public void setBatteryLifeRegionId(Long batteryLifeRegionId) {
        this.batteryLifeRegionId = batteryLifeRegionId;
    }




}


