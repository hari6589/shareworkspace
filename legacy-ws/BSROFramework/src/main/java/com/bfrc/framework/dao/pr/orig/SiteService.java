// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:57:01 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SiteService.java

package com.bfrc.framework.dao.pr.orig;

import com.bfrc.pojo.pradmin.site.Site;
import com.bfrc.framework.spring.*;
import java.util.ArrayList;
import org.hibernate.*;

// Referenced classes of package com.bfrc.pradmin.site:
//            Site

public class SiteService extends HibernateDAOImpl
{

    public SiteService()
    {
    }

    public ArrayList getSites() {return getSites(false);}
    public ArrayList getSites(boolean fetch)
    {
        ArrayList sites;
        sites = new ArrayList();
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        try
        {
            System.out.println("###get all sites (fetch="+fetch+")");
            session = factory.openSession();
            tx = session.beginTransaction();
            String hql = "from Site s";
            if(fetch)
            	hql += " left join fetch s.alerts a left join fetch a.sites";
            sites = (ArrayList)getHibernateTemplate().find(hql);
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
        return sites;
    }

    public Site getSite(String primaryKey)
    {
        Site site;
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        site = null;
        try
        {
            System.out.println("###get site - primary key");
            session = factory.openSession();
            tx = session.beginTransaction();
            site = (Site)session.get(com.bfrc.pojo.pradmin.site.Site.class, primaryKey);
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
        return site;
    }

    public void update(Site site)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###update site");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(site);
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

    public void insert(Site site)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###insert site");
        SessionFactory factory = getSessionFactory();
        try
        {
        	session = factory.openSession();
        	tx = session.beginTransaction();
            session.save(site);
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
        return;
    }

    public void delete(Site site)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###delete site");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.delete(site);
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