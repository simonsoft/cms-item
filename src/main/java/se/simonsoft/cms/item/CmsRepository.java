/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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
		if (parent == null || parent.endsWith("/") || !parent.startsWith("/")) {
			throw new IllegalArgumentException("Parent path must have leading but no trailing slash, got " + parent);
		}		
	}
	
	public CmsRepository(String protocol, String hostWithOptionalPort, String parentPath, String name) {
		if (protocol != null && protocol.length() == 0) {
			throw new IllegalArgumentException("Protocol can be null but not empty");
		}
		if (hostWithOptionalPort != null && hostWithOptionalPort.length() == 0) {
			throw new IllegalArgumentException("Hostname can be null but not empty");
		}
		this.protocol = protocol;
		this.host = hostWithOptionalPort;
		this.parent = parentPath;
		this.name = name;
		if (parent == null || parent.endsWith("/") || !parent.startsWith("/")) {
			throw new IllegalArgumentException("Parent path must have leading but no trailing slash, got " + parent);
		}
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
		if (obj == null) return false;
		if (obj instanceof String) return toString().equals(obj.toString());
		if (!(obj instanceof CmsRepository)) return false;
		CmsRepository r = (CmsRepository) obj;
		if (isHostKnown()) {
			if (!getHost().equals(r.getHost())) return false;
		} else {
			if (r.isHostKnown()) return false;
		}
		return getParentPath().equals(r.getParentPath()) && getName().equals(r.getName());
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
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
