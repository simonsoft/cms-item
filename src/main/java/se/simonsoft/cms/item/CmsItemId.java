package se.simonsoft.cms.item;

/**
 * Used instead of String to identify CMS items (a.k.a. objects/entries/files or folders).
 * Typically immutable as the different getters provides identifiers in different contexts.
 * Facilitates mocking when unit testing methods that deal with CMS paths and URLs.
 */
public interface CmsItemId {
	
	/**
	 * Resource URL, no query string. Not even revision number.
	 * If revision ({@link #getPegRev()}) is specified this is the URL at that revision (i.e. "peg").
	 * @return resource URL, encoded, encoding based on UTF-8 bytes for non-ascii
	 * TODO how about http/https?
	 */
	String getUrl();
	
	/**
	 * The exact revision, if specified, i.e. never HEAD or date or anything but the integer.
	 * @return revision number, or null for revision not specified (HEAD in subversion terminology)
	 */
	Long getPegRev();
	
	/**
	 * Local id without hostname, unique within the context of a connection to a server.
	 * @return logical id, including parameters like peg rev when specified
	 */
	String getLogicalId();

	/**
	 * @return logical id as from {@link #getLogicalId()} but with fully qualified server name
	 */
	String getLogicalIdFull();

	/**
	 * @return path in repository, decoded, including leading slash
	 */
	String getRelPath();
	
	/**
	 * @return repository root URL without trailing slash, encoded
	 */
	String getRepositoryUrl();
	
}
