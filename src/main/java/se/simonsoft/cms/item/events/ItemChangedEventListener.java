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
package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.events.change.CmsChangesetItem;

/**
 * Gets notified when a CmsItem has changed in the CMS (e.g. post-commit).
 * 
 * These events happen after {@link ChangesetEventListener}, potentially after other
 * services have completed processing the commit (e.g. Indexing have completed).
 * This aspect is implementation dependent and there might even be multiple stages.
 * 
 */
public interface ItemChangedEventListener {

	
	/**
	 * Tentative: Would like just a CmsItem as parameter. 
	 * This might not contain change-related information like {@link CmsChangesetItem}.
	 * Preferred solution: CmsChangesetItem interface should extend CmsItem interface.
	 * (potentially 'CmsItemChanged' should extend CmsItem with CmsChangesetItemFlags)
	 * 
	 * The item might not support {@link CmsItem#getContents(java.io.OutputStream)}, implementation dependent.
	 * 
	 * @param changed item
	 */
	void onItemChange(CmsItem item);
	
}
