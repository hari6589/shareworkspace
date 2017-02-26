package com.bfrc.pojo.appointment;

import java.io.Serializable;

public class AppointmentStoreHour implements Serializable{
	
	private String openTime;
    private String closeTime;
    private String firstTimeToSchedule;
    private String lastTimeToSchedule;
	
	public String getOpenTime() {
		return openTime;
	}
	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getFirstTimeToSchedule() {
		return firstTimeToSchedule;
	}
	public void setFirstTimeToSchedule(String firstTimeToSchedule) {
		this.firstTimeToSchedule = firstTimeToSchedule;
	}
	public String getLastTimeToSchedule() {
		return lastTimeToSchedule;
	}
	public void setLastTimeToSchedule(String lastTimeToSchedule) {
		this.lastTimeToSchedule = lastTimeToSchedule;
	}
	
	public String toString(){
    	StringBuffer sb = new StringBuffer();
    	sb.append(" openTime: " + openTime);
    	sb.append(" closeTime: " + closeTime);
    	if(firstTimeToSchedule != null) sb.append(" firstTimeToSchedule " + firstTimeToSchedule);
    	if(lastTimeToSchedule != null) sb.append(" lastTimeToSchedule " + lastTimeToSchedule);
    	return sb.toString();
    }


}
