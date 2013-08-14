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
package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.events.change.CmsChangeset;
import se.simonsoft.cms.item.events.change.CmsChangesetItem;
import se.simonsoft.cms.item.events.change.CmsChangesetItemFlags;

/**
 * Tentative interface, outlining future event model.
 * 
 * TODO Global, or per repository? Global right?
 * 
 * TODO decide how to handle the difference between pre- and post-notification.
 * 
 * TODO how to filter on repository and path,
 * including items anywhere under a specific folder
 * (see {@link CmsChangeset#affectsIndirectly(CmsItemId)}.
 */
public interface ItemEventListener {

	/**
	 * @return the filter for which events this handler wants to be notified about
	 */
	CmsChangesetItemFlags getItemFilter();
	
	/**
	 * @param revision items only carry commit revision
	 * @param event
	 * @throws ItemChangeReject
	 */
	void onItemChange(RepoRevision revision, CmsChangesetItem event) throws ItemChangeReject;
	
	class ItemChangeReject extends Exception {
		private static final long serialVersionUID = 1L;
	}
	
}
