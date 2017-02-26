package com.bfrc.dataaccess.model.oil;
// Generated Aug 30, 2012 3:49:10 PM by Hibernate Tools 3.1.0.beta4



/**
 * OilChangeStorePriceId generated by hbm2java
 */

public class OilChangeStorePriceId  implements java.io.Serializable {
	private static final long serialVersionUID = 4681972708766195398L;
	
	private Long oilChangeArticleNumber;
     private Long storeNumber;


    // Constructors

    /** default constructor */
    public OilChangeStorePriceId() {
    }

    
    /** full constructor */
    public OilChangeStorePriceId(long oilChangeArticleNumber, Long storeNumber) {
        this.oilChangeArticleNumber = oilChangeArticleNumber;
        this.storeNumber = storeNumber;
    }
    

   
    // Property accessors

    public long getOilChangeArticleNumber() {
        return this.oilChangeArticleNumber;
    }
    
    public void setOilChangeArticleNumber(long oilChangeArticleNumber) {
        this.oilChangeArticleNumber = oilChangeArticleNumber;
    }

    public Long getStoreNumber() {
        return this.storeNumber;
    }
    
    public void setStoreNumber(Long storeNumber) {
        this.storeNumber = storeNumber;
    }
   



   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof OilChangeStorePriceId) ) return false;
		 OilChangeStorePriceId castOther = ( OilChangeStorePriceId ) other; 
         
		 return (this.getOilChangeArticleNumber()==castOther.getOilChangeArticleNumber())
 && ( (this.getStoreNumber()==castOther.getStoreNumber()) || ( this.getStoreNumber()!=null && castOther.getStoreNumber()!=null && this.getStoreNumber().equals(castOther.getStoreNumber()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getOilChangeArticleNumber();
         result = 37 * result + ( getStoreNumber() == null ? 0 : this.getStoreNumber().hashCode() );
         return result;
   }   





}
