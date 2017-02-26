package com.bfrc;


import java.util.*;
import java.io.*;

import com.bfrc.pojo.User;
import com.bfrc.pojo.tire.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

//--- Cached in session in small size ---//
//-- it carries all search parameters and put in cache for easy retrievable --//
public class UserSessionData  extends LinkedHashMap<String, Object> implements java.io.Serializable {

	public static final String _USER_SESSION_DATA = "com-bfrc-UserSessionData";
	public static final String _USER_COOKIE_DATA = "bsro.uc";
    /**
	 * 
	 */
	private static final long serialVersionUID = -2184857796351963246L;
	// Constructors
    /** default constructor */
    public UserSessionData() {
    }
    
    public UserSessionData(HttpServletRequest httpServletRequest) {
    	this.httpServletRequest = httpServletRequest;
    }
    public UserSessionData(HttpSession httpSession) {
    	this.httpSession = httpSession;
    }
    
    /*
    Vehicle vehicle;
    public Vehicle getVehicle(){
    	return this.vehicle;
    }
    public void  setVehicle(Vehicle vehicle){
    	this.vehicle = vehicle;
    }
    */
    //-- carry a request for easy session, bean access --//
    HttpServletRequest httpServletRequest;
    public HttpServletRequest getHttpServletRequest(){
    	return this.httpServletRequest;
    }
    public void  setHttpServletRequest(HttpServletRequest httpServletRequest){
    	this.httpServletRequest = httpServletRequest;
    }
    
    HttpSession httpSession;
    public HttpSession getHttpSession(){
    	return this.httpSession;
    }
    public void  setHttpSession(HttpSession httpSession){
    	this.httpSession = httpSession;
    }
    //---- Search Parameters ---//
    
    protected String acesVehicleId;
    protected String year;
    protected String make;
    protected String makeId;
    protected String model;
    protected String modelId;
    protected String submodel;
    private String tpms;
    
    public String getAcesVehicleId() { return acesVehicleId; }
    public String getYear() { return year; }
    public String getMake() { return make; }
    public String getMakeId() { return makeId; }
    public String getModel() {return model; }
    public String getModelId() {return modelId; }
    public String getSubmodel() { return submodel; }

    public void setAcesVehicleId(String param) { acesVehicleId = param; }
    public void setYear(String param) { year = param; }
    public void setMake(String param) { make = param; }
    public void setMakeId(String param) { makeId = param; }
    public void setModel(String param) { model = param; }
    public void setModelId(String param) { modelId = param; }
    public void setSubmodel(String param) { submodel = param; }

    public String getTpms() { return tpms;}

	public void setTpms(String tpms) {this.tpms = tpms;}
	public boolean isTpms() {return "1".equals(tpms);}
	
    private String zip;
    public String getZip(){
        return this.zip;
    }
    public void setZip(String zip){
        this.zip = zip;
    }
    
    private String radius;
    public String getRadius(){
        return this.radius;
    }
    public void setRadius(String radius){
        this.radius = radius;
    }
    
    public int getIntRadius(){
    	if(radius != null)
		try {
			int i= Integer.parseInt(radius);
			if(i<0)
				return 0;
		} catch(NumberFormatException ex) {}
	   return 0;
    }
    
    //-- default store number --//
    private String storeNumber;
    public String getStoreNumber(){
        return this.storeNumber;
    }
    public void setStoreNumber(String storeNumber){
        this.storeNumber = storeNumber;
    }
    
    //--- select another store --//
    private String regularStoreNumber;
    public String getRegularStoreNumber(){
        return this.regularStoreNumber;
    }
    public void setRegularStoreNumber(String regularStoreNumber){
        this.regularStoreNumber = regularStoreNumber;
    }
    
    //--- store count --//
    private int storeCount;
    public int getStoreCount(){
        return this.storeCount;
    }
    public void setStoreCount(int storeCount){
        this.storeCount = storeCount;
    }
    
    //-- search by size --//
    private String crossSection;
    public String getCrossSection(){
        return this.crossSection;
    }
    public void setCrossSection(String crossSection){
        this.crossSection = crossSection;
    }
   
    private String aspect;
    public String getAspect(){
        return this.aspect;
    }
    public void setAspect(String aspect){
        this.aspect = aspect;
    }
   
    private String rimSize;
    public String getRimSize(){
        return this.rimSize;
    }
    public void setRimSize(String rimSize){
        this.rimSize = rimSize;
    }
    
    //-- Search By(vehicle, size, advanced) --//
    private String userRequest;
    public String getUserRequest(){
        return this.userRequest;
    }
    public void setUserRequest(String userRequest){
        this.userRequest = userRequest;
    }
    
    //-- advanced search --//
    private String category;
    public String getCategory(){
        return this.category;
    }
    public void setCategory(String category){
        this.category = category;
    }
    
    private String segment;
    public String getSegment(){
        return this.segment;
    }
    public void setSegment(String segment){
        this.segment = segment;
    }
    
    //-- articles in session --//
    private String articles;
    public String getArticles(){
        return this.articles;
    }
    public void setArticles(String articles){
        this.articles = articles;
    }
    
    private String frontArticles;
    public String getFrontArticles(){
        return this.frontArticles;
    }
    public void setFrontArticles(String frontArticles){
        this.frontArticles = frontArticles;
    }
    
    private String rearArticles;
    public String getRearArticles(){
        return this.rearArticles;
    }
    public void setRearArticles(String rearArticles){
        this.rearArticles = rearArticles;
    }
    
    private String matchedArticles;
    public String getMatchedArticles(){
        return this.matchedArticles;
    }
    public void setMatchedArticles(String matchedArticles){
        this.matchedArticles = matchedArticles;
    }
    
    private String standardSizeArticles;
    public String getStandardSizeArticles(){
        return this.standardSizeArticles;
    }
    public void setStandardSizeArticles(String standardSizeArticles){
        this.standardSizeArticles = standardSizeArticles;
    }
    
    private String optionalSizeArticles;
    public String getOptionalSizeArticles(){
        return this.optionalSizeArticles;
    }
    public void setOptionalSizeArticles(String optionalSizeArticles){
        this.optionalSizeArticles = optionalSizeArticles;
    }
    
    
    //-- store number in session --//
    private String storeNumbers;
    public String getStoreNumbers(){
        return this.storeNumbers;
    }
    public void setStoreNumbers(String storeNumbers){
        this.storeNumbers = storeNumbers;
    }
    
    //-- matched sets parameters--//
    private String frbKey;
	public String getFrbKey() {
		return frbKey;
	}
	public void setFrbKey(String frbKey) {
		this.frbKey = frbKey;
	}
	//--- fron and rear fitment ---// 
    List<Fitment> fitments;
    public List<Fitment> getFitments(){
    	return this.fitments;
    }
    public void  setFitments(List<Fitment> fitments){
    	this.fitments = fitments;
    }
    
    public Fitment getFrontFitment(){
    	if(this.fitments != null){
    		for(Fitment fitment : this.fitments){
    			if('B' == fitment.getFrb() || 'F' == fitment.getFrb()){
    				return fitment;
    			}
    		}
    	}
    	return null;
    }
    
    public Fitment getRearFitment(){
    	if(this.fitments != null){
    		for(Fitment fitment : this.fitments){
    			if('R' == fitment.getFrb()){
    				return fitment;
    			}
    		}
    	}
    	return null;
    }
    
    //-----Log Records Ids -----//
    private Long tpUserId;
    public Long getTpUserId() {
		return tpUserId;
	}
	public void setTpUserId(Long tpUserId) {
		this.tpUserId = tpUserId;
	}
    
     private void readObject(
       ObjectInputStream aInputStream
     ) throws ClassNotFoundException, IOException {
       //always perform the default de-serialization first
       aInputStream.defaultReadObject();

    }

      private void writeObject(
        ObjectOutputStream aOutputStream
      ) throws IOException {
        //perform the default serialization for all non-transient, non-static fields
        aOutputStream.defaultWriteObject();
      }
      
      //--- Profile Info ---//
      private String companyName;
      private String emailAddress;
      private String firstName;
      private String lastName;
      private String streetAddress1;
      private String streetAddress2;
      private String city;
      private String state;
      private String zipCode;
      private String daytimePhone;
      private String eveningPhone;
      private String mobilePhone;
      private HashMap<Long, Long> distances;
      public String getCompanyName() {
          return this.companyName;
      }
      
      public void setCompanyName(String companyName) {
          this.companyName = companyName;
      }
      public String getEmailAddress() {
          return this.emailAddress;
      }
      
      public void setEmailAddress(String emailAddress) {
          this.emailAddress = emailAddress;
      }
      public String getFirstName() {
          return this.firstName;
      }
      
      public void setFirstName(String firstName) {
          this.firstName = firstName;
      }
      public String getLastName() {
          return this.lastName;
      }
      
      public void setLastName(String lastName) {
          this.lastName = lastName;
      }
      public String getStreetAddress1() {
          return this.streetAddress1;
      }
      
      public void setStreetAddress1(String streetAddress1) {
          this.streetAddress1 = streetAddress1;
      }
      public String getStreetAddress2() {
          return this.streetAddress2;
      }
      
      public void setStreetAddress2(String streetAddress2) {
          this.streetAddress2 = streetAddress2;
      }
      public String getCity() {
          return this.city;
      }
      
      public void setCity(String city) {
          this.city = city;
      }
      public String getState() {
          return this.state;
      }
      
      public void setState(String state) {
          this.state = state;
      }
      public String getZipCode() {
          return this.zipCode;
      }
      
      public void setZipCode(String zipCode) {
          this.zipCode = zipCode;
      }
      public String getDaytimePhone() {
          return this.daytimePhone;
      }
      
      public void setDaytimePhone(String daytimePhone) {
          this.daytimePhone = daytimePhone;
      }
      public String getEveningPhone() {
          return this.eveningPhone;
      }
      
      public void setEveningPhone(String eveningPhone) {
          this.eveningPhone = eveningPhone;
      }
      public String getMobilePhone() {
          return this.mobilePhone;
      }
      
      public void setMobilePhone(String mobilePhone) {
          this.mobilePhone = mobilePhone;
      }
      
      //--- myFirstStone ---//
      private User user;
      boolean isLoggedIn(){
    	  return user != null;
      }
      
      public User getUser(){
    	  return this.user;
      }
      public void setUser(User user){
    	  this.user = user;
      }

	public HashMap<Long, Long> getDistances() {
		return distances;
	}

	public void setDistances(HashMap<Long, Long> distances) {
		this.distances = distances;
	}
}