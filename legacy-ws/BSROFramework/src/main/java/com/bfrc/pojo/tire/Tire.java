package com.bfrc.pojo.tire;


import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class Tire  implements java.io.Serializable, Cloneable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -2649174867277685184L;
	// Constructors
    /** default constructor */
    public Tire() {
    }
    
    private long displayId;
    public long getDisplayId(){return this.displayId;}
    public void setDisplayId(long displayId){this.displayId = displayId;}
   
    private String generateCatalogPage;
    public String getGenerateCatalogPage(){
        return this.generateCatalogPage;
    }

    public void setGenerateCatalogPage(String generateCatalogPage){
        this.generateCatalogPage = generateCatalogPage;
    }
   
    private String loadRange;
    public String getLoadRange(){
        return this.loadRange;
    }

    public void setLoadRange(String loadRange){
        this.loadRange = loadRange;
    }
   
    private long loadIndex;
    public long getLoadIndex(){return this.loadIndex;}
    public void setLoadIndex(long loadIndex){this.loadIndex = loadIndex;}
    
    private String tireName;
    public String getTireName(){
        return this.tireName;
    }

    public void setTireName(String tireName){
        this.tireName = tireName;
    }
   
    private String brand;
    public String getBrand(){
        return this.brand;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }
   
    public long getTiregroupId(){ return this.tireGroupId;}
    public void setTiregroupId(long tireGroupId){this.tireGroupId = tireGroupId;}
    
    private long tireGroupId;
    public long getTireGroupId(){ return this.tireGroupId;}
    public void setTireGroupId(long tireGroupId){this.tireGroupId = tireGroupId;}
   
    private String tireGroupName;
    public String getTireGroupName(){
        return this.tireGroupName;
    }
    public void setTireGroupName(String tireGroupName){
        this.tireGroupName = tireGroupName;
    }
    
    private long tireClassId;
    public long getTireClassId(){ return this.tireClassId;}
    public void setTireClassId(long tireClassId){this.tireClassId = tireClassId;}
    private String tireClassName;
    public String getTireClassName(){
        return this.tireClassName;
    }
    public void setTireClassName(String tireClassName){
        this.tireClassName = tireClassName;
    }
    
    private long tireSegmentId;
    public long getTireSegmentId(){ return this.tireSegmentId;}
    public void setTireSegmentId(long tireSegmentId){this.tireSegmentId = tireSegmentId;}
    private String tireSegmentName;
    public String getTireSegmentName(){
        return this.tireSegmentName;
    }
    public void setTireSegmentName(String tireSegmentName){
        this.tireSegmentName = tireSegmentName;
    }
    
    private String tireSize;
    public String getTireSize(){
        return this.tireSize;
    }

    public void setTireSize(String tireSize){
        this.tireSize = tireSize;
    }
   
    private String standardOptional;
    public String getStandardOptional(){
    	if(this.standardOptional == null)
    		this.standardOptional="S";
        return this.standardOptional;
    }

    public void setStandardOptional(String standardOptional){
        this.standardOptional = standardOptional;
    }
   
    public String getStandardOptionalDisplay(){
    	return "S".equals(this.standardOptional) ? "Standard" : "Optional";
    }
    
    private String frontRearBoth;
    public String getFrontRearBoth(){
    	if(this.frontRearBoth == null)
    		this.frontRearBoth="B";
        return this.frontRearBoth;
    }

    public void setFrontRearBoth(String frontRearBoth){
        this.frontRearBoth = frontRearBoth;
    }
   
    public boolean isFront(){
    	return "F".equalsIgnoreCase(this.frontRearBoth);
    }
    public boolean isRear(){
    	return "R".equalsIgnoreCase(this.frontRearBoth);
    }
    public boolean isBoth(){
    	if(this.frontRearBoth == null)
    		return true;
    	return "B".equalsIgnoreCase(this.frontRearBoth);
    }
    
    public String getFrontRearBothDisplay(){
    	return isBoth() ? "Both" : isFront() ?  "Front": "Rear";
    }
    
    private String vehtype;
    public String getVehtype() {
		return vehtype;
	}


	public void setVehtype(String vehtype) {
		this.vehtype = vehtype;
	}
	
    private String speedRating;
    public String getSpeedRating(){
        return this.speedRating;
    }

    public void setSpeedRating(String speedRating){
        this.speedRating = speedRating;
    }
   
    private String sidewallDescription;
    public String getSidewallDescription(){
        return this.sidewallDescription;
    }

    public void setSidewallDescription(String sidewallDescription){
        this.sidewallDescription = sidewallDescription;
    }
   
    private String mileage;
    public String getMileage(){
        return this.mileage;
    }
    public void setMileage(String mileage){
        this.mileage = mileage;
    }
   
    private String technology;
    public String getTechnology(){
        return this.technology;
    }

    public void setTechnology(String technology){
        this.technology = technology;
    }
   
    private long warrantyId;
    public long getWarrantyId(){return this.warrantyId;}
    public void setWarrantyId(long warrantyId){ this.warrantyId = warrantyId;}
    
    private String warrantyName;
    public String getWarrantyName(){
        return this.warrantyName;
    }
    public void setWarrantyName(String warrantyName){
        this.warrantyName = warrantyName;
    }
    
    
    private String crossSection;
    public String getCrossSection(){
        return this.crossSection;
    }
    public void setCrossSection(String crossSection){
        this.crossSection = crossSection;
    }
   
    private String aspect;
    public String getAspect(){
        return this.aspect;
    }

    public void setAspect(String aspect){
        this.aspect = aspect;
    }
   
    private String rimSize;
    public String getRimSize(){
        return this.rimSize;
    }

    public void setRimSize(String rimSize){
        this.rimSize = rimSize;
    }
   
    private String oemFlag;
    public String getOemFlag(){
        return this.oemFlag;
    }

    public void setOemFlag(String oemFlag){
        this.oemFlag = oemFlag;
    }
   
    @JsonIgnore
    private String discontinued;
    public String getDiscontinued(){
        return this.discontinued;
    }

    public void setDiscontinued(String discontinued){
        this.discontinued = discontinued;
    }
    
    @JsonIgnore
    public boolean isDiscontinued() {
		return "Y".equalsIgnoreCase(this.discontinued);
	}
	
    
    private long article;
    public long getArticle(){
        return this.article;
    }

    public void setArticle(long article){
        this.article = article;
    }

    private long rearArticle;
    public long getRearArticle(){
        return this.rearArticle;
    }
    public void setRearArticle(long rearArticle){
        this.rearArticle = rearArticle;
    }
    
    private String description;
    public String getDescription(){ return this.description;}
    public void setDescription(String description){ this.description = description; }
   
    private long line;
    public long getLine(){ return this.line;}
    public void setLine(long line){ this.line = line; }
   
    private double retailPrice;
    public double getRetailPrice(){ return this.retailPrice;}
    public void setRetailPrice(double retailPrice){ this.retailPrice = retailPrice; }
   
    private String onSale;
    public String getOnSale(){ return (this.onSale == null ? "R" : this.onSale);}
    public void setOnSale(String onSale){ this.onSale = onSale; }
   
    private Date endDate;
    public Date getEndDate(){ return this.endDate;}
    public void setEndDate(Date endDate){ this.endDate = endDate; }
   
    private double exciseTax;
    public double getExciseTax(){ return this.exciseTax;}
    public void setExciseTax(double exciseTax){ this.exciseTax = exciseTax; }
   
    private String exciseTaxArticle;
    public String getExciseTaxArticle(){ return this.exciseTaxArticle;}
    public void setExciseTaxArticle(String exciseTaxArticle){ this.exciseTaxArticle = exciseTaxArticle; }
   
    private double tireFee;
    public double getTireFee(){ return this.tireFee;}
    public void setTireFee(double tireFee){ this.tireFee = tireFee; }
   
    private String feeDesc;
    public String getFeeDesc(){ return this.feeDesc;}
    public void setFeeDesc(String feeDesc){ this.feeDesc = feeDesc; }
   
    private String feeArticle;
    public String getFeeArticle(){ return this.feeArticle;}
    public void setFeeArticle(String feeArticle){ this.feeArticle = feeArticle; }
   
    private double wheelBalanceWeight;
    public double getWheelBalanceWeight(){ return this.wheelBalanceWeight;}
    public void setWheelBalanceWeight(double wheelBalanceWeight){ this.wheelBalanceWeight = wheelBalanceWeight; }
   
    private String wheelWgtArticle;
    public String getWheelWgtArticle(){ return this.wheelWgtArticle;}
    public void setWheelWgtArticle(String wheelWgtArticle){ this.wheelWgtArticle = wheelWgtArticle; }
   
    private double wheelBalanceLabor;
    public double getWheelBalanceLabor(){ return this.wheelBalanceLabor;}
    public void setWheelBalanceLabor(double wheelBalanceLabor){ this.wheelBalanceLabor = wheelBalanceLabor; }
   
    private String wheelBalArticle;
    public String getWheelBalArticle(){ return this.wheelBalArticle;}
    public void setWheelBalArticle(String wheelBalArticle){ this.wheelBalArticle = wheelBalArticle; }
   
    private double valveStem;
    public double getValveStem(){ return this.valveStem;}
    public void setValveStem(double valveStem){ this.valveStem = valveStem; }
   
    private String valveStemArticle;
    public String getValveStemArticle(){ return this.valveStemArticle;}
    public void setValveStemArticle(String valveStemArticle){ this.valveStemArticle = valveStemArticle; }
   
    private double disposalPrice;
    public double getDisposalPrice(){ return this.disposalPrice;}
    public void setDisposalPrice(double disposalPrice){ this.disposalPrice = disposalPrice; }
   
    private String disposalFeeArticle;
    public String getDisposalFeeArticle(){ return this.disposalFeeArticle;}
    public void setDisposalFeeArticle(String disposalFeeArticle){ this.disposalFeeArticle = disposalFeeArticle; }
   
    private double tireInstallPrice;
    public double getTireInstallPrice(){ return this.tireInstallPrice;}
    public void setTireInstallPrice(double tireInstallPrice){ this.tireInstallPrice = tireInstallPrice; }
   
    private String tireInstallArticle;
    public String getTireInstallArticle(){ return this.tireInstallArticle;}
    public void setTireInstallArticle(String tireInstallArticle){ this.tireInstallArticle = tireInstallArticle; }

    //private boolean isAvailable;
    //public boolean getAvailable() { return this.isAvailable;}
    //public void setAvailable(boolean isAvailable) { this.isAvailable = isAvailable;}
    public boolean isAvailable() { return this.retailPrice > 0; }
    private int quantity;
    public int getQuantity() { return this.quantity;}
    public void setQuantity(int quantity) { this.quantity = quantity;}
    
    private double salePrice;
    public double getSalePrice() { return this.salePrice;}
    public void setSalePrice(double salePrice) { this.salePrice = salePrice;}
    
    private double priceTotal;
    public double getPriceTotal() { 
    	if(priceTotal <= 0)
    		return quantity*this.retailPrice;
    	return priceTotal;
    }
    public void setPriceTotal(double priceTotal) { this.priceTotal = priceTotal;}
    
    private double salePriceTotal;
    public double getSalePriceTotal() {
    	if(salePriceTotal <= 0)
    		return quantity*this.salePrice;
    	return this.salePriceTotal;
    }
    public void setSalePriceTotal(double salePriceTotal) { this.salePriceTotal = salePriceTotal;}
    
    private Tire rearTire;
    public Tire getRearTire() { return this.rearTire;}
    public void setRearTire(Tire rearTire) { this.rearTire = rearTire;}

    private boolean notBrandedProduct;
    public boolean isNotBrandedProduct() {
		return notBrandedProduct;
	}
	public void setNotBrandedProduct(boolean notBrandedProduct) {
		this.notBrandedProduct = notBrandedProduct;
	}
	
	
	public Tire clone() throws CloneNotSupportedException {
		Tire c = (Tire)super.clone();
		c.setArticle(article);
		c.setAspect(aspect);
		c.setBrand(brand);
		c.setCrossSection(crossSection);
		c.setDescription(description);
		c.setDiscontinued(discontinued);
		c.setDisplayId(displayId);
		c.setDisposalFeeArticle(disposalFeeArticle);
		c.setDisposalPrice(disposalPrice);
		c.setEndDate(endDate);
		c.setExciseTax(exciseTax);
		c.setExciseTaxArticle(exciseTaxArticle);
		c.setFeeArticle(disposalFeeArticle);
		c.setFeeDesc(feeDesc);
		c.setFrontRearBoth(frontRearBoth);
		c.setGenerateCatalogPage(generateCatalogPage);
		c.setLine(line);
		c.setLoadIndex(loadIndex);
		c.setLoadRange(loadRange);
		c.setMileage(mileage);
		c.setOemFlag(oemFlag);
		c.setOnSale(onSale);
		c.setPriceTotal(priceTotal);
		c.setQuantity(quantity);
		c.setQuantity(quantity);
		c.setRearArticle(rearArticle);
		c.setRearTire(c);
		c.setRetailPrice(retailPrice);
		c.setRimSize(rimSize);
		c.setSalePrice(salePrice);
		c.setSalePriceTotal(salePriceTotal);
		c.setSidewallDescription(sidewallDescription);
		c.setSpeedRating(speedRating);
		c.setStandardOptional(standardOptional);
		c.setTechnology(technology);
		c.setTireClassId(tireClassId);
		c.setTireClassName(tireClassName);
		c.setTireFee(tireFee);
		c.setTiregroupId(tireGroupId);
		c.setTireGroupName(tireGroupName);
		c.setTireInstallArticle(tireInstallArticle);
		c.setTireInstallPrice(tireInstallPrice);
		c.setTireName(tireName);
		c.setTireSegmentId(tireClassId);
		c.setTireSegmentName(tireSegmentName);
		c.setTireSize(tireSize);
		c.setValveStem(valveStem);
		c.setValveStemArticle(valveStemArticle);
		c.setWarrantyId(warrantyId);
		c.setWarrantyName(warrantyName);
		c.setWheelBalanceLabor(wheelBalanceLabor);
		c.setWheelBalanceWeight(wheelBalanceWeight);
		c.setWheelBalArticle(wheelBalArticle);
		c.setWheelWgtArticle(wheelWgtArticle);
		c.setNotBrandedProduct(notBrandedProduct);
		
		return c;
	}
	@Override
	public String toString() {
		return String.format("Tire [displayId=%s, tireName=%s, brand=%s, article=%s]",
				displayId, tireName, brand, article);
	}
    
}