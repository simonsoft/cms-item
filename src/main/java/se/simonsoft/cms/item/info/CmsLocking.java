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
package se.simonsoft.cms.item.info;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.commit.CmsCommit;

/**
 * Ideas for locking service, draft interface.
 * 
 * TODO Evaluate lock information directly from CmsItemLookup
 * 
 * TODO How to pass own locks to {@link CmsCommit}
 */
public interface CmsLocking {

	LockToken lock(CmsItemId item, String message);
	
	void unlock(CmsItemId item, LockToken lockToken);
	
	/**
	 * @return lock properties or null if the item is not locked
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
	
	/**
	 * Removes existing lock on an item in the repository, regardless of owner.
	 * Use {@link #unlock(CmsItemId, LockToken)} with a token for safe unlock within an operation.
	 * @param item An item that is locked (or any item?)
	 */
	void lockBreak(CmsItemId item);
	
	/**
	 * Same as {@link #lock(CmsItemId, String)} but also breaks existing locks by anyone.
	 * @param item
	 * @param message
	 * @return
	 */
	LockToken lockOrSteal(CmsItemId item, String message);
	
}
