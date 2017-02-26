package com.bfrc.dataaccess.svc.vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import app.bsro.model.alignment.AlignmentSearchResults;
import app.bsro.model.tire.VehicleFitment;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.dataaccess.dao.generic.FitmentDAO;
import com.bfrc.dataaccess.dao.generic.StoreInventoryDAO;
import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.model.vehicle.Fitment;
import com.bfrc.dataaccess.model.vehicle.TirePressure;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.SeoVehicleDataDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bfrc.pojo.alignment.AlignmentPricing;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Tire;
import com.hibernate.dao.base.BaseDao;

@Service
public class TireVehicleServiceImpl implements TireVehicleService {
	
	@Autowired
	private VehicleDAO vehicleDao;
	
	@Autowired
	private FitmentDAO fitmentDao;
	
	@Autowired
	StoreInventoryDAO storeInventoryDao;
	
	@Autowired
	private PricingDAO pricingDAO;
	
	@Autowired
	@Qualifier("baseAlignmentPricingQuoteDAO")
	BaseDao baseAlignmentPricingQuoteDAO;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	StoreDAO storeDAO;
	
	@Autowired
	private SeoVehicleDataDAO seoVehicleDataDAO;
	
	@Autowired
	HibernateUtil hibernateUtil;
	
	private Map<String, List<String>> vehTypeServiceCategories;
	
	public List<String> getYears() {
		@SuppressWarnings("unchecked")
		List<String> years = (List<String>)vehicleDao.getYearList();

		return years;
	}
	
	public List<TireVehicleMake> getMakesByYear(String year) {
		return vehicleDao.getMakesByYear(year);
	}
	
	public List<TireVehicleModel> getModelsByYearAndMakeName(String year, String makeName) {
		return vehicleDao.getModelsByYearAndMakeName(year, makeName);
	}
	
	public List<TireVehicleModel> getModelsByMakeName(String makeName) {
		return vehicleDao.getModelsByMakeName(makeName);
	}
	
	public List<String> getYearsByMakeAndModelNames(String makeName, String modelName){
		return vehicleDao.getYearOptionsByMakeModel(makeName, modelName);
	}
	
	public List<TireVehicleSubModel> getSubModelsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) {
		return vehicleDao.getSubModelsByYearAndMakeNameAndModelName(year, makeName, modelName);
	}
	
	public List<TireVehicleSubModel> getSubModelsByYearMakeAndModelName(String year, String makeName, String modelName) {
		return vehicleDao.getSubModelsByYearMakeAndModelNames(year, makeName, modelName);
	}
	
	public List<TireVehicleSubModel> getTrimByYearMakeAndModelName(String year, String makeName, String modelName) {
		return vehicleDao.getTrimByYearMakeAndModelNames(year, makeName, modelName);
	}
	
	public List<TireVehicleMake> getAllMakes() {
		return vehicleDao.getAllMakes(); 
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String siteName) {
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(friendlyMake);
		if (friendlyVehicleDataBean != null) {
			SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE", "/vehicle/"+friendlyMake+"/");
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE", "/vehicle/generic-make/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
			List<TireVehicleModel> models = getModelsByMakeName(friendlyMake);
			if (models != null && !models.isEmpty()) {
				friendlyVehicleDataBean.setModels(models);
			}
			if (seoVehicleData != null) {
				friendlyVehicleDataBean.setSeoVehicleData(seoVehicleData);
			}
		}		
		return friendlyVehicleDataBean;
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String siteName) {
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(friendlyMake, friendlyModel);
		if (friendlyVehicleDataBean != null) {
			SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/");
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL", "/vehicle/generic-make/generic-model/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
			List<String> years = getYearsByMakeAndModelNames(friendlyMake, friendlyModel);
			if (years != null && !years.isEmpty()) {
				friendlyVehicleDataBean.setYears(years);
			}
			if (seoVehicleData != null) {
				friendlyVehicleDataBean.setSeoVehicleData(seoVehicleData);
			}
		}
		return friendlyVehicleDataBean;
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String siteName) {
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(friendlyMake, friendlyModel, year);
		if (friendlyVehicleDataBean != null) {
			SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year);
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR", "/vehicle/generic-make/generic-model/generic-year/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
			List<TireVehicleSubModel> submodels = getTrimByYearMakeAndModelName(year, friendlyMake, friendlyModel);
			if (submodels != null && !submodels.isEmpty()) {
				friendlyVehicleDataBean.setSubmodels(submodels);
			}
			if (seoVehicleData != null) {
				friendlyVehicleDataBean.setSeoVehicleData(seoVehicleData);
			}
		}
		return friendlyVehicleDataBean;
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel, String siteName) {
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(friendlyMake, friendlyModel, year, friendlySubmodel);
		if (friendlyVehicleDataBean != null) {
			SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year+"/"+friendlySubmodel+"/");
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/generic-make/generic-model/generic-year/generic-trim/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
			if (seoVehicleData != null) {
				friendlyVehicleDataBean.setSeoVehicleData(seoVehicleData);
			}
		}
		return friendlyVehicleDataBean;
	}
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(Long acesVehicleId, String siteName) {
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(acesVehicleId);
		if (friendlyVehicleDataBean != null) {
			SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyVehicleDataBean.getMakeFriendlyName()+
					"/"+friendlyVehicleDataBean.getModelFriendlyName()+"/"+friendlyVehicleDataBean.getYear()+"/"+friendlyVehicleDataBean.getSubmodelFriendlyName()+"/");
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/generic-make/generic-model/generic-year/generic-trim/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
			if (seoVehicleData != null) {
				friendlyVehicleDataBean.setSeoVehicleData(seoVehicleData);
			}
		}
		return friendlyVehicleDataBean;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(Long acesVehicleId,
			String siteName) {
		SeoVehicleData seoVehicleData = null;
		FriendlyVehicleDataBean friendlyVehicleDataBean = vehicleDao.getFriendlyVehicleDataBean(acesVehicleId);
		if (friendlyVehicleDataBean != null) {
			seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyVehicleDataBean.getMakeFriendlyName()+
					"/"+friendlyVehicleDataBean.getModelFriendlyName()+"/"+friendlyVehicleDataBean.getYear()+"/"+friendlyVehicleDataBean.getSubmodelFriendlyName()+"/");
			if (seoVehicleData == null) {
				seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/generic-make/generic-model/generic-year/generic-trim/");
				seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
			}
		}
		return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String siteName)  {		
		
		SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyMake+"/");
		if (seoVehicleData == null) {
			FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, null, null, null);
			seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE", "/vehicle/generic-make/");
			seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
		}
		return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String siteName)  {		
		
		SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/");
		if (seoVehicleData == null) {
			FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, friendlyModel, null, null);
			seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL", "/vehicle/generic-make/generic-model/");
			seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
		}
		return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String siteName)  {		
		
		SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year+"/");
		if (seoVehicleData == null) {
			FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, friendlyModel, year, null);
			seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR", "/vehicle/generic-make/generic-model/generic-year/");
			seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
		}
		return seoVehicleData;
	}
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel, String siteName)  {		
		
		SeoVehicleData seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/"+friendlyMake+"/"+friendlyModel+"/"+year+"/"+friendlySubmodel+"/");
		if (seoVehicleData == null) {
			FriendlyVehicleDataBean friendlyVehicleDataBean = generateFriendlyVehicleDataBean(friendlyMake, friendlyModel, year, friendlySubmodel);
			seoVehicleData = seoVehicleDataDAO.getSEOVehicleData(siteName, "VEHICLE_MAKE_MODEL_YEAR_TRIM", "/vehicle/generic-make/generic-model/generic-year/generic-trim/");
			seoVehicleData = formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean);
		}
		return seoVehicleData;
	}
	
	public List<FriendlyVehicleDataBean> getMakeModelsByVehicleType(String vehicleType) {
		String fullTireTypeName = TireSearchUtils.getNewTireTypeName(vehicleType);
		if(!"all-tires".equalsIgnoreCase(vehicleType) && fullTireTypeName.isEmpty()) {
			return null;
		}
		List<String> categories = getVehTypeServiceCategories().get(vehicleType);
		List<FriendlyVehicleDataBean> friendlyVehicleDataBeans = vehicleDao.getMakeModelsByVehicleType(categories);
		return friendlyVehicleDataBeans;
	}
	
	public List<TirePressure> getTirePressuresByName(String year, String make, String model, String submodel){
		Collection<Fitment> list = fitmentDao.findByYearMakeModelAndSubModel(year, make, model, submodel);
		return processTirePressureResults(list);
	}
	
	public List<TirePressure> getTirePressuresById(String year, Long makeId, Long modelId, Long submodelId){
		Collection<Fitment> list = fitmentDao.findByYearMakeIdModelIdAndSubModelId(year, makeId, modelId, submodelId);
		return processTirePressureResults(list);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Collection<StoreInventory> getInventoryByStore(Long storeNumber){
		Collection coll = new ArrayList();
		coll.add(storeNumber);
		Collection<StoreInventory> results 
			= (Collection<StoreInventory>)storeInventoryDao.execNamedQuery("StoreInventory.getInventoryByStore", null, coll);
		return results;
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public List<StoreInventory> getInventoryByStoreAndArticle(Long storeNumber, Long article){
		List coll = new ArrayList();
		coll.add(storeNumber);
		coll.add(article);
		List<StoreInventory> results 
			= (ArrayList<StoreInventory>)storeInventoryDao.execNamedQuery("StoreInventory.getInventoryByStoreAndArticle", null, coll);
		return results;
	}

	private List<TirePressure> processTirePressureResults(Collection<Fitment> results){
		List<TirePressure> tirePressureList = new ArrayList<TirePressure>();
		Iterator<Fitment> i = results.iterator();
		 while(i != null && i.hasNext()) {
			Fitment fit = i.next();
			TirePressure pressure = new TirePressure();
			pressure.setAcesVehicleId(fit.getAcesVehicleId());
			if("F".equalsIgnoreCase(fit.getFrb())){
				pressure.setFrontRearBoth("FRONT");
				pressure.setFrontInflation(new Integer(fit.getFrontInf()));
			}else if("R".equalsIgnoreCase(fit.getFrb())){
				pressure.setFrontRearBoth("REAR");
				pressure.setRearInflation(new Integer(fit.getRearInf()));
			}else{
				pressure.setFrontRearBoth("BOTH");
				pressure.setFrontInflation(new Integer(fit.getFrontInf()));
				pressure.setRearInflation(new Integer(fit.getRearInf()));
			}
			pressure.setTireSize(fit.getTireSize());
			pressure.setSpeedRating(fit.getSpeedRating());
			
			if("S".equalsIgnoreCase(fit.getStandardInd()))
				pressure.setStandardIndicator("Standard");
			else
				pressure.setStandardIndicator("Optional");
			
			tirePressureList.add(pressure);
		 }
		return tirePressureList;
	}

	public String getMakeNameByMakeId(Object id) {
		return vehicleDao.getMakeNameByMakeId(id);
	}

	public String getModelNameByModelId(Object id) {
		return vehicleDao.getModelNameByModelId(id);
	}
	
	public List<Tire> populateTiresByArticles(String siteName,
			String articleNumbersdelimited, String storeNumber) {
		return vehicleDao.populateTiresByArticles(siteName, articleNumbersdelimited, storeNumber);
	}
	
	public List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber, String regularStoreNumber) {
		return vehicleDao.populateTiresByArticles(siteName, articleNumbersdelimited, storeNumber, regularStoreNumber);
	}

	public TireVehicle fetchVehicleByAcesVehicleId(Long acesVehicleId) {
		return vehicleDao.fetchVehicleByAcesVehicleId(acesVehicleId);
	}
	
	public List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelName(Integer year, String make, String model, String submodel) {
		return vehicleDao.getVehiclesByYearMakeNameModelNameSubmodelName(year, make, model, submodel);
	}

	public BSROWebServiceResponse getAlignmentPricingByStore(String acesVehicleId, Long storeNumber, String siteName) {
		
		AlignmentPricing alignmentPricing = null;
		TireVehicle tireVehicle = null;
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		if (!StringUtils.isNullOrEmpty(siteName))
				locator.getConfig().setSiteName(siteName);
		
		try {
			if (!StringUtils.isNullOrEmpty(acesVehicleId))
				tireVehicle = fetchVehicleByAcesVehicleId(Long.parseLong(acesVehicleId));
			
			/*if (tireVehicle == null) {
				response.setMessage("Invalid ACESVEHICLEID ");
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				return response;
			}*/
		} catch (Exception e) {
			response.setMessage("Exception Occured while finding Vehicle based on ACSVehicleID : " + e.getMessage());
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
				 
		try {
			boolean isExcluded = (tireVehicle != null) ? pricingDAO.isAlignmentPricingExclusion(tireVehicle.getYear(),tireVehicle.getMakeName(),tireVehicle.getModelName()) : false;
			if(isExcluded) {
				response.setMessage("No Alignment Options Found for Your " +tireVehicle.getYear()+" , "+tireVehicle.getMakeName()+" , "+tireVehicle.getModelName()+" , "+tireVehicle.getSubmodelName());
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
				return response;
			} else {
				alignmentPricing = pricingDAO.getAlignmentPricing(storeNumber);
				if(alignmentPricing != null && !alignmentPricing.equals("")) {
					AlignmentSearchResults results = new AlignmentSearchResults();
					
					// alignment pricing details
					results.setAlignmentPricingId(alignmentPricing.getAlignmentPricingId());
					results.setDistrict(alignmentPricing.getDistrict());
					results.setLifetimeArticle(alignmentPricing.getLifetimeArticle());
					results.setLifetimePricing(alignmentPricing.getLifetimePricing());
					results.setStandardArticle(alignmentPricing.getStandardArticle());
					results.setStandardPricing(alignmentPricing.getStandardPricing());
					results.setStoreType(alignmentPricing.getStoreType());
					results.setThreeYearArticle(alignmentPricing.getThreeYearArticle());
					results.setThreeYearPricing(alignmentPricing.getThreeYearPricing());
					
					// vehicle details
					if (tireVehicle != null) {
						VehicleFitment fitment = new VehicleFitment();
						fitment.setYear(tireVehicle.getYear());
						fitment.setMake(tireVehicle.getMakeName());
						fitment.setModel(tireVehicle.getModelName());
						fitment.setSubmodel(tireVehicle.getSubmodelName());
						fitment.setAcesVehicleId(tireVehicle.getAcesVehicleId());
						results.setFitment(fitment);
					}
					
					// store details
					results.setStoreNumber(storeNumber.toString());
					response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
				    response.setPayload(results);
				    return response;
				}
			}
		} catch(Exception e) {
			response.setMessage("Exception Occured getAlignmentPricingByStore Method : " + e.getMessage());
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		return response;
	}
	
	public AlignmentPricingQuote getAlignmentQuote(Long quoteId, String siteName) {
		
		AlignmentPricingQuote alignmentQuote = null;
		try
		{
			if (!StringUtils.isNullOrEmpty(siteName))
				locator.getConfig().setSiteName(siteName);
			
			alignmentQuote = (AlignmentPricingQuote)baseAlignmentPricingQuoteDAO.findUniqueBy("alignmentQuoteId", quoteId);
		}catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return alignmentQuote;
	}
	
	public BSROWebServiceResponse createRepairAlignmentQuote(Long storeNumber,
			String article, String altype, Long acesVehicleId,
			String alpricingId, String firstName, String lastName,
			String emailId, String siteName)
		{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Store store = null;
		TireVehicle tireVehicle = null;
		try
		{
			if (!StringUtils.isNullOrEmpty(siteName))
				locator.getConfig().setSiteName(siteName);
			
			 AlignmentPricingQuote bean = new AlignmentPricingQuote();
			 try {
			 tireVehicle = fetchVehicleByAcesVehicleId(acesVehicleId);
			 if (tireVehicle == null) {
					response.setMessage("Invalid ACESVEHICLEID ");
					response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
					return response;
				}
			 } catch (Exception e) {
				 response.setMessage("Exception Occured while finding Vehicle based on ACSVehicleID : " + e.getMessage());
				 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				 return response;
			 }
			 
			 try {
			 store = storeDAO.findStoreById(storeNumber);
				if (store == null) {
					response.setMessage("Invalid store number.");
					response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
					return response;
				}
				} catch (Exception e) {
				 response.setMessage("Exception Occured while finding Store based on Store Number : " + e.getMessage());
				 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				 return response;
			 }
			 
	       // short alpricingid=0;
	        Date requestedDate = new Date();
	        AlignmentPricing ap = null;
	        try {
	        ap = pricingDAO.getAlignmentPricing(store.getNumber());
	        if(ap == null){
	        	response.setMessage("Invalid AlignmentPricing.");
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				return response;
	        }
	        } catch (Exception e) {
				 response.setMessage("Exception Occured while finding Alignment based on Store Number : " + e.getMessage());
				 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				 return response;
			 }
	        
	        bean.setFirstName(firstName);
	        bean.setEmailAddress(emailId);
	        bean.setLastName(lastName);
	        bean.setStoreNumber(store.getNumber());
	        bean.setVehicleYear(Short.valueOf(tireVehicle.getYear()));
	        bean.setVehicleMake(tireVehicle.getMakeName());
	        bean.setVehicleModel(tireVehicle.getModelName());
	        bean.setVehicleSubmodel(tireVehicle.getSubmodelName());
	        bean.setArticle(Long.parseLong(article));
	        bean.setZip(store.getZip());
	        bean.setWebSite(siteName);
	        bean.setAlignmentPricingId(Short.parseShort(alpricingId));
	        bean.setDonationName(null);
	        bean.setDonationArticle(null);
	        if("standard".equals(altype)){
	           bean.setPricingName("Standard Pricing");
	           bean.setArticle(ap.getStandardArticle());
	           bean.setPrice(ap.getStandardPricing());
	          // typelabel="Standard";
	          // alprice=ap.getStandardPricing();
	          // artnum=ap.getStandardArticle();
	        }
	        if("lifetime".equals(altype)){
	           bean.setPricingName("Lifetime Pricing");
	           bean.setArticle(ap.getLifetimeArticle());
	           bean.setPrice(ap.getLifetimePricing());
	           
	          // typelabel="Lifetime";
	          // alprice=ap.getLifetimePricing();
	          // artnum=ap.getLifetimeArticle();
	        }
	        if ("threeyear".equalsIgnoreCase(altype)) {
	        	bean.setPricingName("Three Year Pricing");
	        	bean.setArticle(ap.getThreeYearArticle());
	        	bean.setPrice(ap.getThreeYearPricing());
	        }
	       bean.setCreatedDate(requestedDate);
	       Long id = pricingDAO.createAlignmentPricingQuote(bean);
	       response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
	       
	       if(id!= null)
	       response.setPayload(bean);
			
		}catch(Exception e)
		{
			 response.setMessage("Exception Occured createRepairAlignmentQuote Method : " + e.getMessage());
			 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			 return response;
		}
		return response;
	}
	
	public Map<String, List<String>> getVehTypeServiceCategories() {
		if (vehTypeServiceCategories == null) {
			vehTypeServiceCategories = new HashMap<String, List<String>>();
			vehTypeServiceCategories.put("suv-cuv", new ArrayList<String>(Arrays.asList("SUV")));
			vehTypeServiceCategories.put("light-truck", new ArrayList<String>(Arrays.asList("TRK")));
			vehTypeServiceCategories.put("car-minivan", new ArrayList<String>(Arrays.asList("VAN", "MVAN", "PASS")));
			vehTypeServiceCategories.put("all-tires", new ArrayList<String>(Arrays.asList("VAN", "MVAN", "PASS", "SUV", "TRK")));
		}
		return vehTypeServiceCategories;
	}
	
	private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, FriendlyVehicleDataBean friendlyVehicleDataBean) {
		if (seoVehicleData != null && friendlyVehicleDataBean != null) {
			if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getSubmodelName())) {
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 2);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 3);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 4);
			} else if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getYear())) {
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 2);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 3);
			} else if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getModelName())) {
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 2);
			} else if (!StringUtils.isNullOrEmpty(friendlyVehicleDataBean.getMakeName())) {
				formatSeoVehicleData(seoVehicleData, friendlyVehicleDataBean, 1);
			}
			return seoVehicleData;
		}
		return null;
	}
	
	private SeoVehicleData formatSeoVehicleData(SeoVehicleData seoVehicleData, FriendlyVehicleDataBean friendlyVehicleDataBean, int fcode) {
		switch (fcode) {
			case 1:
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-make", friendlyVehicleDataBean.getMakeFriendlyName()));
				seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Make}", friendlyVehicleDataBean.getMakeName()));
				break;
			case 2:
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-model", friendlyVehicleDataBean.getModelFriendlyName()));
				seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Model}", friendlyVehicleDataBean.getModelName()));
				seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Model}", friendlyVehicleDataBean.getModelName()));
				break;
			case 3:
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-year", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Year}", friendlyVehicleDataBean.getYear()));
				seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Year}", friendlyVehicleDataBean.getYear()));
				break;
			case 4:
				seoVehicleData.setRecordId(StringUtils.replace(seoVehicleData.getRecordId(), "generic-trim", friendlyVehicleDataBean.getSubmodelFriendlyName()));
				seoVehicleData.setTitle(StringUtils.replace(seoVehicleData.getTitle(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setDescription(StringUtils.replace(seoVehicleData.getDescription(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setHero(StringUtils.replace(seoVehicleData.getHero(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setCta(StringUtils.replace(seoVehicleData.getCta(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setHeader1(StringUtils.replace(seoVehicleData.getHeader1(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setContent1(StringUtils.replace(seoVehicleData.getContent1(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setHeader2(StringUtils.replace(seoVehicleData.getHeader2(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setContent2(StringUtils.replace(seoVehicleData.getContent2(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setHeader3(StringUtils.replace(seoVehicleData.getHeader3(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				seoVehicleData.setContent3(StringUtils.replace(seoVehicleData.getContent3(), "{Trim}", friendlyVehicleDataBean.getSubmodelName()));
				break;
			default:
				break;
		}
		
		return seoVehicleData;
	}
	
	private FriendlyVehicleDataBean generateFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel){
		FriendlyVehicleDataBean friendlyVehicleDataBean = new FriendlyVehicleDataBean();
		friendlyVehicleDataBean.setMakeName(friendlyMake);
		friendlyVehicleDataBean.setMakeFriendlyName(friendlyMake);
		friendlyVehicleDataBean.setModelName(friendlyModel);
		friendlyVehicleDataBean.setModelFriendlyName(friendlyModel);
		friendlyVehicleDataBean.setSubmodelName(friendlySubmodel);
		friendlyVehicleDataBean.setSubmodelFriendlyName(friendlySubmodel);
		friendlyVehicleDataBean.setYear(year);
		return friendlyVehicleDataBean;
	}

}
