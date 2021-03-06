package com.bfrc.pojo.tirepromotion;
// Generated Jun 12, 2009 11:31:56 AM by Hibernate Tools 3.2.1.GA



/**
 * TirePromotionLocationId generated by hbm2java
 */
public class TirePromotionLocationId  implements java.io.Serializable {


     private long promoId;
     private long storeNumber;

    public TirePromotionLocationId() {
    }

    public TirePromotionLocationId(long promoId, long storeNumber) {
       this.promoId = promoId;
       this.storeNumber = storeNumber;
    }
   
    public long getPromoId() {
        return this.promoId;
    }
    
    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }
    public long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(long storeNumber) {
        this.storeNumber = storeNumber;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TirePromotionLocationId) ) return false;
		 TirePromotionLocationId castOther = ( TirePromotionLocationId ) other; 
         
		 return (this.getPromoId()==castOther.getPromoId())
 && (this.getStoreNumber()==castOther.getStoreNumber());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getPromoId();
         result = 37 * result + (int)this.getStoreNumber();
         return result;
   }   


}


