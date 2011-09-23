package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Low level path manipulation within a repository.
 * Deals with CmsItemPath instead of CmsItemId becuase
 * the Id interface has no manipulation methods and
 * the caller might want to handle revision numbers etc.
 */
public class CmsPathTransform {

	private RepoStructureConfig config;

	/**
	 * For old repositories that don't have a release path configured yet.
	 * @param repoConfigTranslationPath
	 */
	public CmsPathTransform(String repoConfigTranslationPath) {
		this(new Config(repoConfigTranslationPath, null));
	}

	/**
	 * Helper constructor to create instance with basic config.
	 * @param repoConfigTranslationPath
	 * @param repoConfigReleasePath
	 */
	public CmsPathTransform(String repoConfigTranslationPath, String repoConfigReleasePath) {
		this(new Config(repoConfigTranslationPath, repoConfigReleasePath));
	}
	
	/**
	 * Configures path transform.
	 * @param structureConfig
	 */
	public CmsPathTransform(RepoStructureConfig structureConfig) {
		this.config = structureConfig;
	}
	
	/**
	 * Validates that the path of this browse item is within an area that is considered
	 * a translation area given the current configuration. 
	 */
	public boolean isTranslation(CmsItemPath path) {
		return isSlave(path, config.getTranslationPath());
	}
	
	/**
	 * Derives path where translation should be stored. 
	 * Supports translation of translation. 
	 * @param path master or translation
	 * @param locale Locale code
	 * @return The translation path
	 */
	public CmsItemPath getTranslationPath(CmsItemPath path, String locale) {
		if (isSlave(path, config.getTranslationPath())) {
			throw new IllegalArgumentException("Translation from translation not supported. Got " + path);
		}
		return getSlavePath(path, config.getTranslationPath(), locale);
	}
	
	/**
	 * Reads the locale identifier from a path given current config.
	 * In effect validating that the path is a valid translation.
	 * @param path translation
	 * @return language code according to path
	 * @throws IllegalArgumentException if the path not {@link #isTranslation(CmsItemPath)}
	 */
	public String getTranslationLocale(CmsItemPath path) {
		if (!isTranslation(path)) {
			throw new IllegalArgumentException("Path is not a translation: " + path);
		}
		return path.getName(config.getTranslationPath().getAreaPathSegmentIndex() + 1);
	}
	
	private boolean isSlave(CmsItemPath path, CmsAreaPlacement conf) {
		int ai = conf.getAreaPathSegmentIndex();
		return ai + 1 < path.getPathSegments().size() // must have one additional segment after area
				&& path.getName(ai).equals(conf.getAreaName());
	}
	
	// reuse the same code for translations and releases
	private CmsItemPath getSlavePath(CmsItemPath master, CmsAreaPlacement conf, String insertName) {
		StringBuffer b = new StringBuffer();
		int n = conf.getAreaPathSegmentIndex();
		int i = 0;
		for (String s : master.getPathSegments()) {
			if (++i == n) {
				b.append('/').append(conf.getAreaName()).append('/').append(insertName);
			}
			b.append('/').append(s);
		}
		return new CmsItemPath(b.toString());
	}
	
	/**
	 * Class from primitive constructor args.
	 */
	private static class Config implements RepoStructureConfig {

		private CmsAreaPlacement translationPath;
		private CmsAreaPlacement releasePath;

		Config(String repoConfigTranslationPath, String repoConfigReleasePath) {
			this.translationPath = new CmsAreaPlacement(repoConfigTranslationPath);
			if (repoConfigReleasePath != null) {
				this.releasePath = new CmsAreaPlacement(repoConfigReleasePath);
			}
		}
		
		@Override
		public CmsAreaPlacement getTranslationPath() {
			return this.translationPath;
		}

		@Override
		public CmsAreaPlacement getReleasePath() {
			return this.releasePath;
		}
		
	}
	
}
