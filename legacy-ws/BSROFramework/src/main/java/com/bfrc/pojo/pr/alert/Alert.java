// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:37 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Alert.java

package com.bfrc.pojo.pr.alert;

import com.bfrc.pojo.pradmin.site.Site;

import java.io.Serializable;
import java.util.*;

// Referenced classes of package com.bfrc.pr.alert:
//            AbstractAlert

public class Alert extends AbstractAlert
    implements Serializable
{

    public Alert()
    {
    	this.sites = new HashSet();
    }

    public Alert(String AlertId)
    {
        super(AlertId);
        this.sites = new HashSet();
    }

    public Set getSites()
    {
        return this.sites;
    }

    public void setSites(Set newSites)
    {
    	this.sites = newSites;
    }

    public ArrayList getSitesIDs()
    {
        ArrayList siteids = new ArrayList();
        String id;
        for(Iterator iter = this.sites.iterator(); iter.hasNext(); siteids.add(id))
        {
            Site site = (Site)iter.next();
            id = site.getSiteId();
        }

        return siteids;
    }

    public void addSite(Site site)
    {
    	this.sites.add(site);
        site.getAlerts().add(this);
    }

    public void removeSite(Site site)
    {
    	this.sites.remove(site);
        site.getAlerts().remove(this);
    }

    public int getSitecount()
    {
        return this.sites.size();
    }

    private Set sites;
}