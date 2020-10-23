package se.simonsoft.cms.item.auth;

import se.simonsoft.cms.item.info.CmsAuthenticationException;

/**
 * Draft of authz for repository access.
 * 
 * TODO: #1337 Consider implementation in Apache instead. 
 *
 */
public interface CmsRepositoryAccessControl {

	public void isAuthorized(String repo) throws CmsAuthenticationException;
	
}
