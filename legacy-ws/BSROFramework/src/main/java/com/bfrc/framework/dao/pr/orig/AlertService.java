// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:37 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AlertService.java

package com.bfrc.framework.dao.pr.orig;

import com.bfrc.pojo.pr.alert.Alert;
import com.bfrc.pojo.pradmin.site.Site;
import com.bfrc.framework.spring.*;
import java.util.*;
import org.hibernate.*;

// Referenced classes of package com.bfrc.pr.alert:
//            Alert

public class AlertService extends HibernateDAOImpl
{

    public AlertService()
    {
    }

    public ArrayList getAlerts()
    {
        return getAlerts("desc");
    }

    public ArrayList getAlerts(String order)
    {
        ArrayList alerts;
        alerts = new ArrayList();
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        try
        {
            System.out.println("###get all alerts");
            session = factory.openSession();
            tx = session.beginTransaction();
            String hql = "from Alert a left join fetch a.sites order by a.alertExpirationDateSearch ";
            if(order.equals("asc"))
                hql += "asc";
            else
                hql += "desc";
            alerts = (ArrayList)getHibernateTemplate().find(hql);
            tx.commit();
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
            if(tx != null)
                try
                {
                    tx.rollback();
                }
                catch(HibernateException e1)
                {
                    e1.printStackTrace();
                }
        }
        finally
        {
            if(session != null)
                try
                {
                    session.close();
                }
                catch(HibernateException e2)
                {
                    e2.printStackTrace();
                }
        }
        return alerts;
    }

    public Alert getAlert(String primaryKey)
    {
        Alert alert;
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        alert = null;
        try
        {
            System.out.println("###get alert - primary key");
            session = factory.openSession();
            tx = session.beginTransaction();
            alert = (Alert)session.get(com.bfrc.pojo.pr.alert.Alert.class, primaryKey);
            tx.commit();
        }
        catch(HibernateException e)
        {
            e.printStackTrace();
            if(tx != null)
                try
                {
                    tx.rollback();
                }
                catch(HibernateException e1)
                {
                    e1.printStackTrace();
                }
        }
        finally
        {
            if(session != null)
                try
                {
                    session.flush();
                    session.close();
                }
                catch(HibernateException e2)
                {
                    e2.printStackTrace();
                }
        }
        return alert;
    }

    public void update(Alert alert, String sitelist[])
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###update alert");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(alert);
            ArrayList allsites = (ArrayList)session.createCriteria(com.bfrc.pojo.pradmin.site.Site.class).list();
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

            tx.commit();
        }
        catch(HibernateException e)
        {
            if(tx != null)
                try
                {
                    tx.rollback();
                }
                catch(HibernateException he)
                {
                    System.err.println("Update Rollback Hibernate Exception:" + he.getMessage());
                    he.printStackTrace();
                }
            System.err.println("Hibernate Exception:" + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                session.flush();
                session.close();
            }
            catch(HibernateException he)
            {
                System.err.println("Update Close Hibernate Exception:" + he.getMessage());
                he.printStackTrace();
            }
        }
        return;
    }

    public void insert(Alert alert, String sitelist[])
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###insert alert, id="+alert.getAlertId());
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            if(sitelist != null)
            {
                for(int i = 0; i < sitelist.length; i++)
                {
                    String key = sitelist[i];
//                    String hql = "from Site s left join fetch s.alerts where s.siteId=?";
                    Site site = (Site)session.load(Site.class, key);
                    alert.addSite(site);
                }

            }
            getHibernateTemplate().save(alert);
            session.save(alert);
            tx.commit();
        }
        catch(HibernateException e)
        {
            if(tx != null)
                try
                {
                    tx.rollback();
                }
                catch(HibernateException he)
                {
                    System.err.println("Insert Rollback Hibernate Exception:" + he.getMessage());
                    he.printStackTrace();
                }
            System.err.println("Hibernate Exception:" + e.getMessage());
            e.printStackTrace();
        }
/*
        finally
        {
            try
            {
                session.flush();
                session.close();
            }
            catch(HibernateException he)
            {
                System.err.println("Insert Close Hibernate Exception:" + he.getMessage());
                he.printStackTrace();
            }
        }
*/
        return;
    }

    public void delete(Alert alert)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###delete alert: "+alert);
        SessionFactory factory = getSessionFactory();
        try
        {
        	Set sites = alert.getSites();
        	Iterator i = sites.iterator();
        	while(i.hasNext())
        		alert.removeSite((Site)i.next());
            session = factory.openSession();
            tx = session.beginTransaction();
            session.delete(alert);
            tx.commit();
        }
        catch(HibernateException e)
        {
            if(tx != null)
                try
                {
                    tx.rollback();
                }
                catch(HibernateException he)
                {
                    System.err.println("Delete Rollback Hibernate Exception:" + he.getMessage());
                    he.printStackTrace();
                }
            System.err.println("Hibernate Exception:" + e.getMessage());
            e.printStackTrace();
        }
        finally
        {
            try
            {
                session.close();
            }
            catch(HibernateException he)
            {
                System.err.println("Delete Close Hibernate Exception:" + he.getMessage());
                he.printStackTrace();
            }
        }
        return;
    }

}