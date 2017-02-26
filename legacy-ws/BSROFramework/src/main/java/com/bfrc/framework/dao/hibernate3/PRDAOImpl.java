package com.bfrc.framework.dao.hibernate3;

import org.hibernate.*;

import com.bfrc.Config;
import com.bfrc.framework.*;
import com.bfrc.framework.dao.PRDAO;
import com.bfrc.framework.dao.pr.*;

public class PRDAOImpl implements PRDAO, Configurable {
	public PRDAOImpl() {
		this.alertService = new AlertService();
		this.articleService = new ArticleService();
		this.releaseService = new ReleaseService();
		this.siteService = new SiteService();
		this.userService = new UserService();
	}
	
	public void setSessionFactory(SessionFactory s) {
		this.alertService.setSessionFactory(s);
		this.articleService.setSessionFactory(s);
		this.releaseService.setSessionFactory(s);
		this.siteService.setSessionFactory(s);
		this.userService.setSessionFactory(s);
	}
	
	private Config config;

	public Config getConfig() {
		return this.config;
	}

	public void setConfig(Config config) {
		this.config = config;
		this.alertService.setConfig(config);
		this.articleService.setConfig(config);
		this.releaseService.setConfig(config);
		this.siteService.setConfig(config);
		this.userService.setConfig(config);
	}
	
	
	AlertService alertService;
	ArticleService articleService;
	ReleaseService releaseService;
	SiteService siteService;
	UserService userService;
	
	public AlertService getAlertService() {
		return this.alertService;
	}

	public ArticleService getArticleService() {
		return this.articleService;
	}

	public ReleaseService getReleaseService() {
		return this.releaseService;
	}

	public SiteService getSiteService() {
		return this.siteService;
	}

	public UserService getUserService() {
		return this.userService;
	}

}
