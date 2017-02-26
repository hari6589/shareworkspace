package com.bfrc.framework.dao;

import java.util.*;

import com.bfrc.pojo.contact.WebSite;

public interface ContactDAO {
	List getAddresses(Integer feedbackId, String type);
	List getBCCAddresses(Integer feedbackId);
	List getCCAddresses(Integer feedbackId);
	List getToAddresses(Integer feedbackId);
	String getSubject(Integer feedbackId);
	String[] getTo(String feedbackSubject,Long storeNumber, String state);
	String[] getCc(String feedbackSubject,Long storeNumber, String state);
	String[] getBcc(String feedbackSubject,Long storeNumber, String state);
	String[] getTo(String feedbackSubject,Long storeNumber);
	String[] getCc(String feedbackSubject,Long storeNumber);
	String[] getBcc(String feedbackSubject,Long storeNumber);
	String getFrom();
	String getUrl();
	List getSubjects();
	List getStates();
	WebSite getSite();
	List getWebSites();
	Map getMappedWebSites();
	List getAllFeedBacks();
	String[] getTo(String feedbackSubject,String webSite);
	String[] getCc(String feedbackSubject,String webSite);
	String[] getBcc(String feedbackSubject,String webSite);
	String[] getTo(String feedbackSubject,String webSite,Long storeNumber, String state);
	String[] getCc(String feedbackSubject,String webSite,Long storeNumber, String state);
	String[] getBcc(String feedbackSubject,String webSite,Long storeNumber, String state);
	List getMainFeedbacks();
	List getFeedbacksByMain(Integer mainFeedbackId);	
}
