package com.bfrc.framework.dao;

import com.bfrc.pojo.fleetcare.*;

import org.springframework.dao.DataAccessException;

public interface FleetCareDAO {
	public void addApplication(NaApplication application) throws DataAccessException;
	public NaManager getManagerByState(String state);
}