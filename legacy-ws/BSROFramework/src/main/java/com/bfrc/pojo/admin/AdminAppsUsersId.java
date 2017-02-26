package com.bfrc.pojo.admin;
// Generated Nov 3, 2011 9:03:49 PM by Hibernate Tools 3.2.1.GA



/**
 * AdminAppsUsersId generated by hbm2java
 */
public class AdminAppsUsersId  implements java.io.Serializable {


     private long userId;
     private long applicationId;

    public AdminAppsUsersId() {
    }

    public AdminAppsUsersId(long userId, long applicationId) {
       this.userId = userId;
       this.applicationId = applicationId;
    }
   
    public long getUserId() {
        return this.userId;
    }
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public long getApplicationId() {
        return this.applicationId;
    }
    
    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof AdminAppsUsersId) ) return false;
		 AdminAppsUsersId castOther = ( AdminAppsUsersId ) other; 
         
		 return (this.getUserId()==castOther.getUserId())
 && (this.getApplicationId()==castOther.getApplicationId());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + (int) this.getUserId();
         result = 37 * result + (int) this.getApplicationId();
         return result;
   }   


}


