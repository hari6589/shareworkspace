package com.bfrc.pojo.tire;


import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.bfrc.*;
import com.bfrc.pojo.store.Store;
import com.bfrc.framework.dao.StoreDAO;
import com.bfrc.framework.dao.VehicleDAO;
import com.bfrc.framework.dao.pricing.PricingLocatorOperator;
import com.bfrc.framework.util.*;
import com.bsro.service.store.StoreService;
public class TireSearchResults  implements java.io.Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 5289640532615572426L;
	// Constructors

	private StoreDAO storeDAO;
	private StoreService storeService;
	private HttpServletRequest request;
	private Config config;
	private PricingLocatorOperator pricingLocator;
	private VehicleDAO vehicleDAO;
	private HttpSession session;
	
	private ServletContext servletContext;
    public void  setServletContext(ServletContext servletContext){
    	this.servletContext = servletContext;
    }
    public ServletContext getServletContext(){
    	return this.servletContext;
    }

	public HttpServletRequest getHttpServletRequest(){
    	return this.request;
    }
    public void  setHttpServletRequest(HttpServletRequest httpServletRequest){
    	this.request = httpServletRequest;
    }
    
	private void locateBeans(){
		ServletContext ctx = null;
		if(servletContext != null)
			ctx = servletContext;
		else	
		    ctx = request.getSession().getServletContext();
		if(storeDAO == null)
		    storeDAO = (StoreDAO)Config.locate(ctx, "storeDAO");
		if(storeService == null)
			storeService = (StoreService)Config.locate(ctx, "storeSearchService");
		if(config == null)
			config = (Config)Config.locate(ctx, "config");
		if(vehicleDAO == null)
			vehicleDAO = (VehicleDAO)Config.locate(ctx, "vehicleDAO");
		if(pricingLocator == null)
			pricingLocator = (PricingLocatorOperator)Config.locate(ctx, "pricingLocator");
		if(session == null)
			session = request.getSession();

	}
	
    /** default constructor */
	public TireSearchResults() {
    }
    
	
    public TireSearchResults(HttpServletRequest httpServletRequest) {
    	request = httpServletRequest;
    }
    
    List<Tire> tires;
    public List<Tire> getTires(){
    	return this.tires;
    }
    public void  setTires(List<Tire> tires){
    	this.tires = tires;
    }
    
    List<Fitment> fitments;
    
    public List<Fitment> getFitments(){
    	if(fitments == null)
    	  return this.userSessionData.getFitments();
    	else 
    	 return fitments;
    }
    public void setFitments(List<Fitment> fitments){
    	this.fitments = fitments;
    }
    
    UserSessionData  userSessionData;
    public UserSessionData getUserSessionData(){
    	return this.userSessionData;
    }
    public void  setUserSessionData(UserSessionData userSessionData){
    	this.userSessionData = userSessionData;
    }
    
    List<Store> stores;
    public List<Store> getStores(){
    	return this.stores;
    }
    public void  setStores(List<Store> stores){
    	this.stores = stores;
    }
    
    public Store getStore(String storeNumber){
    	if(stores != null){
    		for(int i=0; i< stores.size(); i++){
    			Store store = (Store)stores.get(i);
    			if(String.valueOf(store.getNumber()).equals(storeNumber)){
    				return store;
    			}
    		}
    	}
    	return null;
    }
    public Map<String, Store> getMappedStores(){
    	Map<String, Store> m = new LinkedHashMap<String, Store>();
    	if(stores != null){
	    	for(Store store : stores){
	    		m.put(String.valueOf(store.getNumber()), store);
	    	}
    	}
    	return m;
    }
    
    public void applyPrimewellFilter() {
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){    		
	    	for(Tire tire : tires){
	    		if ("Primewell".equalsIgnoreCase(tire.getBrand())) {
	    			l.add(tire);
	    		}
	    	}
    	}
    	setTires(l);
    }
    
    public void applyMobileTireInstallFilterOnSupportedBrands(boolean mobileTireInstallPilotStore) {
    	if (mobileTireInstallPilotStore) {
	    	List<Tire> l = new ArrayList<Tire>();
	    	if(tires != null){    		
		    	for(Tire tire : tires){
		    		if ("Bridgestone".equalsIgnoreCase(tire.getBrand()) || "Firestone".equalsIgnoreCase(tire.getBrand())) {
		    			l.add(tire);
		    		}
		    	}
	    	}
	    	setTires(l);
    	}
    }
    
    public Map<String, Tire> getMappedTires(){
    	Map<String, Tire> m = new LinkedHashMap<String, Tire>();
    	if(tires != null){
	    	for(Tire tire : tires){
	    		m.put(String.valueOf(tire.getArticle()), tire);
	    	}
    	}
    	return m;
    }
    public List<Tire> getFrontTires(){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    	    StringBuffer sb = new StringBuffer("|");
	    	for(Tire tire : tires){
	    		if("F".equalsIgnoreCase(tire.getFrontRearBoth()) ||StringUtils.isNullOrEmpty(tire.getFrontRearBoth())){
	    			l.add(tire);
	    			sb.append(tire.getArticle()+"|");
	    		}
	    	}
	    	userSessionData.setFrontArticles(sb.toString());
    	}
    	return l;
    }
    public List<Tire> getStandardSizeTires(){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    		StringBuffer sb = new StringBuffer("|");
	    	for(Tire tire : tires){
	    		if("Standard".equalsIgnoreCase(tire.getStandardOptionalDisplay())){
	    			l.add(tire);
	    			sb.append(tire.getArticle()+"|");
	    		}
	    	}
	    	userSessionData.setStandardSizeArticles(sb.toString());
    	}
    	return l;
    }
    
    public List<Tire> getOptionalSizeTires(){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    		StringBuffer sb = new StringBuffer("|");
	    	for(Tire tire : tires){
	    		if("Optional".equalsIgnoreCase(tire.getStandardOptionalDisplay())){
	    			l.add(tire);
	    			sb.append(tire.getArticle()+"|");
	    		}
	    	}
	    	userSessionData.setOptionalSizeArticles(sb.toString());
    	}
    	return l;
    }
    
    public boolean hasOptionalSizeTires(){
    	return getOptionalSizeTires().size() > 0;
    }
    
    public boolean isMatchedSet(){
    	return (getFrontTires().size() > 0 && getRearTires().size() > 0);
    }
    public List<Tire> getRearTires(){
    	List<Tire> l = new ArrayList<Tire>();
    	if(tires != null){
    	    StringBuffer sb = new StringBuffer("|");
	    	for(Tire tire : tires){
	    		if("R".equalsIgnoreCase(tire.getFrontRearBoth())){
	    			l.add(tire);
	    			sb.append(tire.getArticle()+"|");
	    		}
	    	}
	    	userSessionData.setRearArticles(sb.toString());
    	}
    	return l;
    }
    
    public Map<String, List<Tire>> getGroupedFrontTires(){
    	Map<String, List<Tire>> m = new LinkedHashMap<String, List<Tire>>();
    	List<Tire> items = getFrontTires();
    	if(items != null && items.size() > 0){
	    	for(Tire tire : items){
	    		String strGroupId = String.valueOf(tire.getTiregroupId());
	    		if(m.get(strGroupId) == null){
	    			m.put(strGroupId, (new ArrayList<Tire>()));
	    		}
	    		((List<Tire>)m.get(strGroupId)).add(tire);
	    	}
    	}
    	return m;
    }
   
    @SuppressWarnings("unchecked")
	public Map<String, List> getGroupedRearTires(){
    	Map<String, List> m = new LinkedHashMap<String, List>();
    	List<Tire> items = getRearTires();
    	if(items != null && items.size() > 0){
	    	for(Tire tire : items){
	    		String strGroupId = String.valueOf(tire.getTiregroupId());
	    		if(m.get(strGroupId) == null){
	    			m.put(strGroupId, (new ArrayList()));
	    		}
	    		((List)m.get(strGroupId)).add(tire);
	    	}
    	}
    	return m;
    }
    @SuppressWarnings("unchecked")
    public Map<String, List> getGroupedTires(){
    	Map<String, List> m = new LinkedHashMap<String, List>();
    	if(tires != null){
	    	for(Tire tire : tires){
	    		String strGroupId = String.valueOf(tire.getTiregroupId());
	    		if(m.get(strGroupId) == null){
	    			m.put(strGroupId, (new ArrayList()));
	    		}
	    		((List)m.get(strGroupId)).add(tire);
	    	}
    	}
    	return m;
    }
    //-- Get Matched Sets --//
    public List<Tire> getMatchedSetTires(){
    	List<Tire> l = new ArrayList<Tire>();
    	try{
    	if(tires != null){
    		if(userSessionData.getFitments() != null && userSessionData.getFitments().size() > 0){
    			String articles = userSessionData.getArticles();
	    		List<Tire> frontTires = getFrontTires();
	    		List<Tire> rearTires = getRearTires();
	    		boolean ok = (frontTires != null && frontTires.size() > 0 
	    				&&    rearTires != null && rearTires.size() > 0);
	    		Fitment frontFitment = userSessionData.getFrontFitment();
	    		Fitment rearFitment = userSessionData.getRearFitment();
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
	    		userSessionData.setMatchedArticles(sb.toString());
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
    
    //-- contain errors if any --//
    private String errorCode;
    public String getErrorCode(){
        return this.errorCode;
    }
    public void setEerrorCode(String errorCode){
        this.errorCode = errorCode;
    }
    
    //-- has error --//
    public boolean hasError(){
        return !StringUtils.isNullOrEmpty(errorCode);
    }
    
    //-- has products --//
    public boolean hasReults(){
        return (tires != null && tires.size() > 0);
    }
    
    
  //-- has products --//
    public boolean hasResults(){
        return (tires != null && tires.size() > 0);
    }
    
    public void refreshUserSessionData(){
       getMatchedSetTires();
       getFrontTires();
       getRearTires();
       getOptionalSizeTires();
       getStandardSizeTires();
    }
    
    public void populateFromUserSessionData(){
    	//if(request == null && userSessionData.getHttpServletRequest() != null)
    	//	request = userSessionData.getHttpServletRequest();
    	locateBeans();
    	String zip = userSessionData.getZip();
        String locatorStoreNumber = userSessionData.getStoreNumber();	
		
        //--- STEP 1 get Stores ---//
        if(ServerUtil.isNullOrEmpty(zip)){
			setEerrorCode("global.error.zip");
			return;
		} else { //find store by address, even it's a zip code
			List<Store> st = storeService.findStoresLightByStoreNumbers(userSessionData.getStoreNumbers());
			if(st!=null && st.size()>0) {
				setStores(st);
			} else {
				setEerrorCode("global.error.nostore");
				return;
			}
		}
		//-- STEP2 get Products--//
		//Vehicle vehicle = userSessionData.getVehicle();
		String method = userSessionData.getUserRequest();
		if(StringUtils.isNullOrEmpty(method))
            method = TireSearchUtils.SEARCH_BY_VEHICLE;
		Store store = getMappedStores().get(userSessionData.getStoreNumber());
		if(store == null){
			try{
			    store = storeDAO.findStoreById(new Long(locatorStoreNumber));
			}catch(Exception ex){
				
			}
			if(store == null && getStores() != null){
				if(getStores().size() > 0){
					store = (Store) getStores().get(0);
				}
			}
		}
    	List<Tire> tires = vehicleDAO.populateTiresByArticles(config.getSiteName(), userSessionData.getArticles(),userSessionData.getStoreNumber(),userSessionData.getRegularStoreNumber());
    	setTires(tires);
    	//-- Populate Front, Rear and Matched Set Tires --//
		if(tires != null){
			String rearArticles = userSessionData.getRearArticles();
			String frontArticles = userSessionData.getFrontArticles();
			for(int i=0; i<tires.size(); i++){
				Tire tire = (Tire)tires.get(i);
				if(rearArticles != null && rearArticles.indexOf("|"+tire.getArticle()+"|") >=0){
					tire.setFrontRearBoth("R");
				}else if(frontArticles != null && frontArticles.indexOf("|"+tire.getArticle()+"|") >=0){
					tire.setFrontRearBoth("F");
				}else{
					tire.setFrontRearBoth("B");
				}
			}
		}
    	//-- Populate Std and Opt Tires --//
		if(userSessionData.getOptionalSizeArticles() != null && tires != null){
			String optArticles = userSessionData.getOptionalSizeArticles();
			for(int i=0; i<tires.size(); i++){
				Tire tire = (Tire)tires.get(i);
				if(optArticles.indexOf("|"+tire.getArticle()+"|") >=0){
					tire.setStandardOptional("O");
				}else{
					tire.setStandardOptional("S");
				}
			}
		}
		setFitments(userSessionData.getFitments());
		refreshUserSessionData(); 
    }
}