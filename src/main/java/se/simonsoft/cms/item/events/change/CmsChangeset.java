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
package se.simonsoft.cms.item.events.change;

import java.util.List;
import java.util.Map;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Backend neutral representation of all changes in a commit or transaction.
 */
public interface CmsChangeset {

	RepoRevision getRevision();
	
	/**
	 * Supports the most common use case - services listening for changes to a specific item.
	 * @param item
	 * @return
	 */
	boolean affects(CmsItemId item);
	
	/**
	 * Services that don't analyze changes may not return
	 * for instance items added as children of a copied folder,
	 * and may report replacements as delete followed by add.
	 * 
	 * Only derived mode is required to behave similar in all impls.
	 * 
	 * @return true if the service derives all changes (even if there are none),
	 *  false if it returns only those reported by the backend
	 */
	boolean isDeriveEnabled();
	
	/**
	 * 
	 * @return true if the changeset is aware of a later HEAD revision
	 */
	//boolean isHeadKnown();
	
	/**
	 * @return all changes, explicit and derived
	 */
	List<CmsChangesetItem> getItems();
	
	/**
	 * TODO This is a draft/suggestion. Requires replace operations to be returned
	 * as a single change instead of add+delete.
	 * 
	 * @return all changes indexed on path
	 */
	Map<CmsItemPath, CmsChangesetItem> getItemsPath();
	
}
