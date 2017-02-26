/**
 * 
 */
package com.bfrc.dataaccess.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.bfrc.dataaccess.dao.myprofile.BFSUserProfileDAO;
import com.bfrc.dataaccess.model.aces.BaseVehicle;
import com.bfrc.dataaccess.model.aces.Vehicle;
import com.bfrc.dataaccess.model.aces.VehicleConfiguration;
import com.bfrc.dataaccess.model.myprofile.BFSUser;
import com.bfrc.dataaccess.model.myprofile.MyAppointment;
import com.bfrc.dataaccess.model.myprofile.MyDevice;
import com.bfrc.dataaccess.model.myprofile.MyDriver;
import com.bfrc.dataaccess.model.myprofile.MyMaintenanceServicePerformed;
import com.bfrc.dataaccess.model.myprofile.MyPicture;
import com.bfrc.dataaccess.model.myprofile.MyProductQuote;
import com.bfrc.dataaccess.model.myprofile.MyPromotion;
import com.bfrc.dataaccess.model.myprofile.MyServiceHistoryVehicle;
import com.bfrc.dataaccess.model.myprofile.MyStore;
import com.bfrc.dataaccess.model.myprofile.MyVehicle;
import com.bfrc.dataaccess.model.myprofile.MyVehicleGas;
import com.bfrc.dataaccess.model.myprofile.PictureType;
import com.bfrc.dataaccess.model.myprofile.ProductType;
import com.bfrc.dataaccess.model.promotion.Promotion;
import com.bfrc.dataaccess.model.store.Store;
import com.bfrc.dataaccess.model.vehicle.Fitment;


/**
 * @author schowdhu
 *
 */
public class BFSUserProfileDAOImpl extends HibernateDaoSupport implements BFSUserProfileDAO {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	
	public Store getStore(String storeNumber){
		return getHibernateTemplate().load(Store.class, new Long(storeNumber));
	}

	public BFSUser getUserProfile(String email, String webSite) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(BFSUser.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.add(Restrictions.eq("userType", webSite));
		
		@SuppressWarnings("unchecked")
		List<BFSUser> usersList = (List<BFSUser>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		return null;
	}
	
	// check user exist based on userId
	public BFSUser doesUserExistBasedonId(Long userId, String userType) 
	{
		BFSUser user = null;
		try
		{
		String hql = "from com.bfrc.dataaccess.model.myprofile.BFSUser usr where usr.userId=:userId";
		Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
		query.setLong("userId", userId);
		query.setMaxResults(1);
		user = (BFSUser) query.uniqueResult();
		}
		catch(Exception e)
		{
			logger.error(e);
			return user;
		}
		return user;
	}

	// this method is used to get the stores based on distance in descending order
		public MyStore getMaxDistanceStore(Long userId, String userType) 
		{
			MyStore myStore = null;
			try
			{
				Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyStore.class);
				criteria.createAlias("driver", "d");
				criteria.add(Restrictions.eq("d.userId", userId));
				criteria.addOrder(Order.desc("distance"));
				criteria.setMaxResults(1);
				myStore = (MyStore) criteria.uniqueResult();
			
			}
			catch(Exception e)
			{
				logger.error(e);
				return myStore;
			}
			return myStore;
		}
	
	public BFSUser doesUserExist(String email, String userType) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(BFSUser.class);
		criteria.add(Restrictions.eq("email", email));
		criteria.createAlias("userType", "type");
		criteria.add(Restrictions.eq("type.name", userType));
		
		@SuppressWarnings("unchecked")
		List<BFSUser> usersList = (List<BFSUser>)criteria.list();
		
		if(usersList != null && !usersList.isEmpty()){
			return usersList.get(0);
		}
		
		return null;
	}
	
	public MyDriver getDriver(Long id){
		return getHibernateTemplate().get(MyDriver.class, id);
	}
	
	public Long saveDriver(MyDriver driver){
		return (Long)getHibernateTemplate().save(driver);
	}

	public MyDriver doesDriverExist(String userType, String email) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyDriver.class);
		criteria.createAlias("user","u");
		criteria.createAlias("user.userType","uType");
		criteria.add(Restrictions.eq("u.email", email));
		criteria.add(Restrictions.eq("uType.name", userType));
		criteria.setMaxResults(1);
		
		@SuppressWarnings("unchecked")
		List<MyDriver> driverList = (List<MyDriver>)criteria.list();
		
		if(driverList != null && !driverList.isEmpty()){
			return driverList.get(0);
		}
		
		return null;
	}
	
	public MyStore doesFavouriteStoreExist(Long userId,String storeNumber) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyStore.class);
		criteria.createAlias("store","store");
		criteria.createAlias("driver","driver");
		criteria.add(Restrictions.eq("store.storeNumber", new Long(storeNumber)));
		criteria.add(Restrictions.eq("driver.userId", userId));
		
		@SuppressWarnings("unchecked")
		List<MyStore> myStoreList = (List<MyStore>)criteria.list();
		 
		if(myStoreList != null && !myStoreList.isEmpty()){
			return myStoreList.get(0);
		}
		
		return null;
	}
	
	public MyPromotion doesFavouritePromotionExist(String email, Long promotionId) {
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyPromotion.class);
		criteria.createAlias("user", "u");
		criteria.add(Restrictions.eq("u.email", email));
		criteria.createAlias("promotion", "p");
		criteria.add(Restrictions.eq("p.promotionId", promotionId));
		
		@SuppressWarnings("unchecked")
		List<MyPromotion> myPromotionList = (List<MyPromotion>)criteria.list();
		
		if(myPromotionList != null && !myPromotionList.isEmpty()){
			return myPromotionList.get(0);
		}
		
		return null;
	}
	
	public void saveDriverStore(MyDriver myDriver, String storeNumber, BigDecimal distance){
		MyStore myStore = new MyStore(myDriver, getStore(storeNumber), distance);
		getHibernateTemplate().save(myStore);
	}
	
	public void updateDriverStore(MyStore myStore){
		getHibernateTemplate().merge(myStore);
		getHibernateTemplate().flush();
	}
	
	public void removeDriverStore(MyStore myStore){
		getHibernateTemplate().delete(myStore);
	}
	
	public void saveUserPromotion(BFSUser user, Promotion promotion){
		MyPromotion myPromotion = new MyPromotion(user, promotion);
		getHibernateTemplate().save(myPromotion);
	}
	
	public void removeUserPromotion(MyPromotion myPromotion){
		getHibernateTemplate().delete(myPromotion);
	}
	
	public VehicleConfiguration getVehicleConfigData(BaseVehicle baseVehicle, String submodel){
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(VehicleConfiguration.class);
		criteria.createAlias("baseVehicle", "baseVehicle");
		criteria.add(Restrictions.eq("baseVehicle.baseVehicleId", baseVehicle.getBaseVehicleId()));
		criteria.add(Restrictions.eq("submodelSeries", submodel));
		
		@SuppressWarnings("unchecked")
		List<VehicleConfiguration> vehicleConfigList = (List<VehicleConfiguration>)criteria.list();
		if(vehicleConfigList != null && !vehicleConfigList.isEmpty()){
			System.out.println("vehicleConfigList-size======>"+vehicleConfigList.size());
			return vehicleConfigList.get(0);
		}
				
		return null;
	}
	
	
	public List<MyVehicle> getVehicles(String email, String userType, Long acesVehicleId){
		logger.info("Inside getVehicles in DAO");
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyVehicle.class);
		criteria.createAlias("driver", "d");
		criteria.createAlias("d.user", "u");
		criteria.add(Restrictions.eq("u.email", email));
		criteria.createAlias("fitment", "f");
		criteria.add(Restrictions.eq("f.acesVehicleId", acesVehicleId));
		
		@SuppressWarnings("unchecked")
		List<MyVehicle> vehicleList = (List<MyVehicle>)criteria.list();
				
		return vehicleList;

	}
	
	public Fitment getVehicle(Integer year, String make, String model, String submodel){
		logger.info("Inside getVehicle in DAO");
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(Fitment.class);
		//criteria.createAlias("baseVehicle", "baseVehicle");
		//criteria.createAlias("baseVehicle.make", "make");
		//criteria.createAlias("baseVehicle.model", "model");
		//criteria.createAlias("submodel", "submodel");
		criteria.add(Restrictions.eq("modelYear", year.toString()));
		criteria.add(Restrictions.eq("makeName", make));
		criteria.add(Restrictions.eq("modelName", model));
		criteria.add(Restrictions.eq("submodel", submodel));
		
		@SuppressWarnings("unchecked")
		List<Fitment> fitmentList = (List<Fitment>)criteria.list();
		if(fitmentList != null && !fitmentList.isEmpty()){
			return fitmentList.get(0);
			
		}
		
		return null;
	}

	public List<MyVehicle> getAllVehicles(String email, String userType){
		logger.info("Inside getVehicle");
		Object[] params = {email};
		
		
		//Filter filter = getHibernateTemplate().getSessionFactory().getCurrentSession().enableFilter("myFilter");
		//List<MyVehicle> vehicleList = (List<MyVehicle>)getHibernateTemplate().find("from MyVehicle v where v.fitment.id.baseVehId=? and v.fitment.id.submodel=?", params);
		
		@SuppressWarnings("unchecked")
		List<MyVehicle> vehicleList = (List<MyVehicle>)getHibernateTemplate().find("from MyVehicle v where v.baseVehicleId=? and v.submodel=?", params);
//		
//		if(vehicleList != null && !vehicleList.isEmpty()){
//			for (MyVehicle myVehicle : vehicleList){
//				for(MyDriver driver : myVehicle.getDrivers()){
//					if(driver.getEmailAddress().equals(driverEmail)){
//						return myVehicle;
//					}
//				}
//			}
//		}
		
		return null;
	}

	public MyVehicle doesVehicleExist(Long myVehicleId){
		logger.info("Inside doesVehicleExist");

		MyVehicle vehicle = getHibernateTemplate().get(MyVehicle.class, myVehicleId);
		
		return vehicle;
	}

	public Long saveVehicle(MyVehicle vehicle) {
		return (Long) getHibernateTemplate().save(vehicle);
	}
	
	public void updateVehicle(MyVehicle vehicle) {
		getHibernateTemplate().merge(vehicle);
		getHibernateTemplate().flush();
	}
	
	public List<VehicleConfiguration> getVehicleConfig(Long acesVehicleId){
		logger.info("Inside getVehicleConfig");
		try
		{
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(VehicleConfiguration.class);
		criteria.createAlias("vehicle", "vehicle");
		criteria.add(Restrictions.eq("vehicle.acesVehicleId", acesVehicleId));
		
		@SuppressWarnings("unchecked")
		List<VehicleConfiguration> vehicleConfigList = (List<VehicleConfiguration>)criteria.list();
		return vehicleConfigList;
		}catch(Exception e)
		{
			return null;
		}
	}
	
	public void updateVehicleHistory(MyVehicle vehicle,  MyServiceHistoryVehicle history){
		logger.info("Inside updateVehicleHistory");
		
		getHibernateTemplate().deleteAll(vehicle.getVehicleHistory());
		vehicle.setVehicleHistory(null);
		getHibernateTemplate().update(vehicle);
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
		
		MyVehicle updatedVeh = getHibernateTemplate().get(MyVehicle.class, vehicle.getMyVehicleId());
		logger.info("vehicleHistoryList size2="+updatedVeh.getVehicleHistory());
		List<MyServiceHistoryVehicle> serviceList = new ArrayList<MyServiceHistoryVehicle>();
		serviceList.add(history);
		updatedVeh.setVehicleHistory(serviceList);
		getHibernateTemplate().merge(updatedVeh);
		getHibernateTemplate().getSessionFactory().getCurrentSession().flush();
	}
	
	public void removeVehicle(MyVehicle vehicle) {
		getHibernateTemplate().delete(vehicle);
		
	}
	
	public void saveVehicleAppointment(MyAppointment myAppt){
		getHibernateTemplate().saveOrUpdate(myAppt);
	}
	
	public MyVehicleGas doesVehicleGasFillupExist(Long id){
		return getHibernateTemplate().get(MyVehicleGas.class, id);
	}
	
	public MyMaintenanceServicePerformed doesMaintenanceServiceDataExist(Long id){
		return getHibernateTemplate().get(MyMaintenanceServicePerformed.class, id);
	}
	
	public List<MyMaintenanceServicePerformed> getMaintenanceServiceData(Long userId, Long acesVehicleId, String vinNumber){
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyMaintenanceServicePerformed.class);
		criteria.createAlias("myVehicle", "myVehicle");
		criteria.createAlias("myVehicle.vehicle", "vehicle");
		criteria.createAlias("myVehicle.driver", "driver");
		criteria.add(Restrictions.eq("vehicle.acesVehicleId", acesVehicleId));
		criteria.add(Restrictions.eq("myVehicle.vinNumber", vinNumber));
		criteria.add(Restrictions.eq("driver.userId", userId));
		
		@SuppressWarnings("unchecked")
		List<MyMaintenanceServicePerformed> servicePerformedList = (List<MyMaintenanceServicePerformed>)criteria.list();
		return servicePerformedList;
	}
	
	public void saveMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed){
		getHibernateTemplate().save(servicePerformed);
	}
	
	public void updateMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed){
		getHibernateTemplate().merge(servicePerformed);
		getHibernateTemplate().flush();
	}
	
	public void removeMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed){
		getHibernateTemplate().delete(servicePerformed);
	}
	
	public List<MyVehicleGas> getMyVehicleGasFillups(Long userId, Long acesVehicleId, String vinNumber){
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyVehicleGas.class);
		criteria.createAlias("myVehicle", "myVehicle");
		criteria.createAlias("myVehicle.vehicle", "vehicle");
		criteria.createAlias("myVehicle.driver", "driver");
		criteria.add(Restrictions.eq("vehicle.acesVehicleId", acesVehicleId));
		criteria.add(Restrictions.eq("myVehicle.vinNumber", vinNumber));
		criteria.add(Restrictions.eq("driver.userId", userId));
		
		@SuppressWarnings("unchecked")
		List<MyVehicleGas> vehicleGasList = (List<MyVehicleGas>)criteria.list();
		
		return vehicleGasList;
	}
	
	public void saveMyVehicleGasFillup(MyVehicleGas myVehicleGas){
		getHibernateTemplate().save(myVehicleGas);
	}
	
	public void updateMyVehicleGasFillup(MyVehicleGas myVehicleGas){
		getHibernateTemplate().merge(myVehicleGas);
		getHibernateTemplate().flush();
		
	}


	public void deleteUser(BFSUser user) {
		getHibernateTemplate().delete(user);
	}

	public void updateUser(BFSUser user) {
		getHibernateTemplate().merge(user);
		getHibernateTemplate().flush();
	}
	
	public void updateDriver(MyDriver driver) {
		getHibernateTemplate().saveOrUpdate(driver);
		getHibernateTemplate().flush();
	}
	
	public MyProductQuote doesQuoteExist(Long quoteId, ProductType type, String email){
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyProductQuote.class);
		criteria.createAlias("user", "u");
		criteria.add(Restrictions.eq("u.email", email));
		criteria.add(Restrictions.eq("quoteId", quoteId));
		criteria.add(Restrictions.eq("productType", type));
		
		@SuppressWarnings("unchecked")
		List<MyProductQuote> prodQuoteList = (List<MyProductQuote>)criteria.list();
		
		if(prodQuoteList != null && !prodQuoteList.isEmpty()){
			return prodQuoteList.get(0);
		}
		
		return null;
	}
	
	public void saveProductQuote(MyProductQuote myQuote) {
		getHibernateTemplate().saveOrUpdate(myQuote);
		
	}

	public void removeProductQuote(MyProductQuote myQuote) {
		getHibernateTemplate().delete(myQuote);
	}
	
	public MyPicture getImage(Long myPictureId){
		return getHibernateTemplate().get(MyPicture.class, myPictureId);
	}
	
	public MyPicture getImages(Long pictureObjectId){
		
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyPicture.class);
		criteria.add(Restrictions.eq("pictureObjectId", pictureObjectId));
		
		@SuppressWarnings("unchecked")
		List<MyPicture> prodQuoteList = (List<MyPicture>)criteria.list();
		
		if(prodQuoteList != null && !prodQuoteList.isEmpty()){
			return prodQuoteList.get(0);
		}
		return null;
		
	}
	
	public List<MyPicture> getImages(PictureType pictureType, Long imageObjectId){
		Object[] params = {imageObjectId, pictureType};
		MyPicture picture =  null;
		@SuppressWarnings("unchecked")
		List<MyPicture> vehicleList = (List<MyPicture>)getHibernateTemplate()
			.find("from MyPicture p where imageProductId=? and pictureType=?", params);
		return vehicleList;
		
	}

	public void saveImage(MyPicture myPicture) {	
		getHibernateTemplate().saveOrUpdate(myPicture);

	}
	public MyDevice getDevice(Long deviceId){
		return getHibernateTemplate().get(MyDevice.class, deviceId);
		
	}
	
	public MyDevice doesDeviceExist(Long userId, String deviceType, String deviceName, String deviceModel){
		Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession().createCriteria(MyDevice.class);
		criteria.createAlias("user", "u");
		criteria.add(Restrictions.eq("u.userId", userId));
		criteria.add(Restrictions.eq("deviceType", deviceType));
		criteria.add(Restrictions.eq("mfgName", deviceName));
		criteria.add(Restrictions.eq("deviceModel", deviceModel));
		
		@SuppressWarnings("unchecked")
		List<MyDevice> deviceList = (List<MyDevice>)criteria.list();
		System.out.println("deviceList="+deviceList.size());
		if(deviceList != null && !deviceList.isEmpty()){
			return deviceList.get(0);
		}
		
		return null;
	}

	public void deleteImage(MyPicture myPicture) {
		getHibernateTemplate().delete(myPicture);
	}
	
	public void saveDevice(MyDevice myDevice) {	
		getHibernateTemplate().saveOrUpdate(myDevice);

	}
	public void updateDevice(MyDevice myDevice) {
		getHibernateTemplate().merge(myDevice);
		getHibernateTemplate().flush();
	}

	public void deleteDevice(MyDevice myDevice) {
		getHibernateTemplate().delete(myDevice);
	}
}
