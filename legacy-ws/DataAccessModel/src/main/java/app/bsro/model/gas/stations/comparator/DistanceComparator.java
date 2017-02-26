package app.bsro.model.gas.stations.comparator;

import java.util.Comparator;

import app.bsro.model.gas.stations.StationPrice;

public class DistanceComparator implements Comparator<StationPrice> {

	public int compare(StationPrice a, StationPrice b) {
		if(a == null || b == null) return 0;
		Double da = new Double(a.getDistance());
		Double db = new Double(b.getDistance());
		return da.compareTo(db);
	}

}
