package com.bfrc.dataaccess.dao.hibernate;

import java.util.Calendar;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;

import com.bfrc.dataaccess.dao.donation.DonationDAO;
import com.bfrc.dataaccess.model.donation.DonationProgram;

public class DonationDAOImpl extends HibernateDaoSupport implements DonationDAO {

	private PlatformTransactionManager txManager;
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	
	@SuppressWarnings("unchecked")
	public List<DonationProgram> getCurrentDonationProgramsForSiteSortedByStartDate(String siteName) {
		Session session = getSession();
		List<DonationProgram> donationPrograms = null;
		
		Calendar currentDate = Calendar.getInstance();
		
		try {
			// Currently, as of 09/2012, the sites only support the notion of one donation program at a time
			// We have heard that they will want to offer a selection of different donation programs in the future
			// This query could be a "uniqueResult" query, but there is no good way to enforce uniqueness constraints in a valid way at the database
			// level because the same article number could legitimately appear under different names during different date ranges.
			// So, we return a list here and let the caller worry about which one to choose.
			donationPrograms = (List<DonationProgram>) session.createCriteria(DonationProgram.class)
					.add(Restrictions.eq("webSite", siteName))
					.add(Restrictions.le("startDate", currentDate)).add(Restrictions.ge("endDate", currentDate)).addOrder(Order.desc("startDate")).list();
		} finally {
			if (session != null) {
				try {
					this.releaseSession(session);
				} catch (HibernateException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return donationPrograms;
	}

}
