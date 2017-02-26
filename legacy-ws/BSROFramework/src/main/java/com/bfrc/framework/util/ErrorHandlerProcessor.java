/**
 * A thread to process all exception emails after periodic intervals
 */
package com.bfrc.framework.util;

import org.springframework.context.ApplicationContext;


public class ErrorHandlerProcessor implements Runnable{
	protected Thread thread;
	protected Long interval;
	protected String emailGroup;
	protected ApplicationContext ctx = ApplicationContextProvider.getContext();
		
	protected ErrorHandlerProcessor() {
		thread = new Thread(this);
		thread.start();
	}

	/*Invoked using sprint configuration*/
	protected ErrorHandlerProcessor(Long interval, String emailGroup) {
		thread = new Thread(this);
		thread.start();
		this.interval = interval;
		this.emailGroup = emailGroup;
	}

	public void run() {
		while(Thread.currentThread().isAlive()) {
			try {
				Thread.sleep(getInterval()*60*1000);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			try {
				//System.out.println("Start processing every "+ getInterval()+ " mins ctx = " + ctx);
				ErrorHandlerManager errHandlerManager = new ErrorHandlerManager(ctx, emailGroup);
				errHandlerManager.emailErrorMessages();
			} catch(Exception e) {
				e.printStackTrace();
			}			
		}
	}	

	/**
	 * @return the emailGroup
	 */
	public String getEmailGroup() {
		return emailGroup;
	}

	/**
	 * @param emailGroup the emailGroup to set
	 */
	public void setEmailGroup(String emailGroup) {
		this.emailGroup = emailGroup;
	}
	
	/**
	 * @return the interval
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * @param interval the interval to set
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}
	
}
