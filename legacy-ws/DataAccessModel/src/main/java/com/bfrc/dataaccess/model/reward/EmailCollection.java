package com.bfrc.dataaccess.model.reward;

import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_DEFAULT)
@JsonIgnoreProperties({"modifiedDate"})
public class EmailCollection  implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
	
    private String tendigitcode;
    private String firstname;
    private String lastname;
    private String email;
    private String bestCustomer;
    private Date modifiedDate;

    public EmailCollection() {
    }

	
    public EmailCollection(String tendigitcode) {
        this.tendigitcode = tendigitcode;
    }
    
    public EmailCollection(String tendigitcode, String firstname, String lastname, String email, String bestCustomer, Date modifiedDate) {
       this.tendigitcode = tendigitcode;
       this.firstname = firstname;
       this.lastname = lastname;
       this.email = email;
       this.bestCustomer = bestCustomer;
       this.modifiedDate = modifiedDate;
    }
   
    public String getTendigitcode() {
        return this.tendigitcode;
    }
    
    public void setTendigitcode(String tendigitcode) {
        this.tendigitcode = tendigitcode;
    }
    
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getBestCustomer() {
        return this.bestCustomer;
    }
    
    public void setBestCustomer(String bestCustomer) {
        this.bestCustomer = bestCustomer;
    }
    
    public Date getModifiedDate() {
        return this.modifiedDate;
    }
    
    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}


