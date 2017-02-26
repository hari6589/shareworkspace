package com.bfrc.pojo.contact;

// Generated Jan 9, 2007 10:55:22 PM by Hibernate Tools 3.2.0.beta8

import java.util.HashSet;
import java.util.Set;

/**
 * WebSite generated by hbm2java
 */
public class WebSite implements java.io.Serializable {

	// Fields    

	private int siteId;

	private String siteName;

	private String siteUrl;

	private String siteWebmaster;

	private Set bfrcWebSiteFeedbacks = new HashSet(0);

	// Constructors

	/** default constructor */
	public WebSite() {
	}

	/** minimal constructor */
	public WebSite(int siteId) {
		this.siteId = siteId;
	}

	/** full constructor */
	public WebSite(int siteId, String siteName, String siteUrl,
			String siteWebmaster, Set bfrcWebSiteFeedbacks) {
		this.siteId = siteId;
		this.siteName = siteName;
		this.siteUrl = siteUrl;
		this.siteWebmaster = siteWebmaster;
		this.bfrcWebSiteFeedbacks = bfrcWebSiteFeedbacks;
	}

	// Property accessors
	public int getSiteId() {
		return this.siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public String getName() {
		return this.siteName;
	}

	public void setName(String siteName) {
		this.siteName = siteName;
	}

	public String getUrl() {
		return this.siteUrl;
	}

	public void setUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getWebmaster() {
		return this.siteWebmaster;
	}

	public void setWebmaster(String siteWebmaster) {
		this.siteWebmaster = siteWebmaster;
	}

	public Set getFeedbacks() {
		return this.bfrcWebSiteFeedbacks;
	}

	public void setFeedbacks(Set bfrcWebSiteFeedbacks) {
		this.bfrcWebSiteFeedbacks = bfrcWebSiteFeedbacks;
	}

}
