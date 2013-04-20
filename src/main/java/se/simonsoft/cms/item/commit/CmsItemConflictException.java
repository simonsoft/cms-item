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
package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Thrown when a committed item's revision in repository does not match the "base" revision.
 * 
 * Indicates modification by someone else since the operation started
 * (i.e. since the base revision was fetched).
 *
 * Error message at commit is something like:
 * svn: E160024: resource out of date; try updating
 * svn: E175002: CHECKOUT of '/svn/demo1/!svn/ver/157/my/file.txt': 409 Conflict (http://host)
 */
public class CmsItemConflictException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private CmsRepository repository;
	private CmsItemPath path;
	private RepoRevision base;

	public CmsItemConflictException(CmsRepository repository, CmsItemPath path, RepoRevision base) {
		super("Detected conflict at '" + path + "'. Item has changed since base reivision " + base.getNumber());
		this.repository = repository;
		this.path = path;
		this.base = base;
	}

	public CmsRepository getRepository() {
		return repository;
	}

	public CmsItemPath getPath() {
		return path;
	}

	public RepoRevision getBase() {
		return base;
	}	
	
}
