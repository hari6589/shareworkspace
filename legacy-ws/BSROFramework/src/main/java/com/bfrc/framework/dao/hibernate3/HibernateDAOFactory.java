package com.bfrc.framework.dao.hibernate3;

import com.bfrc.framework.dao.AdminDAO;
import com.bfrc.framework.dao.AppointmentDAO;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.DAOFactory;
import com.bfrc.framework.dao.DistrictDAO;
import com.bfrc.framework.dao.EmailSignupDAO;
import com.bfrc.framework.dao.EventDAO;
import com.bfrc.framework.dao.LocatorDAO;
import com.bfrc.framework.dao.MaintenanceDAO;
import com.bfrc.framework.dao.PartnerDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.StoreScheduleDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.ZoneDAO;

public class HibernateDAOFactory implements DAOFactory {

	public PromotionDAO getPromotionDAO() {
		return new PromotionDAOImpl();
	}

	public MaintenanceDAO getMaintenanceDAO() {
		return new MaintenanceDAOImpl();
	}

	public AdminDAO getAdminDAO() {
		return new AdminDAOImpl();
	}

	public ContactDAO getContactDAO() {
		return new ContactDAOImpl();
	}

	public CatalogDAO getCatalogDAO() {
		return new CatalogDAOImpl();
	}

	public DistrictDAO getDistrictDAO() {
		return new DistrictDAOImpl();
	}

	public EmailSignupDAO getEmailSignupDAO() {
		return new EmailSignupDAOImpl();
	}

	public EventDAO getEventDAO() {
		return new EventDAOImpl();
	}

	public LocatorDAO getLocatorDAO() {
		return new LocatorDAOImpl();
	}

	public PricingDAO getPricingDAO() {
		return new PricingDAOImpl();
	}

	public StoreDAO getStoreDAO() {
		return new StoreDAOImpl();
	}

	public StoreScheduleDAO getStoreScheduleDAO() {
		return new StoreScheduleDAOImpl();
	}

	public VehicleDAO getVehicleDAO() {
		return new VehicleDAOImpl();
	}

	public AppointmentDAO getAppointmentDAO() {
		return new AppointmentDAOImpl();
	}

	public PartnerDAO getPartnerDAO() {
		return new PartnerDAOImpl();
	}

	public ZoneDAO getZoneDAO() {
		return new ZoneDAOImpl();
	}
}
