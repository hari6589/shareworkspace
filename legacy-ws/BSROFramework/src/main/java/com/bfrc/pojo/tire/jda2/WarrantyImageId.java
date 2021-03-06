package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:23:55 PM by Hibernate Tools 3.2.1.GA


/**
 * WarrantyImageId generated by hbm2java
 */
public class WarrantyImageId  implements java.io.Serializable {


     private long warrantyId;
     private byte[] image;

    public WarrantyImageId() {
    }

	
    public WarrantyImageId(long warrantyId) {
        this.warrantyId = warrantyId;
    }
    public WarrantyImageId(long warrantyId, byte[] image) {
       this.warrantyId = warrantyId;
       this.image = image;
    }
   
    public long getWarrantyId() {
        return this.warrantyId;
    }
    
    public void setWarrantyId(long warrantyId) {
        this.warrantyId = warrantyId;
    }
    public byte[] getImage() {
        return this.image;
    }
    
    public void setImage(byte[] image) {
        this.image = image;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof WarrantyImageId) ) return false;
		 WarrantyImageId castOther = ( WarrantyImageId ) other; 
         
		 return (this.getWarrantyId()==castOther.getWarrantyId())
 && ( (this.getImage()==castOther.getImage()) || ( this.getImage()!=null && castOther.getImage()!=null && this.getImage().equals(castOther.getImage()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getWarrantyId();
         result = 37 * result + ( getImage() == null ? 0 : this.getImage().hashCode() );
         return result;
   }   


}


