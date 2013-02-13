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
package se.simonsoft.cms.item.commit;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;

/**
 * Item modifications, never two of them at the same path.
 */
public class CmsCommitChangeset extends LinkedList<CmsCommitChange>
		implements List<CmsCommitChange> {
	
	private static final long serialVersionUID = 1L;

	private String historyMessage = null;
	private boolean keepLocks = false;
	private Map<CmsItemPath, CmsCommitChange> map = new HashMap<CmsItemPath, CmsCommitChange>(); // to simplify validation, may waste a bit of memory but probably negligible
	private Map<String, String> locks = new HashMap<String, String>();
	
	@Override
	public boolean add(CmsCommitChange change) {
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
	 * use {@link #add(CmsCommitChange)} follwed by multiple {@link #addLock(CmsItemPath, CmsItemLock)}.
	 * 
	 * @param change like in {@link #add(CmsCommitChange)}
	 * @param lock Lock matching the item's current lock, lock ID being the token needed by backend
	 */
	public void add(CmsCommitChange change, CmsItemLock lock) {
		add(change);
		addLock(change.getPath(), lock);
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
		return getLocks().containsKey(cmsItemPath.getPath());
	}
	
	/**
	 * @return paths mapped to lock tokens, for backend operations
	 */
	public Map<String, String> getLocks() {
		return locks;
	}
	
	/**
	 * Get all locks under a folder
	 * @param ancestor
	 * @return
	 */
	//public Map<String, CmsItemLock> getLocks(CmsItemPath ancestor) {
	//	return locks;
	//}

	public void setLocks(Map<CmsItemPath, CmsItemLock> locks) {
		locks.clear();
		for (CmsItemPath p : locks.keySet()) {
			addLock(p, locks.get(p));
		}
	}
	
	public void addLock(CmsItemPath path, CmsItemLock lock) {
		locks.put(path.getPath(), lock.getID());
	}

}
