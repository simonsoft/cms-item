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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Arbitrary data about the item list, to make {@link CmsItemList} a useful Data Transfer Object on its own.
 */
public class CmsItemListMetaMap implements Map<String, Object> {
	
	public enum Key {
		/**
		 * Service ID.
		 */
		id,
		/**
		 * Page title, after CMS prefix.
		 */
		title,
		/**
		 * Headline
		 * TODO fallback to title?
		 */
		headline,
		/**
		 * Plaintext description of the data, HTML markup will be escaped
		 */
		description,
		/**
		 * The resource being listed, URL
		 */
		resource,
		/**
		 * The resource peg being listed
		 */
		peg,
		/**
		 * The resource being listed, logical ID
		 */
		resourceId
	}
	
	private Map<String, Object> meta;

	public CmsItemListMetaMap() {
		this(new HashMap<String, Object>());
	}
	
	public CmsItemListMetaMap(Map<String, Object> meta) {
		this.meta = meta;
	}
	
	/**
	 * Helper for implementing {@link CmsItemList#getMeta(CmsItemListMetaMap.Key)}.
	 */
	public String itemListGetMeta(Key known) {
		Object v = get(known.name());
		if (v == null) {
			return null;
		}
		return v.toString();
	}

	/**
	 * @param key toString to {@link #put(String, Object)}
	 * @param value to {@link #put(String, Object)}
	 * @return for chained creation
	 */
	public CmsItemListMetaMap put(Key key, Object value) {
		put(key.toString(), value);
		return this;
	}

	@Override
	public Object get(Object key) {
		if (key instanceof Key) {
			key = key.toString();
		}
		return meta.get(key);
	}	
	
	@Override
	public void clear() {
		meta.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		if (key instanceof Key) {
			key = key.toString();
		}
		return meta.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return meta.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return meta.entrySet();
	}

	@Override
	public boolean isEmpty() {
		return meta.isEmpty();
	}

	@Override
	public Set<String> keySet() {
		return meta.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		return meta.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		meta.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		if (key instanceof Key) {
			key = key.toString();
		}
		return meta.remove(key);
	}

	@Override
	public int size() {
		return meta.size();
	}

	@Override
	public Collection<Object> values() {
		return meta.values();
	}

}
