package se.simonsoft.cms.version;

/**
 * Identifies component version.
 * Top level projects should display the value of {@link #getTag()},
 * tag will always return a value, even if version not {@link #isKnown()}. 
 */
public interface CmsComponentVersion {

	public static final String DEFAULT_TAG = "dev";
	
	/**
	 * @return true if the component version is known
	 */
	boolean isKnown();
	
	/**
	 * @return name of the build project, useful to identify branches,
	 *  null if not an official build
	 */
	String getBuildName();
	
	/**
	 * @return build number within the build project,
	 *  null if not an official build
	 */
	Integer getBuildNumber();
	
	/**
	 * @return revision of the source tree when building,
	 *  null if not an official build
	 */
	Integer getSourceRevision();
	
	/**
	 * @return The preferred display label to uniquely identify version,
	 *  '{@value #DEFAULT_TAG}' if not an official build
	 */
	String getTag();
	
}
