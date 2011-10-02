package se.simonsoft.cms.item;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents repository URL information.
 * 
 * We recommend against using non-ascii characters in hostname, parent path or repository name,
 * but this class does not care if path elements are encoded or not, it returns them as given.
 */
public class CmsRepository {

	private static final Pattern P_ROOT = Pattern.compile("(https?)://([^/]+)");
	private static final Pattern P_URL = Pattern.compile("(https?)://([^/]+)(.*)/([^/]+)");
	private String protocol;
	private String host;
	private String parent;
	private String name;
	
	public CmsRepository(String repositoryUrl) {
		Matcher m = P_URL.matcher(repositoryUrl);
		if (!m.matches()) {
			throw new IllegalArgumentException("Failed to parse repository URL: " + repositoryUrl);
		}
		this.protocol = m.group(1);
		this.host = m.group(2);
		this.parent = m.group(3);
		this.name = m.group(4);
	}
	
	public CmsRepository(String rootUrl, String parentPath, String name) {
		Matcher m = P_ROOT.matcher(rootUrl);
		if (!m.matches()) {
			throw new IllegalArgumentException("Failed to parse server root URL: " + rootUrl);
		}
		this.protocol = m.group(1);
		this.host = m.group(2);
		this.parent = parentPath;
		this.name = name;		
	}
	
	public CmsRepository(String protocol, String hostWithOptionalPort, String parentPath, String name) {
		this.protocol = protocol;
		this.host = hostWithOptionalPort;
		this.parent = parentPath;
		this.name = name;
	}
	
	/**
	 * @return URL to server root, no trailing slash, protocol http or https
	 */
	public String getServerRootUrl() {
		return protocol + "://" + host;
	}
	
	/**
	 * @return Full ÚRL to repository, no trailing slash, should be ascii only
	 */
	public String getUrl() {
		return getServerRootUrl() + getParentPath() + "/" + getName();
	}
	
	/**
	 * @return name of the repository, last part of path, also called "base"
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return repository parent, from server root, no trailing slash, e.g. "/svn",
	 *   empty string for repositories directly in server root
	 */
	public String getParentPath() {
		return parent;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && toString().equals(obj.toString());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public String toString() {
		return getUrl();
	}
	
}
