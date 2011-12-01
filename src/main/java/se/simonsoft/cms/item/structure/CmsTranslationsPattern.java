package se.simonsoft.cms.item.structure;

/**
 * Used to mark intent when calling this low level API,
 * for future refactoring and addition of rules.
 * Identical to {@link CmsAreaPattern}.
 */
public class CmsTranslationsPattern extends CmsAreaPattern {

	/**
	 * Translation pattern to use if not configured in the current repository.
	 */
	public static final String DEFAULT_TRANSLATIONS_PATTERN = "/*/lang";
	
	public CmsTranslationsPattern(String configValue)
			throws IllegalArgumentException {
		super(configValue);
	}

}
