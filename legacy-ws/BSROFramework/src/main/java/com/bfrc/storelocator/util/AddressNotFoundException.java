package com.bfrc.storelocator.util;

/**
 * @author edc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class AddressNotFoundException extends LocatorException {
	public final static String ERR = "The address you entered was unable to be located";
	public AddressNotFoundException() {
		super(ERR);
	}
}
