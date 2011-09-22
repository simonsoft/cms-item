package se.simonsoft.cms.version;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsComponentVersionNopackageTest {

	@Test
	public void testGetVersion() {
		CmsComponentVersion v = new CmsComponentVersionNopackage();
		assertFalse(v.isKnown());
		assertTrue(v.isSnapshot());
		assertEquals(CmsComponentVersion.UNKNOWN_VERSION, v.getVersion());
		assertEquals("", v.getBuildName());
		assertEquals(null, v.getBuildNumber());
		assertEquals(null, v.getSourceRevision());
		assertEquals(CmsComponentVersion.UNKNOWN_VERSION, v.getLabel());
	}

}
