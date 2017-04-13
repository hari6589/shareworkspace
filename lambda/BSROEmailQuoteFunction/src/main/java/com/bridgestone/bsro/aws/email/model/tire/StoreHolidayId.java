package com.bridgestone.bsro.aws.email.model.tire;


public class StoreHolidayId implements java.io.Serializable {
     private Short year;
     private Byte month;
     private Byte day;

    public StoreHolidayId() {
    }
    public Short getYear() {
        return this.year;
    }
    
    public void setYear(Short year) {
        this.year = year;
    }

    public Byte getMonth() {
        return this.month;
    }
    
    public void setMonth(Byte month) {
        this.month = month;
    }

    public Byte getDay() {
        return this.day;
    }
    
    public void setDay(Byte day) {
        this.day = day;
    }
   
    public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof StoreHolidayId) ) return false;
		 StoreHolidayId castOther = ( StoreHolidayId ) other; 
         
		 return (this.getYear()==castOther.getYear())
 && (this.getMonth()==castOther.getMonth())
 && (this.getDay()==castOther.getDay());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getYear().intValue();
         result = 37 * result + this.getMonth().intValue();
         result = 37 * result + this.getDay().intValue();
         return result;
   }   

}