/**
 * 
 */
package app.bsro.model.appointment;

import java.util.Comparator;

/**
 * @author schowdhu
 *
 */
public class AppointmentServiceComparator implements Comparator<AppointmentServiceCategory>{

	public int compare(AppointmentServiceCategory o1, AppointmentServiceCategory o2) {
		Integer sortOrd1 = o1.getSortOrder();
		Integer sortOrd2 = o2.getSortOrder();
		
		if(sortOrd1 == null)
			return -1;
		else if(sortOrd2 == null)
			return 1;	
		else if(sortOrd1 < sortOrd2) 
			return -1;
		else if (sortOrd1 == sortOrd2) 
			return 0;
		else 
			return 1;
	}
	
}
