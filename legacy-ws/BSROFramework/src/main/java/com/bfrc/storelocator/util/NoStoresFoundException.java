package com.bfrc.storelocator.util;

/**
 * @author edc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NoStoresFoundException extends LocatorException {
	public final static String ERR_START = "There were no stores found within ";
	public final static String ERR_END = " miles of the address you entered";
	public NoStoresFoundException() {
		this(50);
	}
	public NoStoresFoundException(int miles) {
		super(ERR_START + miles + ERR_END);
	}
}
