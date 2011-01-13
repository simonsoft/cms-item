package se.simonsoft.cms.item;

public interface CmsItemId {
	
	/**
	 * Resource URL, no query string. Not even revision number.
	 * If revision ({@link #getRevision()}) is specified this is the URL at that revision (i.e. "peg").
	 * @return decoded resource URL, i.e. might contain whitespace
	 * TODO how about http/https?
	 */
	String getUrl();
	
	/**
	 * The exact revision, if specified, i.e. never HEAD or date or anything but the integer.
	 * @return revision number, or null for revision not specified (HEAD in subversion terminology)
	 */
	Long getRevision();
	
	/**
	 * Local id without hostname, unique within the context of a connection to a server.
	 * @return logical id
	 * TODO with or without revision?
	 */
	String getLogicalId();
	//String getLogicalIdRev();
	
	/**
	 * @return logical id as from {@link #getLogicalId()} but with fully qualified server name
	 * TODO with or without revision?
	 */
	String getLogicalIdFull();

	/**
	 * @return path in repository, decoded, including leading slash
	 */
	String getPath();
	
	/**
	 * @return repository root URL without trailing slash
	 */
	String getRepositoryUrl();
	
}
