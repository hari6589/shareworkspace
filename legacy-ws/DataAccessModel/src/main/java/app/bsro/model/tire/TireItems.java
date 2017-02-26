package app.bsro.model.tire;

import com.bfrc.dataaccess.model.inventory.StoreInventory;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author smoorthy
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TireItems {
	
	private Tire tire;
	private TirePromotion tirePromotion;
    private StoreInventory storeInventory;
    private MindshareTiresurveyDetails tireSurveyDetails;
    
    public TireItems() {
    	
    }

	public Tire getTire() {
		return tire;
	}

	public void setTire(Tire tire) {
		this.tire = tire;
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

	public MindshareTiresurveyDetails getTireSurveyDetails() {
		return tireSurveyDetails;
	}

	public void setTireSurveyDetails(MindshareTiresurveyDetails tireSurveyDetails) {
		this.tireSurveyDetails = tireSurveyDetails;
	}

}
