package com.bsro.service.tirecatalog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.Config;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.dao.SurveyDAO;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.framework.util.Util;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda2.Brand;
import com.bfrc.pojo.tire.jda2.Display;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bsro.databean.tirecatalog.TireCatalogDataBean;

public class TireCatalogServiceUtils {

    private static Map<String,String> introData;
	static {
		introData = new LinkedHashMap<String,String>();
		introData.put("Winter", "When teeth-chattering weather strikes, we deliver with an array of Bridgestone Blizzak tires, the definitive wheels for wet, snowy and icy conditions. The right winter tires? They forge on, handling freezing temps dependably.");
	    introData.put("Light Truck", "Tough and comfortable. All-season performance. Wet and dry condition-worthy. These are just a few of the traits these tires possess. Throw in something that appeals to your aesthetics &ndash; rugged good looks &ndash; and it&#39;s clear our wide range of light truck tires aren&#39;t short on appeal.");
	    introData.put("SUV/CUV", "We heart spacious, sturdy rides. And we know they need durable tires that maximize performance, provide off- and on-road endurance and highway stability. But quiet &ndash; even noise-canceling and smooth? Yeah, we&#39;ve got that covered, too.");
	    introData.put("Car & Minivan", "From the soccer fields to grocery and convenience stores, our car and minivan tires offer practicality with a dash of panache. Long lasting and dependable, these tires help you &ndash; and the ones relying on you &ndash; go the distance.");
	    introData.put("Performance", "For drivers with cars that demand superior cornering, our tire brands&#39; sleek performance tires emphasize top-tier handling and are suited to high-end, sophisticated vehicles, which favor high-speed capabilities, a tight grip, low heat and improved wet traction.");
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
	public static TireCatalogDataBean getResultData(HttpServletRequest request){
		TireCatalogDataBean data = new TireCatalogDataBean();
		ServletContext ctx = request.getSession().getServletContext();
		JDA2CatalogDAO jda2CatalogDAO = (JDA2CatalogDAO)Config.locate(ctx, "jda2CatalogDAO");
		SurveyDAO surveyDAO = (SurveyDAO)Config.locate(ctx, "surveyDAO");
		CatalogDAO catalogDAO = (CatalogDAO)Config.locate(ctx, "catalogDAO");
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
	        Display disp = (Display)allSiteTires.get(i);
	        TireGrouping tg = (TireGrouping)mappedGroupings.get(disp.getId());
	        if(tg != null){
	            String segment = tg.getClass_();
	            if(!mappedSegmentTires.containsKey(segment)){
	                List<Display> l = new ArrayList<Display>();
	                l.add(disp);
	                mappedSegmentTires.put(segment, l);
	            }else{
	                ((List<Display>)mappedSegmentTires.get(segment)).add(disp);
	            }
	        }
	    }
	    data.setMappedSegmentTires(mappedSegmentTires);
	    Map mappedTypeTires = new LinkedHashMap();
	    for(int i=0; i< allSiteTires.size(); i++){
	        Display disp = (Display)allSiteTires.get(i);
	        TireGrouping tg = (TireGrouping)mappedGroupings.get(disp.getId());
	        if(tg != null){
	            String segment = tg.getTireVehicle();
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
	    vehicleTypeNames.add("AllTires");
	    data.setVehicleTypeNames(vehicleTypeNames);
	    int idx = 0;
	    String type = request.getParameter("type") == null ? "" : request.getParameter("type");
	    for(int i=0; i<vehicleTypeNames.size(); i++){
	        if(vehicleTypeNames.get(i).toString().equals(type) || type.equals(StringUtils.nameFilter(vehicleTypeNames.get(i).toString()))){
	            idx=i;
	        }
	    }
	    type = (String)vehicleTypeNames.get(idx);
	    List typeTires = (List)mappedTypeTires.get(type);
	    if("AllTires".equalsIgnoreCase(type))
	    {
	    	typeTires  = new ArrayList<Display>();
	    	for(int i=0; i<vehicleTypeNames.size()-1; i++){
	    		String type2 = (String)vehicleTypeNames.get(i);
	    		typeTires.addAll((List<Display>)mappedTypeTires.get(type2));
	    	}
	    	mappedTypeTires.put(type, typeTires);
	    }
	    List sortedTires = sortTires(mappedBrands, surveyData, typeTires, request.getParameter("sortBy"));
	    mappedTypeTires.remove(type);
	    mappedTypeTires.put(type, sortedTires);
	    request.setAttribute("sortList", sortList);
	    request.setAttribute("introData", introData);
		return data;
		
	}
	
	static java.text.DecimalFormat df8_2 = new java.text.DecimalFormat("00000.00");
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static List sortTires(Map mappedBrands, Map mappedSurveyData, List tires, String sortBy){
        List sortedList = new ArrayList();
        List attr_id = new ArrayList();
        Map m = new HashMap();
        double maxNumber = 99999;
        for(int i=0; i<tires.size(); i++){
            Display tire = (Display)tires.get(i);
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
        Collections.sort(attr_id);
        for(int i=0; i< attr_id.size(); i++){
            String s = (String)attr_id.get(i);
            Long id = Long.valueOf(s.substring(s.indexOf("|")+1));
            sortedList.add(m.get(id));
        }
        return sortedList;
    }
}
