package com.bsro.api.rest.util;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Help with debugging.  A timer utility class to track start and end times and convert the difference
 * to a Nm Ns format.
 * @author Brad Balmer
 *
 */
public class TimerUtils {

	private Logger log = Logger.getLogger(getClass().getName());
	private long start;
	private long end;
	
	/**
	 * Default constructor which sets the start time to the current time
	 */
	public TimerUtils() {
		start = System.currentTimeMillis();
	}
	
	/**
	 * Sets the end time to the current time
	 */
	public void end() {
		end = System.currentTimeMillis();
	}

	/**
	 * Override the start variable
	 * @param start
	 */
	public void setStart(long start) {
		this.start = start;
	}
	
	/**
	 * Override the end variable
	 * @param end
	 */
	public void setEnd(long end) {
		this.end = end;
	}
	
	/**
	 * Write out formatted calculated difference to info log
	 */
	public void infoLog() {
		log.info(calculate());
	}	
	
	/**
	 * Write out formatted calculated difference to debug log
	 */
	public void debugLog() {
		log.fine(calculate());
	}
	
	/**
	 * Write out formatted calculated difference to System.out
	 */
	public void stdOutLog() {
		System.out.println(calculate());
	}
	
	private String calculate() {
		long diff = end - start;
		
		long minutes = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
		long seconds = TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS);
		
		return ("Processed in "+minutes+"m "+seconds+"s");
	}
}
