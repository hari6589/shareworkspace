package com.bsro.service.tirecatalog;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.framework.dao.CatalogDAO;
import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.framework.dao.PricingDAO;
import com.bfrc.framework.dao.SurveyDAO;
import com.bfrc.framework.util.StringUtils;
import com.bfrc.framework.util.TireCatalogUtils;
import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda.Configuration;
import com.bfrc.pojo.tire.jda.Display;
import com.bfrc.pojo.tire.jda.Fact;
import com.bfrc.pojo.tire.jda.Technology;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bfrc.security.Encode;
import com.bsro.databean.tirecatalog.TireDetailDataBean;
import com.bsro.webservice.BSROWebserviceServiceImpl;

@Service("tireCatalogService")
public class TireCatalogServiceImpl extends BSROWebserviceServiceImpl implements
		TireCatalogService {

	@Autowired
	CatalogDAO catalogDAO;

	@Autowired
	JDA2CatalogDAO jda2CatalogDAO;

	@Autowired
	PricingDAO pricingDAO;
	
	@Autowired
	SurveyDAO surveyDAO;
	
	public TireDetailDataBean getTireDetailData(String tireBrand,
			String tireName, String tireId, String tireImgDir, String imgUri) {
		TireDetailDataBean dataBean = new TireDetailDataBean();
		List allTires = jda2CatalogDAO.getAllDisplays();
		Map mappedBrands = jda2CatalogDAO.getMappedBrands();
		List bestInClassTires = catalogDAO.getBestInClassTires();
		Map mappedBestInClassTires = TireCatalogUtils
				.getMappedBestInClassTires(bestInClassTires);
		java.util.Map name_id = new HashMap();
		for (int i = 0; i < allTires.size(); i++) {
			com.bfrc.pojo.tire.jda2.Display tire = (com.bfrc.pojo.tire.jda2.Display) allTires
					.get(i);
			com.bfrc.pojo.tire.jda2.Brand brand = (com.bfrc.pojo.tire.jda2.Brand) mappedBrands
					.get(tire.getBrandId() + "");
			String name = tire.getValue();
			long id = tire.getId();
			if (!"Y".equals(tire.getGenerateCatalogPage()))
				id = 0;
			String brandName = brand.getValue();
			String s = StringUtils.nameFilter(brandName + "|" + name)
					.toLowerCase();
			name_id.put(s, id + "");
		}
		List sizes = null;
		Display tire = null;
		String idfromName = null;
		if (!com.bfrc.framework.util.ServerUtil.isNullOrEmpty(tireBrand)
				&& !com.bfrc.framework.util.ServerUtil.isNullOrEmpty(tireName)) {
			String b = tireBrand;
			String n = tireName;
			String ss = StringUtils.nameFilter(b + "|" + n).toLowerCase();
			idfromName = (String) name_id.get(ss);
		}
		boolean isBestInClassTire = false;
		if (tire == null) {
			// -- check a request from jsp --//
			if (idfromName != null) {
				try {
					tire = catalogDAO.getProductLine(Integer
							.parseInt(idfromName));
					sizes = (List) catalogDAO.getProductsInLine(Integer
							.parseInt(idfromName));
					dataBean.setSizes(sizes);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else if (!com.bfrc.framework.util.ServerUtil
					.isNullOrEmpty(tireId)) {
				String strId = Encode.html(tireId);
				try {
					tire = catalogDAO.getProductLine(Integer.parseInt(strId));
					sizes = (List) catalogDAO.getProductsInLine(Integer
							.parseInt(strId));
					dataBean.setSizes(sizes);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			if (tire == null) {
				return dataBean;
				// out.write("<p>Sorry, there is no information available on the requested product.</p>");
				// return;
			}
		}
		dataBean.setTire(tire);
		// --- get new mappings here --//
		List groupings = jda2CatalogDAO.getAllTireGrouping();
		Map mappedGroupings = TireCatalogUtils.getMappedTireGrouping(groupings);
		TireGrouping tireGrouping = (TireGrouping) mappedGroupings.get(tire
				.getId());
		dataBean.setTireGrouping(tireGrouping);

		isBestInClassTire = TireCatalogUtils.isBestInClassTire(
				mappedBestInClassTires, tire.getId());
		dataBean.setIsBestInClassTire(isBestInClassTire);

		double[] prices = pricingDAO.getTirePricingPriceRange(tire.getId());
		dataBean.setPrices(prices);
		
		// prices is an array, 0 is minimum price, 1 is maximum price
		if (prices != null) {
			if (prices.length > 0) {
				dataBean.setMinimumPrice(prices[0]);
				
				if (prices.length > 1) {
					dataBean.setMaximumPrice(prices[1]);
				}
			}
		}

		MindshareTiresurveyDetails detail = null;
		if (isBestInClassTire) {
			Map mappedSurveyData = surveyDAO
					.getMappedMindshareTiresurveyDetails();
			detail = (MindshareTiresurveyDetails) mappedSurveyData.get(tire
					.getId());
		}
		dataBean.setSurveyDetail(detail);

		sizes = dataBean.getSizes();
		if (sizes == null)
			return dataBean;

		Map temps = new HashMap(), treads = new HashMap(), tracts = new HashMap();
		Map specs = new TreeMap();
		boolean suppressTechnologyColumn = true;
		List<String> speeds = new ArrayList();
		for (int i = 0; i < sizes.size(); i++) {
			Configuration c = (Configuration) sizes.get(i);
			if(c.getTechnology() != null && !"none".equalsIgnoreCase(c.getTechnology().getValue()))
				suppressTechnologyColumn = false;
			if(c.getTemperature() != null && !"N/A".equalsIgnoreCase(c.getTemperature().getValue()))				
				temps.put(c.getTemperature().getValue(), "");
			treads.put(c.getTreadwear(), "");
			if(c.getTraction() != null && !"N/A".equalsIgnoreCase(c.getTraction().getValue()))				
				tracts.put(c.getTraction().getValue(), "");
			specs.put(c, "");
			if(!speeds.contains(c.getSpeed().getValue()))
	             speeds.add(c.getSpeed().getValue());
		}
		if (temps.isEmpty())
			temps.put("N/A", "");
		if (tracts.isEmpty())
			tracts.put("N/A", "");
		dataBean.setSpeeds(speeds);
		dataBean.setSuppressTechnologyColumn(suppressTechnologyColumn);
		dataBean.setTemps(temps);
		dataBean.setSpecs(specs);
		dataBean.setTreads(treads);
		dataBean.setTracts(tracts);

		Set features = tire.getFeatures();
		if (features != null && features.size() > 0) {
			dataBean.setFeatures(features);
		}
		List factIds = new ArrayList();
		Set facts = tire.getFacts();
		Iterator it = facts.iterator();
		while (it.hasNext()) {
			Fact currentFact = (Fact) it.next();
			if (!"One Month Road Test".equals(currentFact.getName())
					&& !"none".equalsIgnoreCase(currentFact.getName()))
				factIds.add(new Long(currentFact.getId()));
		}
		dataBean.setFactIds(factIds);

		// int techSize = techIds.size();
		Technology technology = tire.getTechnology();
		boolean showTechnology = (tire.getTechnology() != null && !"none"
				.equalsIgnoreCase(tire.getTechnology().getValue()));
		boolean showFacts = factIds.size() > 0;
		boolean showBrand = !"Winterforce".equals(tire.getBrand().getValue());
		dataBean.setShowBrand(showBrand);
		dataBean.setShowTechnology(showTechnology);
		dataBean.setShowFacts(showFacts);

		String priceRange = "";
		if (prices != null) {
			//priceRange = "$" + StringUtils.formatDecimal(prices[0], "0.00")
			//		+ " - $" + StringUtils.formatDecimal(prices[1], "0.00")
			//		+ " &#42;ea";
			//if (prices[0] <= 0)
				priceRange = "Up to $"
						+ StringUtils.formatDecimal(prices[1], "0.00")
						+ " &#42;ea";
		}
		dataBean.setPriceRange(priceRange);

		return dataBean;
	}

}
