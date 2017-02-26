package app.bsro.model.oil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class OilChangeSearchResult implements Serializable {
	private static final long serialVersionUID = 4666323341983706030L;
	
	private String vehicleId;
	private Long storeNumber;
	private BigDecimal vehicleOilCapacity;
	
	private List<OilChangePackage> oilChangePackages;

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	
	public Long getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(Long storeNumber) {
		this.storeNumber = storeNumber;
	}

	public List<OilChangePackage> getOilChangePackages() {
		return oilChangePackages;
	}

	public void setOilChangePackages(List<OilChangePackage> oilChangePackages) {
		this.oilChangePackages = oilChangePackages;
	}

	public BigDecimal getVehicleOilCapacity() {
		return vehicleOilCapacity;
	}

	public void setVehicleOilCapacity(BigDecimal vehicleOilCapacity) {
		this.vehicleOilCapacity = vehicleOilCapacity;
	}
}
