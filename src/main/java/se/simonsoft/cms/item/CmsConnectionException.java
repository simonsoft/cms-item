package se.simonsoft.cms.item;

/**
 * Error thrown if connection to the CMS server, repository etc
 * could not be established.
 * 
 * When thrown in a high level interface like {@link CmsItemLookup},
 * it it safe to assume that the underlying implementation has attempted
 * retries and that the error is persistent.
 */
public class CmsConnectionException extends Exception {

	private static final long serialVersionUID = 1L;

	public CmsConnectionException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
