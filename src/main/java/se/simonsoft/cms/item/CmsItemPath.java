package se.simonsoft.cms.item;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a path (not-encoded) as a stack of path segments separated by slash,
 * prohibiting filesystem specific syntax such as "../".
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
 */
public class CmsItemPath {

	public static final String URL_ENCODING_CHARSET = "UTF-8";

	private static final String INVALID_CHARS = "/*\\\\"; // TODO add more strictly prohibited chars
	private static final String VALID_SEGMENT = "[^" + INVALID_CHARS + "]*[^" + INVALID_CHARS + "\\s]+"; 
	private static final Pattern VALID_SEGMENT_PATTERN = Pattern.compile('^' + VALID_SEGMENT + '$');
	private static final String VALID_PATH = "(/" + VALID_SEGMENT + ")+";
	private static final Pattern VALID_PATH_PATTERN = Pattern.compile('^' + VALID_PATH + '$');
	
	private String path;

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
	
	/** Decided to not expose any encoded path from this class. SVNURL is normative with regards to encoding and we should not implement our own encoding.
	 * @return path segments encoded using url escape, slashes preserved, non-ascii characters encoded using {@value #URL_ENCODING_CHARSET}
	 */
	@SuppressWarnings("unused")
	private String getPathUrlEncoded() {
		StringBuffer r = new StringBuffer();
		String[] s = getPathSegmentsArray();
		for (int i = 0; i < s.length; i++) {
			r.append('/');
			String[] w = s[i].split("\\s");
			for (int j = 0; j < w.length; j++) {
				if (j > 0) {
					r.append("%20");
				}
				r.append(urlEncode(w[j]));
			}
		}
		return r.toString();
	}
	
	private String urlEncode(String s) {
		try {
			return URLEncoder.encode(s, URL_ENCODING_CHARSET);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("predefined encoding not supported: " + URL_ENCODING_CHARSET, e);
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
	
	private String[] getPathSegmentsArray() {
		return getPathTrimmed().split("/");
	}
	
	public List<String> getPathSegments() {
		
		List<String> pathList = Arrays.asList(this.getPathSegmentsArray());
		return pathList;
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
	
	/**
	 * @param path segment
	 * @throws IllegalArgumentException if invalid segment, such as containing slash
	 */	
	public CmsItemPath append(String pathSegment) {
		if (!VALID_SEGMENT_PATTERN.matcher(pathSegment).matches()) {
			throw new IllegalArgumentException("Invalid path segment: " + pathSegment);
		}
		return new CmsItemPath(path + '/' + pathSegment);
	}

	/**
	 * @param path another path
	 */	
	public CmsItemPath append(List<String> list) {
		
		CmsItemPath result = new CmsItemPath(this.path);
		for (String segment: list) {
			result = result.append(segment);
		}
		return result;
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
