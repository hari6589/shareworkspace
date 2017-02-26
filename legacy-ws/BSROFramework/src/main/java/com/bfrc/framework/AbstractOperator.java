package com.bfrc.framework;

import com.bfrc.Config;

public abstract class AbstractOperator implements Configurable, Operator {
	private Config config;

	public Config getConfig() {
		return this.config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
}
