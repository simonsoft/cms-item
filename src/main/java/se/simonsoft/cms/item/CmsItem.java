package se.simonsoft.cms.item;

import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Models a specific revision of a file entry in the CMS.
 * 
 * The interfaces in this package are provided as a means of handling CMS contents
 * that is independent of retrieval method. Data could originate from repository access,
 * working copy, index, cache etc. and might be fetched as needed instead of at instantiation.
 */
public interface CmsItem {

	CmsItemId getId();

	/**
	 * Item "kind" can not be identified from {@link #getId()}.
	 * @return file or folder or special kinds
	 */
	CmsItemKind getKind();
	
	/**
	 * @return Item status value, arbitrary value (enum defined per customer), null if not set
	 */
	String getStatus();	
	
	/**
	 * @return contents checksum
	 */
	Checksum getChecksum();
	
	/**
	 * @return all versioned properties on this item
	 */
	CmsItemProperties getProperties();
	
}
