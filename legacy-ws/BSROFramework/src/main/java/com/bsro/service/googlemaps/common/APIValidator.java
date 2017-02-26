package com.bsro.service.googlemaps.common;


public class APIValidator {

	/**
	 * Designed to detect configuration errors with the CLIENT value. We want to avoid sending invalid info to Google, as such this method will check
	 * against known invalid options.
	 * 
	 * Enforces the following rules, which we know to be requirements:
	 * 1) Must start with gme-
	 * 2) Must be at least 5 chars long.
	 * 3) Must not include anything but letters, numbers, dashes and underscores.
	 * 
	 * Access modifier: default (on purpose, so we can unit test)
	 * 
	 * @return true if checks passed, otherwise false.
	 */
	public static boolean isClientIdSafe (String clientId) {

		if (clientId == null) {
			return false;
		}

		if (clientId.length() < 5) {
			return false;
		}

		if (!clientId.matches("[a-zA-Z0-9_\\-]*")) {
			return false;
		}

		if (!clientId.startsWith("gme-")) {
			return false;
		}

		return true;
	}


	/**
	 * Designed to detect configuration errors with the CHANNEL value. We want to avoid sending invalid info to Google, as such this method will check
	 * against known invalid options.
	 * 
	 * As of 2012.01.02 Google has published the following rules for CHANNEL values:
	 * 1) Must be an ASCII alphanumeric string.
	 * 2) Period (.) and hyphen (-) characters are allowed.
	 * 
	 * Reference: According to https://developers.google.com/maps/documentation/business/guide?hl=en
	 * 
	 * Access modifier: default (on purpose, so we can unit test)
	 * 
	 * @return true if checks passed, otherwise false.
	 */
	public static boolean isChannelIdSafe (String channelId) {

		if (channelId == null) {
			return false;
		}

		if (channelId.isEmpty()) {
			return false;
		}

		if (!channelId.matches("[\\p{ASCII}\\.\\-]*")) {
			return false;
		}

		return true;
	}
}
