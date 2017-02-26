package com.bfrc.pojo.interstatebattery;
// Generated Dec 1, 2009 2:07:59 PM by Hibernate Tools 3.2.1.GA



/**
 * InterstateBatteryMaster generated by hbm2java
 */
public class InterstateBatteryMaster  implements java.io.Serializable {


     private String productCode;
     private String product;
     private Long partNumber;
     private Long totalWarranty;
     private Long replacementWarranty;
     private Long cca;
     private Long rcMinutes;

    public InterstateBatteryMaster() {
    }

	
    public InterstateBatteryMaster(String productCode) {
        this.productCode = productCode;
    }
    public InterstateBatteryMaster(String productCode, String product, Long partNumber, Long totalWarranty, Long replacementWarranty, Long cca, Long rcMinutes) {
       this.productCode = productCode;
       this.product = product;
       this.partNumber = partNumber;
       this.totalWarranty = totalWarranty;
       this.replacementWarranty = replacementWarranty;
       this.cca = cca;
       this.rcMinutes = rcMinutes;
    }
   
    public String getProductCode() {
        return this.productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getProduct() {
        return this.product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }
    public Long getPartNumber() {
        return this.partNumber;
    }
    
    public void setPartNumber(Long partNumber) {
        this.partNumber = partNumber;
    }
    public Long getTotalWarranty() {
        return this.totalWarranty;
    }
    
    public void setTotalWarranty(Long totalWarranty) {
        this.totalWarranty = totalWarranty;
    }
    public Long getReplacementWarranty() {
        return this.replacementWarranty;
    }
    
    public void setReplacementWarranty(Long replacementWarranty) {
        this.replacementWarranty = replacementWarranty;
    }
    public Long getCca() {
        return this.cca;
    }
    
    public void setCca(Long cca) {
        this.cca = cca;
    }
    public Long getRcMinutes() {
        return this.rcMinutes;
    }
    
    public void setRcMinutes(Long rcMinutes) {
        this.rcMinutes = rcMinutes;
    }

    public String getTotalWarrantyYearsOrMonths() {
    	String warranty;
    	
    	if(this.totalWarranty == null) {
    		warranty = "";
    	}
    	else if(totalWarranty%12 == 0) {
    		warranty = this.totalWarranty/12 + "-Year";
    	}
    	else {
    		warranty = this.totalWarranty + "-Month";
    	}
    	
    	return warranty;
    }

}


