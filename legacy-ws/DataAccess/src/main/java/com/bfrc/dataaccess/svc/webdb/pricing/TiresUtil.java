package com.bfrc.dataaccess.svc.webdb.pricing;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.model.inventory.StoreInventory;
import com.bfrc.dataaccess.svc.vehicle.TireVehicleService;
import com.bfrc.dataaccess.util.Sites;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.util.ServerUtil;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.tire.Fitment;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.jda2.TireGrouping;

/**
 * @author smoorthy
 *
 */

@Service
public class TiresUtil {
	
	@Autowired
	private JDA2CatalogDAO jda2CatalogDAO;
	
	@Autowired
	private TireVehicleService tireVehicleService;
	
	@Autowired
	CatalogDAO catalogDAO;
	
	@SuppressWarnings("rawtypes")
	public Map getMappedBestInClassTires() {
		List bestInClassTires = catalogDAO.getBestInClassTires();
		return TireCatalogUtils.getMappedBestInClassTires(bestInClassTires);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map getMappedTireGrouping(List groupings){
	       Map m = new HashMap();
	       for(int i=0; i< groupings.size();i++){
	         TireGrouping tg = (TireGrouping)groupings.get(i);
	         m.put(tg.getDisplayId(), tg);
	       }
	       return m;
	}
	
	public Map<String, Tire> getMappedTires(List<Tire> tires){
    	Map<String, Tire> m = new LinkedHashMap<String, Tire>();
    	if(tires != null){
	    	for(Tire tire : tires){
	    		m.put(String.valueOf(tire.getArticle()), tire);
	    	}
    	}
    	return m;
    }
	
	@SuppressWarnings("rawtypes")
	public String getTireType(Tire tire) {
		String tireType = null;
		
		List groupings = jda2CatalogDAO.getAllTireGrouping();
		java.util.Map mappedGroupings = getMappedTireGrouping(groupings);
		TireGrouping tg = (TireGrouping)mappedGroupings.get(tire.getDisplayId());
        tireType = tg == null ? tire.getTireGroupName() : tg.getTireVehicle();
        
		return tireType;
	}
	
	public String getTireBrandName(Tire tire) {
		String sb = TireCatalogUtils.getSubBrandImage(tire.getTireName(),false);
		if (sb == null) {
			sb = tire.getBrand();
		}
		return sb;
	}
	
	public String getTireBrandImage(Tire tire) {
		String brandImage = "fcac-logo-"+tire.getBrand().trim()+".png";
        String sb = TireCatalogUtils.getSubBrandImage(tire.getTireName().trim(),false);
        if(sb != null)
        	brandImage = sb+"_logo.png";
        
		return brandImage;
	}
	
	public String getTireImage(Tire tire) {
		String fdTirename = StringUtils.nameFilter(tire.getTireName().trim());
		String img60 = fdTirename + ".png";
		
		return img60;
	}
	
	public StoreInventory getStoreInventory(Long storeNumber, Long tireArticleNo) {
		Collection<StoreInventory> storeInventories = tireVehicleService.getInventoryByStore(storeNumber);
		if (storeInventories != null && !storeInventories.isEmpty()) {
			for(StoreInventory storeInventory : storeInventories){
				if (storeInventory.getArticleNumber() != null && storeInventory.getArticleNumber().equals(tireArticleNo)) {
					return storeInventory;
				}					
			}
		}
		
		return null;
	}
	
	public StoreInventory getStoreInventoryByArticle(Long storeNumber, Long tireArticleNo) {
		List<StoreInventory> storeInventories = null;
		storeInventories = (ArrayList<StoreInventory>) tireVehicleService.getInventoryByStoreAndArticle(storeNumber, tireArticleNo);
		if (storeInventories != null && !storeInventories.isEmpty()) {
			return storeInventories.get(0);
		}
		
		return null;
	}
	
	public Fitment getFrontFitment(List<Fitment> fitments){
    	if(fitments != null){
    		for(Fitment fitment : fitments){
    			if('B' == fitment.getFrb() || 'F' == fitment.getFrb()){
    				return fitment;
    			}
    		}
    	}
    	return null;
    }
    
    public Fitment getRearFitment(List<Fitment> fitments){
    	if(fitments != null){
    		for(Fitment fitment : fitments){
    			if('R' == fitment.getFrb()){
    				return fitment;
    			}
    		}
    	}
    	return null;
    }
	
	public List<Tire> getStandardSizeTires(List<Tire> tires){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    		for(Tire tire : tires){
	    		if("Standard".equalsIgnoreCase(tire.getStandardOptionalDisplay())){
	    			l.add(tire);
	    		}
	    	}
    	}
    	return l;
    }
    
    public List<Tire> getOptionalSizeTires(List<Tire> tires){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    		for(Tire tire : tires){
	    		if("Optional".equalsIgnoreCase(tire.getStandardOptionalDisplay())){
	    			l.add(tire);
	    		}
	    	}
    	}
    	return l;
    }
	
	public List<Tire> getFrontTires(List<Tire> tires){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    	    for(Tire tire : tires){
	    		if("F".equalsIgnoreCase(tire.getFrontRearBoth()) ||StringUtils.isNullOrEmpty(tire.getFrontRearBoth())){
	    			l.add(tire);
	    		}
	    	}
    	}
    	return l;
    }
	
	public List<Tire> getRearTires(List<Tire> tires){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    	    for(Tire tire : tires){
	    		if("R".equalsIgnoreCase(tire.getFrontRearBoth())){
	    			l.add(tire);
	    		}
	    	}
    	}
    	return l;
    }
	
	public boolean hasOptionalSizeTires(List<Tire> tires){
    	return getOptionalSizeTires(tires).size() > 0;
    }
	
	public boolean hasStandardSizeTires(List<Tire> tires){
    	return getStandardSizeTires(tires).size() > 0;
    }
	
	public boolean isMatchedSet(List<Tire> tires){
    	return (getFrontTires(tires).size() > 0 && getRearTires(tires).size() > 0);
    }
	
	public List<Tire> getMatchedSetTires(List<Tire> tires, List<Fitment> fitments, String articles){
    	List<Tire> l = new ArrayList<Tire>();
    	try{
    	if(tires != null){
    		if(fitments != null && fitments.size() > 0){
	    		List<Tire> frontTires = getFrontTires(tires);
	    		List<Tire> rearTires = getRearTires(tires);
	    		boolean ok = (frontTires != null && frontTires.size() > 0 
	    				&&    rearTires != null && rearTires.size() > 0);
	    		Fitment frontFitment = getFrontFitment(fitments);
	    		Fitment rearFitment = getRearFitment(fitments);
	    		ok = (ok && frontFitment != null && rearFitment != null);
	    		StringBuffer sb = new StringBuffer("|");
	    		if(ok){
	    			//-- TIRE SELECTOR RULES and GENERAL CATALOG PAGE UPDATES by cs 20100527 --//
	    			/*********
	    			 If the front tires have SL and the rear tires have XL, then the following combinations options are OK:
	    			a)      Front = SL, rear = XL
	    			b)      Front = XL, rear = XL
	    			
	    			
	    			If the front is XL and the rear is SL, the following combinations are OK:
	    			a)      Front = XL, rear = SL
	    			b)      Front = XL, rear = XL
	    			
	    			If the front and rear are both SL, the following combinations are OK:
	    			a)      Front = SL, rear = SL
	    			
	    			b)      Front = XL, rear = XL
	    			
	    			If front and rear are both XL, only the following is allowed:
	    			a) Front = XL, rear = XL
	    			 *********/
	    			String vehicleFrontLoadRange = Util.getVehicleLoadRange(frontFitment.getLoadRange());
	    			String vehicleRearLoadRange = Util.getVehicleLoadRange(rearFitment.getLoadRange());
	    			
	    			for(Tire frontTire : frontTires){
	    				long frontDisplayId = frontTire.getDisplayId();
	    				for(Tire rearTire : rearTires){
	    					long rearDisplayId = rearTire.getDisplayId();
	    					if(frontDisplayId == rearDisplayId){
	    						long frontArticle = frontTire.getArticle();
	    						long rearArticle  = rearTire.getArticle();
	    						String frontRange = Util.getVehicleLoadRange(frontTire.getLoadRange());
	    						String rearRange = Util.getVehicleLoadRange(rearTire.getLoadRange());
	    						String frontSpeedRating = frontTire.getSpeedRating();
	 						    String rearSpeedRating  = rearTire.getSpeedRating();
		 						   if("SL".equalsIgnoreCase(vehicleFrontLoadRange) && "XL".equalsIgnoreCase(vehicleRearLoadRange)){
								       if(("SL".equalsIgnoreCase(frontRange) && "XL".equalsIgnoreCase(rearRange))
								    	|| ("XL".equalsIgnoreCase(frontRange) && "XL".equalsIgnoreCase(rearRange))	   
								    	){
								    	   if(frontSpeedRating != null && frontSpeedRating.equals(rearSpeedRating)){
								    		   Tire frontTireC = (Tire)frontTire.clone();
								    		   frontTireC.setRearArticle(rearArticle);
								    		   frontTireC.setRearTire(rearTire);
								    		   l.add(frontTireC);
								    		   if(articles.indexOf("|"+frontArticle + "_" + rearArticle+"|") < 0){
								    			   sb.append(frontArticle + "_" + rearArticle+"|");
								    		   }
								    		   com.bfrc.framework.util.ServerUtil.debug("t\t frontRange:    "+frontRange+"    rearRange: "+rearRange);
								    		   com.bfrc.framework.util.ServerUtil.debug(frontArticle + "_" + rearArticle);
								    	   }
								       }
									   
								   }else if("XL".equalsIgnoreCase(vehicleFrontLoadRange) && "SL".equalsIgnoreCase(vehicleRearLoadRange)){
									   if(("XL".equalsIgnoreCase(frontRange) && "SL".equalsIgnoreCase(rearRange))
										|| ("XL".equalsIgnoreCase(frontRange) && "XL".equalsIgnoreCase(rearRange))	   
								    	){
								    	   if(frontSpeedRating != null && frontSpeedRating.equals(rearSpeedRating)){
								    		   Tire frontTireC = (Tire)frontTire.clone();
								    		   frontTireC.setRearArticle(rearArticle);
								    		   frontTireC.setRearTire(rearTire);
								    		   l.add(frontTireC);
								    		   if(articles.indexOf("|"+frontArticle + "_" + rearArticle+"|") < 0){
								    			   sb.append(frontArticle + "_" + rearArticle+"|");
								    		   }
								    		   com.bfrc.framework.util.ServerUtil.debug("t\t frontRange:    "+frontRange+"    rearRange: "+rearRange);
								    		   com.bfrc.framework.util.ServerUtil.debug(frontArticle + "_" + rearArticle);
								    	   }
								       }
									   
								   }else if("SL".equalsIgnoreCase(vehicleFrontLoadRange) && "SL".equalsIgnoreCase(vehicleRearLoadRange)){
									   if(("SL".equalsIgnoreCase(frontRange) && "SL".equalsIgnoreCase(rearRange))
												|| ("XL".equalsIgnoreCase(frontRange) && "XL".equalsIgnoreCase(rearRange))	   
										    	){
										    	   if(frontSpeedRating != null && frontSpeedRating.equals(rearSpeedRating)){
										    		   Tire frontTireC = (Tire)frontTire.clone();
										    		   frontTireC.setRearArticle(rearArticle);
										    		   frontTireC.setRearTire(rearTire);
										    		   l.add(frontTireC);
										    		   if(articles.indexOf("|"+frontArticle + "_" + rearArticle+"|") < 0){
										    			   sb.append(frontArticle + "_" + rearArticle+"|");
										    		   }
										    		   com.bfrc.framework.util.ServerUtil.debug("t\t frontRange:    "+frontRange+"    rearRange: "+rearRange);
										    		   com.bfrc.framework.util.ServerUtil.debug(frontArticle + "_" + rearArticle);
										    	   }
										       }
											   
								  }else if("XL".equalsIgnoreCase(vehicleFrontLoadRange) && "XL".equalsIgnoreCase(vehicleRearLoadRange)){
									   if(("XL".equalsIgnoreCase(frontRange) && "XL".equalsIgnoreCase(rearRange))
								    	){
								    	   if(frontSpeedRating != null && frontSpeedRating.equals(rearSpeedRating)){
								    		   Tire frontTireC = (Tire)frontTire.clone();
								    		   frontTireC.setRearArticle(rearArticle);
								    		   frontTireC.setRearTire(rearTire);
								    		   l.add(frontTireC);
								    		   if(articles.indexOf("|"+frontArticle + "_" + rearArticle+"|") < 0){
								    			   sb.append(frontArticle + "_" + rearArticle+"|");
								    		   }
								    		   ServerUtil.debug("t\t frontRange:    "+frontRange+"    rearRange: "+rearRange);
								    		   ServerUtil.debug(frontArticle + "_" + rearArticle);
								    	   }
								       }
											   
								  }else{
									   Tire frontTireC = (Tire)frontTire.clone();
						    		   frontTireC.setRearArticle(rearArticle);
						    		   frontTireC.setRearTire(rearTire);
						    		   l.add(frontTireC);
						    		   if(articles.indexOf("|"+frontArticle + "_" + rearArticle+"|") < 0){
						    			   sb.append(frontArticle + "_" + rearArticle+"|");
						    		   }
								       ServerUtil.debug(frontArticle + "_" + rearArticle);
								  }
	    					}
	    				}
	    			}
	    		}
    		}else{
    			// --- maybe already populated ---//
				for (Tire tire : tires) {
					if(tire.getRearArticle() > 0){
						l.add(tire);
					}
				}
    		}
    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    		//ignore the error
    	}
    	return l;
    }

}
