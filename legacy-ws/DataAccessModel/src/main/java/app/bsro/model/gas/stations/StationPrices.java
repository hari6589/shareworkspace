package app.bsro.model.gas.stations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StationPrices implements Serializable {

	private static final long serialVersionUID = 6142747351218610802L;
	private String latitude;
	private String longitude;
	private int count;
	private String grade;
	
	public enum Params {
		GRADE("grade"),
		SEARCH_RADIUS("radius");
		
		private String param;
		private Params(String param) {
			this.param = param;
		}
		
		public String getParam() {
			return param;
		}
	}
	
	
	private List<StationPrice> stations = new ArrayList<StationPrice>();
	
	public void setStations(List<StationPrice> stations) {
		this.stations = stations;
	}
	public List<StationPrice> getStations() {
		return stations;
	}
	
	public void addStation(StationPrice station) {
		stations.add(station);
	}

	public int getCount() {
		return stations==null?0:stations.size();
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getGrade() {
		return grade;
	}
}
