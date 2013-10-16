/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.simonsoft.cms.item;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simonsoft.cms.item.impl.CmsItemIdUrl;

/**
 * Represents repository URL information.
 * 
 * We recommend against using non-ascii characters in hostname, parent path or repository name,
 * but this class does not care if path elements are encoded or not, it returns them as given.
 * 
 * However, we would only enforce that if this class was made abstract so that a configured repository is the only instance.
 * Or if only {@link #CmsRepository(String)} was visible.
 * We currently use new repository instances in for example {@link se.simonsoft.cms.item.impl.CmsItemIdArg#CmsItemIdArg(String)}.
 */
public class CmsRepository implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected static final String URLENCODE_ENCODING = "UTF-8";
	
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
		normalize();
	}
	
	/**
	 * @param rootUrl without trailing slash
	 * @param parentPath with leading but not trailing slash,
	 *  can not be urlencoded or require urlencoding because encoding behaviour is undefined
	 * @param name repository name, no slashes,
	 *  can not be urlencoded or require urlencoding because encoding behaviour is undefined
	 */
	public CmsRepository(String rootUrl, String parentPath, String name) {
		Matcher m = P_ROOT.matcher(rootUrl);
		if (!m.matches()) {
			throw new IllegalArgumentException("Failed to parse server root URL: " + rootUrl);
		}
		this.protocol = m.group(1);
		this.host = m.group(2);
		this.parent = parentPath; // ? maybe urlencode
		this.name = name;         // ? maybe urlencode
		if (parent == null || parent.endsWith("/") || !parent.startsWith("/")) {
			throw new IllegalArgumentException("Parent path must have leading but no trailing slash, got " + parent);
		}
		normalize();
	}
	
	/**
	 * @param protocol
	 * @param parentPath with leading but not trailing slash,
	 *  can not be urlencoded or require urlencoding because encoding behaviour is undefined
	 * @param name repository name, no slashes,
	 *  can not be urlencoded or require urlencoding because encoding behaviour is undefined
	 */
	public CmsRepository(String protocol, String hostWithOptionalPort, String parentPath, String name) {
		if (protocol != null && protocol.length() == 0) {
			throw new IllegalArgumentException("Protocol can be null but not empty");
		}
		if (protocol != null && protocol.indexOf(':') + protocol.indexOf('/') > -2) {
			throw new IllegalArgumentException("Invalid protocol " + protocol);
		}
		if (hostWithOptionalPort != null && hostWithOptionalPort.length() == 0) {
			throw new IllegalArgumentException("Hostname can be null but not empty");
		}
		if (hostWithOptionalPort != null && hostWithOptionalPort.contains("/")) {
			throw new IllegalArgumentException("Path character found in host " + hostWithOptionalPort);
		}
		this.protocol = protocol;
		this.host = hostWithOptionalPort;
		this.parent = parentPath;
		this.name = name;
		if (parent == null) {
			throw new IllegalArgumentException("Parent path can not be null");
		}
		if (parent.length() == 0) {
			// OK, repositories at server root
		} else if (parent.endsWith("/") || !parent.startsWith("/")) {
			throw new IllegalArgumentException("Parent path must have leading but no trailing slash, got " + parent);
		}
		normalize();
	}
	
	/**
	 * Used to name a repository when a hostname is not known.
	 * Needed because we sometimes have logical ids without host.
	 * 
	 * @param parentPath With leading but not traling slash
	 * @param name Repository name, no slashes
	 */
	public CmsRepository(String parentPath, String name) {
		this(null, null, parentPath, name);
	}
	
	/**
	 * Called last in every constructor.
	 */
	private void normalize() {
		if (protocol != null && host != null) {
			if ("http".equals(protocol) && host.endsWith(":80")) {
				host = getHostname();
			}
			if ("https".equals(protocol) && host.endsWith(":443")) {
				host = getHostname();
			}
		}
	}
	
	/**
	 * @return item id for use in path based operations, with relpath null, not valid as logical id
	 */
	public CmsItemId getItemId() {
		return new CmsItemIdUrl(this, (CmsItemPath) null);
	}
	
	/**
	 * Analogous to isFullyQualified in SvnLogicalId.
	 * @return true if the repository URL is known, false if only parent path and repo name
	 */
	public boolean isHostKnown() {
		return this.host != null;
	}
	
	/**
	 * @return hostname and possibly port
	 */
	public String getHost() {
		if (!isHostKnown()) {
			throw new IllegalStateException("Repository identified only by parent path and name: " + this.parent + "/" + this.name);
		}
		return this.host;
	}
	
	/**
	 * @return hostname without port
	 */
	public String getHostname() {
		if (!isHostKnown()) {
			throw new IllegalStateException("Repository identified only by parent path and name: " + this.parent + "/" + this.name);
		}
		int c = this.host.indexOf(':');
		if (c >= 0) {
			return this.host.substring(0, c);
		}
		return this.host;
	}
	
	/**
	 * @return URL to server root, no trailing slash, protocol http or https
	 */
	public String getServerRootUrl() {
		if (!isHostKnown()) {
			throw new IllegalStateException("Repository identified only by parent path and name: " + this.parent + "/" + this.name);
		}
		return protocol + "://" + host;
	}
	
	/**
	 * @return Full ÚRL to repository, no trailing slash, should be ascii only
	 */
	public String getUrl() {
		return getServerRootUrl() + getUrlAtHost();
	}
	
	/**
	 * Returns URL used in UI operations on the same host.
	 * Unlike {@link #getUrl()} this is supported when host is not known (see {@link #CmsRepository(String, String)}.
	 * @return Repository URL from server root starting with slash
	 */
	public String getUrlAtHost() {
		return getParentPath() + "/" + getName();
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
	
	/**
	 * In case we some day introduce support for whitespaces and non-asciis in repo name or parent
	 * @return analogous to {@link #getUrlAtHost()} but never encoded, common as prefix to path in indexing etc
	 */
	public String getPath() {
		return getParentPath() + "/" + getName();
	}

	protected String urlencodeSegment(String pathSegment) {
		try {
			return URLEncoder.encode(pathSegment, URLENCODE_ENCODING).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			// encoding is not from user input so this is clearly a fatal error
			throw new RuntimeException("Failed to urlencode using encoding " + URLENCODE_ENCODING + ", value: " + pathSegment);
		}
	}
	
	public String urldecode(String encodedPath) {
		try {
			return URLDecoder.decode(encodedPath, CmsRepository.URLENCODE_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to urldecode using encoding " + URLENCODE_ENCODING + ", value: " + encodedPath);
		}
	}
	
	/**
	 * Verifies that parent path and repository name is identical.
     * Verifies host equality on the two instances only if host is known on both.
     * That is for consistency with CmsItemId and because different host is quite uncommon
	 * while temporarily passing CmsItemId without host is quite common.
	 * Callers can specifically check that repo1.isHostKnown() == repo2.isHostKnown().
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj instanceof String) return toString().equals(obj.toString());
		if (!(obj instanceof CmsRepository)) return false;
		CmsRepository r = (CmsRepository) obj;
		if (isHostKnown() && r.isHostKnown()) {
			if (!getHost().equals(r.getHost())) return false;
		}
		return getParentPath().equals(r.getParentPath()) && getName().equals(r.getName());
	}

	@Override
	public int hashCode() {
		return getPath().hashCode();
	}

	/**
	 * @return URL if host is known, string containing class name, parent path and name if not
	 */
	@Override
	public String toString() {
		if (!isHostKnown()) {
			return "CmsRepository:" + getParentPath() + "/" + getName();
		}		
		return getUrl();
	}
	
}
