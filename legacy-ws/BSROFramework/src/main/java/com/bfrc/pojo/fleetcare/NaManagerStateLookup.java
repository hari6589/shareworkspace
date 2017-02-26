package com.bfrc.pojo.fleetcare;

import com.bfrc.pojo.fleetcare.NaManager;

public class NaManagerStateLookup implements
		java.io.Serializable {

	// Fields

	private String state;
	private NaManager naManagers;

	// Constructors

	/** default constructor */
	public NaManagerStateLookup() {
	}

	/** full constructor */
	public NaManagerStateLookup(String state, NaManager naManagers) {
		this.state = state;
		this.naManagers = naManagers;
	}

	// Property accessors

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public NaManager getNaManagers() {
		return this.naManagers;
	}

	public void setNaManagers(NaManager naManagers) {
		this.naManagers = naManagers;
	}

}