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

public class ScheduledMaintenances implements Serializable {
	private static final long serialVersionUID = 6595595373508018927L;
	private Long acesVehicleId;
	private int total;
	private List<ServiceTypes> data;
	
	public ScheduledMaintenances(){}
	public ScheduledMaintenances(List<ServiceMaintenanceTO> records) {
		data = new ArrayList<ServiceTypes>();
//		char[] delims = {' ','/','-'};
		String delims = "-,/, ";
		
		String lastServiceType = "";
		String lastNote = "";
		Map<String, List<ComponentType>> operations = new HashMap<String, List<ComponentType>>();
		for(ServiceMaintenanceTO to : records) {
			setAcesVehicleId(to.getAcesVehicleId());
			
			String currServiceType = StringUtils.trimToEmpty(to.getServiceType());;//WordUtils.capitalizeFully(StringUtils.trimToEmpty(to.getServiceType()), delims) ;
			String currOp = StringUtils.trimToEmpty(to.getOperationTypeTxt());
			String currComp = WordUtils.capitalizeFully(StringUtils.trimToEmpty(to.getComponentTxt()), delims) ;
			String currNote = StringUtils.trimToEmpty(to.getFootnoteTxt());
			ComponentType componentType = new ComponentType(currComp, currNote);
			
			//If the current and the last mileageIntervals are the same then we are just appending data
			// to the existing operations list.  Need to check to see if the operation type has changed
			// or not.
			if(lastServiceType.equals(currServiceType)) {
				List<ComponentType> components = operations.get(currOp);
				if(components == null) {
					components = new ArrayList<ComponentType>();
					components.add(componentType);
					operations.put(currOp, components);
				} else {
					components.add(componentType);
				}
			} else {
				//We have changed mileageIntervals so add the last one to the mileageTypes list and start
				// a new one.
				if(operations.size() > 0) {
					ServiceTypes serviceType = new ServiceTypes(lastServiceType, operations);
					addServiceType(serviceType);
				}	
				
				operations = new HashMap<String, List<ComponentType>>();
				lastServiceType = currServiceType;
				lastNote = currNote;
				List<ComponentType> components = new ArrayList<ComponentType>(1);
				components.add(new ComponentType(currComp, currNote));
				operations.put(currOp, components);
				
			}
			
		}
		
		//Add the last one
		ServiceTypes serviceType = new ServiceTypes(lastServiceType, operations);
		addServiceType(serviceType);
		
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

	public List<ServiceTypes> getData() {
		return data;
	}
	public void setData(List<ServiceTypes> mileageTypes) {
		this.data = mileageTypes;
	}	
	
	private void addServiceType(ServiceTypes serviceType) {
		data.add(serviceType);
	}
}
