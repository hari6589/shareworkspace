package com.bfrc.dataaccess.svc.webdb.emailcollection;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.bsro.model.promotions.SpecialOffer;
import app.bsro.model.webservice.BSROWebServiceResponse;
import app.bsro.model.webservice.BSROWebServiceResponseCode;

import com.bfrc.dataaccess.core.beans.PropertyAccessor;
import com.bfrc.dataaccess.dao.emailcollection.EmailCollectionDAO;
import com.bfrc.dataaccess.dao.rewards.RewardsLogDAO;
import com.bfrc.dataaccess.exception.InvalidEmailException;
import com.bfrc.dataaccess.model.email.EmailSignup;
import com.bfrc.dataaccess.model.promotion.Promotion;
import com.bfrc.dataaccess.model.reward.EmailCollection;
import com.bfrc.dataaccess.model.reward.RewardsLog;
import com.bfrc.dataaccess.svc.webdb.DisclaimerService;
import com.bfrc.dataaccess.svc.webdb.EmailService;
import com.bfrc.dataaccess.svc.webdb.PromotionsService;
import com.bfrc.framework.util.ServerUtil;

/**
 * @author smoorthy
 *
 */

@Service
public class EmailCollectionServiceImpl implements EmailCollectionService {
	
	@Autowired
	EmailCollectionDAO emailCollectionDAO;
	
	@Autowired
	RewardsLogDAO rewardsLogDAO;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PromotionsService promotionService;
	
	@Autowired
	private DisclaimerService disclaimerService;
	
	@Autowired
	private PropertyAccessor propertyAccessor;
	
	private String offerDateDisclaimerFormat = "MMMM d, yyyy";
	
	private static final String LANDING_PAGE_ID_REWARDS = "rewards";
	private static final String NO_DATA_FOUND = "No Data found";

	public BSROWebServiceResponse getPACUserDetails(String tendigitcode) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		EmailCollection emailCollection = emailCollectionDAO.get(tendigitcode);
		if (emailCollection == null) {
			response.setMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		response.setPayload(emailCollection);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

	public BSROWebServiceResponse updatePACUserDetails(String tendigitcode,
			String firstName, String lastName, String email) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		EmailCollection emailCollection = emailCollectionDAO.get(tendigitcode);
		if (emailCollection == null) {
			response.setMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		if (!emailCollection.getFirstname().equalsIgnoreCase(firstName) || !emailCollection.getLastname().equalsIgnoreCase(lastName) || !emailCollection.getEmail().equalsIgnoreCase(email)) {
			emailCollection.setFirstname(firstName);
			emailCollection.setLastname(lastName);
			emailCollection.setEmail(email);
			emailCollectionDAO.update(emailCollection);
			
			emailCollection = emailCollectionDAO.get(tendigitcode);
		}
		
		response.setPayload(emailCollection);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}

	public BSROWebServiceResponse logRewards(String tenDigitCode, String confirmOptIn, String siteName) {
		BSROWebServiceResponse response = new BSROWebServiceResponse();
		
		/*
		 * 		Get the EmailCollection data by 10 digit Personal Access Code
		 * */
		EmailCollection emailCollection = emailCollectionDAO.get(tenDigitCode);
		if (emailCollection == null) {
			response.setMessage("Sorry, the 10 digit code you entered was not in our system. Please try again.");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			return response;
		}
		
		if (ServerUtil.isNullOrEmpty(confirmOptIn)) {
			confirmOptIn = "no";
		}
		
		try {
			/*
			 * 		Save Email SignUp (RTMS_WEBDB.EMAIL_SIGNUP) data.
			 * */
			saveEmailSignupInfo(emailCollection, confirmOptIn);
		} catch (InvalidEmailException e) {
			response.setMessage("Sorry, Error updating the Email signup info.");
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_ERROR.name());
			return response;
		}
		
		saveRewardsLog(emailCollection.getTendigitcode(), siteName);
		
		/*
		 * 		Get the loyalty offers by LANDING_PAGE_ID 'rewards' from RTMS_WEBDB.PROMOTION
		 * */
		List<SpecialOffer> offers = getLoyaltyOffers(siteName);
		if(offers == null || offers.isEmpty()){
			response.setStatusCode(BSROWebServiceResponseCode.BUSINESS_SERVICE_INFO.name());
			
			/*
			 * 		Get the disclaimer message for no loyalty offers
			 * */
			String disclaimerMsg = getDisclaimerMessage(siteName);			
			if (disclaimerMsg != null && disclaimerMsg.trim().length() > 0) {
				response.setMessage(disclaimerMsg);
			} else {
				response.setMessage(NO_DATA_FOUND);
			}
			
			return response;
		}
		
		response.setPayload(offers);
		response.setStatusCode(BSROWebServiceResponseCode.SUCCESSFUL.name());
		return response;
	}
	
	private void saveEmailSignupInfo(EmailCollection emailCollection, String confirmOptIn) throws InvalidEmailException {
		if (emailCollection.getEmail() != null) {
			EmailSignup emailSignup = new EmailSignup();
			
			emailSignup.setFirstName(emailCollection.getFirstname());
			emailSignup.setLastName(emailCollection.getLastname());
			emailSignup.setCreatedDate(new Date());
			emailSignup.setSource(LANDING_PAGE_ID_REWARDS);
			emailSignup.setEmailAddress(emailCollection.getEmail());
			emailSignup.setConfirmOptin(confirmOptIn);
			emailSignup.setAccessCode(emailCollection.getTendigitcode());
			
			emailService.subscribe(emailSignup);			
		}
	}
	
	private List<SpecialOffer> getLoyaltyOffers(String siteName) {
		List <Promotion> promoList = promotionService.getOffersByLandingPage(siteName, new Date(), LANDING_PAGE_ID_REWARDS, null, null);
		List<SpecialOffer> offers = new ArrayList<SpecialOffer>();
		if(promoList != null && !promoList.isEmpty()){
			for(Promotion p : promoList) {
				SpecialOffer so = new SpecialOffer();
				so.setId(p.getPromotionId());
				so.setDescription(p.getDescription());
				so.setCategory(LANDING_PAGE_ID_REWARDS);
				so.setImageUrl(propertyAccessor.getStringProperty("specialOffersCouponUrl")+so.getId());
				if(p.getFlashIconBlob() != null)
					so.setBannerUrl(propertyAccessor.getStringProperty("specialOffersBannerUrl")+so.getId());
				
				if (!ServerUtil.isNullOrEmpty(p.getDisclaimer()))
					so.setDisclaimer(p.getDisclaimer());
				
				SimpleDateFormat sdf = new SimpleDateFormat(offerDateDisclaimerFormat);
				if(p.getOfferStartDate() != null) 
					so.setOfferStartDate(sdf.format(p.getOfferStartDate()));
				if(p.getOfferEndDate() != null) 
					so.setOfferEndDate(sdf.format(p.getOfferEndDate()));
				so.setFriendlyId(p.getFriendlyId());
				offers.add(so);
			}
		}
		
		return offers;
	}
	
	private String getDisclaimerMessage(String siteName) {
		return disclaimerService.getDisclaimerDescription(siteName, LANDING_PAGE_ID_REWARDS);
	}
	
	private void saveRewardsLog(String tendigitcode, String siteName) {
		RewardsLog rewardsLog = new RewardsLog(tendigitcode, new Date(), siteName);
		rewardsLogDAO.save(rewardsLog);
	}

}
