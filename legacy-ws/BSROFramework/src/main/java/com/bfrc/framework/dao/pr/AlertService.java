// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:37 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AlertService.java

package com.bfrc.framework.dao.pr;

import org.hibernate.*;

import com.bfrc.pojo.pr.alert.Alert;
import com.bfrc.pojo.pradmin.site.Site;
import com.bfrc.framework.spring.*;
import java.util.*;

// Referenced classes of package com.bfrc.pr.alert:
//            Alert

public class AlertService extends HibernateDAOImpl
{

    public AlertService()
    {
    }

    public List getAlerts()
    {
        return getAlerts("desc");
    }

    public List getAlerts(String order)
    {
        System.out.println("###get all alerts");
        String hql = "from Alert a left join fetch a.sites s order by a.alertExpirationDateSearch ";
        if(order.equals("asc"))
            hql += "asc";
        else
            hql += "desc";
        return getHibernateTemplate().find(hql);
    }

    public Alert getAlert(String primaryKey)
    {
        System.out.println("###get alert - primary key");
        return (Alert)getHibernateTemplate().get(Alert.class, primaryKey);
    }

    public void update(Alert alert, String sitelist[])
    {
        System.out.println("###update alert");
        List allsites = getHibernateTemplate().find("from Site s left join fetch s.alerts");
        for(Iterator i = allsites.iterator(); i.hasNext();)
        {
            Site s = (Site)i.next();
            boolean addsite = false;
            if(sitelist != null)
            {
                for(int q = 0; q < sitelist.length; q++)
                {
                    String key = sitelist[q];
                    if(!key.equals(s.getSiteId()))
                        continue;
                    addsite = true;
                    break;
                }

            }
            if(addsite)
                alert.addSite(s);
            else
                alert.removeSite(s);
        }
        getHibernateTemplate().update(alert);
        return;
    }

    public void insert(Alert myAlert, String sitelist[])
    {
    	Alert alert = myAlert;
    	Session session = null;
    	try {
    		session = getSessionFactory().openSession();
    		Transaction tx = session.beginTransaction();
	        System.out.println("###insert alert, id="+alert.getAlertId());
	        if(sitelist != null)
	        {
	            for(int i = 0; i < sitelist.length; i++)
	            {
	                String key = sitelist[i];
	                Site site = (Site)session.load(Site.class, key);
	                alert.addSite(site);
	            }
	        }
	        session.save(alert);
	        tx.commit();
    	} finally {
    		if(session != null)
    			session.close();
    	}
        return;
    }

    public void delete(Alert alert)
    {
        System.out.println("###delete alert: "+alert);
        Set sites = alert.getSites();
        Iterator i = sites.iterator();
        while(i.hasNext())
        	alert.removeSite((Site)i.next());
        getHibernateTemplate().delete(alert);
        return;
    }

}