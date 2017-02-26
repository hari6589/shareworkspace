package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.admin.AdminIpAddress;

public interface IpAccessDAO {

	List<AdminIpAddress> getAllIps();
	AdminIpAddress checkIpAddress(String ip);
	AdminIpAddress findUniqueBy(String attribute,String id);
	void update(AdminIpAddress ipAddress);
	void save(AdminIpAddress ipAddress);
}
