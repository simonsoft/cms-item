package se.simonsoft.cms.item.structure;

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

}
