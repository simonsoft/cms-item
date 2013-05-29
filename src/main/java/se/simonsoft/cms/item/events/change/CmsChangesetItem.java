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
public interface CmsChangesetItem {

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
	 * @return the <em>commit</em> revision, i.e. 
	 */
	RepoRevision getRevision();
	
	boolean isFile();
	
	boolean isFolder();
	
	/**
	 * @return true if the entry is a copy to a new location
	 */
	boolean isCopy();
	
	/**
	 * @return "copy from" data, including derived, if {@link #isCopy()}, null otherwise
	 */
	CmsItemPath getCopyFromPath();
	
	/**
	 * @return the revision if {@link #isCopy()}, null otherwise
	 */
	Long getCopyFromRevision();
	
	/**
	 * @return true if the item was added, excluding replace
	 */
	boolean isAdd();

	/**
	 * @return true if the item was replaced, i.e. not a diff but completely changed
	 */
	boolean isReplace();
	
	/**
	 * @return true if the item was deleted, excluding replace
	 */
	boolean isDelete();
	
	/**
	 * @return true if content was actually modified,
	 *  true for copies if destination is not identical to source
	 */
	boolean isContentModified();
	
	/**
	 * @return true if content was added, copied, replaced, modified or deleted
	 */
	boolean isContent();
	
	/**
	 * @return true if the property set was actually modified,
	 *  true for copies if destination props differ from source
	 */
	boolean isPropertiesModified();
	
	/**
	 * @return true on anything that might mean a different set of properties at the path
	 *  
	 */
	boolean isProperties();
	
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
	CmsChangesetItem getPreviousChange();
	
}
