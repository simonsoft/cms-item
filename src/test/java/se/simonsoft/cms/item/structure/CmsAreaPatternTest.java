package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class CmsAreaPatternTest {

	@Test
	public void testGetAreaName() {
		assertEquals("m", new CmsAreaPattern("/*/m").getAreaName());
		assertEquals("Area51", new CmsAreaPattern("/Area51").getAreaName());
	}

	@Test
	public void testGetAreaPathSegmentIndex() {
		assertEquals(2, new CmsAreaPattern("/*/m").getAreaPathSegmentIndex());
		assertEquals(1, new CmsAreaPattern("/Area51").getAreaPathSegmentIndex());
	}

	@Test
	public void testToString() {
		assertEquals("/*/m", "" + new CmsAreaPattern("/*/m"));
		assertEquals("/Area51", "" + new CmsAreaPattern("/Area51"));
	}

	@Test
	public void testEqualsObject() {
		assertTrue(new CmsAreaPattern("/*/lang").equals(new CmsAreaPattern("/*/lang")));
		assertFalse(new CmsAreaPattern("/*/lan").equals(new CmsAreaPattern("/*/lang")));
	}

	@Test
	public void testInvalidTranslationPathConfig() {
		new CmsAreaPattern("/lang");
		new CmsAreaPattern("/*/lang");
		new CmsAreaPattern("/*/trans");
		try {
			new CmsAreaPattern("/lang/*/");
			fail("Should not support path config /lang/*/");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new CmsAreaPattern("/*/*/lang/");
			fail("Should not support path config /*/*/lang/");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testTranslationMaster() {
		CmsItemPath p = new CmsItemPath("/vvab/xml/documents/900108.xml");
		assertFalse(new CmsTranslationsPattern("/lang").isPathInside(p));
		assertFalse(new CmsTranslationsPattern("/*/lang").isPathInside(p));
		try {
			new CmsTranslationsPattern("/lang").getPathLabel(p);
			fail("getPathLabel should throw exception when path is not a translation");
		} catch (IllegalArgumentException e) {
			// expected
		}
		assertEquals("/lang/sv-SE/vvab/xml/documents/900108.xml", "" +
				new CmsTranslationsPattern("/lang").getPathInside(p, "sv-SE"));
		assertEquals("/vvab/lang/sv-SE/xml/documents/900108.xml", "" +
				new CmsTranslationsPattern("/*/lang").getPathInside(p, "sv-SE"));
	}
	
	@Test
	public void testIsInsideFalsePositives() {
		CmsItemPath p = new CmsItemPath("/vvab/xml/documents/900108.xml");
		// these are in a translation area and must be detected as translations even if they
		// don't have a language code
		assertTrue(new CmsAreaPattern("/*/xml").isPathInside(p));
		assertTrue(new CmsAreaPattern("/vvab").isPathInside(p));
		// this one can't be a translation/release because we know it must have one additional level
		assertFalse(new CmsAreaPattern("/*/xml").isPathInside(new CmsItemPath("/v/xml/12345.xml")));
	}

	@Test
	public void testGetTranslationSlave2() {
		CmsItemPath p = new CmsItemPath("/vvab/lang/sv-SE/xml/documents/900108.xml");
		assertTrue(new CmsTranslationsPattern("/*/lang").isPathInside(p));
		assertFalse(new CmsTranslationsPattern("/lang").isPathInside(p));
		assertFalse(new CmsTranslationsPattern("/*/langs").isPathInside(p));
		assertFalse(new CmsTranslationsPattern("/*/lan").isPathInside(p));
		assertEquals("sv-SE", new CmsTranslationsPattern("/*/lang").getPathLabel(p));
		try {
			new CmsTranslationsPattern("/lang").getPathLabel(p);
			fail("getPathLabel should throw exception if translation path is not according to config");
		} catch (IllegalArgumentException e) {
			// expected
		}
		// TODO support translation from translation
	}	

}
