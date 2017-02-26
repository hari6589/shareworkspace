package com.bfrc.framework;

import javax.servlet.http.*;

public interface Expirable {
	String getStartPage();
	boolean isExpired(HttpSession session);
}
