package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:15:50 PM by Hibernate Tools 3.2.1.GA



/**
 * Class generated by hbm2java
 */
public class TireClass  implements java.io.Serializable {


     private long id;
     private String value;

    public TireClass() {
    }

    public TireClass(long id, String value) {
       this.id = id;
       this.value = value;
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




}


