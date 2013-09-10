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
package se.simonsoft.cms.item;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import se.simonsoft.cms.item.info.CmsLockQuery;

public abstract class CmsItemLockCollection implements Serializable {


	private static final long serialVersionUID = 1L;
	private CmsRepository repository;
	private Map<CmsItemPath, CmsItemLock> map = new HashMap<CmsItemPath, CmsItemLock>();
	
	
	public CmsItemLockCollection(CmsRepository repository) {
		
		this.repository = repository;
	}
	
	
	protected void add(CmsItemLock itemLock) {
		
		if (!repository.equals(itemLock.getItemId().getRepository())) {
			throw new IllegalArgumentException("requested itemId does not match CmsRepository");
		}
		
		map.put(itemLock.getItemId().getRelPath(), itemLock);
	}
	
	
	/** Searches the lock collection for a lock on the object. 
	 * A revision on the item id is disregarded, i.e. returns locks even for historical objects.
	 * @param itemId
	 * @return The lock or null if no lock is known.
	 */
	public CmsItemLock getLocked(CmsItemId itemId) {
		
		if (!repository.equals(itemId.getRepository())) {
			throw new IllegalArgumentException("requested itemId does not match CmsRepository");
		}

		return map.get(itemId.getRelPath());
	}
	
	
	
	/**
	 * Returns items that are currently locked.
	 * @param query Filtering
	 */
	CmsItemLockCollection getLocked(CmsLockQuery query) {
		throw new UnsupportedOperationException("Lock query filtering not implemented");
	}
	
}
