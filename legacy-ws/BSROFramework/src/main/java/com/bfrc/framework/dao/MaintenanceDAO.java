package com.bfrc.framework.dao;

import java.util.*;

import com.bfrc.pojo.*;
import com.bfrc.pojo.maintenance.*;

public interface MaintenanceDAO {
	public final static int DAYS = 60;
	public List getServiceList(long acesVehicleId);
	public List getServiceList(long acesVehicleId, String serviceType);
	public List getServiceTypeList(long acesVehicleId);
	List getServicesForUserAndDefaultVehicle(User u);
	List getServicesForUserAndVehicle(User u, UserVehicle v);
	List getServicesForUserAndVehicleAndMileage(User u, UserVehicle v, Integer mileage);
	void insertSignup(MaintSignup m);
}