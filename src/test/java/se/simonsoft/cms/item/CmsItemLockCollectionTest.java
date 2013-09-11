package se.simonsoft.cms.item;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.impl.CmsItemIdUrl;
import se.simonsoft.cms.item.impl.CmsItemLockImpl;

public class CmsItemLockCollectionTest {

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testAddWrongRepository() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(repo1){}.add(new CmsItemLockImpl(new CmsItemIdUrl(repo2, new CmsItemPath("/file")), "token", "owner", "comment", new Date(), new Date()));
	}

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testGetWrongRepository() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(repo1){}.getLocked(new CmsItemIdUrl(repo2, new CmsItemPath("/file")));
	}

	@SuppressWarnings("serial")
	@Test(expected=IllegalArgumentException.class)
	public void testConstructorRepositoryMismatch() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		new CmsItemLockCollection(
				new CmsItemLockImpl(new CmsItemIdUrl(repo1, new CmsItemPath("/1")), "t1", "", "", new Date(), null),
				new CmsItemLockImpl(new CmsItemIdUrl(repo2, new CmsItemPath("/2")), "t2", "", "", new Date(), null)) {};
	}
	
	/**
	 * CmsItemLockCollection has stronger requirement on single-repository than CmsCommitChangeset. Change constructor for changeset?
	 * Isn't optional repository validation a feature anyway?
	 */
	public void testRepositoryNull() {
		CmsRepository repo1 = mock(CmsRepository.class);
		CmsRepository repo2 = mock(CmsRepository.class);
		@SuppressWarnings("serial")
		CmsItemLockCollection anyrepo = new CmsItemLockCollection(null) {};
		anyrepo.add(new CmsItemLockImpl(new CmsItemIdUrl(repo1, new CmsItemPath("/1")), "t1", "", "", new Date(), null));
		anyrepo.add(new CmsItemLockImpl(new CmsItemIdUrl(repo2, new CmsItemPath("/2")), "t2", "", "", new Date(), null));
		anyrepo.getLocked(new CmsItemIdUrl(repo1, new CmsItemPath("/1")));
		anyrepo.getLocked(new CmsItemIdUrl(repo2, new CmsItemPath("/2")));
	}
	
	@Test
	public void testIteration() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/2")), "token1", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/3")), "token1", "", "", new Date(), null);
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
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/2")), "token2", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/3")), "token3", "", "", new Date(), null);
		@SuppressWarnings("serial")
		Map<String,String> tokens = new CmsItemLockCollection(lock2, lock3, lock1) {}.getPathTokens();
		assertEquals("token1", tokens.get("/1"));
		assertEquals("token2", tokens.get("/2"));
		assertEquals("token3", tokens.get("/3"));
	}

	@Test
	public void testContainsPath() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsItemLock lock1 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/2")), "token2", "", "", new Date(), null);
		CmsItemLock lock3 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/3")), "token3", "", "", new Date(), null);
		@SuppressWarnings("serial")
		CmsItemLockCollection locks = new CmsItemLockCollection(lock2, lock3, lock1) {};
		assertTrue(locks.containsPath(lock1.getItemId().getRelPath()));
		assertTrue(locks.containsPath(new CmsItemPath("/1")));
		assertTrue(locks.containsPath(new CmsItemPath("/2")));
		assertTrue(locks.containsPath(new CmsItemPath("/3")));
		assertFalse(locks.containsPath(new CmsItemPath("/11")));
		assertFalse(locks.containsPath(null));
		assertFalse(locks.containsPath(CmsItemPath.ROOT));
	}

	@Test
	public void testContains() {
		CmsRepository repo = mock(CmsRepository.class);
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock2 = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/2")), "token2", "", "", now, null);
		CmsItemLock lock2other = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/2")), "token2other", "", "", now, null);
		@SuppressWarnings("serial")
		CmsItemLockCollection locks = new CmsItemLockCollection(lock1a, lock2) {};
		assertTrue(locks.contains(lock1a));
		assertTrue("lock equals", locks.contains(lock1b));
		assertTrue(locks.contains(lock2));
		assertFalse("not same token", locks.contains(lock2other));
	}

	@SuppressWarnings("serial")
	@Test
	public void testAddEquals() {
		CmsRepository repo = mock(CmsRepository.class);
		Date now = new Date();
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", now, null);
		assertTrue(lock1a.equals(lock1b));
		CmsItemLockCollection c = new CmsItemLockCollection(null) {}; // any repository
		c.add(lock1a);
		try {
			c.add(lock1b);
			fail("Adding an equal lock indicates code error, should cause exception");
		} catch (IllegalArgumentException e) {
			assertTrue("Got " + e, e.getMessage().startsWith("Duplicate lock information"));
		}
	}	
	
	@SuppressWarnings("serial")
	@Test
	public void testAddSamePath() {
		CmsRepository repo = mock(CmsRepository.class);
		CmsRepository repx = mock(CmsRepository.class);
		CmsItemLock lock1a = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLock lock1b = new CmsItemLockImpl(new CmsItemIdUrl(repo, new CmsItemPath("/1")), "token2", "", "", new Date(), null);
		CmsItemLock lock1x = new CmsItemLockImpl(new CmsItemIdUrl(repx, new CmsItemPath("/1")), "token1", "", "", new Date(), null);
		CmsItemLockCollection cAny = new CmsItemLockCollection(null) {};
		cAny.add(lock1a);
		cAny.add(lock1x);
		CmsItemLockCollection cSingle = new CmsItemLockCollection(repo) {};
		cSingle.add(lock1a);
		try {
			cSingle.add(lock1b);
			fail("Should not accept two items of the same path");
		} catch (IllegalArgumentException e) {
			assertTrue("Got " + e, e.getMessage().startsWith("Duplicate lock path /1 in the same repository"));
		}
		// Dropping this edge case because setting repository==null effectively disables validation
		//try {
		//	cAny.add(lock1b);
		//	fail("Should not accept two items of the same path if they have the same repository");
		//} catch (IllegalArgumentException e) {
		//	assertTrue("Got " + e, e.getMessage().startsWith("Duplicate lock path /1 in the same repository"));
		//}
	}
	
}
