package com.bfrc.framework.dao.hibernate3;

import java.util.*;
import java.util.Map;

import com.bfrc.*;
import com.bfrc.framework.dao.*;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.realestate.RealestateRegionContact;
import com.bfrc.pojo.store.*;
import com.bfrc.pojo.tire.jda.*;

public class JDACatalogDAOImpl extends HibernateDAOImpl implements JDACatalogDAO {
	//-- brand --//
	public Brand getBrandById(Object id){
		if(id == null) return null;
		return (Brand)getHibernateTemplate().get(Brand.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllBrands(){
		String hql = "from Brand";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedBrands(){
		List l = getAllBrands();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Brand item = (Brand)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	
	//-- Category --//
	public Category getCategoryById(Object id){
		if(id == null) return null;
		return (Category)getHibernateTemplate().get(Category.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllCategories(){
		String hql = "from Category";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedCategories(){
		List l = getAllCategories();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Category item = (Category)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Configuration --//
	public Configuration getConfigurationById(Object id){
		if(id == null) return null;
		return (Configuration)getHibernateTemplate().get(Configuration.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllConfigurations(){
		String hql = "from Configuration";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedConfigurations(){
		List l = getAllConfigurations();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Configuration item = (Configuration)l.get(i);
			m.put(String.valueOf(item.getSku()), item);
		}
		return m;
	}
	
	//-- Display --//
	public Display getDisplayById(Object id){
		if(id == null) return null;
		return (Display)getHibernateTemplate().get(Display.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllDisplays(){
		String hql = "from Display";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedDisplays(){
		List l = getAllDisplays();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Display item = (Display)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Fact --//
	public Fact getFactById(Object id){
		if(id == null) return null;
		return (Fact)getHibernateTemplate().get(Fact.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllFacts(){
		String hql = "from Fact";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedFacts(){
		List l = getAllFacts();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Fact item = (Fact)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Feature --//
	public Feature getFeatureById(Object id){
		if(id == null) return null;
		return (Feature)getHibernateTemplate().get(Feature.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllFeatures(){
		String hql = "from Feature";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedFeatures(){
		List l = getAllFeatures();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Feature item = (Feature)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Mileage --//
	public Mileage getMileageById(Object id){
		if(id == null) return null;
		return (Mileage)getHibernateTemplate().get(Mileage.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllMileages(){
		String hql = "from Mileage";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedMileages(){
		List l = getAllMileages();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Mileage item = (Mileage)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Segment --//
	public Segment getSegmentById(Object id){
		if(id == null) return null;
		return (Segment)getHibernateTemplate().get(Segment.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllSegments(){
		String hql = "from Segment";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedSegments(){
		List l = getAllSegments();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Segment item = (Segment)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Sidewall --//
	public Sidewall getSidewallById(Object id){
		if(id == null) return null;
		return (Sidewall)getHibernateTemplate().get(Sidewall.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllSidewalls(){
		String hql = "from Sidewall";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedSidewalls(){
		List l = getAllSidewalls();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Sidewall item = (Sidewall)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Speed --//
	public Speed getSpeedById(Object id){
		if(id == null) return null;
		return (Speed)getHibernateTemplate().get(Speed.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllSpeeds(){
		String hql = "from Speed";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedSpeeds(){
		List l = getAllSpeeds();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Speed item = (Speed)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Technology --//
	public Technology getTechnologyById(Object id){
		if(id == null) return null;
		return (Technology)getHibernateTemplate().get(Technology.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllTechnologies(){
		String hql = "from Technology";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedTechnologies(){
		List l = getAllTechnologies();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Technology item = (Technology)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	
	//-- Temperature --//
	public Temperature getTemperatureById(Object id){
		if(id == null) return null;
		return (Temperature)getHibernateTemplate().get(Temperature.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllTemperatures(){
		String hql = "from Temperature";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedTemperatures(){
		List l = getAllTemperatures();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Temperature item = (Temperature)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Tiregroup --//
	public Tiregroup getTiregroupById(Object id){
		if(id == null) return null;
		return (Tiregroup)getHibernateTemplate().get(Tiregroup.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllTiregroups(){
		String hql = "from Tiregroup";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedTiregroups(){
		List l = getAllTiregroups();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Tiregroup item = (Tiregroup)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Traction --//
	public Traction getTractionById(Object id){
		if(id == null) return null;
		return (Traction)getHibernateTemplate().get(Traction.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllTractions(){
		String hql = "from Traction";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedTractions(){
		List l = getAllTractions();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Traction item = (Traction)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}
	
	//-- Warranty --//
	public Warranty getWarrantyById(Object id){
		if(id == null) return null;
		return (Warranty)getHibernateTemplate().get(Warranty.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllWarranties(){
		String hql = "from Warranty";
		return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedWarranties(){
		List l = getAllWarranties();
		Map m = new LinkedHashMap();
		for(int i=0; l != null && i<l.size(); i++){
			Warranty item = (Warranty)l.get(i);
			m.put(String.valueOf(item.getId()), item);
		}
		return m;
	}

}
