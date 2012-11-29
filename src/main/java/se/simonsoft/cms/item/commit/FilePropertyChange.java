package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Propset on an item with no other changes, see also for example {@link FileModification#getProp}.
 * 
 * Svnkit SVNEditor differentiates between file property change and folder property change.
 */
public class FilePropertyChange implements CmsCommitChange {

	private CmsItemPath path;
	private RepoRevision base;
	private CmsItemProperties properties;

	/**
	 * @param path
	 * @param base properties are overwritten rather than diffed, for now at least, so this is not very important
	 * @param properties property changes as returned by {@link CmsItemProperties#getKeySet()} to be executed on the item, while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 */
	public FilePropertyChange(CmsItemPath path, RepoRevision base, CmsItemProperties properties) {
		this.path = path;
		this.base = base;
		this.properties = properties;
	}
	
	/**
	 * @return property changes as returned by {@link CmsItemProperties#getKeySet()} to be executed on the item, while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 */
	public CmsItemProperties getPropertyChange() {
		return properties;
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
