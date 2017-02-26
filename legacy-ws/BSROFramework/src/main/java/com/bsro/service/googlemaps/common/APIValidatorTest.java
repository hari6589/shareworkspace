package com.bsro.service.googlemaps.common;


import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.BeforeClass;


/**
 * Tests for {@link APIValidator}.
 * 
 * @author pluttrell
 */
public class APIValidatorTest {

	@Test
	public void testIsClientIdSafe () {

		assertFalse(APIValidator.isClientIdSafe(null));

		assertFalse(APIValidator.isClientIdSafe(""));

		assertFalse(APIValidator.isClientIdSafe("abc-bla"));

		assertFalse(APIValidator.isClientIdSafe("abc"));

		assertFalse(APIValidator.isClientIdSafe("asdf"));

		assertFalse(APIValidator.isClientIdSafe("gme_abc"));

		assertFalse(APIValidator.isClientIdSafe("gme-abc$"));

		assertFalse(APIValidator.isClientIdSafe("gme-+"));

		assertFalse(APIValidator.isClientIdSafe("gme-."));

		assertFalse(APIValidator.isClientIdSafe("gme-^"));

		assertFalse(APIValidator.isClientIdSafe("gme-abc$"));

		assertFalse(APIValidator.isClientIdSafe("gme-abc~"));

		assertFalse(APIValidator.isClientIdSafe("gme-abc;"));

		assertTrue(APIValidator.isClientIdSafe("gme-abc"));

		assertTrue(APIValidator.isClientIdSafe("gme-abc-123"));

		assertTrue(APIValidator.isClientIdSafe("gme-abc_123"));
	}


	@Test
	public void testIsChannelIdSafe () {

		assertFalse(APIValidator.isChannelIdSafe(null));

		assertFalse(APIValidator.isChannelIdSafe(""));

		assertTrue(APIValidator.isChannelIdSafe("www.firestonecompleteautocare.com"));

		assertTrue(APIValidator.isChannelIdSafe("dev01.firestonecompleteautocare.com"));

		assertTrue(APIValidator.isChannelIdSafe("-"));
		
		assertTrue(APIValidator.isChannelIdSafe("."));

	}
}