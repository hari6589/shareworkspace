package app.bsro.compare.oil;

import java.util.Comparator;

import app.bsro.model.oil.OilChangePackage;

public class OilChangePackageComparator implements Comparator<OilChangePackage> {

	public int compare(OilChangePackage o1, OilChangePackage o2) {
		Integer o1OilTypeFriendlyId = null;
		Integer o2OilTypeFriendlyId = null;
		if (o1 != null && o1.getOilType() != null && o1.getOilType().getSequence() != null) {
			o1OilTypeFriendlyId = o1.getOilType().getSequence();
		}
		if (o2 != null && o2.getOilType() != null && o2.getOilType().getSequence() != null) {
			o2OilTypeFriendlyId = o2.getOilType().getSequence();
		}
		
		if (o1OilTypeFriendlyId != null && o2OilTypeFriendlyId != null) {
			return o1OilTypeFriendlyId.compareTo(o2OilTypeFriendlyId);
		} else if (o1OilTypeFriendlyId == null && o2OilTypeFriendlyId != null) {
			// count null as "less"
			return -1;
		} else if (o2OilTypeFriendlyId == null && o1OilTypeFriendlyId != null) {
			// count null as "less"
			return +1;
		}
		
		return 0;
	}

}
