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
package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

/**
 * URL-only item id with no support for logical IDs,
 * good for webapp use like Repos "target" arguments.
 * 
 * This impl does not support {@link #getLogicalId()} and {@link #getLogicalIdFull()}. Use {@link CmsItemIdArg} instead.
 * 
 * TODO? deprecate because equivalent to
 * {@link CmsRepository#getItemId()} with {@link CmsItemId#withRelPath(CmsItemPath)} and {@link CmsItemId#withPegRev(Long)}.
 *  - to discourage from direct instantiation except for transfer with {@link CmsItemIdArg}.
 *  
 * @deprecated replaced by {@link CmsItemIdArg} for transfer and {@link CmsRepository#getItemId()} for repo + target
 */
public class CmsItemIdUrl extends CmsItemIdEncoderBase {
	
	private CmsItemPath path;
	private CmsRepository repository;
	private Long pegRev;

	/**
	 * The repository root id.
	 * @param repository
	 */
	public CmsItemIdUrl(CmsRepository repository) {
		this(repository, null, null);
	}	
	
	public CmsItemIdUrl(CmsRepository repository, CmsItemPath itemPath) {
		this(repository, itemPath, null);
	}
	
	public CmsItemIdUrl(CmsRepository repository, CmsItemPath itemPath, Long pegRev) {
		super(repository);
		this.repository = repository;
		this.path = itemPath;
		if (itemPath == CmsItemPath.ROOT) {
			this.path = null; //Root path should be represented with null value in CmsItemId
		}
		this.pegRev = pegRev;
	}

	public CmsItemIdUrl(CmsRepository repository, String targetCmsItemPath) {
		this(repository, new CmsItemPath(targetCmsItemPath));
	}

	@Override
	public String getLogicalIdFull() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Logical ID requested for URL-only item id " + getUrl());
	}
	
	@Override
	public String getLogicalId() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Logical ID requested for URL-only item id " + getUrl());
	}

	@Override
	public CmsItemPath getRelPath() {
		return path;
	}
	
	@Override
	public String getUrl() {
		if (path == null) {
			return getRepositoryUrl();
		}
		return getRepositoryUrl() + urlencode(path);
	}

	@Override
	public String getUrlAtHost() {
		return getRepository().getUrlAtHost() + urlencode(path);
	}
	
	public String getUrlAndPeg() {
		return getUrl() + (getPegRev() == null ? "" : "?p=" + getPegRev());
	}
	
	@Override
	public Long getPegRev() {
		return pegRev;
	}

	@Override
	public CmsRepository getRepository() {
		return repository;
	}

	@Override
	public String getRepositoryUrl() {
		return repository.getUrl();
	}

	@Override
	public CmsItemId withRelPath(CmsItemPath newRelPath) {
		return new CmsItemIdUrl(repository, newRelPath);
	}

	@Override
	public CmsItemId withPegRev(Long newPegRev) {
		return new CmsItemIdUrl(repository, path, newPegRev);
	}

	@Override
	public String toString() {
		if (getRelPath() == null) {
			return repository.toString();
		}
		return repository.toString() + getRelPath();
	}
	
}
