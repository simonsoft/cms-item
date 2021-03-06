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
package se.simonsoft.cms.item.events.change;

import java.util.List;
import java.util.Map;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.inspection.CmsContentsReader;

/**
 * Backend neutral representation of all changes in a commit or transaction.
 * 
 * For revision properties use {@link CmsContentsReader#getRevisionProperties(RepoRevision)}.
 */
public interface CmsChangeset {

	/**
	 * Note to implementer: don't return a {@link se.simonsoft.cms.item.inspection.CmsRepositoryInspection}
	 * @return repository
	 */
	CmsRepository getRepository();
	
	/**
	 * @return the changeset's revision
	 */
	RepoRevision getRevision();
	
	//CmsItemProperties getRevisionProperties(); // unlike changeset items revision properties can be updated after the commit and should be accessed through CmsContentsReader 
	
	
	/**
	 * Services that don't analyze changes may not return
	 * for instance items added as children of a copied folder,
	 * and may report replacements as delete followed by add.
	 * 
	 * Only derived mode is required to behave similar in all impls.
	 * 
	 * @return true if the service derives all changes (even if there are none),
	 *  false if it returns only those reported by the backend,
	 *  true if derive is not applicable with the running backend (i.e. all items are by design included)
	 */
	boolean isDeriveEnabled();
	
	/**
	 * Paths in alphabetical order, regardless of entry kind.
	 * @return all changes, explicit and derived
	 */
	List<CmsChangesetItem> getItems();
	
	/**
	 * In the CMS abstraction of a changeset there's only one change item per path.
	 * 
	 * This is probably a good idea as it simplifies the data model,
	 * and svn on the client side represents replace properly.
	 * 
	 * Note that a move would still be represented as a delete on one path and an add on a different one,
	 * with copy-from pointing to the former.
	 * 
	 * @return all changes indexed on path
	 */
	Map<CmsItemPath, CmsChangesetItem> getItemsPath();
	
	/**
	 * @return true if there are no item changes in this changeset
	 */
	boolean isEmpty();
	
}
