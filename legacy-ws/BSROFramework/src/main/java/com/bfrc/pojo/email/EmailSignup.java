/*
 * Created Fri Oct 28 09:57:01 CDT 2005 by MyEclipse Hibernate Tool.
 */ 
package com.bfrc.pojo.email;

import java.io.Serializable;

/**
 * A class that represents a row in the 'EMAIL_SIGNUP' table. 
 * This class may be customized as it is never re-generated 
 * after being created.
 */
public class EmailSignup
    extends AbstractEmailSignup
    implements Serializable
{
    /**
     * Simple constructor of EmailSignup instances.
     */
    public EmailSignup()
    {
    }

    /**
     * Constructor of EmailSignup instances given a simple primary key.
     * @param signupId
     */
    public EmailSignup(java.lang.Long signupId)
    {
        super(signupId);
    }

    /* Add customized code below */

}
