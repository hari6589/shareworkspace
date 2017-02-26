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

public class PeriodicMaintenanceMilestones implements Serializable {
	private static final long serialVersionUID = -4279416948297758096L;
	private Long acesVehicleId;
	private int total;
	private List<TimeIntervalTypes> data;
	
	public PeriodicMaintenanceMilestones(){}
	public PeriodicMaintenanceMilestones(List<ServiceMaintenanceTO> records) {
		data = new ArrayList<TimeIntervalTypes>();
//		char[] delims = {' ','/','-'};
		String delims = "-,/, ";

//		String delims = "-,/, ,(,)";
		
		long lastTimeInterval = 0;
		String lastTimeUnits = "";
		String lastNote = "";
		Map<String, List<ComponentType>> operations = new HashMap<String, List<ComponentType>>();
		for(ServiceMaintenanceTO to : records) {
			setAcesVehicleId(to.getAcesVehicleId());
			
			long currTimeInterval = to.getTimeInterval().longValue();
			if(currTimeInterval > 0) {
				String currTimeUnits = WordUtils.capitalizeFully(StringUtils.trimToEmpty(to.getTimeUnits()), delims) ;
				String currOp = StringUtils.trimToEmpty(to.getOperationTypeTxt());
				String currComp = WordUtils.capitalizeFully(StringUtils.trimToEmpty(to.getComponentTxt()), delims) ;
				String currNote = StringUtils.trimToEmpty(to.getFootnoteTxt());
				
				//If the current and the last mileageIntervals are the same then we are just appending data
				// to the existing operations list.  Need to check to see if the operation type has changed
				// or not.
				if(lastTimeInterval == currTimeInterval) {
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
						TimeIntervalTypes timeIntervalTypes = new TimeIntervalTypes(lastTimeInterval, lastTimeUnits, operations, lastNote);
						addTimeIntervalType(timeIntervalTypes);
					}	
					
					operations = new HashMap<String, List<ComponentType>>();
					lastTimeInterval = currTimeInterval;
					lastTimeUnits = currTimeUnits;
					lastNote = currNote;
					List<ComponentType> components = new ArrayList<ComponentType>(1);
					components.add(new ComponentType(currComp, currNote));
					operations.put(currOp, components);
					
				}
			}
			
		}
		
		//Add the last one
		TimeIntervalTypes timeIntervalType = new TimeIntervalTypes(lastTimeInterval, lastTimeUnits, operations, lastNote);
		addTimeIntervalType(timeIntervalType);
		
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

	public List<TimeIntervalTypes> getData() {
		return data;
	}
	public void setData(List<TimeIntervalTypes> timeIntervalTypes) {
		this.data = timeIntervalTypes;
	}	
	
	private void addTimeIntervalType(TimeIntervalTypes timeIntervalType) {
		data.add(timeIntervalType);
	}
}
