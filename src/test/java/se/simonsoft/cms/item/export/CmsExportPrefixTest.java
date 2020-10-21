package se.simonsoft.cms.item.export;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsExportPrefixTest {

	@Test
	public void test() {
		new CmsExportPrefix("cms4");
		new CmsExportPrefix("cms4-suffix");
		new CmsExportPrefix("CMS4"); // Adding support for uppercase.
		new CmsExportPrefix("01234567890123456789012345678901234567890123456789"); // 50 chars
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongString() {
		new CmsExportPrefix("01234567890123456789012345678901234567890123456789morethan50");
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		new CmsExportPrefix("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCharPercent() {
		new CmsExportPrefix("cms%");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCharUnderscore() {
		// Currently don't allow underscore since it is generally avoided in URLs.
		new CmsExportPrefix("cms_");
	}
}
