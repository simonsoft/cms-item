package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

public class FolderAdd extends CmsCommitChangeBase {

	private CmsItemPath path;
	private RepoRevision baseRevision;

	public FolderAdd(CmsItemPath path, RepoRevision baseRevisionForParent) {
		this.path = path;
		this.baseRevision = baseRevisionForParent;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return baseRevision;
	}

	@Override
	protected StatContent getStatContents() {
		return StatContent.A;
	}

	@Override
	protected StatProps getStatProps() {
		return StatProps._;
	}

	@Override
	protected boolean isCopy() {
		return false;
	}

}
