// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:56:45 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReleaseService.java

package com.bfrc.framework.dao.pr.orig;

import com.bfrc.pojo.pr.pressrelease.Release;
import com.bfrc.framework.spring.*;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.*;
import org.hibernate.criterion.*;

// Referenced classes of package com.bfrc.pr.pressrelease:
//            Release

public class ReleaseService extends HibernateDAOImpl
{

    public ReleaseService()
    {
    }

    public ArrayList getReleases()
    {
        return getReleases("desc", null, null, null, -1);
    }

    public ArrayList getReleases(int maxresults)
    {
        return getReleases("desc", null, null, null, maxresults);
    }

    public ArrayList getReleases(Date startDate, Date endDate, String keyword)
    {
        return getReleases("desc", startDate, endDate, keyword, -1);
    }

    public ArrayList getReleases(Date startDate, Date endDate, String keyword, int maxresults)
    {
        return getReleases("desc", startDate, endDate, keyword, maxresults);
    }

    public ArrayList getReleases(String order, Date startDate, Date endDate, String keyword, int maxresults)
    {
        ArrayList releases;
        releases = new ArrayList();
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        try
        {
            System.out.println("###get all releases by keyword/date");
            session = factory.openSession();
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(com.bfrc.pojo.pr.pressrelease.Release.class);
            if(order.equals("desc"))
                c.addOrder(Order.desc("releaseReleaseDateSearch"));
            else
                c.addOrder(Order.asc("releaseReleaseDateSearch"));
            if(startDate != null && endDate != null)
                c.add(Restrictions.between("releaseReleaseDateSearch", startDate, endDate));
            if(keyword != null)
                c.add(Restrictions.like("releaseCopy", keyword, MatchMode.ANYWHERE));
            if(maxresults > 0)
                c.setMaxResults(maxresults);
            releases = (ArrayList)c.list();
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
        return releases;
    }

    public Release getRelease(String primaryKey)
    {
        Release release;
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        release = null;
        try
        {
            System.out.println("###get release - primary key");
            session = factory.openSession();
            tx = session.beginTransaction();
            release = (Release)session.get(com.bfrc.pojo.pr.pressrelease.Release.class, primaryKey);
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
        return release;
    }

    public void update(Release release)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###update release");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(release);
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

    public void insert(Release release)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###insert release");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.save(release);
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

    public void delete(Release release)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###delete release");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.delete(release);
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