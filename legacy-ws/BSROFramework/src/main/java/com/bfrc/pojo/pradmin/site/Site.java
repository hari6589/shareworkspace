// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:57:01 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Site.java

package com.bfrc.pojo.pradmin.site;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

// Referenced classes of package com.bfrc.pradmin.site:
//            AbstractSite

public class Site extends AbstractSite
    implements Serializable
{

    public Site()
    {
    	this.alerts = new HashSet();
    }

    public Site(String SiteId)
    {
        super(SiteId);
        this.alerts = new HashSet();
    }

    public Set getAlerts()
    {
        return this.alerts;
    }

    public void setAlerts(Set newAlerts)
    {
    	this.alerts = newAlerts;
    }

    public int getAlertcount()
    {
        return this.alerts.size();
    }

    private Set alerts;
}