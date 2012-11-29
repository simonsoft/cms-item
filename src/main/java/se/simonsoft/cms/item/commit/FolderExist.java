package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Create folder and parent folders only if they didn't exist already.
 */
public class FolderExist implements CmsCommitChange {

	private CmsItemPath path;

	public FolderExist(CmsItemPath path) {
		this.path = path;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	/**
	 * Could be an interface method unless we want the specific class for chaining.
	 * @param properties property change to do on items that are created, but not existing items
	 * @return this instance for chaining
	 */
	public CmsCommitChange setPropertyChange(CmsItemProperties properties) {
		throw new UnsupportedOperationException("not implemented"); // should we have an onCreate callback, and what sync issues would that introduce?
	}
	
	/**
	 * @return null, this change does by design check current state and thus can't use a base revision 
	 */
	@Override
	public RepoRevision getBaseRevision() {
		return null;
	}
	
}
