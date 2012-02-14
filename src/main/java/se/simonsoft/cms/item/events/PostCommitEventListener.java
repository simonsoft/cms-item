package se.simonsoft.cms.item.events;

import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;

public interface PostCommitEventListener {

	public void onPostCommit(CmsRepository repository, RepoRevision revision);
	
}
