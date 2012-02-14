package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsRepositoryTest {

	@Test
	public void test() {
		CmsRepository r = new CmsRepository("http://cmsdemo.pdsvision.net/svn/re%20po1");
		assertEquals("http://cmsdemo.pdsvision.net/svn/re%20po1", r.getUrl());
		assertEquals("http://cmsdemo.pdsvision.net", r.getServerRootUrl());
		assertEquals("/svn", r.getParentPath());
		assertEquals("re%20po1", r.getName());
		assertEquals("http://cmsdemo.pdsvision.net/svn/re%20po1", r.toString());
		assertTrue(r.equals(new CmsRepository("http://cmsdemo.pdsvision.net/svn/re%20po1")));
		assertFalse(r.equals(new CmsRepository("https://cmsdemo.pdsvision.net/svn/re%20po1")));
		assertTrue("equals with string should be ok", r.equals("http://cmsdemo.pdsvision.net/svn/re%20po1"));
		assertFalse("slash must match", r.equals("http://cmsdemo.pdsvision.net/svn/re%20po1/"));
	}
	
	@Test
	public void testCustom() {
		CmsRepository r = new CmsRepository("https://x.y.zz:32123/r");
		assertEquals("https://x.y.zz:32123/r", r.getUrl());
		assertEquals("https://x.y.zz:32123", r.getServerRootUrl());
		assertEquals("", r.getParentPath());
		assertEquals("r", r.getName());
		assertEquals("https://x.y.zz:32123/r", r.toString());
		assertEquals("x.y.zz:32123", r.getHost());
		assertEquals("x.y.zz", r.getHostname());
	}

	@Test
	public void testPartsConstructor() {
		CmsRepository r = new CmsRepository("https://x.y.zz:321", "/parent/path", "repo");
		assertEquals("https://x.y.zz:321/parent/path/repo", r.toString());
		CmsRepository r2 = new CmsRepository("https", "demo.simonsoftcms.se", "/svn", "demo1");
		assertEquals("https://demo.simonsoftcms.se/svn/demo1", r2.toString());
	}
	
	@Test
	public void testHostUnknown() {
		CmsRepository r = new CmsRepository("/parent", "repo1");
		assertFalse(r.isHostKnown());
		assertEquals("/parent", r.getParentPath());
		assertEquals("repo1", r.getName());
		try {
			r.getHost();
			fail("Should throw exception for getHost when host is unknown");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo1", e.getMessage());
		}
		try {
			r.getServerRootUrl();
			fail("Should throw exception for getServerRootUrl when host is unknown");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo1", e.getMessage());
		}
		try {
			r.getUrl();
			fail("Should throw exception for getUrl when host is unknown");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo1", e.getMessage());
		}
		assertEquals("CmsRepository:/parent/repo1", r.toString());
		assertTrue(r.equals(new CmsRepository(null, null, "/parent", "repo1")));
		assertFalse(r.equals(new CmsRepository("http", "host.name", "/parent", "repo1")));
	}
}
