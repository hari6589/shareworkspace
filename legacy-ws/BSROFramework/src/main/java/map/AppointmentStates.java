package map;

import java.util.*;

import org.json.*;

import com.bfrc.Bean;

import com.bfrc.framework.dao.*;

public class AppointmentStates extends LinkedHashMap implements Bean {

	StoreDAO storeDAO;

	public StoreDAO getStoreDAO() {
		return this.storeDAO;
	}

	public void setStoreDAO(StoreDAO storeDAO) {
		this.storeDAO = storeDAO;
		initialize();
	}
	
	public void initialize() {
		Map m = new HashMap();
		m.put("partner", "");
		List l = this.storeDAO.getStates(m);
		Iterator i = l.iterator();
		while(i.hasNext()) {
			Object[] o = (Object[])i.next();
			this.put(o[1], o[0]);
		}
	}
	
	public String toJSON() {
		JSONArray list = new JSONArray();
		Iterator i = this.keySet().iterator();
		while(i.hasNext()) {
			JSONArray item = new JSONArray();
			Object o = i.next();
			item.put(o);
			item.put(this.get(o));
			list.put(item);
		}
		return list.toString();
	}
}
