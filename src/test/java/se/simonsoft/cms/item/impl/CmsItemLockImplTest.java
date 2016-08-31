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
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

public class CmsItemLockImplTest {

	@Test
	public void testEquals() {
		CmsRepository repo = mock(CmsRepository.class);		
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1x = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "tokenX", "", "", now, null);
		CmsItemLock lock1f = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/F")), "token1", "", "", now, null);
		CmsItemLock lock1o = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "o", "", now, null);
		CmsItemLock lock1m = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "m", now, null);
		CmsItemLock lock1d = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", new Date(now.getTime() + 1), null);
		CmsItemLock lock1e = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "", "", now, new Date());
		// maybe these rules could be relaxed but the safe start is to verify everything
		assertTrue(lock1a.equals(lock1b));
		assertFalse(lock1a.equals(lock1x));
		assertFalse(lock1a.equals(lock1f));
		assertFalse(lock1a.equals(lock1o));
		assertFalse(lock1a.equals(lock1m));
		assertFalse(lock1a.equals(lock1d));
		assertFalse(lock1a.equals(lock1e));
	}
	
	@Test
	public void testToString() {
		
		CmsRepository repo = mock(CmsRepository.class);
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdArg(repo, new CmsItemPath("/1")), "token1", "username", "comment", now, null);
		
		assertEquals("[/1|username|token1]", lock1a.toString());
	}

}
