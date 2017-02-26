package app.bsro.model.promotions;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class SpecialOffers {

	private List<SpecialOffer> tirePromotions;
	private List<SpecialOffer> coupons;
	private List<SpecialOffer> promotions;
	
	public SpecialOffers(){}
	public SpecialOffers(List<SpecialOffer> tire, List<SpecialOffer> coupon, List<SpecialOffer> promotion) {
		setTirePromotions(tire);
		setCoupons(coupon);
		setPromotions(promotion);
	}
	
	public void setCoupons(List<SpecialOffer> coupons) {
		this.coupons = coupons;
	}
	public void setPromotions(List<SpecialOffer> promotions) {
		this.promotions = promotions;
	}
	public void setTirePromotions(List<SpecialOffer> tirePromotions) {
		this.tirePromotions = tirePromotions;
	}
	public List<SpecialOffer> getCoupons() {
		return coupons;
	}
	public List<SpecialOffer> getPromotions() {
		return promotions;
	}
	public List<SpecialOffer> getTirePromotions() {
		return tirePromotions;
	}
}
