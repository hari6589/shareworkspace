package app.bsro.model.scheduled.maintenance.ncd.rtq;

import javax.xml.bind.annotation.XmlElement;

public class JobResult implements Comparable<JobResult>{
	
	//Returns for the Job Listing (RTQ2030)
	private String invoiceDate;
	private Integer invoiceNbr;
	private String companyIndicator;
	private String storeNbr;
	private String storeAddress;
	private String storeCity;
	private String storeState;
	private String storeZip;
	private Integer mileage;
	private Double invoiceAmt;
	private String description;
	private String status;
	private Integer articleNbr;
	private Double sequencNbr;
	private String itemDescription;
	private Integer itemType;
	private Integer qty;
	private Double itemPrice;
	
	
	@XmlElement(name="XINVOICEDATE")
	public String getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(String invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	
	@XmlElement(name="XINVOICENBR")
	public Integer getInvoiceNbr() {
		return invoiceNbr;
	}
	public void setInvoiceNbr(Integer invoiceNbr) {
		this.invoiceNbr = invoiceNbr;
	}
	
	@XmlElement(name="XCOMPANY")
	public String getCompanyIndicator() {
		return companyIndicator;
	}
	public void setCompanyIndicator(String companyIndicator) {
		this.companyIndicator = companyIndicator;
	}
	
	@XmlElement(name="XSTORENBR")
	public String getStoreNbr() {
		return storeNbr;
	}
	public void setStoreNbr(String storeNbr) {
		this.storeNbr = storeNbr;
	}
	
	@XmlElement(name="XADDRESS")
	public String getStoreAddress() {
		return storeAddress;
	}
	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}
	
	@XmlElement(name="XCITY")
	public String getStoreCity() {
		return storeCity;
	}
	public void setStoreCity(String storeCity) {
		this.storeCity = storeCity;
	}
	
	@XmlElement(name="XSTATE")
	public String getStoreState() {
		return storeState;
	}
	public void setStoreState(String storeState) {
		this.storeState = storeState;
	}
	
	@XmlElement(name="XMILEAGE")
	public Integer getMileage() {
		return mileage;
	}
	public void setMileage(Integer mileage) {
		this.mileage = mileage;
	}
	
	@XmlElement(name="XINVOICEAMOUNT")
	public Double getInvoiceAmt() {
		return invoiceAmt;
	}
	public void setInvoiceAmt(Double invoiceAmt) {
		this.invoiceAmt = invoiceAmt;
	}
	
	@XmlElement(name="XJOBDESC")
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = uncleanXml(description);
	}
	
	@XmlElement(name="XSTATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name="XARTICLENBR")
	public Integer getArticleNbr() {
		return articleNbr;
	}
	public void setArticleNbr(Integer articleNbr) {
		this.articleNbr = articleNbr;
	}
	
	@XmlElement(name="XSEQUENCENBR")
	public Double getSequencNbr() {
		return sequencNbr;
	}
	public void setSequencNbr(Double sequencNbr) {
		this.sequencNbr = sequencNbr;
	}
	
	@XmlElement(name="XITEMDESC")
	public String getItemDescription() {
		return itemDescription;
	}
	public void setItemDescription(String itemDescription) {
		this.itemDescription = uncleanXml(itemDescription);
	}
	
	@XmlElement(name="XPRODTYPE")
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
	@XmlElement(name="XQUANTITY")
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	
	@XmlElement(name="XPRICE")
	public Double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}
	
	public void setStoreZip(String storeZip) {
		this.storeZip = storeZip;
	}
	
	@XmlElement(name="XZIPCODE")
	public String getStoreZip() {
		return storeZip;
	}
	
	@Override
	public String toString() {
		return "EDWRTQRecord [invoiceDate="
				+ invoiceDate + ", invoiceNbr=" + invoiceNbr
				+ ", companyIndicator=" + companyIndicator + ", storeNbr="
				+ storeNbr + ", storeAddress=" + storeAddress + ", storeCity="
				+ storeCity + ", storeState=" + storeState + ", mileage="
				+ mileage + ", invoiceAmt=" + invoiceAmt + ", description="
				+ description + ", status=" + status + ", articleNbr="
				+ articleNbr + ", sequencNbr=" + sequencNbr
				+ ", itemDescription=" + itemDescription + ", itemType="
				+ itemType + ", qty=" + qty + ", itemPrice=" + itemPrice + "]";
	}
	
	private String uncleanXml(String value) {
		if(value == null) return value;
		value = value.replaceAll("&amp;", "&");
		 value = value.replaceAll("&apos;", "'");
		 value = value.replaceAll("&gt;", ">");
		 value = value.replaceAll("&lt;", "<");
		 
		 return value;
	}
	
	public int compareTo(JobResult o) {
		if(o == null || o.getInvoiceNbr() == null || this.getInvoiceNbr() == null) return -1;
		else return this.getInvoiceNbr().compareTo(o.getInvoiceNbr());
	}
	
}
