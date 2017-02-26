package com.bfrc.framework.util;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.bfrc.framework.dao.JDA2CatalogDAO;
import com.bfrc.pojo.tire.Tire;
import com.bfrc.pojo.tire.TireIcon;
import com.bfrc.pojo.tire.jda2.*;
import com.bfrc.*;


/**
 * @author css
 *
 */
/**
 * @author css
 *
 */
/**
 * @author css
 *
 */
public class TireCatalogUtils {
	public static final String CATEGORY= "category";
	public static final String SEGMENT= "segment";
	public static final String CLASS= "class_";
	public static final String TIRE_VEHICLE= "tireVehicle";
	public static final String TIRE_CLASS= "tireClass";
	public static final String TIRE_TYPE= "tireType";
	/**
	 * @param propertyName
	 * @param groupings
	 * @return
	 */
	public static Map getMappedNumbers(String propertyName, List groupings){
        Map m = new HashMap();
        List dids = new ArrayList();
       
        for(int i=0; i< groupings.size();i++){
          TireGrouping tg = (TireGrouping)groupings.get(i);
          if(!dids.contains(tg.getDisplayId())){
              dids.add(tg.getDisplayId());
              String value = tg.getTireClass();
              if(TIRE_CLASS.equalsIgnoreCase(propertyName)){
                  value = tg.getTireClass();
              }else if(TIRE_VEHICLE.equalsIgnoreCase(propertyName)){
                  value = tg.getTireVehicle();
              }else if(TIRE_TYPE.equalsIgnoreCase(propertyName)){
                  value = tg.getTireType();
              }else if(SEGMENT.equalsIgnoreCase(propertyName)){
                  value = tg.getSegment();
              }else if(CATEGORY.equalsIgnoreCase(propertyName)){
                  value = tg.getCategory();
              }else if(CLASS.equalsIgnoreCase(propertyName)){
                  value = tg.getClass_();
              }
              if(!m.containsKey(value)){
                  m.put(value, new Integer(1));
              }else{
                  Integer cnt = (Integer)m.get(value);
                  m.put(value, cnt.intValue()+1);
              }
          }
        }
        return m;
   }

   /**
 * @param propertyName
 * @param groupings
 * @return
 */
    public static List getTireGroupingNames(String propertyName, List groupings){
        
        List names = new ArrayList();

        for(int i=0; i< groupings.size();i++){
          TireGrouping tg = (TireGrouping)groupings.get(i);
          String value = tg.getTireClass();
          if(TIRE_CLASS.equalsIgnoreCase(propertyName)){
              value = tg.getTireClass();
          }else if(TIRE_VEHICLE.equalsIgnoreCase(propertyName)){
              value = tg.getTireVehicle();
          }else if(TIRE_TYPE.equalsIgnoreCase(propertyName)){
              value = tg.getTireType();
          }else if(SEGMENT.equalsIgnoreCase(propertyName)){
              value = tg.getSegment();
          }else if(CATEGORY.equalsIgnoreCase(propertyName)){
              value = tg.getCategory();
          }else if(CLASS.equalsIgnoreCase(propertyName)){
              value = tg.getClass_();
          }
          if(!names.contains(value)){
              names.add(value); 
          }
        }
        if(TIRE_VEHICLE.equalsIgnoreCase(propertyName)){
        	List orderedNames = new ArrayList();
        	orderedNames.add("Car & Minivan");
        	orderedNames.add("Performance");
        	orderedNames.add("SUV/CUV");
        	orderedNames.add("Light Truck");
        	orderedNames.add("Winter");
        	for(int i =0; i< names.size(); i++){
        		String s = (String)names.get(i);
        		if(!orderedNames.contains(s)){
        			orderedNames.add(s);
        		}
        	}
        	return orderedNames;
        }
        Collections.sort(names);
        return names;
   }
    
  
    public static List getVehicleTypeClassNames(String vehicleType, List groupings){
        List names = new ArrayList();
        for(int i=0; i< groupings.size();i++){
          TireGrouping tg = (TireGrouping)groupings.get(i);
          String vt = tg.getTireVehicle();
          if(vt.equalsIgnoreCase(vehicleType) || StringUtils.categoryNameFilter(vt, "-").equalsIgnoreCase(vehicleType)){
        	  
              String value = tg.getClass_();
	          if(!names.contains(value)){
	              names.add(value); 
	          }
          }
        }
        Collections.sort(names);
        return names;
   }
   
   /**
 * @param request
 * @return
 */
    public static Map getMappedTireNames(HttpServletRequest request){
       ServletContext application = request.getSession().getServletContext();
       Config thisconfig = (Config)Config.locate(application,"config");
       JDA2CatalogDAO jda2CatalogDAO = (JDA2CatalogDAO)Config.locate(application,"jda2CatalogDAO");
       List l2 = jda2CatalogDAO.getAllDisplays();
       Map mb = jda2CatalogDAO.getMappedBrands();
       Map m = new LinkedHashMap();
       Map temp = new HashMap();
       List rawNames = new ArrayList();
       for(int i=0; i< l2.size(); i++){
           Display tire = (Display)l2.get(i);
           String tireName = tire.getValue();
           String brand = ((Brand)mb.get(String.valueOf(tire.getBrandId()))).getValue();
           if(!tireName.toLowerCase().startsWith(brand.toLowerCase())){
               tireName = brand + " "+tireName;
           }
           rawNames.add(tireName);
           temp.put(tireName, tire.getId());
       }
       Collections.sort(rawNames);
       for(int i=0; i< rawNames.size(); i++){
           String tireName = (String)rawNames.get(i);
           m.put(temp.get(tireName), tireName);
       }
       return m;
   }
   
   /**
 * @param groupings
 * @param propertyName
 * @param name
 * @return
 */
    public static List getTireGroupingDisplayIds(List groupings, String propertyName, String name){
       
       List ids = new ArrayList();

       for(int i=0; i< groupings.size();i++){
         TireGrouping tg = (TireGrouping)groupings.get(i);
         String value = tg.getTireClass();
         if(TIRE_CLASS.equalsIgnoreCase(propertyName)){
             value = tg.getTireClass();
         }else if(TIRE_VEHICLE.equalsIgnoreCase(propertyName)){
             value = tg.getTireVehicle();
         }else if(TIRE_TYPE.equalsIgnoreCase(propertyName)){
             value = tg.getTireType();
         }else if(SEGMENT.equalsIgnoreCase(propertyName)){
             value = tg.getSegment();
         }else if(CATEGORY.equalsIgnoreCase(propertyName)){
             value = tg.getCategory();
         }else if(CLASS.equalsIgnoreCase(propertyName)){
             value = tg.getClass_();
         }
         if(value.equalsIgnoreCase(name)){
             if(!ids.contains(tg.getDisplayId())) {
            	 ids.add(tg.getDisplayId());
             }
         }
       }
       return ids;
  }
    
    public static List getTireGroupingDisplayIds(List groupings, String propertyName, String name, String subclass){
        
        List ids = new ArrayList();
        if(name == null || subclass == null)
        	return ids;
        for(int i=0; i< groupings.size();i++){
          TireGrouping tg = (TireGrouping)groupings.get(i);
          String value = null;
          String subvalue = null;
          if("VehicleTypeClass".equalsIgnoreCase(propertyName)){
              value = tg.getTireVehicle();
              subvalue = tg.getClass_();
          }
          if(name.equalsIgnoreCase(value) && subclass.equalsIgnoreCase(subvalue)){
        	  
              if(!ids.contains(tg.getDisplayId())) {
             	 ids.add(tg.getDisplayId());
              }
          }
        }
        return ids;
   }
   
  /*
   * get the mapped grouping in the pair of displayid<->TireGrouping
   * @param groupings
   * @return mapped Groupins
  */
/**
 * @param groupings
 * @return
 */
public static Map getMappedTireGrouping(List groupings){
       Map m = new HashMap();
       for(int i=0; i< groupings.size();i++){
         TireGrouping tg = (TireGrouping)groupings.get(i);
         m.put(tg.getDisplayId(), tg);
       }
       return m;
  }

	public static List sortSizes(List l, String car) {
		boolean rim = false;
		if("rim".equalsIgnoreCase(car))
			rim = true;
		List out = new ArrayList();
		TreeSet metric = new TreeSet(), flotation = new TreeSet(), alpha = new TreeSet();
		Iterator i = l.iterator();
		while(i.hasNext()) {
			String size = (String)i.next();
			if(rim) {
				if("0".equals(size))
					continue;
				if(size.length() == 2)
					size += "00";
				metric.add(size);
			} else try {
				if("NONE".equals(size) && !"aspect".equalsIgnoreCase(car))
					continue;
				Integer in = Integer.valueOf(size);
				if(in.compareTo(new Integer(500)) < 0)
					metric.add(in);
				else
					flotation.add(in);
			} catch(NumberFormatException ex) {
				alpha.add(size);
			}
		}
		if(rim) {
			i = metric.iterator();
			while(i.hasNext()) {
				String value = (String)i.next();
				if(value.length() >= 4 && "00".equals(value.substring(2, 4)))//fix out of bound error
					value = value.substring(0, 2);
				out.add(value);
			}
		} else {
			//--if cross put 145,155, etc on the first choice--//
			if("cross".equalsIgnoreCase(car)){
				i = metric.iterator();
				while(i.hasNext()){
					Integer size = (Integer)i.next();
					if(size.intValue() > 100)
					 out.add(size);
				}
				i = metric.iterator();
				while(i.hasNext()){
					Integer size = (Integer)i.next();
					if(size.intValue() <=100)
					 out.add(size);
				}
			}else{
				i = metric.iterator();
				while(i.hasNext())
					out.add(i.next());
			}
			i = flotation.iterator();
			while(i.hasNext())
				out.add(i.next());
			i = alpha.iterator();
			while(i.hasNext())
				out.add(i.next());
		}
		return out;
	}
	
	public static List formatSizes(List l, String car) {
		boolean rim = false;
		if("rim".equalsIgnoreCase(car))
			rim = true;
		List out = new ArrayList();
		Iterator i = l.iterator();
		while(i.hasNext()) {
			String value = StringUtils.formatTireSize(String.valueOf(i.next()),rim);
			out.add(value);
		}
		return out;
	}
	
	public static Map getMappedBestInClassTires(List tires){
		Map m = new LinkedHashMap();
		for(int i=0; tires != null && i <tires.size(); i++){
			com.bfrc.pojo.tire.jda2.Display disp = (com.bfrc.pojo.tire.jda2.Display)tires.get(i);
			m.put(disp.getId(), disp);
		}
		return m;
	}
	
	public static boolean isBestInClassTire(Map mappedBestInClassTires, Display display){
		if(display == null) 
			return false;
		return (mappedBestInClassTires != null && mappedBestInClassTires.containsKey(Long.valueOf(display.getId())));
	}
	
	public static boolean isBestInClassTire(Map mappedBestInClassTires, Object id){
		if(id == null)
			return false;
		return (mappedBestInClassTires != null && mappedBestInClassTires.containsKey(Long.valueOf(id.toString())));
	}
	
	public static Map getTireIcons(Map mappedGroupings, List checkIconTires){
		Map displayid_icons = new HashMap();
		if(checkIconTires != null){
			for(int i =0; i< checkIconTires.size(); i++){
				Object obj = checkIconTires.get(i);
				com.bfrc.pojo.tire.jda2.TireGrouping tg = null;
				long displayId = 0;
				String tireName = null;
				if("com.bfrc.pojo.tire.jda2.Display".equals(obj.getClass().getCanonicalName())){
					com.bfrc.pojo.tire.jda2.Display tire = (com.bfrc.pojo.tire.jda2.Display)checkIconTires.get(i);
					tg = (com.bfrc.pojo.tire.jda2.TireGrouping)mappedGroupings.get(tire.getId());
					tireName = tire.getValue() == null ? "" : tire.getValue().toLowerCase();
					displayId = tire.getId();
				}else{
					Tire tire = (Tire)checkIconTires.get(i);
					tg = (com.bfrc.pojo.tire.jda2.TireGrouping)mappedGroupings.get(tire.getDisplayId());
					tireName = tire.getTireName() == null ? "" : tire.getTireName().toLowerCase();
					displayId = tire.getDisplayId();
				}


				if(!displayid_icons.containsKey(displayId)){
					List icons = new ArrayList();
					boolean performanceFound = false;
					boolean summerFound = false;
					if(tg != null){
						String tireClass = tg.getClass_() == null ? "" : tg.getClass_().toLowerCase();
						if(tireClass.indexOf("winter") >=0){
							TireIcon icon = new TireIcon("icon-snowflake.png","icon-snowflake-lrg.png","Snow Tires","Snow Tires");
							icons.add(icon);
						}
						//wait for the correction of the new mapping 5/10 release
						if(tireClass.indexOf("summer") >=0){
							TireIcon icon = new TireIcon("icon-sun.png","icon-sun-lrg.png","Summer Tires","Summer Tires");
							icons.add(icon);
							summerFound = true;
						}
						if(tireClass.indexOf("all-season") >=0){
							TireIcon icon = new TireIcon("icon-all-season.png","icon-all-season-lrg.png","All Season Tires","All Season Tires");
							icons.add(icon);
						}
						if(tireClass.indexOf("maximum traction") >=0){
							TireIcon icon = new TireIcon("icon-max-traction.png","icon-max-traction-lrg.png","Max Traction Tires","Max Traction Tires");
							icons.add(icon);
						}
						if(tireClass.indexOf("all-terrain") >=0){
							TireIcon icon = new TireIcon("icon-all-terrain.png","icon-all-terrain-lrg.png","All Terrain Tires","All Terrain Tires");
							icons.add(icon);
						}
						if(tireClass.indexOf("performance") >=0){
							performanceFound = true;
							TireIcon icon = new TireIcon("icon-ultra-perform.png","icon-ultra-perform-lrg.png","Performance Tires/Ultra High Performance Tires","Performance Tires/Ultra High Performance Tires");
							icons.add(icon);
						}
					}
					if(tireName.indexOf("ecopia") >=0){
						TireIcon icon = new TireIcon("icon-green.png","icon-green-lrg.png","Green Tires","Green Tires");
						icons.add(icon);
					}
					if(tireName.indexOf("rft") >=0){
						TireIcon icon = new TireIcon("icon-run-flat.png","icon-run-flat-lrg.png","Run Flat Tires","Run Flat Tires");
						icons.add(icon);
					}
					/*
               if(tire.getTiregroupId() == 3){
            	   if(!performanceFound){
            	   TireIcon icon = new TireIcon("icon-ultra-perform.png","icon-ultra-perform-lrg.png","Performance Tires/Ultra High Performance Tires","Performance Tires/Ultra High Performance Tires");
                   icons.add(icon);
            	   }
               }
               if(tireClassOld.contains("summer")){
            	   if(!summerFound){
            	       TireIcon icon = new TireIcon("icon-sun.png","icon-sun-lrg.png","Summer Tires","Summer Tires");
                       icons.add(icon);
            	   }
               }
					 */
					if(icons.size() >0)
						displayid_icons.put(displayId,icons);
				}
			}
		}
	    return displayid_icons;
	}
	
	public static String getSubBrandImage(String tireName,boolean reset){
	       if(tireName == null)
	           return null;
	       java.util.List subbrands = (java.util.List)CacheService.getInstance().get("_SUB_BRANDS_");
	       if(subbrands == null ||reset){
	           subbrands = new java.util.ArrayList();
	           subbrands.add("Affinity");
	           subbrands.add("Blizzak");
	           subbrands.add("Destination");
	           subbrands.add("DriveGuard");
	           subbrands.add("Dueler");
	           subbrands.add("Duravis");
	           subbrands.add("Ecopia");
	           subbrands.add("Firehawk");
	           subbrands.add("Fuzion");
	           subbrands.add("Potenza");
	           subbrands.add("Transforce");
	           subbrands.add("Turanza");
	           subbrands.add("Winterforce");
	           CacheService.getInstance().add("_SUB_BRANDS_",subbrands);
	       }

	       for(int i=0; i<subbrands.size();i++){
	           String sb = (String)subbrands.get(i);
	           if(tireName.toLowerCase().indexOf(sb.toLowerCase()) >=0){
	               return sb;
	           }
	       }
	       return null;
	   }
}