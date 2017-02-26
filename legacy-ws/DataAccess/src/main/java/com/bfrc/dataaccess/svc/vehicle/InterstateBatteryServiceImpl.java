/**
 * 
 */
package com.bfrc.dataaccess.svc.vehicle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.battery.Battery;
import app.bsro.model.battery.BatteryAutomobile;
import app.bsro.model.battery.BatteryMaster;
import app.bsro.model.battery.BatteryProductNameEnum;
import app.bsro.model.battery.BatteryQuote;
import app.bsro.model.battery.BatteryWebPrice;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.core.util.hibernate.HibernateUtil;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.pojo.interstatebattery.BatteryLifeDuration;
import com.bfrc.pojo.promotion.Promotion;
import com.bfrc.pojo.store.Store;
import com.bsro.service.battery.BatteryService;

/**
 * @author schowdhu
 *
 */
@Service
public class InterstateBatteryServiceImpl implements InterstateBatteryService {
	
	@Autowired
	private HibernateUtil hibernateUtil;
	
	@Autowired
	private BatteryService batteryService;
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	PromotionDAO promotionDAO;
	
	@Autowired
	InterstateBatteryDAO batteryDAO;

	DecimalFormat df = new DecimalFormat("#.00");
	
	public Map<String, String> getYears() {
		
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryAutomobile.getYearList");
		
		@SuppressWarnings("unchecked")
		List<Long> years = query.list();
		
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (years != null) {
			for (Long year : years) {
				options.put(String.valueOf(year), String.valueOf(year));
			}
		}
		return options;
		
	}

	public Map<String, String> getMakesByYear(String year) {
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryAutomobile.getMakeList");
		query.setLong("year", new Long(year));
		
		@SuppressWarnings("unchecked")
		List<String> makes = query.list();
	
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (makes != null) {
			for (String make : makes) {
				options.put(make, make);
			}
		}
		return options;
	}

	public Map<String, String> getModelsByYearAndMakeName(String year, String makeName) {
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryAutomobile.getModelList");
		query.setLong("year", new Long(year));
		query.setString("makeName", makeName);
		
		@SuppressWarnings("unchecked")
		List<String> models = query.list();

		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (models != null) {
			for (String model : models) {
				options.put(model, model);
			}
		}
		
		return options;
	}

	public Map<String, String> getEngineSizesByYearAndMakeAndModel(String year,
			String makeName, String modelName) {

		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryAutomobile.getEngineList");
		query.setLong("year", new Long(year));
		query.setString("makeName", makeName);
		query.setString("modelName", modelName);
		
		@SuppressWarnings("unchecked")
		List<String> engineSizes = query.list();

		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (engineSizes != null) {
			for (String engineSize : engineSizes) {
				options.put(engineSize, engineSize);
			}
		}
		
		return options;
	}

	public List<BatteryAutomobile> getBatteryAutomobileList(String year,
			String makeName, String modelName, String engineSize) {
		
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryAutomobile.getBatteryAutomobileList");
		query.setLong("year", new Long(year));
		query.setString("makeName", makeName);
		query.setString("modelName", modelName);
		query.setString("engineSize", engineSize);
		
		@SuppressWarnings("unchecked")
		List<BatteryAutomobile> batteryList = query.list();
		
		return batteryList;
	}
	
	private BatteryMaster getBatteryMaster(String productCode){
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryMaster.getByProductCode");
		query.setString("code", productCode);
		
		@SuppressWarnings("unchecked")
		List<BatteryMaster> batteryMasterList = query.list();

		if(batteryMasterList != null && !batteryMasterList.isEmpty())
			return batteryMasterList.get(0);
		
		return null;
	}
	
	private BatteryWebPrice getBatteryPrice(String product){
		Session session = hibernateUtil.getSessionFactory().getCurrentSession();
		Query query = session.getNamedQuery("BatteryWebPrice.getByProduct");
		query.setString("product", product);
		
		@SuppressWarnings("unchecked")
		List<BatteryWebPrice> batteryPriceList = query.list();

		if(batteryPriceList != null && !batteryPriceList.isEmpty())
			return batteryPriceList.get(0);
		
		return null;
	}
	
	public List<Battery> getBatterySearchResults(String year,
			String makeName, String modelName, String engineSize){
		List<Battery> searchResults = batteryService.findBatteriesByVehicle(year, makeName, modelName, engineSize);		
		return searchResults;
	}

	public BSROWebServiceResponse getBatteryQuote(String quoteId,String batteryRebateId, String siteName) 
	{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		BatteryQuote battery = null;
		try
		{
			if (!StringUtils.isNullOrEmpty(siteName))
				locator.getConfig().setSiteName(siteName);
			
			battery = batteryService.retrieveQuote(quoteId, batteryRebateId);
			
			if (!StringUtils.isNullOrEmpty(battery))
			{
			
			if (!StringUtils.isNullOrEmpty(battery.getSubtotal()))
				battery.setSubtotal(roundOfValue(battery.getSubtotal()));
			
			if (!StringUtils.isNullOrEmpty(battery.getTotal()))
				battery.setTotal(roundOfValue(battery.getTotal()));
			
				response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
				response.setPayload(battery);
				return response;
			}
			else
			{
				response.setMessage("NoDataFound");
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
				return response;
			}
			
		}catch(Exception e)
		{
			response.setMessage("Exception Occured while getBatteryQuote method : " + e.getMessage());
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
	}

	public BSROWebServiceResponse saveBatteryQuote(String siteName, String storeNumber,
			String productCode, String donationName, String donationArticle,
			String zip, String year, String make, String model, String engine,
			String donationYOrN, String donationAmount,
			String donationAmountOther, String firstName, String lastName)
	{
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		Long batteryQuoteId = null;
		BatteryQuote battery = null;
		try
		{
			if (!StringUtils.isNullOrEmpty(siteName))
				locator.getConfig().setSiteName(siteName);
			
			batteryQuoteId = batteryService.saveQuote(siteName, storeNumber, productCode, donationName, donationArticle, zip, year, make, model, engine, donationYOrN, donationAmount, donationAmountOther, firstName, lastName);
		}catch(Exception e)
		{
			 response.setMessage("Exception Occured while saving Battery quote : " + e.getMessage());
			 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			 return response;
		}
		
		try
		{
			if (!StringUtils.isNullOrEmpty(batteryQuoteId))
			battery = batteryService.retrieveQuote(batteryQuoteId.toString(), "fcac_battery_rebate");
			else
			{
				response.setMessage("Exception Occured while saving Battery quote ");
				response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
				return response;
			}
			
			if (!StringUtils.isNullOrEmpty(battery.getSubtotal()))
				battery.setSubtotal(roundOfValue(battery.getSubtotal()));
			
			if (!StringUtils.isNullOrEmpty(battery.getTotal()))
				battery.setTotal(roundOfValue(battery.getTotal()));
			
		}catch(Exception e)
		{
			 response.setMessage("Exception Occured while fetching Battery quote: " + e.getMessage());
			 response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			 return response;
		}
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.toString());
		response.setPayload(battery);
		return response;
		
	}
	
	
	public BatteryService getBatteryService() {
		return batteryService;
	}

	public void setBatteryService(BatteryService batteryService) {
		this.batteryService = batteryService;
	}
	
	public Map<String, String> getBatteryLife(String zip, String siteName) {
		
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		BatteryLifeDuration batterylife = batteryDAO.getBatteryLifeDurationByZipCode(zip);
		
		long lifeMonth = 0;
		long lifeYear = 0;
		long lifeMonthMode = 0;
		Map<String, String> batteryLife = new LinkedHashMap<String, String>();
		
		if (batterylife != null) {
			lifeMonth = Math.round(batterylife.getBatteryLifeAverage().doubleValue());
			lifeYear = (long) (lifeMonth / 12);
			lifeMonthMode = lifeMonth % 12;
			batteryLife.put("lifeYear", String.valueOf(lifeYear));
			batteryLife.put("lifeMonth", String.valueOf(lifeMonthMode));
			batteryLife.put("lifeTotMonth", String.valueOf(lifeMonth));
		}
		
		return batteryLife;
	}
	
	private Double roundOfValue(Double value)
	{
		if (!StringUtils.isNullOrEmpty(value))
			return (Double.valueOf(df.format(value)));
		else
			return null;
	}
	
	// fetching rebate information based on id
	private Promotion getBatteryPromotion()
	{
		String promoId = "fcac_battery_rebate";
		Promotion rebate = promotionDAO.getPromotionByFriendlyId(promoId);
		if (rebate != null) {
			if (rebate.getExpirationDate() == null)
				rebate.setExpirationDate(new Date());
			if (rebate.getStartDate() == null)
				rebate.setStartDate(new Date());

				Date today = new Date();
				Date startDate = rebate.getStartDate();
				Date endDate = rebate.getExpirationDate();
				if (today.after(startDate) && today.before(endDate)) {
					return rebate;
				}
				}
				else
				{
					return null;
				}
			
		return null;
		
	}
}
