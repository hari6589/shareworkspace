package app.bsro.model.promotions;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@XmlRootElement(name="specialOffer")
public class SpecialOffer implements Comparable<SpecialOffer>{

	private Long id;
	private String description;
//	private String type;
	private String category;
	private String disclaimer;
	private String imageUrl;
	private String bannerUrl;
	private String landingUrl;
	private String offerEndDate;
	private String offerStartDate;
	private String friendlyId;
	private Long orderId;
	private String title;
	private String price;
	private String brand;
	
	private Boolean homePageOffer;
	private Boolean offerWithoutPrice;
	private String offerDescription;
	private String rebateText;
	
	private PromotionType promotionType;
	private String subtitleOne;
 	private String invalidator;
 	private String subtitleTwo;
 	private String brandLogoURL;
 	private String brandImageURL;
 	
 	private String imageThumbUrl;
 	private String stackFriendlyId;
 	private String thumbTitle;
 	private String thumbSubtitle;
 		
	public String getThumbTitle() {
		return thumbTitle;
	}
	public void setThumbTitle(String thumbTitle) {
		this.thumbTitle = thumbTitle;
	}
	public String getThumbSubtitle() {
		return thumbSubtitle;
	}
	public void setThumbSubtitle(String thumbSubtitle) {
		this.thumbSubtitle = thumbSubtitle;
	}
	public String getImageThumbUrl() {
		return imageThumbUrl;
	}
	public void setImageThumbUrl(String imageThumbUrl) {
		this.imageThumbUrl = imageThumbUrl;
	}
	@JsonSerialize(using=ToStringSerializer.class)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
	public String getDisclaimer() {
		return disclaimer;
	}
	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getBannerUrl() {
		return bannerUrl;
	}
	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}
	public String getLandingUrl() {
		return landingUrl;
	}
	public void setLandingUrl(String landingUrl) {
		this.landingUrl = landingUrl;
	}
	public String getOfferEndDate() {
		return offerEndDate;
	}
	public void setOfferEndDate(String offerEndDate) {
		this.offerEndDate = offerEndDate;
	}
	public String getOfferStartDate() {
		return offerStartDate;
	}
	public void setOfferStartDate(String offerStartDate) {
		this.offerStartDate = offerStartDate;
	}
	public String getFriendlyId() {
		return friendlyId;
	}
	public void setFriendlyId(String friendlyId) {
		this.friendlyId = friendlyId;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Boolean getHomePageOffer() {
		return homePageOffer;
	}
	public void setHomePageOffer(Boolean homePageOffer) {
		if(homePageOffer != null)
			this.homePageOffer = homePageOffer;
		else
			this.homePageOffer = false;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL, using=ToStringSerializer.class)
	public Boolean getOfferWithoutPrice() {
		return offerWithoutPrice;
	}
	
	public void setOfferWithoutPrice(Boolean offerWithoutPrice) {
		if(offerWithoutPrice != null)
			this.offerWithoutPrice = offerWithoutPrice;
		else
			this.offerWithoutPrice = false;
	}
	
	public String getOfferDescription() {
		return offerDescription;
	}
	public void setOfferDescription(String offerDescription) {
		this.offerDescription = offerDescription;
	}
	public String getRebateText() {
		return rebateText;
	}
	public void setRebateText(String rebateText) {
		this.rebateText = rebateText;
	}
	public PromotionType getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(PromotionType promotionType) {
		this.promotionType = promotionType;
	}
	public int compareTo(SpecialOffer o) {
		if (this.getOrderId() != null && o.getOrderId() != null)
			return this.getOrderId().compareTo(o.getOrderId());
		
		return this.getId().compareTo(o.getId());
	}
	
	public String getSubtitleOne() {
		return subtitleOne;
	}
	public void setSubtitleOne(String subtitleOne) {
		this.subtitleOne = subtitleOne;
	}
	public String getInvalidator() {
		return invalidator;
	}
	public void setInvalidator(String invalidator) {
		this.invalidator = invalidator;
	}
	public String getSubtitleTwo() {
		return subtitleTwo;
	}
	public void setSubtitleTwo(String subtitleTwo) {
		this.subtitleTwo = subtitleTwo;
	}
	public String getBrandLogoURL() {
		return brandLogoURL;
	}
	public void setBrandLogoURL(String brandLogoURL) {
		this.brandLogoURL = brandLogoURL;
	}
	public String getBrandImageURL() {
		return brandImageURL;
	}
	public void setBrandImageURL(String brandImageURL) {
		this.brandImageURL = brandImageURL;
	}
	
	public String getStackFriendlyId() {
		return stackFriendlyId;
	}

	public void setStackFriendlyId(String stackFriendlyId) {
		this.stackFriendlyId = stackFriendlyId;
	}
		
	
}
