package se.simonsoft.cms.item;

public class CmsItemPath {

	public String getPath() {
		return null;
	}
	
	public String getPathUrlEncoded() {
		return null;
	}
	
	public String getPathTrimmed() {
		return getPathTrimmed(true, true);
	}

	public String getPathTrimmed(boolean trimStart, boolean trimEnd) {
		// SvnLogicalId.trimSlashes
		return null;
	}
	
	public String getName() {
		// rightmost component
		// BrowseEntry.getLastPathComponent ?
		return null;
	}
	
	/** 
	 * Provides a component of the relative path.
	 * Request Integer.MAX_VALUE to get that last one (deprecated behavior).
	 * @param index from 0? TODO maybe we should have >0 to count from left and <0 from right?
	 * @return
	 */
	public String getName(int index) {
		// SvnLogicalId.getRelPathComponent
		return null;
	}
	
	public String getNameBase() {
		return null;
	}
	
	public String getExtension() {
		return null;
	}
	
	public CmsItemPath getParent() {
		// SvnLogicalId.removePathTail
		// BrowseEntry.getPathParent ?
		return null;
	}
	
	public CmsItemPath append(String pathSegment) {
		// SvnLogicalId.appendPath
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		throw new UnsupportedOperationException("not implemented");
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		// normalize
		return null;
	}
	
}
