/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

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
			assertEquals("Repository identified only by parent path and name: /svn/demo1", e.getMessage());
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
	
	@Test
	public void testEqualsAfterWith() {
		CmsItemId i = new CmsItemIdArg("x-svn://local.test/r^/y");
		assertTrue(i.equals(i.withPegRev(null)));
		assertTrue(i.withPegRev(null).equals(i));
		CmsItemId ip = new CmsItemIdArg("x-svn://local.test/r^/y?p=1");
		assertTrue(ip.withPegRev(null).equals(i));
		assertTrue(i.equals(ip.withPegRev(null)));
		assertTrue(i.withRelPath(null).equals(new CmsItemIdArg("x-svn://local.test/r^/")));
	}
	
	@Test
	public void testEqualsAndHashCode() {
		CmsItemIdArg id1 = new CmsItemIdArg("x-svn://local.test/r^/x");
		CmsItemIdArg id2 = new CmsItemIdArg("x-svn://local.test/r^/x");
		assertTrue(id1.equals(id2));
		Set<CmsItemId> hashSet = new HashSet<CmsItemId>();
		hashSet.add(id1);
		assertTrue("Equal ids must have the same hash code or collections may fail to note the equality",
				hashSet.contains(id2));
		assertFalse("Different path should in general result in different hash code",
				id1.hashCode() == new CmsItemIdArg("x-svn://local.test/r^/y").hashCode());
		assertTrue("hashCode is allowed to ignore hostname", // SvnLogicalId does the same through toString ecluding hostname
				id1.hashCode() == new CmsItemIdArg("x-svn:///r^/x").hashCode());
	}

	@Test
	public void testNoHostRepo() {
		CmsItemIdArg i = new CmsItemIdArg("x-svn:///parent/repo^/x");
		assertEquals("repo", i.getRepository().getName());
		assertEquals("/parent", i.getRepository().getParentPath());
		try {
			i.getRepository().getUrl();
			fail("Should throw exception on repository URL until hostname is set");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo", e.getMessage());
		}
		try {
			i.getRepository().getHost();
			fail("Should throw exception on repository host until hostname is set");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo", e.getMessage());
		}
		try {
			i.getRepository().getServerRootUrl();
			fail("Should throw exception on server url until hostname is set");
		} catch (IllegalStateException e) {
			assertEquals("Repository identified only by parent path and name: /parent/repo", e.getMessage());
		}
		assertEquals("toString can not produce a URL so the best info we have is the logicalId of the repo",
				"CmsRepository:/parent/repo", i.getRepository().toString());
		assertTrue(i.getRepository().equals(i.getRepository()));
		assertTrue(i.getRepository().equals(i.withPegRev(1L).getRepository()));
		assertFalse(i.getRepository().equals(new CmsItemIdArg("x-svn://x.y/parent/repo^/x").getRepository()));
		assertFalse(i.getRepository().equals(new CmsRepository("http://x.y/parent/repo")));
		assertNotNull(i.getRepository().hashCode());
		// now set hostname
		i.setHostname("host.name");
		assertEquals("http://host.name/parent/repo", i.getRepository().getUrl());
		assertEquals("/parent", i.getRepository().getParentPath());
	}
	
	@Test
	public void testEqualsPortNumber() {
		assertTrue(new CmsItemIdArg("x-svn://x.y:123/p/repo^/x").equals(new CmsItemIdArg("x-svn://x.y:123/p/repo^/x")));
		assertFalse(new CmsItemIdArg("x-svn://x.y:123/p/repo^/x").equals(new CmsItemIdArg("x-svn://x.y:124/p/repo^/x")));
	}
	
	@Test
	public void testUris() {
		assertEquals("/svn/demo1/v/ab/c.xml", new CmsItemIdArg("x-svn:///svn/demo1^/v/ab/c.xml").getUrlAtHost());
		assertEquals("/parent/demo2/v/a%20b/c.xml", new CmsItemIdArg("x-svn:///parent/demo2^/v/a%20b/c.xml").getUrlAtHost());
		assertEquals("/svn/r2/v/a%20b/c.xml", new CmsItemIdArg("x-svn://host.x/svn/r2^/v/a%20b/c.xml").getUrlAtHost());
		assertEquals("/s/r2/v/a%20b/c.xml", new CmsItemIdArg("x-svn://host.x/s/r2^/v/a%20b/c.xml?p=123").getUrlAtHost());
		assertEquals("not expecting parent path or repo name to be encoded, should not be allowed",
				"/svn/not allowed/v/a%20b/c.xml", new CmsItemIdArg("x-svn:///svn/not allowed^/v/a%20b/c.xml").getUrlAtHost());
	}
	
}
