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
	
	@Test
	public void testEqualsHttpHttpsStandardPorts() {
		assertTrue(new CmsRepository("http://test.net/svn/r1").equals(new CmsRepository("http://test.net/svn/r1")));
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
	
}
