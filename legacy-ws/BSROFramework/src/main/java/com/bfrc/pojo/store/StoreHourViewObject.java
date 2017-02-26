package com.bfrc.pojo.store;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.bfrc.storelocator.util.LocatorUtil;

public class StoreHourViewObject implements java.io.Serializable, Comparable<StoreHourViewObject> {
	private static final long serialVersionUID = 1410507206786856996L;
	private String dayOfWeek;
	private String openTime;
	private String closeTime;
	private boolean closed = false;
	private String holiday;

	public static final Map<String, Integer> DAY_ORDER = new HashMap<String, Integer>() {
		private static final long serialVersionUID = -5091508452423908561L;
		{
			put(LocatorUtil.DAY_ABBREV[6], 0);
			put(LocatorUtil.DAY_ABBREV[0], 1);
			put(LocatorUtil.DAY_ABBREV[1], 2);
			put(LocatorUtil.DAY_ABBREV[2], 3);
			put(LocatorUtil.DAY_ABBREV[3], 4);
			put(LocatorUtil.DAY_ABBREV[4], 5);
			put(LocatorUtil.DAY_ABBREV[5], 6);
		}
	};

	public static final Map<Integer, String> CALENDAR_DAY_ABBREV = new HashMap<Integer, String>() {
		private static final long serialVersionUID = -4143238937587449672L;
		{
			put(Calendar.SUNDAY, LocatorUtil.DAY_ABBREV[6]);
			put(Calendar.MONDAY, LocatorUtil.DAY_ABBREV[0]);
			put(Calendar.TUESDAY, LocatorUtil.DAY_ABBREV[1]);
			put(Calendar.WEDNESDAY, LocatorUtil.DAY_ABBREV[2]);
			put(Calendar.THURSDAY, LocatorUtil.DAY_ABBREV[3]);
			put(Calendar.FRIDAY, LocatorUtil.DAY_ABBREV[4]);
			put(Calendar.SATURDAY, LocatorUtil.DAY_ABBREV[5]);
		}
	};

	public StoreHourViewObject() {
	}

	public StoreHourViewObject(StoreHour storeHour) {
		dayOfWeek = storeHour.getId().getWeekDay();
		openTime = LocatorUtil.format(storeHour.getOpenTime());
		closeTime = LocatorUtil.format(storeHour.getCloseTime());
	}

	public StoreHourViewObject(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
		closed = true;
	}

	public String getOpenTime() {
		return this.openTime;
	}

	public void setOpenTime(String openTime) {
		this.openTime = openTime;
	}

	public String getCloseTime() {
		return this.closeTime;
	}

	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	
	public String getHoliday() {
		return holiday;
	}
	
	public boolean isOverride() {
		return holiday != null;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(" dayOfWeek: " + dayOfWeek);
		if (closed) {
			sb.append(" closed");
		} else {
			sb.append(" openTime: " + openTime);
			sb.append(" closeTime: " + closeTime);
		}
		return sb.toString();
	}

	@Override
	public int compareTo(StoreHourViewObject o) {
		return DAY_ORDER.get(dayOfWeek).compareTo(DAY_ORDER.get(o.getDayOfWeek()));
	}
}