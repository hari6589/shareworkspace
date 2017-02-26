package com.bfrc.framework.dao;

import org.springframework.dao.DataAccessException;

import com.bfrc.framework.dao.appointment.*;

public interface StoreScheduleDAO {
public StoreScheduleDAO populate() throws DataAccessException;
public boolean isHoliday(String testYear, String testMonth, String testDay);
public boolean isHoliday(String testYear, String testMonth, String testDay, String storeNumber);
public Hours[] getHours(String storeNumber);
public void addYear(String s);
public void addMonth(String s);
public void addDay(String s);
}
