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

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.RepoRevision;

public interface CmsCommit {

	public RepoRevision run(CmsCommitChangeset fileModifications) throws CmsItemLockedException;
	
	/**
	 * API to be decided.
	 * @param message
	 * @param expory null if lock should never expire
	 * @param item
	 * @return
	 * @throws CmsItemLockedException
	 */
	//public CmsItemLock lock(String message, Date expiry, CmsItemId... item) throws CmsItemLockedException;
	
	/**
	 * API to be decided.
	 * @param otherUsersToo
	 * @param item
	 */
	//public void unlock(boolean otherUsersToo, CmsItemId... item);
	
}
