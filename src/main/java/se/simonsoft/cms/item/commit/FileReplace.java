package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Wraps an actual change with an instruction that there should be
 * an existing file at the target path, which should be replaced.
 */
public class FileReplace implements CmsCommitChange {

	private CmsCommitChange replacement;

	protected FileReplace(CmsCommitChange replacement) {
		this.replacement = replacement;
	}
	
	public FileReplace(FileAdd add) {
		this((CmsCommitChange) add);
	}
	
	public FileReplace(FileCopy add) {
		this((CmsCommitChange) add);
	}
	
	@Override
	public CmsItemPath getPath() {
		return replacement.getPath();
	}

	@Override
	public RepoRevision getBaseRevision() {
		return replacement.getBaseRevision();
	}

}
