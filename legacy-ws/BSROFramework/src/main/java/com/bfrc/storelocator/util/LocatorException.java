package com.bfrc.storelocator.util;

import java.io.Serializable;

/**
 * @author edc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class LocatorException extends Exception implements Serializable {
	public LocatorException(String s) {
		super(s);
		this.reason = s;
	}
	protected String reason;
	public String getReason() { return this.reason; }
	public String toString() { return this.reason; }
}
