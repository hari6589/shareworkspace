package com.bfrc.dataaccess.model.contact;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class WebSite implements Serializable {

	private static final long serialVersionUID = 1L;
	private int siteId;
	private String siteName;
	private String siteUrl;
	private String siteWebmaster;
	private Set bfrcWebSiteFeedbacks = new HashSet(0);

	public WebSite() {}

	public WebSite(int siteId) {
		this.siteId = siteId;
	}

	public WebSite(int siteId, String siteName, String siteUrl,
			String siteWebmaster, Set bfrcWebSiteFeedbacks) {
		this.siteId = siteId;
		this.siteName = siteName;
		this.siteUrl = siteUrl;
		this.siteWebmaster = siteWebmaster;
		this.bfrcWebSiteFeedbacks = bfrcWebSiteFeedbacks;
	}

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
