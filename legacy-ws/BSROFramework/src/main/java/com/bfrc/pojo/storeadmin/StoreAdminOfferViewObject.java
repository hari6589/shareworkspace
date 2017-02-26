package com.bfrc.pojo.storeadmin;

import com.bfrc.security.Encode;

public class StoreAdminOfferViewObject implements Comparable<StoreAdminOfferViewObject> {
	private StoreAdminOffer offer;
	private StoreAdminOfferTemplate template;
	private StoreAdminOfferTemplateImages templateImages;
	private String templateNameEncoded;
	private String promotionType;

	public StoreAdminOfferViewObject() {
	}
	
	public StoreAdminOfferViewObject(StoreAdminOffer offer, StoreAdminOfferTemplate template) {
		setOffer(offer);
		setTemplate(template);
	}
	
	public StoreAdminOfferViewObject(StoreAdminOffer offer, StoreAdminOfferTemplate template, StoreAdminOfferTemplateImages templateImages) {
		setOffer(offer);
		setTemplate(template);
		setTemplateImages(templateImages);
	}
	
	public StoreAdminOfferViewObject(String promotionType, StoreAdminOffer offer, StoreAdminOfferTemplate template, StoreAdminOfferTemplateImages templateImages) {
		setOffer(offer);
		setTemplate(template);
		setTemplateImages(templateImages);
		setPromotionType(promotionType);
	}

	public StoreAdminOffer getOffer() {
		return offer;
	}

	public void setOffer(StoreAdminOffer offer) {
		this.offer = offer;
	}

	public StoreAdminOfferTemplate getTemplate() {
		return template;
	}

	public void setTemplate(StoreAdminOfferTemplate storeAdminOfferTemplate) {
		this.template = storeAdminOfferTemplate;
		templateNameEncoded = storeAdminOfferTemplate.getName() == null ? "" : Encode.html(storeAdminOfferTemplate.getName().replace("'","\\'").replace("%", "percent"));
	}
	
	public StoreAdminOfferTemplateImages getTemplateImages() {
		return templateImages;
	}

	public void setTemplateImages(StoreAdminOfferTemplateImages storeAdminOfferTemplateImages) {
		this.templateImages = storeAdminOfferTemplateImages;
	}

	public String getTemplateNameEncoded() {
		return templateNameEncoded;
	}

	public void setTemplateNameEncoded(String templateNameEncoded) {
		this.templateNameEncoded = templateNameEncoded;
	}
	
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	@Override
	public int compareTo(StoreAdminOfferViewObject o) {
		if (("banner".equalsIgnoreCase(o.getPromotionType()) || "priority".equalsIgnoreCase(o.getPromotionType())) && o.getOffer().getPriority() == null)
			return 1;
		if (("banner".equalsIgnoreCase(getPromotionType()) || "priority".equalsIgnoreCase(getPromotionType())) && getOffer().getPriority() == null)
			return 1;
		int compare = 1;
		
		if ("banner".equalsIgnoreCase(getPromotionType()) || "priority".equalsIgnoreCase(getPromotionType()))
			compare = getOffer().getPriority().compareTo(o.getOffer().getPriority());
		else
			compare = getOffer().getTemplateId().compareTo(o.getOffer().getTemplateId());
		
		return compare;
	}
}
