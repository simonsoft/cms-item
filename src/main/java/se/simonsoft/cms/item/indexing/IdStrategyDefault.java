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
package se.simonsoft.cms.item.indexing;

import javax.inject.Singleton;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

@Singleton
public class IdStrategyDefault implements IdStrategy {

	/**
	 * @return defaults to empty string because paths have no trailing slash
	 */
	protected String getRootPath() {
		return "";
	}
	
	/**
	 * @return what's inbetween the idhead and the revision identifier
	 */
	protected String getPegSeparator() {
		return "@";
	}
	
	protected String getInfoSeparator() {
		return "#";
	}
	
	/**
	 * @param itemId
	 * @return full revision from itemId, which currently has only the number
	 */
	protected RepoRevision getRevision(CmsItemId itemId) {
		if (itemId.getPegRev() == null) {
			throw new IllegalArgumentException("Item must have revision to be valid as ID");
		}
		return new RepoRevision(itemId.getPegRev(), null);
	}
	
	@Override
	public long getRevisionMax() {
		return 9999999999L;
	}
	
	/**
	 * @param revision
	 * @return how to represent a revision in id
	 */
	@Override
	public String getIdRevision(RepoRevision revision) {
		return String.format("%010d", revision.getNumber());
	}
	
	protected String getIdHost(CmsRepository repository) {
		return repository.getHost();
	}
	
	@Override
	public String getIdRepository(CmsRepository repository) {
		return getIdHost(repository) + repository.getUrlAtHost();
	}
	
	@Override
	public String getId(CmsItemId itemId, RepoRevision revision) {
		if (revision == null) {
			throw new IllegalArgumentException("Use getIdHead for id without revision");
		}
		if (itemId.getPegRev() != null && !revision.equals(new RepoRevision(itemId.getPegRev(), null))) {
			throw new IllegalArgumentException("Provided revision " + revision + " does not match id "+ itemId);
		}
		return getIdHead(itemId) + getPegSeparator() + getIdRevision(revision); 
	}
	
	@Override
	public String getIdHead(CmsItemId itemId) {
		return itemId.getRepository().getHost() + itemId.getUrlAtHost();
	}
	
	@Override
	public String getId(CmsRepository repository, RepoRevision revision, CmsItemPath path) {
		return getId(repository.getItemId().withRelPath(path), revision); 
	}

	@Override
	public String getIdHead(CmsRepository repository, CmsItemPath path) {
		return getIdHead(repository.getItemId().withRelPath(path));
	}
	
	@Override
	public String getIdCommit(CmsRepository repository, RepoRevision revision) {
		return getIdEntry(repository, getIdRevision(revision));
	}

	@Override
	public String getIdEntry(CmsRepository repository, String repositoryField) {
		return getIdRepository(repository) + getInfoSeparator() + repositoryField;
	}	
	
}
