package com.bfrc.dataaccess.dao.donation;

import java.util.List;

import com.bfrc.dataaccess.model.donation.DonationProgram;

public interface DonationDAO {
	List<DonationProgram> getCurrentDonationProgramsForSiteSortedByStartDate(String siteName);
}
