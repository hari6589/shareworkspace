package com.bfrc.backend;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.Config;
import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.dao.SurveyDAO;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.pojo.ext.BestInClassTireResultData;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda2.Display;
import com.bfrc.pojo.tire.jda2.TireGrouping;

public class BestInClassService {

	public BestInClassTireResultData getResultData(HttpServletRequest request){
		BestInClassTireResultData data = new BestInClassTireResultData();
		ServletContext ctx = request.getSession().getServletContext();
		JDA2CatalogDAO jda2CatalogDAO = (JDA2CatalogDAO)Config.locate(ctx, "jda2CatalogDAO");
		SurveyDAO surveyDAO = (SurveyDAO)Config.locate(ctx, "surveyDAO");
		CatalogDAO catalogDAO = (CatalogDAO)Config.locate(ctx, "catalogDAO");
		Map surveyData = surveyDAO.getMappedMindshareTiresurveyDetails();
		data.setMappedMindshareTiresurveyDetails(surveyData);
		List groupings = jda2CatalogDAO.getAllTireGrouping();
	    Map mappedGroupins = TireCatalogUtils.getMappedTireGrouping(groupings);
	    List bestInClassTires = catalogDAO.getBestInClassTires();
	    data.setBestInClassTires(bestInClassTires);
	    Map mappedBestInClassTires = TireCatalogUtils.getMappedBestInClassTires(bestInClassTires);
	    Map mappedSegmentBestInClassTires = new LinkedHashMap();
	    for(int i=0; i< bestInClassTires.size(); i++){
	    	Display disp = (Display)bestInClassTires.get(i);
	    	TireGrouping tg = (TireGrouping)mappedGroupins.get(disp.getId());
	    	//String segment = tg.getTireVehicle();
	    	String segment = tg.getClass_();
	    	if(!mappedSegmentBestInClassTires.containsKey(segment)){
                List l = new ArrayList();
                l.add(disp);
                mappedSegmentBestInClassTires.put(segment, l);
            }else{
                ((List)mappedSegmentBestInClassTires.get(segment)).add(disp);
            }
	    }
	    data.setMappedSegmentBestInClassTires(mappedSegmentBestInClassTires);
	    
		return data;
		
	}
}
