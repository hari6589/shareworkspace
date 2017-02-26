package com.bfrc.dataaccess.svc.catalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.model.catalog.CatalogBean;
import com.bfrc.dataaccess.model.catalog.Product;
import com.bfrc.dataaccess.util.Sites;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.dao.SurveyDAO;
import com.bfrc.framework.dao.store.LocatorOperator;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.framework.util.TireSearchUtils;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda.Display;
import com.bfrc.pojo.tire.jda2.Brand;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bsro.databean.tirecatalog.TireCatalogDataBean;
import com.bsro.databean.tirecatalog.TireDetailDataBean;
import com.bsro.service.tirecatalog.TireCatalogService;

import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

/**
 * @author smoorthy
 *
 */

@Service
public class CatalogServiceImpl implements CatalogService {
	
	@Autowired
	LocatorOperator locator;
	
	@Autowired
	TireCatalogService tireCatalogService;
	
	@Autowired
	private JDA2CatalogDAO jda2CatalogDAO;
	
	@Autowired
	CatalogDAO catalogDAO;
	
	@Autowired
	SurveyDAO surveyDAO;
	
	@SuppressWarnings("rawtypes")
	public BSROWebServiceResponse getTireProductDetails(String siteName, String brand, String tireName, String displayId) {
		String imgUri=Sites.getWebSiteTireImageDir(siteName);
        String tireImgDir = Sites.getWebSiteAppRoot(siteName)+imgUri;
        BSROWebServiceResponse response = new BSROWebServiceResponse();
        
        if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
        
        /*
         * 		Get the product details
         * */
        TireDetailDataBean dataBean = tireCatalogService.getTireDetailData(brand, tireName, displayId, tireImgDir, imgUri);
        List sizes = dataBean.getSizes();
        Display tire = dataBean.getTire();
        if(tire == null || (sizes == null) || sizes.isEmpty()) {
        	response.setMessage("Sorry, there is no information available on the requested product.");
			response.setStatusCode(BSROWebServiceResponseCode.VALIDATION_ERROR.name());
			
			return response;
        }
        
        double[] prices = dataBean.getPrices();
        TireGrouping tireGrouping = dataBean.getTireGrouping();
        Map temps = dataBean.getTemps();
        Map treads = dataBean.getTreads();
        Map tracts = dataBean.getTracts();
        Map specs = dataBean.getSpecs();
        Set features = dataBean.getFeatures();
        
        Map<String, String> display = new HashMap<String, String>();
        String fdTirename = StringUtils.nameFilter(tire.getValue().trim());
        String brandImage = "fcac-logo-"+tire.getBrand().getValue().trim()+".png";
        String sb = TireCatalogUtils.getSubBrandImage(tire.getValue().trim(),false);
        if(sb != null)
        	brandImage = sb+"_logo.png";
        
        display.put("tireId", String.valueOf(tire.getId()));
        display.put("tireName", tire.getValue());
        display.put("tireDesc", tire.getDescription());
        display.put("model", tire.getValue());
        display.put("brand", tire.getBrand().getValue());
        display.put("category", tire.getCategory().getValue());
        display.put("theClass", tire.getTheClass().getValue());
        display.put("tireBrandImage", brandImage);
        display.put("tireImage", fdTirename+".png");
        
        if (tireGrouping == null) {
        	display.put("vehType", tire.getCategory().getValue().replaceAll("Tires",""));
        	display.put("tireType", tire.getTiregroup().getValue());
        	display.put("segment", tire.getTheClass().getValue());
        } else {
        	display.put("vehType", tireGrouping.getTireVehicle());
        	display.put("tireType", tireGrouping.getTireType());
        	display.put("segment", tireGrouping.getSegment());
        }
        
        /*
         * 		Populate the response data
         * */
        Product product = new Product();
        product.setDisplay(display);
        product.setRatings(TireSearchUtils.getMappedSpeedRatingAndSpeedValue());
        product.setSpecs(specs.keySet());
        product.setTemps(temps.keySet());
        product.setTreads(treads.keySet());
        product.setTracts(tracts.keySet());
        product.setSpeeds(dataBean.getSpeeds());
        if(features != null && features.size() > 0)
        	product.setFeatures(features);        
        if (prices != null && prices.length > 0) {
        	String[] strPrices = new String[prices.length];
        	int arrIndex = 0;
        	for (double price : prices) {
        		strPrices[arrIndex++] = String.valueOf(price);
        	}
        	product.setPrices(strPrices);
        }
        product.setBestInClassTire(dataBean.getIsBestInClassTire());
        
        //getImageURLs(tireImgDir, tire.getValue(), product);
        
        response.setPayload(product);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		
		return response;
	}

	@SuppressWarnings({ "rawtypes" })
	public BSROWebServiceResponse getTiresByVehicleType(String vehicleType, String siteName, String sortBy) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		CatalogBean bean = new CatalogBean();
		
		TireCatalogDataBean resultData = getTireCatalogData(vehicleType, siteName, sortBy);
		List groupings = resultData.getTireGroupings();
		Map mappedTypeTires = resultData.getMappedTypeTires();
		Map mappedGroupins = resultData.getMappedTireGroupings();
		Map mappedBrands = resultData.getMappedTireBrands();
		List tires = (List)mappedTypeTires.get(vehicleType);
		
		if (tires != null && !tires.isEmpty()) {
			List<Map<String, String>> displays = new ArrayList<Map<String, String>>();
			for (int i = 0; i < tires.size(); i++) {
				com.bfrc.pojo.tire.jda2.Display tire = (com.bfrc.pojo.tire.jda2.Display) tires.get(i);
				Map<String, String> display = new HashMap<String, String>();
				TireGrouping tireGrouping = (TireGrouping)mappedGroupins.get(tire.getId());
				Map mappedBestInClassTires = resultData.getMappedBestInClassTires();
				Map mappedSurveyData = resultData.getMappedMindshareTiresurveyDetails();
				
				MindshareTiresurveyDetails surveyData = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getId());
				Brand brand = (Brand)mappedBrands.get(tire.getBrandId()+"");
				String tireName = tire.getValue() == null ? "" : tire.getValue().trim();
				String fdTirename = StringUtils.nameFilter(tireName);
				String brandImage = "fcac-logo-"+brand.getValue().trim()+".png";
		        String sb = TireCatalogUtils.getSubBrandImage(tire.getValue().trim(),false);
		        if(sb != null)
		        	brandImage = sb+"_logo.png";
		        
		        display.put("tireId", String.valueOf(tire.getId()));
		        display.put("tireName", tire.getValue());
		        display.put("model", tire.getValue());
		        display.put("brand", brand.getValue());
		        display.put("tireBrandImage", brandImage);
		        display.put("tireImage", fdTirename+".png");
		        display.put("theClass", tireGrouping.getClass_());
		        display.put("generateCatalogPage", ""+tire.getGenerateCatalogPage());
		        display.put("isBestInClassTire", ""+mappedBestInClassTires.containsKey(tire.getId()));
		        if (surveyData != null) {
		        	display.put("dryTraction", ""+surveyData.getDryTraction());
		        	display.put("wetTraction", ""+surveyData.getWetTraction());
		        	display.put("tractionInSnowIce", ""+surveyData.getTractionInSnowIce());
		        	display.put("tireStability", ""+surveyData.getTireStability());
		        	display.put("noiseLevel", ""+surveyData.getNoiseLevel());
		        	display.put("rideComfort", ""+surveyData.getRideComfort());
		        	display.put("tireWear", ""+surveyData.getTireWear());
		        }
		        displays.add(display);
			}
			
			bean.setVehicleTypes(getVehicleTypeNames(resultData.getVehicleTypeNames()));
			bean.setSortList(sortList);
			bean.setFilters(getFilters(vehicleType, groupings));
			if (!displays.isEmpty()) {
				bean.setDisplays(displays);
			}

			response.setPayload(bean);
			response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		} else {
			response.setMessage("No products available on the requested category - "+vehicleType);
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
		}
		
		return response;
	}
	
	private static Map<String,String> sortList;
	static {
		sortList = new LinkedHashMap<String,String>();
		sortList.put("brand", "Brand");
	    sortList.put("wet", "Wet");
	    sortList.put("dry", "Dry");
	    sortList.put("comfort", "Comfort");
	    sortList.put("winter", "Winter/Snow");
	    sortList.put("treadwear", "Treadwear");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TireCatalogDataBean getTireCatalogData(String vehicleType, String siteName, String sortBy) {
		TireCatalogDataBean data = new TireCatalogDataBean();
		
		if (!StringUtils.isNullOrEmpty(siteName))
			locator.getConfig().setSiteName(siteName);
		
		Map surveyData = surveyDAO.getMappedMindshareTiresurveyDetails();
		data.setMappedMindshareTiresurveyDetails(surveyData);
		List groupings = jda2CatalogDAO.getAllTireGrouping();
	    Map mappedGroupings = TireCatalogUtils.getMappedTireGrouping(groupings);
	    data.setMappedTireGroupings(mappedGroupings);
	    List bestInClassTires = catalogDAO.getBestInClassTires();
	    data.setBestInClassTires(bestInClassTires);
	    Map mappedBestInClassTires = TireCatalogUtils.getMappedBestInClassTires(bestInClassTires);
	    data.setMappedBestInClassTires(mappedBestInClassTires);
	    List allSiteTires = catalogDAO.getSiteTires();
	    data.setAllSiteTires(allSiteTires);
	    
	    Map<String, List> mappedSegmentTires = new LinkedHashMap<String, List>();
	    for(int i=0; i< allSiteTires.size(); i++){
	    	com.bfrc.pojo.tire.jda2.Display disp = (com.bfrc.pojo.tire.jda2.Display)allSiteTires.get(i);
	        TireGrouping tg = (TireGrouping)mappedGroupings.get(disp.getId());
	        if(tg != null){
	            String segment = tg.getClass_();
	            if(!mappedSegmentTires.containsKey(segment)){
	                List<com.bfrc.pojo.tire.jda2.Display> l = new ArrayList<com.bfrc.pojo.tire.jda2.Display>();
	                l.add(disp);
	                mappedSegmentTires.put(segment, l);
	            }else{
	                ((List<com.bfrc.pojo.tire.jda2.Display>)mappedSegmentTires.get(segment)).add(disp);
	            }
	        }
	    }
	    data.setMappedSegmentTires(mappedSegmentTires);
	    Map mappedTypeTires = new LinkedHashMap();
	    for(int i=0; i< allSiteTires.size(); i++){
	    	com.bfrc.pojo.tire.jda2.Display disp = (com.bfrc.pojo.tire.jda2.Display)allSiteTires.get(i);
	        TireGrouping tg = (TireGrouping)mappedGroupings.get(disp.getId());
	        if(tg != null){
	        	String segment = StringUtils.categoryNameFilter(tg.getTireVehicle(), "-");
	        	if(!mappedTypeTires.containsKey(segment)){
	                List l = new ArrayList();
	                l.add(disp);
	                mappedTypeTires.put(segment, l);
	            }else{
	                ((List)mappedTypeTires.get(segment)).add(disp);
	            }
	        }
	    }
	    data.setMappedTypeTires(mappedTypeTires);
	    data.setTireGroupings(groupings);
	    Map allTires = jda2CatalogDAO.getMappedDisplays();
	    data.setAllMappedTires(allTires);
	    data.setMappedTireGroupings(mappedGroupings);
	    
	    Map mappedBrands = jda2CatalogDAO.getMappedBrands();
	    data.setMappedTireBrands(mappedBrands);
	    
	    List vehicleTypeNames = TireCatalogUtils.getTireGroupingNames(TireCatalogUtils.TIRE_VEHICLE, groupings);
	    vehicleTypeNames.add("all-tires");
	    data.setVehicleTypeNames(vehicleTypeNames);
	    List typeTires = (List)mappedTypeTires.get(vehicleType);
	    if("all-tires".equalsIgnoreCase(vehicleType))
	    {
	    	typeTires  = new ArrayList<com.bfrc.pojo.tire.jda2.Display>();
	    	for(int i=0; i<vehicleTypeNames.size()-1; i++){
	    		String type2 = StringUtils.categoryNameFilter((String)vehicleTypeNames.get(i), "-");
	    		typeTires.addAll((List<com.bfrc.pojo.tire.jda2.Display>)mappedTypeTires.get(type2));
	    	}
	    	mappedTypeTires.put(vehicleType, typeTires);
	    }
	    
	    List sortedTires = sortTires(mappedBrands, surveyData, typeTires, sortBy);
	    mappedTypeTires.remove(vehicleType);
	    mappedTypeTires.put(vehicleType, sortedTires);
	    
	    return data;
	}
	
	static java.text.DecimalFormat df8_2 = new java.text.DecimalFormat("00000.00");
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List sortTires(Map mappedBrands, Map mappedSurveyData, List tires, String sortBy){
        List sortedList = new ArrayList();
        List attr_id = new ArrayList();
        Map m = new HashMap();
        double maxNumber = 99999;
        if (tires != null && !tires.isEmpty()) {
	        for(int i=0; i<tires.size(); i++){
	        	com.bfrc.pojo.tire.jda2.Display tire = (com.bfrc.pojo.tire.jda2.Display)tires.get(i);
	            m.put(tire.getId(), tire);
	            MindshareTiresurveyDetails surveyData = (MindshareTiresurveyDetails)mappedSurveyData.get(tire.getId());
	            //String totalDry = StringUtils.formatDecimal(((detail.getTireStability().doubleValue()+detail.getDryTraction().doubleValue())/2), "0.0");
	            //String totalComfort = StringUtils.formatDecimal(((detail.getRideComfort().doubleValue()+detail.getNoiseLevel().doubleValue())/2), "0.0");
	
	            if("brand".equalsIgnoreCase(sortBy)){
	                Brand brand = (Brand)mappedBrands.get(tire.getBrandId()+"");
	                attr_id.add(brand.getValue()+"|"+tire.getId());
	
	            }else if("wet".equalsIgnoreCase(sortBy)){
	                double num = surveyData == null ? 0 :  surveyData.getWetTraction() == null ? 0.01 : surveyData.getWetTraction().doubleValue();
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }else if("dry".equalsIgnoreCase(sortBy)){
	                double num = surveyData == null ? 0 :  (surveyData.getTireStability().doubleValue()+surveyData.getDryTraction().doubleValue())/2;
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }else if("comfort".equalsIgnoreCase(sortBy)){
	                double num = surveyData == null ? 0 :  (surveyData.getRideComfort().doubleValue()+surveyData.getNoiseLevel().doubleValue())/2;
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }else if("winter".equalsIgnoreCase(sortBy)){
	                double num = surveyData == null ? 0 :  surveyData.getTractionInSnowIce() == null ? 0.01 : surveyData.getTractionInSnowIce().doubleValue();
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }else if("treadwear".equalsIgnoreCase(sortBy)){
	                double num = surveyData == null ? 0 :  surveyData.getTireWear() == null ? 0.01 : surveyData.getTireWear().doubleValue();
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }else{
	                double num = surveyData == null ? 0 : 1;
	                attr_id.add(df8_2.format(maxNumber-num)+"|"+tire.getId());
	            }
	        }
    	}
        Collections.sort(attr_id);
        for(int i=0; i< attr_id.size(); i++){
            String s = (String)attr_id.get(i);
            Long id = Long.valueOf(s.substring(s.indexOf("|")+1));
            sortedList.add(m.get(id));
        }
        return sortedList;
    }
    
    @SuppressWarnings("rawtypes")
	private Map<String, String> getVehicleTypeNames(List vehicleTypeNames) {
    	Map<String, String> typeNames = new HashMap<String, String>();
		if (vehicleTypeNames != null && !vehicleTypeNames.isEmpty()) {
			for (int i = 0; i < vehicleTypeNames.size(); i++) {
				String type = (String) vehicleTypeNames.get(i);
				if ("all-tires".equalsIgnoreCase(type)) {
					typeNames.put(StringUtils.categoryNameFilter(type.toLowerCase(), "-"), "All Tires");
				} else {
					typeNames.put(StringUtils.categoryNameFilter(type.toLowerCase(), "-"), type);
				}
			}			
		}
		return typeNames;
	}
    
    @SuppressWarnings("rawtypes")
	private List<String> getFilters(String vehicleType, List groupings) {
    	List<String> filters = new ArrayList<String>();
    	List subnames = TireCatalogUtils.getVehicleTypeClassNames(vehicleType, groupings);
    	if (subnames != null && !subnames.isEmpty()) {
    		for(int y=0; subnames != null && y<subnames.size();y++ ){
    			filters.add((String)subnames.get(y));
    		}
    	}
		return (!filters.isEmpty()) ? filters : null;
	}

	/*private void getImageURLs(String tireImgDir, String tireName, Product product) {
		tireName = tireName == null ? "" : tireName.trim();
		String fdTirename = StringUtils.nameFilter(tireName);
		product.setImg60(tireImgDir + "full-60/" + fdTirename + ".png");
		product.setImg90(tireImgDir+"full-90/" + fdTirename + ".png");
		product.setImg180(tireImgDir+"full-180/" + fdTirename + ".png");
		product.setImgZoom90(tireImgDir+"zoom-90/" + fdTirename + ".png");
		product.setImgFeature(tireImgDir+"features/" + fdTirename + "_English.png");		
	}*/
}
