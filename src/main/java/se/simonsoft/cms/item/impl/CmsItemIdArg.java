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
package se.simonsoft.cms.item.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

/**
 * Concrete class for use in the common case when a valid logical id
 * is known from metadata etc and provided as string argument to an operation.
 * 
 * Mutable: allows adding of hostname if not already set.
 * 
 * Provides a String argument constructor for use as JAX-RS parameter.
 * 
 */
public class CmsItemIdArg extends CmsItemIdBase { 
	// Difficult to extend CmsItemIdEncoderBase due to constructor CmsItemIdArg(String logicalId).
	// Could move the pattern matching to CmsItemIdBase

	// TODO before adding more validation here make sure it can be shared
	// between SvnLogicalId and other implementations.
	// This class could have some basic validation in the constructor
	// but since it is used to process input incrementally
	// it needs an explicit validate() method for semantic validation.
	
	public static final String DEFAULT_PROTOCOL = "http";
	public static final String PEG = "?p=";
	public static final Pattern NICE = Pattern.compile(PROTO_PREFIX + "([^/]*)(.*/)([^:^]*)\\^(/|[^:?#]+[^/])/?(?:\\?p=(\\d+))?");
	
	/**
	 * Root path is not represented in CmsItemPath so we need to define
	 * what corresponds to relpath null.
	 * Single slash is consistent with how SvnLogicalId represents repo root id,
	 * though it is inconsistent with other paths as it ends with slash.
	 */
	public static final String REPO_ROOT_PATH = "/";
	
	private String relpathEncoded; // the path part of the logical ID
	private CmsItemPath relpathDecoded;
	private Long pegRev = null;
	private boolean orgfull;
	private boolean orgpeg;
	private CmsRepository repository;
	
	/**
	 * This empty constructor only exist to please kryo de-serialization which needs an no arg constructor
	 * Do not initialize with this constructor
	 */
	@SuppressWarnings("unused")
	private CmsItemIdArg() {}
	
	/**
	 * @param logicalId with or without hostname and peg rev
	 */
	public CmsItemIdArg(String logicalId) {
		Matcher m = NICE.matcher(logicalId);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid logical id: " + logicalId);
		}
		String host = m.group(1);
		String parent = m.group(2);
		String repo = m.group(3);
		if (host.length() == 0) {
			this.repository = new CmsRepository(parent.substring(0, parent.length() - 1), repo);
		} else {
			this.repository = new CmsRepository(DEFAULT_PROTOCOL, host, parent.substring(0, parent.length() - 1), repo);
			this.orgfull = true;
		}
		setRelPathEncoded(m.group(4), repository); // this is obviously not a configured repository so we'll get default encoding
		if (m.group(5) != null) {
			this.pegRev  = Long.parseLong(m.group(5));
			this.orgpeg = true;
		}
	}
	
	/**
	 * Preferrably use {@link CmsRepository#getItemId()} instead.
	 */
	public CmsItemIdArg(CmsRepository repository) {
		this(repository, null, null);
	}	

	/**
	 * Preferrably use {@link CmsRepository#getItemId()} with {@link CmsItemId#withRelPath(CmsItemPath)} instead.
	 */
	public CmsItemIdArg(CmsRepository repository, CmsItemPath itemPath) {
		this(repository, itemPath, null);
	}
	
	/**
	 * Preferrably use {@link CmsRepository#getItemId()} with {@link CmsItemId#withRelPath(CmsItemPath)} and {@link CmsItemId#withPegRev(Long)} instead.
	 */
	public CmsItemIdArg(CmsRepository repository, CmsItemPath itemPath, Long pegRev) {
		this.repository = repository;
		if (itemPath == null) {
			this.relpathDecoded = null; //Root path should be represented with null value in CmsItemId
			this.relpathEncoded = REPO_ROOT_PATH;
		} else {
			this.relpathDecoded = itemPath;
			this.relpathEncoded = repository.urlencode(relpathDecoded);
		}
		this.pegRev = pegRev;
	}


	
/*
	protected CmsItemIdArg(CmsRepository repository, CmsItemIdRelpathEncoded relpathEncoded, Long pegRev) {
		this.repository = repository;
		this.orgfull = repository.isHostKnown();
		if (relpathEncoded == null) {
			throw new IllegalArgumentException("relpath can be " + REPO_ROOT_PATH + " but not null");
		}
		setRelPathEncoded(relpathEncoded.getRelpathEncoded(), repository);
		this.pegRev = pegRev;
		this.orgpeg = pegRev != null;
	}
*/

	private void setRelPathEncoded(String relpathEncoded, CmsRepository repository) {
		if (this.relpathEncoded != null) {
			throw new IllegalStateException("Instance should not be mutable");
			// does not guard against making a repository id point to an item in the repo.
		}
		this.relpathEncoded = relpathEncoded; // REMOVE after testing
		this.relpathDecoded = getRelPath(relpathEncoded, repository);
		//this.relpathEncoded = repository.urlencode(relpathDecoded);
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
	
	
	/**
	 * @param fullyQualifiedName Often set by the web application and not the caller
	 * when the caller is on the same server, including port if non-standard
	 */
	public void setHostname(String fullyQualifiedName) {
		if (repository.isHostKnown()) {
			throw new IllegalStateException("Hostname already set for " + getLogicalIdFull());
		}
		if (fullyQualifiedName == null || fullyQualifiedName.length() == 0) {
			throw new IllegalArgumentException("Method not intended for removing hostname");
		}
		repository = new CmsRepository(DEFAULT_PROTOCOL, fullyQualifiedName, repository.getParentPath(), repository.getName());
	}
	
	/**
	 * Makes sure hostname is set and matching a given value.
	 * @param fullyQualifiedName hostname to set or to expect
	 * @throws IllegalArgumentException If the hostname of the logicalId does not match the given
	 */
	public void setHostnameOrValidate(String fullyQualifiedName)
			throws IllegalArgumentException {
		if (repository.isHostKnown()) {
			if (!repository.getHost().equals(fullyQualifiedName))  {
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
		if (!repository.isHostKnown()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}		
		return getRepository().getUrl();
	}
	
	@Override
	public CmsRepository getRepository() {
		return repository;
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
		if (REPO_ROOT_PATH.equals(relpathEncoded)) {
			return getRepository().getUrl();
		}
		return getRepository().getUrl() + relpathEncoded;
	}
	
	@Override
	public String getUrlAtHost() {
		return getRepository().getUrlAtHost() + relpathEncoded;
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
		return getLogicalId("");
	}

	@Override
	public String getLogicalIdFull() {
		if (!repository.isHostKnown()) {
			throw new IllegalStateException("Hostname unknown for " + getLogicalId());
		}
		return getLogicalId(repository.getHost());
	}
	
	protected String getLogicalId(String anyHost) {
		return PROTO_PREFIX
				+ anyHost
				+ repository.getParentPath() + "/" + repository.getName() + "^" 
				+ relpathEncoded
				+ getQuery(pegRev);		
	}

	@Override
	public CmsItemPath getRelPath() {
		return relpathDecoded;
	}
	
	
	private String getQuery(Long anyPegRev) {
		if (anyPegRev != null) {
			return PEG + anyPegRev;
		}
		return "";
	}
	
	private static CmsItemPath getRelPath(String relpathEncoded, CmsRepository repository) {
		if (REPO_ROOT_PATH.equals(relpathEncoded)) {
			return null;
		}
		String decoded = new CmsItemIdEncoderBase.Encoder(repository).decode(relpathEncoded);
		return new CmsItemPath(decoded);
	}
	
	/**
	 * As this implementation can not do encoding it only accepts new path that is ancestor of current.
	 * Use SvnLogicalId for more complex manipulations.
	 */
	@Override
	public CmsItemId withRelPath(CmsItemPath newRelPath) {
		
		return new CmsItemIdArg(this.repository, newRelPath, pegRev);
	}

	@Override
	public CmsItemId withPegRev(Long newPegRev) {
		return new CmsItemIdArg(this.repository, this.relpathDecoded, newPegRev);
	}

	@Override
	public String toString() {
		if (repository.isHostKnown()) {
			return getLogicalIdFull();
		} else {
			return getLogicalId();
		}
	}
	
	// TODO: Remove?
	class CmsItemIdRelpathEncoded {
		
		private String relpathEncoded;
		
		CmsItemIdRelpathEncoded(String relpathEncoded) {
			
			if (relpathEncoded == null) {
				throw new IllegalArgumentException("relpath can be " + REPO_ROOT_PATH + " but not null");
			}
			this.relpathEncoded = relpathEncoded;
		}

		public String getRelpathEncoded() {
			return relpathEncoded;
		}
			
		
	}

}
