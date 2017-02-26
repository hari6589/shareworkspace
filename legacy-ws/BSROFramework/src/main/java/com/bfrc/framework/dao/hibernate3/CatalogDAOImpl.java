package com.bfrc.framework.dao.hibernate3;

import java.util.*;
import java.util.Map;

import com.bfrc.*;
import com.bfrc.framework.businessdata.DataOperator;
import com.bfrc.framework.dao.*;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.pojo.store.*;
import com.bfrc.pojo.tire.jda.*;

public class CatalogDAOImpl extends HibernateDAOImpl implements CatalogDAO {
	public static String escape(String in) {
		if(in == null)
			return null;
		String out = "";
		for(int i = 0;i < in.length();i++) {
			char test = in.charAt(i);
			if(test == '\u2122' || test == '\u00BF')
				out += "&#153;";
			else if(test == '\u00AE')
				out += "&#174;";
			else out += test;
		}
		return out;
	}
	
	private StoreDAO storeDAO;
	public StoreDAO getStoreDAO() {
		return storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
	}

	private String limitDisplay(String storeType) {
		Config c = getConfig();
		String out = "";
		String type = "";
		if(storeType != null) {
			java.util.Map storeMap = storeDAO.getFullStoreMap(c);
			BfrcStoreMap store = (BfrcStoreMap)storeMap.get(storeType.trim());
			String parent = store.getParentType(); 
			if(parent == null)
				type = store.getStoreType();
			else type = parent;
			type = type.trim();
		}	
		//else if(Config.FCAC.equalsIgnoreCase(c.getSiteName()) || Config.ET.equals(c.getSiteName()) || Config.FCFC.equals(c.getSiteName()))
		//	type = Config.FCAC;
		else if(Config.TP.equalsIgnoreCase(c.getSiteName()) || Config.WWT.equalsIgnoreCase(c.getSiteName()) || Config.PWT.equalsIgnoreCase(c.getSiteName()) || Config.HT.equalsIgnoreCase(c.getSiteName()))
			type = Config.TP;
		else
			type = Config.FCAC;
		out = "'" + type + "' = tw.id.websource";
		return out;
	}
	
	public List getProductLinesInCategory(long tiregroupId) {
		return getProductLinesInCategory(tiregroupId, null);
	}
	
	public List getProductLinesInCategory(long tiregroupId, String storeType) {
		String hql = "from Display d join fetch d.tiregroup tg join fetch d.brand b "
			+ "join fetch d.theClass tc join d.configurations c "
			+ "join fetch c.tireWebsources tw "
			+ "where d.tiregroup.id = ? and " + limitDisplay(storeType)
			+ " order by tc.value, b.value, d.value, d.id";
		List l = getHibernateTemplate().find(hql, new Long(tiregroupId));
		List out = new ArrayList();
		Display old = null, d = null;
		Iterator i = l.iterator();
		while(i.hasNext()) {
			old = d;
			Object[] results = (Object[])i.next();
			d = (Display)results[0];
			if(old == null || old.getId() != d.getId()) {
				setTechnology(d);
				out.add(d);
			}
		}
		return out;
	}
	
	private void setTechnology(Display d) {
		String hql = "select distinct c.technology "
			+ "from Display d join d.configurations c "
			+ "where d.id=?";
		List l = getHibernateTemplate().find(hql, new Long(d.getId()));
		Iterator i = l.iterator();
		Technology noTech = null, sealantTech = null, unitTech = null, unitSealantTech = null, unitAqTech = null,
			unitAqIITech = null, runFlatTech = null, unitRunFlatTech = null, unitAqRunFlatTech = null;
		long noneId = 2234, sealantId = 2359, unitId = 2167, unitSealantId = 2170, unitAqId = 2168,
			unitAqIIId = 2937, runFlatId = 2169, unitRunFlatId = 2171, unitAqRunFlatId = 2292;
		while(i.hasNext()) {
			Technology t = (Technology)i.next();
			long id = t.getId();
			if(id == -1 || id == noneId) {
				noTech = t;
			} else if(id == sealantId) {
				sealantTech = t;
			} else if(id == unitId) {
				unitTech = t;
			} else if(id == unitSealantId) {
				unitSealantTech = t;
			} else if(id == unitAqId) {
				unitAqTech = t;
			} else if(id == unitAqIIId) {
				unitAqIITech = t;
			} else if(id == runFlatId) {
				runFlatTech = t;
			} else if(id == unitRunFlatId) {
				unitRunFlatTech = t;
			} else if(id == unitAqRunFlatId) {
				unitAqRunFlatTech = t;
			}
		}
		if(noTech != null)
			d.setTechnology(noTech);
		else if(sealantTech != null)
			d.setTechnology(sealantTech);
		else if(unitTech != null)
			d.setTechnology(unitTech);
		else if(unitSealantTech != null)
			d.setTechnology(unitSealantTech);
		else if(unitAqTech != null)
			d.setTechnology(unitAqTech);
		else if(unitAqIITech != null)
			d.setTechnology(unitAqIITech);
		else if(runFlatTech != null)
			d.setTechnology(runFlatTech);
		else if(unitRunFlatTech != null)
			d.setTechnology(unitRunFlatTech);
		else if(unitAqRunFlatTech != null)
			d.setTechnology(unitAqRunFlatTech);
	}

	public Display getProductLine(long displayId) {
		String hql = "from Display d join fetch d.brand left join fetch d.facts join fetch d.warranty "
			+ "left join fetch d.features join fetch d.theClass s join fetch d.tiregroup join fetch d.category "
			+ "where d.id=?";
		Display d = (Display)getOne(hql, displayId);
		if(d != null) {
			setTechnology(d);
			Set facts = d.getFacts();
			Iterator i = facts.iterator();
			while(i.hasNext()) {
				Fact f = (Fact)i.next();
				if("One Month Road Test".equals(f.getName()))
					d.setOnemonthroadtest(true);
			}
		}
		return d;
	}

	public List getProductsInLine(long displayId) {
		return getProductsInLine(displayId, null);
	}
	
	public List getProductsInLine(long displayId, String storeType, boolean POS) {
		String hql = "from Configuration c "
			+ "join fetch c.mileage join fetch c.speed join fetch c.temperature "
			+ "join fetch c.traction join fetch c.sidewall "
			+ "join fetch c.tireWebsources tw join fetch c.technology "
			+ "where c.display.id=?";
		if(!POS)
			hql += " and " + limitDisplay(storeType);
		return getHibernateTemplate().find(hql, new Long(displayId));
	}

	public Configuration getProduct(long sku) {
		String hql = "from Configuration c "
			+ "join fetch c.mileage join fetch c.speed join fetch c.temperature "
			+ "join fetch c.traction join fetch c.sidewall join fetch c.technology "
			+ "join fetch c.tireWebsources "
			+ "where c.sku=?";
		return (Configuration)getOne(hql, sku);
	}

	public byte[] getBrandImage(long id) {
		String hql = "from BrandImage t where t.brandId=?";
		BrandImage b = (BrandImage)getOne(hql, id);
		if(b != null)
			return b.getImage();
		return null;
	}

	public byte[] getFactImage(long id) {
		String hql = "from FactImage t where t.factId=?";
		FactImage f = (FactImage)getOne(hql, id);
		if(f != null)
			return f.getImage();
		return null;
	}

	public byte[] getTechnologyImage(long id) {
		String hql = "from TechnologyImage t where t.technologyId=?";
		TechnologyImage t = (TechnologyImage)getOne(hql, id);
		if(t != null)
			return t.getImage();
		return null;
	}

	public byte[] getTiregroupImage(long id) {
		String hql = "from TiregroupImage t where t.id.tiregroupId=?";
		TiregroupImage t = (TiregroupImage)getOne(hql, id);
		if(t != null)
			return t.getImage();
		return null;
	}

	public byte[] getTireImage(long id) {
		String hql = "from TireImage t where t.displayId=?";
		TireImage t = (TireImage)getOne(hql, id);
		if(t != null)
			return t.getProductImage();
		return null;
	}

	public byte[] getTireNameImage(long id) {
		String hql = "from TireImage t where t.displayId=?";
		TireImage t = (TireImage)getOne(hql, id);
		if(t != null)
			return t.getProductNameImage();
		return null;
	}

	public Fact getFact(long id) {
		String hql = "from Fact f where f.id=?";
		return (Fact)getOne(hql, id);
	}

	public Technology getTechnology(long id) {
		String hql = "from Technology t where t.id=?";
		Technology t = (Technology)getOne(hql, id);
		if(t != null) {
			String desc = t.getDescription();
			t.setDescription(CatalogDAOImpl.escape(desc));
		}
		return t;
	}

	public List getCrossSections() {
		String hql = "select distinct c.crossSection from Configuration c order by c.crossSection";
		return getHibernateTemplate().find(hql);
	}

	public List getAspectRatios(String cross) {
		String hql = "select distinct c.aspect from Configuration c";
		if(cross != null) {
			hql += " where c.crossSection=:cross order by c.aspect";
			return getHibernateTemplate().findByNamedParam(hql, "cross", cross);
		} else {
			hql += " order by c.aspect";
			return getHibernateTemplate().find(hql);
		}
	}
	
	public List getAspectRatios() {
		return getAspectRatios(null);
	}

	public List getRimSizes(String cross, String aspect) {
		String hql = "select distinct c.rimSize from Configuration c";
		if(cross!=null && aspect!=null) {
			hql += " where c.crossSection=:cross and c.aspect=:aspect order by c.rimSize";
			return getHibernateTemplate().findByNamedParam(hql, new String[]{"cross","aspect"}, new String[]{cross,aspect});
		} else {
			hql += " order by c.rimSize";
			return getHibernateTemplate().find(hql);
		}
	}
	
	public List getRimSizes() {
		return getRimSizes(null, null);
	}

	public byte[] getWarrantyPDF(long id) {
		String hql = "from WarrantyImage w where w.warrantyId=?";
		WarrantyImage w = (WarrantyImage)getOne(hql, id);
		if(w != null)
			return w.getImage();
		return null;
	}

	public List getProductsInLine(long id, boolean POS) {
		return getProductsInLine(id, null, POS);
	}

	public List getProductsInLine(long id, String storeType) {
		return getProductsInLine(id, storeType, false);
	}
	
	
	@SuppressWarnings("rawtypes")
	public List getCrossSectionList() {
		String hql = "select distinct f.id.crossSection as cross from Fitment f order by f.id.crossSection desc";
		List l1 = getHibernateTemplate().find(hql);
		hql = "select distinct c.crossSection as cross from Configuration2 c order by c.crossSection desc";
		List l2 = getHibernateTemplate().find(hql);
		for(int i=0; i<l2.size(); i++){
			if(!l1.contains(l2.get(i)))
				l1.add(l2.get(i));
		}
		return l1;
	}
	
	@SuppressWarnings("rawtypes")
	public List getAspectList(Object cross) {
		String hql = "select distinct f.id.aspect as aspect from Fitment f where f.id.crossSection=? order by f.id.aspect";
        List l1 = getHibernateTemplate().find(hql, new Object[]{cross});
        hql = "select distinct c.aspect as aspect from Configuration2 c where c.crossSection=? order by c.aspect";
		List l2 = getHibernateTemplate().find(hql, new Object[]{cross});
		for(int i=0; i<l2.size(); i++){
			if(!l1.contains(l2.get(i)))
				l1.add(l2.get(i));
		}
		return l1;
	}
	
	@SuppressWarnings("rawtypes")
	public List getRimList(Object cross, Object aspect) {
		String hql = "select distinct f.id.rimSize as rim from Fitment f where f.id.crossSection=? and f.id.aspect=? order by f.id.rimSize";
        List l1 = getHibernateTemplate().find(hql, new Object[]{cross, aspect});
        hql = "select distinct c.rimSize as rim from Configuration2 c  where c.crossSection=? and c.aspect=? order by c.rimSize";
		List l2 = getHibernateTemplate().find(hql, new Object[]{cross, aspect});
		for(int i=0; i<l2.size(); i++){
			if(!l1.contains(l2.get(i)))
				l1.add(l2.get(i));
		}
        return l1;
	}
	
	public Object getVehicleSizes(Object o) {
		Map m = (Map) o;
		String result = com.bfrc.framework.Operator.ERROR;
		String operation = (String)m.get(com.bfrc.framework.Operator.OPERATION);
		String type = (String)m.get("type");
		if("dropdown".equals(operation) && type != null) {
			boolean rim = "rim".equals(type); 
			List l = null;
			if("cross".equals(type))
				l = this.getCrossSectionList();
			else if("aspect".equals(type)) {
				if(m.get("cross")!=null) {
					l = this.getAspectList((String)m.get("cross"));
				}
			}	
			else if(rim) {
				if(m.get("cross")!=null && m.get("aspect")!=null) {
					l = this.getRimList((String)m.get("cross"), (String)m.get("aspect"));
				}
			}
				
			if(l != null) {
				l = TireCatalogUtils.sortSizes(l, type);
				List l2 = TireCatalogUtils.formatSizes(l, type);
				m.put(com.bfrc.framework.Operator.RESULT, l);
				m.put("formattedResult", l2);
			}
			result = com.bfrc.framework.Operator.SUCCESS;
		}
		return result;
	}

	public List getBestInClassTires(){
		List l = getHibernateTemplate().findByNamedQuery("getBestInClassTires",getWebSource());
		if(l==null)
			l= new ArrayList();
		return l;
	}
	public List getSiteTires(){
		List l = getHibernateTemplate().findByNamedQuery("getSiteTires",getWebSource());
		if(l==null)
			l= new ArrayList();
		return l;
	}
	private String getWebSource() {
		Config c = getConfig();
		String type = Config.FCAC;
		if(Config.TP.equalsIgnoreCase(c.getSiteName()) || Config.WWT.equalsIgnoreCase(c.getSiteName()) || Config.HT.equalsIgnoreCase(c.getSiteName()))
			type = Config.TP;
		return type;
	}
}
