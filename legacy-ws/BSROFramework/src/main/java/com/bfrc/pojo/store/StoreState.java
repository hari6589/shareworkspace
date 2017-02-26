/*
 * Created on Nov 3, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.bfrc.pojo.store;

import java.io.Serializable;

/**
 * @author jfang
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StoreState implements Serializable {
	private String state;
	private String name;

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the state.
	 */
	public String getState() {
		return this.state;
	}
	/**
	 * @param state The state to set.
	 */
	public void setState(String state) {
		this.state = state;
	}
}
