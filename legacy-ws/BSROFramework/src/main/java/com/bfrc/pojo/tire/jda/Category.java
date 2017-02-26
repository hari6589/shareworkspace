package com.bfrc.pojo.tire.jda;

// Generated Nov 23, 2007 4:02:41 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Category generated by hbm2java
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Category implements java.io.Serializable {

	// Fields    

	private long id;

	private String value;

	private Set displaies = new HashSet(0);

	// Constructors

	/** default constructor */
	public Category() {
	}

	/** minimal constructor */
	public Category(long id, String value) {
		this.id = id;
		this.value = value;
	}

	/** full constructor */
	public Category(long id, String value, Set displaies) {
		this.id = id;
		this.value = value;
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

	@JsonIgnore
	public Set getDisplaies() {
		return this.displaies;
	}

	@JsonIgnore
	public void setDisplaies(Set displaies) {
		this.displaies = displaies;
	}

}
