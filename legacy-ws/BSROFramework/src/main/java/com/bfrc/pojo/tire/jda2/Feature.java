package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:21:45 PM by Hibernate Tools 3.2.1.GA



/**
 * Feature generated by hbm2java
 */
public class Feature  implements java.io.Serializable {


     private long id;
     private String value;
     private String benefit;

    public Feature() {
    }

	
    public Feature(long id, String value) {
        this.id = id;
        this.value = value;
    }
    public Feature(long id, String value, String benefit) {
       this.id = id;
       this.value = value;
       this.benefit = benefit;
    }
   
    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public String getValue() {
        return this.value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    public String getBenefit() {
        return this.benefit;
    }
    
    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }




}


