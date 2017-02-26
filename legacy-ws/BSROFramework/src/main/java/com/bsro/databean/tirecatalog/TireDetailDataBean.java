package com.bsro.databean.tirecatalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfrc.pojo.survey.MindshareTiresurveyDetails;
import com.bfrc.pojo.tire.jda.Display;
import com.bfrc.pojo.tire.jda2.TireGrouping;
import com.bsro.databean.BaseDataBean;

public class TireDetailDataBean extends BaseDataBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6466049664737644714L;
	@SuppressWarnings("rawtypes")
	private List sizes;
	private Display tire;
	private MindshareTiresurveyDetails surveyDetail;
	private double[] prices;
	private Double minimumPrice;
	private Double maximumPrice;
	private boolean isBestInClassTire;
	private TireGrouping tireGrouping;
	private boolean showThumbs;
	private String img60;
	private String img90;
	private String img180;
	private String imgZoom90;
	private String imgFeature;

	@SuppressWarnings("rawtypes")
	private Map specs;
	@SuppressWarnings("rawtypes")
	private Map temps;
	@SuppressWarnings("rawtypes")
	private Map treads;
	@SuppressWarnings("rawtypes")
	private Map tracts;
	@SuppressWarnings("rawtypes")
	private Set features;

	private boolean showTechnology;
	private boolean showFacts;
	private boolean showBrand;
	private boolean suppressTechnologyColumn;

	@SuppressWarnings("rawtypes")
	private List factIds;

	private String priceRange;

	private List<String> speeds;
	
	public TireGrouping getTireGrouping() {
		return this.tireGrouping;
	}

	public void setTireGrouping(TireGrouping tireGrouping) {
		this.tireGrouping = tireGrouping;
	}

	@SuppressWarnings("rawtypes")
	public List getSizes() {
		return this.sizes;
	}

	@SuppressWarnings("rawtypes")
	public void setSizes(List sizes) {
		this.sizes = sizes;
	}

	public Display getTire() {
		return this.tire;
	}

	public void setTire(Display tire) {
		this.tire = tire;
	}

	public MindshareTiresurveyDetails getSurveyDetail() {
		return this.surveyDetail;
	}

	public void setSurveyDetail(MindshareTiresurveyDetails surveyDetail) {
		this.surveyDetail = surveyDetail;
	}

	public double[] getPrices() {
		return this.prices;
	}

	public void setPrices(double[] prices) {
		this.prices = prices;
	}

	public boolean getIsBestInClassTire() {
		return this.isBestInClassTire;
	}

	public void setIsBestInClassTire(boolean isBestInClassTire) {
		this.isBestInClassTire = isBestInClassTire;
	}

	public boolean getShowThumbs() {
		return this.showThumbs;
	}

	public void setShowThumbs(boolean showThumbs) {
		this.showThumbs = showThumbs;
	}

	public String getImg60() {
		return this.img60;
	}

	public void setImg60(String img60) {
		this.img60 = img60;
	}

	public String getImg90() {
		return this.img90;
	}

	public void setImg90(String img90) {
		this.img90 = img90;
	}

	public String getImg180() {
		return this.img180;
	}

	public void setImg180(String img180) {
		this.img180 = img180;
	}

	public String getImgZoom90() {
		return this.imgZoom90;
	}

	public void setImgZoom90(String imgZoom90) {
		this.imgZoom90 = imgZoom90;
	}

	public String getImgFeature() {
		return this.imgFeature;
	}

	public void setImgFeature(String imgFeature) {
		this.imgFeature = imgFeature;
	}

	@SuppressWarnings("rawtypes")
	public Map getSpecs() {
		return this.specs;
	}

	@SuppressWarnings("rawtypes")
	public void setSpecs(Map specs) {
		this.specs = specs;
	}

	@SuppressWarnings("rawtypes")
	public Map getTemps() {
		return this.temps;
	}

	@SuppressWarnings("rawtypes")
	public void setTemps(Map temps) {
		this.temps = temps;
	}

	@SuppressWarnings("rawtypes")
	public Map getTracts() {
		return this.tracts;
	}

	@SuppressWarnings("rawtypes")
	public void setTracts(Map tracts) {
		this.tracts = tracts;
	}

	@SuppressWarnings("rawtypes")
	public Map getTreads() {
		return this.treads;
	}

	@SuppressWarnings("rawtypes")
	public void setTreads(Map treads) {
		this.treads = treads;
	}

	@SuppressWarnings("rawtypes")
	public Set getFeatures() {
		return this.features;
	}

	@SuppressWarnings("rawtypes")
	public void setFeatures(Set features) {
		this.features = features;
	}

	public boolean getShowTechnology() {
		return this.showTechnology;
	}

	public void setShowTechnology(boolean showTechnology) {
		this.showTechnology = showTechnology;
	}

	public boolean getShowFacts() {
		return this.showFacts;
	}

	public void setShowFacts(boolean showFacts) {
		this.showFacts = showFacts;
	}

	public boolean getShowBrand() {
		return this.showBrand;
	}

	public void setShowBrand(boolean showBrand) {
		this.showBrand = showBrand;
	}

	@SuppressWarnings("rawtypes")
	public List getFactIds() {
		return this.factIds;
	}

	@SuppressWarnings("rawtypes")
	public void setFactIds(List factIds) {
		this.factIds = factIds;
	}

	public String getPriceRange() {
		return this.priceRange;
	}

	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public boolean getSuppressTechnologyColumn() {
		return this.suppressTechnologyColumn;
	}

	public void setSuppressTechnologyColumn(boolean suppressTechnologyColumn) {
		this.suppressTechnologyColumn = suppressTechnologyColumn;
	}
	
	public List<String> getSpeeds(){
		return this.speeds;
	}

	public void setSpeeds(List<String> speeds){
		this.speeds = speeds;
	}


	public Double getMinimumPrice() {
		return minimumPrice;
	}

	public void setMinimumPrice(Double minimumPrice) {
		this.minimumPrice = minimumPrice;
	}

	public Double getMaximumPrice() {
		return maximumPrice;
	}

	public void setMaximumPrice(Double maximumPrice) {
		this.maximumPrice = maximumPrice;
	}
}
