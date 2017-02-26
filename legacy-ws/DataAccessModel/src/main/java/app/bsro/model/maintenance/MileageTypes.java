package app.bsro.model.maintenance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MileageTypes implements Serializable {

	private String mileage;
	private List<OperationTypes> services;
	
	public MileageTypes(){}
	public MileageTypes(Long mileage, Map<String, List<ComponentType>> operations, String note) {
		setMileage(mileage.toString());
		services = new ArrayList<OperationTypes>();
		Iterator<String> iter = operations.keySet().iterator();
		while(iter.hasNext()) {
			String operationType = iter.next();
			services.add( new OperationTypes(operationType, operations.get(operationType)) );
		}
		
		Collections.sort(services);
	}
	
	public void setMileage(String mileage) {
		this.mileage = mileage;
	}
	public String getMileage() {
		return mileage;
	}
	public void setServices(List<OperationTypes> operationTypes) {
		this.services = operationTypes;
	}
	
	public List<OperationTypes> getServices() {
		return services;
	}
}
