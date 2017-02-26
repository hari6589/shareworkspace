package com.bfrc.pojo.tire.jda2;
// Generated Mar 19, 2011 12:21:45 PM by Hibernate Tools 3.2.1.GA



/**
 * FeatureJoinId generated by hbm2java
 */
public class FeatureJoinId  implements java.io.Serializable {


     private long displayId;
     private long featureId;

    public FeatureJoinId() {
    }

    public FeatureJoinId(long displayId, long featureId) {
       this.displayId = displayId;
       this.featureId = featureId;
    }
   
    public long getDisplayId() {
        return this.displayId;
    }
    
    public void setDisplayId(long displayId) {
        this.displayId = displayId;
    }
    public long getFeatureId() {
        return this.featureId;
    }
    
    public void setFeatureId(long featureId) {
        this.featureId = featureId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof FeatureJoinId) ) return false;
		 FeatureJoinId castOther = ( FeatureJoinId ) other; 
         
		 return (this.getDisplayId()==castOther.getDisplayId())
 && (this.getFeatureId()==castOther.getFeatureId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getDisplayId();
         result = 37 * result + (int) this.getFeatureId();
         return result;
   }   


}


