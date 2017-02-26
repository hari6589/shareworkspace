package com.bfrc.framework.dao;

import java.util.*;

import com.bfrc.pojo.*;
import com.bfrc.pojo.partner.*;

public interface PartnerDAO extends UserDAO {
	void enrollCompany(Company c) throws Exception;
	void signupEmployee(User u, UserVehicle v, Company c) throws Exception;
	void insertRecord(Record r, User u, Company c);
	Company findCompanyById(Long id);
	Company findCompanyBySignupCode(String c);
	List getRecords(String status, String sort);
	void deleteRecord(Long id);
	void updateRecordNote(Long id, String note, Date contactDate, String contactType);
	void updateRecordStatus(Long id, String status);
	Record findRecordById(Long id);
	List getCompanies(String status, String sort);
	List getCompanies(String status, String sort, int page);
	int getTotalCompanies(String status);
	List getSignupHistory(Long filter);
	List getSignupPercentages();
	List getActiveCompanies();
	void updateSignupCode(Company c, String signupCode) throws Exception;
	void updateLogo(Company c, byte[] image) throws Exception;
	List getActiveSignupCompanies();
	void deleteCompany(Company c) throws Exception;
	void updateTerms(Company c, boolean terms) throws Exception;
	byte[] getWelcomePDF(Company c) throws Exception;
	List findCompaniesByKeyword(String keyword);
	void updateEmailAddress(Company c, String email) throws Exception;
    void updateCompany(Company c, byte[] image) throws Exception;
	void updateCompany(Company c) throws Exception;
	List searchRecords(String firstName, String lastName, String companyName, String emailAddress,String sort);
}
