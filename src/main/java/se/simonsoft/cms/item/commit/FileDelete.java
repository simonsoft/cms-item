package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

public class FileDelete implements CmsCommitChange {

	private CmsItemPath path;
	private RepoRevision base;

	public FileDelete(CmsItemPath path, RepoRevision base) {
		this.path = path;
		this.base = base;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return base;
	}

}
