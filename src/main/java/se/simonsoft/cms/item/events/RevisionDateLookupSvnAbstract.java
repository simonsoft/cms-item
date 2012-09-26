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
package se.simonsoft.cms.item.events;

import java.util.Date;
import java.util.TreeMap;

/**
 * Handles caching and map API based for date lookups.
 */
public abstract class RevisionDateLookupSvnAbstract extends TreeMap<Long, Date>
		implements RevisionDateLookup {

	private static final long serialVersionUID = 1L;

	@Override
	public Date get(Object key) {
		if (!containsKey(key)) {
			if (key instanceof Long) {
				Date got = getDate((Long) key);
				put((Long) key, got);
			}
			throw new IllegalArgumentException();
		}
		return super.get(key);
	}
	
	/**
	 * Looks up date from backend.
	 * @param revisionNumber the revision id
	 * @return corresponding date as stored in backend
	 */
	abstract Date getDate(Long revisionNumber);
	
}
