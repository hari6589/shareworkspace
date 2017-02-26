// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:57 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ArticleService.java

package com.bfrc.framework.dao.pr.orig;

import com.bfrc.pojo.pr.article.Article;
import com.bfrc.framework.spring.*;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.*;
import org.hibernate.criterion.*;

// Referenced classes of package com.bfrc.pr.article:
//            Article

public class ArticleService extends HibernateDAOImpl
{

    public ArticleService()
    {
    }

    public ArrayList getArticles()
    {
        return getArticles("desc", null, null, null, -1);
    }

    public ArrayList getArticles(int maxresults)
    {
        return getArticles("desc", null, null, null, maxresults);
    }

    public ArrayList getArticles(Date startDate, Date endDate, String keyword)
    {
        return getArticles("desc", startDate, endDate, keyword, -1);
    }

    public ArrayList getArticles(String order, Date startDate, Date endDate, String keyword, int maxresults)
    {
        ArrayList articles;
        articles = new ArrayList();
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        try
        {
            System.out.println("###get all articles by keyword/date");
            session = factory.openSession();
            tx = session.beginTransaction();
            Criteria c = session.createCriteria(com.bfrc.pojo.pr.article.Article.class);
            if(order.equals("desc"))
                c.addOrder(Order.desc("articlePublicationDateSearch"));
            else
                c.addOrder(Order.asc("articlePublicationDateSearch"));
            if(startDate != null && endDate != null)
                c.add(Restrictions.between("articlePublicationDateSearch", startDate, endDate));
            if(keyword != null)
                c.add(Restrictions.like("articleCopy", keyword, MatchMode.ANYWHERE));
            if(maxresults > 0)
                c.setMaxResults(maxresults);
            articles = (ArrayList)c.list();
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
        return articles;
    }

    public Article getArticle(String primaryKey)
    {
        Article article;
        Session session = null;
        Transaction tx = null;
        SessionFactory factory = getSessionFactory();
        article = null;
        try
        {
            System.out.println("###get article - primary key");
            session = factory.openSession();
            tx = session.beginTransaction();
            article = (Article)session.get(com.bfrc.pojo.pr.article.Article.class, primaryKey);
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
        return article;
    }

    public void update(Article article)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###update article");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(article);
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

    public void insert(Article article)
    {
//        Session session = null;
        Transaction tx = null;
        System.out.println("###insert article");
//        SessionFactory factory = getSessionFactory();
        try
        {
//            session = factory.openSession();
//            tx = session.beginTransaction();
            getHibernateTemplate().save(article);
//            tx.commit();
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
//                session.flush();
//                session.close();
            }
            catch(HibernateException he)
            {
                System.err.println("Insert Close Hibernate Exception:" + he.getMessage());
                he.printStackTrace();
            }
        }
        return;
    }

    public void delete(Article article)
    {
        Session session = null;
        Transaction tx = null;
        System.out.println("###delete article");
        SessionFactory factory = getSessionFactory();
        try
        {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.delete(article);
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