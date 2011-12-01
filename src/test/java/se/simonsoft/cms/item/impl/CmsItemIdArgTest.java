package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Ignore;
import org.junit.Test;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;

public class CmsItemIdArgTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateNoRoot() {
		new CmsItemIdArg("x-svn:///svn/demo1/vvab/graphics/0001.tif");
	}
	
	@Test(expected=IllegalArgumentException.class)
	@Ignore
	public void testValidateNoStartSlash() {
		new CmsItemIdArg("x-svn://svn/demo1^/vvab/graphics/0001.tif");
	}
	
	@Test
	public void testPegHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn://demo.simonsoftcms.se/svn/demo1^/vvab/xml/Docs/Sa%20s.xml?p=9");
		assertTrue(p.isFullyQualified());
		assertTrue(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		assertTrue(p.isPeggedOriginally());
		assertEquals("/vvab/xml/Docs/Sa s.xml", p.getRelPath().toString());
		assertEquals("http://demo.simonsoftcms.se/svn/demo1", p.getRepository().toString());
		assertEquals(p.getLogicalIdFull(), p.toString());
	}
	
	@Test
	public void testPegNoHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/sections/In%20in.xml?p=101");
		assertFalse(p.isFullyQualified());
		assertFalse(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		assertTrue(p.isPeggedOriginally());
		p.setHostnameOrValidate("example.net");
		assertEquals("http://example.net/svn/demo1/vvab/xml/sections/In%20in.xml", p.getUrl());
	}
	
	@Test
	public void testNoHostNoPeg() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/graphics/0001.tif");
		assertFalse(p.isFullyQualified());
		assertFalse(p.isFullyQualifiedOriginally());
		assertFalse(p.isPegged());
		assertFalse(p.isPeggedOriginally());
		assertEquals("x-svn:///svn/demo1^/vvab/graphics/0001.tif", p.getLogicalId());
		assertEquals("/vvab/graphics/0001.tif", p.getRelPath().toString());
		assertNull(p.getPegRev());
		try {
			p.getLogicalIdFull();
			fail("Expected exception");
		} catch (IllegalStateException e) {
			assertEquals("Hostname unknown for " + p.getLogicalId(), e.getMessage());
		}
		try {
			p.getRepositoryUrl();
		} catch (IllegalStateException e) {
			assertEquals("Hostname unknown for " + p.getLogicalId(), e.getMessage());
		}
		try {
			p.getUrl();
		} catch (IllegalStateException e) {
			assertEquals("Hostname unknown for " + p.getLogicalId(), e.getMessage());
		}
		p.setHostname("x.y.z");
		assertTrue(p.isFullyQualified());
		assertFalse(p.isFullyQualifiedOriginally());
		assertEquals("x-svn:///svn/demo1^/vvab/graphics/0001.tif", p.getLogicalId());
		assertEquals("x-svn://x.y.z/svn/demo1^/vvab/graphics/0001.tif", p.getLogicalIdFull());
		assertEquals("http://x.y.z/svn/demo1/vvab/graphics/0001.tif", p.getUrl());
		assertEquals("http://x.y.z/svn/demo1", p.getRepositoryUrl());
		assertFalse(p.isPegged());
		assertEquals("API spec", null, p.getPegRev());
		p.setPegRev(1234567);
		assertTrue(p.isPegged());
		assertFalse(p.isPeggedOriginally());
		assertEquals(1234567, p.getPegRev().longValue());
		assertEquals("x-svn:///svn/demo1^/vvab/graphics/0001.tif?p=1234567", p.getLogicalId());
		assertEquals("x-svn://x.y.z/svn/demo1^/vvab/graphics/0001.tif?p=1234567", p.getLogicalIdFull());
		assertEquals("http://x.y.z/svn/demo1/vvab/graphics/0001.tif", p.getUrl());
		assertEquals("http://x.y.z/svn/demo1", p.getRepositoryUrl());
		try {
			p.setHostname("x.y.z");
			fail("Expected exception");
		} catch (IllegalStateException e) {
			assertEquals("Hostname already set for " + p.getLogicalIdFull(), e.getMessage());
		}
		p.setHostnameOrValidate("x.y.z");
		try {
			p.setHostnameOrValidate("xx.y.z");
			fail("Expected exception");
		} catch (IllegalArgumentException e) {
			assertEquals("Unexpected hostname in x-svn://x.y.z/svn/demo1^/vvab/graphics/0001.tif?p=1234567, expected xx.y.z",
					e.getMessage());
		}
	}
	
	@Test
	public void testGetUrl() {
		assertEquals("http://host.n/svn/d1/vv/xml/8.xml", 
				new CmsItemIdArg("x-svn://host.n/svn/d1^/vv/xml/8.xml").getUrl());
		assertEquals("http://host.n/svn/d1/vv/xml",
				new CmsItemIdArg("x-svn://host.n/svn/d1^/vv/xml").getUrl());
		assertEquals("http://host.n/svn/d1/vv/xml", 
				new CmsItemIdArg("x-svn://host.n/svn/d1^/vv/xml/").getUrl());	
	}
	
	@Test
	public void testEquals() {
		CmsItemIdArg i1 = new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml");
		assertTrue(i1.equals(new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml")));
		i1.setHostname("x.y");
		// not sure about this
		assertFalse("Hostname difference", i1.equals(new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml")));
		assertFalse("Hostname difference other way", new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml").equals(i1));
	}
	
	@Test
	public void testEqualsOtherImpl() {
		CmsItemIdArg i1 = new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml");
		// other CmsItemIds
		CmsItemId i3 = mock(CmsItemId.class);
		when(i3.getLogicalId()).thenReturn("x-svn:///svn/d1^/vv/r/A/xml/8.xml");
		assertTrue("Same logicalId, no host info", i1.equals(i3));
		when(i3.getLogicalIdFull()).thenReturn("x-svn://host.xy/svn/d1^/vv/r/A/xml/8.xml");
		assertFalse("Can't know if the id is the same when only one has hostname", i1.equals(i3));
		i1.setHostname("host.xy");
		assertTrue("Same logicalIdFull", i1.equals(i3));
	}
	
	@Test
	public void testWithRelPath() {
		CmsItemId i1 = new CmsItemIdArg("x-svn://x.y/svn/r1^/vv/xml/8.xml?p=7");
		CmsItemId parent = i1.withRelPath(new CmsItemPath("/vv")); // actually parent's parent
		assertEquals("x-svn:///svn/r1^/vv?p=7", parent.getLogicalId());
		assertEquals("x-svn://x.y/svn/r1^/vv?p=7", parent.getLogicalIdFull());
		assertEquals("For consistency, URLs can not have traling slash", 
				"http://x.y/svn/r1/vv", parent.getUrl());
	}
	
	@Test
	public void testWithRelPathToRepoRoot() {
		CmsItemId i1 = new CmsItemIdArg("x-svn://x.y/parent/repo^/a/b.xml");
		CmsItemId repo = i1.withRelPath(null);
		assertEquals("x-svn:///parent/repo^/", repo.getLogicalId());
		assertEquals("For consistency, URLs can not have traling slash",
				"http://x.y/parent/repo", repo.getUrl());
		CmsItemId i2 = new CmsItemIdArg("x-svn://x.y/p/r^/a?p=9");
		CmsItemId repoRootRev = i2.withRelPath(null);
		assertEquals("x-svn:///p/r^/?p=9", repoRootRev.getLogicalId());
		// We have not path to represent root
		//CmsItemId repoAlso = i1.withRelPath(new CmsItemPath("/"));
		//assertEquals("x-svn://x.y/parent/repo^/", repoAlso.getLogicalIdFull());
		assertEquals("CmsItemPath can not represent root", null, repo.getRelPath());
	}
	
	@Test
	public void testWithRelPathEncoding() {
		CmsItemId i1 = new CmsItemIdArg("x-svn:///svn/demo1^/v/a%20b/c.xml");
		assertEquals("Should preserve logicalId encoding", 
				"x-svn:///svn/demo1^/v/a%20b", i1.withRelPath(new CmsItemPath("/v/a b")).getLogicalId());
		try {
			i1.withRelPath(new CmsItemPath("/v/a b/d.xml"));
			fail("only parents can be supported, unless we want to introduce encoding logic in CmsItemIdArg");
		} catch (IllegalArgumentException e) {
			assertEquals("New path based on this CmsItemIdArg must be parent of /v/a b/c.xml", e.getMessage());
		}
	}
	
	@Test
	public void testWithPegRev() {
		CmsItemId i1 = new CmsItemIdArg("x-svn://x.y/svn/r^/a/b.xml");
		assertEquals("x-svn://x.y/svn/r^/a/b.xml?p=9", i1.withPegRev(9L).getLogicalIdFull());
		assertEquals(i1, i1.withPegRev(1234L).withPegRev(null));
	}

}
