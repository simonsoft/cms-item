package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;

public abstract class CmsCommitChangeBase implements CmsCommitChange {

	/**
	 * Sorting on path.
	 */
	@Override
	public int compareTo(CmsCommitChange item) {
		if (item == null) {
			return compareTo((CmsItemPath) null);
		}
		return compareTo(getPath());
	}

	@Override
	public int compareTo(CmsItemPath path) {
		if (path == null) {
			return getPath() == null ? 0 : getPath().compareTo(null);
		}
		return getPath().compareTo(path);
	}

}
