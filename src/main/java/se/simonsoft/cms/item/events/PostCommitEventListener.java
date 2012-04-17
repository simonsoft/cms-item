package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Gets notified when a commit is done to the CMS.
 * 
 * Only privileged services can use this to access the repository,
 * because there is no authentication available.
 * 
 * Any service might be interested in the event, for caching etc,
 * because between these events nothing changes in the repositories
 * except locks and exceptionally revprops.
 * 
 * Listeners are registered by binding to a set of this interface
 * using the multibinder feature.
 * Hooks are executed in the order they are bound.
 */
public interface PostCommitEventListener {

	public void onPostCommit(CmsRepository repository, RepoRevision revision);
	
}
