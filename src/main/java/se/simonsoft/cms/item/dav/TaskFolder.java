package se.simonsoft.cms.item.dav;

import java.util.Date;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Represents a folder created for a specific task,
 * which users access using a URL, {@link #getUrl()},
 * possibly a "secret URL".
 * 
 * The back end uses a relative path, {@link #getRelPath()},
 * and maps this to back end storage using configuration info.
 * 
 * No actual local paths should be exposed.
 */
public interface TaskFolder {

	/**
	 * @return The user's URL to this area
	 */
	String getUrl();
	
	/**
	 * @return The path from DAV root to the folder
	 */
	CmsItemPath getRelPath();
	
	/**
	 * @return The timestamp after which the share will be automatically deleted, null if never
	 */
	Date getExpires();
	
}
