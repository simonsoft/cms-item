package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Low level path manipulation within a repository.
 */
public class CmsPathTransform {

	/**
	 * For old repositories that don't have a release path configured yet.
	 * @param repoConfigTranslationPath
	 */
	public CmsPathTransform(String repoConfigTranslationPath) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Helper constructor to create instance with basic config.
	 * @param repoConfigTranslationPath
	 * @param repoConfigReleasePath
	 */
	public CmsPathTransform(String repoConfigTranslationPath, String repoConfigReleasePath) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Configures path transform.
	 * @param structureConfig
	 */
	public CmsPathTransform(RepoStructureConfig structureConfig) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Validates that the path of this browse item is within an area that is considered
	 * a translation area given the current configuration. 
	 */
	public boolean isTranslation(CmsItemPath path) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Derives path where translation should be stored. 
	 * Supports translation of translation. 
	 * @param path master or translation
	 * @param locale Locale code
	 * @return The translation path
	 */
	public CmsItemPath getTranslationPath(CmsItemPath path, String locale) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Reads the locale identifier from a path given current config.
	 * In effect validating that the path is a valid translation.
	 * @param path translation
	 * @return language code according to path
	 * @throws IllegalArgumentException if the path not {@link #isTranslation(CmsItemPath)}
	 */
	public String getTranslationLocale(CmsItemPath path) {
		throw new UnsupportedOperationException();
	}
	
	// can we reuse the same code for translations and releases?
	private CmsItemPath getSlavePath(CmsItemPath master, CmsAreaPlacement config) {
		throw new UnsupportedOperationException();
	}
	
}
