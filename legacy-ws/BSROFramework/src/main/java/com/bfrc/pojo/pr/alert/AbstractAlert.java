// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 9/26/2006 1:52:37 PM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   AbstractAlert.java

package com.bfrc.pojo.pr.alert;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Referenced classes of package com.bfrc.pr.alert:
//            Alert

public abstract class AbstractAlert
    implements Serializable
{

    public Long getAlertCreatorId()
    {
        return this.alertCreatorId;
    }

    public void setAlertCreatorId(Long alertCreatorId)
    {
        this.alertCreatorId = alertCreatorId;
    }

    public Date getAlertExpirationDateSearch()
    {
        return this.alertExpirationDateSearch;
    }

    public void setAlertExpirationDateSearch(Date alertExpirationDateSearch)
    {
        this.alertExpirationDateSearch = alertExpirationDateSearch;
    }

    public AbstractAlert()
    {
    	this.hashValue = 0;
    }

    public AbstractAlert(String AlertId)
    {
    	this.hashValue = 0;
        setAlertId(AlertId);
    }

    public String getAlertId()
    {
        return this.AlertId;
    }

    public void setAlertId(String AlertId)
    {
    	this.hashValue = 0;
        this.AlertId = AlertId;
    }

    public String getAlertTitle()
    {
        return this.alertTitle;
    }

    public void setAlertTitle(String alertTitle)
    {
        this.alertTitle = alertTitle;
    }

    public String getAlertLink()
    {
        return this.alertLink;
    }

    public void setAlertLink(String alertLink)
    {
        this.alertLink = alertLink;
    }

    public String getAlertDescription()
    {
        return this.alertDescription;
    }

    public void setAlertDescription(String alertDescription)
    {
        this.alertDescription = alertDescription;
    }

    public String getAlertExpiration()
    {
        return this.alertExpiration;
    }

    public void setAlertExpiration(String alertExpiration)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        if(alertExpiration != null && !alertExpiration.equals(""))
            try
            {
                Date newDate = sdf.parse(alertExpiration);
                setAlertExpirationDateSearch(newDate);
            }
            catch(ParseException e)
            {
                e.printStackTrace();
            }
        this.alertExpiration = alertExpiration;
    }

    public String getAlertActive()
    {
        return this.alertActive;
    }

    public void setAlertActive(String alertActive)
    {
        this.alertActive = alertActive;
    }

    public boolean equals(Object rhs)
    {
        if(rhs == null)
            return false;
        if(!(rhs instanceof Alert))
            return false;
        Alert that = (Alert)rhs;
        return getAlertId() == null || that.getAlertId() == null || getAlertId().equals(that.getAlertId());
    }

    public int hashCode()
    {
        if(this.hashValue == 0)
        {
            int result = 17;
            int AlertIdValue = getAlertId() != null ? getAlertId().hashCode() : 0;
            result = result * 37 + AlertIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

    private int hashValue;
    private String AlertId;
    private String alertTitle;
    private String alertLink;
    private String alertDescription;
    private String alertExpiration;
    private String alertActive;
    private Date alertExpirationDateSearch;
    private Long alertCreatorId;
}