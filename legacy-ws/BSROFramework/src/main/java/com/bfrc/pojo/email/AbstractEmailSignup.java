/*
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 *
 * Created Fri Oct 28 09:57:01 CDT 2005 by MyEclipse Hibernate Tool.
 */
package com.bfrc.pojo.email;

import java.io.Serializable;
import java.util.Date;

import com.bfrc.framework.util.StringUtils;

/**
 * A class that represents a row in the EMAIL_SIGNUP table. 
 * You can customize the behavior of this class by editing the class, {@link EmailSignup()}.
 * WARNING: DO NOT EDIT THIS FILE. This is a generated file that is synchronized
 * by MyEclipse Hibernate tool integration.
 */
public abstract class AbstractEmailSignup 
    implements Serializable, IEmailSignup
{
	private String optinCode, confirmOptin;
    /** The cached hash code value for this instance.  Settting to 0 triggers re-calculation. */
    private int hashValue = 0;

    /** The composite primary key value. */
    private java.lang.Long signupId;

    /** The value of the simple firstName property. */
    private java.lang.String firstName;

    /** The value of the simple middleInitial property. */
    private java.lang.String middleInitial;

    /** The value of the simple lastName property. */
    private java.lang.String lastName;

    /** The value of the simple address1 property. */
    private java.lang.String address1;

    /** The value of the simple address2 property. */
    private java.lang.String address2;

    /** The value of the simple city property. */
    private java.lang.String city;

    /** The value of the simple state property. */
    private java.lang.String state;

    /** The value of the simple zip property. */
    private java.lang.String zip;

    /** The value of the simple emailAddress property. */
    private java.lang.String emailAddress;

    /** The value of the simple createdDate property. */
    private java.util.Date createdDate;

    /** The value of the simple source property. */
    private java.lang.String source;
    
    
    private String couponId;
    private String friendsEmail;
    private String phoneNumber;
    private String actionCode;
    private String accessCode;
    private Date modifiedDate;

    /**
     * Simple constructor of AbstractEmailSignup instances.
     */
    public AbstractEmailSignup()
    {
    }

    /**
     * Constructor of AbstractEmailSignup instances given a simple primary key.
     * @param signupId
     */
    public AbstractEmailSignup(java.lang.Long signupId)
    {
        this.setSignupId(signupId);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getSignupId()
	 */
    public java.lang.Long getSignupId()
    {
        return this.signupId;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setSignupId(java.lang.Long)
	 */
    public void setSignupId(java.lang.Long signupId)
    {
        this.hashValue = 0;
        this.signupId = signupId;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getFirstName()
	 */
    public java.lang.String getFirstName()
    {
        return StringUtils.userNameFilter(this.firstName,50);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setFirstName(java.lang.String)
	 */
    public void setFirstName(java.lang.String firstName)
    {
        this.firstName = firstName;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getMiddleInitial()
	 */
    public java.lang.String getMiddleInitial()
    {
        return this.middleInitial;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setMiddleInitial(java.lang.String)
	 */
    public void setMiddleInitial(java.lang.String middleInitial)
    {
        this.middleInitial = middleInitial;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getLastName()
	 */
    public java.lang.String getLastName()
    {
        return StringUtils.userNameFilter(this.lastName,80);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setLastName(java.lang.String)
	 */
    public void setLastName(java.lang.String lastName)
    {
        this.lastName = lastName;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getAddress1()
	 */
    public java.lang.String getAddress1()
    {
        return StringUtils.truncate(this.address1,50);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setAddress1(java.lang.String)
	 */
    public void setAddress1(java.lang.String address1)
    {
        this.address1 = address1;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getAddress2()
	 */
    public java.lang.String getAddress2()
    {
        return StringUtils.truncate(this.address2,50);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setAddress2(java.lang.String)
	 */
    public void setAddress2(java.lang.String address2)
    {
        this.address2 = address2;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getCity()
	 */
    public java.lang.String getCity()
    {
        return StringUtils.truncate(this.city,50);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setCity(java.lang.String)
	 */
    public void setCity(java.lang.String city)
    {
        this.city = city;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getState()
	 */
    public java.lang.String getState()
    {
        return StringUtils.truncate(this.state,2);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setState(java.lang.String)
	 */
    public void setState(java.lang.String state)
    {
        this.state = state;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getZip()
	 */
    public java.lang.String getZip()
    {
        return StringUtils.truncate(this.zip,10);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setZip(java.lang.String)
	 */
    public void setZip(java.lang.String zip)
    {
        this.zip = zip;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getEmailAddress()
	 */
    public java.lang.String getEmailAddress()
    {
        return StringUtils.truncate(this.emailAddress,255);
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setEmailAddress(java.lang.String)
	 */
    public void setEmailAddress(java.lang.String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getCreatedDate()
	 */
    public java.util.Date getCreatedDate()
    {
        return this.createdDate;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setCreatedDate(java.util.Date)
	 */
    public void setCreatedDate(java.util.Date createdDate)
    {
        this.createdDate = createdDate;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#getSource()
	 */
    public java.lang.String getSource()
    {
        return this.source;
    }

    /* (non-Javadoc)
	 * @see com.bfrc.pojo.email.IEmailSignup#setSource(java.lang.String)
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
        if (! (rhs instanceof EmailSignup))
            return false;
        EmailSignup that = (EmailSignup) rhs;
        if (this.getSignupId() == null || that.getSignupId() == null)
            return false;
        return (this.getSignupId().equals(that.getSignupId()));
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
            int signupIdValue = this.getSignupId() == null ? 0 : this.getSignupId().hashCode();
            result = result * 37 + signupIdValue;
            this.hashValue = result;
        }
        return this.hashValue;
    }

	public String getOptinCode() {
		return optinCode;
	}

	public void setOptinCode(String optinCode) {
		this.optinCode = optinCode;
	}

	public String getConfirmOptin() {
		return confirmOptin;
	}

	public void setConfirmOptin(String confirmOptin) {
		this.confirmOptin = confirmOptin;
	}
	
	public String getCouponId() {
        return this.couponId;
    }
	
    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
    public String getFriendsEmail() {
        return this.friendsEmail;
    }
    
    public void setFriendsEmail(String friendsEmail) {
        this.friendsEmail = friendsEmail;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getActionCode() {
        return this.actionCode;
    }
    
    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }
    
    public String getAccessCode() {
		return accessCode;
	}
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}
	
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}