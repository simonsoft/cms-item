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

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Item modifications, never two of them at the same path.
 */
public class CmsCommitChangeset extends LinkedList<CmsCommitChange>
		implements List<CmsCommitChange> {
	
	private static final long serialVersionUID = 1L;

	private String historyMessage = null;
	private boolean keepLocks = false;
	private Map<String, String> locks = new HashMap<String, String>();
	
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

	public Map<String, String> getLocks() {
		return locks;
	}

	public void setLocks(Map<CmsItemPath, String> locks) {
		locks.clear();
		for (CmsItemPath p : locks.keySet()) {
			addLock(p, locks.get(p));
		}
	}
	
	public void addLock(CmsItemPath path, String lockToken) {
		locks.put(path.getPath(), lockToken);
	}

}
