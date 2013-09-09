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
package se.simonsoft.cms.item.events.change;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Represents a file or folder affected in a changeset.
 */
public interface CmsChangesetItem extends CmsChangesetItemFlags {

	/**
	 * @return true if the item change was reported explicitly by the CMS backend
	 */
	boolean isExplicit();
	
	/**
	 * @return true if the item change was derived, for example as unchanged content of a copied folder
	 */
	boolean isDerived();
	
	/**
	 * If the changeset is loaded with knowledge of later revisions this method
	 * can tell if the item will for sure be reported as modified or deleted in a later revision.
	 * @return true if the item will be modified or deleted in a later known revision
	 */
	boolean isOverwritten();
	
	CmsItemPath getPath();
	
	/**
	 * @return 0 for folders, size in bytes for files
	 * @throws IllegalArgumentException if the item is a folder
	 */
	long getFilesize();
	
	/**
	 * Note that this revision survives copy/move 
	 * (when there is a {@link #getCopyFromPath()}) unchanged,
	 * unless there are actually modifications to content or properties.
	 * At this revision the item might not have existed at its current path.
	 * 
	 * To get the "path" revision, use {@link CmsChangeset#getRevision()}.
	 * 
	 * Note also for indexing purposes that other attributes that are actually
	 * from the commit, such as last author, will change at move.
	 * 
	 * @return the <em>commit</em> revision,
	 * 	i.e. different than current revision if neither content nor properties changed
	 */
	RepoRevision getRevision();
	
	/**
	 * @return "copy from" data, including derived, if {@link #isCopy()}, null otherwise
	 */
	CmsItemPath getCopyFromPath();
	
	/**
	 * @return the revision if {@link #isCopy()}, null otherwise
	 */
	RepoRevision getCopyFromRevision();
	
	/**
	 * Can probably be implemented using svnlook history.
	 * 
	 * A changeset list at the revision for this previous change
	 * would include the same information.
	 * 
	 * TODO specify how this handles move:
	 *  - if current operation is a move indexing should mark previous commit as non-head
	 *  - if previous operation was a move indexing should mark that as non-head, although commit revision never changed
	 * 
	 * @return Previous change for this item, including derived.
	 */
	@Deprecated // returning a revision is less of a distraction for the indexing case
	CmsChangesetItem getPreviousChange();
	
	/**
	 * TODO API design depends on if we handle atomic moves or not
	 * Finds previous revision when the same path was affected by a a commit, including copies (explicit and derived) that did not affect contents or properties.
	 * @return the revision at the same path that 
	 */
	RepoRevision getRevisionObsoleted();
	
}
