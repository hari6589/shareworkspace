package tirequote;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TireQuote {

	private Long tireQuoteId;
	private Long storeNumber;
    private Short quantity;
    private Short rearQuantity;
    private Date createdDate;
    private String firstName;
    private String lastName;
    private String emailAddress;
    
    private VehicleFitment vehicleFitment;
    private TireSize tireSize;
    private Tire tire;
    private Tire rearTire;
    private TirePromotion tirePromotion;
    private StoreInventory storeInventory;
    private QuoteItem quoteItem;
    
    private String emptyCart;
        
    public TireQuote() {
    	
    }
    
    public Long getTireQuoteId() {
		return tireQuoteId;
	}
	
	public void setTireQuoteId(Long tireQuoteId) {
		this.tireQuoteId = tireQuoteId;
	}

	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public Short getQuantity() {
		return quantity;
	}

	public void setQuantity(Short quantity) {
		this.quantity = quantity;
	}
	
	public Short getRearQuantity() {
		return rearQuantity;
	}

	public void setRearQuantity(Short rearQuantity) {
		this.rearQuantity = rearQuantity;
	}

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
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Tire getTire() {
		return tire;
	}

	public void setTire(Tire tire) {
		this.tire = tire;
	}
	
	public Tire getRearTire() {
		return rearTire;
	}

	public void setRearTire(Tire rearTire) {
		this.rearTire = rearTire;
	}

	public VehicleFitment getVehicleFitment() {
		return vehicleFitment;
	}

	public void setVehicleFitment(VehicleFitment vehicleFitment) {
		this.vehicleFitment = vehicleFitment;
	}

	public TireSize getTireSize() {
		return tireSize;
	}

	public void setTireSize(TireSize tireSize) {
		this.tireSize = tireSize;
	}

	public TirePromotion getTirePromotion() {
		return tirePromotion;
	}

	public void setTirePromotion(TirePromotion tirePromotion) {
		this.tirePromotion = tirePromotion;
	}
	
	public StoreInventory getStoreInventory() {
		return storeInventory;
	}

	public void setStoreInventory(StoreInventory storeInventory) {
		this.storeInventory = storeInventory;
	}

	public QuoteItem getQuoteItem() {
		return quoteItem;
	}

	public void setQuoteItem(QuoteItem quoteItem) {
		this.quoteItem = quoteItem;
	}

	public String getEmptyCart() {
		return emptyCart;
	}

	public void setEmptyCart(String emptyCart) {
		this.emptyCart = emptyCart;
	}

	@Override
	public String toString() {
		return "TireQuote [tireQuoteId=" + tireQuoteId + ", storeNumber="
				+ storeNumber + ", quantity=" + quantity + ", rearQuantity="
				+ rearQuantity + ", createdDate=" + createdDate
				+ ", firstName=" + firstName + ", lastName=" + lastName
				+ ", emailAddress=" + emailAddress + ", vehicleFitment="
				+ vehicleFitment + ", tireSize=" + tireSize + ", tire=" + tire
				+ ", rearTire=" + rearTire + ", tirePromotion=" + tirePromotion
				+ ", quoteItem=" + quoteItem + ", emptyCart=" + emptyCart + "]";
	}
	
	

}
