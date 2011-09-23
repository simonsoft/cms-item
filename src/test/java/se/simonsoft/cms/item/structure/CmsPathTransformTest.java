package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class CmsPathTransformTest {

	@Test
	public void testInvalidTranslationPathConfig() {
		new CmsPathTransform("/lang");
		new CmsPathTransform("/*/lang");
		new CmsPathTransform("/*/trans");
		try {
			new CmsPathTransform("/lang/*/");
			fail("Should not support path config /lang/*/");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new CmsPathTransform("/*/*/lang/");
			fail("Should not support path config /*/*/lang/");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testTranslationMaster() {
		CmsItemPath p = new CmsItemPath("/vvab/xml/documents/900108.xml");
		assertFalse(new CmsPathTransform("/lang").isTranslation(p));
		assertFalse(new CmsPathTransform("/*/lang").isTranslation(p));
		try {
			new CmsPathTransform("/lang").getTranslationLocale(p);
			fail("getTranslationLocale should throw exception when path is not a translation");
		} catch (IllegalArgumentException e) {
			// expected
		}
		assertEquals("/lang/sv-SE/vvab/xml/documents/900108.xml", "" +
				new CmsPathTransform("/lang").getTranslationPath(p, "sv-SE"));
		assertEquals("/vvab/lang/sv-SE/xml/documents/900108.xml", "" +
				new CmsPathTransform("/*/lang").getTranslationPath(p, "sv-SE"));
	}
	
	@Test
	public void testIsTranslationFalsePositives() {
		CmsItemPath p = new CmsItemPath("/vvab/xml/documents/900108.xml");
		// these are in a translation area and must be detected as translations even if they
		// don't have a language code
		assertTrue(new CmsPathTransform("/*/xml").isTranslation(p));
		assertTrue(new CmsPathTransform("/vvab").isTranslation(p));
		// this one can't be a translation/release because we know it must have one additional level
		assertFalse(new CmsPathTransform("/*/xml").isTranslation(new CmsItemPath("/v/xml/12345.xml")));
	}

	@Test
	public void testGetTranslationSlave2() {
		CmsItemPath p = new CmsItemPath("/vvab/lang/sv-SE/xml/documents/900108.xml");
		assertTrue(new CmsPathTransform("/*/lang").isTranslation(p));
		assertFalse(new CmsPathTransform("/lang").isTranslation(p));
		assertFalse(new CmsPathTransform("/*/langs").isTranslation(p));
		assertFalse(new CmsPathTransform("/*/lan").isTranslation(p));
		assertEquals("sv-SE", new CmsPathTransform("/*/lang").getTranslationLocale(p));
		try {
			new CmsPathTransform("/lang").getTranslationLocale(p);
			fail("getTranslationLocale should throw exception if translation path is not according to config");
		} catch (IllegalArgumentException e) {
			// expected
		}
		// TODO support translation from translation
	}

}
