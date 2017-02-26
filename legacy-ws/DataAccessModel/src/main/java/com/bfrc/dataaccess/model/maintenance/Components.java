package com.bfrc.dataaccess.model.maintenance;

import java.util.HashSet;
import java.util.Set;

public class Components implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private long componentId;
	private String component;
	private Set<Intervals> intervals = new HashSet<Intervals>(0);

	public Components() {}

	public Components(long componentId) {
		this.componentId = componentId;
	}
	
	public Components(long componentId, String component, Set<Intervals> intervals) {
		this.componentId = componentId;
		this.component = component;
		this.intervals = intervals;
	}

	public long getComponentId() {
		return this.componentId;
	}

	public void setComponentId(long componentId) {
		this.componentId = componentId;
	}

	public String getComponent() {
		return this.component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public Set<Intervals> getIntervals() {
		return this.intervals;
	}

	public void setIntervals(Set<Intervals> intervals) {
		this.intervals = intervals;
	}

	@Override
	public String toString() {
		return "Components [componentId=" + componentId + ", component="
				+ component + "]";
	}

}
