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

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

public class CmsItemIdUrlTest {

	CmsRepository repo1 = new CmsRepository("http://localhost:1234/svn/repo1");
	CmsItemPath p1 = new CmsItemPath("/p1");
	
	@Test(expected=UnsupportedOperationException.class)
	public void testGetLogicalId() {
		new CmsItemIdUrl(repo1, p1).getLogicalId();
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testGetLogicalIdFull() {
		new CmsItemIdUrl(repo1, p1).getLogicalIdFull();
	}

	@Test
	public void testGetRelPath() {
		assertEquals(p1, new CmsItemIdUrl(repo1, p1).getRelPath());
	}

	@Test
	public void testGetUrl() {
		assertEquals("" + repo1 + p1, new CmsItemIdUrl(repo1, p1).getUrl());
	}

	@Test
	public void testGetUrlNoRev() {
		assertEquals("Url shold not contain revision", "" + repo1 + p1, 
				new CmsItemIdUrl(repo1, p1).withPegRev(123L).getUrl());
	}
	
	@Test
	public void testGetPegRev() {
		assertEquals(null, new CmsItemIdUrl(repo1, p1).getPegRev());
		assertEquals(new Long(123), new CmsItemIdUrl(repo1, p1).withPegRev(123L).getPegRev());
	}

	@Test
	public void testGetRepository() {
		assertEquals(repo1, new CmsItemIdUrl(repo1, p1).getRepository());
	}

	@Test
	public void testGetRepositoryUrl() {
		assertEquals("" + repo1, new CmsItemIdUrl(repo1, p1).getRepositoryUrl());
	}

	@Test
	public void testEquals() {
		assertTrue(new CmsItemIdUrl(repo1, p1).equals(new CmsItemIdUrl(repo1, p1)));
		assertTrue(new CmsItemIdUrl(repo1, p1).equals(
				new CmsItemIdUrl(new CmsRepository(repo1.getUrl()), new CmsItemPath(p1.getPath()))));
		assertFalse(new CmsItemIdUrl(repo1, p1).equals(new CmsItemIdUrl(repo1, new CmsItemPath("/p2"))));
		assertFalse(new CmsItemIdUrl(repo1, p1).equals(new CmsItemIdUrl(
				new CmsRepository(repo1.getUrl().replace("http", "https")), p1)));
		assertTrue(new CmsItemIdUrl(repo1, p1).withPegRev(3L)
				.equals(new CmsItemIdUrl(repo1, p1).withPegRev(3L)));
		assertFalse(new CmsItemIdUrl(repo1, p1).withPegRev(null)
				.equals(new CmsItemIdUrl(repo1, p1).withPegRev(3L)));
		assertFalse(new CmsItemIdUrl(repo1, p1).withPegRev(3L)
				.equals(new CmsItemIdUrl(repo1, p1).withPegRev(null)));
		assertFalse(new CmsItemIdUrl(repo1, p1)
				.equals(new CmsItemIdUrl(repo1, p1).withPegRev(3L)));
		assertFalse(new CmsItemIdUrl(repo1, p1).withPegRev(3L)
				.equals(new CmsItemIdUrl(repo1, p1)));
		assertTrue(new CmsItemIdUrl(repo1, p1).withPegRev(3L).withPegRev(null)
				.equals(new CmsItemIdUrl(repo1, p1)));
	}

	@Test
	public void testWithPegRev() {
		CmsItemIdUrl id1 = new CmsItemIdUrl(repo1, p1);
		assertEquals(null, id1.getPegRev());
		CmsItemId id2 = id1.withPegRev(1L);
		assertEquals("should be immutable", null, id1.getPegRev());
		assertEquals(new Long(1), id2.getPegRev());
		CmsItemId id3 = id1.withPegRev(2L);
		assertEquals(new Long(1), id2.getPegRev());
		assertEquals(new Long(2), id3.getPegRev());
		assertEquals(null, id3.withPegRev(null).getPegRev());
	}
	
	@Test
	public void testWithRelPath() {
		CmsItemIdUrl id1 = new CmsItemIdUrl(repo1, p1);
		assertEquals("" + repo1 + "/p3", id1.withRelPath(new CmsItemPath("/p3")).getUrl());
		assertEquals("should be immutable", new CmsItemIdUrl(repo1, p1), id1);
	}
	
	@Test
	public void testUrlencode() {
		assertEquals("" + repo1 + "/a%26b", new CmsItemIdUrl(repo1, new CmsItemPath("/a&b")).getUrl());
		assertEquals("" + repo1 + "/a%2Bb", new CmsItemIdUrl(repo1, new CmsItemPath("/a+b")).getUrl());
		CmsRepository renc = mock(CmsRepository.class);
		when(renc.getUrl()).thenReturn("http://+&%/r1");
		assertEquals("Don't touch repository, was given a URL at creation",
				"http://+&%/r1/p1", new CmsItemIdUrl(renc, p1).getUrl());
	}
	
}
