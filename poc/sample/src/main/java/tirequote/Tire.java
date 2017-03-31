package tirequote;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Tire {
	
	private Long article;
	private Long rearArticle;
	private String brand;
	private String tireName;
	private String tireGroupName;
	private String tireClassName;
	private String tireSegmentName;
	private String tireSize;
	private String tireType;
	private Long loadIndex;
	private Long loadIndexPounds;
	private String loadRange;
	private String vehType;
	private String speedRating;
	private String speedRatingMPH;
	private String sidewallDescription;
	private Long mileage;
	private String technology;
	private String warrantyName;
	private String description;
	private Double retailPrice;
	private Double salePrice;
	private String onSale;
	private Boolean bestInClass;
	private String standardOptional;
    private String frb;
    private String generateCatalogPage;
    private String oemFlag;
    private String discontinued;
    private Boolean notBrandedProduct;
    private String tireBrandName;
	private String tireBrandImage;
	private String tireImage;
		
	public Tire() {
		
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getArticle() {
		return article;
	}
	
	public void setArticle(Long article) {
		this.article = article;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getRearArticle() {
		return rearArticle;
	}
	
	public void setRearArticle(Long rearArticle) {
		this.rearArticle = rearArticle;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getTireName() {
		return tireName;
	}

	public void setTireName(String tireName) {
		this.tireName = tireName;
	}

	public String getTireGroupName() {
		return tireGroupName;
	}

	public void setTireGroupName(String tireGroupName) {
		this.tireGroupName = tireGroupName;
	}

	public String getTireClassName() {
		return tireClassName;
	}

	public void setTireClassName(String tireClassName) {
		this.tireClassName = tireClassName;
	}

	public String getTireSegmentName() {
		return tireSegmentName;
	}

	public void setTireSegmentName(String tireSegmentName) {
		this.tireSegmentName = tireSegmentName;
	}

	public String getTireSize() {
		return tireSize;
	}

	public void setTireSize(String tireSize) {
		this.tireSize = tireSize;
	}

	public String getTireType() {
		return tireType;
	}

	public void setTireType(String tireType) {
		this.tireType = tireType;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getLoadIndex() {
		return loadIndex;
	}

	public void setLoadIndex(Long loadIndex) {
		this.loadIndex = loadIndex;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getLoadIndexPounds() {
		return loadIndexPounds;
	}

	public void setLoadIndexPounds(Long loadIndexPounds) {
		this.loadIndexPounds = loadIndexPounds;
	}
	
	public String getLoadRange() {
		return loadRange;
	}
	
	public void setLoadRange(String loadRange) {
		this.loadRange = loadRange;
	}

	public String getVehType() {
		return vehType;
	}

	public void setVehType(String vehType) {
		this.vehType = vehType;
	}

	public String getSpeedRating() {
		return speedRating;
	}

	public void setSpeedRating(String speedRating) {
		this.speedRating = speedRating;
	}

	public String getSpeedRatingMPH() {
		return speedRatingMPH;
	}

	public void setSpeedRatingMPH(String speedRatingMPH) {
		this.speedRatingMPH = speedRatingMPH;
	}

	public String getSidewallDescription() {
		return sidewallDescription;
	}

	public void setSidewallDescription(String sidewallDescription) {
		this.sidewallDescription = sidewallDescription;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getMileage() {
		return mileage;
	}

	public void setMileage(Long mileage) {
		this.mileage = mileage;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public String getWarrantyName() {
		return warrantyName;
	}

	public void setWarrantyName(String warrantyName) {
		this.warrantyName = warrantyName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(Double retailPrice) {
		this.retailPrice = retailPrice;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}

	public String getOnSale() {
		return onSale;
	}

	public void setOnSale(String onSale) {
		this.onSale = onSale;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Boolean getBestInClass() {
		return bestInClass;
	}

	public void setBestInClass(Boolean bestInClass) {
		this.bestInClass = bestInClass;
	}

	public String getStandardOptional() {
		return standardOptional;
	}

	public void setStandardOptional(String standardOptional) {
		this.standardOptional = standardOptional;
	}

	public String getFrb() {
		return frb;
	}

	public void setFrb(String frb) {
		this.frb = frb;
	}
	
	public String getGenerateCatalogPage() {
		return generateCatalogPage;
	}
	
	public void setGenerateCatalogPage(String generateCatalogPage) {
		this.generateCatalogPage = generateCatalogPage;
	}

	public String getOemFlag() {
		return oemFlag;
	}

	public void setOemFlag(String oemFlag) {
		this.oemFlag = oemFlag;
	}

	public String getDiscontinued() {
		return discontinued;
	}

	public void setDiscontinued(String discontinued) {
		this.discontinued = discontinued;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Boolean getNotBrandedProduct() {
		return notBrandedProduct;
	}

	public void setNotBrandedProduct(Boolean notBrandedProduct) {
		this.notBrandedProduct = notBrandedProduct;
	}

	public String getTireBrandName() {
		return tireBrandName;
	}

	public void setTireBrandName(String tireBrandName) {
		this.tireBrandName = tireBrandName;
	}

	public String getTireBrandImage() {
		return tireBrandImage;
	}

	public void setTireBrandImage(String tireBrandImage) {
		this.tireBrandImage = tireBrandImage;
	}

	public String getTireImage() {
		return tireImage;
	}

	public void setTireImage(String tireImage) {
		this.tireImage = tireImage;
	}

	@Override
	public String toString() {
		return "Tire [article=" + article + ", rearArticle=" + rearArticle
				+ ", brand=" + brand + ", tireName=" + tireName
				+ ", tireGroupName=" + tireGroupName + ", tireClassName="
				+ tireClassName + ", tireSegmentName=" + tireSegmentName
				+ ", tireSize=" + tireSize + ", tireType=" + tireType
				+ ", loadIndex=" + loadIndex + ", loadIndexPounds="
				+ loadIndexPounds + ", loadRange=" + loadRange + ", vehType="
				+ vehType + ", speedRating=" + speedRating
				+ ", speedRatingMPH=" + speedRatingMPH
				+ ", sidewallDescription=" + sidewallDescription + ", mileage="
				+ mileage + ", technology=" + technology + ", warrantyName="
				+ warrantyName + ", description=" + description
				+ ", retailPrice=" + retailPrice + ", salePrice=" + salePrice
				+ ", onSale=" + onSale + ", bestInClass=" + bestInClass
				+ ", standardOptional=" + standardOptional + ", frb=" + frb
				+ ", generateCatalogPage=" + generateCatalogPage + ", oemFlag="
				+ oemFlag + ", discontinued=" + discontinued
				+ ", notBrandedProduct=" + notBrandedProduct
				+ ", tireBrandName=" + tireBrandName + ", tireBrandImage="
				+ tireBrandImage + ", tireImage=" + tireImage + "]";
	}
	
}
