package se.simonsoft.cms.item.info;

import java.util.Date;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Repository-wide read-only services.
 * 
 * TODO evaluate service, collect impl pieces for example YoungestRevision in webapp.
 */
public interface RepositoryLookup {

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
	
}
