package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:23:55 PM by Hibernate Tools 3.2.1.GA



/**
 * Warranty generated by hbm2java
 */
public class Warranty  implements java.io.Serializable {


     private long id;
     private String value;
     private String name;
     private String description;

    public Warranty() {
    }

	
    public Warranty(long id, String value, String name) {
        this.id = id;
        this.value = value;
        this.name = name;
    }
    public Warranty(long id, String value, String name, String description) {
       this.id = id;
       this.value = value;
       this.name = name;
       this.description = description;
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
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }




}


