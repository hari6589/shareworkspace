package com.bfrc.framework.util;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class PropertyConfigUtil extends PropertyPlaceholderConfigurer
{

    private String _defaultFileName;
    private ResourceLoader _resourceLoader;
    private String _serverName;
    private static Map keys_props;
    private static Map file_attributes;
    private static String _defaultPropFileName;
    private static String _livePropFileName;
    private static String _localPropFileName;
    private static boolean _isLocalExist;
    private static boolean _isLiveExist;
    public PropertyConfigUtil(ResourceLoader resourceLoader, String defaultName)
    {
        if(defaultName == null || defaultName.length() < 1)
        {
            String message = "Error, the default name must be specified";
            throw new IllegalArgumentException(message);
        }
        if(resourceLoader == null)
        {
            String message = "Error, the resource loader must not be null";
            throw new IllegalArgumentException(message);
        }
        _defaultFileName = defaultName;
        _resourceLoader = resourceLoader;
        
        _serverName = ServerUtil.getHostname();
        
    }

    /**
     * Load in reverseorder of priority:
     * 
     * - specific server
     * - live environment (because it's clustered)
     * - default properties file
     * 
     * Last one in wins, so later configs override earlier ones.
     * 
     * This way, we can specify behavior for one prod server and not the other. (a very rare situation, but one we have for ExpertTire and appointment retries)
     * 
     * @param location
     */
    public void setDefaultLocation(String location)
    {
        try {
        	List<Resource> resourceList = new ArrayList<Resource>();
        	
        	
	        String defaultLocation = (new StringBuffer()).append(location).append(_defaultFileName).append(".properties").toString();
	        Resource defaultResource = _resourceLoader.getResource(defaultLocation);
	        
	        if (defaultResource.exists()) {
		        _defaultPropFileName =  defaultResource.getFile().getPath();
		        System.out.println("                               <====================>"+_defaultPropFileName);
		        
		        resourceList.add(defaultResource);
	        }
        	
	        
        	if (ServerUtil.isProduction()) {
	        	String liveEnvironmentLocation = location+"live.properties";
	        	Resource liveConfig = _resourceLoader.getResource(liveEnvironmentLocation);
	        	if (liveConfig.exists()) {
	            	_isLiveExist = true;
	            	_livePropFileName = liveConfig.getFile().getPath();
	            	System.out.println("                               <====================>"+_livePropFileName);
	            	
	            	resourceList.add(liveConfig);
	        	}
        	}
	        
            String hostnameLocation = (new StringBuffer()).append(location).append(_serverName).append(".properties").toString();
            Resource hostConfig = _resourceLoader.getResource(hostnameLocation);
            
        	if (hostConfig.exists()) {
	        	_isLocalExist = true;
	        	_localPropFileName = hostConfig.getFile().getPath();
	        	System.out.println("                               <====================>"+_localPropFileName);
	            
	        	resourceList.add(hostConfig);
        	} 

	        

	       
	        super.setLocations(resourceList.toArray(new Resource[resourceList.size()]));
        
        } catch(Exception ex) {
        	System.out.println("                               No Config Found, ignore the configuration Load.");
        	//ex.printStackTrace();
        }
       
        /*
        try{
            super.loadProperties(Current());
            //super.mergeProperties();
        }catch(Exception ex){
        	ex.printStackTrace();
        }
        */
        //super.setProperties(Current());
        
    }
    
    public static void loadData(){
		System.out.println("				... loading data for ... "+_defaultPropFileName);
		try {
            Properties props = (Properties)keys_props.get(_defaultPropFileName.toUpperCase());
            if(props == null) props = new Properties();
            FileInputStream input = new FileInputStream(_defaultPropFileName);
            File file = new java.io.File(_defaultPropFileName);
            props.clear();
            props.load(input);
            parsePropertiesDefault(props);
            if(keys_props == null )keys_props = new HashMap();
            keys_props.put(_defaultPropFileName.toUpperCase(),props);
            input.close();
            if(file_attributes == null)  file_attributes = new HashMap();
            file_attributes.put(_defaultPropFileName.toUpperCase()+"_lastLoaded",file.lastModified()+"");
            if(_isLocalExist){
            	System.out.println("				... loading data for ... "+_localPropFileName);
	            Properties props2 = (Properties)keys_props.get(_localPropFileName.toUpperCase());
	            if(props2 == null) props2 = new Properties();
	            FileInputStream input2 = new FileInputStream(_localPropFileName);
	            File file2 = new java.io.File(_localPropFileName);
	            props2.clear();
	            props2.load(input2);
	            parsePropertiesDefault(props2);
	            input.close();
	            if(file_attributes == null)  file_attributes = new HashMap();
	            file_attributes.put(_localPropFileName.toUpperCase()+"_lastLoaded",file2.lastModified()+"");
	            Enumeration keys = props2.keys();
	            while(keys.hasMoreElements()){
	                String key = (String)keys.nextElement();
	                props.setProperty(key, props2.getProperty(key));
	            }
            }
            
            
        } catch (Exception e) {
          System.out.println("                               No Config Found, ignore the configuration Load.");
         //e.printStackTrace();
        }
	}
    
        public static Properties Current(){
            checkData();
            return (Properties)keys_props.get(_defaultPropFileName.toUpperCase());
        }
	private static void checkData(){
		
		if(isFileChanged(_defaultPropFileName) || (_isLocalExist && isFileChanged(_localPropFileName))){
			   loadData();         
		}
	}
	public static boolean isFileChanged(String fileName)
	{
		if(fileName == null) fileName = _defaultPropFileName;
		boolean isChanged = false;
	        if(keys_props == null )keys_props = new HashMap();
                Properties props = (Properties)keys_props.get(_defaultPropFileName.toUpperCase());
                if(props == null ){ 
                	
                    loadData(); 
                    isChanged = false;
                }
		if(file_attributes == null)  file_attributes = new HashMap();
                
                long lastLoaded = 0;
                if(file_attributes.get(fileName.toUpperCase()+"_lastLoaded") == null){
                	isChanged =  true;
                }else{
                    lastLoaded = Long.parseLong((String)file_attributes.get(fileName.toUpperCase()+"_lastLoaded"));
                 }
               
                java.io.File file = null;
                if(!isChanged){
                    try{
                        file = new java.io.File(fileName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //System.out.println("file.lastModified()"+file.lastModified()+" = "+fileName.toUpperCase()+"_lastLoaded"+" : "+lastLoaded);

                    if (file.lastModified() > lastLoaded)
                    {
                        isChanged = true;
                        file_attributes.put(fileName.toUpperCase()+"_lastLoaded",file.lastModified()+"");
                    }
                }
        return isChanged;
    } // end checkFile  
    public static void parsePropertiesDefault(java.util.Properties props){
        parsePropertiesDefault(null,props);
    }
    public static void parsePropertiesDefault(String keyprefix,java.util.Properties props){
        if(keyprefix == null) keyprefix="";
       try{
           java.util.Map old_refs = new java.util.HashMap();
           
           java.util.Enumeration prp_keys = props.propertyNames();
           String key = null;
           String val = null;
           String key_o = null;
           String old_s = null;
           String new_s = null;
           while(prp_keys.hasMoreElements()){
               key = (String)prp_keys.nextElement();
               val = props.getProperty(key);
               if(!"".equals(keyprefix) && key.indexOf(keyprefix) == 0 ){
                    String prodkey = key.substring(keyprefix.length());
                    props.remove(prodkey);
                    props.remove(key);
                    props.setProperty(prodkey,val);
               }
           }
           prp_keys = props.propertyNames();
           old_refs.putAll(props);
           while(prp_keys.hasMoreElements()){
                   key = (String)prp_keys.nextElement();
                   val = props.getProperty(key);
                   if( val != null){
                       for(java.util.Iterator it = old_refs.keySet().iterator(); it.hasNext();){
                           try{
                               key_o = (String)it.next();
                               new_s = (String)old_refs.get(key_o);
                               old_s = "${"+key_o+"}";
                               if( val.indexOf(old_s) >=0 ){
                                   val = ServerUtil.replace(val,old_s,new_s);
                                   props.setProperty(key,val);
                               }
                           }catch(Exception ex){
                                   ex.printStackTrace();
                           }
                       }
                   }
           }
       }catch(Exception ex){
               //ex.printStackTrace();
       }
   }

}