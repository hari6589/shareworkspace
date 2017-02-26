package com.bfrc.dataaccess.svc.vehicle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.websiteuservehicle.UserSubvehicleBattery;
import app.bsro.model.websiteuservehicle.UserSubvehicleOil;
import app.bsro.model.websiteuservehicle.UserSubvehicleTire;
import app.bsro.model.websiteuservehicle.UserVehicle;

import com.bfrc.dataaccess.dao.WebSiteUserDAO;
import com.bfrc.dataaccess.dao.WebSiteUserVehicleDAO;
import com.bfrc.dataaccess.model.user.WebSiteUser;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserSubvehicle;
import com.bfrc.dataaccess.model.vehicle.WebSiteUserVehicle;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.pojo.interstatebattery.InterstateAutomobile;
import com.bsro.databean.vehicle.TireVehicle;

@Service
public class WebSiteUserVehicleDataServiceImpl implements WebSiteUserVehicleDataService {

	private Log logger =  LogFactory.getLog(WebSiteUserVehicleDataServiceImpl.class);
	
	@Autowired
	private WebSiteUserVehicleDAO webSiteUserVehicleDao;
	
	@Autowired
	private WebSiteUserDAO webSiteUserDao;

	@Autowired
	private VehicleDAO vehicleDao;

	@Autowired
	private InterstateBatteryDAO interstateBatteryDao;

	public UserVehicle createWebSiteUserVehicleFromTireData(Long webSiteUserId, String displayName, String year, String make, String model, String submodel) throws Throwable {		
		WebSiteUser webSiteUser = webSiteUserDao.getUserById(webSiteUserId);
		
		WebSiteUserVehicle webSiteUserVehicle = new WebSiteUserVehicle();
		
		webSiteUserVehicle.setDisplayName(displayName);
		webSiteUserVehicle.setWebSiteUserId(webSiteUser.getWebSiteUserId());
		webSiteUserVehicle.setWebSiteUserSubvehicles(new HashSet<WebSiteUserSubvehicle>());
	
		
		List<TireVehicle> tireVehicles = vehicleDao.getVehiclesByYearMakeNameModelNameSubmodelName(Integer.valueOf(year), make, model, submodel);
		
		WebSiteUserSubvehicle tireSubvehicle = new WebSiteUserSubvehicle();
		
		if (tireVehicles != null && !tireVehicles.isEmpty()) {
			tireSubvehicle.setVehicleType(WebSiteUserVehicle.VEHICLE_TYPE_TIRE);
			tireSubvehicle.setYear(Integer.valueOf(tireVehicles.get(0).getYear()));
			tireSubvehicle.setMake(tireVehicles.get(0).getMakeName());
			tireSubvehicle.setModel(tireVehicles.get(0).getModelName());
			tireSubvehicle.setSubmodel(tireVehicles.get(0).getSubmodelName());
			tireSubvehicle.setVehicleObjectId(Long.toString(tireVehicles.get(0).getAcesVehicleId()));
			tireSubvehicle.setWebSiteUserVehicle(webSiteUserVehicle);
			webSiteUserVehicle.getWebSiteUserSubvehicles().add(tireSubvehicle);
		}
		
		try {
			WebSiteUserSubvehicle webSiteUserSubvehicleBattery = matchWebSiteUserSubvehicleBattery(tireSubvehicle);
			webSiteUserSubvehicleBattery.setWebSiteUserVehicle(webSiteUserVehicle);
			if (webSiteUserSubvehicleBattery != null) {
				webSiteUserVehicle.getWebSiteUserSubvehicles().add(webSiteUserSubvehicleBattery);
			}
		} catch (Throwable throwable) {
			// but continue, even if there's no match
			logger.error("Error creating battery subvehicle", throwable);
		}
		
		webSiteUserVehicle = webSiteUserVehicleDao.createWebSiteUserVehicle(webSiteUserVehicle);
		
		UserVehicle userVehicle = this.populateUserVehicleFromWebSiteUserVehicle(webSiteUserVehicle);
		
		return userVehicle;
	}

	public WebSiteUserVehicle updateWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle) throws IOException {
		return webSiteUserVehicleDao.updateWebSiteUserVehicle(webSiteUserVehicle);
	}

	public void deleteWebSiteUserVehicle(Long webSiteUserVehicleId) throws IOException {
		webSiteUserVehicleDao.deleteWebSiteUserVehicle(webSiteUserVehicleId);
	}

	public UserVehicle fetchWebSiteUserVehicle(Long webSiteUserVehicleId) throws IOException {
		WebSiteUserVehicle webSiteUserVehicle = webSiteUserVehicleDao.fetchWebSiteUserVehicle(webSiteUserVehicleId);
		return populateUserVehicleFromWebSiteUserVehicle(webSiteUserVehicle);
	}

	public List<UserVehicle> fetchWebSiteUserVehiclesByWebSiteUser(Long webSiteUserId) throws IOException {
		List<WebSiteUserVehicle> webSiteUserVehicles = webSiteUserVehicleDao.fetchWebSiteUserVehiclesByWebSiteUser(webSiteUserId);
		
		List<UserVehicle> userVehicles = new ArrayList<UserVehicle>();
		
		if (webSiteUserVehicles != null) {
			for (WebSiteUserVehicle webSiteUserVehicle : webSiteUserVehicles) {
				userVehicles.add(populateUserVehicleFromWebSiteUserVehicle(webSiteUserVehicle));
			}
		}
		
		return userVehicles;
	}

	private UserVehicle populateUserVehicleFromWebSiteUserVehicle(WebSiteUserVehicle webSiteUserVehicle) {
		UserVehicle userVehicle = new UserVehicle();
		userVehicle.setId(webSiteUserVehicle.getWebSiteUserVehicleId());
		
		if (webSiteUserVehicle.getWebSiteUserSubvehicles() != null) {
			for (WebSiteUserSubvehicle subvehicle : webSiteUserVehicle.getWebSiteUserSubvehicles()) {
					if (WebSiteUserVehicle.VEHICLE_TYPE_TIRE.equals(subvehicle.getVehicleType())) {
						List<TireVehicle> tireVehicles = vehicleDao.getVehiclesByYearMakeNameModelNameSubmodelName(subvehicle.getYear(), subvehicle.getMake(), subvehicle.getModel(), subvehicle.getSubmodel());
						
						Boolean hasTpms = false;
						
						if (tireVehicles != null) {
							for (TireVehicle tireVehicle : tireVehicles) {								
								if (tireVehicle.getHasTpms() == 1) {
									hasTpms = true;
								} else {
									hasTpms = false;
								}

							}
						}
						
						UserSubvehicleTire userSubvehicleTire = new UserSubvehicleTire();
						userSubvehicleTire.setId(subvehicle.getVehicleObjectId());
						userSubvehicleTire.setYear(Integer.toString(subvehicle.getYear()));
						userSubvehicleTire.setMake(subvehicle.getMake());
						userSubvehicleTire.setModel(subvehicle.getModel());
						userSubvehicleTire.setSubmodel(subvehicle.getSubmodel());
						userSubvehicleTire.setHasTpms(hasTpms);
						userVehicle.setTire(userSubvehicleTire);
					} else if (WebSiteUserVehicle.VEHICLE_TYPE_BATTERY.equals(subvehicle.getVehicleType())) {
						UserSubvehicleBattery userSubvehicleBattery = new UserSubvehicleBattery();
						userSubvehicleBattery.setId(subvehicle.getVehicleObjectId());
						userSubvehicleBattery.setYear(Integer.toString(subvehicle.getYear()));
						userSubvehicleBattery.setMake(subvehicle.getMake());
						userSubvehicleBattery.setModel(subvehicle.getModel());
						userSubvehicleBattery.setEngine(subvehicle.getEngine());
						userVehicle.setBattery(userSubvehicleBattery);
					} else if (WebSiteUserVehicle.VEHICLE_TYPE_OIL.equals(subvehicle.getVehicleType())) {
						UserSubvehicleOil userSubvehicleOil = new UserSubvehicleOil();
						userSubvehicleOil.setId(subvehicle.getVehicleObjectId());
						userSubvehicleOil.setYear(Integer.toString(subvehicle.getYear()));
						userSubvehicleOil.setMake(subvehicle.getMake());
						userSubvehicleOil.setModelSubmodelEngine(subvehicle.getModel());
						userVehicle.setOil(userSubvehicleOil);
					}
			}
			
			if (StringUtils.isNotEmpty(webSiteUserVehicle.getDisplayName())) {
				userVehicle.setDisplayName(webSiteUserVehicle.getDisplayName());
			}
			
			String bestAvailableYear = null;
			String bestAvailableMake = null;
			String bestAvailableModel = null;
			String bestAvailableSubmodel = null;
			String bestAvailableEngine = null;
			
			if (userVehicle.getTire() != null) {
				bestAvailableYear = userVehicle.getTire().getYear();
				bestAvailableMake = userVehicle.getTire().getMake();
				bestAvailableModel = userVehicle.getTire().getModel();
				bestAvailableSubmodel = userVehicle.getTire().getSubmodel();
			}
			if (userVehicle.getBattery() != null) {
				if (StringUtils.isEmpty(bestAvailableYear)) {
					bestAvailableYear = userVehicle.getBattery().getYear();
				}
				if (StringUtils.isEmpty(bestAvailableMake)) {
					bestAvailableMake = userVehicle.getBattery().getMake();
				}
				if (StringUtils.isEmpty(bestAvailableModel)) {
					bestAvailableModel = userVehicle.getBattery().getModel();
				}
				if (StringUtils.isEmpty(bestAvailableEngine)) {
					bestAvailableEngine = userVehicle.getBattery().getEngine();
				}
			}
			
			if (userVehicle.getOil() != null) {
				if (StringUtils.isEmpty(bestAvailableYear)) {
					bestAvailableYear = userVehicle.getOil().getYear();
				}
				if (StringUtils.isEmpty(bestAvailableMake)) {
					bestAvailableMake = userVehicle.getOil().getMake();
				}
				if (StringUtils.isEmpty(bestAvailableModel) && StringUtils.isEmpty(bestAvailableSubmodel) && StringUtils.isEmpty(bestAvailableEngine)) {
					bestAvailableModel = userVehicle.getOil().getModelSubmodelEngine();
				}
			}
			
			StringBuilder fullName = new StringBuilder("");

			if (!StringUtils.isEmpty(bestAvailableYear)) {
				userVehicle.setYear(bestAvailableYear);
				fullName.append(bestAvailableYear).append(" ");
			}
			if (!StringUtils.isEmpty(bestAvailableMake)) {
				userVehicle.setMake(bestAvailableMake);
				fullName.append(bestAvailableMake).append(" ");
			}
			if (!StringUtils.isEmpty(bestAvailableModel)) {
				userVehicle.setModel(bestAvailableModel);
				fullName.append(bestAvailableModel).append(" ");
			}
			if (!StringUtils.isEmpty(bestAvailableSubmodel)) {
				userVehicle.setSubmodel(bestAvailableSubmodel);
				fullName.append(bestAvailableSubmodel).append(" ");
			}
			if (!StringUtils.isEmpty(bestAvailableEngine)) {
				userVehicle.setEngine(bestAvailableEngine);
				fullName.append(bestAvailableEngine).append(" ");
			}
			
			String fullNameString = StringUtils.trim(fullName.toString());
			
			userVehicle.setFullName(fullNameString);
		}
		
		return userVehicle;
	}
	
	public WebSiteUserSubvehicle createWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) throws IOException {
		return webSiteUserVehicleDao.createWebSiteUserSubvehicle(webSiteUserSubvehicle);
	}

	public WebSiteUserSubvehicle updateWebSiteUserSubvehicle(WebSiteUserSubvehicle webSiteUserSubvehicle) throws IOException {
		return webSiteUserVehicleDao.updateWebSiteUserSubvehicle(webSiteUserSubvehicle);
	}

	public void deleteWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) throws IOException {
		webSiteUserVehicleDao.deleteWebSiteUserSubvehicle(webSiteUserSubvehicleId);
	}

	public WebSiteUserSubvehicle fetchWebSiteUserSubvehicle(Long webSiteUserSubvehicleId) throws IOException {
		return webSiteUserVehicleDao.fetchWebSiteUserSubvehicle(webSiteUserSubvehicleId);
	}

	private WebSiteUserSubvehicle matchWebSiteUserSubvehicleTire(WebSiteUserSubvehicle webSiteUserSubvehicleOther) {
		Integer year = webSiteUserSubvehicleOther.getYear();
		String make = webSiteUserSubvehicleOther.getMake();
		String model = webSiteUserSubvehicleOther.getModel();
		String submodel = webSiteUserSubvehicleOther.getSubmodel();

		boolean yearFound = false;
		boolean makeFound = false;
		boolean modelFound = false;
		TireVehicle tireVehicle = null;
		WebSiteUserSubvehicle webSiteUserSubvehicle = new WebSiteUserSubvehicle();
		webSiteUserSubvehicle.setWebSiteUserVehicle(webSiteUserSubvehicleOther.getWebSiteUserVehicle());
		webSiteUserSubvehicle.setVehicleType(WebSiteUserVehicle.VEHICLE_TYPE_TIRE);

		if (year != null && vehicleDao.getMakeList(year).size() > 0) {
			yearFound = true;
			webSiteUserSubvehicle.setYear(year);
		}
		if (yearFound && make != null && vehicleDao.getModelListByNameCaseInsensitive(year.toString(), make).size() > 0) {
			makeFound = true;
			webSiteUserSubvehicle.setMake(make);
		}
		if (yearFound && makeFound && model != null && vehicleDao.getSubmodelListByNameCaseInsensitive(year, make, model).size() > 0) {
			modelFound = true;
			webSiteUserSubvehicle.setModel(model);
		}
		if (yearFound && makeFound && modelFound && submodel != null) {
			List<TireVehicle> tireVehicleList = vehicleDao.getVehiclesByYearMakeNameModelNameSubmodelNameCaseInsensitive(year, make, model, submodel);
			if (tireVehicleList.size() > 0) {
				webSiteUserSubvehicle.setSubmodel(submodel);
				tireVehicle = tireVehicleList.get(0);
				webSiteUserSubvehicle = createWebSiteUserSubvehicleFromTireVehicle(tireVehicle);
			}
		}

		return webSiteUserSubvehicle;
	}

	private WebSiteUserSubvehicle matchWebSiteUserSubvehicleBattery(WebSiteUserSubvehicle webSiteUserSubvehicleOther) {
		Integer year = webSiteUserSubvehicleOther.getYear();
		String make = webSiteUserSubvehicleOther.getMake();
		String model = webSiteUserSubvehicleOther.getModel();
		String engine = webSiteUserSubvehicleOther.getEngine();

		boolean yearFound = false;
		boolean makeFound = false;
		boolean modelFound = false;
		InterstateAutomobile interstateAutomobile = null;
		WebSiteUserSubvehicle webSiteUserSubvehicle = new WebSiteUserSubvehicle();
		webSiteUserSubvehicle.setWebSiteUserVehicle(webSiteUserSubvehicleOther.getWebSiteUserVehicle());
		webSiteUserSubvehicle.setVehicleType(WebSiteUserVehicle.VEHICLE_TYPE_BATTERY);

		if (year != null && interstateBatteryDao.getMakeList(year).size() > 0) {
			yearFound = true;
			webSiteUserSubvehicle.setYear(year);
		}
		if (yearFound && make != null) {
			List<String> models = interstateBatteryDao.getModelListIgnoreCase(year.toString(), make);
			if (models.size() > 0) {
				makeFound = true;
				webSiteUserSubvehicle.setMake(make);
				if (models.size() == 1 && model == null)
					model = models.get(0);
			}
		}
		if (yearFound && makeFound && model != null) {
			List<String> engines = interstateBatteryDao.getEngineListIgnoreCase(year.toString(), make, model);
			if (engines.size() > 0) {
				modelFound = true;
				webSiteUserSubvehicle.setModel(model);
				if (engines.size() == 1 && engine == null)
					engine = engines.get(0);
			}
		}
		if (yearFound && makeFound && modelFound && engine != null) {
			List<InterstateAutomobile> interstateAutomobileList = interstateBatteryDao.getBatteryListIgnoreCase(year.toString(), make, model, engine);
			if (interstateAutomobileList.size() > 0) {
				webSiteUserSubvehicle.setEngine(engine);
				interstateAutomobile = interstateAutomobileList.get(0);
				webSiteUserSubvehicle = createWebSiteUserSubvehicleFromInterstateAutomobile(interstateAutomobile);
			}
		}

		return webSiteUserSubvehicle;
	}

	private WebSiteUserSubvehicle createWebSiteUserSubvehicleFromTireVehicle(TireVehicle tireVehicle) {
		WebSiteUserSubvehicle webSiteUserSubvehicleTire = new WebSiteUserSubvehicle();

		webSiteUserSubvehicleTire.setMake(tireVehicle.getMakeName());
		webSiteUserSubvehicleTire.setMakeId(tireVehicle.getMakeId().toString());
		webSiteUserSubvehicleTire.setModel(tireVehicle.getModelName());
		webSiteUserSubvehicleTire.setModelId(tireVehicle.getModelId().toString());
		webSiteUserSubvehicleTire.setSubmodel(tireVehicle.getSubmodelName());
		webSiteUserSubvehicleTire.setYear(new Integer(tireVehicle.getYear()));
		webSiteUserSubvehicleTire.setVehicleObjectId(tireVehicle.getAcesVehicleId().toString());
		webSiteUserSubvehicleTire.setVehicleType(WebSiteUserVehicle.VEHICLE_TYPE_TIRE);
		return webSiteUserSubvehicleTire;
	}

	private WebSiteUserSubvehicle createWebSiteUserSubvehicleFromInterstateAutomobile(InterstateAutomobile interstateAutomobile) {
		WebSiteUserSubvehicle webSiteUserSubvehicle = new WebSiteUserSubvehicle();
		webSiteUserSubvehicle.setMake(interstateAutomobile.getMake());
		webSiteUserSubvehicle.setModel(interstateAutomobile.getModel());
		webSiteUserSubvehicle.setEngine(interstateAutomobile.getEngine());
		webSiteUserSubvehicle.setYear(new Integer(interstateAutomobile.getYear()));
		webSiteUserSubvehicle.setVehicleObjectId(Long.toString(interstateAutomobile.getAutomobileId()));
		webSiteUserSubvehicle.setVehicleType(WebSiteUserVehicle.VEHICLE_TYPE_BATTERY);
		return webSiteUserSubvehicle;
	}
}
