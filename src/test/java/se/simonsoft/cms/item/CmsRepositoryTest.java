/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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
package se.simonsoft.cms.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
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
		assertTrue("http/https on standard ports should be considered equal",
				r.equals(new CmsRepository("https://cmsdemo.pdsvision.net/svn/re%20po1")));
		assertTrue("equals with string should be ok", r.equals("http://cmsdemo.pdsvision.net/svn/re%20po1"));
		assertFalse("trailing slash makes a lot of difference when building URLs",
				r.equals("http://cmsdemo.pdsvision.net/svn/re%20po1/"));
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
		assertEquals("https://x.y.zz:321/parent/path/repo", r.getUrl());
		CmsRepository r2 = new CmsRepository("https", "demo.simonsoftcms.se", "/svn", "demo1");
		assertEquals("https://demo.simonsoftcms.se/svn/demo1", r2.toString());
		try {
			new CmsRepository("http", "x.y/", "/s", "r");
			fail("Should validate host");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new CmsRepository("http:", "x.y", "/s", "r");
			fail("Should validate protocol");
		} catch (IllegalArgumentException e) {
			// expected
		}
		try {
			new CmsRepository("http//", "x.y", "/s", "r");
			fail("Should validate protocol");
		} catch (IllegalArgumentException e) {
			// expected
		}
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
		assertTrue(new CmsRepository(null, null, "/parent", "repo1").equals(r));
		 
		assertTrue("By design we should assume equals=true if one of the repository instances has no host",
				r.equals(new CmsRepository("http", "host.name", "/parent", "repo1")));
		assertTrue(new CmsRepository("/parent", "repo1").equals(r));
		assertFalse(r.equals(new CmsRepository("http://host.name/parent/repo2")));
		assertFalse(new CmsRepository("/parent", "repo3").equals(r));
		
		assertEquals(new CmsRepository("http://host.name/parent/repo1").hashCode(), r.hashCode());
		assertEquals(new CmsRepository("http", "host.name", "/parent", "repo1").hashCode(), r.hashCode());
	}
	
	@Test
	public void testEqualsHttpHttpsStandardPorts() {
		assertTrue(new CmsRepository("http://test.net/svn/r1").equals(new CmsRepository("http://test.net/svn/r1")));
		assertFalse(new CmsRepository("http://tes.net/svn/r1").equals(new CmsRepository("http://test.net/svn/r1")));
		assertTrue("Equals should be true for standard ports regardless of http/https",
				new CmsRepository("http://test.net/svn/r1").equals(new CmsRepository("https://test.net/svn/r1")));
		assertFalse("Any custom ports used and it could be separate hosts",
				new CmsRepository("http://test.net:81/svn/r1").equals(new CmsRepository("https://test.net/svn/r1")));
		assertFalse("Any custom ports used for https and it could be separate hosts",
				new CmsRepository("http://test.net/svn/r1").equals(new CmsRepository("https://test.net:444/svn/r1")));
		assertTrue("Specifying port 80 should be normalized",
				new CmsRepository("http://test.net:80/svn/r1").equals(new CmsRepository("https://test.net/svn/r1")));
		assertTrue("Specifying port 443 should be normalized",
				new CmsRepository("http://test.net/svn/r1").equals(new CmsRepository("https://test.net:443/svn/r1")));
		assertEquals(new CmsRepository("http://test.net/svn/r1").hashCode(), new CmsRepository("https://test.net:443/svn/r1").hashCode());
	}
	
	@Test()
	public void testProtocolNull() {
		CmsRepository pNull = new CmsRepository(null, "test.net", "/svn", "repo");
		
		// This documents how the code currently works. Insane?!
		assertTrue(pNull.isHostKnown());
		assertEquals("null://test.net", pNull.getServerRootUrl());
		
		//assertTrue(new CmsRepository("http://test.net/svn/r1").equals(pNull));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHostNotEmpty() {
		new CmsRepository("", "/svn", "repo");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testHostnameNotEmpty() {
		new CmsRepository("http", "", "/svn", "repo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHostUrlNotEmpty1() {
		new CmsRepository("http://", "/svn", "repo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHostUrlNotEmpty2() {
		new CmsRepository("http:///", "/svn", "repo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParentTrailingSlash() {
		new CmsRepository("http://test.me", "/svn/", "repo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParentNoLeadingSlash() {
		new CmsRepository("http://test.me", "svn", "repo");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testHostIllegalChars() {
		new CmsRepository("http", "://test.me", "/svn", "repo");
	}
	
	@Test
	public void testNormalizePort() {
		assertEquals("myhost", new CmsRepository("http://myhost:80/s/r").getHost());
		assertEquals("myhost:443", new CmsRepository("http://myhost:443/s/r").getHost());
		assertEquals("sslhost", new CmsRepository("https://sslhost:443/s/r").getHost());
		assertEquals("sslhost:80", new CmsRepository("https://sslhost:80/s/r").getHost());
		assertEquals("myhost", new CmsRepository("http://myhost:80", "/s", "r").getHost());
		assertEquals("myhost", new CmsRepository("http", "myhost:80", "/s", "r").getHost());
	}
	
	@Test
	public void testGetUrlAtHost() {
		assertEquals("/svn/repo1", new CmsRepository("http://myhost/svn/repo1").getUrlAtHost());
		assertEquals("/svn/re%20po1", new CmsRepository("http://myhost/svn/re%20po1").getUrlAtHost());
	}
	
	@Test
	public void testRepositoryInServerRoot() {
		CmsRepository r = new CmsRepository("http://myhost/repoN");
		assertNotNull("Parent path starts with slash and does not need to be null if root", r.getParentPath());
		assertEquals("", r.getParentPath());
		assertTrue(new CmsRepository("", "repoN").equals(r));
		assertFalse(new CmsRepository("/svn", "repoN").equals(r));
	}
	
	@Test
	public void testGetItemId() {
		CmsRepository repo1 = new CmsRepository("https://host/svn/repo1");
		assertTrue("Should preserve the instance", repo1 == repo1.getItemId().getRepository());
		assertEquals("Relpath has no representation of root", null, repo1.getItemId().getRelPath());
//		assertEquals(new CmsItemIdArg("x-svn://host/svn/repo^/"), // not sure this is a good syntax, why not omit the inconsistent slash?
//				repo1.getItemId());
		CmsItemId dir = repo1.getItemId().withRelPath(new CmsItemPath("/folder"));
		assertEquals(repo1, dir.getRepository());
		assertEquals(null, repo1.getItemId().getPegRev());
		assertEquals(new Long(3), repo1.getItemId().withPegRev(3L).getPegRev());
	}
	
	@Test
	public void testGetItemIdTransfer() {
		CmsRepository repo1 = new CmsRepository("https://host/svn/repo1");
		CmsItemId item1 = repo1.getItemId("https://host/svn/repo1/fo%20a/file.txt");
		assertEquals(repo1, item1.getRepository());
		assertEquals("/fo a/file.txt", item1.getRelPath().toString());
	}
	
	@Test
	@Ignore // what's the spec?
	public void testGetItemIdTransferSslAgnostic() {
		CmsRepository repo1 = new CmsRepository("http://host/svn/repo1");
		CmsItemId item1 = repo1.getItemId("https://host/svn/repo1/fo%20a/file.txt");
		assertEquals(repo1, item1.getRepository());
		assertEquals("/fo a/file.txt", item1.getRelPath().toString());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testGetItemIdDifferentRepo() {
		new CmsRepository("https://host/svn/repo1").getItemId("https://host/svn/repo2/file.txt");
	}
	
	@Test
	public void testGetItemIdTransferDefaultBackend() {
		CmsRepository repo1 = new CmsRepository("https://host/svn/repo1");
		CmsItemId item1 = repo1.getItemId("https://host/svn/repo1/fo%20a/file.txt");
		assertEquals(repo1, item1.getRepository());
		assertEquals("x-svn:///svn/repo1^/fo%20a/file.txt", item1.getLogicalId());
		assertEquals("x-svn://host/svn/repo1^/fo%20a/file.txt", item1.getLogicalIdFull());
	}
	
	@Test
	@Ignore // I don't think preservation of encoding is the desired behavior any more (if it ever was).
	public void testGetItemIdTransferPreservesEncoding() {
		CmsRepository repo1 = new CmsRepository("https://host/svn/repo1");
		assertEquals("https://host/svn/repo1/fo%2Ba/file.txt",
				repo1.getItemId("https://host/svn/repo1/fo%2Ba/file.txt").getUrl());
		assertEquals("https://host/svn/repo1/fo+a/file.txt",
				repo1.getItemId("https://host/svn/repo1/fo+a/file.txt").getUrl());		
	}
	
	@Test
	public void testGetItemIdTransferNormativeEncoding() {
		CmsRepository repo1 = new CmsRepository("https://host/svn/repo1");
		assertEquals("https://host/svn/repo1/fo+a/file.txt",
				repo1.getItemId("https://host/svn/repo1/fo%2Ba/file.txt").getUrl());
		
		// TODO: Sort out the issues with decoding +
		/*
		assertEquals("https://host/svn/repo1/fo+a/file.txt",
				repo1.getItemId("https://host/svn/repo1/fo+a/file.txt").getUrl());	
		*/
		assertEquals("https://host/svn/repo1/fo%20(a)/file.txt",
				repo1.getItemId("https://host/svn/repo1/fo%20(a)/file.txt").getUrl());	
	}
	
	
	@Test
	public void testGetItemIdFromUrl() { // was: CmsItemIdArgTest.testConstructorUrl() 
		
		String url1 = "http://host.n/svn/d1";
		CmsRepository repo1 = new CmsRepository(url1);
		
		CmsItemId id1_0 = repo1.getItemId("http://host.n/svn/d1");
		assertEquals(repo1, id1_0.getRepository());
		assertEquals(url1, id1_0.getRepository().getUrl());
		assertEquals("x-svn:///svn/d1^/", id1_0.getLogicalId());
		assertEquals("x-svn://host.n/svn/d1^/", id1_0.getLogicalIdFull());
		assertNull(id1_0.getRelPath());
		
		CmsItemId id1_1 = repo1.getItemId("http://host.n/svn/d1/vv/xml/8.xml");
		assertEquals(repo1, id1_1.getRepository());
		assertEquals(url1, id1_1.getRepository().getUrl());
		assertEquals("x-svn:///svn/d1^/vv/xml/8.xml", id1_1.getLogicalId());
		assertEquals("x-svn://host.n/svn/d1^/vv/xml/8.xml", id1_1.getLogicalIdFull());
		assertEquals("/vv/xml/8.xml", id1_1.getRelPath().toString());
		
		CmsItemId id1_2 = repo1.getItemId("http://host.n/svn/d1/vv/xml/8.xml").withPegRev(123L);
		assertEquals("x-svn:///svn/d1^/vv/xml/8.xml?p=123", id1_2.getLogicalId());
		assertEquals("x-svn://host.n/svn/d1^/vv/xml/8.xml?p=123", id1_2.getLogicalIdFull());
		assertEquals("/vv/xml/8.xml", id1_2.getRelPath().toString());
		
		CmsItemId id1_3 = repo1.getItemId("http://host.n/svn/d1/v/a%20b/c.xml");
		assertEquals(repo1, id1_1.getRepository());
		assertEquals(url1, id1_1.getRepository().getUrl());
		assertEquals("x-svn:///svn/d1^/v/a%20b/c.xml", id1_3.getLogicalId());
		assertEquals("x-svn://host.n/svn/d1^/v/a%20b/c.xml", id1_3.getLogicalIdFull());
		assertEquals("/v/a b/c.xml", id1_3.getRelPath().toString());
		
		
		String url2 = "http://host.n/svn/svnparent/d1";
		CmsRepository repo2 = new CmsRepository(url2);
		
		CmsItemId id2_1 = repo2.getItemId("http://host.n/svn/svnparent/d1/vv/xml/8.xml");
		assertEquals(repo2, id2_1.getRepository());
		assertEquals(url2, id2_1.getRepository().getUrl());
		assertEquals("x-svn:///svn/svnparent/d1^/vv/xml/8.xml", id2_1.getLogicalId());
		assertEquals("x-svn://host.n/svn/svnparent/d1^/vv/xml/8.xml", id2_1.getLogicalIdFull());
		assertEquals("/vv/xml/8.xml", id2_1.getRelPath().toString());
		
		try {
			@SuppressWarnings("unused")
			CmsItemId id1_illegal = repo1.getItemId("http://host.n/svn/");
			fail("should throw IAE");
		} catch (IllegalArgumentException e) {
			
		}
	}
	
}
