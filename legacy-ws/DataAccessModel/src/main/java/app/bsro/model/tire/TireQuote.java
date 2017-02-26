package app.bsro.model.tire;

import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;
import org.codehaus.jackson.JsonNode;

import com.bfrc.dataaccess.model.inventory.StoreInventory;

import app.bsro.model.util.DateUtils;
import app.bsro.model.util.DateTimeDeserializeUtil;

/**
 * @author smoorthy
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
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
    
    @JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getTireQuoteId() {
		return tireQuoteId;
	}
	
	public void setTireQuoteId(Long tireQuoteId) {
		this.tireQuoteId = tireQuoteId;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Short getQuantity() {
		return quantity;
	}

	public void setQuantity(Short quantity) {
		this.quantity = quantity;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Short getRearQuantity() {
		return rearQuantity;
	}

	public void setRearQuantity(Short rearQuantity) {
		this.rearQuantity = rearQuantity;
	}

	@JsonSerialize(using = DateUtils.class)
	public Date getCreatedDate() {
		return createdDate;
	}
	
	@JsonDeserialize(using = DateTimeDeserializeUtil.class)
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

}
