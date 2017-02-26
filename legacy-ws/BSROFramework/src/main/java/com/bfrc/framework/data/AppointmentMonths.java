package com.bfrc.framework.data;

import java.util.*;

import com.bfrc.framework.dao.appointment.*;

public class AppointmentMonths extends java.util.LinkedHashMap {
	private ListAppointmentTimesOperator listAppointmentTimes;
	
	public ListAppointmentTimesOperator getListAppointmentTimes() {
		return listAppointmentTimes;
	}

	public void setListAppointmentTimes(
			ListAppointmentTimesOperator listAppointmentTimes) {
		this.listAppointmentTimes = listAppointmentTimes;
	}

	public void populate(String storeNumber) throws Exception {
		HashMap map = new HashMap();
		map.put("storeNumber", storeNumber);
		listAppointmentTimes.operate(map);
		List l = (List)map.get("result");
		Iterator i = l.iterator();
		while(i.hasNext()) {
			Object o = i.next();
			this.put(o, o);
		}
	}
}
