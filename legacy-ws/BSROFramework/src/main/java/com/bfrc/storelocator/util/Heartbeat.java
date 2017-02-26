package com.bfrc.storelocator.util;

import com.bfrc.framework.dao.store.GeocodeOperator;

/**
 * @author Ed Knutson
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Heartbeat implements Runnable {
	private GeocodeOperator op = null;
	private boolean firstLogicUp = false, init = false;
	private long interval = 1000 * 60 * 5;
	private Thread thread;
	public Heartbeat() {
		//this.thread = new Thread(this);
		//this.thread.start();
	}
	
	public void setGeocodeOperator(GeocodeOperator op) {
		this.op = op;
		//if(!this.init)
		//	check();
	}
	
	public boolean isInit() {
		return this.init;
	}
	
	public boolean isFirstLogicUp() {
		//--- 20100527 releases: REMOVE HEART BEAT by CS ---//
		//return this.firstLogicUp;
		return true;
	}
	
	public void check() {
		/*
		try {
			if(this.op != null) {
				this.init = true;
				this.op.checkFirstLogic("207 e michigan", "milwaukee", "WI", "53202");
				this.firstLogicUp = true;
			} else System.err.println("Call to Heartbeat before setting geocodeOperator detected");
		} catch(Exception ex) {
			this.firstLogicUp = false;
			System.err.println("Firstlogic server unreachable by Heartbeat ("
				+ ex.getClass().getName()
				+ ")");
//				+ ex.getMessage());
//			ex.printStackTrace(System.err);
		}
		*/
	}
	
	public void run() {
		/*
		try {
			while(Thread.currentThread().isAlive()) {
				if(this.init)
					check();
				Thread.sleep(this.interval);
			}
		} catch(Exception ex) {}
		*/
	}

}