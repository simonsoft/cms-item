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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import se.simonsoft.cms.item.info.CmsLockQuery;

public abstract class CmsItemLockCollection implements Serializable, Iterable<CmsItemLock> {

	private static final long serialVersionUID = 1L;
	
	private CmsRepository repository;
	private Map<CmsItemPath, CmsItemLock> map = new LinkedHashMap<CmsItemPath, CmsItemLock>();
	
	/**
	 * @param repository non-null to validate that all locks are for the same repository
	 */
	public CmsItemLockCollection(CmsRepository repository) {
		this.repository = repository;
	}
	
	public CmsItemLockCollection(CmsItemLock first, CmsItemLock... more) {
		this(first.getItemId().getRepository());
		add(first);
		for (CmsItemLock l : more) {
			add(l);
		}
	}


	protected void add(CmsItemLock itemLock) {
		
		if (repository != null && !repository.equals(itemLock.getItemId().getRepository())) {
			throw new IllegalArgumentException("requested itemId does not match CmsRepository");
		}
		
		map.put(itemLock.getItemId().getRelPath(), itemLock);
	}
	
	protected CmsRepository getRepository() {
		return this.repository;
	}
	
	/** Searches the lock collection for a lock on the object. 
	 * A revision on the item id is disregarded, i.e. returns locks even for historical objects.
	 * @param itemId
	 * @return The lock or null if no lock is known.
	 */
	public CmsItemLock getLocked(CmsItemId itemId) {
		
		if (repository != null && !repository.equals(itemId.getRepository())) {
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

	/**
	 * Provides return value for lock operation with single path.
	 * @return null if no lock, the lock if there's exactly one
	 * @throws IllegalArgumentException if there is more than one lock, which should never happen if locking was done with a single path
	 */
	public CmsItemLock getSingle() {
		if (map.size() > 1) {
			throw new IllegalStateException("Expected single lock but had " + map.size());
		}
		if (map.size() == 0) {
			return null;
		}
		return map.values().iterator().next();
	}

	/**
	 * {@link Collection#contains(Object)}
	 * @param lock
	 * @return
	 */
	public boolean contains(CmsItemLock lock) {
		return map.containsValue(lock);
	}
	
	public boolean containsPath(CmsItemPath path) {
		return map.containsKey(path);
	}
	
	/**
	 * {@link Collection#size()}.
	 * @return number of locks
	 */
	public int size() {
		return map.size();
	}
	
	/**
	 * {@link Collection#isEmpty()}.
	 * @return
	 */
	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public Iterator<CmsItemLock> iterator() {
		return map.values().iterator();
	}
	
	public Map<String, String> getPathTokens() {
		Map<String, String> pt = new LinkedHashMap<String, String>();
		for (CmsItemPath path : map.keySet()) {
			pt.put(path.getPath(), map.get(path).getToken());
		}
		return pt;
	}
	
}
