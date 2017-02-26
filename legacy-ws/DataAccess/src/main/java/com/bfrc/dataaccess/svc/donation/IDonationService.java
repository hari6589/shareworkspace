package com.bfrc.dataaccess.svc.donation;

import com.bfrc.dataaccess.model.donation.DonationProgram;

public interface IDonationService {
	public DonationProgram getActiveDonationProgramForSite(String siteName);
}
