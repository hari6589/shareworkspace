package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Temperature generated by hbm2java
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Temperature implements java.io.Serializable {

	// Fields    

	private long id;

	private String value;

	private Set configurations = new HashSet(0);

	// Constructors

	/** default constructor */
	public Temperature() {
	}

	/** minimal constructor */
	public Temperature(long id, String value) {
		this.id = id;
		this.value = value;
	}

	/** full constructor */
	public Temperature(long id, String value, Set configurations) {
		this.id = id;
		this.value = value;
		this.configurations = configurations;
	}

	// Property accessors
	@JsonIgnore
	public long getId() {
		return this.id;
	}

	@JsonIgnore
	public void setId(long id) {
		this.id = id;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonIgnore
	public Set getConfigurations() {
		return this.configurations;
	}

	@JsonIgnore
	public void setConfigurations(Set configurations) {
		this.configurations = configurations;
	}

}
