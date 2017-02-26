package com.bfrc.dataaccess.svc.donation;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bfrc.dataaccess.dao.donation.DonationDAO;
import com.bfrc.dataaccess.model.donation.DonationProgram;

@Service
public class DonationServiceImpl implements IDonationService {

	private Log logger = LogFactory.getLog(DonationServiceImpl.class);

	private DonationDAO donationDao;
	
	public DonationDAO getDonationDao() {
		return donationDao;
	}

	public void setDonationDao(DonationDAO donationDAO) {
		this.donationDao = donationDAO;
	}
	
	public DonationProgram getActiveDonationProgramForSite(String siteName) {
		// Currently, as of 09/2012, the sites only support the notion of one donation program at a time
		// We have heard that they will want to offer a selection of different donation programs in the future
		// This query could be a "uniqueResult" query, but there is no good way to enforce uniqueness constraints in a valid way at the database
		// level because the same article number could legitimately appear under different names during different date ranges.
		// So, we return a list here and choose the first item for now. The result should be sorted by start date and therefore return a consistent result.
		List<DonationProgram> donationPrograms = donationDao.getCurrentDonationProgramsForSiteSortedByStartDate(siteName);
		if (donationPrograms != null && !donationPrograms.isEmpty()) {
			return donationPrograms.get(0);
		}
		return null;
	}

}
