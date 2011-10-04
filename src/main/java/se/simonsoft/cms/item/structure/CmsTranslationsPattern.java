package se.simonsoft.cms.item.structure;

/**
 * Used to mark intent when calling this low level API,
 * for future refactoring and addition of rules.
 * Identical to {@link CmsAreaPattern}.
 */
public class CmsTranslationsPattern extends CmsAreaPattern {

	public CmsTranslationsPattern(String configValue)
			throws IllegalArgumentException {
		super(configValue);
	}

}
