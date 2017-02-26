// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:53:43 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   UserService.java

package com.bfrc.framework.dao.pr;

import com.bfrc.pojo.admin.GlobalAdmin;
import com.bfrc.framework.spring.*;
import java.util.ArrayList;
import org.hibernate.*;
import org.hibernate.criterion.*;

// Referenced classes of package com.bfrc.pradmin.user:
//            User

public class UserService extends HibernateDAOImpl
{

    public UserService()
    {
    }

    public ArrayList getUsers()
    {
        ArrayList users;
        users = new ArrayList();
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        try
        {
            System.out.println("###get all users");
            session = factory.openSession();
            tx = session.beginTransaction();
// FIXME get admins with pr access
            users = (ArrayList)session.createCriteria(com.bfrc.pojo.admin.GlobalAdmin.class).list();
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
        return users;
    }

    public GlobalAdmin isValidUser(String username, String password)
    {
        GlobalAdmin user = getUser(username, password);
        if(user != null)
            return user;
        return null;
    }

    public GlobalAdmin getUser(Long primaryKey)
    {
    	GlobalAdmin user;
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        user = null;
        try
        {
            System.out.println("###get user - primary key");
            session = factory.openSession();
            tx = session.beginTransaction();
            user = (GlobalAdmin)session.get(com.bfrc.pojo.admin.GlobalAdmin.class, primaryKey);
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
        return user;
    }

    private GlobalAdmin getUser(String username, String password)
    {
        Session session;
        SessionFactory factory;
        session = null;
        factory = getSessionFactory();
        GlobalAdmin user;
        try
        {
            System.out.println("###get user");
            System.out.println("###get username:" + username);
            System.out.println("###get userpass:" + password);
            session = factory.openSession();
            user = (GlobalAdmin)session.createCriteria(com.bfrc.pojo.admin.GlobalAdmin.class).add(Restrictions.eq("name", username)).add(Restrictions.eq("password", password)).uniqueResult();
        } catch(ObjectNotFoundException onfe) {
        	System.out.println("Object Not Found Exception:" + onfe.getMessage());
        	user = null;
        } catch(HibernateException e) {
        	System.out.println("Hibernate Exception:" + e.getMessage());
        	user = null;
        } finally {
            if(session != null)
                try
                {
                    session.close();
                }
                catch(HibernateException e)
                {
                    System.err.println("Hibernate Exception:" + e.getMessage());
                    throw new RuntimeException(e);
                }
        }
        return user;
    }

    public void update(GlobalAdmin user)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###update user");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(user);
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
            System.err.println("###update user:" + user.getUserId());
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

}