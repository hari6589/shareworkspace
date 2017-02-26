package com.bfrc.dataaccess.model.oil;
// Generated Aug 30, 2012 3:49:10 PM by Hibernate Tools 3.1.0.beta4



/**
 * OilStorePriceId generated by hbm2java
 */

public class OilStorePriceId  implements java.io.Serializable {
	private static final long serialVersionUID = 8916772913875454631L;
	
	private Long oilArticleNumber;
     private Long storeNumber;


    // Constructors

    /** default constructor */
    public OilStorePriceId() {
    }

    
    /** full constructor */
    public OilStorePriceId(long oilArticleNumber, Long storeNumber) {
        this.oilArticleNumber = oilArticleNumber;
        this.storeNumber = storeNumber;
    }
    

   
    // Property accessors

    public long getOilArticleNumber() {
        return this.oilArticleNumber;
    }
    
    public void setOilArticleNumber(long oilArticleNumber) {
        this.oilArticleNumber = oilArticleNumber;
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
		 if ( !(other instanceof OilStorePriceId) ) return false;
		 OilStorePriceId castOther = ( OilStorePriceId ) other; 
         
		 return (this.getOilArticleNumber()==castOther.getOilArticleNumber())
 && ( (this.getStoreNumber()==castOther.getStoreNumber()) || ( this.getStoreNumber()!=null && castOther.getStoreNumber()!=null && this.getStoreNumber().equals(castOther.getStoreNumber()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getOilArticleNumber();
         result = 37 * result + ( getStoreNumber() == null ? 0 : this.getStoreNumber().hashCode() );
         return result;
   }   





}