// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:57:01 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AbstractSite.java

package com.bfrc.pojo.pradmin.site;

import java.io.Serializable;

// Referenced classes of package com.bfrc.pradmin.site:
//            Site

public abstract class AbstractSite
    implements Serializable
{

    public AbstractSite()
    {
    	this.hashValue = 0;
    }

    public AbstractSite(String siteId)
    {
    	this.hashValue = 0;
        setSiteId(siteId);
    }

    public String getSiteId()
    {
        return this.siteId;
    }

    public void setSiteId(String siteId)
    {
    	this.hashValue = 0;
        this.siteId = siteId;
    }

    public String getSiteName()
    {
        return this.siteName;
    }

    public void setSiteName(String siteName)
    {
        this.siteName = siteName;
    }

    public String getSiteUrl()
    {
        return this.siteUrl;
    }

    public void setSiteUrl(String siteUrl)
    {
        this.siteUrl = siteUrl;
    }

    public boolean equals(Object rhs)
    {
        if(rhs == null)
            return false;
        if(!(rhs instanceof Site))
            return false;
        Site that = (Site)rhs;
        return getSiteId() == null || that.getSiteId() == null || getSiteId().equals(that.getSiteId());
    }

    public int hashCode()
    {
        if(this.hashValue == 0)
        {
            int result = 17;
            int siteIdValue = getSiteId() != null ? getSiteId().hashCode() : 0;
            result = result * 37 + siteIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

    private int hashValue;
    private String siteId;
    private String siteName;
    private String siteUrl;
}