package tirequote;
import java.util.Iterator;

import org.json.JSONArray;

public class States extends java.util.LinkedHashMap<String, String> {
	public States() {
    this.put("AL","Alabama");
    this.put("AK","Alaska");
    this.put("AZ","Arizona");
    this.put("AR","Arkansas");
    this.put("CA","California");
    this.put("CO","Colorado");
    this.put("CT","Connecticut");
    this.put("DE","Delaware");
    this.put("DC","District of Columbia");    
    this.put("FL","Florida");
    this.put("GA","Georgia");
    this.put("HI","Hawaii");    
    this.put("ID","Idaho");
    this.put("IL","Illinois");
    this.put("IN","Indiana");
    this.put("IA","Iowa");    
    this.put("KS","Kansas");
    this.put("KY","Kentucky");    
    this.put("LA","Louisiana");
    this.put("ME","Maine");
    this.put("MD","Maryland");
    this.put("MA","Massachusetts");
    this.put("MI","Michigan");
    this.put("MN","Minnesota");
    this.put("MS","Mississippi");
    this.put("MO","Missouri");
    this.put("MT","Montana");    
    this.put("NE","Nebraska");
    this.put("NV","Nevada");
    this.put("NH","New Hampshire");
    this.put("NJ","New Jersey");
    this.put("NM","New Mexico");
    this.put("NY","New York");
    this.put("NC","North Carolina");
    this.put("ND","North Dakota");    
    this.put("OH","Ohio");
    this.put("OK","Oklahoma");
    this.put("OR","Oregon");    
    this.put("PA","Pennsylvania");
    this.put("RI","Rhode Island");    
    this.put("SC","South Carolina");
    this.put("SD","South Dakota");    
    this.put("TN","Tennessee");
    this.put("TX","Texas");    
    this.put("UT","Utah");    
    this.put("VT","Vermont");
    this.put("VA","Virginia");    
    this.put("WA","Washington");
    this.put("WV","West Virginia");
    this.put("WI","Wisconsin");
    this.put("WY","Wyoming");    
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
