// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:57 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ArticleService.java

package com.bfrc.framework.dao.pr;

import org.springframework.orm.hibernate3.*;

import com.bfrc.pojo.pr.article.Article;
import com.bfrc.framework.spring.*;
import java.util.*;

// Referenced classes of package com.bfrc.pr.article:
//            Article

public class ArticleService extends HibernateDAOImpl
{

    public ArticleService()
    {
    }

    public List getArticles()
    {
        return getArticles("desc", null, null, null, -1);
    }

    public List getArticles(int maxresults)
    {
        return getArticles("desc", null, null, null, maxresults);
    }

    public List getArticles(Date startDate, Date endDate, String keyword)
    {
        return getArticles("desc", startDate, endDate, keyword, -1);
    }

    public List getArticles(String order, Date startDate, Date endDate, String keyword, int maxresults)
    {
        ArrayList args = new ArrayList();
        System.out.println("###get all articles by keyword/date");
        String hql = "from Article a";
        if(startDate != null && endDate != null) {
        	hql += " where a.articlePublicationDateSearch between (?, ?)";
            args.add(startDate);
            args.add(endDate);
        }
        if(keyword != null) {
        	if(args.size() == 0)
        		hql += " where ";
        	else hql += " and ";
        	hql += " a.articleCopy like ?";
            args.add("%" + keyword + "%");
        }
        HibernateTemplate template = getHibernateTemplate(); 
        if(maxresults > 0)
            template.setMaxResults(maxresults);
        hql += " order by a.articlePublicationDateSearch " + order;
        return template.find(hql, args.toArray());
    }

    public Article getArticle(String primaryKey)
    {
        System.out.println("###get article - primary key");
        return (Article)getHibernateTemplate().get(com.bfrc.pojo.pr.article.Article.class, primaryKey);
    }

    public void update(Article article)
    {
        System.out.println("###update article");
        getHibernateTemplate().update(article);
        return;
    }

    public void insert(Article article)
    {
        System.out.println("###insert article");
        getHibernateTemplate().save(article);
        return;
    }

    public void delete(Article article)
    {
        System.out.println("###delete article");
        getHibernateTemplate().delete(article);
        return;
    }

}