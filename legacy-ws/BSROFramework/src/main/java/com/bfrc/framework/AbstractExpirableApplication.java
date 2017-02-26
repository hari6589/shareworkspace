package com.bfrc.framework;

public abstract class AbstractExpirableApplication implements Expirable,
		Application {
	public String getStartPage() { return "start"; }
}
