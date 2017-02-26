// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:57:01 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   SiteService.java

package com.bfrc.framework.dao.pr;

import com.bfrc.pojo.pradmin.site.Site;
import com.bfrc.framework.spring.*;
import java.util.*;

// Referenced classes of package com.bfrc.pradmin.site:
//            Site

public class SiteService extends HibernateDAOImpl
{

    public SiteService()
    {
    }

    public List getSites() {
    	return getSites(false);
    }
    
    public List getSites(boolean fetch)
    {
        System.out.println("###get all sites (fetch=" + fetch + ")");
        String hql = "from Site s left join fetch s.alerts a";
        if(fetch)
        	hql += " left join fetch a.sites";
        return getHibernateTemplate().find(hql);
    }

    public Site getSite(String primaryKey)
    {
        System.out.println("###get site - primary key");
        return (Site)getHibernateTemplate().get(com.bfrc.pojo.pradmin.site.Site.class, primaryKey);
    }

    public void update(Site site)
    {
        System.out.println("###update site");
        getHibernateTemplate().update(site);
        return;
    }

    public void insert(Site site)
    {
        System.out.println("###insert site");
        getHibernateTemplate().save(site);
        return;
    }

    public void delete(Site site)
    {
        System.out.println("###delete site");
        getHibernateTemplate().delete(site);
        return;
    }

}