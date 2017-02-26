package com.bsro.api.rest.ws.oil;


import java.math.BigDecimal;
import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.bsro.compare.oil.OilChangePackageComparator;
import app.bsro.model.oil.OilChangePackage;
import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;

import com.bfrc.dataaccess.svc.oil.IOilService;
import com.bsro.core.exception.ws.ErrorMessageException;
import com.bsro.core.exception.ws.WebServiceException;
import com.bsro.oil.util.OilDataConstants;
import com.bsro.oil.util.OilDataFacade;

import com.bsro.api.rest.ws.oil.IOilWs;

import java.io.IOException;

@Component
public class OilWebService implements IOilWs {

	private static final Log logger = LogFactory.getLog(OilDataFacade.class);

	@Autowired
	private IOilService oilService;

	public IOilService getOilService() {
		return oilService;
	}

	public void setOilService(IOilService oilService) {
		this.oilService = oilService;
	}

	/**
	 * @Override
	 * @param headers
	 * @return
	 *
	 */
	public LinkedHashMap<String, String> getYears(HttpHeaders headers) throws IOException {
		String sectorId = OilDataConstants.SECTOR_CARS;
		LinkedHashMap<String, String> yearMap = OilDataFacade.getYears(sectorId);
		return yearMap;
		
	}

	@Override
	public LinkedHashMap<String, String> getManufacturers(HttpHeaders headers, String yearGUID) throws IOException {
		String sectorId = OilDataConstants.SECTOR_CARS;
		LinkedHashMap<String, String> sortedManufacturerMap = OilDataFacade.getMakes(sectorId, yearGUID);
		return sortedManufacturerMap;
	}

	@Override
	public LinkedHashMap<String, String> getModels(HttpHeaders headers, String yearGUID, String manufacturerGUID) throws IOException {

		String sectorId = OilDataFacade.SECTOR_CARS;
		LinkedHashMap<String, String> sortedEquipmentMap = OilDataFacade.getModels(sectorId, yearGUID, manufacturerGUID);
		return sortedEquipmentMap;
	}

	@Override
	public Long createOilChangeQuote(HttpHeaders headers, Long oilArticleNumber, Long storeNumber, BigDecimal quarts, String vehicleId, Integer vehicleYear, String vehicleMake, String vehicleModelSubmodelEngine, String customerZip, String siteName) throws ErrorMessageException {
		// this is just an example of how to use this ValidationException - it's not fully fleshed out, you would probably want an actual validation service call
		if (oilArticleNumber == null) {
			ErrorMessageException validationException = new ErrorMessageException();
			validationException.addFieldError("oilArticleNumber", "Please select a type of oil");
			throw validationException;
		}
		
		try {
			Long quoteId = oilService.createOilChangeQuote(oilArticleNumber, storeNumber, quarts, vehicleId, vehicleYear, vehicleMake, vehicleModelSubmodelEngine, customerZip, siteName);
			return quoteId;
		} catch (Throwable throwable) {
			throw new WebServiceException(throwable);
		}
	}

	@Override
	public OilChangeQuote getOilChangeQuote(HttpHeaders headers, Long oilChangeQuoteId, String siteName) {
		try {
			return oilService.getOilChangeQuote(oilChangeQuoteId, siteName);
		} catch (Throwable throwable) {
			throw new WebServiceException(throwable);
		}
	}
	

	public String updateOilChangeQuoteFirstName(HttpHeaders headers, Long oilChangeQuoteId, String siteName, String firstName) throws ErrorMessageException {
		oilService.updateOilChangeQuoteFirstName(oilChangeQuoteId, siteName, firstName);
		return Boolean.TRUE.toString();
	}

	public String updateOilChangeQuoteLastName(HttpHeaders headers, Long oilChangeQuoteId, String siteName, String lastName) throws ErrorMessageException {
		oilService.updateOilChangeQuoteLastName(oilChangeQuoteId, siteName, lastName);
		return Boolean.TRUE.toString();
	}
	


	
	@Override
	public OilChangeSearchResult getProducts(HttpHeaders headers, String equipmentGUID, Long storeNumber, Boolean isHighMileageVehicle) throws IOException{
		
		OilChangeSearchResult oilChangeSearchResult = new OilChangeSearchResult();
		oilChangeSearchResult.setVehicleId(equipmentGUID);
		oilChangeSearchResult.setStoreNumber(storeNumber);
		oilChangeSearchResult.setOilChangePackages(new ArrayList<OilChangePackage>());
		
		// Create a Dummy list
		List<String> climateStringList = new ArrayList<String>();

		Map<String, OilChangePackage> oatsNameToOilChangePackageMapping = new HashMap<String, OilChangePackage>();
		
		TreeMap<String, String> productMap = new TreeMap<String, String>();
		TreeMap<String, List<String>> recommendationMap = new TreeMap<String, List<String>>();
		
		
		String capacityFrom = OilDataFacade.getCapacityForVehicle(equipmentGUID);
		
		
		OilChangePackage highMileageOilChangePackage = null;
		OilChangePackage oilChangePackage = null;
		
		
		//Get the Product List for a given vehicle guid
		
		Map<String,String> productForVehicleMap = OilDataFacade.getProducts(equipmentGUID);
		

			Iterator it = productForVehicleMap.entrySet().iterator();			
			
			while(it.hasNext()) {				
				Map.Entry<String,String> mapEntry  = (Map.Entry)it.next();				
				
			
				String productName  = mapEntry.getValue();
				
				
				
				oilChangeSearchResult.setVehicleOilCapacity(BigDecimal.valueOf(Double.parseDouble(capacityFrom)));
				productMap.put(productName, productName);
				oilChangePackage = oilService.getOilChangeSearchResultByStoreNumberOatsNameAndQuarts(storeNumber, productName, new BigDecimal(capacityFrom));
					
				// we need this mapping so we can link products with recommendations
				oatsNameToOilChangePackageMapping.put(productName, oilChangePackage);
				if (isHighMileageVehicle && highMileageOilChangePackage == null) {
					
					if (oilChangePackage != null) {
						String oilTypeFriendlyId = oilChangePackage.getOilType().getOilTypeFriendlyId();
						// For High Mileage, do not get value from database, instead use the value coming from OATS webservice.
						if (! oilTypeFriendlyId.equalsIgnoreCase("FULL_SYNTHETIC") ){
							highMileageOilChangePackage = oilService.getHighMileageOilChangePackageByStoreNumberAndViscosityAndQuarts(storeNumber, oilChangePackage.getBaseOil().getViscosity(), new BigDecimal(capacityFrom));
						}
						if (highMileageOilChangePackage != null  ) {
							highMileageOilChangePackage.setIsRecommended(Boolean.TRUE);
							highMileageOilChangePackage.setClimatesForWhichRecommendationApplies(new ArrayList<String>());
							oilChangeSearchResult.getOilChangePackages().add(highMileageOilChangePackage);
						}
					}
				}
				
				if (!isHighMileageVehicle || highMileageOilChangePackage == null) {
					
					if("1".equalsIgnoreCase(mapEntry.getKey())){
					recommendationMap.put(productName, climateStringList);
					}
					String recommendedProductName = productName;
					if (oatsNameToOilChangePackageMapping.containsKey(recommendedProductName)&&oatsNameToOilChangePackageMapping.get(recommendedProductName)!=null) {
						oatsNameToOilChangePackageMapping.get(recommendedProductName).setIsRecommended(Boolean.TRUE);
						if (oatsNameToOilChangePackageMapping.get(recommendedProductName).getClimatesForWhichRecommendationApplies() != null) {
							oatsNameToOilChangePackageMapping.get(recommendedProductName).getClimatesForWhichRecommendationApplies().addAll(recommendationMap.get(recommendedProductName));
						} else {
							oatsNameToOilChangePackageMapping.get(recommendedProductName).setClimatesForWhichRecommendationApplies(recommendationMap.get(recommendedProductName));
						}
					}
				}
		}
			 
		
		oilChangeSearchResult.getOilChangePackages().addAll(oatsNameToOilChangePackageMapping.values());
		Collections.sort(oilChangeSearchResult.getOilChangePackages(), new OilChangePackageComparator());
		
		
		return oilChangeSearchResult;
	}

}
