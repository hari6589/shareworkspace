package app.bsro.model.gas.stations.comparator;

import java.util.Comparator;

import app.bsro.model.gas.stations.StationPrice;

public class PremiumPriceComparator implements Comparator<StationPrice> {

	public int compare(StationPrice a, StationPrice b) {
		if(a == null || b == null) return 0;
		Double da = new Double(a.getPremiumPrice());
		Double db = new Double(b.getPremiumPrice());
		return da.compareTo(db);
	}

}
