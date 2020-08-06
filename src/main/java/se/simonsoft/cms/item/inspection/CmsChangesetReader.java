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
package se.simonsoft.cms.item.inspection;

import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.events.change.CmsChangeset;
import se.simonsoft.cms.item.events.change.CmsChangesetItem;

/**
 * 
 * #919 this service is now per-repository instead, avoiding the need for passing around inspection and allowing different impls per repository
 */
public interface CmsChangesetReader {

	/**
	 * @param revision The changeset to produce
	 * @return changeset at the revision, isOverwritten always set to false
	 */
	CmsChangeset read(RepoRevision revision);
	
	/**
	 * Useful when batch processing revisions to set a later revision, typically current HEAD,
	 * to compare to in order to provide {@link CmsChangesetItem#isOverwritten()} information.
	 * @param revision The changeset to produce
	 * @param referenceRevision Larger than or equal to revision, normally identical in subsequent calls in the same batch
	 * @return changeset at the revision, with isOverwritten set to true if referenceRevision contains a newer version of same item
	 */
	CmsChangeset read(RepoRevision revision, RepoRevision referenceRevision);
	
}
