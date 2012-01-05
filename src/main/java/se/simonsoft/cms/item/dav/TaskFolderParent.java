package se.simonsoft.cms.item.dav;

/**
 * CMS task areas are ideally browsable by the user who creates them,
 * but for other users available only if the exact URL to a specific task folder is known.
 * 
 * These parent folders will only work as expected, secrecy etc,
 * if accompanied by matching access rules on the DAV server.
 */
public interface TaskFolderParent {

	/**
	 * @return The URL for the <em>current user</em> where the area can be browsed or mounted
	 */
	String getUrlUserBrowse();
	
	/**
	 * @param secret part of the folder name, provides uniqueness
	 * @return a new folder, not created but unique and reserved, parent folder existing adn writable
	 */
	TaskFolder getNew(TaskSecret secret);
	
}
