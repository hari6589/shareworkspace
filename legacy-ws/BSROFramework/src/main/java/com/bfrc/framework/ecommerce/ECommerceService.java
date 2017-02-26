package com.bfrc.framework.ecommerce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.WebApplicationContext;

import com.bfrc.Config;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.pojo.pricing.TpArticleLog;
import com.bfrc.pojo.pricing.TpHierarchyPrice;
import com.bfrc.pojo.pricing.TpTaxDesc;
import com.bfrc.pojo.pricing.TpTaxStatus;
import com.bfrc.pojo.pricing.TpUserLog;
import com.bfrc.pojo.pricing.MobileTireInstallFee;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.TireSearchResults;

public class ECommerceService {
	private static final String moneyFormatString = "##.00"; // was pattern
	private static java.text.DecimalFormat decimaValueFormatter = new java.text.DecimalFormat(moneyFormatString);
	private static final String TAX_ITEM_LABOR = "Labor";
	private static final String TAX_ITEM_EXCISE_TAX = "Excise Tax";
	private static final String TAX_ITEM_ROAD_HAZARD = "Road Hazard";
	private static final String TAX_ITEM_DISPOSAL_FEES = "Disposal Fees";
	private static final String TAX_ITEM_PARTS = "Parts";
	private static final String TAX_ITEM_STATE_DISPOSAL_FEES = "STATE DISPOSAL FEES";
	private static final String TAX_ITEM_TIRES = "Tires";
	private static final String TAX_ITEM_OTHER = "Other";
	
	private static ContactDAO contactDAO;
	private static StoreDAO storeDAO;
	private static PricingDAO pricingDAO;
	private static WebApplicationContext applicationCtx;
	private static List<String> shopSuppliesIgnoredStates = new ArrayList<String>(Arrays.asList("CA", "NY"));
	
	private static void locateBeans(HttpServletRequest req){
    	if (req != null) {
    	if(applicationCtx == null)
    	    applicationCtx =  Config.getCtx(req.getSession().getServletContext());
    	if(contactDAO == null)
    	    contactDAO = (ContactDAO)applicationCtx.getBean("contactDAO");  
    	if(storeDAO == null)
    	    storeDAO = (StoreDAO)applicationCtx.getBean("storeDAO");  
    	if(pricingDAO == null)
    		pricingDAO = (PricingDAO)applicationCtx.getBean("pricingDAO");  
    	}
    }
    
    public static Store getStoreById(Object storeNumber,HttpServletRequest req) {
    	if(storeNumber == null) return null;
    	locateBeans(req);
    	return storeDAO.findStoreById(Long.valueOf(storeNumber.toString()));
    }
        
    public static double getTaxRate(Object storeNumber,HttpServletRequest req){
    	if(storeNumber == null) return 0.00d;
    	return getTaxRate(Integer.valueOf(storeNumber.toString()),req);
    }
    
    public static double getTaxRate(Store store){
    	if(store != null && store.getTaxRates() != null){
    		List rates =store.getTaxRates();
    		double total = 0.0d;
    		for(Iterator it = rates.iterator(); it.hasNext();){
    			TpTaxDesc tax = (TpTaxDesc)it.next();
    			if(tax != null){
    			    total += tax.getMaterialRate().doubleValue();
    			}
    		}
    		return total/100;
    	}
    	return 0.00d;
    }
    
    public static double getLaborRate(Store store){
    	if(store != null && store.getTaxRates() != null){
    		List rates =store.getTaxRates();
    		double total = 0.0d;
    		for(Iterator it = rates.iterator(); it.hasNext();){
    			TpTaxDesc tax = (TpTaxDesc)it.next();
    			if(tax != null){
    			    total += tax.getLaborRate().doubleValue();
    			}
    		}
    		return total/100;
    	}
    	return 0.00d;
    }
    
    public static boolean isTaxable(List taxStatus, String taxItemDesc) {
    	for(Iterator it = taxStatus.iterator(); it.hasNext();){
    		TpTaxStatus tax = (TpTaxStatus)it.next();
    		if(tax != null){
    			if (taxItemDesc.equalsIgnoreCase(tax.getDescription().trim())) {
    			   	return tax.getNtaxable() == 1;	
    			}
    		}
    	}
    	return false;
    }
    
    public static boolean isShopSuppliesApply(Store store) {
    	if(store != null){
    		return (!shopSuppliesIgnoredStates.contains(store.getState().trim()) && !"HTP".equalsIgnoreCase(store.getStoreType().trim()));
    	}
    	return true;
    }

    public static double getRoadHazardAmount(Object storeNumber,Object tpArticleId, HttpServletRequest req){
    	trace("             --------------------- storeNumber : "+storeNumber+"  tpArticleId: "+tpArticleId);
    	if(storeNumber == null || tpArticleId == null || req == null) return 0.00d;
    	locateBeans(req);
    	TpArticleLog  tpArticleLog = pricingDAO.findTpArticleLogById(new Long(tpArticleId.toString()));
    	if( tpArticleLog  == null) return 0.00d;
    	TpUserLog tpUserLog = pricingDAO.findTpUserLogByUserId(new Long(tpArticleLog.getTpUserId()));
    	long q = tpUserLog.getQuantity();
    	double retailTotal = q*tpArticleLog.getRetailPrice().doubleValue();
    	trace("             --------------------- retailTotal : "+retailTotal);
    	trace("             --------------------- tpArticleLog.getArticleNumber() : "+tpArticleLog.getArticleNumber());
    	TpHierarchyPrice tpHierarchyPrice = pricingDAO.findTpHierarchyPriceByStoreNumberAndArticle(storeNumber,tpArticleLog.getArticleNumber()+"");
    	
    	if(tpHierarchyPrice == null) return 0.00d;
    	trace("             --------------------- tpHierarchyPrice : "+tpHierarchyPrice);
    	trace("             --------------------- tpHierarchyPrice.getValveStem() : "+tpHierarchyPrice.getValveStem());
    	trace("             --------------------- tpHierarchyPrice.getRoadHazardMinAmount() : "+tpHierarchyPrice.getRoadHazardMinAmount());
    	trace("             --------------------- tpHierarchyPrice.getRoadHazardPercent() : "+tpHierarchyPrice.getRoadHazardPercent());
    	
    	if(tpHierarchyPrice.getRoadHazardMinAmount() != null && tpHierarchyPrice.getRoadHazardPercent() != null){
    		
    	    double minAmount = tpHierarchyPrice.getRoadHazardMinAmount().doubleValue()*q;
    	    double amount = tpHierarchyPrice.getRoadHazardPercent().doubleValue()*retailTotal/100;
    	    trace("             --------------------- minAmount : "+minAmount+"    amount: "+amount);
    	    if(tpArticleLog.getRearRetailPrice() != null && tpArticleLog.getRearRetailPrice().doubleValue() > 0){
    	    	minAmount += tpHierarchyPrice.getRoadHazardMinAmount().doubleValue()*q;
    	    	amount += tpHierarchyPrice.getRoadHazardPercent().doubleValue()*(q*tpArticleLog.getRearRetailPrice().doubleValue())/100;
    	    	trace("             --------------------- minAmount : "+minAmount+"    amount: "+amount);
    	    }
    	    /* return the bigger one */
    	    return amount >= minAmount ? amount : minAmount;
    	}
    	return 0.00d;
    	
    }
    
    public static boolean isMobileTireInstallStore(Store store) {
    	return "MVAN".equalsIgnoreCase(store.getStoreType());
    }
    
    public static boolean isRoadHazardTaxable(Store store){
    	return "chicago".equalsIgnoreCase(store.getCity());
    }
        
    //  for trace 
    static void trace(Object obj){
    	//System.out.println(obj);
    }
    
    //------------------------------------- VERSION 2 ---------------------//
    public static java.util.Map loadTaxData(
			List results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount, boolean fromHistory,
			HttpServletRequest request) {
		java.util.Map taxData = new java.util.HashMap();
		double taxRate = com.bfrc.framework.ecommerce.ECommerceService.getTaxRate(store);
        double laborRate = com.bfrc.framework.ecommerce.ECommerceService.getLaborRate(store);
        double priceQty = 1.0;
		double retailPrice = 0.0, rearPrice = 0.0, rearSubtotal = 0.0;
		Tire tire = (Tire)results.get(0);
		Tire rearTire = null;
        if(results.size() > 1){
        	rearTire = (Tire)results.get(1);
        }
		if (fromHistory) {
			retailPrice = tpArticleLog.getRetailPrice().doubleValue();
		} else {
			retailPrice = tire.getRetailPrice();
		}
		retailPrice = Double.valueOf(decimaValueFormatter.format(retailPrice - tireDiscount));
		double unitPrice = retailPrice / priceQty;
		double coefficient = count / priceQty;
		double subtotal = retailPrice * coefficient;
		double balancingWeight = (tire.getWheelBalanceWeight()) * count;
		double balancingLabor = (tire.getWheelBalanceLabor()) * count;
		double balancing = (tire.getWheelBalanceWeight() + tire
				.getWheelBalanceLabor())
				* count;
		double valve = tire.getValveStem() * count;
		double excise = tire.getExciseTax() * count;
		double fee = tire.getTireFee() * count;
		double disposal = tire.getDisposalPrice() * count;
		double tpmsVskAmount = 0.0;
        double tpmsLaborAmount = 0.0;
        double mobileTireInstallationFee = 0.0;
        int tpmsVskArticleNumber = 0;
        int  tpmsLaborArticleNumber = 0;
        if(tpArticleLog != null){
        	tpmsVskAmount = tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
        	tpmsLaborAmount = tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
        	tpmsVskArticleNumber = tpArticleLog.getTpmsVskArticleNumber() == null ? 0 : tpArticleLog.getTpmsVskArticleNumber().intValue();
        	tpmsLaborArticleNumber = tpArticleLog.getTpmsLaborArticleNumber() == null ? 0 : tpArticleLog.getTpmsLaborArticleNumber().intValue();
        	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
        }
		
        //--- get it from history data ---//
        if(fromHistory){
        	balancingWeight = tpArticleLog.getBalanceWeight() == null ? 0.0d : tpArticleLog.getBalanceWeight().doubleValue() * count;
            balancingLabor = tpArticleLog.getBalanceLabor() == null ? 0.0d : tpArticleLog.getBalanceLabor().doubleValue() * count;
            balancing = (balancingWeight + balancingLabor);
            valve = tpArticleLog.getValveStem() == null ? 0.0d : tpArticleLog.getValveStem().doubleValue() * count;
            excise = tpArticleLog.getExciseTax() == null ? 0.0d : tpArticleLog.getExciseTax().doubleValue()  * count;
            fee = tpArticleLog.getTireFee() == null ? 0.0d : tpArticleLog.getTireFee().doubleValue() * count;
            disposal = tpArticleLog.getDisposal() == null ? 0.0d : tpArticleLog.getDisposal().doubleValue() * count;
            tpmsVskAmount = tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
        	tpmsLaborAmount = tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
        	tpmsVskArticleNumber = tpArticleLog.getTpmsVskArticleNumber() == null ? 0 : tpArticleLog.getTpmsVskArticleNumber().intValue();
        	tpmsLaborArticleNumber = tpArticleLog.getTpmsLaborArticleNumber() == null ? 0 : tpArticleLog.getTpmsLaborArticleNumber().intValue();
        	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
        }
		double roadHazard = 0.0;
		if (rearTire != null) {
			if (fromHistory) {
				rearPrice = Double.valueOf(decimaValueFormatter.format((tpArticleLog.getRearRetailPrice().doubleValue() - rearTireDiscount)));
			} else {
				rearPrice = Double.valueOf(decimaValueFormatter.format((rearTire.getRetailPrice() - rearTireDiscount)));
			}
			rearPrice = rearPrice/ priceQty;
            rearSubtotal = rearPrice * coefficient;
			double balancingWeightRear = 0.0d;
            double balancingLaborRear = 0.0d;
            if(fromHistory){
            	balancingWeightRear = tpArticleLog.getRearBalanceWeight() == null ? 0.0d : tpArticleLog.getRearBalanceWeight().doubleValue() * count;
            	balancingLaborRear = tpArticleLog.getRearBalanceLabor() == null ? 0.0d : tpArticleLog.getRearBalanceLabor().doubleValue() * count;
            	balancingWeight += balancingWeightRear;
            	balancingLabor += balancingLaborRear;
            	balancing += (balancingWeightRear + balancingLaborRear);
	            valve += tpArticleLog.getRearValveStem() == null ? 0.0d : tpArticleLog.getRearValveStem().doubleValue() * count;
                excise += tpArticleLog.getRearExciseTax() == null ? 0.0d : tpArticleLog.getRearExciseTax().doubleValue()  * count;
                fee += tpArticleLog.getRearTireFee() == null ? 0.0d : tpArticleLog.getRearTireFee().doubleValue() * count;
                disposal += tpArticleLog.getRearDisposal() == null ? 0.0d : tpArticleLog.getRearDisposal().doubleValue() * count;
                tpmsVskAmount += tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
            	tpmsLaborAmount += tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
            	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
            }else{
            	balancingWeightRear = (rearTire.getWheelBalanceWeight()) * count;
            	balancingLaborRear = (rearTire.getWheelBalanceLabor()) * count;
            	balancingWeight += balancingWeightRear;
            	balancingLabor += balancingLaborRear;
                balancing += (balancingWeightRear + balancingLaborRear);
                valve += rearTire.getValveStem() * count;
	            excise += rearTire.getExciseTax() * count;
	            fee += rearTire.getTireFee() * count;
	            disposal += rearTire.getDisposalPrice() * count;
	            if(tpArticleLog != null){
	                tpmsVskAmount += tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
            	    tpmsLaborAmount += tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
            	    mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
	            }
            	
            }
		}
		
		//--- if TPMS Then Remove Valve Stem ---//
		if(tpArticleLog != null){
	        if(tpArticleLog.getTpmsVskArticleNumber() != null && tpArticleLog.getTpmsVskArticleNumber().intValue() > 0){
	            valve = 0.0d;
	        }
		}
		
        double shopSupplies = 0;
        if(fromHistory){
        	shopSupplies = tpArticleLog.getShopHazardSupplyAmount() == null ? 0.0d : tpArticleLog.getShopHazardSupplyAmount().doubleValue();
        } else if (com.bfrc.framework.ecommerce.ECommerceService.isShopSuppliesApply(store)) {		// Rule #3: No shop supply charges allowed in California and New York
	        double totalLabourAmount = (tire.getWheelBalanceLabor() * count) + tpmsLaborAmount;
	        double totalInvoiceAmount = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
	        if (rearTire != null) {
	        	totalLabourAmount += (rearTire.getWheelBalanceLabor() * count);
	        }
	        if (totalInvoiceAmount > 35) {	// Rule #1: General rule is 6% on labor article's when their total value exceeds $35.00
	        	shopSupplies = Double.valueOf(decimaValueFormatter.format(totalLabourAmount * 0.06));
	        }
	        if (shopSupplies > 25) {		//	Rule #2; Shop supply charges not to exceed $25.00
	        	shopSupplies = 25;
	        }
        }
        List taxStatus = store.getTaxStatus();
        boolean partsTaxable = isTaxable(taxStatus, TAX_ITEM_PARTS);
		boolean tireTaxable = isTaxable(taxStatus, TAX_ITEM_TIRES);
		boolean balanceLaborTaxable = isTaxable(taxStatus, TAX_ITEM_LABOR);
		boolean laborTaxable = isTaxable(taxStatus, TAX_ITEM_LABOR);
		boolean disposalTaxable = isTaxable(taxStatus, TAX_ITEM_DISPOSAL_FEES);
		boolean exciseTaxable = isTaxable(taxStatus, TAX_ITEM_EXCISE_TAX);
		boolean roadHazardTaxable = isTaxable(taxStatus, TAX_ITEM_ROAD_HAZARD);
		boolean feeTaxable = isTaxable(taxStatus, TAX_ITEM_STATE_DISPOSAL_FEES);

		if (tpArticleLog != null)
			roadHazard = getRoadHazardAmount(store.getStoreNumber(),
					tpArticleLog.getTpArticleId() + "", request);
		double tireTax = (tireTaxable) ? Double.valueOf(decimaValueFormatter.format((subtotal + rearSubtotal) * taxRate)) : 0.0d;
		double balancingLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(balancingLabor * taxRate)) : 0.0d;
		double balancingWeightTax = 0.0d;
		double valveTax = (partsTaxable) ? Double.valueOf(decimaValueFormatter.format(valve * taxRate)) : 0.0d;
		double exciseTax = (exciseTaxable) ? Double.valueOf(decimaValueFormatter.format(excise * taxRate)) : 0.0d;
		double disposalTax = (disposalTaxable) ? Double.valueOf(decimaValueFormatter.format(disposal * taxRate)) : 0.0d;
		double shopSuppliesTax = Double.valueOf(decimaValueFormatter.format(shopSupplies * taxRate));
		double tpmsVskTax  = (partsTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsVskAmount*taxRate)) : 0.0d;
		double tpmsLaborTax  = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsLaborAmount*taxRate)) : 0.0d;
		double feeTax = (feeTaxable) ? Double.valueOf(decimaValueFormatter.format(fee*taxRate)) : 0.0d;
		
		if(fromHistory){
        	tireTax = tpArticleLog.getRetailTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getRetailTaxAmount().doubleValue()));
            //balancingWeightTax = tpArticleLog.getWheelBalanceTaxAmount() == null ? 0.0d : tpArticleLog.getWheelBalanceTaxAmount().doubleValue();
            balancingLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(balancingLabor * taxRate)) : 0.0d;
        	balancingWeightTax = 0.0d;
        	valveTax = tpArticleLog.getValveStemTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getValveStemTaxAmount().doubleValue()));
            exciseTax = tpArticleLog.getExciseTaxTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getExciseTaxTaxAmount().doubleValue()));
            disposalTax = tpArticleLog.getDisposalTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getDisposalTaxAmount().doubleValue()));
            shopSuppliesTax  = tpArticleLog.getShopHazardSupplyTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getShopHazardSupplyTaxAmount().doubleValue()));
            tpmsVskTax  = tpArticleLog.getTpmsVskTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getTpmsVskTaxAmount().doubleValue()));
            tpmsLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsLaborAmount*taxRate)) : 0.0d;
            feeTax = (feeTaxable) ? Double.valueOf(decimaValueFormatter.format(fee*taxRate)) : 0.0d;
        }

		double roadHazardTax = (isRoadHazardTaxable(store) && roadHazardTaxable) ? Double.valueOf(decimaValueFormatter.format(roadHazard * taxRate)) : 0.0d;
		if(fromHistory){
        	roadHazardTax = tpArticleLog.getRoadHazardTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getRoadHazardTaxAmount().doubleValue()));
        }
		
		double tax  = tireTax
			        + balancingWeightTax
			        + balancingLaborTax
			        + valveTax
			        + exciseTax
			        + disposalTax
			        + feeTax
			        + tpmsVskTax
			        + shopSuppliesTax
			        + tpmsLaborTax;
		
		tax = Double.valueOf(decimaValueFormatter.format(tax));
		double balancingTax = balancingWeightTax + balancingLaborTax;
		double totalNoTax = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+shopSupplies+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
        double totalNoTaxSupplies = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
        double total = Double.valueOf(decimaValueFormatter.format(subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+shopSupplies + tax+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee));
        
        taxData.put("taxRate", new Double(taxRate));
		taxData.put("laborRate", new Double(laborRate));
		taxData.put("unitPrice", new Double(unitPrice));
		taxData.put("subtotal", new Double(subtotal));
		
		taxData.put("balancing", new Double(balancing));
		taxData.put("balancingWeight", new Double(balancingWeight));
		taxData.put("balancingLabor", new Double(balancingLabor));
		taxData.put("valve", new Double(valve));
		taxData.put("disposal", new Double(disposal));
		taxData.put("excise", new Double(excise));
		taxData.put("fee", new Double(fee));
		taxData.put("shopSupplies", new Double(shopSupplies));
		taxData.put("roadHazard", new Double(roadHazard));
		taxData.put("rearPrice", new Double(rearPrice));
		taxData.put("rearSubtotal", new Double(rearSubtotal));
		
		taxData.put("tireTax", new Double(tireTax));
		taxData.put("balancingWeightTax", new Double(balancingWeightTax));
		taxData.put("balancingLaborTax", new Double(balancingLaborTax));
		taxData.put("balancingTax", new Double(balancingTax));
		taxData.put("valveTax", new Double(valveTax));
		taxData.put("exciseTax", new Double(exciseTax));
		taxData.put("disposalTax", new Double(disposalTax));
		taxData.put("shopSuppliesTax", new Double(shopSuppliesTax));
		taxData.put("feeTax", new Double(feeTax));
		taxData.put("roadHazardTax", new Double(roadHazardTax));
		taxData.put("partsTaxable", new Boolean(partsTaxable));
		taxData.put("tireTaxable", new Boolean(tireTaxable));
		taxData.put("balanceLaborTaxable", new Boolean(balanceLaborTaxable));
		taxData.put("disposalTaxable", new Boolean(disposalTaxable));
		taxData.put("exciseTaxable", new Boolean(exciseTaxable));
		taxData.put("roadHazardTaxable", new Boolean(roadHazardTaxable));
		taxData.put("feeTaxable", new Boolean(feeTaxable));
		
		taxData.put("isMobileTireInstallStore", new Boolean(isMobileTireInstallStore(store)));
		taxData.put("mobileTireInstallFee", new Double(mobileTireInstallationFee));

		taxData.put("tax", new Double(tax));
		taxData.put("total", new Double(total));
		if(fromHistory){
			if(tpArticleLog.getTotal() != null){
			    taxData.put("total", new Double(tpArticleLog.getTotal().doubleValue()));
			}
		}
		taxData.put("totalNoTax", new Double(totalNoTax));
		taxData.put("totalNoTaxSupplies", new Double(totalNoTaxSupplies));

		if(tpmsVskArticleNumber > 0){
        	taxData.put("tpmsVskArticleNumber", tpmsVskArticleNumber);
        	taxData.put("tpmsVskAmount", tpmsVskAmount);
        }
        if(tpmsLaborArticleNumber > 0){
        	taxData.put("tpmsLaborArticleNumber", tpmsLaborArticleNumber);
        	taxData.put("tpmsLaborAmount", tpmsLaborAmount);
        }
        taxData.put("tpmsVskTax", new Double(tpmsVskTax));
        
		return taxData;
	}
    
    //---price calculation using different front and rear qty
    public static java.util.Map loadTaxData(
			List results,
			int count, int rearCount,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount, boolean fromHistory,
			HttpServletRequest request) {
		java.util.Map taxData = new java.util.HashMap();
		double taxRate = com.bfrc.framework.ecommerce.ECommerceService.getTaxRate(store);
        double laborRate = com.bfrc.framework.ecommerce.ECommerceService.getLaborRate(store);
        double priceQty = 1.0;
		double retailPrice = 0.0, rearPrice = 0.0, rearSubtotal = 0.0;
		Tire tire = (Tire)results.get(0);
		Tire rearTire = null;
        if(results.size() > 1){
        	rearTire = (Tire)results.get(1);
        }
		if (fromHistory) {
			retailPrice = tpArticleLog.getRetailPrice().doubleValue();
		} else {
			retailPrice = tire.getRetailPrice();
		}
		retailPrice = retailPrice - tireDiscount;
		double unitPrice = retailPrice / priceQty;
		double coefficient = count / priceQty;
		double rearCoefficient = rearCount / priceQty;
		double subtotal = retailPrice * coefficient;
		double balancingWeight = (tire.getWheelBalanceWeight()) * count;
		double balancingLabor = (tire.getWheelBalanceLabor()) * count;
		double balancing = (tire.getWheelBalanceWeight() + tire
				.getWheelBalanceLabor())
				* count;
		double valve = tire.getValveStem() * count;
		double excise = tire.getExciseTax() * count;
		double fee = tire.getTireFee() * count;
		double disposal = tire.getDisposalPrice() * count;
		double tpmsVskAmount = 0.0;
        double tpmsLaborAmount = 0.0;
        double mobileTireInstallationFee = 0.0;
        int tpmsVskArticleNumber = 0;
        int  tpmsLaborArticleNumber = 0;
        if(tpArticleLog != null){
        	tpmsVskAmount = tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
        	tpmsLaborAmount = tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
        	tpmsVskArticleNumber = tpArticleLog.getTpmsVskArticleNumber() == null ? 0 : tpArticleLog.getTpmsVskArticleNumber().intValue();
        	tpmsLaborArticleNumber = tpArticleLog.getTpmsLaborArticleNumber() == null ? 0 : tpArticleLog.getTpmsLaborArticleNumber().intValue();
        	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
        }
		//--- get it from history data ---//
        if(fromHistory){
        	balancingWeight = tpArticleLog.getBalanceWeight() == null ? 0.0d : tpArticleLog.getBalanceWeight().doubleValue() * count;
            balancingLabor = tpArticleLog.getBalanceLabor() == null ? 0.0d : tpArticleLog.getBalanceLabor().doubleValue() * count;
            balancing = (balancingWeight + balancingLabor);
            valve = tpArticleLog.getValveStem() == null ? 0.0d : tpArticleLog.getValveStem().doubleValue() * count;
            excise = tpArticleLog.getExciseTax() == null ? 0.0d : tpArticleLog.getExciseTax().doubleValue()  * count;
            fee = tpArticleLog.getTireFee() == null ? 0.0d : tpArticleLog.getTireFee().doubleValue() * count;
            disposal = tpArticleLog.getDisposal() == null ? 0.0d : tpArticleLog.getDisposal().doubleValue() * count;
            tpmsVskAmount = tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * count;
        	tpmsLaborAmount = tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * count;
        	tpmsVskArticleNumber = tpArticleLog.getTpmsVskArticleNumber() == null ? 0 : tpArticleLog.getTpmsVskArticleNumber().intValue();
        	tpmsLaborArticleNumber = tpArticleLog.getTpmsLaborArticleNumber() == null ? 0 : tpArticleLog.getTpmsLaborArticleNumber().intValue();
        	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
        }
		double roadHazard = 0.0;
		if (rearTire != null) {
			if (fromHistory) {
				rearPrice = (tpArticleLog.getRearRetailPrice().doubleValue() - rearTireDiscount);
			} else {
				rearPrice = (rearTire.getRetailPrice() - rearTireDiscount);
			}
			rearPrice = rearPrice/ priceQty;
            rearSubtotal = rearPrice * rearCoefficient;
			double balancingWeightRear = 0.0d;
            double balancingLaborRear = 0.0d;
            if(fromHistory){
            	balancingWeightRear = tpArticleLog.getRearBalanceWeight() == null ? 0.0d : tpArticleLog.getRearBalanceWeight().doubleValue() * rearCount;
            	balancingLaborRear = tpArticleLog.getRearBalanceLabor() == null ? 0.0d : tpArticleLog.getRearBalanceLabor().doubleValue() * rearCount;
            	balancingWeight += balancingWeightRear;
            	balancingLabor += balancingLaborRear;
            	balancing += (balancingWeightRear + balancingLaborRear);
	            valve += tpArticleLog.getRearValveStem() == null ? 0.0d : tpArticleLog.getRearValveStem().doubleValue() * rearCount;
                excise += tpArticleLog.getRearExciseTax() == null ? 0.0d : tpArticleLog.getRearExciseTax().doubleValue()  * rearCount;
                fee += tpArticleLog.getRearTireFee() == null ? 0.0d : tpArticleLog.getRearTireFee().doubleValue() * rearCount;
                disposal += tpArticleLog.getRearDisposal() == null ? 0.0d : tpArticleLog.getRearDisposal().doubleValue() * rearCount;
                tpmsVskAmount += tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * rearCount;
            	tpmsLaborAmount += tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * rearCount;
            	mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
            }else{
            	balancingWeightRear = (rearTire.getWheelBalanceWeight()) * rearCount;
            	balancingLaborRear = (rearTire.getWheelBalanceLabor()) * rearCount;
                balancing += (balancingWeightRear + balancingLaborRear);
                valve += rearTire.getValveStem() * rearCount;
	            excise += rearTire.getExciseTax() * rearCount;
	            fee += rearTire.getTireFee() * rearCount;
	            disposal += rearTire.getDisposalPrice() * rearCount;
	            if(tpArticleLog != null){
	                tpmsVskAmount += tpArticleLog.getTpmsVskAmount() == null ? 0.0d : tpArticleLog.getTpmsVskAmount().doubleValue() * rearCount;
            	    tpmsLaborAmount += tpArticleLog.getTpmsLaborAmount() == null ? 0.0d : tpArticleLog.getTpmsLaborAmount().doubleValue() * rearCount;
            	    mobileTireInstallationFee = tpArticleLog.getInstallFee() == null ? 0.0d : tpArticleLog.getInstallFee().doubleValue();
	            }
            	
            }
		}
		//--- if TPMS Then Remove Valve Stem ---//
		if (tpArticleLog != null) {
	        if(tpArticleLog.getTpmsVskArticleNumber() != null && tpArticleLog.getTpmsVskArticleNumber().intValue() > 0){
	            valve = 0.0d;
	        }
		}
        double shopSupplies = 0;
        if (com.bfrc.framework.ecommerce.ECommerceService.isShopSuppliesApply(store)) {		// Rule #3: No shop supply charges allowed in California and New York
	        double totalLabourAmount = (tire.getWheelBalanceLabor() * count) + tpmsLaborAmount;
	        double totalInvoiceAmount = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
	        if (rearTire != null) {
	        	totalLabourAmount += (rearTire.getWheelBalanceLabor() * rearCount);
	        }
	        if (totalInvoiceAmount > 35) {	// Rule #1: General rule is 6% on labor article's when their total value exceeds $35.00
	        	shopSupplies = Double.valueOf(decimaValueFormatter.format(totalLabourAmount * 0.06));
	        }
	        if (shopSupplies > 25) {		//	Rule #2; Shop supply charges not to exceed $25.00
	        	shopSupplies = 25;
	        }
        }
		List taxStatus = store.getTaxStatus();
		boolean partsTaxable = isTaxable(taxStatus, TAX_ITEM_PARTS);
		boolean tireTaxable = isTaxable(taxStatus, TAX_ITEM_TIRES);
		boolean balanceLaborTaxable = isTaxable(taxStatus, TAX_ITEM_LABOR);
		boolean laborTaxable = isTaxable(taxStatus, TAX_ITEM_LABOR);
		boolean disposalTaxable = isTaxable(taxStatus, TAX_ITEM_DISPOSAL_FEES);
		boolean exciseTaxable = isTaxable(taxStatus, TAX_ITEM_EXCISE_TAX);
		boolean roadHazardTaxable = isTaxable(taxStatus, TAX_ITEM_ROAD_HAZARD);
		boolean feeTaxable = isTaxable(taxStatus, TAX_ITEM_STATE_DISPOSAL_FEES);

		if (tpArticleLog != null)
			roadHazard = getRoadHazardAmount(store.getStoreNumber(),
					tpArticleLog.getTpArticleId() + "", request);
		double tireTax = (tireTaxable) ? Double.valueOf(decimaValueFormatter.format((subtotal + rearSubtotal) * taxRate)) : 0.0d;
		double balancingLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(balancingLabor * taxRate)) : 0.0d;
		double balancingWeightTax = 0.0d;
		double valveTax = (partsTaxable) ? Double.valueOf(decimaValueFormatter.format(valve * taxRate)) : 0.0d;
		double exciseTax = (exciseTaxable) ? Double.valueOf(decimaValueFormatter.format(excise * taxRate)) : 0.0d;
		double disposalTax = (disposalTaxable) ? Double.valueOf(decimaValueFormatter.format(disposal * taxRate)) : 0.0d;
		double shopSuppliesTax = Double.valueOf(decimaValueFormatter.format(shopSupplies * taxRate));
		double tpmsVskTax  = (partsTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsVskAmount*taxRate)) : 0.0d;
		double tpmsLaborTax  = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsLaborAmount*taxRate)) : 0.0d;
		double feeTax = (feeTaxable) ? Double.valueOf(decimaValueFormatter.format(fee*taxRate)) : 0.0d;
		if(fromHistory){
        	tireTax = tpArticleLog.getRetailTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getRetailTaxAmount().doubleValue()));
            //balancingWeightTax = tpArticleLog.getWheelBalanceTaxAmount() == null ? 0.0d : tpArticleLog.getWheelBalanceTaxAmount().doubleValue();
            balancingLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(balancingLabor * taxRate)) : 0.0d;
        	balancingWeightTax = 0.0d;
        	valveTax = tpArticleLog.getValveStemTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getValveStemTaxAmount().doubleValue()));
            exciseTax = tpArticleLog.getExciseTaxTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getExciseTaxTaxAmount().doubleValue()));
            disposalTax = tpArticleLog.getDisposalTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getDisposalTaxAmount().doubleValue()));
            shopSuppliesTax  = tpArticleLog.getShopHazardSupplyTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getShopHazardSupplyTaxAmount().doubleValue()));
            tpmsVskTax  = tpArticleLog.getTpmsVskTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getTpmsVskTaxAmount().doubleValue()));
            tpmsLaborTax = (laborTaxable) ? Double.valueOf(decimaValueFormatter.format(tpmsLaborAmount*taxRate)) : 0.0d;
            feeTax = (feeTaxable) ? Double.valueOf(decimaValueFormatter.format(fee*taxRate)) : 0.0d;
        }

		double roadHazardTax = (isRoadHazardTaxable(store) && roadHazardTaxable) ? Double.valueOf(decimaValueFormatter.format(roadHazard * taxRate)) : 0.0d;
		if(fromHistory){
        	roadHazardTax = tpArticleLog.getRoadHazardTaxAmount() == null ? 0.0d : Double.valueOf(decimaValueFormatter.format(tpArticleLog.getRoadHazardTaxAmount().doubleValue()));
        }
		
		double tax  = tireTax
			        + balancingWeightTax
			        + balancingLaborTax
			        + valveTax
			        + exciseTax
			        + disposalTax
			        + feeTax
			        + tpmsVskTax
			        + shopSuppliesTax
			        + tpmsLaborTax;
		double balancingTax = balancingWeightTax + balancingLaborTax;
		double totalNoTax = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+shopSupplies+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
        double totalNoTaxSupplies = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;

        double total = subtotal + rearSubtotal + balancing + valve + excise + fee + disposal+shopSupplies + tax+tpmsVskAmount+tpmsLaborAmount+mobileTireInstallationFee;
        
		taxData.put("taxRate", new Double(taxRate));
		taxData.put("laborRate", new Double(laborRate));

		taxData.put("unitPrice", new Double(unitPrice));
		taxData.put("subtotal", new Double(subtotal));
		taxData.put("balancing", new Double(balancing));
		taxData.put("balancingWeight", new Double(balancingWeight));
		taxData.put("balancingLabor", new Double(balancingLabor));
		taxData.put("valve", new Double(valve));
		taxData.put("disposal", new Double(disposal));
		taxData.put("excise", new Double(excise));
		taxData.put("fee", new Double(fee));
		taxData.put("shopSupplies", new Double(shopSupplies));
		taxData.put("roadHazard", new Double(roadHazard));

		taxData.put("rearPrice", new Double(rearPrice));
		taxData.put("rearSubtotal", new Double(rearSubtotal));

		taxData.put("tireTax", new Double(tireTax));
		taxData.put("balancingWeightTax", new Double(balancingWeightTax));
		taxData.put("balancingLaborTax", new Double(balancingLaborTax));
		taxData.put("balancingTax", new Double(balancingTax));
		taxData.put("valveTax", new Double(valveTax));
		taxData.put("exciseTax", new Double(exciseTax));
		taxData.put("disposalTax", new Double(disposalTax));
		taxData.put("shopSuppliesTax", new Double(shopSuppliesTax));
		taxData.put("feeTax", new Double(feeTax));
		taxData.put("roadHazardTax", new Double(roadHazardTax));

		taxData.put("partsTaxable", new Boolean(partsTaxable));
		taxData.put("tireTaxable", new Boolean(tireTaxable));
		taxData.put("balanceLaborTaxable", new Boolean(balanceLaborTaxable));
		taxData.put("disposalTaxable", new Boolean(disposalTaxable));
		taxData.put("exciseTaxable", new Boolean(exciseTaxable));
		taxData.put("roadHazardTaxable", new Boolean(roadHazardTaxable));
		taxData.put("feeTaxable", new Boolean(feeTaxable));
		taxData.put("isMobileTireInstallStore", new Boolean(isMobileTireInstallStore(store)));
		taxData.put("mobileTireInstallFee", new Double(mobileTireInstallationFee));

		taxData.put("tax", new Double(tax));
		taxData.put("total", new Double(total));
		if(fromHistory){
			if(tpArticleLog.getTotal() != null){
			    taxData.put("total", new Double(tpArticleLog.getTotal().doubleValue()));
			}
		}
		taxData.put("totalNoTax", new Double(totalNoTax));
		taxData.put("totalNoTaxSupplies", new Double(totalNoTaxSupplies));

		if(tpmsVskArticleNumber > 0){
        	taxData.put("tpmsVskArticleNumber", tpmsVskArticleNumber);
        	taxData.put("tpmsVskAmount", tpmsVskAmount);
        }
        if(tpmsLaborArticleNumber > 0){
        	taxData.put("tpmsLaborArticleNumber", tpmsLaborArticleNumber);
        	taxData.put("tpmsLaborAmount", tpmsLaborAmount);
        }
        taxData.put("tpmsVskTax", new Double(tpmsVskTax));
        
		return taxData;
	}
    
	public static java.util.Map loadTaxData(
			TireSearchResults results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount, boolean fromHistory,
			HttpServletRequest request) {
		List tires = new ArrayList();
		Tire tire = (Tire)results.getFrontTires().get(0);
		tires.add(tire);
		Tire rearTire = null;
        if(results.isMatchedSet()){
        	rearTire = (Tire)results.getRearTires().get(0);
        	tires.add(rearTire);
        }
        return loadTaxData(tires, count, store, tpArticleLog,
				tireDiscount, rearTireDiscount, fromHistory, request);
		
	}

	public static java.util.Map loadTaxData(
			TireSearchResults results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount,
			HttpServletRequest request) {
		return loadTaxData(results, count, store, tpArticleLog,
				tireDiscount, rearTireDiscount, false, request);
	}
	
	public static java.util.Map loadTaxData(
			List results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount) {
		return loadTaxData(results, count, store, tpArticleLog,
				tireDiscount, rearTireDiscount, false, null);
	}
	
	public static java.util.Map loadTaxData(
			List results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			double tireDiscount, double rearTireDiscount,
			HttpServletRequest request) {
		return loadTaxData(results, count, store, tpArticleLog,
				tireDiscount, rearTireDiscount, false, request);
	}

	public static java.util.Map loadTaxData(
			TireSearchResults results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			HttpServletRequest request) {

		return loadTaxData(results, count, store, tpArticleLog, 0.0,
				0.0, request);
	}
	
	public static java.util.Map loadTaxData(
			List results,
			int count,
			com.bfrc.pojo.store.Store store,
			com.bfrc.pojo.pricing.TpArticleLog tpArticleLog,
			HttpServletRequest request) {

		return loadTaxData(results, count, store, tpArticleLog, 0.0,
				0.0, request);
	}
}
