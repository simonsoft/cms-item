package se.simonsoft.cms.item;


public interface CmsAccess {

	/**
	 * Loads item based on id.
	 * 
	 * Implementations can be either cross-repository, requiring paths/URLs/logicalIds with repo specified,
	 * or single-repo throwing exceptions if repo is specified and not identical to the connected repo. 
	 * 
	 * @param id
	 * @return
	 */
	CmsItem getItem(CmsItemId id);
	
}
