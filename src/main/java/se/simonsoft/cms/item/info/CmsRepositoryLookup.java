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

import java.util.Date;

import se.simonsoft.cms.item.CmsItemLockCollection;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.config.CmsConfigRepositoryPath;

/**
 * Repository-wide read-only services.
 * 
 * TODO evaluate service, collect impl pieces for example YoungestRevision in webapp.
 */
public interface CmsRepositoryLookup {

	/**
	 * @param repository the central repository to check
	 * @return the current highest revision (i.e. HEAD)
	 * @throws CmsConnectionException if connection to repository failed
	 * @throws CmsItemNotFoundException if repository was not found 
	 */
	RepoRevision getYoungest(CmsRepository repository) throws CmsConnectionException, CmsItemNotFoundException;;
	
	/**
	 * @param repository the central repository to check
	 * @param nativeRevisionId For Subversion a Long or a String parseable as a long
	 * @return the exact timestamp (UTC milliseconds of course) when the revision was made
	 * @throws IllegalArgumentException if the revision type does not match the backend
	 * @throws CmsConnectionException if connection to repository failed
	 * @throws CmsItemNotFoundException if repository was not found
	 * @see RepoRevision#getDate()
	 */
	Date getRevisionTimestamp(CmsRepository repository, Object nativeRevisionId) 
			throws IllegalArgumentException, CmsConnectionException, CmsItemNotFoundException;
	
	
	/**
	 * Returns items that are currently locked in the repository.
	 * @param repository the repository to check
	 */
	CmsItemLockCollection getLocked(CmsRepository repository);
	
	
	/**
	 * Returns the config for a specific path in the repository.
	 * (Typically taking inherited properties into account)
	 * @param repository
	 * @param path
	 * @return config for the path in the repository
	 */
	/*
	CmsRepositoryPathConfig getConfig(CmsRepository repository, CmsItemPath path);
	*/
}
