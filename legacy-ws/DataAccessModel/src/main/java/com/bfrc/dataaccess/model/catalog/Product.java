package com.bfrc.dataaccess.model.catalog;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author smoorthy
 *
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
public class Product {
	
	private Map<String, String> display;	
	private Map ratings;
	private Set specs;
	private Set temps;
	private Set treads;
	private Set tracts;
	private Set features;
	private List<String> speeds;
	private String[] prices;
	private boolean isBestInClassTire;
	private String img60;
	private String img90;
	private String img180;
	private String imgZoom90;
	private String imgFeature;
	
	public Map<String, String> getDisplay() {
		return display;
	}

	public void setDisplay(Map<String, String> display) {
		this.display = display;
	}

	public Map getRatings() {
		return ratings;
	}
	
	public void setRatings(Map ratings) {
		this.ratings = ratings;
	}
	
	public Set getSpecs() {
		return specs;
	}
	
	public void setSpecs(Set specs) {
		this.specs = specs;
	}
	
	public Set getTemps() {
		return temps;
	}
	
	public void setTemps(Set temps) {
		this.temps = temps;
	}
	
	public Set getTreads() {
		return treads;
	}
	
	public void setTreads(Set treads) {
		this.treads = treads;
	}
	
	public Set getTracts() {
		return tracts;
	}
	
	public void setTracts(Set tracts) {
		this.tracts = tracts;
	}
	
	public Set getFeatures() {
		return features;
	}
	
	public void setFeatures(Set features) {
		this.features = features;
	}

	public List<String> getSpeeds() {
		return speeds;
	}

	public void setSpeeds(List<String> speeds) {
		this.speeds = speeds;
	}

	public String[] getPrices() {
		return prices;
	}

	public void setPrices(String[] prices) {
		this.prices = prices;
	}

	public boolean getIsBestInClassTire() {
		return isBestInClassTire;
	}

	public void setBestInClassTire(boolean isBestInClassTire) {
		this.isBestInClassTire = isBestInClassTire;
	}

	public String getImg60() {
		return img60;
	}

	public void setImg60(String img60) {
		this.img60 = img60;
	}

	public String getImg90() {
		return img90;
	}

	public void setImg90(String img90) {
		this.img90 = img90;
	}

	public String getImg180() {
		return img180;
	}

	public void setImg180(String img180) {
		this.img180 = img180;
	}

	public String getImgZoom90() {
		return imgZoom90;
	}

	public void setImgZoom90(String imgZoom90) {
		this.imgZoom90 = imgZoom90;
	}

	public String getImgFeature() {
		return imgFeature;
	}

	public void setImgFeature(String imgFeature) {
		this.imgFeature = imgFeature;
	}

}
