package se.simonsoft.cms.item;

public enum CmsItemKind {

	File,
	/**
	 * Often called "dir" in subversion "kind" info
	 */
	Folder,
	/**
	 * Special kind of folder, useful for styling etc, use {@link #isFolder()} to handle as folder
	 */
	Repository,
	/**
	 * Currently not supported in CMS.
	 */
	Symlink;
	
	/**
	 * @return subversion's "kind" string (but maybe not for symlink)
	 */
	public String getKind() {
		if (isFolder()) {
			return "dir";
		}
		return toString().toLowerCase();
	}
	
	public boolean isFile() {
		return this == File;
	}
	
	public boolean isFolder() {
		return this == Folder || this == Repository;
	}
	
}
