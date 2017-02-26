package com.bfrc.framework.dao.hibernate3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.bfrc.Config;
import com.bfrc.UserSessionData;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.TireSearchDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bfrc.pojo.UserVehicle;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.TireSearchResults;
import com.bfrc.pojo.tire.TireSubBrand;
import com.bfrc.storelocator.util.LocatorUtil;
import com.bsro.service.store.StoreService;

public class TireSearchDAOImpl extends HibernateDAOImpl implements TireSearchDAO {
	private StoreDAO storeDAO;
	private StoreService storeService;
	private PricingLocatorOperator pricingLocator;
	private LocatorOperator locator;
	private PricingDAO pricingDAO;
	private VehicleDAO vehicleDAO;
	
	public StoreService getStoreService() {
		return storeService;
	}

	public void setStoreService(StoreService storeService) {
		this.storeService = storeService;
	}

	public StoreDAO getStoreDAO() {
		return storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

	public PricingLocatorOperator getPricingLocator() {
		return pricingLocator;
	}

	public void setPricingLocator(PricingLocatorOperator pricingLocator) {
		this.pricingLocator = pricingLocator;
	}

	public LocatorOperator getLocator() {
		return locator;
	}

	public void setLocator(LocatorOperator locator) {
		this.locator = locator;
	}

	public PricingDAO getPricingDAO() {
		return pricingDAO;
	}

	public void setPricingDAO(PricingDAO pricingDAO) {
		this.pricingDAO = pricingDAO;
	}

	public VehicleDAO getVehicleDAO() {
		return vehicleDAO;
	}

	public void setVehicleDAO(VehicleDAO vehicleDAO) {
		this.vehicleDAO = vehicleDAO;
	}
	
	
	public TireSearchResults fetchTireSearchResults(UserSessionData userSessionData, String remoteIP, String geoPoint, String tireSuppressionOverrideToken){
		TireSearchResults results = new TireSearchResults();
		String zip = userSessionData.getZip();
        String locatorStoreNumber = userSessionData.getStoreNumber();	
		
        //--- STEP 1 get Stores ---//
        if(ServerUtil.isNullOrEmpty(zip)){
			results.setEerrorCode("global.error.zip");
			return results;
		} else { //find store by address, even it's a zip code
			List st = getStoresByAddress(userSessionData, remoteIP, geoPoint);
			if(st!=null && st.size()>0) {
				results.setStores(st);
			} else {
				results.setEerrorCode("global.error.nostore");
				return results;
			}
		}/* else { //find store by zip only	
			HashMap map = new HashMap();
			if(Config.PPS.equals(config.getSiteName()))
				map.put("partner", "");
			map.put("pricing", "");
			map.put("licensee", new Boolean(true));
			map.put("remoteIP", request.getRemoteAddr());
			map.put("zip", zip);
			String radius = userSessionData.getRadius();
			if(!ServerUtil.isNullOrEmpty(radius)){
				try {
					map.put("radius", Integer.valueOf(radius));
				} catch(NumberFormatException ex) {}
			}
			try {
				pricingLocator.operate(map);
			} catch(Exception ex) {
				results.setEerrorCode("global.error.nostore");
				return results;
			}
			Store[] stores = (Store[])map.get("result");
			if(stores == null){
				results.setEerrorCode("global.error.nostore");
				return results;
			}else{
				List<Store> lstores = new ArrayList<Store>();
				for(int i=0; i<stores.length; lstores.add(stores[i]),i++)
			    results.setStores(lstores);
			}
			Store firststore = stores[0];
			if(ServerUtil.isNullOrEmpty(locatorStoreNumber)) {
				locatorStoreNumber = String.valueOf(firststore.getNumber());
				userSessionData.setStoreNumber(locatorStoreNumber);
			}
			userSessionData.setStoreCount(stores.length);
			
			StringBuffer sb = new StringBuffer();
			for(Store store: stores){
				if(sb.length() == 0)
					sb.append("|");
				sb.append(String.valueOf(store.getNumber())+"|");
			}
			userSessionData.setStoreNumbers(sb.toString());
		}*/
		//-- STEP2 get Products--//
		
		//Vehicle vehicle = userSessionData.getVehicle();
		String method = userSessionData.getUserRequest();
		if(StringUtils.isNullOrEmpty(method))
            method = TireSearchUtils.SEARCH_BY_VEHICLE;
		Store store = results.getMappedStores().get(userSessionData.getStoreNumber());
		if(store == null){
			store = storeService.findStoreLightById(new Long(locatorStoreNumber));
		}
		//Util.debug(" The tire search entry store is "+store.toString());
		Store regularStore = null;		
		// erk - 11/9/06 - get closest regular store if selected store is a licensee
		String type = store.getType();
		String storeNumber = String.valueOf(store.getNumber());
		if(getConfig().getSiteName().equals(Config.TP)){//TP Only
			java.util.Map secondaryMap = storeDAO.getSecondaryStoreMap(pricingLocator.getConfig());
			Iterator i = secondaryMap.keySet().iterator();
			while(i.hasNext()) {
				String secondaryType = (String)i.next();
				if(secondaryType.equals(type.trim())) {
					HashMap m = new HashMap();
					try {
						m.put("remoteIP", remoteIP);
						m.put("zip", zip);
						m.put("licensee", new Boolean(false));
						m.put("pricing", "y");
						//System.out.println("RA1---------------------------------");
						pricingLocator.operate(m);
						Store[] regularStores = (Store[])m.get("result");
						regularStore = regularStores[0];
						
						userSessionData.setRegularStoreNumber(String.valueOf(regularStore.getNumber()));
						//storeNumber = String.valueOf(regularStore.getNumber());
					} catch(Exception ex) {
						//ex.printStackTrace(System.out);
						// rerun search using broader criteria
						try {
							m.put("full", new Boolean(true));
							//System.out.println("RA2---------------------------------");
							pricingLocator.operate(m);
							Store[] regularStores = (Store[])m.get("result");
							regularStore = regularStores[0];
							//System.out.println("RA3---------------------------------"+regularStore.getNumber());
							userSessionData.setRegularStoreNumber(String.valueOf(regularStore.getNumber()));
							//storeNumber = String.valueOf(regularStore.getNumber());
						} catch(Exception fex) {
							results.setEerrorCode("global.error.nostore");
							return results;
						}
					}
				}
			}
		}else if(getConfig().getSiteName().equals(Config.FIVESTAR)){//5STAR Only
			
			HashMap m = new HashMap();
			try {
				m.put("remoteIP", remoteIP);
				m.put("zip", zip);
				m.put("licensee", new Boolean(false));
				m.put("5starPrimary", "y");
				m.put("pricing", "y");
				pricingLocator.operate(m);
				Store[] regularStores = (Store[])m.get("result");
				regularStore = regularStores[0];
				
				userSessionData.setRegularStoreNumber(String.valueOf(regularStore.getNumber()));
				//storeNumber = String.valueOf(regularStore.getNumber());
			} catch(Exception ex) {
				//ex.printStackTrace(System.out);
				// rerun search using broader criteria
					results.setEerrorCode("global.error.nostore");
					return results;
			}
		}
		if(regularStore == null){
			userSessionData.setRegularStoreNumber(null);
		}
		if (TireSearchUtils.SEARCH_BY_SIZE.equalsIgnoreCase(method)) {
			searchTiresBySize(userSessionData,results);
		}else{
			results = getVehicleFitments(userSessionData,results);
			results = searchTiresByVehicle(userSessionData,results);
			UserVehicle vehicle = new UserVehicle();
			vehicle.setYear(userSessionData.getYear());
            vehicle.setMake(userSessionData.getMake());
            vehicle.setModel(userSessionData.getModel());
            vehicle.setSubmodel(userSessionData.getSubmodel());
			long id = pricingDAO.logGetProducts(Long.valueOf(storeNumber), vehicle);
			userSessionData.setTpUserId(new Long(id));
		}
		if(!StringUtils.isNullOrEmpty(results.getErrorCode()))
			return results;
		if(ServerUtil.TIRE_SUPPRESSION){
			if(tireSuppressionOverrideToken != null && "7YUAwgWQt9wijqwu6vr319ri13i".equals(tireSuppressionOverrideToken)){
			}else{
			removeSuppressedTires(results,storeNumber);
			// Update the pipe-delimited list of articles as well...it is used to search "from cache"
			if(results.getTires() != null && results.getTires().size() > 0){
				StringBuffer sb = new StringBuffer();
				sb.append("|");
				for(Tire tire: results.getTires()){
					sb.append(String.valueOf(tire.getArticle())+"|");
				}
				userSessionData.setArticles(sb.toString());
			}
			}
		}
		populateTirePricing(userSessionData,results);
		results.setUserSessionData(userSessionData);
		results.refreshUserSessionData();
		//session.setAttribute(UserSessionData._USER_SESSION_DATA, userSessionData);
		return results;
	}
	
	private void removeSuppressedTires(TireSearchResults results, Object storeNumber){
		if(storeNumber == null)
			return;
		List globals = pricingDAO.getGlobalSppressedTireArticles();
		Map storeNumberTires = pricingDAO.getStoreNumberMappedSppressedTireArticles();
		Map<Long, List<String>> skuVehType = pricingDAO.getGlobalVehTypeDisplayIDSuppressedTireArticles();
		Long sn = Long.valueOf(storeNumber.toString());
		List snTires = (List)storeNumberTires.get(sn);
		if(snTires != null && snTires.size() > 0){
			globals.addAll(snTires);
		}
		List tires = results.getTires();
		if(tires == null)
			return;
		List suppressedTires = new ArrayList();
		for(int i=0; tires != null && i<tires.size(); i++){
			Tire tire = (Tire)tires.get(i);
			if(globals.contains(Long.valueOf(tire.getArticle()))){
				suppressedTires.add(tire);
			} else {
				List vehTypes = skuVehType.get(Long.valueOf(tire.getArticle()));
				if (vehTypes != null && vehTypes.size() > 0) {
					if (vehTypes.contains(tire.getVehtype())) {
						suppressedTires.add(tire);
					}
				}
			}
		}
		for(int i=0; i<suppressedTires.size(); i++){
			Tire tire = (Tire)suppressedTires.get(i);
			tires.remove(tire);
		}
	}
	
	private List getStoresByAddress(UserSessionData userSessionData, String remoteIP, String geoPoint) {
		String address = userSessionData.getZip();
		String app = locator.getConfig().getSiteName();
	    Float[] location;
	    long requestId = locator.getRequestId();
	    if (ServerUtil.isNullOrEmpty(geoPoint)) {
	        location = locator.geoLocationWithBing(requestId, app, address, null, null, null, remoteIP);
	    } else {
	        location = new Float[2];
	        String[] pointString = geoPoint.split(",");
	        location[1] = new Float(pointString[0]);
	        location[0] = new Float(pointString[1]);
	    }
	    long[][] storeArray = null;
		if(ServerUtil.isNullOrEmpty(userSessionData.getRadius())) 
			userSessionData.setRadius("3");
		try {
			if(getConfig().getSiteName().equals(Config.HT) || getConfig().getSiteName().equals(Config.WWT))
				storeArray = locator.getClosestStores(location, app, true, Integer.parseInt(userSessionData.getRadius()), false, null, true, null);
			else
				storeArray = locator.getClosestStores(location, app, true, Integer.parseInt(userSessionData.getRadius()), false, null, false, null);
		} catch(Exception ex) {}
		Store[] stores = null;
		if (storeArray != null && storeArray.length > 0) {
	        stores = storeService.getStoresLight(storeArray);
		}
		List<Store> lstores = new ArrayList<Store>();
		boolean notClosedStoresExist = false;
		if(stores!=null) {
			String prefferedStoreNumber = userSessionData.getStoreNumber();
			List<Long> storeNumbers = new ArrayList<Long>();
			boolean added = false;
			for(int i=0; i<stores.length; i++) {
				Store store = stores[i];
				lstores.add(store);
				storeNumbers.add(store.getStoreNumber());
				store.setStoreHour(LocatorUtil.getStoreHourHTML(locator.getLocatorDAO().getStoreHour(store.getNumber()), true));
				if(!added && store.getActive() != 0) {
					added = true;
				}
			}
			
			if(org.apache.commons.lang.StringUtils.isBlank(prefferedStoreNumber)) {
				if(!storeNumbers.isEmpty()) {
					prefferedStoreNumber = String.valueOf(storeNumbers.get(0));
				}
			}
			else{
				long storeNumber = Long.parseLong(prefferedStoreNumber);
				if (!storeNumbers.contains(storeNumber)) {
					if(!storeNumbers.isEmpty()) {
						prefferedStoreNumber = String.valueOf(storeNumbers.get(0));
					}
				}
			}
			
			notClosedStoresExist = added;
			userSessionData.setStoreCount(stores.length);
			userSessionData.setStoreNumber(prefferedStoreNumber);
			
			String storeNumbersStr = org.apache.commons.lang.StringUtils.join(storeNumbers.iterator(), "|");
			userSessionData.setStoreNumbers(storeNumbersStr);
			
			HashMap<Long,Long> distances = new HashMap<Long,Long>();
			if (stores != null && stores.length > 0) {
				for(int i = 0 ; i < stores.length ; i++) {
					distances.put(stores[i].getStoreNumber(), storeArray[i][1]);
				}
				userSessionData.setDistances(distances);
			}
			
//			storeService.setMilitaryStores(lstores);
//			storeService.setHolidayHours(lstores);
		}
		return notClosedStoresExist ? lstores : Collections.EMPTY_LIST;
	}
	
	
	public TireSearchResults searchTiresBySize(UserSessionData userSessionData,TireSearchResults results){
		List<Tire> tires = vehicleDAO.searchTiresBySize(getConfig().getSiteName(), userSessionData.getStoreNumber(),userSessionData.getCrossSection(), userSessionData.getAspect(), userSessionData.getRimSize());
		if(tires != null && tires.size() > 0){
			StringBuffer sb = new StringBuffer();
			sb.append("|");
			for(Tire tire: tires){
				sb.append(String.valueOf(tire.getArticle())+"|");
				//articles.add(new Long(tire.getArticle()));
			}
			userSessionData.setArticles(sb.toString());
			results.setTires(tires);
		}else{
			results.setEerrorCode("global.error.noproductsforsize");
		}
		results.setUserSessionData(userSessionData);
        return results;
	}
	
	@SuppressWarnings("unchecked")
	public TireSearchResults searchTiresByVehicle(UserSessionData userSessionData,TireSearchResults results){
		
		boolean byAdvanced = TireSearchUtils.SEARCH_BY_ADVANCED.equalsIgnoreCase(userSessionData.getUserRequest());
		boolean queryTirePrice  = true;
		String tireGroup = userSessionData.getCategory();
		String tireClass = userSessionData.getSegment();
		if(byAdvanced){
			if(StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
				queryTirePrice  = false;
			}else if(!StringUtils.isNullOrEmpty(tireGroup) && StringUtils.isNullOrEmpty(tireClass)){
				queryTirePrice  = false;
			}
		}

		//Util.debug(" The tire search dao store is "+userSessionData.getStoreNumber());
		List<Tire> tires = vehicleDAO.searchTiresByVehicle(getConfig().getSiteName(), userSessionData.getStoreNumber(), userSessionData.getStoreNumber(),userSessionData.getAcesVehicleId(),  
				byAdvanced, tireGroup, tireClass);
		if(tires != null && tires.size() > 0){
			if(queryTirePrice){
				StringBuffer sb = new StringBuffer();
				sb.append("|");
				for(Tire tire: tires){
					sb.append(String.valueOf(tire.getArticle())+"|");
					//articles.add(new Long(tire.getArticle()));
				}
			    userSessionData.setArticles(sb.toString());
			}
			results.setTires(tires);
		}else{
			results.setEerrorCode("global.error.noproductsforvehicle");
		}
		results.setUserSessionData(userSessionData);
        return results;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public TireSearchResults getVehicleFitments(UserSessionData userSessionData,TireSearchResults results){
		//Vehicle vehicle = userSessionData.getVehicle();
		List fitments = null;
		if(StringUtils.isNullOrEmpty(userSessionData.getAcesVehicleId())){
			try{
				Integer.parseInt(userSessionData.getMakeId());
				fitments =vehicleDAO.getFitmentsByIds(userSessionData.getYear(), 
                        userSessionData.getMakeId(), 
                        userSessionData.getModelId(), 
                        userSessionData.getSubmodel());
			}catch(Exception ex){
				fitments =vehicleDAO.getFitmentsByNames(userSessionData.getYear(), 
						                                userSessionData.getMake(), 
						                                userSessionData.getModel(), 
						                                userSessionData.getSubmodel());
			}
		}else{
		    fitments =vehicleDAO.getFitments(userSessionData.getAcesVehicleId());
		}
		if(fitments == null){
			results.setEerrorCode("global.error.noproductsforvehicle");
			return results;
		}else{
			if(StringUtils.isNullOrEmpty(userSessionData.getAcesVehicleId())){
			    Fitment fit = (Fitment)fitments.get(0);
			    userSessionData.setAcesVehicleId(String.valueOf(fit.getAcesVehicleId()));
			}
			
		}
		userSessionData.setFitments(fitments);
		results.setUserSessionData(userSessionData);
		return results;
	}
	
	@SuppressWarnings("unchecked")
	public TireSearchResults populateTirePricing(UserSessionData userSessionData,TireSearchResults results){
		if(userSessionData.getArticles() != null){
			List<Tire> tires = vehicleDAO.populateTiresPricing(getConfig().getSiteName(), userSessionData.getArticles(), userSessionData.getStoreNumber(), userSessionData.getRegularStoreNumber(), results.getTires());
			results.setTires(tires);
			results.setUserSessionData(userSessionData);
		}
        return results;
	}
	
	@SuppressWarnings("unchecked")
	public TireSearchResults populateTireDescription(UserSessionData userSessionData,TireSearchResults results){
		List<Tire> tires = vehicleDAO.populateTiresDescription(userSessionData.getArticles());
		results.setTires(tires);
		results.setUserSessionData(userSessionData);
        return results;
	}
	
	public List<TireSubBrand> getAllTireSubBrands() {
		Session session = getSession();
		try {
			@SuppressWarnings("unchecked")
			List<TireSubBrand> results = (List<TireSubBrand>) session.createCriteria(TireSubBrand.class).addOrder(Order.asc("value")).list();
			return results;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
	
	public List<TireSubBrand> getTireSubBrandsByBrandId(Long brandId) {
		if (brandId == null) {
			return null;
		}
		
		Session session = getSession();
		try {
			@SuppressWarnings("unchecked")
			List<TireSubBrand> results = (List<TireSubBrand>) session.createCriteria(TireSubBrand.class).add(Restrictions.eq("brandId", brandId)).addOrder(Order.asc("value")).list();
			return results;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
	
	public List<TireSubBrand> getTireSubBrandsByBrandName(String brandName) {
		if (brandName == null) {
			return null;
		}
		
		Session session = getSession();
		try {
			Query query = session.getNamedQuery("GetTireSubBrandsByBrandName").setResultTransformer(Transformers.aliasToBean(TireSubBrand.class));
			
			query.setString(0, brandName);
			
			@SuppressWarnings("unchecked")
			List<TireSubBrand> results = (List<TireSubBrand>) query.list();
			return results;
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return null;
	}
}
	
