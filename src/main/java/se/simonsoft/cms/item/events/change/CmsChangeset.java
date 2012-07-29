package se.simonsoft.cms.item.events.change;

import java.util.List;
import java.util.Map;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Backend neutral representation of all changes in a commit or transaction.
 */
public interface CmsChangeset {

	RepoRevision getRevision();
	
	/**
	 * Services that don't analyze changes may not return
	 * for instance items added as children of a copied folder,
	 * and may report replacements as delete followed by add.
	 * 
	 * Only derived mode is required to behave similar in all impls.
	 * 
	 * @return true if the service derives all changes (even if there are none),
	 *  false if it returns only those reported by the backend
	 */
	boolean isDeriveEnabled();
	
	/**
	 * @return all changes, explicit and derived
	 */
	List<CmsChangesetItem> getItems();
	
	/**
	 * TODO Requires replace operations to be returned
	 * as a single change instead of add+delete.
	 * 
	 * @return all changes indexed on path
	 */
	Map<CmsItemPath, CmsChangesetItem> getItemsPath();
	
}
