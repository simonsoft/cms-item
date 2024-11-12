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
package se.simonsoft.cms.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.impl.CmsItemIdArg;
import se.simonsoft.cms.item.impl.CmsItemLockImpl;

public class CmsItemLockCollectionTest {

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testAddWrongRepository() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(repo1){}.add(new CmsItemLockImpl(new CmsItemIdArg(repo2, new CmsItemPath("/file")), "token", "owner", "comment", new Date(), new Date()));
	}

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testGetWrongRepository() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(repo1){}.getLocked(new CmsItemIdArg(repo2, new CmsItemPath("/file")));
	}

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorRepositoryMismatch() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(
				new CmsItemLockImpl(new CmsItemIdArg(repo1, new CmsItemPath("/1")), "t1", "", "", new Date(), null),
				new CmsItemLockImpl(new CmsItemIdArg(repo2, new CmsItemPath("/2")), "t2", "", "", new Date(), null)) {};
	}
	
	@Test
	public void testIteration() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/2")), "token1", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/3")), "token1", "", "", new Date(), null);
		@SuppressWarnings("serial")
		CmsItemLockCollection locks = new CmsItemLockCollection(lock2, lock3, lock1) {};
		Iterator<CmsItemLock> it = locks.iterator();
		assertEquals(lock2, it.next());
		assertEquals(lock3, it.next());
		assertEquals(lock1, it.next());
		assertFalse(it.hasNext());
	}
	
	@Test
	public void testGetPathTokens() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/2")), "token2", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/3")), "token3", "", "", new Date(), null);
		@SuppressWarnings("serial")
		Map<String,String> tokens = new CmsItemLockCollection(lock2, lock3, lock1) {}.getPathTokens();
		assertEquals("token1", tokens.get("/1"));
		assertEquals("token2", tokens.get("/2"));
		assertEquals("token3", tokens.get("/3"));
	}

	@Test
	public void testContainsPath() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/2")), "token2", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/3")), "token3", "", "", new Date(), null);
		@SuppressWarnings("serial")
		CmsItemLockCollection locks = new CmsItemLockCollection(lock2, lock3, lock1) {};
		assertTrue(locks.containsPath(lock1.getItemId().getRelPath()));
		assertTrue(locks.containsPath(new CmsItemPath("/1")));
		assertTrue(locks.containsPath(new CmsItemPath("/2")));
		assertTrue(locks.containsPath(new CmsItemPath("/3")));
		assertFalse(locks.containsPath(new CmsItemPath("/11")));
		assertFalse(locks.containsPath(null));
		assertNotNull(locks.getLocked(new CmsItemPath("/1")));
		assertNull(locks.getLocked(new CmsItemPath("/11")));
		assertNull(locks.getLocked((CmsItemPath) null));
	}

	@Test
	public void testContains() {
		CmsRepository repo = mock(CmsRepository.class);
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/2")), "token2", "", "", now, null);
		CmsItemLock lock2other = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/2")), "token2other", "", "", now, null);
		@SuppressWarnings("serial")
		CmsItemLockCollection locks = new CmsItemLockCollection(lock1a, lock2) {};
		assertTrue(locks.contains(lock1a));
		assertTrue("lock equals", locks.contains(lock1b));
		assertTrue(locks.contains(lock2));
		assertFalse("not same token", locks.contains(lock2other));
	}

	@Test
	public void testAddEquals() {
		CmsRepository repo = mock(CmsRepository.class);
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		assertTrue(lock1a.equals(lock1b));
	}	
	
	@SuppressWarnings("serial")
	@Test
	public void testAddSamePath() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token2", "", "", new Date(), null);
		CmsItemLockCollection cSingle = new CmsItemLockCollection(repo) {};
		cSingle.add(lock1a);
		try {
			cSingle.add(lock1b);
			fail("Should not accept two items of the same path");
		} catch (IllegalArgumentException e) {
			assertTrue("Got " + e, e.getMessage().startsWith("Duplicate lock path /1 in the same repository"));
		}
	}
	
}
