package se.simonsoft.cms.item.info;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;

/**
 * Ideas for locking service, draft interface.
 * 
 * TODO Evaluate lock information directly from CmsItemLookup
 */
public interface CmsLocking {

	LockToken lock(CmsItemId item, String message);
	
	void unlock(CmsItemId item, LockToken lockToken);
	
	/**
	 * @deprecated probably directly in {@link CmsItem}, useful anyway?
	 */
	boolean isLocked(CmsItemId item);
	
	/**
	 * @deprecated probably directly in {@link CmsItem}
	 */
	LockInfo getLock(CmsItemId item);
	
	/**
	 * Implementation may cache granted locks <em>per user</em> like a working copy
	 * in memory or even on disk, for later return if queried by the same user.
	 * 
	 * @param item
	 * @return null if token is not known or item is not locked
	 */
	LockToken getKnownLock(CmsItemId item);
	
	LockToken lockSteal(CmsItemId item, String message);
	
	LockToken lockOrSteal(CmsItemId item, String message);
	
}
