package se.simonsoft.cms.item.info;

/**
 * Error thrown if request fails due to authentication or authorization which is sometimes difficult to distinguish.
 * It might in the future make sense to subclass this exception either depending on the source of the 
 * exception or to make Authorization issue more specific 
 *
 */
public class CmsAuthenticationException extends RuntimeException {
	
	// TODO: Would it make sense with a flag indicating if issue is authn or authz?
	
	private static final long serialVersionUID = 1L;

	public CmsAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
