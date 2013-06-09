package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Wraps an actual change with an instruction that there should be
 * an existing folder at the target path, which should be replaced.
 */
public class FolderReplace implements CmsCommitChange {

	private CmsCommitChange replacement;

	protected FolderReplace(CmsCommitChange replacement) {
		this.replacement = replacement;
	}
	
	public FolderReplace(FolderAdd add) {
		this((CmsCommitChange) add);
	}
	
	//public FolderReplace(FolderCopy add) {
	//	this((CmsCommitChange) add);
	//}
	
	@Override
	public CmsItemPath getPath() {
		return replacement.getPath();
	}

	@Override
	public RepoRevision getBaseRevision() {
		return replacement.getBaseRevision();
	}

}
