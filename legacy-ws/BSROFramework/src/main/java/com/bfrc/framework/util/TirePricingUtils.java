package com.bfrc.framework.util;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.TirePromotionDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.pojo.store.Store;
import com.bfrc.pojo.tire.*;
import com.bfrc.pojo.tirepromotion.TirePromotionEvent;
import com.bfrc.security.*;
import com.bfrc.*;


public class TirePricingUtils {
	
    
	
	private static StoreDAO storeDAO;
	private static HttpServletRequest request;
	private static Config config;
	private static PricingLocatorOperator pricingLocator;
	private static VehicleDAO vehicleDAO;
	private static TirePromotionDAO tirePromotionDAO;
    private static Config thisconfig;
	public static HttpServletRequest getHttpServletRequest(){
    	return request;
    }
    public static void  setHttpServletRequest(HttpServletRequest httpServletRequest){
    	request = httpServletRequest;
    }
	private static void locateBeans(HttpSession httpSession){
		ServletContext ctx = httpSession.getServletContext();
		if(storeDAO == null)
		    storeDAO = (StoreDAO)Config.locate(ctx, "storeDAO");
		if(config == null)
			config = (Config)Config.locate(ctx, "config");
		if(vehicleDAO == null)
			vehicleDAO = (VehicleDAO)Config.locate(ctx, "vehicleDAO");
		if(pricingLocator == null)
			pricingLocator = (PricingLocatorOperator)Config.locate(ctx, "pricingLocator");
		if(tirePromotionDAO == null)
			tirePromotionDAO = (TirePromotionDAO)Config.locate(ctx, "tirePromotionDAO");
		if(thisconfig == null)
			thisconfig = (Config) Config.locate(ctx, "config");
	}
	private static void locateBeans(HttpServletRequest request){
		locateBeans(request.getSession());
	}
	//--- get Tire Promotion Data in one loop ---//
	public static Map getOfferData(HttpServletRequest request,String article, String rearArticle, Store store, String qty) {
		return getOfferData(request,article,rearArticle, store, qty,null);
	}
	
	static private double zeroDiscountPromoAmount = 0.0000001;
	public static Map getOfferData(HttpServletRequest request,String article, String rearArticle, Store store, String qty,java.util.Map offersData) {
		locateBeans(request);
		Map m = new HashMap();
		if(offersData == null)
		    offersData = tirePromotionDAO.loadTirePromotionData(thisconfig.getSiteName(), store.getStoreNumber().longValue());
		TirePromotionEvent promo = null;
		List rebateOffers = new ArrayList();
		List discountOffers = new ArrayList();
		List pid_list = new ArrayList();
		int count = Integer.parseInt(qty);
		String[] promoIds = null;
		String promoId = null;
		double discountAmount = 0.0d;
		double frontDiscountAmount = 0.0d;
		double frontDiscountAmountTotal = 0.0d;
		double rearDiscountAmount = 0.0d;
		double rearDiscountAmountTotal = 0.0d;
		if (offersData.containsKey(article)) {
			String ps = (String) offersData.get(article);
			//System.out.println("________________________________"+ps);
			promoIds = ps.split("\\|");
			for (int i = 0; i < promoIds.length; i++) {
				promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
				promoId = String.valueOf(promo.getPromoId());
				//System.out.println(promoIds[i]+"________________________________"+promo);
				if (('P' == promo.getSourcePromotionType().getPromoType()) || ('D' == promo.getSourcePromotionType().getPromoType())) {
					double this_frontDiscountAmount = 0;
					try {
						this_frontDiscountAmount = tirePromotionDAO.getDiscountAmountByStoreNumberMinQtyPromoId(
								Long.parseLong(article),
								store.getStoreNumber().longValue(),
								(byte) count,
								promo.getPromoId());
					} catch (Exception ex) {
					}

					if (this_frontDiscountAmount > 0.0) {
						frontDiscountAmount = this_frontDiscountAmount;
						discountAmount += this_frontDiscountAmount * count;
						frontDiscountAmountTotal += this_frontDiscountAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					}else{//zero Discount Promo - 20120719
						frontDiscountAmount = zeroDiscountPromoAmount;
						discountAmount += zeroDiscountPromoAmount * count;
						frontDiscountAmountTotal += zeroDiscountPromoAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					}
					//System.out.println("________________________________1");
					//break; //only one promo allowed
				} else {
					if (!pid_list.contains(promoId)) {
						rebateOffers.add(promo);
						pid_list.add(promoId);
					}
					//System.out.println("________________________________2");
				}
			}
		}

		if (rearArticle != null) {
			if (offersData.containsKey(rearArticle)) {
				String ps = (String) offersData.get(rearArticle);
				//System.out.println("________________________________"+ps);
				promoIds = ps.split("\\|");
				for (int i = 0; i < promoIds.length; i++) {
					promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
					promoId = String.valueOf(promo.getPromoId());
					//System.out.println(promoIds[i]+"________________________________"+promo);
					if (('P' == promo.getSourcePromotionType().getPromoType()) || ('D' == promo.getSourcePromotionType().getPromoType())) {
						double this_rearDiscountAmount = 0;
						try {
							this_rearDiscountAmount = tirePromotionDAO.getDiscountAmountByStoreNumberMinQtyPromoId(
									Long.parseLong(rearArticle),
									store.getStoreNumber().longValue(),
									(byte) count,
									promo.getPromoId());
						} catch (Exception ex) {
						}
						if (this_rearDiscountAmount > 0.0) {
							rearDiscountAmount = this_rearDiscountAmount;
							discountAmount += this_rearDiscountAmount * count;
							rearDiscountAmountTotal += this_rearDiscountAmount * count;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}else{//zero Discount Promo - 20120719
							rearDiscountAmount = zeroDiscountPromoAmount;
							discountAmount += zeroDiscountPromoAmount * count;
							rearDiscountAmountTotal += zeroDiscountPromoAmount * count;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}
						//System.out.println("frontDiscountAmount________________________"+frontDiscountAmount+" strArticle: "+strArticle+" store.getStoreNumber():"+store.getStoreNumber()
						//        +" count:"+count+" promo.getPromoId(): "+promo.getPromoId());
						//break; //only one promo allowed
					} else {
						if (!pid_list.contains(promoId)) {
							rebateOffers.add(promo);
							pid_list.add(promoId);
						}
					}
				}
			}
		}

		m.put("rebateOffers", rebateOffers);
		m.put("discountOffers", discountOffers);
		m.put("discountAmount", discountAmount);
		m.put("frontDiscountAmount", frontDiscountAmount);
		m.put("frontDiscountAmountTotal", frontDiscountAmountTotal);
		m.put("rearDiscountAmount", rearDiscountAmount);
		m.put("rearDiscountAmountTotal", rearDiscountAmountTotal);
		return m;
	}
	
	//--- discount calculation using different front, rear quantity ---//
	public static Map getOfferData(HttpServletRequest request,String article, String rearArticle, Store store, String qty, String rearQty, java.util.Map offersData) {
		locateBeans(request);
		Map m = new HashMap();
		if(offersData == null)
		    offersData = tirePromotionDAO.loadTirePromotionData(thisconfig.getSiteName(), store.getStoreNumber().longValue());
		TirePromotionEvent promo = null;
		List rebateOffers = new ArrayList();
		List discountOffers = new ArrayList();
		List pid_list = new ArrayList();
		int count = Integer.parseInt(qty);
		String[] promoIds = null;
		String promoId = null;
		double discountAmount = 0.0d;
		double frontDiscountAmount = 0.0d;
		double frontDiscountAmountTotal = 0.0d;
		double rearDiscountAmount = 0.0d;
		double rearDiscountAmountTotal = 0.0d;
		if (offersData.containsKey(article)) {
			String ps = (String) offersData.get(article);
			//System.out.println("________________________________"+ps);
			promoIds = ps.split("\\|");
			for (int i = 0; i < promoIds.length; i++) {
				promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
				promoId = String.valueOf(promo.getPromoId());
				//System.out.println(promoIds[i]+"________________________________"+promo);
				if (('P' == promo.getSourcePromotionType().getPromoType()) || ('D' == promo.getSourcePromotionType().getPromoType())) {
					double this_frontDiscountAmount = 0;
					try {
						this_frontDiscountAmount = tirePromotionDAO.getDiscountAmountByStoreNumberMinQtyPromoId(
								Long.parseLong(article),
								store.getStoreNumber().longValue(),
								(byte) count,
								promo.getPromoId());
					} catch (Exception ex) {
					}

					if (this_frontDiscountAmount > 0.0) {
						frontDiscountAmount = this_frontDiscountAmount;
						discountAmount += this_frontDiscountAmount * count;
						frontDiscountAmountTotal += this_frontDiscountAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					}else{//zero Discount Promo - 20120719
						frontDiscountAmount = zeroDiscountPromoAmount;
						discountAmount += zeroDiscountPromoAmount * count;
						frontDiscountAmountTotal += zeroDiscountPromoAmount * count;
						if (!pid_list.contains(promoId)) {
							discountOffers.add(promo);
							pid_list.add(promoId);
						}
					}
					//System.out.println("________________________________1");
					//break; //only one promo allowed
				} else {
					if (!pid_list.contains(promoId)) {
						rebateOffers.add(promo);
						pid_list.add(promoId);
					}
					//System.out.println("________________________________2");
				}
			}
		}

		if (rearArticle != null) {
			int rearCount = Integer.parseInt(rearQty);
			if (offersData.containsKey(rearArticle)) {
				String ps = (String) offersData.get(rearArticle);
				//System.out.println("________________________________"+ps);
				promoIds = ps.split("\\|");
				for (int i = 0; i < promoIds.length; i++) {
					promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
					promoId = String.valueOf(promo.getPromoId());
					//System.out.println(promoIds[i]+"________________________________"+promo);
					if (('P' == promo.getSourcePromotionType().getPromoType()) || ('D' == promo.getSourcePromotionType().getPromoType())) {
						double this_rearDiscountAmount = 0;
						try {
							this_rearDiscountAmount = tirePromotionDAO.getDiscountAmountByStoreNumberMinQtyPromoId(
									Long.parseLong(rearArticle),
									store.getStoreNumber().longValue(),
									(byte) rearCount,
									promo.getPromoId());
						} catch (Exception ex) {
						}
						if (this_rearDiscountAmount > 0.0) {
							rearDiscountAmount = this_rearDiscountAmount;							
							rearDiscountAmountTotal += this_rearDiscountAmount * rearCount;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}else{//zero Discount Promo - 20120719
							rearDiscountAmount = zeroDiscountPromoAmount;							
							rearDiscountAmountTotal += zeroDiscountPromoAmount * rearCount;
							if (!pid_list.contains(promoId)) {
								discountOffers.add(promo);
								pid_list.add(promoId);
							}
						}
						//System.out.println("frontDiscountAmount________________________"+frontDiscountAmount+" strArticle: "+strArticle+" store.getStoreNumber():"+store.getStoreNumber()
						//        +" count:"+count+" promo.getPromoId(): "+promo.getPromoId());
						//break; //only one promo allowed
					} else {
						if (!pid_list.contains(promoId)) {
							rebateOffers.add(promo);
							pid_list.add(promoId);
						}
					}
				}
			}
		}

		m.put("rebateOffers", rebateOffers);
		m.put("discountOffers", discountOffers);
		m.put("discountAmount", discountAmount);
		m.put("frontDiscountAmount", frontDiscountAmount);
		m.put("frontDiscountAmountTotal", frontDiscountAmountTotal);
		m.put("rearDiscountAmount", rearDiscountAmount);
		m.put("rearDiscountAmountTotal", rearDiscountAmountTotal);
		return m;
	}
	//--- get Tire Promotion Discount Amount, if any  ---//
	public static double getDiscountAmount(java.util.Map offersData, TirePromotionDAO tirePromotionDAO, Store store,  Tire tire, int qty) {
		String[] promoIds = null;
		String promoId = null;
		double discountAmount = 0.0;
		String article = tire.getArticle()+"";

		if (offersData.containsKey(article)) {
			String ps = (String) offersData.get(article);
			promoIds = ps.split("\\|");
			for (int i = 0; i < promoIds.length; i++) {
				TirePromotionEvent promo = (TirePromotionEvent) offersData.get("PROMO_" + promoIds[i]);
				promoId = String.valueOf(promo.getPromoId());
				if (('P' == promo.getSourcePromotionType().getPromoType()) || ('D' == promo.getSourcePromotionType().getPromoType())) {
					double this_frontDiscountAmount = 0;
					try {
						this_frontDiscountAmount = tirePromotionDAO.getDiscountAmountByStoreNumberMinQtyPromoId(
							Long.parseLong(article),
							store.getStoreNumber().longValue(),
							(byte) qty,
							promo.getPromoId());
					} catch (Exception ex) {}
					if (this_frontDiscountAmount > 0.0) {
						discountAmount += this_frontDiscountAmount * qty;
					}else{//zero Discount Promo - 20120719
						discountAmount  += zeroDiscountPromoAmount*qty;
					}
				}
			}
		}
		return discountAmount;
	}
	
	public static void loadDiscountData(HttpServletRequest request,TireSearchResults results, List tires, Store store, String qty){
		locateBeans(request);
		java.util.Map mappedTires = results.getMappedTires();
		int quantity = 4;
		try{
		    quantity = Integer.parseInt(qty);
		}catch(Exception ex){
			quantity = 4;
		}
		java.util.Map offersData = tirePromotionDAO.loadTirePromotionData(thisconfig.getSiteName(), Long.parseLong(store.getStoreNumber()+""));
		for(int i=0; tires!=null && i< tires.size(); i++) {
	        Tire tire = (Tire)tires.get(i);
	        tire.setQuantity(quantity);
	        Tire rearTire = null;
			String rearArticle = tire.getRearArticle() > 0 ? String.valueOf(tire.getRearArticle()) : null;
			if(!StringUtils.isNullOrEmpty(rearArticle)){
			    rearTire = (Tire)mappedTires.get(rearArticle);
			    rearTire.setQuantity(quantity);
			}
		    boolean hasSpecialOffer = false;
			if(offersData.containsKey(tire.getArticle()+"") || (rearTire != null && offersData.containsKey(rearTire.getArticle()+""))){
		            hasSpecialOffer = true;

            }
            double retailPrice = tire.getRetailPrice();
			if(hasSpecialOffer) {
				double discount = getDiscountAmount(offersData, tirePromotionDAO, store, tire, quantity);
                if(discount > 0){
                    tire.setSalePrice(retailPrice - discount/(quantity));
                }
				if(rearTire!=null){
					discount = TirePricingUtils.getDiscountAmount(offersData, tirePromotionDAO, store, rearTire, quantity);
					if(discount > 0){
						retailPrice = rearTire.getRetailPrice();
	                    rearTire.setSalePrice(retailPrice - discount/(quantity));
	                }
				}
		
			}
		}
	}
}