package com.bfrc.framework.dao;

import java.util.List;

import com.bfrc.pojo.geo.DistrictManager;
import com.bfrc.pojo.geo.HrDistricts;

public interface DistrictDAO {
StoreDAO getStoreDAO();
void setStoreDAO(StoreDAO dao);
com.bfrc.pojo.geo.HrDistricts getDistrict(String id);
com.bfrc.pojo.geo.DistrictManager getDistrictManager(String id);
List getStores(String id);
List getDistrictsInState(String state);
DistrictManager getDistrictManagerByEmail(String email) ;
List getDistrictsByZone(String zoneId, String brand);
List<HrDistricts> getDistrictsByZone(String zoneId, String[] brands);
List getDistrictsByZone(String zoneId);
List getDistrictsByEmail(String email); 
List getDistrictsByEmail(String email, String brand);
List<HrDistricts> getDistrictsByEmail(String email, String[] brands);
List getDistrictIdsByZone(String zoneId);

}
