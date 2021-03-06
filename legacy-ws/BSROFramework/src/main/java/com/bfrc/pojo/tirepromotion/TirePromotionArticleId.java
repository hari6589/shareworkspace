package com.bfrc.pojo.tirepromotion;
// Generated Jun 12, 2009 11:31:56 AM by Hibernate Tools 3.2.1.GA



/**
 * TirePromotionArticleId generated by hbm2java
 */
public class TirePromotionArticleId  implements java.io.Serializable {


     private long promoId;
     private long article;

    public TirePromotionArticleId() {
    }

    public TirePromotionArticleId(long promoId, long article) {
       this.promoId = promoId;
       this.article = article;
    }
   
    public long getPromoId() {
        return this.promoId;
    }
    
    public void setPromoId(long promoId) {
        this.promoId = promoId;
    }
    public long getArticle() {
        return this.article;
    }
    
    public void setArticle(long article) {
        this.article = article;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof TirePromotionArticleId) ) return false;
		 TirePromotionArticleId castOther = ( TirePromotionArticleId ) other; 
         
		 return (this.getPromoId()==castOther.getPromoId())
 && (this.getArticle()==castOther.getArticle());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getPromoId();
         result = 37 * result + (int) this.getArticle();
         return result;
   }   


}


