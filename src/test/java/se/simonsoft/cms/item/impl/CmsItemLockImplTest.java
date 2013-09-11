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
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1x = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "tokenX", "", "", now, null);
		CmsItemLock lock1f = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/F")), "token1", "", "", now, null);
		CmsItemLock lock1o = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "o", "", now, null);
		CmsItemLock lock1m = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "m", now, null);
		CmsItemLock lock1d = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", new Date(now.getTime() + 1), null);
		CmsItemLock lock1e = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, new Date());
		// maybe these rules could be relaxed but the safe start is to verify everything
		assertTrue(lock1a.equals(lock1b));
		assertFalse(lock1a.equals(lock1x));
		assertFalse(lock1a.equals(lock1f));
		assertFalse(lock1a.equals(lock1o));
		assertFalse(lock1a.equals(lock1m));
		assertFalse(lock1a.equals(lock1d));
		assertFalse(lock1a.equals(lock1e));
	}

}
