package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class CmsItemIdArgTest {
	
	@Test
	@Ignore
	public void testPegHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn://demo.simonsoftcms.se/svn/demo1^/vvab/xml/Docs/Sa%20s.xml?p=9");
		assertTrue(p.isFullyQualified());
		assertTrue(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		assertTrue(p.isPeggedOriginally());
	}
	
	@Test
	@Ignore
	public void testPegNoHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/sections/In%20in.xml?p=101");
		assertFalse(p.isFullyQualified());
		assertFalse(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		assertTrue(p.isPeggedOriginally());
	}
	
	@Test
	@Ignore
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
		} catch (IllegalArgumentException e) {
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
		p.setPegRev(1234567);
		assertTrue(p.isPegged());
		assertFalse(p.isPeggedOriginally());
		assertEquals(1234567, p.getPegRev().longValue());
		assertEquals("x-svn:///svn/demo1^/vvab/graphics/0001.tif?p=123456", p.getLogicalId());
		assertEquals("x-svn://x.y.z/svn/demo1^/vvab/graphics/0001.tif?p=123456", p.getLogicalIdFull());
		assertEquals("http://x.y.z/svn/demo1/vvab/graphics/0001.tif", p.getUrl());
		assertEquals("http://x.y.z/svn/demo1", p.getRepositoryUrl());		
	}

}
