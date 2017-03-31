package tirequote;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
//@JsonIgnoreProperties({"promoId","promoName", "promoDisplayName", "rebateOffers", "discountOffers"})
@JsonIgnoreProperties(ignoreUnknown=true)
public class TirePromotion {
	
	private double discountAmount;
	private double frontDiscountAmount;
	private double frontDiscountAmountTotal;
	private double rearDiscountAmount;
	private double rearDiscountAmountTotal;
	private Long promoId;
	private String promoName;
	private String promoDisplayName;
	private List rebateOffers;
	private List discountOffers;
	private List offers;
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getDiscountAmount() {
		return discountAmount;
	}
	
	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getFrontDiscountAmount() {
		return frontDiscountAmount;
	}
	
	public void setFrontDiscountAmount(double frontDiscountAmount) {
		this.frontDiscountAmount = frontDiscountAmount;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getFrontDiscountAmountTotal() {
		return frontDiscountAmountTotal;
	}
	
	public void setFrontDiscountAmountTotal(double frontDiscountAmountTotal) {
		this.frontDiscountAmountTotal = frontDiscountAmountTotal;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getRearDiscountAmount() {
		return rearDiscountAmount;
	}
	
	public void setRearDiscountAmount(double rearDiscountAmount) {
		this.rearDiscountAmount = rearDiscountAmount;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getRearDiscountAmountTotal() {
		return rearDiscountAmountTotal;
	}
	
	public void setRearDiscountAmountTotal(double rearDiscountAmountTotal) {
		this.rearDiscountAmountTotal = rearDiscountAmountTotal;
	}
	
	public List getRebateOffers() {
		return rebateOffers;
	}
	
	public void setRebateOffers(List rebateOffers) {
		this.rebateOffers = rebateOffers;
	}
	
	public List getDiscountOffers() {
		return discountOffers;
	}
	
	public void setDiscountOffers(List discountOffers) {
		this.discountOffers = discountOffers;
	}
	
	public List getOffers() {
		return offers;
	}
	
	public void setOffers(List offers) {
		this.offers = offers;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getPromoId() {
		return promoId;
	}
	
	public void setPromoId(Long promoId) {
		this.promoId = promoId;
	}
	
	public String getPromoName() {
		return promoName;
	}
	
	public void setPromoName(String promoName) {
		this.promoName = promoName;
	}
	
	public String getPromoDisplayName() {
		return promoDisplayName;
	}
	
	public void setPromoDisplayName(String promoDisplayName) {
		this.promoDisplayName = promoDisplayName;
	}

	@Override
	public String toString() {
		return "TirePromotion [discountAmount=" + discountAmount
				+ ", frontDiscountAmount=" + frontDiscountAmount
				+ ", frontDiscountAmountTotal=" + frontDiscountAmountTotal
				+ ", rearDiscountAmount=" + rearDiscountAmount
				+ ", rearDiscountAmountTotal=" + rearDiscountAmountTotal
				+ ", promoId=" + promoId + ", promoName=" + promoName
				+ ", promoDisplayName=" + promoDisplayName + ", rebateOffers="
				+ rebateOffers + ", discountOffers=" + discountOffers + "]";
	}

}
