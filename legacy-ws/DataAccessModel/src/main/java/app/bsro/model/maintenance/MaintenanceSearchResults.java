package app.bsro.model.maintenance;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="searchResults")
public class MaintenanceSearchResults {

	private int size = 0;
	private List<MaintenanceVehicle> vehicles;
	
	public MaintenanceSearchResults(){}
	public MaintenanceSearchResults(List<MaintenanceVehicle> vehicles) {
		super();
		Collections.sort(vehicles);
		setVehicles(vehicles);
	}
	public void setVehicles(List<MaintenanceVehicle> vehicles) {
		this.vehicles = vehicles;
		if(vehicles == null) size = 0;
		else size = vehicles.size();
	}
	
	@XmlElement(name="vehicles")
	public List<MaintenanceVehicle> getVehicles() {
		return vehicles;
	}
	
	@XmlElement(name="size")
	public int getSize() {
		return size;	
	}
}
