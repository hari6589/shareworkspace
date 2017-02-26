package com.bfrc.framework.dao.hibernate3;

import java.util.*;

import com.bfrc.framework.dao.ContactDAO;
import com.bfrc.framework.spring.HibernateDAOImpl;
import com.bfrc.pojo.contact.*;

public class ContactDAOImpl extends HibernateDAOImpl implements ContactDAO {
	public String getSubject(Integer feedbackId) {
		if(feedbackId == null)
			return null;
		List l = getHibernateTemplate().find("from Feedback f where f.id=?", feedbackId);
		if(l != null && l.size() > 0)
			return ((Feedback)l.get(0)).getSubject();
		return null;
	}

	public List getToAddresses(Integer feedbackId) {
		return getAddresses(feedbackId, getConfig().getTo());
	}
	
	public List getBCCAddresses(Integer feedbackId) {
		return getAddresses(feedbackId, getConfig().getBcc());
	}
	
	public List getCCAddresses(Integer feedbackId) {
		return getAddresses(feedbackId, getConfig().getCc());
	}
	
	public List getAddresses(Integer feedbackId, String type) {
		if(feedbackId == null)
			return null;
		List l = getHibernateTemplate().find("select c from FeedbackContact fc join fc.feedback f " +
			"join fc.contact c where f.id=" + feedbackId + " and fc.contactType.name=?", type);
		if(l != null && l.size() > 0) {
			List out = new ArrayList();
			Iterator i = l.iterator();
			while(i.hasNext())
				out.add(((Contact)i.next()).getEmail());
			return out;
		}
		return null;
	}
	
	public List getStates() {
		return getHibernateTemplate().findByNamedQuery("States");
	}
	
	public List getSubjects() {
		String[] names = {"siteName", "feedbackType"};
		Object[] values = new Object[] {getConfig().getSiteName(), getConfig().getContactUs()};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("BFRCWebSiteFeedback", names, values);
	}

	public String[] getTo(String feedbackSubject,Long storeNumber) {
		return getTo(feedbackSubject, storeNumber, null);
	}
	
	public String[] getTo(String feedbackSubject,Long storeNumber, String state) {
		List tos = getEmails(feedbackSubject,getConfig().getTo(),storeNumber, state);
		String[] to = new String[tos.size()];
		for (int i=0;i<tos.size();i++) {
			to[i] = ((Contact) tos.get(i)).getEmail(); 
		}
		return to;
	}

	public String[] getCc(String feedbackSubject,Long storeNumber) {
		return getCc(feedbackSubject, storeNumber, null);
	}

	public String[] getCc(String feedbackSubject,Long storeNumber, String state) {
		List ccs = getEmails(feedbackSubject,getConfig().getCc(),storeNumber,state);
		String[] cc = new String[ccs.size()];
		for (int i=0;i<ccs.size();i++) {
			cc[i] = ((Contact) ccs.get(i)).getEmail(); 
		}
		return cc;
	}

	public String[] getBcc(String feedbackSubject,Long storeNumber) {
		return getBcc (feedbackSubject, storeNumber, null);
	}
	
	public String[] getBcc(String feedbackSubject,Long storeNumber, String state) {
		List bccs = getEmails(feedbackSubject,getConfig().getBcc(),storeNumber, state);
		String[] bcc = new String[bccs.size()];
		for (int i=0;i<bccs.size();i++) {
			bcc[i] = ((Contact) bccs.get(i)).getEmail(); 
		}
		return bcc;
	}

	public String getFrom() {
		String from = "DO-NOT-REPLY<" + ((WebSite) getSites().get(0)).getWebmaster() + ">";
		return from;
	}
	
	public String getUrl() {
		String url = ((WebSite) getSites().get(0)).getUrl(); 
		return url;
	}
	
	private List getEmails(String feedbackId,String contactType,Long storeNumber) {
		return getEmails(feedbackId, contactType, storeNumber, null);
	}

	private List getEmails(String feedbackId,String siteName, String contactType,Long storeNumber, String state) {
		String[] names = {"siteName", "feedbackType", "feedbackSubject", "contactType", "storeNumber", "state"};
		Object[] values = new Object[names.length];
		if(siteName == null)
			values[0] = getConfig().getSiteName();
		else
		    values[0] = siteName;
		values[1] = getConfig().getContactUs();
		values[2] = feedbackId;
		values[3] = contactType;
		values[4] = new Integer((storeNumber==null)?0:storeNumber.intValue());
		values[5] = state == null ? "ZZ" : state;
		List l = getHibernateTemplate().findByNamedQueryAndNamedParam("BFRCWebSiteContact", names, values);
		List out = new ArrayList();
		Iterator i = l.iterator();
		while(i.hasNext()) {
			Contact c = new Contact();
			c.setEmail((String)i.next());
			out.add(c);
		}
		return out;
		
	}
	private List getEmails(String feedbackId,String contactType,Long storeNumber, String state) {
		
		return getEmails(feedbackId,null,contactType,storeNumber, state);
	}

	public WebSite getSite()
	{
		return (WebSite) getSites().get(0);
	}
	
	private List getSites() {
		return getHibernateTemplate().findByValueBean("from WebSite ws where ws.name=:siteName", getConfig());
	}
	
	public List getWebSites() {
		return getHibernateTemplate().find("from WebSite ws order by ws.name ");
	}
	
	public Map getMappedWebSites() {
		List sites = getWebSites();
		if(sites != null){
			Map m = new LinkedHashMap();
			for(int i=0; i< sites.size(); i++){
				WebSite site = (WebSite)sites.get(i);
				m.put(site.getName(), site);
				
			}
			return m;
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public List getAllFeedBacks() {
		String queryString="from Feedback order by feedbackId";
		return getHibernateTemplate().find(queryString);
	}
	
	public String[] listToArray(List l){
		String[] bcc = new String[l.size()];
		for (int i=0;i<l.size();i++) {
			bcc[i] = ((Contact) l.get(i)).getEmail(); 
		}
		return bcc;
	}
	public String[] getTo(String feedbackSubject,String webSite){
		return listToArray(getEmails(feedbackSubject, getConfig().getTo(), webSite));
		
	}
	public String[] getCc(String feedbackSubject,String webSite){
		return listToArray(getEmails(feedbackSubject, getConfig().getCc(), webSite));
	}
	public String[] getBcc(String feedbackSubject,String webSite){
		return listToArray(getEmails(feedbackSubject, getConfig().getBcc(), webSite));
	}
	
	private List getEmails(String feedbackId,String contactType, String webSite) {
		String[] names = {"siteName", "feedbackType", "feedbackSubject", "contactType"};
		Object[] values = new Object[names.length];
		values[0] = webSite;
		values[1] = getConfig().getContactUs();
		values[2] = feedbackId;
		values[3] = contactType;
		List l = getHibernateTemplate().findByNamedQueryAndNamedParam("GetContactUsEmailsByWebSite", names, values);
		List out = new ArrayList();
		Iterator i = l.iterator();
		while(i.hasNext()) {
			Contact c = new Contact();
			c.setEmail((String)i.next());
			out.add(c);
		}
		return out;
	}
	
	public String[] getTo(String feedbackSubject,String webSite,Long storeNumber, String state){
		return listToArray(getEmails(feedbackSubject, webSite, getConfig().getTo(),  storeNumber, state));
	}
	public String[] getCc(String feedbackSubject,String webSite,Long storeNumber, String state){
		return listToArray(getEmails(feedbackSubject, webSite,  getConfig().getCc(), storeNumber, state));
	}
	public String[] getBcc(String feedbackSubject,String webSite,Long storeNumber, String state){
		return listToArray(getEmails(feedbackSubject, webSite,  getConfig().getBcc(), storeNumber, state));
	}

	public List getMainFeedbacks() {
		String names = "siteName";
		Object values = (Object) getConfig().getSiteName();
		return getHibernateTemplate().findByNamedQueryAndNamedParam("BFRCWebSiteMainFeedback", names, values);
	}

	public List getFeedbacksByMain(Integer mainFeedbackId) {
		String[] names = {"siteName", "mainFeedbackId"};
		Object[] values = new Object[] {getConfig().getSiteName(), mainFeedbackId};
		return getHibernateTemplate().findByNamedQueryAndNamedParam("BFRCWebSiteFeedbackByMain", names, values);
	}
}
