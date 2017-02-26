package com.bsro.service.battery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.battery.Battery;
import app.bsro.model.battery.BatteryQuote;

import com.bfrc.dataaccess.model.vehicle.GenericVehicle;
import com.bfrc.framework.dao.InterstateBatteryDAO;
import com.bfrc.framework.dao.PromotionDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.interstatebattery.InterstateAutomobile;
import com.bfrc.pojo.interstatebattery.InterstateBatteryMaster;
import com.bfrc.pojo.interstatebattery.InterstateBatteryQuote;
import com.bfrc.pojo.interstatebattery.InterstateBatteryWebPrices;
import com.bfrc.pojo.promotion.PromotionImages;
import com.bfrc.pojo.store.Store;

@Service("batteryService")
public class BatteryServiceImpl implements BatteryService {
	@Autowired
	private InterstateBatteryDAO batteryDAO;
	
	@Autowired
	private StoreDAO storeDAO;
	
	@Autowired
	PromotionDAO promotionDAO;
	
	private static final Map<String, String> batteryNamesByProduct = new HashMap<String, String>();
	
	static {
		batteryNamesByProduct.put("PF", "Interstate PowerFast");
		batteryNamesByProduct.put("MT", "Mega-Tron II");
		batteryNamesByProduct.put("MTP", "Mega-Tron Plus");
	}
	
	// This should be configured from the database, in the future. Or maybe a properties file, if that's overkill?
	private static final List<String> fcacFeaturedBatteryProductCodes = Arrays.asList("PF-24F-6", "MT-24", "MTP-24");
	
	private static final Map<String, List<String>> featuredBatteriesBySite = new HashMap<String, List<String>>();
	
	static {
		featuredBatteriesBySite.put("FCAC", fcacFeaturedBatteryProductCodes);
	}
	
	private Map<String, InterstateBatteryWebPrices> getBatteryWebPrices() {
		@SuppressWarnings("unchecked")
		Map<String, InterstateBatteryWebPrices> prices = (Map<String, InterstateBatteryWebPrices>)batteryDAO.getMappedBatteryWebPrices();
		return prices;
	}

	private Map<String, InterstateBatteryMaster> getBatteryMasters() {
		@SuppressWarnings("unchecked")
		Map<String, InterstateBatteryMaster>  masters = (Map<String, InterstateBatteryMaster> )batteryDAO.getMappedBatteryMaster();
		return masters;
	}
 	
	public List<Battery> getFeaturedBatteries(String siteName) {
		
		Map<String, InterstateBatteryWebPrices> prices = getBatteryWebPrices();
		Map<String, InterstateBatteryMaster> masters = getBatteryMasters();

		List<Battery> combined = new ArrayList<Battery>();

		if (featuredBatteriesBySite.containsKey(siteName)) {
			for (String productCode : featuredBatteriesBySite.get(siteName)) {		
					Battery battery = getBatteryByProductCode(productCode, prices, masters);
				    
				    combined.add(battery);
			}
		}
		
		return combined;
	}
	
	public Battery getBatteryByProductCode(String productCode) {
		Map<String, InterstateBatteryWebPrices> prices = getBatteryWebPrices();
		Map<String, InterstateBatteryMaster> masters = getBatteryMasters();
		
		Battery battery = this.getBatteryByProductCode(productCode, prices, masters);
	    
	    return battery;
	}
	
	private Battery getBatteryByProductCode(String productCode, Map<String, InterstateBatteryWebPrices> prices, Map<String, InterstateBatteryMaster> masters) {
		Battery battery = new Battery();
	    battery.setProductCode(productCode);
	    // default product name is just the product code (according to some old code I found)
	    // we'll change it later if we can
	    battery.setProductName(productCode);
	    
		InterstateBatteryMaster master = null;
		InterstateBatteryWebPrices price = null;
		
		if (masters.containsKey(battery.getProductCode())) {
			master = masters.get(battery.getProductCode());
			if (prices.containsKey(masters.get(battery.getProductCode()).getProduct())) {
				price = prices.get(masters.get(battery.getProductCode()).getProduct());
			}
		}
		
		if (master == null || price == null) {
			battery.setHasPricing(false);
		} else {
			battery.setHasPricing(true);
			if (batteryNamesByProduct.containsKey(master.getProduct())) {
				battery.setProductName(batteryNamesByProduct.get(master.getProduct()));
			}
		    battery.setProduct(master.getProduct());
		    battery.setPartNumber(master.getPartNumber());
		    battery.setTotalWarrantyMonths(master.getTotalWarranty());
		    battery.setReplacementWarrantyMonths(master.getReplacementWarranty());
		    battery.setPerformanceWarrantyMonths(master.getTotalWarranty());
		    battery.setColdCrankingAmps(master.getCca());
		    battery.setReserveCapacityMinutes(master.getRcMinutes());
		    battery.setWebPrice(price.getWebPrice());
		    battery.setTradeInCredit(price.getTradeinCredit());
		    battery.setInstallationAmount(price.getInstallationAmt());
		    battery.setSalesText(price.getSalesText());
		    battery.setRegularPrice(price.getRegularPrice());
		    battery.setDiscountArticle(price.getDiscountArticle());
		    battery.setDiscountAmount(price.getDiscountAmount());
		}
	    

	    
	    return battery;
	}
	
	public Map<String, String> getBatteryNamesByProduct() {
		return batteryNamesByProduct;
	}
	
	public Long saveQuote(String siteName, String storeNumber, String productCode, String donationName, String donationArticle,
			String zip, String year, String make, String model, String engine, String donationYOrN, String donationAmount, String donationAmountOther, String firstName, String lastName) throws Exception {
		InterstateBatteryQuote quote = new InterstateBatteryQuote();
		
		Store store = null;
		String type = null;
		InterstateBatteryMaster master = null;
		InterstateBatteryWebPrices price = null;
		
		Map<String, InterstateBatteryWebPrices> prices = getBatteryWebPrices();
		Map<String, InterstateBatteryMaster> masters = getBatteryMasters();
		
		store = storeDAO.findStoreById(new Long(storeNumber));
		
		if (ServerUtil.isNullOrEmpty(year) || ServerUtil.isNullOrEmpty(make) || ServerUtil.isNullOrEmpty(model) || ServerUtil.isNullOrEmpty(engine) || ServerUtil.isNullOrEmpty(productCode) || ServerUtil.isNullOrEmpty(zip) || store == null) {
			throw new IllegalArgumentException("One of the following is null: year="+year+", make="+make+", model="+model+", engine="+engine+", productCode="+productCode+", zip="+zip+", store="+store);
		}

		master = (InterstateBatteryMaster) masters.get(productCode);

		if (master == null) { 
			throw new IllegalArgumentException("Product code "+productCode+" does not have a master");
		}
		
		type = master.getProduct();
		price = (InterstateBatteryWebPrices) prices.get(type);
		
		double myDonationAmount = 0;
		double internetDiscount = 0;
		
		if (ServerUtil.isNullOrEmpty(year) || ServerUtil.isNullOrEmpty(make) || ServerUtil.isNullOrEmpty(model) || ServerUtil.isNullOrEmpty(engine) || ServerUtil.isNullOrEmpty(zip) || store == null || master == null || price == null) {
			throw new IllegalArgumentException("One of the following is null: year="+year+", make="+make+", model="+model+", engine="+engine+", master="+master+", price="+price+", zip="+zip+", store="+store);
		}

		if ("Y".equalsIgnoreCase(donationYOrN)) {
			if ((ServerUtil.isNullOrEmpty(donationAmount) || "other".equalsIgnoreCase(donationAmount)) && !ServerUtil.isNullOrEmpty(donationAmountOther)) {
				donationAmount = donationAmountOther;
			}
			try {
				myDonationAmount = Double.parseDouble(donationAmount);
			} catch (Exception ex) {
				myDonationAmount = 0;
				// ex.printStackTrace();
			}
		}
		

		if (price != null) {
			internetDiscount = price.getDiscountAmount() == null ? 0.00 : price.getDiscountAmount().doubleValue();
		}

		quote.setInstallationAmt(price.getInstallationAmt());
		quote.setCca(master.getCca());
		quote.setPartNumber(master.getPartNumber());
		quote.setProduct(master.getProduct());
		quote.setProductCode(master.getProductCode());
		quote.setRcMinutes(master.getRcMinutes());
		quote.setReplacementWarranty(master.getReplacementWarranty());
		quote.setStoreNumber(store.getStoreNumber());
		quote.setTotalWarranty(master.getTotalWarranty());
		quote.setTradeinCredit(price.getTradeinCredit());
		if (internetDiscount > 0) {
			quote.setDiscountAmount(java.math.BigDecimal.valueOf(internetDiscount));
		}
		if (price.getDiscountArticle() != null) {
			quote.setDiscountArticle(price.getDiscountArticle());
		}
		if (price.getRegularPrice() != null) {
			quote.setRegularPrice(price.getRegularPrice());
		}
		// --- server side validation validation ---//
		if (engine.length() > 40 || make.length() > 30 || model.length() > 40 || zip.length() > 10) {
			throw new IllegalArgumentException("One of the following values is too long: engine="+engine+", make="+make+", model="+model+", zip="+zip);
		}
		quote.setVehicleEngine(engine);
		quote.setVehicleMake(make);
		quote.setVehicleModel(model);
		quote.setVehicleYear(new Short(Short.parseShort(year)));
		quote.setWebPrice(price.getWebPrice());
		quote.setZip(zip);
		quote.setWebSite(siteName);
		if (myDonationAmount > 0) {
			quote.setMowaaAmount(new java.math.BigDecimal(myDonationAmount));
		}
		// change donation to dynamic
		quote.setDonationName(donationName);
		if(donationArticle != null)
		quote.setDonationArticle(Long.valueOf(donationArticle));
		
		if(!ServerUtil.isNullOrEmpty(firstName)&&(!"First Name".equals(firstName))) {
			quote.setFirstName(firstName);
		}
		if(!ServerUtil.isNullOrEmpty(lastName)&&(!"Last Name".equals(lastName))) {
			quote.setLastName(lastName);
		}
		batteryDAO.createInterstateBatteryQuote(quote);
		
		return quote.getBatteryQuoteId();
	}
	
	public void updateQuote(Long quoteId, Short quantity, String batteryRebateId) throws Exception {
		if (!ServerUtil.isNullOrEmpty(quoteId)) {
			InterstateBatteryQuote interstateBatteryQuote = batteryDAO.getInterstateBatteryQuote(quoteId);
			interstateBatteryQuote.setQuantity(quantity);			
			
			mapInterstateBatteryQuoteToBatteryQuote(interstateBatteryQuote, batteryRebateId);
			
			batteryDAO.updateInterstateBatteryQuote(interstateBatteryQuote);
		}
	}
	
	public BatteryQuote retrieveQuote(String quoteId, String batteryRebateId) {
		InterstateBatteryQuote interstateBatteryQuote = null;
		
		if (!ServerUtil.isNullOrEmpty(quoteId)) {
			long lid = Long.parseLong(quoteId);
			quoteId = String.valueOf(lid);
			interstateBatteryQuote = batteryDAO.getInterstateBatteryQuote(quoteId);
		} else {
			return null;
		}
		
		return mapInterstateBatteryQuoteToBatteryQuote(interstateBatteryQuote, batteryRebateId);
	}
	
	private BatteryQuote mapInterstateBatteryQuoteToBatteryQuote(InterstateBatteryQuote interstateBatteryQuote, String batteryRebateId) {
		BatteryQuote batteryQuote = new BatteryQuote();
		
		// Basic info
		batteryQuote.setBatteryQuoteId(interstateBatteryQuote.getBatteryQuoteId());
		batteryQuote.setCreatedDate(interstateBatteryQuote.getCreatedDate());
		batteryQuote.setFirstName(interstateBatteryQuote.getFirstName());
		batteryQuote.setLastName(interstateBatteryQuote.getLastName());
		batteryQuote.setStoreNumber(interstateBatteryQuote.getStoreNumber());
		batteryQuote.setZip(interstateBatteryQuote.getZip());
		
		// Vehicle info
		GenericVehicle vehicle = new GenericVehicle();
		vehicle.setYear(Short.toString(interstateBatteryQuote.getVehicleYear()));
		vehicle.setMake(interstateBatteryQuote.getVehicleMake());
		vehicle.setModel(interstateBatteryQuote.getVehicleModel());
		vehicle.setSubmodel(interstateBatteryQuote.getVehicleSubmodel());
		vehicle.setEngine(interstateBatteryQuote.getVehicleEngine());
		batteryQuote.setVehicle(vehicle);
		
		// Battery info
		Battery battery = getBatteryByProductCode(interstateBatteryQuote.getProductCode());
		batteryQuote.setBattery(battery);
		
		Short quantity = 1;
		Short interstateQuantity = interstateBatteryQuote.getQuantity();
		if (interstateQuantity != null && interstateQuantity > 0) {
			quantity = interstateQuantity;
		}
		
		batteryQuote.setQuantity(quantity);
		batteryQuote.setPriceForQuantity(quantity.doubleValue() * battery.getWebPrice().doubleValue());
		batteryQuote.setInstallationForQuantity(quantity.doubleValue() * battery.getInstallationAmount().doubleValue());
		batteryQuote.setSubtotal((battery.getInstallationAmount().doubleValue() + battery.getWebPrice().doubleValue()) * quantity);
		
		// Calculations
		// Old "meals on wheels" donations take precedence for when displaying old quotes - newer quotes contain their own descriptions, etc.
		double mealsOnWheelsDonationAmount = Double.parseDouble(interstateBatteryQuote.getMowaaAmount() == null ? "0" : interstateBatteryQuote.getMowaaAmount().toString());

		if (mealsOnWheelsDonationAmount > 0) {
			batteryQuote.setDonationAmount(mealsOnWheelsDonationAmount);
			batteryQuote.setDonationName(MEALS_ON_WHEELS_DONATION_NAME);
			batteryQuote.setDonationArticle(MEALS_ON_WHEELS_DONATION_ARTICLE);
		} else {
			batteryQuote.setDonationAmount(Double.parseDouble(interstateBatteryQuote.getDonationAmount() == null ? "0" : interstateBatteryQuote.getDonationAmount().toString()));
			batteryQuote.setDonationName(interstateBatteryQuote.getDonationArticle() == null ? "" : interstateBatteryQuote.getDonationArticle() + "");
			batteryQuote.setDonationArticle(interstateBatteryQuote.getDonationName() == null ? "" : interstateBatteryQuote.getDonationName());
		}		
		batteryQuote.setTotal(batteryQuote.getSubtotal() + batteryQuote.getDonationAmount());
		BigDecimal total = new BigDecimal(batteryQuote.getTotal());
		interstateBatteryQuote.setTotalPrice(total);
		
		batteryQuote.setIsEligibleForBatteryRebate(false);
		if (batteryRebateId != null) {
			PromotionImages rebate = promotionDAO.getPromotionImagesById(batteryRebateId);
			if (rebate != null) {
				if (rebate.getExpirationDate() == null)
					rebate.setExpirationDate(new Date());
				if (rebate.getStartDate() == null)
					rebate.setStartDate(new Date());
			}
	
			Date rebateEndDate = null;
			if (rebate != null) {
				Date today = new Date();
				Date rebateStartDate = rebate.getStartDate();
				rebateEndDate = rebate.getExpirationDate();
	
				if (today.after(rebateStartDate) && today.before(rebateEndDate)) {
					//String rebateAmountString = rebate.getPriceInfo();
					//BigDecimal rebateAmount = new BigDecimal(rebateAmountString);
					
					batteryQuote.setIsEligibleForBatteryRebate(true);
					//batteryQuote.setBatteryRebateAmount(rebateAmount.doubleValue());
					//batteryQuote.setTotalAfterRebate(batteryQuote.getTotal() - batteryQuote.getBatteryRebateAmount());
					battery.setPromoText(rebate.getDescription());
					battery.setRebateId(rebate.getFriendlyId());
					battery.setExpirayDate(rebate.getExpirationDate());
					batteryQuote.setBattery(battery);
				}
			}
		}

		return batteryQuote;
	}

	
	@SuppressWarnings("unchecked")
	public List<Battery> findBatteriesByVehicle(String year, String make, String model, String engine) {
		List<Battery> results = new ArrayList<Battery>();
		
		Map<String, InterstateBatteryWebPrices> prices = getBatteryWebPrices();
		Map<String, InterstateBatteryMaster> masters = getBatteryMasters();

		InterstateBatteryMaster master = null;

		String productCode = null;
		String type = null;
		String cca = null;
		Map<String, List<InterstateAutomobile>> type_items = new LinkedHashMap<String, List<InterstateAutomobile>>();
		Map<String, String> type_ccas = new LinkedHashMap<String, String>();
		Map<String, InterstateAutomobile> type_item = new LinkedHashMap<String, InterstateAutomobile>();
		
		List<InterstateAutomobile> list = null;
		list = (List<InterstateAutomobile>)batteryDAO.getBatteryList(new Short(year), make, model, engine);
				
		for (InterstateAutomobile battery : list) {
			productCode = battery.getProductCode();
			type = getTypeFromProductCode(productCode);
			cca = battery.getCca();
			master = (InterstateBatteryMaster) masters.get(productCode);
			if (master != null) {
				if (type_items.get(type) == null) {
					List<InterstateAutomobile> items = new ArrayList<InterstateAutomobile>();
					items.add(battery);
					type_items.put(type, items);
					type_ccas.put(type, cca);
				} else {
					if (type_ccas.get(type) != null && type_ccas.get(type).equals(cca)) {
						type_items.get(type).add(battery);
					}
				}
			} else {
				if (type_items.get(type) == null) {
					List<InterstateAutomobile> items = new ArrayList<InterstateAutomobile>();
					items.add(battery);
					type_items.put(type, items);
					type_ccas.put(type, cca);
				} else {
					if (type_ccas.get(type) != null && type_ccas.get(type).equals(cca)) {
						type_items.get(type).add(battery);
					}
				}
			}
		}

		Iterator<String> typeIterator = type_items.keySet().iterator();
		while (typeIterator.hasNext()) {
			type = typeIterator.next();

			if (type_item.get(type) == null) {
				// if CCA the same pick the products randomly
				List<InterstateAutomobile> rawitems = type_items.get(type);
				List<InterstateAutomobile> items = new ArrayList<InterstateAutomobile>();
				for (int k = 0; k < rawitems.size(); k++) {
					InterstateAutomobile battery = rawitems.get(k);
					master = null;
					if (battery != null) {
						productCode = battery.getProductCode();
						master = (InterstateBatteryMaster) masters.get(productCode);
						if (master != null) {
							items.add(battery);
						}
					}
				}
				if (items.size() > 0) {
					InterstateAutomobile battery = null;
					if (items.size() > 1) {
						Random rand = new Random();
						int r = rand.nextInt(items.size());
						battery = items.get(r);
						type_item.put(type, battery);
					} else {
						battery = items.get(0);
						type_item.put(type, battery);
					}
				}
			}
		}
		
		// All of the above is existing logic to create a map of InterstateAutomobile objects that are keyed to "type" (aka., "product")
		// Downstream, it appears that we never use the InterstateAutomobile object for anything but its product code
		// So, below, we abstract all of the complexity of this away and just return Battery objects with the necessary data
		List<String> types = null;
		if (type_item != null) {
			String[] typeArray = (String[]) type_item.keySet().toArray(new String[type_item.keySet().size()]);
			types = (List<String>) Arrays.asList(typeArray);
		}
		
		for (String product : types) {
			InterstateAutomobile interstateAutomobile = type_item.get(product);
			Battery myBattery = getBatteryByProductCode(interstateAutomobile.getProductCode(), prices, masters);
			myBattery.setProductOption(interstateAutomobile.getOptn());
			results.add(myBattery);
		}
		
		return results;
	}
	
	private String getTypeFromProductCode(String productCode) {
		int index = productCode.indexOf("-");
		if (index > 0) {
			return productCode.substring(0, index);
		}
		return "Other";
	}
	
	public void setInterstateBatteryDAO(InterstateBatteryDAO batteryDAO) {
		this.batteryDAO = batteryDAO;
	}
	
	public Map<String, String> getYearOptions() throws Exception {
		@SuppressWarnings("unchecked")
		List<Long> years = (List<Long>)batteryDAO.getYearList();
		
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (years != null) {
			for (Long year : years) {
				options.put(year.toString(), year.toString());
			}
		}
		
		return options;
	}
	
	public Map<String, String> getMakeOptionsByYear(String year) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> makes = batteryDAO.getMakeList(year);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (makes != null) {
			for (String make : makes) {
				options.put(make, make);
			}
		}
		
		return options;
	}
	
	public Map<String, String> getModelOptionsByYearAndMakeName(String year, String makeName) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> models = batteryDAO.getModelList(year, makeName);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (models != null) {
			for (String model : models) {
				options.put(model, model);
			}
		}
		
		return options;
	}
	
	public Map<String, String> getEngineOptionsByYearAndMakeNameAndModelName(String year, String makeName, String modelName) throws Exception {
		@SuppressWarnings("unchecked")
		List<String> engines = batteryDAO.getEngineList(year, makeName, modelName);
		Map<String, String> options = new LinkedHashMap<String, String>();
		
		if (engines != null) {
			for (String engine : engines) {
				options.put(engine, engine);
			}
		}
		
		return options;
	}
}
