package se.simonsoft.cms.item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a path (not-encoded) from a repository root as a stack of path segments 
 * separated by slash, prohibiting filesystem specific syntax such as "../".
 * <p>
 * Leading slash is required for consistency, as it precedes every path segment.
 * All calling code need to be aware if leading slash is included or not, so it is better
 * to require leading slash than trailing slash, which might be considered insignificant.
 * Trailing slashes are always trimmed, meaning that "/folder/" equals "/folder".
 * <p>
 * Minimal validation is done in constructor and {@link #append(String)}.
 * It is up to the calling code to implement strict validation.
 * Path segments may not end with whitespace because that might not be visible in output.
 * They may however start with whitespace because that poses no immediate practical problems.
 * <p>
 * Many of the methods in this class are undefined for the empty path, or root.
 * One problem with a root concept would be that it is a special case with regards to trailing slash.
 * Thus, empty string or slash only is not allowed in the constructor,
 * and {@link #getParent()} returns null if the path has only one segment.
 * On a higher level, root path can be represented with null. This is to Subversion's
 * special handling of repository root, i.e. most operations are not allowed.  
 */
public class CmsItemPath {

	public static final String URL_ENCODING_CHARSET = "UTF-8";

	private static final String INVALID_CHARS = "/*\\\\"; // TODO add more strictly prohibited chars
	private static final String VALID_SEGMENT = "[^" + INVALID_CHARS + "]*[^" + INVALID_CHARS + "\\s]+"; 
	private static final Pattern VALID_SEGMENT_PATTERN = Pattern.compile('^' + VALID_SEGMENT + '$');
	private static final String VALID_PATH = "(/" + VALID_SEGMENT + ")+";
	private static final Pattern VALID_PATH_PATTERN = Pattern.compile('^' + VALID_PATH + '$');
	
	private String path;
	private List<String> pathList = null;

	/**
	 * @param path with leading slash, trailing slash will be trimmed
	 * @throws IllegalArgumentException for invalid path such as containing double slashes
	 */
	public CmsItemPath(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		if (!VALID_PATH_PATTERN.matcher(path).matches()) {
			throw new IllegalArgumentException("Invalid path: " + path);
		}
		this.path = path;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getPathTrimmed() {
		return getPathTrimmed(true, true);
	}

	protected String getPathTrimmed(boolean trimStart, boolean trimEnd) {
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
	
	/** Provides the last component of the path, i.e. the file name or the folder name.
	 * @return the name
	 */
	public String getName() {
		return getName(-1);
	}
	
	/** 
	 * Provides a component of the relative path.
	 * Request Integer.MAX_VALUE to get that last one (deprecated behavior).
	 * @param segmentPosition, >0 to count from left and <0 from right.
	 * @return
	 * @throws IllegalArgumentException if segmentPosition is 0.
	 */
	public String getName(int segmentPosition) {
		// SvnLogicalId.getRelPathComponent
		String result = null;
		
		String comp[] = getPathSegmentsArray();
		
		if (segmentPosition == 0) {
			throw new IllegalArgumentException("Path segment position must be >0 or <0");
		}
		
		int index;
		if (segmentPosition == Integer.MAX_VALUE) {
			index = comp.length-1;
		} else if (segmentPosition < 0) {
			index = comp.length + segmentPosition;
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
	
	/** Provides the whole path as an array of path segments.
	 * @return
	 */
	private String[] getPathSegmentsArray() {
		return getPathTrimmed().split("/");
	}
	
	/** Provides the whole path as a list of path segments.
	 * @return immutable list of path segments
	 */
	public List<String> getPathSegments() {
		if (pathList == null) {
			pathList = new ArrayList<String>(Arrays.asList(this.getPathSegmentsArray())) {
				private static final long serialVersionUID = 1L;
				private static final String MSG = "Immutable path segments list from CmsItemPath";
				@Override public String set(int index, String element) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean add(String element) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public void add(int index, String element) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean addAll(Collection<? extends String> c) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean addAll(int index, Collection<? extends String> c) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public String remove(int index) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean remove(Object o) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean removeAll(Collection<?> c) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override protected void removeRange(int fromIndex, int toIndex) {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public void clear() {
					throw new UnsupportedOperationException(MSG);
				}
				@Override public boolean retainAll(Collection<?> c) {
					throw new UnsupportedOperationException(MSG);
				}
			};
		}
		return pathList;
	}

	/** The name without extension. See {@link #getExtension()} for definition of extension.
	 * @return
	 */
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
	
	/** The extension of the name. 
	 * <p>
	 * The extension is the part after the last dot, 
	 * unless the last dot is the first character, e.g. hidden files.
	 * @return extension or empty string.
	 */
	public String getExtension() {
		String n = getName();
		int d = n.lastIndexOf('.');
		if (d == 0) {
			return "";
		}
		return n.substring(d+1);
	}
	
	/**
	 * @return parent path or null if the path has only one segment (root is undefined)
	 */
	public CmsItemPath getParent() {
		// BrowseEntry.getPathParent
		String noslashPath = getPathTrimmed();
		int lastIdx = noslashPath.lastIndexOf('/');
		
		if (lastIdx == -1 || lastIdx == 0) {
			return null;
		} 
		
		return new CmsItemPath('/' + noslashPath.substring(0, lastIdx));		
	}
	
	/** Appends a single path segment to the path.
	 * @param path segment
	 * @throws IllegalArgumentException if invalid segment, such as containing slash
	 */	
	public CmsItemPath append(String pathSegment) {
		if (!VALID_SEGMENT_PATTERN.matcher(pathSegment).matches()) {
			throw new IllegalArgumentException("Invalid path segment: " + pathSegment);
		}
		return new CmsItemPath(path + '/' + pathSegment);
	}

	/** Append path segments to the end of the CmsItemPath.
	 * @param list of path segments to append
	 */	
	public CmsItemPath append(List<String> list) {
		
		CmsItemPath result = new CmsItemPath(this.path);
		for (String segment: list) {
			result = result.append(segment);
		}
		return result;
	}	
	
	/** Returns a portion of this path as a list. See List.subList(...)
	 * @param fromIndex - low endpoint (inclusive) of the subList
	 * @param toIndex - high endpoint (exclusive) of the subList 
	 * @return
	 */
	public List<String> subPath(int fromIndex, int toIndex) {
		
		List<String> pathList = this.getPathSegments();
		int fromAbs = fromIndex;
		if (fromIndex < 0) {
			fromAbs = pathList.size() + fromIndex;
		}
		
		int toAbs = toIndex;
		if (toIndex < 0) {
			toAbs = pathList.size() + toIndex;
		}
		
		return this.getPathSegments().subList(fromAbs, toAbs);
	}
	/** Returns a portion of this path as a list, from fromIndex to the end of the path.
	 * @param fromIndex - low endpoint (inclusive) of the subList
	 */ 
	public List<String> subPath(int fromIndex) {
		
		List<String> pathList = this.getPathSegments();
		return this.subPath(fromIndex, pathList.size());
	}
	
	/** Determines if this path is an ancestor of child.
	 * @param child
	 * @return
	 */
	public boolean isAncestorOf(CmsItemPath child) {
		
		return isAncestorOf(child, false);
	}
	
	/** Determines if this path is a direct parent of child.
	 * @param child
	 * @return
	 */
	public boolean isParentOf(CmsItemPath child) {
		
		return isAncestorOf(child, true);
	}
	
	/** Determines if this path is an ancestor/parent of child.
	 * @param child
	 * @param parentOnly requires that this path is a direct parent.
	 * @return
	 */
	private boolean isAncestorOf(CmsItemPath child, boolean parentOnly) {
		
		List<String> thisList = this.getPathSegments();
		List<String> childList = child.getPathSegments();
		
		if (thisList.size() >= childList.size()) {
			return false;
		}
		
		if (parentOnly && thisList.size()+1 != childList.size()) {
			// Strict requirement on direct parent
			return false;
		}
		
		return thisList.equals(childList.subList(0, thisList.size()));
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof CmsItemPath && path.equals(((CmsItemPath) obj).getPath());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		// TODO normalize?
		return path;
	}
	
}
