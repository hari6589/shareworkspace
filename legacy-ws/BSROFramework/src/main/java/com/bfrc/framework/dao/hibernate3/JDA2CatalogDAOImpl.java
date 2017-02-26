package com.bfrc.framework.dao.hibernate3;

import java.util.*;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.bfrc.*;
import com.bfrc.framework.dao.*;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.CacheService;
import com.bfrc.pojo.realestate.RealestateRegionContact;
import com.bfrc.pojo.store.*;
import com.bfrc.pojo.tire.jda2.*;
import com.bfrc.pojo.tire.jda2.Class;

public class JDA2CatalogDAOImpl extends HibernateDAOImpl implements JDA2CatalogDAO {
	
	private static final long checkCacheTimeOut=30*60*1000;
	//-- brand --//
	public Brand getBrandById(Object id){
		if(id == null) return null;
		return (Brand)getHibernateTemplate().get(Brand.class,new Long(id.toString()));
	}
	
	@SuppressWarnings("unchecked")
	public List<Brand> getAllBrandsInAlphabeticalOrder(){
		String hql = "from Brand2 order by value asc";
		return (List<Brand>)getHibernateTemplate().find(hql);
	}
	
	@SuppressWarnings("unchecked")
	public List getAllBrands(){
		String hql = "from Brand2";
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
		String hql = "from Category2";
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
	
	//-- class --//
	public Class getClassById(Object id){
	if(id == null) return null;
	return (Class)getHibernateTemplate().get(Class.class,new Long(id.toString()));
	}
	@SuppressWarnings("unchecked")
	public List getAllClasses(){
	String hql = "from Class2";
	return getHibernateTemplate().find(hql);
	}
	@SuppressWarnings("unchecked")
	public Map getMappedClasses(){
	List l = getAllClasses();
	Map m = new LinkedHashMap();
	for(int i=0; l != null && i<l.size(); i++){
	Class item = (Class)l.get(i);
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
		String hql = "from Configuration2";
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
		//add cache here for performance improvement
		String cacheKey = "_JDA2DAO_GETALLDISPLAYS_";
		boolean resetCache = CacheService.getInstance().needResetCache(cacheKey,checkCacheTimeOut);
		List l = (List)CacheService.getInstance().get(cacheKey);
		if(resetCache){
			String hql = "from Display2 d";
			if(Config.FIVESTAR.equals(getConfig().getSiteName())){
				hql += " where d.brandId = 241 ";
			}
			if(l != null) //reuse object for better performance
				l.clear();
			else
				l = new ArrayList();
			l.addAll(getHibernateTemplate().find(hql));
			CacheService.getInstance().add("_JDA2DAO_GETALLDISPLAYS_", l);
		}
		return l;
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
		String hql = "from Fact2";
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
		String hql = "from Feature2";
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
		String hql = "from Mileage2";
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
		String hql = "from Segment2";
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
		String hql = "from Sidewall2";
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
		String hql = "from Speed2";
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
		String hql = "from Technology2";
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
		String hql = "from Temperature2";
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
		String hql = "from Tiregroup2";
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
		String hql = "from Traction2";
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
		String hql = "from Warranty2";
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
	
	public List getAllTireGrouping(){
		Session s = getSession();
		try{
		String hql = " SELECT tg.* from BFS_TIRE_CATALOG_JDA.TIRE_GROUPING tg, BFS_TIRE_CATALOG_JDA.DISPLAY d "
			+ " where d.id =  tg.display_id ";
		if(Config.FIVESTAR.equals(getConfig().getSiteName())){
			hql += " and d.brand_id = 241 ";
		}
		SQLQuery query  = s.createSQLQuery(hql).addEntity(com.bfrc.pojo.tire.jda2.TireGrouping.class);
		return query.list();
		}finally {
			//s.close();
			this.releaseSession(s);
		}
	}
	
	public List getDisplaysByIds(List ids){
		if(ids == null  || ids.isEmpty() )
			return null;
		List l = getHibernateTemplate().findByNamedParam("from Display2 d where d.id in (:ids) order by d.value, d.id",
				"ids",
				ids);
		return l;
		
	}
}
