package tirequote;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuoteItem {
	
	private int totalUnits;							// total units or quantity - number of tires.
	private double unitPrice;							// price per unit (without discount/regular price).
	private double totalTirePrice;						// price for 'n'/quanity tires (without discount/regular price).
	
	private double rearTotalUnits;						// REAR total units or quantity - number of tires.
	private double rearUnitPrice;						// REAR price per unit (without discount/regular price).
	private double rearTotalTirePrice;					// REAR price for 'n'/quanity tires (without discount/regular price).
	
	private double discount;							// discount price total.
	
	private double tireMounting;						// tire mounting fee. FREE for regular stores & $80 for Mobile Tire Installation stores
	private double wheelBalance;						// wheel balance cost for 'n' tires
	private double valveStem;
	private double tpmsValveServiceKit;
	private double tpmsValveServiceKitLabor;
	private double exciseFee;
	private double stateEnvironmentalFee;
	private double scrapTireRecyclingCharge;
	private double shopSupplies;
	private double tax;
	private double total;
	
	public QuoteItem() {
		
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getTotalUnits() {
		return totalUnits;
	}
	
	public void setTotalUnits(int totalUnits) {
		this.totalUnits = totalUnits;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getUnitPrice() {
		return unitPrice;
	}
	
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTotalTirePrice() {
		return totalTirePrice;
	}
	
	public void setTotalTirePrice(double totalTirePrice) {
		this.totalTirePrice = totalTirePrice;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getRearTotalUnits() {
		return rearTotalUnits;
	}
	
	public void setRearTotalUnits(double rearTotalUnits) {
		this.rearTotalUnits = rearTotalUnits;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getRearUnitPrice() {
		return rearUnitPrice;
	}
	
	public void setRearUnitPrice(double rearUnitPrice) {
		this.rearUnitPrice = rearUnitPrice;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getRearTotalTirePrice() {
		return rearTotalTirePrice;
	}
	
	public void setRearTotalTirePrice(double rearTotalTirePrice) {
		this.rearTotalTirePrice = rearTotalTirePrice;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTireMounting() {
		return tireMounting;
	}
	
	public void setTireMounting(double tireMounting) {
		this.tireMounting = tireMounting;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getWheelBalance() {
		return wheelBalance;
	}
	
	public void setWheelBalance(double wheelBalance) {
		this.wheelBalance = wheelBalance;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getValveStem() {
		return valveStem;
	}
	
	public void setValveStem(double valveStem) {
		this.valveStem = valveStem;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTpmsValveServiceKit() {
		return tpmsValveServiceKit;
	}
	
	public void setTpmsValveServiceKit(double tpmsValveServiceKit) {
		this.tpmsValveServiceKit = tpmsValveServiceKit;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTpmsValveServiceKitLabor() {
		return tpmsValveServiceKitLabor;
	}
	
	public void setTpmsValveServiceKitLabor(double tpmsValveServiceKitLabor) {
		this.tpmsValveServiceKitLabor = tpmsValveServiceKitLabor;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getExciseFee() {
		return exciseFee;
	}

	public void setExciseFee(double exciseFee) {
		this.exciseFee = exciseFee;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getStateEnvironmentalFee() {
		return stateEnvironmentalFee;
	}
	
	public void setStateEnvironmentalFee(double stateEnvironmentalFee) {
		this.stateEnvironmentalFee = stateEnvironmentalFee;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getScrapTireRecyclingCharge() {
		return scrapTireRecyclingCharge;
	}
	
	public void setScrapTireRecyclingCharge(double scrapTireRecyclingCharge) {
		this.scrapTireRecyclingCharge = scrapTireRecyclingCharge;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getShopSupplies() {
		return shopSupplies;
	}
	
	public void setShopSupplies(double shopSupplies) {
		this.shopSupplies = shopSupplies;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTax() {
		return tax;
	}
	
	public void setTax(double tax) {
		this.tax = tax;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "QuoteItem [totalUnits=" + totalUnits + ", unitPrice="
				+ unitPrice + ", totalTirePrice=" + totalTirePrice
				+ ", rearTotalUnits=" + rearTotalUnits + ", rearUnitPrice="
				+ rearUnitPrice + ", rearTotalTirePrice=" + rearTotalTirePrice
				+ ", discount=" + discount + ", tireMounting=" + tireMounting
				+ ", wheelBalance=" + wheelBalance + ", valveStem=" + valveStem
				+ ", tpmsValveServiceKit=" + tpmsValveServiceKit
				+ ", tpmsValveServiceKitLabor=" + tpmsValveServiceKitLabor
				+ ", exciseFee=" + exciseFee + ", stateEnvironmentalFee="
				+ stateEnvironmentalFee + ", scrapTireRecyclingCharge="
				+ scrapTireRecyclingCharge + ", shopSupplies=" + shopSupplies
				+ ", tax=" + tax + ", total=" + total + "]";
	}
	
}
