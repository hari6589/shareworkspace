package app.bsro.model.maintenance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.ToStringSerializer;

public class TimeIntervalTypes implements Serializable {
	private static final long serialVersionUID = 7420482682450645269L;
	private Long timeInterval;
	private String timeUnits;
	private List<OperationTypes> services;
	
	public TimeIntervalTypes(){}
	public TimeIntervalTypes(Long timeInterval, String timeUnits, Map<String, List<ComponentType>> operations, String note) {
		setTimeInterval(timeInterval);
		setTimeUnits(timeUnits);
		services = new ArrayList<OperationTypes>();
		Iterator<String> iter = operations.keySet().iterator();
		while(iter.hasNext()) {
			String operationType = iter.next();
			services.add( new OperationTypes(operationType, operations.get(operationType)) );
		}
		
		Collections.sort(services);
	}
	
	public void setTimeInterval(Long timeInterval) {
		this.timeInterval = timeInterval;
	}
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}
	@JsonSerialize(include=JsonSerialize.Inclusion.NON_DEFAULT, using=ToStringSerializer.class)
	public Long getTimeInterval() {
		return timeInterval;
	}
	public String getTimeUnits() {
		return timeUnits;
	}
	public void setServices(List<OperationTypes> operationTypes) {
		this.services = operationTypes;
	}
	
	public List<OperationTypes> getServices() {
		return services;
	}
}
