package se.simonsoft.cms.item.events.change;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

/**
 * Represents a file or folder affected in a changeset.
 */
public interface CmsChangesetItem {

	/**
	 * @return true if the item change was reported explicitly by the CMS backend
	 */
	boolean isExplicit();
	
	/**
	 * @return true if the item change was derived, for example as unchanged content of a copied folder
	 */
	boolean isDerived();
	
	CmsItemPath getPath();
	
	RepoRevision getRevision();
	
	boolean isFile();
	
	boolean isFolder();
	
	/**
	 * @return true if the entry is a copy to a new location
	 */
	boolean isCopy();
	
	/**
	 * @return "copy from" data, including derived, if {@link #isCopy()}, null otherwise
	 */
	CmsItemPath getCopyFromPath();
	
	/**
	 * @return the revision if {@link #isCopy()}, null otherwise
	 */
	Long getCopyFromRevision();
	
	/**
	 * @return true if the item was added, excluding replace
	 */
	boolean isAdd();

	/**
	 * @return true if the item was replaced, i.e. not a diff but completely changed
	 */
	boolean isReplace();
	
	/**
	 * @return true if the item was deleted, excluding replace
	 */
	boolean isDelete();
	
	/**
	 * @return true if content was actually modified,
	 *  true for copies if destination is not identical to source
	 */
	boolean isContentModified();
	
	/**
	 * @return true if content was added, copied, replaced, modified or deleted
	 */
	boolean isContent();
	
	/**
	 * @return true if the property set was actually modified,
	 *  true for copies if destination props differ from source
	 */
	boolean isPropertiesModified();
	
	/**
	 * @return true on anything that might mean a different set of properties at the path
	 *  
	 */
	boolean isProperties();
	
	/**
	 * Can probably be implemented using svnlook history.
	 * 
	 * A changeset list at the revision for this previous change
	 * would include the same information.
	 * 
	 * @return Previous change for this item, including derived.
	 */
	CmsChangesetItem getPreviousChange();
	
}
