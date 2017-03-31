package tirequote;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VehicleFitment {
	
	private String year;
    private String make;
    private String model;
    private String submodel;
    private Long acesVehicleId;
    private boolean tpmsInd;
        
    public VehicleFitment() {
    	
    }
    
	public String getYear() {
		return year;
	}
	
	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSubmodel() {
		return submodel;
	}

	public void setSubmodel(String submodel) {
		this.submodel = submodel;
	}

	public Long getAcesVehicleId() {
		return acesVehicleId;
	}
	
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	
	public boolean isTpmsInd() {
		return tpmsInd;
	}

	public void setTpmsInd(boolean tpmsInd) {
		this.tpmsInd = tpmsInd;
	}

	@Override
	public String toString() {
		return "VehicleFitment [year=" + year + ", make=" + make + ", model="
				+ model + ", submodel=" + submodel + ", acesVehicleId="
				+ acesVehicleId + ", tpmsInd=" + tpmsInd + "]";
	}

}
