package com.bsro.service.oil;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import app.bsro.model.oil.OilChangeQuote;
import app.bsro.model.oil.OilChangeSearchResult;

import com.bsro.exception.errors.ErrorMessageException;

public interface OilService {
	 public Map<String,String> getYears() throws IOException;
	 public Map<String,String> getManufacturers(String yearId) throws IOException;
	 public Map<String,String> getModels(String yearId, String manufacturerId) throws IOException;
	 public Map<String,String> getMileages();
	 public OilChangeSearchResult getProducts(String equipmentId, Long storeNumber, Boolean isHighMileageVehicle) throws IOException;
	 public Long createOilChangeQuote(Long oilArticleNumber, Long storeNumber, BigDecimal quarts, String vehicleId, Integer vehicleYear, String vehicleMake, String vehicleModelSubmodelEngine, String customerZip,String siteName,Boolean isHighMileage) throws IOException, ErrorMessageException;		
	 public OilChangeQuote getOilChangeQuote(Long oilChangeQuoteId, String siteName) throws IOException, ErrorMessageException;
	 public Boolean updateOilChangeQuoteFirstName(Long oilChangeQuoteId, String siteName, String firstName) throws IOException, ErrorMessageException;
	 public Boolean updateOilChangeQuoteLastName(Long oilChangeQuoteId, String siteName, String lastName) throws IOException, ErrorMessageException;
}
