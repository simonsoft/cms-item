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

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * A modification of contents and/or properties at a {@link CmsItemPath}
 * in a repository given by execution context.
 */
public interface CmsItemPatch {

	CmsItemPath getPath();

	/**
	 * Important because all operations assume that they know the current state of the repository,
	 * but that state is always from a certain point in time (or actually a time range unless there is an exact revision number).
	 * @return the revision that changes are based on, used to check for conflicts with other changes
	 */
	RepoRevision getBaseRevision();
	
	// TODO getProperties(); though not for Delete?
	
	// TODO maybe optional, how to handle the difference between unlock or keep lock at commit?
	//CmsItemLock getLock();
	
}
