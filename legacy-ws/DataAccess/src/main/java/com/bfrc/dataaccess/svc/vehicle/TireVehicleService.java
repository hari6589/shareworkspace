package com.bfrc.dataaccess.svc.vehicle;

import java.util.Collection;
import java.util.List;

import app.bsro.model.webservice.BSROWebServiceResponse;

import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.model.vehicle.TirePressure;
import com.bsro.databean.vehicle.TireVehicle;
import com.bsro.databean.vehicle.TireVehicleMake;
import com.bsro.databean.vehicle.TireVehicleModel;
import com.bsro.databean.vehicle.TireVehicleSubModel;
import com.bsro.databean.vehicle.FriendlyVehicleDataBean;
import com.bfrc.pojo.alignment.AlignmentPricing;
import com.bfrc.pojo.alignment.AlignmentPricingQuote;
import com.bfrc.pojo.lookup.SeoVehicleData;
import com.bfrc.pojo.tire.Tire;

public interface TireVehicleService {
	public List<String> getYears();
	
	public List<TireVehicleMake> getMakesByYear(String year);
	
	public String getMakeNameByMakeId(Object id);
	
	public List<TireVehicleModel> getModelsByYearAndMakeName(String year, String makeName);
	
	public String getModelNameByModelId(Object id);
	
	public List<TireVehicleSubModel> getSubModelsByYearAndMakeNameAndModelName(String year, String makeName, String modelName);
	
	public List<TireVehicleSubModel> getSubModelsByYearMakeAndModelName(String year, String makeName, String modelName);
	
	public List<TireVehicleSubModel> getTrimByYearMakeAndModelName(String year, String makeName, String modelName);
	
	public List<TireVehicleMake> getAllMakes();
	
	public List<TireVehicleModel> getModelsByMakeName(String makeName);
	
	public List<String> getYearsByMakeAndModelNames(String makeName, String modelName);
	
	public List<TirePressure> getTirePressuresByName(String year, String make, String model, String submodel);
	
	public List<TirePressure> getTirePressuresById(String year, Long makeId, Long modelId, Long submodelId);
	
	public Collection<StoreInventory> getInventoryByStore(Long storeNumber);
	
	public Collection<StoreInventory> getInventoryByStoreAndArticle(Long storeNumber, Long Article);
	
	public List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber);
	
	public List<Tire> populateTiresByArticles(String siteName, String articleNumbersdelimited,String storeNumber, String regularStoreNumber);
	
	public TireVehicle fetchVehicleByAcesVehicleId(Long acesVehicleId);
	
	List<TireVehicle> getVehiclesByYearMakeNameModelNameSubmodelName(Integer year, String make, String model, String submodel);

	public BSROWebServiceResponse getAlignmentPricingByStore(String acesVehicleId, Long storeNumber, String siteName);

	public AlignmentPricingQuote getAlignmentQuote(Long quoteId,String siteName);

	public BSROWebServiceResponse createRepairAlignmentQuote(Long storeNumber,
			String articleNumber, String altype, Long acesVehicleId,
			String alpricingId, String firstName, String lastName,
			String emailId, String siteName);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String siteName);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String siteName);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String siteName);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel, String siteName);
	
	public FriendlyVehicleDataBean getFriendlyVehicleDataBean(Long acesVehicleId, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(Long acesVehicleId, String siteName);
	
	public List<FriendlyVehicleDataBean> getMakeModelsByVehicleType(String vehicleType);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String siteName);
	
	public SeoVehicleData getSEOVehicleDataBean(String friendlyMake, String friendlyModel, String year, String friendlySubmodel, String siteName);
}
