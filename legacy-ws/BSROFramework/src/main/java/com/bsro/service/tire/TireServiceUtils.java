package com.bsro.service.tire;

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
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda2.Brand;
import com.bfrc.pojo.tire.jda2.Display;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bsro.databean.tire.LocalPageDataBean;
import com.bsro.databean.tirecatalog.TireCatalogDataBean;

public class TireServiceUtils {

	public static LocalPageDataBean getLocalPageData(HttpServletRequest request, String by, String make, String model, String city, String state, String tireType){
		LocalPageDataBean data = new LocalPageDataBean();
		if("batteryVehicle".equalsIgnoreCase(by)){
			String indexes = TireServiceData.batteryVehicleContent.get(make+"|"+model);
			int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0;
			if(indexes != null){
				String[] tokens = indexes.split(",");
				String t1= tokens[0];
				i1 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				i2 = Integer.parseInt(t2.substring(6));	
				String t3= tokens[2];
				i3 = Integer.parseInt(t3.substring(6));
				String t4= tokens[3];
				i4 = Integer.parseInt(t4.substring(6));			
				String t5= tokens[4];
				i5 = Integer.parseInt(t5.substring(6));		
				String t6= tokens[5];
				i6 = Integer.parseInt(t6.substring(6));
								
			}
			String s1 = TireServiceData.batteryVehicleContentP1S1.get(i1);
			String s2 = TireServiceData.batteryVehicleContentP1S2.get(i2);
			data.setParagragh1(s1.replace("[VEHICLE]", make+" "+model)+" "+s2.replace("[VEHICLE]", make+" "+model));
			
			
			String s3 = TireServiceData.batteryVehicleContentP2S1.get(i3);
			String s4 = TireServiceData.batteryVehicleContentP2S2.get(i4);
			data.setParagragh2(s3.replace("[VEHICLE]", make+" "+model)+" "+s4.replace("[VEHICLE]", make+" "+model));
			
			
			String s5 = TireServiceData.batteryVehicleContentP3S1.get(i5);
			String s6 = TireServiceData.batteryVehicleContentP3S2.get(i6);
			data.setParagragh3(s5.replace("[VEHICLE]", make+" "+model)+" "+s6.replace("[VEHICLE]", make+" "+model));
			
			//-- HEADER DATA --//
			String indexesH = TireServiceHeaderCopyData.batteryVehicleHeaders.get(make+"|"+model);
			int h11=0,h12=0,h21=0,h22=0;
			int ROTATOR_SIZE=3;
			int[] h23 = new int[ROTATOR_SIZE];
			if(indexesH != null){
				String[] tokens = indexesH.split(",");
				String t1= tokens[0];
				h11 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				String[] t2tokens = t2.substring(6).split(":");	
				for(int i=0; i< ROTATOR_SIZE; i++){
					h23[i] = Integer.parseInt(t2tokens[i]);
				}

			}
			//-- header 1
			String h1s1= (String)TireServiceHeaderCopyData.batteryVehicleHeadersH1S1.get(h11);
			data.setHeader1Sentence1(h1s1.replace("{make, model}", make+" "+model));
			String h1s2 = (String)TireServiceHeaderCopyData.batteryVehicleHeadersH1S2.get(h11);
			data.setHeader1Sentence2(h1s2.replace("{make, model}", make+" "+model));
			//--header 2
			String h2s1 = (String)TireServiceHeaderCopyData.batteryVehicleHeadersH2S1.get(h21);
			data.setHeader2Sentence1(h2s1.replace("{make, model}", make+" "+model));
			String h2s2 = (String)TireServiceHeaderCopyData.batteryVehicleHeadersH2S2.get(h21);
			data.setHeader2Sentence2(h2s2.replace("{make, model}", make+" "+model));
			List h2s3List = new ArrayList();
			List h2s4List = new ArrayList();
			for(int i=0; i< ROTATOR_SIZE; i++){
				int ih = h23[i];
				String h2s3  = (String)TireServiceHeaderCopyData.batteryVehicleHeadersH2S3.get(ih);
				h2s3List.add(h2s3.replace("{make, model}", make+" "+model));
				String h2s4  = (String)TireServiceHeaderCopyData.batteryVehicleHeadersH2S4.get(ih);
				h2s4List.add(h2s4.replace("{make, model}", make+" "+model));
			}
			data.setHeader2Sentence3(h2s3List);
			data.setHeader2Sentence4(h2s4List);

		}else if("batteryCityState".equalsIgnoreCase(by)){
			String indexes = TireServiceData.batteryCityStateContent.get(city+"|"+state);
			int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0;
			if(indexes != null){
				String[] tokens = indexes.split(",");
				String t1= tokens[0];
				i1 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				i2 = Integer.parseInt(t2.substring(6));	
				String t3= tokens[2];
				i3 = Integer.parseInt(t3.substring(6));
				String t4= tokens[3];
				i4 = Integer.parseInt(t4.substring(6));			
				String t5= tokens[4];
				i5 = Integer.parseInt(t5.substring(6));		
				String t6= tokens[5];
				i6 = Integer.parseInt(t6.substring(6));
								
			}
			String s1 = TireServiceData.batteryCityStateContentP1S1.get(i1);
			String s2 = TireServiceData.batteryCityStateContentP1S2.get(i2);
			data.setParagragh1(s1.replace("[CITY, STATE]", city+", "+state)+" "+s2.replace("[CITY, STATE]", city+", "+state));
			
			
			String s3 = TireServiceData.batteryCityStateContentP2S1.get(i3);
			String s4 = TireServiceData.batteryCityStateContentP2S2.get(i4);
			data.setParagragh2(s3.replace("[CITY, STATE]", city+", "+state)+" "+s4.replace("[CITY, STATE]", city+", "+state));
			
			
			String s5 = TireServiceData.batteryCityStateContentP3S1.get(i5);
			String s6 = TireServiceData.batteryCityStateContentP3S2.get(i6);
			data.setParagragh3(s5.replace("[CITY, STATE]", city+", "+state)+" "+s6.replace("[CITY, STATE]", city+", "+state));
			
			//-- HEADER DATA --//
			String indexesH = TireServiceHeaderCopyData.batteryCityStateHeaders.get(city+"|"+state);
			int h11=0,h12=0,h21=0,h22=0;
			int ROTATOR_SIZE=3;
			int[] h23 = new int[ROTATOR_SIZE];
			if(indexesH != null){
				String[] tokens = indexesH.split(",");
				String t1= tokens[0];
				h11 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				String[] t2tokens = t2.substring(6).split(":");	
				for(int i=0; i< ROTATOR_SIZE; i++){
					h23[i] = Integer.parseInt(t2tokens[i]);
				}

			}
			//-- header 1
			String h1s1= (String)TireServiceHeaderCopyData.batteryCityStateHeadersH1S1.get(h11);
			data.setHeader1Sentence1(h1s1.replace("{city, state}", city+", "+state));
			String h1s2 = (String)TireServiceHeaderCopyData.batteryCityStateHeadersH1S2.get(h11);
			data.setHeader1Sentence2(h1s2.replace("{city, state}", city+", "+state));
			//--header 2
			String h2s1 = (String)TireServiceHeaderCopyData.batteryCityStateHeadersH2S1.get(h21);
			data.setHeader2Sentence1(h2s1.replace("{city, state}", city+", "+state));
			String h2s2 = (String)TireServiceHeaderCopyData.batteryCityStateHeadersH2S2.get(h21);
			data.setHeader2Sentence2(h2s2.replace("{city, state}", city+", "+state));
			List h2s3List = new ArrayList();
			List h2s4List = new ArrayList();
			for(int i=0; i< ROTATOR_SIZE; i++){
				int ih = h23[i];
				String h2s3  = (String)TireServiceHeaderCopyData.batteryCityStateHeadersH2S3.get(ih);
				h2s3List.add(h2s3.replace("{city, state}", city+", "+state));
				String h2s4  = (String)TireServiceHeaderCopyData.batteryCityStateHeadersH2S4.get(ih);
				h2s4List.add(h2s4.replace("{city, state}", city+", "+state));
			}
			data.setHeader2Sentence3(h2s3List);
			data.setHeader2Sentence4(h2s4List);
			

		}else if("tireVehicle".equalsIgnoreCase(by)){
			String indexes = TireServiceData.tireVehicleContent.get(make+"|"+model);
			int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0;
			if(indexes != null){
				String[] tokens = indexes.split(",");
				String t1= tokens[0];
				i1 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				i2 = Integer.parseInt(t2.substring(6));	
				String t3= tokens[2];
				i3 = Integer.parseInt(t3.substring(6));
				String t4= tokens[3];
				i4 = Integer.parseInt(t4.substring(6));			
				String t5= tokens[4];
				i5 = Integer.parseInt(t5.substring(6));		
				String t6= tokens[5];
				i6 = Integer.parseInt(t6.substring(6));
								
			}
			String s1 = TireServiceData.tiresVehicleContentP1S1.get(i1);
			String s2 = TireServiceData.tiresVehicleContentP1S2.get(i2);
			data.setParagragh1(s1.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType)+" "+s2.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType));
			
			
			String s3 = TireServiceData.tiresVehicleContentP2S1.get(i3);
			String s4 = TireServiceData.tiresVehicleContentP2S2.get(i4);
			data.setParagragh2(s3.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType)+" "+s4.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType));
			
			
			String s5 = TireServiceData.tiresVehicleContentP3S1.get(i5);
			String s6 = TireServiceData.tiresVehicleContentP3S2.get(i6);
			data.setParagragh3(s5.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType)+" "+s6.replace("[VEHICLE]", make+" "+model).replace("[TIRE TYPE]", tireType));
			
			//-- HEADER DATA --//
			String indexesH = TireServiceHeaderCopyData.tireVehicleHeaders.get(make+"|"+model);
			int h11=0,h12=0,h21=0,h22=0,h23=0;
			int ROTATOR_SIZE=3;
			if(indexesH != null){
				String[] tokens = indexesH.split(",");
				String t1= tokens[0];
				h11 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				h23 = Integer.parseInt(t2.substring(6));

			}
			//-- header 1
			String h1s1= (String)TireServiceHeaderCopyData.tireVehicleHeadersH1S1.get(h11);
			data.setHeader1Sentence1(h1s1.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			String h1s2 = (String)TireServiceHeaderCopyData.tireVehicleHeadersH1S2.get(h11);
			data.setHeader1Sentence2(h1s2.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			//--header 2
			String h2s1 = (String)TireServiceHeaderCopyData.tireVehicleHeadersH2S1.get(h21);
			data.setHeader2Sentence1(h2s1.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			String h2s2 = (String)TireServiceHeaderCopyData.tireVehicleHeadersH2S2.get(h21);
			data.setHeader2Sentence2(h2s2.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			
			List h2s3List = new ArrayList();
			List h2s4List = new ArrayList();
			String h2s3 = (String)TireServiceHeaderCopyData.tireVehicleHeadersH2S3.get(h23);
			String h2s4 = (String)TireServiceHeaderCopyData.tireVehicleHeadersH2S4.get(h23);
			h2s3List.add(h2s3.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			h2s4List.add(h2s4.replace("{make, model}", make+" "+model).replace("{tire type}", tireType));
			data.setHeader2Sentence3(h2s3List);
			data.setHeader2Sentence4(h2s4List);

		}else if("tireCityState".equalsIgnoreCase(by)){
			String indexes = TireServiceData.tireCityStateContent.get(city+"|"+state);
			int i1=0,i2=0,i3=0,i4=0,i5=0,i6=0;
			if(indexes != null){
				String[] tokens = indexes.split(",");
				String t1= tokens[0];
				i1 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				i2 = Integer.parseInt(t2.substring(6));	
				String t3= tokens[2];
				i3 = Integer.parseInt(t3.substring(6));
				String t4= tokens[3];
				i4 = Integer.parseInt(t4.substring(6));			
				String t5= tokens[4];
				i5 = Integer.parseInt(t5.substring(6));		
				String t6= tokens[5];
				i6 = Integer.parseInt(t6.substring(6));
								
			}
			String s1 = TireServiceData.tiresCityStateContentP1S1.get(i1);
			String s2 = TireServiceData.tiresCityStateContentP1S2.get(i2);
			data.setParagragh1(s1.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType)+" "+s2.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType));
			
			
			String s3 = TireServiceData.tiresCityStateContentP2S1.get(i3);
			String s4 = TireServiceData.tiresCityStateContentP2S2.get(i4);
			data.setParagragh2(s3.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType)+" "+s4.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType));
			
			
			String s5 = TireServiceData.tiresCityStateContentP3S1.get(i5);
			String s6 = TireServiceData.tiresCityStateContentP3S2.get(i6);
			data.setParagragh3(s5.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType)+" "+s6.replace("[CITY, STATE]", city+", "+state).replace("[TIRE TYPE]", tireType));
			
			//-- HEADER DATA --//
			String indexesH = TireServiceHeaderCopyData.tireCityStateHeaders.get(city+"|"+state);
			int h11=0,h12=0,h21=0,h22=0,h23=0;
			int ROTATOR_SIZE=3;
			if(indexesH != null){
				String[] tokens = indexesH.split(",");
				String t1= tokens[0];
				h11 = Integer.parseInt(t1.substring(6));
				String t2= tokens[1];
				h23 = Integer.parseInt(t2.substring(6));

			}
			//-- header 1
			String h1s1= (String)TireServiceHeaderCopyData.tireCityStateHeadersH1S1.get(h11);
			data.setHeader1Sentence1(h1s1.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			String h1s2 = (String)TireServiceHeaderCopyData.tireCityStateHeadersH1S2.get(h11);
			data.setHeader1Sentence2(h1s2.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			//--header 2
			String h2s1 = (String)TireServiceHeaderCopyData.tireCityStateHeadersH2S1.get(h21);
			data.setHeader2Sentence1(h2s1.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			String h2s2 = (String)TireServiceHeaderCopyData.tireCityStateHeadersH2S2.get(h21);
			data.setHeader2Sentence2(h2s2.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			List h2s3List = new ArrayList();
			List h2s4List = new ArrayList();
			String h2s3 = (String)TireServiceHeaderCopyData.tireCityStateHeadersH2S3.get(h23);
			String h2s4 = (String)TireServiceHeaderCopyData.tireCityStateHeadersH2S4.get(h23);
			h2s3List.add(h2s3.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			h2s4List.add(h2s4.replace("{city, state}", city+", "+state).replace("{tire type}", tireType));
			data.setHeader2Sentence3(h2s3List);
			data.setHeader2Sentence4(h2s4List);

		}
		return data;
	}
	
}
