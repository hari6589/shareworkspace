package com.bfrc.pojo.fleetcare;

import java.util.HashSet;
import java.util.Set;


public class NaManager implements java.io.Serializable {

	// Fields

	private Long id;
	private String accountManagerName;
	private String accountManagerEmailAddress;
	private String accountManagerPhoneNumber;
	private Set naManagerStateLookups = new HashSet(0);

	// Constructors

	/** default constructor */
	public NaManager() {
	}

	/** minimal constructor */
	public NaManager(Long id, String accountManagerName,
			String accountManagerEmailAddress) {
		this.id = id;
		this.accountManagerName = accountManagerName;
		this.accountManagerEmailAddress = accountManagerEmailAddress;
	}

	/** full constructor */
	public NaManager(Long id, String accountManagerName,
			String accountManagerEmailAddress, Set naManagerStateLookups) {
		this.id = id;
		this.accountManagerName = accountManagerName;
		this.accountManagerEmailAddress = accountManagerEmailAddress;
		this.naManagerStateLookups = naManagerStateLookups;
	}

	// Property accessors

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountManagerName() {
		return this.accountManagerName;
	}

	public void setAccountManagerName(String accountManagerName) {
		this.accountManagerName = accountManagerName;
	}

	public String getAccountManagerEmailAddress() {
		return this.accountManagerEmailAddress;
	}

	public void setAccountManagerEmailAddress(String accountManagerEmailAddress) {
		this.accountManagerEmailAddress = accountManagerEmailAddress;
	}

	public Set getNaManagerStateLookups() {
		return this.naManagerStateLookups;
	}

	public void setNaManagerStateLookups(Set naManagerStateLookups) {
		this.naManagerStateLookups = naManagerStateLookups;
	}
	
	public String getAccountManagerPhoneNumber() {
		return this.accountManagerPhoneNumber;
	}

	public void setAccountManagerPhoneNumber(String accountManagerPhoneNumber) {
		this.accountManagerPhoneNumber = accountManagerPhoneNumber;
	}

}