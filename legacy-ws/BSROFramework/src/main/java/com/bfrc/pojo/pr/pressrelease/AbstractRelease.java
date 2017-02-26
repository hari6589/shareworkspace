// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:56:45 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AbstractRelease.java

package com.bfrc.pojo.pr.pressrelease;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.bfrc.pr.pressrelease:
//            Release

public abstract class AbstractRelease
    implements Serializable
{

    public AbstractRelease()
    {
    	this.hashValue = 0;
    }

    public AbstractRelease(String releaseId)
    {
    	this.hashValue = 0;
        setReleaseId(releaseId);
    }

    public String getReleaseId()
    {
        return this.releaseId;
    }

    public void setReleaseId(String releaseId)
    {
    	this.hashValue = 0;
        this.releaseId = releaseId;
    }

    public String getReleaseContactInfo()
    {
        return this.releaseContactInfo;
    }

    public void setReleaseContactInfo(String releaseContactInfo)
    {
        this.releaseContactInfo = releaseContactInfo;
    }

    public String getReleaseCopy()
    {
        return this.releaseCopy;
    }

    public void setReleaseCopy(String releaseCopy)
    {
        this.releaseCopy = releaseCopy;
    }

    public String getReleaseReleaseDate()
    {
        return this.releaseReleaseDate;
    }

    public void setReleaseReleaseDate(String releaseReleaseDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        if(releaseReleaseDate != null && !releaseReleaseDate.equals(""))
            try
            {
                Date newDate = sdf.parse(releaseReleaseDate);
                setReleaseReleaseDateSearch(newDate);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        this.releaseReleaseDate = releaseReleaseDate;
    }

    public String getReleaseSubtitle()
    {
        return this.releaseSubtitle;
    }

    public void setReleaseSubtitle(String releaseSubtitle)
    {
        this.releaseSubtitle = releaseSubtitle;
    }

    public String getReleaseTitle()
    {
        return this.releaseTitle;
    }

    public void setReleaseTitle(String releaseTitle)
    {
        this.releaseTitle = releaseTitle;
    }

    public boolean equals(Object rhs)
    {
        if(rhs == null)
            return false;
        if(!(rhs instanceof Release))
            return false;
        Release that = (Release)rhs;
        return getReleaseId() == null || that.getReleaseId() == null || getReleaseId().equals(that.getReleaseId());
    }

    public int hashCode()
    {
        if(this.hashValue == 0)
        {
            int result = 17;
            int releaseIdValue = getReleaseId() != null ? getReleaseId().hashCode() : 0;
            result = result * 37 + releaseIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

    public Long getReleaseCreatorId()
    {
        return this.releaseCreatorId;
    }

    public void setReleaseCreatorId(Long releaseCreatorId)
    {
        this.releaseCreatorId = releaseCreatorId;
    }

    public Date getReleaseReleaseDateSearch()
    {
        return this.releaseReleaseDateSearch;
    }

    public void setReleaseReleaseDateSearch(Date releaseReleaseDateSearch)
    {
        this.releaseReleaseDateSearch = releaseReleaseDateSearch;
    }

    private int hashValue;
    private String releaseId;
    private String releaseTitle;
    private String releaseSubtitle;
    private String releaseReleaseDate;
    private String releaseCopy;
    private String releaseContactInfo;
    private Long releaseCreatorId;
    private Date releaseReleaseDateSearch;
}