/**
 * 
 */
package com.bfrc.dataaccess.dao.myprofile;

import java.math.BigDecimal;
import java.util.List;

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
import com.bfrc.dataaccess.model.vehicle.Fitment;

/**
 * @author schowdhury
 *
 */
public interface BFSUserProfileDAO {
	
	public BFSUser getUserProfile(String email, String website);
	
	public BFSUser doesUserExist(String email, String userType);
	
	public BFSUser doesUserExistBasedonId(Long userId, String userType);
	
	public MyStore getMaxDistanceStore(Long userId, String userType);
	
	public void updateUser(BFSUser user);
	
	public void deleteUser(BFSUser user);
	
	public MyDriver doesDriverExist(String userType, String email);
	
	public MyDriver getDriver(Long myDriverId);
	
	public Long saveDriver(MyDriver driver);
	
	public void updateDriver(MyDriver driver);
	
	public MyStore doesFavouriteStoreExist(Long userId,String storeNumber);
	
	public void saveDriverStore(MyDriver myDriver, String storeNumber, BigDecimal distance);
	
	public void updateDriverStore(MyStore myStore);
	
	public void removeDriverStore(MyStore myStore);
	
	public MyPromotion doesFavouritePromotionExist(String email, Long promotionId);
	
	public void saveUserPromotion(BFSUser user, Promotion promotion);
	
	public void removeUserPromotion(MyPromotion myPromotion);
	
	public List<MyVehicle> getVehicles(String email, String userType, Long acesVehicleId);
	
	public Fitment getVehicle(Integer year, String make, String model, String submodel);
	
	public List<MyVehicle> getAllVehicles(String email, String userType);
	
	public MyVehicle doesVehicleExist(Long vehicleId);
	
	public List<VehicleConfiguration> getVehicleConfig(Long acesVehicleId);
	
	public Long saveVehicle(MyVehicle vehicle);
	
	public void updateVehicle(MyVehicle vehicle);
	
	public void updateVehicleHistory(MyVehicle vehicle, MyServiceHistoryVehicle history);
	
	public void removeVehicle(MyVehicle myVehicle);
	
	public void saveVehicleAppointment(MyAppointment myAppt);
	
	public MyVehicleGas doesVehicleGasFillupExist(Long id);
	
	public List<MyVehicleGas> getMyVehicleGasFillups(Long userId, Long acesVehicleId, String vinNumber);
	
	public void saveMyVehicleGasFillup(MyVehicleGas myVehicleGas);
	
	public void updateMyVehicleGasFillup(MyVehicleGas myVehicleGas);
	
	public MyMaintenanceServicePerformed doesMaintenanceServiceDataExist(Long id);
	
	public List<MyMaintenanceServicePerformed> getMaintenanceServiceData(Long userId, Long acesVehicleId, String vinNumber);
	
	public void saveMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed);
	
	public void removeMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed);
	
	public void updateMaintenanceServiceData(MyMaintenanceServicePerformed servicePerformed);
	
	public VehicleConfiguration getVehicleConfigData(BaseVehicle baseVehicle, String submodel);
	
	public MyProductQuote doesQuoteExist(Long quoteId, ProductType type, String email);
	
	public void saveProductQuote(MyProductQuote myQuote);
	
	public void removeProductQuote(MyProductQuote myQuote);
	
	public MyPicture getImage(Long myPictureId);
	
	public MyPicture getImages(Long pictureObjectId);
	
	public List<MyPicture> getImages(PictureType pictureType, Long imageObjectId);
	
	public void saveImage(MyPicture myPicture);
	
	public MyDevice getDevice(Long deviceId);
	
	public MyDevice doesDeviceExist(Long userId, String deviceType, 
			String deviceName, String deviceModel);

	public void deleteImage(MyPicture myPicture);
	
	public void saveDevice(MyDevice myDevice);
	
	public void updateDevice(MyDevice myDevice);
	
	public void deleteDevice(MyDevice myDevice);
}
