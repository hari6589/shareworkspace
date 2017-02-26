package app.bsro.model.battery;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import app.bsro.model.util.DateUtils;

import com.bfrc.dataaccess.model.vehicle.GenericVehicle;

/**
 * Abstracted view of battery  quote. This does not exactly map to the record in the database, because we don't actually
 * use all of the columns when presenting a quote, instead calculating the battery information on the fly.
 * @author mholmes
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class BatteryQuote {
    private long batteryQuoteId;
    private Long storeNumber;
    private String zip;
    private Date createdDate;
    private String firstName;
    private String lastName;
    private String donationName;
    private Double donationAmount;
    private String donationArticle;
    private Short quantity;
    private Double priceForQuantity;
    private Double installationForQuantity;
    private Double subtotal;
    private Double total;
    private Boolean isEligibleForBatteryRebate;
    private Double batteryRebateAmount;
    private Date batteryRebateExpirationDate;
    private Double totalAfterRebate;
    
    private Battery battery;
    private GenericVehicle vehicle;
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public long getBatteryQuoteId() {
		return batteryQuoteId;
	}
	public void setBatteryQuoteId(long batteryQuoteId) {
		this.batteryQuoteId = batteryQuoteId;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	@JsonSerialize(using = DateUtils.class)
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDonationName() {
		return donationName;
	}
	public void setDonationName(String donationName) {
		this.donationName = donationName;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getDonationAmount() {
		return donationAmount;
	}
	public void setDonationAmount(Double donationAmount) {
		this.donationAmount = donationAmount;
	}
	public String getDonationArticle() {
		return donationArticle;
	}
	public void setDonationArticle(String donationArticle) {
		this.donationArticle = donationArticle;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getSubtotal() {
		return subtotal;
	}
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getTotalAfterRebate() {
		return totalAfterRebate;
	}
	public void setTotalAfterRebate(Double totalAfterRebate) {
		this.totalAfterRebate = totalAfterRebate;
	}
	public Battery getBattery() {
		return battery;
	}
	public void setBattery(Battery battery) {
		this.battery = battery;
	}
	public GenericVehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(GenericVehicle vehicle) {
		this.vehicle = vehicle;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getBatteryRebateAmount() {
		return batteryRebateAmount;
	}
	public void setBatteryRebateAmount(Double batteryRebateAmount) {
		this.batteryRebateAmount = batteryRebateAmount;
	}
	public Date getBatteryRebateExpirationDate() {
		return batteryRebateExpirationDate;
	}
	public void setBatteryRebateExpirationDate(Date batteryRebateExpirationDate) {
		this.batteryRebateExpirationDate = batteryRebateExpirationDate;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Boolean getIsEligibleForBatteryRebate() {
		return isEligibleForBatteryRebate;
	}
	public void setIsEligibleForBatteryRebate(Boolean isEligibleForBatteryRebate) {
		this.isEligibleForBatteryRebate = isEligibleForBatteryRebate;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Short getQuantity() {
		return quantity;
	}
	public void setQuantity(Short quantity) {
		this.quantity = quantity;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getPriceForQuantity() {
		return priceForQuantity;
	}
	public void setPriceForQuantity(Double priceForQuantity) {
		this.priceForQuantity = priceForQuantity;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Double getInstallationForQuantity() {
		return installationForQuantity;
	}
	public void setInstallationForQuantity(Double installationForQuantity) {
		this.installationForQuantity = installationForQuantity;
	}	
}
