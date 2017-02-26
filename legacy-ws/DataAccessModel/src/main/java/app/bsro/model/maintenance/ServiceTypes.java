package app.bsro.model.maintenance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ServiceTypes implements Serializable {
	private static final long serialVersionUID = 3519513998118398400L;
	private String service;
	private List<OperationTypes> services;
	
	public ServiceTypes(){}
	public ServiceTypes(String service, Map<String, List<ComponentType>> operations) {
		setService(service);
		services = new ArrayList<OperationTypes>();
		Iterator<String> iter = operations.keySet().iterator();
		while(iter.hasNext()) {
			String operationType = iter.next();
			services.add( new OperationTypes(operationType, operations.get(operationType)) );
		}
		
		Collections.sort(services);
	}
	
	public void setService(String service) {
		this.service = service;
	}
	public String getService() {
		return service;
	}
	public void setServices(List<OperationTypes> operationTypes) {
		this.services = operationTypes;
	}
	
	public List<OperationTypes> getServices() {
		return services;
	}

}
