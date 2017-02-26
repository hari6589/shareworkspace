package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.geo.ZoneList;
import com.bfrc.pojo.geo.ZoneManager;

public interface ZoneDAO {
ZoneManager getZoneManagerByEmail(String email) ;
List getZonesByEmail(String email);
List getZonesByEmail(String email, String brand);
List<ZoneList> getZonesByEmail(String email, String[] brands);
List getAllZones();
List getAllZonesByBrand(String brand);
List<ZoneList> getAllZonesByBrand(String[] brands);
}
