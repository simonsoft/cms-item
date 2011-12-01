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
	
	public static final String PROTO = "x-svn";
	public static final String PROTO_PREFIX = PROTO + "://";
	
	/**
	 * Resource URL, no query string. Not even revision number.
	 * If revision ({@link #getPegRev()}) is specified this is the URL at that revision (i.e. "peg").
	 * If protocol is not known, always assumes "http" as CMS servers should be capable of redirecting.
	 * If there are implementations that may return "https" it should be clearly stated (and maybe reconsidered),
	 * as http is always used internally on servers.
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
	 * Note that the future definition of persistent may include fragment identifiers and transforms.
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
	
	/**
	 * Derives ID with new relative path from repository root.
	 * @param newRelPath Any rel path, null to get repository root ID
	 * @return Same ID but with new relative path
	 */
	CmsItemId withRelPath(CmsItemPath newRelPath);
	
	/**
	 * Derives ID with new pegRev.
	 * @param newPegRev Any peg revision, null to make ID represent HEAD.
	 * @return Same ID but with new peg revision
	 */
	CmsItemId withPegRev(Long newPegRev); 

}
