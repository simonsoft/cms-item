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
package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

public class CmsItemLockedException extends RuntimeException {

	private static final long serialVersionUID = 2L;

	private CmsRepository repository;
	private CmsItemPath path;
	private CmsItemLock lock;
	
	public CmsItemLockedException(CmsRepository repository, CmsItemPath path) {
		super("Item is already locked at path " + path);
		this.repository = repository;
		this.path = path;
	}
	
	public CmsItemLockedException(CmsRepository repository, CmsItemPath path, CmsItemLock lock) {
		super("Item is already locked by user " + lock.getOwner() + " at path " + path);
		this.repository = repository;
		this.path = path;
		this.lock = lock;
	}
	
	public CmsRepository getRepository() {
		return repository;
	}
	
	public CmsItemPath getPath() {
		return path;
	}
	
	public CmsItemLock getLock() {
		return lock;
	}
	
}
