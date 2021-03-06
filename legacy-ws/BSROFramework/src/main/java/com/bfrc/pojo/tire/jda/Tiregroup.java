package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Tiregroup generated by hbm2java
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Tiregroup implements java.io.Serializable {

	// Fields    

	private long id;

	private String value;

	private String description;

	private Set displaies = new HashSet(0);

	// Constructors

	/** default constructor */
	public Tiregroup() {
	}

	/** minimal constructor */
	public Tiregroup(long id, String value, String description) {
		this.id = id;
		this.value = value;
		this.description = description;
	}

	/** full constructor */
	public Tiregroup(long id, String value, String description, Set displaies) {
		this.id = id;
		this.value = value;
		this.description = description;
		this.displaies = displaies;
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

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonIgnore
	public Set getDisplaies() {
		return this.displaies;
	}

	@JsonIgnore
	public void setDisplaies(Set displaies) {
		this.displaies = displaies;
	}

}
