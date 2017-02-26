package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.admin.GlobalAdmin;

public interface AdminDAO {
	GlobalAdmin getUser(String name, String password);

	String getPassword(String name);

	GlobalAdmin fetchByEmail(String email);
	
	List<GlobalAdmin> getAllAdminWithEmail(String brand);
	
	Long save(GlobalAdmin globalAdmin);
	
	Long save(GlobalAdmin globalAdmin, long[] appids);
	
	void update(GlobalAdmin globalAdmin);
	
	GlobalAdmin getAdminWithToken(String token);
}
