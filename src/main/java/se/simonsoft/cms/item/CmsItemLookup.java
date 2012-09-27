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
package se.simonsoft.cms.item;

/**
 * Provides access to CMS contents using repository, indexing, working copy, caching or a combination.
 * 
 * Implementations can be either cross-repository, requiring paths/URLs/logicalIds with repo specified,
 * or single-repo throwing exceptions if repo is specified and not identical to the connected repo.
 * 
 * @deprecated use {@link se.simonsoft.cms.item.info.CmsItemLookup}
 */
public interface CmsItemLookup {

	/**
	 * Loads item based on id.
	 * Returned instance may be "online", i.e. load data as requested.
	 */
	CmsItem getItem(CmsItemId id) throws CmsConnectionException, CmsItemNotFoundException;
	
}
