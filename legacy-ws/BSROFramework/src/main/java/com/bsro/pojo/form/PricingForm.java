package com.bsro.pojo.form;

import java.io.Serializable;

public class PricingForm extends UserForm implements Serializable {
		
		private static final long serialVersionUID = 1L;
		private String storeNumber;
		private String method;
		private String category;
		private String segment;
		private String cross;
		private String aspect;
		private String rim;
		private String zip;
		private String allBrands;
		private String geoPoint;
		public String getStoreNumber() {
			return storeNumber;
		}
		public void setStoreNumber(String storeNumber) {
			this.storeNumber = storeNumber;
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getSegment() {
			return segment;
		}
		public void setSegment(String segment) {
			this.segment = segment;
		}
		public String getCross() {
			return cross;
		}
		public void setCross(String cross) {
			this.cross = cross;
		}
		public String getAspect() {
			return aspect;
		}
		public void setAspect(String aspect) {
			this.aspect = aspect;
		}
		public String getRim() {
			return rim;
		}
		public void setRim(String rim) {
			this.rim = rim;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getAllBrands() {
			return allBrands;
		}
		public void setAllBrands(String allBrands) {
			this.allBrands = allBrands;
		}
		public String getGeoPoint() {
			return geoPoint;
		}
		public void setGeoPoint(String geoPoint) {
			this.geoPoint = geoPoint;
		}

}
