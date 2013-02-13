package se.simonsoft.cms.item.commit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.InputStream;
import java.util.Date;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public class CmsCommitChangesetTest {

	@Test
	public void testAddDuplicate() {
		CmsCommitChangeset c = new CmsCommitChangeset();
		RepoRevision r = new RepoRevision(1, new Date(1));
		c.add(new FileModification(new CmsItemPath("/p3"), r, mock(InputStream.class), mock(InputStream.class)));
		try {
			c.add(new FilePropertyChange(new CmsItemPath("/p3"), r, mock(CmsItemProperties.class)));
			fail("modify+propset should be same operation");
		} catch (IllegalStateException e) { // likely not a user error so avoiding IllegalArgumentException
			//assertEquals("Modify and propset attempted at /p3@1 - shoule be modify with propset", e.getMessage());
			// easier impl
			assertEquals("Duplicate changeset entries recorded for /p3", e.getMessage());
		}
	}

	@Test
	public void testAddDuplicateCopy() {
		CmsCommitChangeset c = new CmsCommitChangeset();
		RepoRevision r = new RepoRevision(3, new Date(3));
		c.add(new FileDelete(new CmsItemPath("/p1"), r));
		// should be ok because this is an earlier revision,
		// but it is unlikely that we'll need this any time soon
		// TODO c.add(new FileCopy(new CmsItemPath("/p1"), new RepoRevision(2, new Date(2)), new CmsItemPath("/p2")));
		try {
			c.add(new FileCopy(new CmsItemPath("/px"), r, new CmsItemPath("/p1")));
			fail("delete+copy should be allowed only as a move operation");
		} catch (IllegalStateException e) { // likely not a user error so avoiding IllegalArgumentException
			//assertEquals("Delete and copy attempted at /p1@3 - should be a move", e.getMessage());
			// easier impl
			assertEquals("Duplicate changeset entries recorded for /p1", e.getMessage());
		}	
		try {
			c.add(new FileCopy(new CmsItemPath("/p1"), r, new CmsItemPath("/p2")));
			// TODO fail("delete+copy should be allowed only as a move operation");
		} catch (IllegalStateException e) { // likely not a user error so avoiding IllegalArgumentException
			//assertEquals("Delete and copy attempted at /p1@3 - should be a move", e.getMessage());
			// easier impl
			assertEquals("Duplicate changeset entries recorded for /p1", e.getMessage());
		}
	}	
	
	@Test
	public void testLockInfo() {
		CmsCommitChangeset c = new CmsCommitChangeset();
		RepoRevision r = new RepoRevision(3, new Date(3));
		assertFalse(c.isLocksSet());
		CmsItemLock lock1 = mock(CmsItemLock.class);
		c.add(new FileDelete(new CmsItemPath("/p2"), r), lock1);
		assertTrue(c.isLocksSet());
		assertTrue(c.isLockSet(new CmsItemPath("/p2")));
		assertEquals(1, c.getLocks().size());
		CmsItemLock lock2 = mock(CmsItemLock.class);
		c.add(new FileDelete(new CmsItemPath("/p3"), r), lock2);
		assertEquals(2, c.getLocks().size());
		
		assertFalse(c.isKeepLocks());
		c.setKeepLocks(true);
		assertTrue(c.isKeepLocks());
	}
	

}
