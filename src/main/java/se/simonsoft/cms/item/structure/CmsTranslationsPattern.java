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

	/**
	 * @return The graphics translation path that corresponds to this config.
	 */
	public CmsTranslationsPattern getGraphicsPattern() {
		if (this.isAreaRelative()) {
			throw new UnsupportedOperationException("The pattern '" + this
					+ "' is already relative and can't be converted to a graphics translation pattern");
		}
		return new CmsTranslationsPattern(getAreaName());
	}

}
