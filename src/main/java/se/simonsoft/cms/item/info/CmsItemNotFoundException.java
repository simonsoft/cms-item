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
package se.simonsoft.cms.item.info;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;


/**
 * Thrown when communication succeeds but the item is not found.
 * 
 * Declared as a checked exception although this is not really recoverable,
 * because the CMS is a multi-user environment where this type of error is normal
 * due to the time gap between user input and operation execution.
 * 
 * Services that rely on indexed data might want to retry a few times before
 * throwing this error as it may occur because of indexing delays.
 */
@SuppressWarnings("deprecation") // for backwards compatibility
public class CmsItemNotFoundException extends se.simonsoft.cms.item.CmsItemNotFoundException {

	private static final long serialVersionUID = 1L;
	
	private CmsRepository repository;
	private CmsItemPath path;
	private Object revision;

	public CmsItemNotFoundException(CmsItemId id) {
		this(id.getRepository(), id.getRelPath(), id.getPegRev());
	}
	
	public CmsItemNotFoundException(CmsRepository repository, CmsItemPath atPath) {
		this(repository, atPath, null);
	}

	/**
	 * @param repository the repository that was accessed
	 * @param atPath the path that was tried
	 * @param atRevision the revision at which the path was tried, toString should produce user readable representation
	 */
	public CmsItemNotFoundException(CmsRepository repository, CmsItemPath atPath, Object atRevision) {
		super(null, "Not found: " + repository + atPath + (atRevision == null ? "" : "@" + atRevision));
		this.repository = repository;
		this.path = atPath;
		this.revision = atRevision;
	}
	
	public CmsRepository getRepository() {
		return repository;
	}

	public CmsItemPath getPath() {
		return path;
	}
	
	/**
	 * @return The revision at which access was attempted, null if HEAD
	 */
	public Object getRevision() {
		return revision;
	}
	
}
