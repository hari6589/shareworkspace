package com.bfrc.framework.dao;

import java.util.*;

import com.bfrc.pojo.tire.jda.*;

public interface JDACatalogDAO {
	//-- brand --//
	Brand getBrandById(Object id);
	List getAllBrands();
	Map getMappedBrands();
	
	
	//-- Category --//
	Category getCategoryById(Object id);
	List getAllCategories();
	Map getMappedCategories();
	
	//-- Configuration --//
	Configuration getConfigurationById(Object id);
	List getAllConfigurations();
	Map getMappedConfigurations();
	
	//-- Display --//
	Display getDisplayById(Object id);
	List getAllDisplays();
	Map getMappedDisplays();
	
	//-- Fact --//
	Fact getFactById(Object id);
	List getAllFacts();
	Map getMappedFacts();
	
	//-- Feature --//
	Feature getFeatureById(Object id);
	List getAllFeatures();
	Map getMappedFeatures();
	
	//-- Mileage --//
	Mileage getMileageById(Object id);
	List getAllMileages();
	Map getMappedMileages();
	
	//-- Segment --//
	Segment getSegmentById(Object id);
	List getAllSegments();
	Map getMappedSegments();
	
	//-- Sidewall --//
	Sidewall getSidewallById(Object id);
	List getAllSidewalls();
	Map getMappedSidewalls();
	
	//-- Speed --//
	Speed getSpeedById(Object id);
	List getAllSpeeds();
	Map getMappedSpeeds();
	
	//-- Technology --//
	Technology getTechnologyById(Object id);
	List getAllTechnologies();
	Map getMappedTechnologies();
	
	
	//-- Temperature --//
	Temperature getTemperatureById(Object id);
	List getAllTemperatures();
	Map getMappedTemperatures();
	
	//-- Tiregroup --//
	Tiregroup getTiregroupById(Object id);
	List getAllTiregroups();
	Map getMappedTiregroups();
	
	//-- Traction --//
	Traction getTractionById(Object id);
	List getAllTractions();
	Map getMappedTractions();
	
	//-- Warranty --//
	Warranty getWarrantyById(Object id);
	List getAllWarranties();
	Map getMappedWarranties();
}
