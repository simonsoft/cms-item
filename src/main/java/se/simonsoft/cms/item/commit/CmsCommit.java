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
package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemLockCollection;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Operations that modify the repository in atomic changesets
 * (and transient locks).
 * 
 * The API is extended by adding new {@link CmsPatchItem} implementations,
 * which backends can then add support for when needed.
 * 
 * Backend specific operations can be impl'd in the respective module.
 * 
 * Implementers: Please note the restrictions documented in {@link CmsPatchset} and {@link CmsPatchItem}.
 */
public interface CmsCommit {
	
	public RepoRevision run(CmsPatchset fileModifications) throws CmsItemLockedException;
	
	// Possible ways for backends to declare their capabilities, if we need such a thing
	//public boolean supports(Class<CmsPatchItem> modificationType);
	//public Set<Class<CmsPatchItem>> getCapabilities();
	
	/**
	 * Locks an item.
	 * Lock expiry date is not supported in the general svn case.
	 * Multiple items not supported because in svn they would have different tokens even if locked in the same operations.
	 * @param message Lock comment
	 * @param base The base revision, i.e. lock only if the items have not been touched since then
	 * @param item Item to lock
	 * @return identical Lock for all items (TODO is this the same token?)
	 * @throws CmsItemLockedException If a path is already locked
	 */
	public CmsItemLockCollection lock(String message, RepoRevision base, CmsItemPath... item) throws CmsItemLockedException;
	
	/**
	 * Release lock on item without making a commit.
	 * Assumes that the caller guards against unintentional lock breaking.
	 * @param item to be unlocked
	 * @param lock with the token for unlock, breaks lock if different user than authenticated
	 */
	public void unlock(CmsItemLock... lock);
	
}
