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
package se.simonsoft.cms.item.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

/**
 * Concrete class for use in the common case when a valid logical id
 * is known from metadata etc and provided as string argument to an operation.
 * 
 * Mutable: allows adding of hostname and peg revision if not already set.
 * 
 * Provides a String argument constructor for use as JAX-RS parameter.
 * 
 * This class never needs to deal with encoding of logical ID, which is tricky,
 * only decoding of path, which is simple. Assuming hostname is always ascii.
 */
public class CmsItemIdArg implements CmsItemId {

	// TODO before adding more validation here make sure it can be shared
	// between SvnLogicalId and other implementations.
	// This class could have some basic validation in the constructor
	// but since it is used to process input incrementally
	// it needs an explicit validate() method for semantic validation.
	
	public static final String HTTP_PREFIX = "http://";
	public static final String PEG = "?p=";
	public static final Pattern NICE = Pattern.compile(PROTO_PREFIX + "([^/]*)(.*/)([^^]*)\\^(/|[^?]+[^/])/?(?:\\?p=(\\d+))?");
	
	/**
	 * Root path is not represented in CmsItemPath so we need to define
	 * what corresponds to relpath null.
	 * Single slash is consistent with how SvnLogicalId represents repo root id,
	 * though it is inconsistent with other paths as it ends with slash.
	 */
	public static final String REPO_ROOT_PATH = "/";
	
	private String host = "";
	private String parent;
	private String repo;
	private String relpath;
	private Long pegRev = null;
	private boolean orgfull;
	private boolean orgpeg;
	
	/**
	 * @param logicalId with or without hostname and peg rev
	 */
	public CmsItemIdArg(String logicalId) {
		Matcher m = NICE.matcher(logicalId);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid logical id: " + logicalId);
		}
		this.host = m.group(1);
		this.parent = m.group(2);
		this.repo = m.group(3);
		this.relpath = m.group(4);
		if (m.group(5) != null) {
			this.pegRev  = Long.parseLong(m.group(5));
			this.orgpeg = true;
		}
		if (isFullyQualified()) {
			this.orgfull = true;
		}
	}
	
	public boolean isFullyQualified() {
		return host.length() > 0;
	}
	
	public boolean isFullyQualifiedOriginally() {
		return orgfull;
	}
	
	/**
	 * Using an is-name rather than has-name to conform with javabean spec.
	 * @return true if id has peg rev
	 */
	public boolean isPegged() {
		return pegRev != null;
	}
	
	public boolean isPeggedOriginally() {
		return orgpeg;
	}
	
	/**
	 * @param fullyQualifiedName Often set by the web application and not the caller
	 * when the caller is on the same server, including port if non-standard
	 */
	public void setHostname(String fullyQualifiedName) {
		if (isFullyQualified()) {
			throw new IllegalStateException("Hostname already set for " + getLogicalIdFull());
		}
		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0) {
			throw new IllegalArgumentException("Method not intended for removing hostname");
		}
		host = fullyQualifiedName;
	}
	
	/**
	 * Makes sure hostname is set and matching a given value.
	 * @param fullyQualifiedName hostname to set or to expect
	 * @throws IllegalArgumentException If the hostname of the logicalId does not match the given
	 */
	public void setHostnameOrValidate(String fullyQualifiedName)
			throws IllegalArgumentException {
		if (isFullyQualified()) {
			if (!host.equals(fullyQualifiedName))  {
				throw new IllegalArgumentException("Unexpected hostname in " + getLogicalIdFull() + ", expected " + fullyQualifiedName);
			}
		} else {
			setHostname(fullyQualifiedName);
		}
	}
	
	/**
	 * Used specifically for user input, like {@link #setHostname(String)}
	 * @param pegRev when given as an optional parameter alongside the id
	 */
	public void setPegRev(long pegRev) {
		if (isPegged()) {
			throw new IllegalStateException("Peg revision already set for " + getLogicalId());
		} else {
			this.pegRev = pegRev;
		}
	}
	
	/**
	 * Used for validation in services that support only operations in
	 * a current repository connection session.
	 */
	public String getRepositoryUrl() {
		if (!isFullyQualified()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}
		return HTTP_PREFIX + host + parent + repo;
	}
	
	@Override
	public CmsRepository getRepository() {
		if (!isFullyQualified()) {
			return new CmsRepository(parent.substring(0, parent.length() - 1), repo);
		}
		return new CmsRepository(getRepositoryUrl());
	}

	/**
	 * Used for example when separating object and revision fields in a form.
	 * @return Location part of the id, no peg rev.
	public String getLogicalIdPath() {
		return PROTO_PREFIX + parent + repo + "^" + relpath;
	}
	 */	
	
	@Override
	public String getUrl() {
		if (REPO_ROOT_PATH.equals(relpath)) {
			return getRepository().getUrl();
		}
		return getRepository().getUrl() + relpath;
	}
	
	@Override
	public String getUrlAtHost() {
		return null;
	}

	@Override
	public Long getPegRev() {
		return pegRev;
	}

	/**
	 * Always known in this implementation, unlike {@link #getLogicalIdFull()}.
	 */
	@Override
	public String getLogicalId() {
		return toString("", relpath, pegRev);
	}

	@Override
	public String getLogicalIdFull() {
		if (!isFullyQualified()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}
		return toString(host, relpath, pegRev);
	}

	@Override
	public CmsItemPath getRelPath() {
		return getRelPath(relpath);
	}
	
	
	private String getQuery(Long anyPegRev) {
		if (anyPegRev != null) {
			return PEG + anyPegRev;
		}
		return "";
	}
	
	private CmsItemPath getRelPath(String relpathEncoded) {
		String decoded;
		try {
			decoded = URLDecoder.decode(relpathEncoded, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Failed to decode with charset UTF-8");
		}
		if (REPO_ROOT_PATH.equals(relpathEncoded)) {
			return null;
		}
		return new CmsItemPath(decoded);
	}
	
	private String toString(String anyHost, String relpathEncoded, Long anyPegRev) {
		return PROTO_PREFIX
				+ anyHost
				+ parent + repo + "^" 
				+ (relpathEncoded == null ? REPO_ROOT_PATH : relpathEncoded)
				+ getQuery(anyPegRev);
	}
	
	/**
	 * As this implementation can not do encoding it only accepts new path that is ancestor of current.
	 * Use SvnLogicalId for more complex manipulations.
	 */
	@Override
	public CmsItemId withRelPath(CmsItemPath newRelPath) {
		if (newRelPath == null) {
			return new CmsItemIdArg(toString(host, null, pegRev)); 
		}
		if (!newRelPath.isAncestorOf(getRelPath())) {
			throw new IllegalArgumentException("New path based on this CmsItemIdArg must be parent of /v/a b/c.xml");
		}
		for (String p = relpath; p.length() > 0; p = p.replaceFirst("/[^/]+$", "")) {
			if (getRelPath(p).equals(newRelPath)) {
				return new CmsItemIdArg(toString(host, p, pegRev));
			}
		}
		throw new RuntimeException("Failed to get encoded parent " + newRelPath + " from " + this);
	}

	@Override
	public CmsItemId withPegRev(Long newPegRev) {
		return new CmsItemIdArg(toString(host, relpath, newPegRev));
	}

	@Override
	public String toString() {
		if (isFullyQualified()) {
			return getLogicalIdFull();
		} else {
			return getLogicalId();
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof CmsItemId)) {
			return false;
		}
		CmsItemId o = (CmsItemId) obj;
		if (isFullyQualified()) {
			try {
				return getLogicalIdFull().equals(o.getLogicalIdFull());
			} catch (Exception e) {
				return false;
			}
		} else {
			if (!getLogicalId().equals(o.getLogicalId())) {
				return false;
			}
			try {
				if (o.getLogicalIdFull() != null && !getLogicalId().equals(o.getLogicalIdFull())) {
					return false;
				}
			} catch (Exception e) {
				// ignore
			}
			return true;
		}
	}

	@Override
	public int hashCode() {
		return getLogicalId().hashCode();
	}

}
