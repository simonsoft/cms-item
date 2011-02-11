package se.simonsoft.cms.item;

public class CmsItemPath {

	private String path;

	public CmsItemPath(String path) {
		this.path = path;
	}
	
	public String getPath() {
		return null;
	}
	
	public String getPathUrlEncoded() {
		return null;
	}
	
	public String getPathTrimmed() {
		return getPathTrimmed(true, true);
	}

	// TODO always trim trailing slash?
	public String getPathTrimmed(boolean trimStart, boolean trimEnd) {
		int start = 0;
		int end = path.length();

		if (trimStart && path.startsWith("/")) {
			start++;
		}
		if (trimEnd && path.endsWith("/")) {
			end--;
		}
		return path.substring(start, end);
	}
	
	public String getName() {
		// rightmost component
		// BrowseEntry.getLastPathComponent ?
		return null;
	}
	
	/** 
	 * Provides a component of the relative path.
	 * Request Integer.MAX_VALUE to get that last one (deprecated behavior).
	 * @param segmentPosition from 0? TODO maybe we should have >0 to count from left and <0 from right?
	 * @return
	 */
	public String getName(int segmentPosition) {
		// SvnLogicalId.getRelPathComponent
		String result = null;
		
		String comp[] = path.split("/");
		
		int index;
		if (segmentPosition == Integer.MAX_VALUE) {
			index = comp.length-1;
		} else {
			index = segmentPosition - 1; // new argument definition
		}
		
		try {
			result = comp[index];
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		
		return result;
	}
	
	public String getNameBase() {
		String n = getName();
		String ext = getExtension();
		
		int endIdx = n.length();
		if (ext.length() > 0) {
			endIdx -= ext.length();
			// accounting for the dot
			endIdx--;
		}
		return n.substring(0, endIdx);
	}
	
	public String getExtension() {
		String n = getName();
		int d = n.lastIndexOf('.');
		if (d == 0) {
			return "";
		}
		return n.substring(d+1);
	}
	
	public CmsItemPath getParent() {
		// BrowseEntry.getPathParent
		String noslashPath = getPathTrimmed();
		int lastIdx = noslashPath.lastIndexOf('/');
		
		if (lastIdx == -1 || lastIdx == 0) {
			return null;
		} 
		
		return new CmsItemPath(noslashPath.substring(0, lastIdx));		
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
