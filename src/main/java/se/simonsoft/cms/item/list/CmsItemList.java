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
package se.simonsoft.cms.item.list;

import se.simonsoft.cms.item.CmsItem;

/**
 * Represents a list of items, or at the very least a list of IDs resulting from some query.
 * 
 * What you can get out of the list depends on the service used to retrieve it
 * and the parameters given to the call.
 */
public interface CmsItemList extends Iterable<CmsItem> {

	public static final int SIZE_UNKNOWN = -1;
	
	
	/**
	 * Based on the query that produced the list.
	 * @return the total number of items matching the iteration criteria
	 */	
	public long sizeFound();
	
	/*
	public long sizePage() {
		return backingList.size(); // TODO consider adding to interface
	}
	*/
	
	/**
	 * @return {@link CmsItemListMetaMap}
	 */
	public CmsItemListMetaMap getMeta();

	/**
	 * @param known key
	 * @return convenience access to predefined meta field value.toString
	 */
	public String getMeta(CmsItemListMetaMap.Key known);
	
	
}
