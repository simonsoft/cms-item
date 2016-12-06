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
package se.simonsoft.cms.item.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

public class CmsItemIdBaseTest {

	@Test
	public void testArgAndUrl() {
		CmsItemIdArg arg = new CmsItemIdArg("x-svn:///svn/repo1^/a/b%20c.txt");
		CmsItemIdUrl url = new CmsItemIdUrl(new CmsRepository("/svn", "repo1"), new CmsItemPath("/a/b c.txt"));
		assertTrue(arg.equals(url));
		assertTrue(url.equals(arg));
		assertEquals(arg.hashCode(), url.hashCode());
		assertFalse(arg.withPegRev(1L).equals(url));
		assertFalse("hash code should differ on peg rev because there might be collections with a history of the same file",
				arg.withPegRev(1L).hashCode() == arg.hashCode());
		assertTrue(arg.withPegRev(2L).equals(url.withPegRev(2L)));
		assertTrue(url.withPegRev(2L).equals(arg.withPegRev(2L)));
		assertFalse(arg.withPegRev(2L).equals(url.withPegRev(3L)));
		assertEquals(arg.withPegRev(2L).hashCode(), url.withPegRev(2L).hashCode());
		// this should not change result of equals
		arg.setHostnameOrValidate("host.x");
		assertTrue("one unknown host, still equals", arg.equals(url));
		assertTrue(url.equals(arg));
		CmsItemIdUrl urlh = new CmsItemIdUrl(new CmsRepository("http://host.y/svn/repo1"), new CmsItemPath("/a/b c.txt"));
		assertFalse("different host, not equals", urlh.equals(arg));
	}
	
	@Test
	public void testRepositoryRoot() {
		CmsItemIdArg arg = new CmsItemIdArg("x-svn:///svn/repo1^/");
		CmsItemIdUrl url = new CmsItemIdUrl(new CmsRepository("/svn", "repo1"), (CmsItemPath) null);
		assertTrue(arg.equals(url));
		assertTrue(url.equals(arg));
		assertEquals(arg.hashCode(), url.hashCode());
		assertFalse(url.withRelPath(new CmsItemPath("/x")).equals(arg));
		assertFalse(url.withRelPath(new CmsItemPath("/x")).equals(url));
	}

}
