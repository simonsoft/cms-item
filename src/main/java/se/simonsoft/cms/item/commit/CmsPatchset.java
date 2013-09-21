/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemLockCollection;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Item modifications, never two of them at the same path.
 */
public class CmsPatchset extends LinkedList<CmsPatchItem>
		implements List<CmsPatchItem> {
	
	private static final long serialVersionUID = 1L;

	private static final RepoRevision BASE_OVERWRITE = new RepoRevision(-1, null);
	
	private CmsRepository repository;	
	private RepoRevision base;
	
	private String historyMessage = null;
	private boolean keepLocks = false;
	private Map<CmsItemPath, CmsPatchItem> map = new HashMap<CmsItemPath, CmsPatchItem>(); // to simplify validation, may waste a bit of memory but probably negligible
	private Locks locks = new Locks();
	
	/**
	 * Produces a changeset without base revision - not recommended.
	 *
	 * Can still be safe if all items are CmsPatchItem.SupportsIndividualBase.
	 * 
	 * @param repository Commits apply to a single repository
	 * 
	 * @deprecated With no base revision results from other users may be overwritten unknowingly
	 */
	public CmsPatchset(CmsRepository repository) {
		this(repository, BASE_OVERWRITE);
	}
	
	/**
	 * Changeset with a base revision, enables detection of conflicts and race conditions.
	 * 
	 * Also some implementations need to traverse the structure, and should do so at a base revision,
	 * because someone else might just now have deleted one of the folders that existed when this user invoked the operation.
	 * 
	 * @param repository Commits apply to a single repository
	 * @param baseRevision Used to check for conflicts
	 */
	public CmsPatchset(CmsRepository repository, RepoRevision baseRevision) {
		this.repository = repository;
		if (baseRevision == null) {
			throw new IllegalArgumentException("Base revision is required for commit operations");
		}
		this.base = baseRevision;
	}

	public CmsRepository getRepository() {
		return repository;
	}
	
	/**
	 * Used to check for concurrent modifications,
	 * so that uses don't overwrite each other's changes.
	 * 
	 * Important because all operations assume that they know the current state of the repository,
	 * but that state is always from a certain point in time (or actually a time range unless there is an exact revision number).
	 * 
	 * @return the revision that changes are based on, used to check for conflicts with other changes
	 *  = the revision that the user saw when deciding to do these changes
	 */
	public RepoRevision getBaseRevision() {
		return this.base;
	}
	
	@Override
	public boolean add(CmsPatchItem change) {
		CmsItemPath path = change.getPath();
		if (map.containsKey(path)) {
			throw new IllegalStateException("Duplicate changeset entries recorded for " + path);
		}
		map.put(path, change);
		return super.add(change);
	}
	
	/**
	 * Makes it possible to commit to a locked item,
	 * releasing the lock after commit or keeping all locks if {@link #isKeepLocks()}.
	 * 
	 * For folder move/delete when there are multiple locks under the folder,
	 * use {@link #add(CmsPatchItem)} follwed by multiple {@link #addLock(CmsItemPath, CmsItemLock)}.
	 * 
	 * @param change like in {@link #add(CmsPatchItem)}
	 * @param lock Lock matching the item's current lock, lock ID being the token needed by backend
	 */
	public void add(CmsPatchItem change, CmsItemLock lock) {
		add(change);
		addLock(lock);
	}
	
	public String getHistoryMessage() {
		return historyMessage;
	}

	public void setHistoryMessage(String historyMessage) {
		this.historyMessage = historyMessage;
	}

	public boolean isKeepLocks() {
		return keepLocks;
	}

	public void setKeepLocks(boolean keepLocks) {
		this.keepLocks = keepLocks;
	}

	public boolean isLocksSet() {
		return locks.size() > 0;
	}
	
	public boolean isLockSet(CmsItemPath cmsItemPath) {
		return getLocks().containsPath(cmsItemPath);
	}
	
	/**
	 * @return paths mapped to lock tokens, for backend operations
	 */
	public CmsItemLockCollection getLocks() {
		return locks;
	}
	
	public void addLock(CmsItemLock lock) {
		locks.add(lock);
	}

	private class Locks extends CmsItemLockCollection {
		public Locks() {
			super(null); // TODO require repository for changeset or accept null for lock collection
		}
		private static final long serialVersionUID = 1L;
		public void add(CmsItemLock lock) {
			super.add(lock);
		}
	}
	
}