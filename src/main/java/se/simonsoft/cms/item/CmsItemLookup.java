package se.simonsoft.cms.item;

/**
 * Provides access to CMS contents using repository, indexing, working copy, caching or a combination.
 * 
 * Implementations can be either cross-repository, requiring paths/URLs/logicalIds with repo specified,
 * or single-repo throwing exceptions if repo is specified and not identical to the connected repo. 
 */
public interface CmsItemLookup {

	/**
	 * Loads item based on id.
	 * Returned instance may be "online", i.e. load data as requested.
	 */
	CmsItem getItem(CmsItemId id);
	
}
