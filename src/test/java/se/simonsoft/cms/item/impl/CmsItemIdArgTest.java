/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateColonInRepo() {
		new CmsItemIdArg("x-svn:///svn/d:mo1^/vvab/graphics/0001.tif");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateColonInPath() {
		new CmsItemIdArg("x-svn:///svn/demo1^/vvab/grap:ics/0001.tif");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateQuestionInPath() {
		new CmsItemIdArg("x-svn:///svn/demo1^/vvab/grap?ics/0001.tif");
	}
	
	@Test //Ampersand is actually allowed in URL path. Related to the differences in encoding rules btw path and query.
	public void testValidateAmpersandInPath() {
		new CmsItemIdArg("x-svn:///svn/demo1^/vvab/grap&ics/0001.tif");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragment() {
		new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/topic.dita#topic1/para1");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testValidateFragmentRev() {
		new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/topic.dita?p=123#topic1/para1");
	}
	
	@Test
	public void testPegHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn://demo.simonsoftcms.se/svn/demo1^/vvab/xml/Docs/Sa%20s.xml?p=9");
		assertTrue(p.getRepository().isHostKnown());
		assertTrue(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		assertEquals("/vvab/xml/Docs/Sa s.xml", p.getRelPath().toString());
		assertEquals("http://demo.simonsoftcms.se/svn/demo1", p.getRepository().toString());
		assertEquals(p.getLogicalIdFull(), p.toString());
	}
	
	@Test
	public void testPegNoHost() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/xml/sections/In%20in.xml?p=101");
		assertFalse(p.getRepository().isHostKnown());
		assertFalse(p.isFullyQualifiedOriginally());
		assertTrue(p.isPegged());
		p.setHostnameOrValidate("example.net");
		assertEquals("http://example.net/svn/demo1", p.getRepository().getUrl());
		assertEquals("http://example.net/svn/demo1/vvab/xml/sections/In%20in.xml", p.getUrl());
	}
	
	@Test
	public void testNoHostNoPeg() {
		CmsItemIdArg p = new CmsItemIdArg("x-svn:///svn/demo1^/vvab/graphics/0001.tif");
		assertFalse(p.getRepository().isHostKnown());
		assertFalse(p.isFullyQualifiedOriginally());
		assertFalse(p.isPegged());
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
		assertTrue(p.getRepository().isHostKnown());
		assertFalse(p.isFullyQualifiedOriginally());
		assertEquals("x-svn:///svn/demo1^/vvab/graphics/0001.tif", p.getLogicalId());
		assertEquals("x-svn://x.y.z/svn/demo1^/vvab/graphics/0001.tif", p.getLogicalIdFull());
		assertEquals("http://x.y.z/svn/demo1/vvab/graphics/0001.tif", p.getUrl());
		assertEquals("http://x.y.z/svn/demo1", p.getRepositoryUrl());
		assertFalse(p.isPegged());
		assertEquals("API spec", null, p.getPegRev());
		p.setPegRev(1234567);
		assertTrue(p.isPegged());
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
		assertTrue("Assume same hostname when one is not set",
				i1.equals(new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml")));
		assertTrue("Assume the other way too",
				new CmsItemIdArg("x-svn:///svn/d1^/vv/r/A/xml/8.xml").equals(i1));
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
	public void testRepoRootId() {
		CmsItemId repoId = new CmsItemIdArg("x-svn://local.test/r^/");
		assertEquals(null, repoId.getRelPath());
	}
	
	@Test
	public void testWithRelPathToRepoRoot() {
		CmsItemId i1 = new CmsItemIdArg("x-svn://x.y/parent/repo^/a/b.xml");
		CmsItemId repoId = i1.withRelPath(null);
		assertEquals("x-svn:///parent/repo^/", repoId.getLogicalId());
		assertEquals("For consistency, URLs can not have traling slash",
				"http://x.y/parent/repo", repoId.getUrl());
		CmsItemId i2 = new CmsItemIdArg("x-svn://x.y/p/r^/a?p=9");
		CmsItemId repoRootRev = i2.withRelPath(null);
		assertEquals("x-svn:///p/r^/?p=9", repoRootRev.getLogicalId());
		// We have not path to represent root
		//CmsItemId repoAlso = i1.withRelPath(new CmsItemPath("/"));
		//assertEquals("x-svn://x.y/parent/repo^/", repoAlso.getLogicalIdFull());
		assertEquals("CmsItemPath can not represent root", null, repoId.getRelPath());
	}
	
	@Test
	public void testWithRelPathEncoding() {
		CmsItemId i1 = new CmsItemIdArg("x-svn:///svn/demo1^/v/a%20b/c.xml");
		assertEquals("Should preserve logicalId encoding (from constructor)", 
				"x-svn:///svn/demo1^/v/a%20b", i1.withRelPath(new CmsItemPath("/v/a b")).getLogicalId());
		
		assertEquals("Should now be able to do logicalId encoding", 
				"x-svn:///svn/demo1^/v%20(copy)/a%20b", i1.withRelPath(new CmsItemPath("/v (copy)/a b")).getLogicalId());
		
		
		// The preservation can be discussed. Should it normalize even via constructor? Will impact equals(..)?
		CmsItemId i2 = new CmsItemIdArg("x-svn:///svn/demo1^/v%20%28copy%29/a%20b/fä.xml");
		assertEquals("Should preserve logicalId encoding (from constructor)", 
				"x-svn:///svn/demo1^/v%20%28copy%29/a%20b/fä.xml", i2.getLogicalId());
		
		// Important to ensure that new paths are freshly encoded with normalizing code. (new in cms-item 2.3)
		assertEquals("Should normalize logicalId encoding (when changing relpath)", 
				"x-svn:///svn/demo1^/v%20(copy)/a%20b/f%C3%A4.xml", i2.withRelPath(new CmsItemPath("/v (copy)/a b/fä.xml")).getLogicalId());
		
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
		assertTrue("assume same host when one is not set",
				i.getRepository().equals(new CmsItemIdArg("x-svn://x.y/parent/repo^/x").getRepository()));
		assertTrue(i.getRepository().equals(new CmsRepository("http://x.y/parent/repo")));
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
		
		assertEquals("/svn/repo1/demo/Documents/Presentation%20-%20(B).xml", 
				new CmsItemIdArg("x-svn://localtesthost/svn/repo1^/demo/Documents/Presentation%20-%20(B).xml").getUrlAtHost());
	}
	
	@Test
	public void testUrldecode() {
		
		assertEquals("/a&b", new CmsItemIdArg("x-svn://host.x/s/r2^/a&b").getRelPath().toString());
		assertEquals("/(a b)", new CmsItemIdArg("x-svn://host.x/s/r2^/(a%20b)").getRelPath().toString());
		// TODO Support + in decoder (don't decode it).
		// Plus should NOT be encoded in path of URL. However, it means space in query.
		// http://stackoverflow.com/questions/1005676/urls-and-plus-signs
		//assertEquals("/a+b", new CmsItemIdArg("x-svn://host.x/s/r2^/a+b").getRelPath().toString());
	}
	
	@Test
	public void testHostnameMatchPort() {
		new CmsItemIdArg("x-svn://host.x:1234/svn/r^/x").setHostnameOrValidate("host.x:1234");
		try {
			new CmsItemIdArg("x-svn://host.x:1234/svn/r^/x").setHostnameOrValidate("host.x");
			fail("Should require exactly the same host for set after instantiation");
		} catch (IllegalArgumentException e) {
			// expected
		}
	}
	
	@Test
	public void testRelPathInstanceReuse() {
		CmsItemId id = new CmsItemIdArg("x-svn:///svn/demo1^/v/ab/c.xml");
		assertTrue("there's quite some string manipulation in conversion to path so this should be done only once",
				id.getRelPath() == id.getRelPath());
	}
	
	@Test
	public void testValidateRelPathAtCreation() {
		try {
			new CmsItemIdArg("x-svn:///svn/demo1^whot");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid path: 'whot'", e.getMessage());
		}
	}
	
	@Test
	public void testRepositoryNameNeedsEncoding() {
		// It is reasonable to not support this in repo part of logical IDs, but should we maybe warn or bail out?
		CmsItemId id1 = new CmsItemIdArg("x-svn:///svn/de%20mo1^/ab.xml");
		CmsItemId id2 = new CmsItemIdArg("x-svn:///s%20vn/demo1^/ab.xml");
		CmsItemId id3a = new CmsItemIdArg("x-svn:///s%20vn/de%20mo1^/a%20b.xml");
		CmsItemId id3b = new CmsItemIdArg("x-svn://host.a:987/s%20vn/de%20mo1^/a%20b.xml");
		assertEquals("x-svn:///svn/de%20mo1^/ab.xml", id1.getLogicalId());
		assertEquals("/svn", id1.getRepository().getParentPath());
		//assertEquals("de mo1", id1.getRepository().getName());
		assertEquals("/svn/de%20mo1", id1.getRepository().getUrlAtHost());
		//assertEquals("/s vn", id2.getRepository().getParentPath());
		assertEquals("/s%20vn/demo1", id2.getRepository().getUrlAtHost());
		assertEquals("/s%20vn/de%20mo1", id3a.getRepository().getUrlAtHost());
		assertEquals("http://host.a:987/s%20vn/de%20mo1", id3b.getRepository().getUrl());
		assertEquals("http://host.a:987/s%20vn/de%20mo1/a%20b.xml", id3b.getUrl());
	}
	

	
}
