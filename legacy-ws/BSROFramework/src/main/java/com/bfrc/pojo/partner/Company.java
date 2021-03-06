package com.bfrc.pojo.partner;
// Generated Nov 27, 2006 7:58:57 AM by Hibernate Tools 3.1.0.beta4

import com.bfrc.Bean;
import com.bfrc.pojo.User;

import java.util.*;


/**
 * Company generated by hbm2java
 */

public class Company  implements Bean {

    // Fields    

	private Set signups = new HashSet(0);
	private int signupCount;
     private long id;
     private Date createdDate;
     private boolean active;
     private String name;
     private String signupCode;
     private String message;
     private String question1;
     private String question2;
     private String question3;
     private String employees;
     private byte[] image;
     private Boolean acceptTerms = new Boolean(false);
     private String emailAddress;
     private String status;

     public String toString() {
    	 StringBuffer sb = new StringBuffer("ID: " + id + "\nActive: " + active + "\n");
    	 if(name != null)
    		 sb.append("Name: " + name + "\n");
    	 if(emailAddress != null)
    		 sb.append("E-mail Address: " + emailAddress + "\n");
    	 if(signupCode != null)
    		 sb.append("Signup Code: " + signupCode + "\n");
    	 if(message != null)
    		 sb.append("Message: " + message + "\n");
    	 if(question1 != null)
    		 sb.append("Question 1: " + question1 + "\n");
    	 if(question2 != null)
    		 sb.append("Question 2: " + question2 + "\n");
    	 if(question3 != null)
    		 sb.append("Question 3: " + question3 + "\n");
    	 if(employees != null)
    		 sb.append("Employees: " + employees + "\n");
    	 if(acceptTerms != null)
    		 sb.append("Terms Accepted: " + acceptTerms + "\n");
    	 if(image != null && image.length > 0)
    		 sb.append("Logo present\n");
    	 return sb.toString();
     }

    // Constructors

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	/** default constructor */
    public Company() {
    }

	/** minimal constructor */
    public Company(long id, Date createdDate, boolean active, String name) {
        this.id = id;
        this.createdDate = createdDate;
        this.active = active;
        this.name = name;
    }
    
    /** full constructor */
    public Company(long id, Date createdDate, boolean active, String name, String signupCode, String message, String question1, String question2, String question3, String employees, User user) {
        this.id = id;
        this.createdDate = createdDate;
        this.active = active;
        this.name = name;
        this.signupCode = signupCode;
        this.message = message;
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
        this.employees = employees;
    }
    

   
    // Property accessors

    public long getId() {
        return this.id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActive() {
        return this.active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getSignupCode() {
        return this.signupCode;
    }
    
    public void setSignupCode(String signupCode) {
        this.signupCode = signupCode;
    }

    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuestion1() {
        return this.question1;
    }
    
    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return this.question2;
    }
    
    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return this.question3;
    }
    
    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getEmployees() {
        return this.employees;
    }
    
    public void setEmployees(String employees) {
        this.employees = employees;
    }

	public Boolean getAcceptTerms() {
		return acceptTerms;
	}

	public void setAcceptTerms(Boolean acceptTerms) {
		this.acceptTerms = acceptTerms;
	}

	public int getSignupCount() {
		return signupCount;
	}

	public void setSignupCount(int signupCount) {
		this.signupCount = signupCount;
	}

	public Set getSignups() {
		return signups;
	}

	public void setSignups(Set signups) {
		this.signups = signups;
	}
	
	public String getStatus() {
		if(status == null ) {
			if(signupCode == null && active) 
				status ="N";
			else
				status = active ? "A" : "I";
		}
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

}
