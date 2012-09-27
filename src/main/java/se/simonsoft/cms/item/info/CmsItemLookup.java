package se.simonsoft.cms.item.info;

import java.util.Set;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;

/**
 * Provides access to CMS contents using repository, indexing, working copy, caching or a combination.
 * 
 * Implementations can be either cross-repository, requiring paths/URLs/logicalIds with repo specified,
 * or single-repo throwing exceptions if repo is specified and not identical to the connected repo.
 */
@SuppressWarnings("deprecation") // remove for 2.1
public interface CmsItemLookup extends se.simonsoft.cms.item.CmsItemLookup {

	/**
	 * @param parent folder, optional peg rev
	 * @return children, iteration order decided by backend
	 * @throws CmsConnectionException if connection to the server or repository failed
	 * @throws CmsItemNotFoundException if the item could not be found
	 */	
	CmsItem getItem(CmsItemId id)
		throws CmsConnectionException, CmsItemNotFoundException;
	
	/**
	 * @param parent folder, optional peg rev
	 * @return children, iteration order decided by backend
	 * @throws CmsConnectionException if connection to the server or repository failed
	 * @throws CmsItemNotFoundException if the item could not be found
	 */
	Set<CmsItemId> getImmediates(CmsItemId parent)
		throws CmsConnectionException, CmsItemNotFoundException;

	/**
	 * @param parent folder, optional peg rev
	 * @return children that are folders, iteration order decided by backend
	 * @throws CmsConnectionException if connection to the server or repository failed
	 * @throws CmsItemNotFoundException if the item could not be found
	 */	
	Set<CmsItemId> getImmediateFolders(CmsItemId parent)
		throws CmsConnectionException, CmsItemNotFoundException;
	
}
