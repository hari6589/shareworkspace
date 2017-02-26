package app.bsro.model.tire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

/**
 * @author smoorthy
 *
 */

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class TireSearchResults {
	
	private Long storeNumber;
	private Short quantity;
	private VehicleFitment vehicleFitment;
    private TireSize tireSize;
    private Map<Long,TireItems> tires;
    private List<String> allArticles;
    private List<String> standardArticles;
    private List<String> optionalArticles;
    private List<String> frontArticles;
    private List<String> rearArticles;
    private Map<String, List<String>> matchedSetArticles;
    private Map<String, List<String>> filters;
    private SeoVehicleData seoVehicleData;
    
    public TireSearchResults() {
    	
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

	public Map<Long,TireItems> getTires() {
		return tires;
	}

	public void setTires(Map<Long,TireItems> tires) {
		this.tires = tires;
	}

	public List<String> getAllArticles() {
		return allArticles;
	}

	public void setAllArticles(List<String> allArticles) {
		this.allArticles = allArticles;
	}

	public List<String> getStandardArticles() {
		return standardArticles;
	}

	public void setStandardArticles(List<String> standardArticles) {
		this.standardArticles = standardArticles;
	}

	public List<String> getOptionalArticles() {
		return optionalArticles;
	}

	public void setOptionalArticles(List<String> optionalArticles) {
		this.optionalArticles = optionalArticles;
	}

	public List<String> getFrontArticles() {
		return frontArticles;
	}

	public void setFrontArticles(List<String> frontArticles) {
		this.frontArticles = frontArticles;
	}

	public List<String> getRearArticles() {
		return rearArticles;
	}

	public void setRearArticles(List<String> rearArticles) {
		this.rearArticles = rearArticles;
	}

	public Map<String, List<String>> getMatchedSetArticles() {
		return matchedSetArticles;
	}

	public void setMatchedSetArticles(Map<String, List<String>> matchedSetArticles) {
		this.matchedSetArticles = matchedSetArticles;
	}

	public Map<String, List<String>> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, List<String>> filters) {
		this.filters = filters;
	}
	
	public SeoVehicleData getSeoVehicleData() {
		return seoVehicleData;
	}
	
	public void setSeoVehicleData(SeoVehicleData seoVehicleData) {
		this.seoVehicleData = seoVehicleData;
	}

}
