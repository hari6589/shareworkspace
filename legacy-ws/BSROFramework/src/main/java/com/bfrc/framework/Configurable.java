package com.bfrc.framework;

import com.bfrc.Config;

public interface Configurable {
	void setConfig(Config c);
	Config getConfig();
}
