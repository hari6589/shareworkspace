/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Wed Nov 02 17:10:28 CST 2005 by MyEclipse Hibernate Tool.
 */
package com.bfrc.pojo.email;

import java.io.Serializable;

/**
 * A class that represents a row in the EMAIL_UNSUBSCRIBE table. 
 * You can customize the behavior of this class by editing the class, {@link EmailUnsubscribe()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractEmailUnsubscribe 
    implements Serializable, IEmailUnsubscribe
{
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.Long unsubscribeId;

    /** The value of the simple emailAddress property. */
    private java.lang.String emailAddress;

    /** The value of the simple unsubscribeDate property. */
    private java.util.Date unsubscribeDate;

    /** The value of the simple source property. */
    private java.lang.String source;

    /**
     * Simple constructor of AbstractEmailUnsubscribe instances.
     */
    public AbstractEmailUnsubscribe()
    {
    }

    /**
     * Constructor of AbstractEmailUnsubscribe instances given a simple primary key.
     * @param unsubscribeId
     */
    public AbstractEmailUnsubscribe(java.lang.Long unsubscribeId)
    {
        this.setUnsubscribeId(unsubscribeId);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#getUnsubscribeId()
	 */
    public java.lang.Long getUnsubscribeId()
    {
        return this.unsubscribeId;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#setUnsubscribeId(java.lang.Long)
	 */
    public void setUnsubscribeId(java.lang.Long unsubscribeId)
    {
        this.hashValue = 0;
        this.unsubscribeId = unsubscribeId;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#getEmailAddress()
	 */
    public java.lang.String getEmailAddress()
    {
        return this.emailAddress;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#setEmailAddress(java.lang.String)
	 */
    public void setEmailAddress(java.lang.String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#getUnsubscribeDate()
	 */
    public java.util.Date getUnsubscribeDate()
    {
        return this.unsubscribeDate;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#setUnsubscribeDate(java.util.Date)
	 */
    public void setUnsubscribeDate(java.util.Date unsubscribeDate)
    {
        this.unsubscribeDate = unsubscribeDate;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#getSource()
	 */
    public java.lang.String getSource()
    {
        return this.source;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailUnsubscribe#setSource(java.lang.String)
	 */
    public void setSource(java.lang.String source)
    {
        this.source = source;
    }

    /**
     * Implementation of the equals comparison on the basis of equality of the primary key values.
     * @param rhs
     * @return boolean
     */
    public boolean equals(Object rhs)
    {
        if (rhs == null)
            return false;
        if (! (rhs instanceof EmailUnsubscribe))
            return false;
        EmailUnsubscribe that = (EmailUnsubscribe) rhs;
        if (this.getUnsubscribeId() == null || that.getUnsubscribeId() == null)
            return false;
        return (this.getUnsubscribeId().equals(that.getUnsubscribeId()));
    }

    /**
     * Implementation of the hashCode method conforming to the Bloch pattern with
     * the exception of array properties (these are very unlikely primary key types).
     * @return int
     */
    public int hashCode()
    {
        if (this.hashValue == 0)
        {
            int result = 17;
            int unsubscribeIdValue = this.getUnsubscribeId() == null ? 0 : this.getUnsubscribeId().hashCode();
            result = result * 37 + unsubscribeIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }
}
