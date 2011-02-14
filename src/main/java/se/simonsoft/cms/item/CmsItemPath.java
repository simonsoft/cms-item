package se.simonsoft.cms.item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Represents a path as a stack of path segments separated by slash,
 * prohibiting filesystem specific syntax such as "../".
 * 
 * Leading slash is required for consistency, as it precedes every path segment.
 * Trailing slashes are always trimmed, meaning that "/folder/" equals "/folder".
 */
public class CmsItemPath {

	public static final String URL_ENCODING_CHARSET = "UTF-8";

	// TODO unicode support
	private static final String VALID_SEGMENT = "[a-zA-Z0-9_\\-\\.~(), ]+"; // TODO add all allowed chars
	private static final Pattern VALID_SEGMENT_PATTERN = Pattern.compile('^' + VALID_SEGMENT + '$');
	private static final String VALID_PATH = "(/" + VALID_SEGMENT + ")+";
	private static final Pattern VALID_PATH_PATTERN = Pattern.compile('^' + VALID_PATH + '$');
	
	private String path;

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
	
	/**
	 * @return path segments encoded using url escape, slashes preserved, non-ascii characters encoded using {@value #URL_ENCODING_CHARSET}
	 */
	public String getPathUrlEncoded() {
		try {
			return URLEncoder.encode(path, URL_ENCODING_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("predefined encoding not supported", e);
		}
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
	
	public String getName() {
		return getName(-1);
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
		
		String comp[] = getPathTrimmed().split("/");
		
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
		
		return new CmsItemPath('/' + noslashPath.substring(0, lastIdx));		
	}
	
	public CmsItemPath append(String pathSegment) {
		if (!VALID_SEGMENT_PATTERN.matcher(pathSegment).matches()) {
			throw new IllegalArgumentException("Invalid path segment: " + pathSegment);
		}
		return new CmsItemPath(path + '/' + pathSegment);
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
