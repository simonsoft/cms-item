package se.simonsoft.cms.version;

/**
 * Identifies component version.
 * Top level projects should display the value of {@link #getBuildTag()},
 * tag will always return a value, even if version not {@link #isKnown()}. 
 */
public interface CmsComponentVersion {

	public static final String DEFAULT_NAME = "dev";
	
	/**
	 * @return true if the component version is known
	 */
	boolean isKnown();
	
	/**
	 * @return name of the build project, useful to identify branches,
	 *  '{@value #DEFAULT_NAME}' if not an official build
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
	 * @return Build server's tag, null if not a build server package
	 */
	String getBuildTag();
	
	/**
	 * @return display label with name, revision and build number,
	 * excluding any internal info like URLs and build server identification
	 */
	String toString();
	
}
