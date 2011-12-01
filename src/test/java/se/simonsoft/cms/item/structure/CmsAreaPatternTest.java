package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;

public class CmsAreaPatternTest {

	@Test
	public void testGetAreaName() {
		assertEquals("m", new CmsAreaPattern("/*/m").getAreaName());
		assertEquals("Area51", new CmsAreaPattern("/Area51").getAreaName());
	}
	
	@Test
	public void testGetArea() {
		assertEquals(new CmsItemPath("/A1"), 
				new CmsAreaPattern("/A1").getArea(new CmsItemPath("/whatever")));
		assertEquals(new CmsItemPath("/my docs/re"), 
				new CmsAreaPattern("/*/re").getArea(new CmsItemPath("/my docs/x.xml")));
	}
	
	@Test
	public void getAreaItemPathOffset() {
		assertEquals(1, new CmsAreaPattern("/*/m").getAreaItemPathOffset());
		assertEquals(0, new CmsAreaPattern("/Area51").getAreaItemPathOffset());
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
		CmsTranslationsPattern config = new CmsTranslationsPattern("/*/lang");
		assertEquals("sv-SE", config.getPathLabel(p));
		assertEquals("/vvab/xml/documents/900108.xml", "" + config.getPathOutside(p));
		assertEquals("Translation of translation",
				"/vvab/lang/en/xml/documents/900108.xml", "" + config.getPathInside(p, "en"));
		try {
			new CmsTranslationsPattern("/lang").getPathLabel(p);
			fail("getPathLabel should throw exception if translation path is not according to config");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test(expected=IllegalArgumentException.class) 
	public void testTranslateToSame() {
		new CmsTranslationsPattern("/lang").getPathInside(
				new CmsItemPath("/lang/de_DE/ab/xml/c.xml"), "de_DE");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLabelNull() {
		new CmsAreaPattern("/x").getPathInside(new CmsItemPath("y"), null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLabelSlash() {
		new CmsAreaPattern("/x").getPathInside(new CmsItemPath("y"), "A/B");
	}

	@Test
	public void testRelease() {
		CmsItemPath m = new CmsItemPath("/vvab/xml/documents/900108.xml");
		CmsReleasesPattern top = new CmsReleasesPattern("/r");
		CmsReleasesPattern sub = new CmsReleasesPattern("/*/_release");
		assertTrue(top.isPathOutside(m));
		assertTrue(sub.isPathOutside(m));
		CmsItemPath rtop = top.getPathInside(m, "_B");
		assertEquals("/r/_B/vvab/xml/documents/900108.xml", "" + rtop);
		assertTrue(top.isPathInside(rtop));
		assertFalse(sub.isPathInside(rtop));
		assertEquals("_B", top.getPathLabel(rtop));
		assertEquals(m, top.getPathOutside(rtop));
		CmsItemPath rsub = sub.getPathInside(m, "1.2");
		assertEquals("/vvab/_release/1.2/xml/documents/900108.xml", "" + rsub);
		assertFalse(top.isPathInside(rsub));
		assertTrue(sub.isPathInside(rsub));
		assertEquals("1.2", sub.getPathLabel(rsub));
		assertEquals(m, sub.getPathOutside(rsub));
		// now translate the release
		
	}
	
	@Test
	public void testGraphicsTranslation() {
		CmsTranslationsPattern relative = new CmsTranslationsPattern("lang");
		assertTrue(relative.isAreaRelative());
		assertEquals(-1, relative.getAreaPathSegmentIndex());
		CmsItemPath master = new CmsItemPath("/ab/graphics/001/123.tif");
		assertTrue(relative.isPathOutside(master));
		CmsItemPath t = relative.getPathInside(master, "sv_SE");
		assertEquals("/ab/graphics/001/lang/sv_SE/123.tif", "" + t);
		assertTrue(relative.isPathInside(t));
		assertTrue("relative from root would be ok", relative.isPathInside(new CmsItemPath("/lang/de/x.gif")));
		assertFalse("relative without label not ok", relative.isPathInside(new CmsItemPath("/lang/x.gif")));		
		assertEquals("sv_SE", relative.getPathLabel(t));
		assertEquals("/ab/graphics/001/123.tif", "" + relative.getPathOutside(t));
	}
	
	@Test
	@Ignore // just an idea
	public void testShortenPath() {
		// with translations from releases we increase the risk of running into windows path length limitation
		new CmsAreaPattern("/*/lang/-/"); // means eliminate next path element after label
	}
	
}
