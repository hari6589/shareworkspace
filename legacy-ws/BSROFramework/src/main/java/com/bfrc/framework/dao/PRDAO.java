package com.bfrc.framework.dao;

import com.bfrc.framework.dao.pr.*;

public interface PRDAO {
	AlertService getAlertService();
	ArticleService getArticleService();
	ReleaseService getReleaseService();
	SiteService getSiteService();
	UserService getUserService();
}
