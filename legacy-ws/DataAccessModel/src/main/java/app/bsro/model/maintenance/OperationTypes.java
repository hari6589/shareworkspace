package app.bsro.model.maintenance;

import java.io.Serializable;
import java.util.List;

public class OperationTypes implements Comparable<OperationTypes>, Serializable {
	private static final long serialVersionUID = -3470455711447071047L;
	private String operationType;
	private List<ComponentType> components;
	
	public OperationTypes(){}
	public OperationTypes(String type, List<ComponentType> comps) {
		setOperationType(type);
		setComponents(comps);
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setComponents(List<ComponentType> components) {
		this.components = components;
	}
	public List<ComponentType> getComponents() {
		return components;
	}
	
	public int compareTo(OperationTypes o) {
		return this.operationType.compareTo(o.getOperationType());
	}
}
