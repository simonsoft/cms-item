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
	 * @return the 'Implementation-Version', i.e. version from POM
	 */
	String getVersion();
	
	/**
	 * @return true if version (or lack thereof) indicates we're not running a release build
	 */
	boolean isSnapshot();
	
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
	 * Constructs different version label for snapshots and releases.
	 * @return best effort version identification label, with build info only if needed
	 */
	String getLabel();
	
}
