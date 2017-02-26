package com.bfrc.framework.dao;

import com.bfrc.Bean;

public interface DAOFactory extends Bean {
AdminDAO getAdminDAO();
CatalogDAO getCatalogDAO();
ContactDAO getContactDAO();
DistrictDAO getDistrictDAO();
EmailSignupDAO getEmailSignupDAO();
EventDAO getEventDAO();
LocatorDAO getLocatorDAO();
MaintenanceDAO getMaintenanceDAO();
PricingDAO getPricingDAO();
PromotionDAO getPromotionDAO();
StoreDAO getStoreDAO();
StoreScheduleDAO getStoreScheduleDAO();
VehicleDAO getVehicleDAO();
PartnerDAO getPartnerDAO();
ZoneDAO getZoneDAO();
// by Jin Fang @05.02.06
AppointmentDAO getAppointmentDAO();
}
