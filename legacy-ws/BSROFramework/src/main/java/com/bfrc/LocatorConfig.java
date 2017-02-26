package com.bfrc;

public class LocatorConfig extends Base {
	private Integer mapWidth = new Integer(595);
	private Integer mapHeight = new Integer(450);
	private String esriGeoSource = "auto";
	private String esriMapSource = "ArcWeb:TA.Streets.US";
	private String esriUserIconSource = "ArcWeb:User.Defined.Icons";
	private String esriUserIconPrefix = "arcweb_";
	private Integer zoomLevel = new Integer(7);
	private Integer storeZoomLevel = new Integer(1);
	private Integer zoomTick = new Integer(16);
	private Float[] zoomLevels = {new Float(0.25), new Float(0.5), new Float(1), new Float(2.5), new Float(5), new Float(10),
		new Float(25), new Float(50), new Float(100), new Float(250), new Float(500)};
	private Integer radius = new Integer(3);
	private Integer[] radii = {new Integer(10), new Integer(25), new Integer(50), new Integer(100), new Integer(150), new Integer(250)};
	private Integer storesPerPage = new Integer(6);
	private Integer minStoresPerPage = new Integer(3);
	private Integer maxStoresPerPage = new Integer(8);
	private Integer faneuilStoresPerPage = new Integer(6);
	private String faneuilUrl = "/locate/fresult.jsp?store=";
	private String firstlogicHostname = "mktfl-ak-bfr";
	private String bingMapsAjaxControlKey = "";
	private String bingMapsStaticImageryKey = "";
	
	public String getEsriGeoSource() {
		return this.esriGeoSource;
	}
	public void setEsriGeoSource(String esriGeoSource) {
		this.esriGeoSource = esriGeoSource;
	}
	public String getEsriMapSource() {
		return this.esriMapSource;
	}
	public void setEsriMapSource(String esriMapSource) {
		this.esriMapSource = esriMapSource;
	}
	public Integer getFaneuilStoresPerPage() {
		return this.faneuilStoresPerPage;
	}
	public void setFaneuilStoresPerPage(Integer faneuilStoresPerPage) {
		this.faneuilStoresPerPage = faneuilStoresPerPage;
	}
	public String getFirstlogicHostname() {
		return this.firstlogicHostname;
	}
	public void setFirstlogicHostname(String firstlogicHostname) {
		this.firstlogicHostname = firstlogicHostname;
	}
	public Integer getMapHeight() {
		return this.mapHeight;
	}
	public void setMapHeight(Integer mapHeight) {
		this.mapHeight = mapHeight;
	}
	public Integer getMapWidth() {
		return this.mapWidth;
	}
	public void setMapWidth(Integer mapWidth) {
		this.mapWidth = mapWidth;
	}
	public Integer getMaxStoresPerPage() {
		return this.maxStoresPerPage;
	}
	public void setMaxStoresPerPage(Integer maxStoresPerPage) {
		this.maxStoresPerPage = maxStoresPerPage;
	}
	public Integer getMinStoresPerPage() {
		return this.minStoresPerPage;
	}
	public void setMinStoresPerPage(Integer minStoresPerPage) {
		this.minStoresPerPage = minStoresPerPage;
	}
	public Integer getRadius() {
		return this.radius;
	}
	public void setRadius(Integer radius) {
		this.radius = radius;
	}
	public Integer getStoresPerPage(String app) {
		if(Config.FANEUIL.equals(app))
			return getFaneuilStoresPerPage();
		return this.storesPerPage;
	}
	public void setStoresPerPage(Integer storesPerPage) {
		this.storesPerPage = storesPerPage;
	}
	public Integer getZoomLevel() {
		return this.zoomLevel;
	}
	public void setZoomLevel(Integer zoomLevel) {
		this.zoomLevel = zoomLevel;
	}
	public String getFaneuilUrl() {
		return this.faneuilUrl;
	}
	public void setFaneuilUrl(String faneuilUrl) {
		this.faneuilUrl = faneuilUrl;
	}
	public Integer[] getRadii() {
		return this.radii;
	}
	public void setRadii(Integer[] radii) {
		this.radii = radii;
	}
	public Float[] getZoomLevels() {
		return this.zoomLevels;
	}
	public void setZoomLevels(Float[] zoomLevels) {
		this.zoomLevels = zoomLevels;
	}
	public Integer getStoreZoomLevel() {
		return this.storeZoomLevel;
	}
	public void setStoreZoomLevel(Integer storeZoomLevel) {
		this.storeZoomLevel = storeZoomLevel;
	}
	public Integer getZoomTick() {
		return this.zoomTick;
	}
	public void setZoomTick(Integer zoomTick) {
		this.zoomTick = zoomTick;
	}
	public String getEsriUserIconPrefix() {
		return this.esriUserIconPrefix;
	}
	public void setEsriUserIconPrefix(String esriUserIconPrefix) {
		this.esriUserIconPrefix = esriUserIconPrefix;
	}
	public String getEsriUserIconSource() {
		return this.esriUserIconSource;
	}
	public void setEsriUserIconSource(String esriUserIconSource) {
		this.esriUserIconSource = esriUserIconSource;
	}
	public String getBingMapsAjaxControlKey() {
		return bingMapsAjaxControlKey;
	}
	public void setBingMapsAjaxControlKey(String bingMapsAjaxControlKey) {
		this.bingMapsAjaxControlKey = bingMapsAjaxControlKey;
	}
	public String getBingMapsStaticImageryKey() {
		return bingMapsStaticImageryKey;
	}
	public void setBingMapsStaticImageryKey(String bingMapsStaticImageryKey) {
		this.bingMapsStaticImageryKey = bingMapsStaticImageryKey;
	}
	
}
