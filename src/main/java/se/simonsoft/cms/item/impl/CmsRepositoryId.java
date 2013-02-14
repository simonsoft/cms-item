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
package se.simonsoft.cms.item.impl;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

/**
 * Maybe {@link CmsRepository} should implement {@link CmsItemId} because using {@link CmsItemPath}=null to represent root feels awkward.
 * This is an intermediate solution, pending discussion.
 */
public class CmsRepositoryId implements CmsItemId {

	private CmsRepository repo;

	public CmsRepositoryId(CmsRepository repo) {
		this.repo = repo;
	}
	
	@Override
	public String getUrl() {
		return repo.getUrl();
	}

	@Override
	public String getUrlAtHost() {
		return repo.getUrlAtHost();
	}

	@Override
	public Long getPegRev() {
		return null;
	}

	@Override
	public String getLogicalId() {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public String getLogicalIdFull() {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public CmsItemPath getRelPath() {
		return null;
	}

	@Override
	public CmsRepository getRepository() {
		return repo;
	}

	@Override
	public String getRepositoryUrl() {
		return repo.getUrl();
	}

	@Override
	public CmsItemId withRelPath(CmsItemPath newRelPath) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public CmsItemId withPegRev(Long newPegRev) {
		throw new UnsupportedOperationException("not implemented");
	}

}
