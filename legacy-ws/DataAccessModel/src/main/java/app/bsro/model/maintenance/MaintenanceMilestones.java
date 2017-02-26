package app.bsro.model.maintenance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

import org.apache.commons.lang.StringUtils;

import app.bsro.model.util.WordUtils;

public class MaintenanceMilestones implements Serializable {
	private static final long serialVersionUID = 4719836332784929523L;
	private Long acesVehicleId;
	private int total;
	private List<MileageTypes> data;
	
	public MaintenanceMilestones(){}
	public MaintenanceMilestones(List<ServiceMaintenanceTO> records) {
		data = new ArrayList<MileageTypes>();
		String delims = "-,/, ";

//		String delims = "-,/, ,(,)";
		
		long lastMileage = 0;
		String note = "";
		Map<String, List<ComponentType>> operations = new HashMap<String, List<ComponentType>>();
		for(ServiceMaintenanceTO to : records) {
			setAcesVehicleId(to.getAcesVehicleId());
			
			long currMileage = to.getMileageInterval().longValue();
			String currNote = to.getFootnoteTxt();
			String currOp = StringUtils.trimToEmpty(to.getOperationTypeTxt());
			String currComp = WordUtils.capitalizeFully(StringUtils.trimToEmpty(to.getComponentTxt()), delims) ;

			//If the current and the last mileageIntervals are the same then we are just appending data
			// to the existing operations list.  Need to check to see if the operation type has changed
			// or not.
			if(lastMileage == currMileage) {
				List<ComponentType> components = operations.get(currOp);
				if(components == null) {
					components = new ArrayList<ComponentType>();
					components.add(new ComponentType(currComp, currNote));
					operations.put(currOp, components);
				} else {
					components.add(new ComponentType(currComp, currNote));
				}
			} else {
				//We have changed mileageIntervals so add the last one to the mileageTypes list and start
				// a new one.
				if(operations.size() > 0) {
					MileageTypes mileageTypes = new MileageTypes(lastMileage, operations, note);
					addMileageType(mileageTypes);
				}	
				
				operations = new HashMap<String, List<ComponentType>>();
				lastMileage = currMileage;
				note = currNote;
				List<ComponentType> components = new ArrayList<ComponentType>(1);
				components.add(new ComponentType(currComp, currNote));
				operations.put(currOp, components);
				
			}
			
		}
		
		//Add the last one
		MileageTypes mileageTypes = new MileageTypes(lastMileage, operations, note);
		addMileageType(mileageTypes);
		
		setTotal(getData().size());
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getAcesVehicleId() {
		return acesVehicleId;
	}
	public void setAcesVehicleId(Long acesVehicleId) {
		this.acesVehicleId = acesVehicleId;
	}
	
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}

	public List<MileageTypes> getData() {
		return data;
	}
	public void setData(List<MileageTypes> mileageTypes) {
		this.data = mileageTypes;
	}	
	
	private void addMileageType(MileageTypes mileageType) {
		data.add(mileageType);
	}
}
