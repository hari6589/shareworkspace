package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:23:55 PM by Hibernate Tools 3.2.1.GA


/**
 * TiregroupImage generated by hbm2java
 */
public class TiregroupImage  implements java.io.Serializable {


     private TiregroupImageId id;
     private byte[] image;

    public TiregroupImage() {
    }

	
    public TiregroupImage(TiregroupImageId id) {
        this.id = id;
    }
    public TiregroupImage(TiregroupImageId id, byte[] image) {
       this.id = id;
       this.image = image;
    }
   
    public TiregroupImageId getId() {
        return this.id;
    }
    
    public void setId(TiregroupImageId id) {
        this.id = id;
    }
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }




}


