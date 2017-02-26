package com.bfrc.framework.util;

import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bfrc.Config;
import com.bfrc.UserSessionData;
import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.spring.MailManager;
import com.bfrc.pojo.User;
import com.bfrc.pojo.contact.Mail;
import com.bfrc.pojo.store.Store;
import com.hibernate.dao.base.BaseDao;

import org.json.*;

public class UserProfileUtils {
   public static void saveUserDataCookie(HttpServletRequest request,HttpServletResponse response, JSONObject jo){
       String cookie=StringUtils.base64Encode(jo.toString());
       javax.servlet.http.Cookie userCookie = getCookie(request.getCookies(),UserSessionData._USER_COOKIE_DATA);
       if(userCookie == null)
           userCookie = new javax.servlet.http.Cookie(UserSessionData._USER_COOKIE_DATA, cookie);
       else
           userCookie.setValue(cookie);
       int expiry = 60*60*24*365;//1 year
       userCookie.setMaxAge(expiry);
       //javax.servlet.http.Cookie userCookie = new javax.servlet.http.Cookie(UserSessionData._USER_COOKIE_DATA, cookie);
       //userCookie.setMaxAge(expiry);
       response.addCookie(userCookie);
   }
   
   public static void saveCookie(HttpServletRequest request, HttpServletResponse response, String name, String cookie){
       javax.servlet.http.Cookie userCookie = getCookie(request.getCookies(),name);
       if(userCookie == null)
           userCookie = new javax.servlet.http.Cookie(name, cookie);
       else
           userCookie.setValue(cookie);
       int expiry = 60*60*24*90;//3 months
       userCookie.setMaxAge(expiry);
       response.addCookie(userCookie);

   }
   public static String getUserDataCookie(HttpServletRequest request){
       String s = getCookieValue(request.getCookies(),UserSessionData._USER_COOKIE_DATA,null);
       if(s != null)
           s= StringUtils.base64Decode(s);
        return s;
   }

   public static void copyAll(Map m, JSONObject jo){
       if(m == null || jo == null)
           return;
       try{
           for(java.util.Iterator it = m.keySet().iterator(); it.hasNext(); ){
               String key = (String)it.next();
               jo.put(key, m.get(key));
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
   }
   
   public static void deleteStoreCookie(HttpServletRequest request, HttpServletResponse response, Object value){
	   deleteUserProfileCookie(request, response, "s", value);
	   
   }
   public static void deleteVehicleCookie(HttpServletRequest request, HttpServletResponse response, Object value){
	   deleteUserProfileCookie(request, response, "v", value);
	   
   }
   
   public static void deleteUserProfileCookie(HttpServletRequest request, HttpServletResponse response, String key, Object value){
       try{
    	    if(value == null)
    	    	return;
    	    String thisid = value.toString();
            JSONObject joCookie = null;
            String userDataCookie = getUserDataCookie(request);
            
            if(StringUtils.isNullOrEmpty(userDataCookie)){
                joCookie = new JSONObject();
            }else{
                joCookie = new JSONObject(userDataCookie);
            }
            boolean containEntry = false;
            if("s".equals(key)){//user stores
                JSONArray jl = null;
                JSONArray newjl = new JSONArray();
                try{
                    jl = (JSONArray)joCookie.getJSONArray(key);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                if(jl == null){
                    return;
                }else{
                    for(int i=0; i<jl.length();i++){
                        //Map m =(Map)jl.get(i);
                        JSONObject m = (JSONObject)jl.get(i);
                        String id = (String)m.get("sn");
                        if(!thisid.equals(id)){
                            newjl.put(m);
                        }else{
                        	containEntry = true;
                        }
                    }
                }
                joCookie.put(key, newjl);
            }else if("v".equals(key)){//user stores
                JSONArray newjl = new JSONArray();
                JSONArray jl = null;
                try{
                    jl = (JSONArray)joCookie.getJSONArray(key);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                if(jl == null){
                    return;
                }else{
                    for(int i=0; i<jl.length();i++){
                        //Map m =(Map)jl.get(i);
                        JSONObject m = (JSONObject)jl.get(i);
                        String id = (String)m.get("vi");
                        if(!thisid.equals(id)){
                            newjl.put(m);
                        }else{
                        	containEntry = true;
                        }
                    }
                }
                joCookie.put(key, newjl);
            }else{
                ;//do nothing
            }
            //System.out.println("2-------------------------------------");
            //System.out.println(joCookie);
            if(containEntry){
                saveUserDataCookie(request,response, joCookie);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
   }
   
   public static void saveUserStoreCookie(HttpServletRequest request, HttpServletResponse response, String storeNumber, String personalName, boolean dft){
	   Map value = new HashMap();
	   value.put("sn", storeNumber);
	   value.put("pn", personalName);
	   value.put("df", dft);
	   saveUserProfileCookie(request, response, "s", value);
   }
   
   public static void saveUserStoreCookie(HttpServletRequest request, HttpServletResponse response, 
		                                 String vehicleId, String year, String make, String model, String submodel, 
		                                 String engine, String anualMileage, String currentMileage, String personalName, boolean dft){
	   Map value = new HashMap();
	   value.put("vi", vehicleId);
	   if(!StringUtils.isNullOrEmpty(personalName))
	       value.put("pn", personalName);
	   value.put("df", dft);
	   if(!StringUtils.isNullOrEmpty(year))
		   value.put("yr", year);
	   if(!StringUtils.isNullOrEmpty(make))
		   value.put("mk", make);
	   if(!StringUtils.isNullOrEmpty(model))
		   value.put("md", model);
	   if(!StringUtils.isNullOrEmpty(submodel))
		   value.put("sm", submodel);
	   if(!StringUtils.isNullOrEmpty(engine))
		   value.put("eg", engine);
	   if(!StringUtils.isNullOrEmpty(anualMileage))
		   value.put("am", anualMileage);
	   if(!StringUtils.isNullOrEmpty(currentMileage))
		   value.put("cm", currentMileage);
	   saveUserProfileCookie(request, response, "v", value);
   }
   
   public static void saveUserProfileCookie(HttpServletRequest request, HttpServletResponse response, 
           String firstName, String lastName, String email, String address1, String address2, String city, 
           String state, String zip, String phone){
		Map value = new HashMap();
		if(!StringUtils.isNullOrEmpty(firstName))
		    value.put("fn", firstName);
		if(!StringUtils.isNullOrEmpty(lastName))
		    value.put("ln", firstName);
		if(!StringUtils.isNullOrEmpty(email))
		    value.put("em", email);
		if(!StringUtils.isNullOrEmpty(address1))
		    value.put("a1", address1);
		if(!StringUtils.isNullOrEmpty(address2))
		    value.put("a2", address1);
		if(!StringUtils.isNullOrEmpty(city))
		    value.put("ct", city);
		if(!StringUtils.isNullOrEmpty(state))
		    value.put("st", state);
		if(!StringUtils.isNullOrEmpty(zip))
		    value.put("zp", firstName);
		if(!StringUtils.isNullOrEmpty(phone))
		    value.put("ph", phone);
		
		saveUserProfileCookie(request, response, "pf", value);
   }
   
   public static void saveUserStoreCookie(HttpServletRequest request, HttpServletResponse response, Object value){
	   saveUserProfileCookie(request, response, "s", value);
   }
   public static void saveUserVehicleCookie(HttpServletRequest request, HttpServletResponse response, Object value){
	   saveUserProfileCookie(request, response, "v", value);
   }
   public static void saveUserProfileCookie(HttpServletRequest request, HttpServletResponse response, Object value){
	   saveUserProfileCookie(request, response, "pf", value);
   }
   public static void saveUserProfileCookie(HttpServletRequest request, HttpServletResponse response, String key, Object value){
       try{
            JSONObject joCookie = null;
            String userDataCookie = getUserDataCookie(request);
            //System.out.println("1-------------------------------------");
            //System.out.println(userDataCookie);
            if(StringUtils.isNullOrEmpty(userDataCookie)){
                joCookie = new JSONObject();
            }else{
                joCookie = new JSONObject(userDataCookie);
            }
            if("s".equals(key)){//user stores
                Map thisVal = (Map)value;
                String thisid = (String)((Map)value).get("sn");
                String strDf = ((Map)value).get("df") == null ? "0" : ((Map)value).get("df").toString();
                
                JSONArray jl = null;
                try{
                    jl = (JSONArray)joCookie.getJSONArray(key);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                if(jl == null){
                    jl = new JSONArray();
                    jl.put(value);
                }else{
                    boolean containEntry = false;
                    for(int i=0; i<jl.length();i++){
                        //Map m =(Map)jl.get(i);
                        JSONObject m = (JSONObject)jl.get(i);
                        String id = (String)m.get("sn");
                        if(thisid.equals(id)){
                            copyAll(thisVal,m);
                            containEntry = true;
                        }else{
                            //--- if passed in is default store, then we need update other stores to be false ---//
                            if("1".equals(strDf)){
                               m.put("df", new Boolean(false));
                            }
                        }
                    }
                    if(!containEntry)
                       jl.put(value);
                }
                joCookie.put(key, jl);
            }else if("v".equals(key)){//user stores
                Map thisVal = (Map)value;
                String thisid = (String)((Map)value).get("vi");
                String strDf = ((Map)value).get("df") == null ? "0" : ((Map)value).get("df").toString();
                JSONArray jl = null;
                try{
                    jl = (JSONArray)joCookie.getJSONArray(key);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                if(jl == null){
                    jl = new JSONArray();
                    jl.put(value);
                }else{
                    boolean containEntry = false;
                    for(int i=0; i<jl.length();i++){
                        //Map m =(Map)jl.get(i);
                        JSONObject m = (JSONObject)jl.get(i);
                        String id = (String)m.get("vi");
                        if(thisid.equals(id)){
                            copyAll(thisVal,m);
                            containEntry = true;
                        }else{
                            //--- if passed in is default store, then we need update other stores to be false ---//
                            if("1".equals(strDf)){
                               m.put("df", new Boolean(false));
                            }
                        }
                    }
                    if(!containEntry)
                       jl.put(value);
                }
                joCookie.put(key, jl);
            }else{
                joCookie.put(key, value);
            }
            //System.out.println("2-------------------------------------");
            //System.out.println(joCookie);
            saveUserDataCookie(request,response, joCookie);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
   }

   public static JSONObject getUserProfileFromCookie(HttpServletRequest request, ServletContext application){
	   String s =  getUserDataCookie(request);
           if(s != null){
               try{
                   //System.out.println("s===="+s);
                   JSONObject jo = new JSONObject(s);
                   return jo.getJSONObject("pf");
               }catch(Exception ex){
                    ex.printStackTrace();
                }
           }
           return null;
   }

   public static List getUserVehiclesFromCookie(HttpServletRequest request, ServletContext application){
	   String s =  getUserDataCookie(request);
           if(s != null){
               try{
                   JSONObject jo = new JSONObject(s);
                   JSONArray ja =  jo.getJSONArray("v");
                   List items = new ArrayList();
                   if(ja != null){
                       for(int i=0; i< ja.length(); i++){
                           JSONObject jo2 = (JSONObject)ja.get(i);
                           String acesVehicleId = (String)jo2.get("vi");
                           String subModel = (String)jo2.get("sm");
                           List l  = Config.initBeans(application).vehicleDAO.getFitments(acesVehicleId);
                           if(l != null){
                               items.add(l.iterator().next());
                           }
                       }
                       return items;
                   }
                   
               }catch(Exception ex){
                    ex.printStackTrace();
                }
           }
           return null;
   }

   public static List getUserStoresFromCookie(HttpServletRequest request, ServletContext application){
	   String s =  getUserDataCookie(request);
           if(s != null){
               try{
                   JSONObject jo = new JSONObject(s);
                   JSONArray ja =  jo.getJSONArray("s");
                   List items = new ArrayList();
                   if(ja != null){
                       for(int i=0; i< ja.length(); i++){
                           JSONObject jo2 = (JSONObject)ja.get(i);
                           String id = (String)jo2.get("sn");
                           String pn = (String)jo2.get("pn");
                           Store store  = Config.initBeans(application).storeDAO.findStoreById(Long.valueOf(id));
                           if(store != null){
                               items.add(store);
                           }
                       }
                       return items;
                   }

               }catch(Exception ex){
                    ex.printStackTrace();
                }
           }
           return null;
   }

   public static String getCookieValue(javax.servlet.http.Cookie[] cookies,
           String cookieName,
           String defaultValue) {
            for(int i=0; i<cookies.length; i++) {
                    javax.servlet.http.Cookie cookie = cookies[i];
                    //System.out.println("3------------------"+cookie.getValue());
                    if (cookieName.equals(cookie.getName())){

                        return(cookie.getValue());
                    }
            }
        return(defaultValue);
    }

   public static javax.servlet.http.Cookie getCookie(javax.servlet.http.Cookie[] cookies, String cookieName) {
            for(int i=0; i<cookies.length; i++) {
                javax.servlet.http.Cookie cookie = cookies[i];
                if (cookieName.equals(cookie.getName())){
                    return cookie;
                }
            }
        return null;
    }
   
   public static UserSessionData getUserSessionData(HttpSession session){
	   UserSessionData userData = (UserSessionData)session.getAttribute(UserSessionData._USER_SESSION_DATA);
	   if(userData == null) {
	       userData = new UserSessionData();
	       session.setAttribute(UserSessionData._USER_SESSION_DATA, userData);
	   }
	   return userData;
   }
   
   public static void sendUserPassword(User user, ServletContext application, String emailFile){
       //user.setDefaultFlag(new Boolean(true));
       Config thisConfig = Config.locate(application);
       BaseDao baseUserDAO = (BaseDao)Config.locate(application,"baseUserDAO");
       ContactDAO contactDAO = (ContactDAO)Config.locate(application,"contactDAO");
       user.setUserTypeId(thisConfig.getUserType());
       boolean sendEmail = true;
       try{
           if(user.getId() <= 0){
               user.setPassword(StringUtils.generatePassword());
               baseUserDAO.save(user);
           }else{
               if(!StringUtils.isNullOrEmpty(user.getPassword())){
                  //-- send user with db password and set the defaultPlag to true --//
               }else{
                  //-- send user with generated password and set the defaultPlag to true --//
                  user.setPassword(StringUtils.generatePassword());
               }
               baseUserDAO.update(user);
           }
       }catch(Exception ex){
           sendEmail =false;
       }
       Map data = new HashMap();
       data.put("FIRST_NAME", user.getFirstName());
       data.put("PWD", user.getPassword());
       data.put("EMAIL_ADDRESS", user.getEmailAddress());
       if(sendEmail){
            String message = ServerUtil.populateEmailMessage(emailFile, data);
            MailManager
                mailManager = (MailManager)Config.locate(application, "mailManager");
            try{
                Mail mail = new Mail();
                mail.setTo(new String[]{user.getEmailAddress()});
                mail.setSubject(thisConfig.getSiteFullName()+" -  Password");
                String from = contactDAO.getFrom();
                mail.setFrom(from);
                mail.setHtml(true);
                mail.setBody(message);
                mailManager.sendMail(mail);
            }catch(Exception ex){
               ex.printStackTrace();
           }
       }
    }

}