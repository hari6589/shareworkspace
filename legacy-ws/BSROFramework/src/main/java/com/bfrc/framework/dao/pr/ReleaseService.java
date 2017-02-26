// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:56:45 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   ReleaseService.java

package com.bfrc.framework.dao.pr;

import com.bfrc.pojo.pr.pressrelease.Release;
import com.bfrc.framework.spring.*;
import java.util.*;

import org.springframework.orm.hibernate3.*;

// Referenced classes of package com.bfrc.pr.pressrelease:
//            Release

public class ReleaseService extends HibernateDAOImpl
{

    public ReleaseService()
    {
    }

    public List getReleases()
    {
        return getReleases("desc", null, null, null, -1);
    }

    public List getReleases(int maxresults)
    {
        return getReleases("desc", null, null, null, maxresults);
    }

    public List getReleases(Date startDate, Date endDate, String keyword)
    {
        return getReleases("desc", startDate, endDate, keyword, -1);
    }

    public List getReleases(Date startDate, Date endDate, String keyword, int maxresults)
    {
        return getReleases("desc", startDate, endDate, keyword, maxresults);
    }

    public List getReleases(String order, Date startDate, Date endDate, String keyword, int maxresults)
    {
        ArrayList args = new ArrayList();
        System.out.println("###get all releases by keyword/date");
        String hql = "from Release r";
        if(startDate != null && endDate != null) {
        	hql += " where r.releaseReleaseDateSearch between (?, ?)";
            args.add(startDate);
            args.add(endDate);
        }
        if(keyword != null) {
        	if(args.size() == 0)
        		hql += " where ";
        	else hql += " and ";
        	hql += " r.releaseCopy like ?";
            args.add("%" + keyword + "%");
        }
        HibernateTemplate template = getHibernateTemplate(); 
        if(maxresults > 0)
            template.setMaxResults(maxresults);
        hql += " order by r.releaseReleaseDateSearch " + order;
        return template.find(hql, args.toArray());
    }

    public Release getRelease(String primaryKey)
    {
        System.out.println("###get release - primary key");
        return (Release)getHibernateTemplate().get(com.bfrc.pojo.pr.pressrelease.Release.class, primaryKey);
    }

    public void update(Release release)
    {
        System.out.println("###update release");
        getHibernateTemplate().update(release);
        return;
    }

    public void insert(Release release)
    {
        System.out.println("###insert release");
        getHibernateTemplate().save(release);
        return;
    }

    public void delete(Release release)
    {
        System.out.println("###delete release");
        getHibernateTemplate().delete(release);
        return;
    }

}