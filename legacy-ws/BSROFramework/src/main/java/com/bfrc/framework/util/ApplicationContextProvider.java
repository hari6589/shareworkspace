/**
 * 
 */
package com.bfrc.framework.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ApplicationContextProvider implements ApplicationContextAware { 

	private static ApplicationContext ctx;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ctx = applicationContext;
	}

	public static ApplicationContext getContext() {
		return ctx;
	}

}
