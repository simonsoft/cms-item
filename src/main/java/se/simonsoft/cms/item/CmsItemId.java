package se.simonsoft.cms.item;

/**
 * Used instead of String to identify CMS items (a.k.a. objects/entries/files or folders).
 * 
 * The interface methods should return the <em>persistent</em> form of id,
 * typically location and revision -- never parameters for transformation etc.
 * 
 * Typically immutable as the different getters provides identifiers in different contexts.
 * 
 * Facilitates mocking when unit testing methods that deal with CMS paths and URLs.
 */
public interface CmsItemId {
	
	/**
	 * Resource URL, no query string. Not even revision number.
	 * If revision ({@link #getPegRev()}) is specified this is the URL at that revision (i.e. "peg").
	 * If protocol is not known, always assumes "http" as CMS servers should be capable of redirecting.
	 * If there are implementations that may return "https" it should be clearly stated (and maybe reconsidered).
	 * @return resource URL, encoded, encoding based on UTF-8 bytes for non-ascii
	 */
	String getUrl();
	
	/**
	 * The exact revision, if specified, i.e. never HEAD or date or anything but the integer.
	 * @return revision number, or null for revision not specified (HEAD in subversion terminology)
	 */
	Long getPegRev();
	
	/**
	 * Local id without hostname, unique within the context of a connection to a server.
	 * TODO Still unknown if fragment identifiers should be considered persistent.
	 * @return logical id, including persistent parameters, i.e. peg rev, but not including actions etc.
	 */
	String getLogicalId();

	/**
	 * Hybrid between persistent id and URL-like id with hostname.
	 * @return logical id as from {@link #getLogicalId()} but with fully qualified server name,
	 *  stripping any other non-persistent info like transforms
	 */
	String getLogicalIdFull();

	/**
	 * @return path in repository
	 */
	CmsItemPath getRelPath();
	
	/**
	 * @return repository root, toString is URL without trailing slash, encoded
	 */
	CmsRepository getRepository();
	
	/**
	 * @return repository root URL without trailing slash, encoded
	 * @deprecated Use {@link #getRepository()}.getUrl()
	 */
	String getRepositoryUrl();
	
}
