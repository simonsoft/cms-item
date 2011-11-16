package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Used to mark intent when calling this low level API,
 * for future refactoring and addition of rules.
 * Identical to {@link CmsAreaPattern}.
 */
public class CmsReleasesPattern extends CmsAreaPattern {

	public CmsReleasesPattern(String configValue)
			throws IllegalArgumentException {
		super(configValue);
	}

	/**
	 * See {@link CmsAreaPattern#getPathInside(CmsItemPath, String)} but
	 * note that this can <em>not</em> be used to predict the release path
	 * because profiling might affect the destination file name.
	 * 
	 * Currently the support for transforming with profile info is added in
	 * adapter code.
	 */
	@Override
	public CmsItemPath getPathInside(CmsItemPath master, String destinationLabel) {
		return super.getPathInside(master, destinationLabel);
	}

}
