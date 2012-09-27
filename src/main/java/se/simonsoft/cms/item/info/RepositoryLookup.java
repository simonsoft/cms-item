package se.simonsoft.cms.item.info;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Repository-wide read-only services.
 * 
 * TODO evaluate service, see Youngest impl in webapp.
 */
public interface RepositoryLookup {

	RepoRevision getYoungest(CmsRepository repository);
	
}
