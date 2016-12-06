/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

/**
 * Used instead of String to identify CMS items (a.k.a. objects/entries/files or folders).
 * <p>
 * Preferred instantiation is through {@link CmsRepository#getItemId()}
 * followed by {@link #withRelPath(CmsItemPath)} and {@link #withPegRev(Long)}.
 * <p>
 * The interface methods should return the <em>persistent</em> form of id,
 * typically location and revision -- never parameters for transformation etc.
 * <p>
 * Typically immutable as the different getters provides identifiers in different contexts.
 * <p>
 * Facilitates mocking when unit testing methods that deal with CMS paths and URLs.
 * <p>
 * Can <em>not</em> be used to distinguish between files and folders.
 * The rationale for this is that these ids often come as string input from user operations,
 * where there is no such metadata and a convention on trailing slash is difficult to enforce.
 * Full item information is provided through {@link CmsItem}, for example from {@link CmsItemLookup}.
 * Services that return lists of ids may instead provide method variants returning only files,
 * only folders and both.
 */
public interface CmsItemId {
	
	/**
	 * Default {@link #getLogicalId()} protocol, unless a backend impl specifies otherwise (which we don't have the architecture for now).
	 */
	static final String PROTO = "x-svn";
	/**
	 * Shared across implementations.
	 */
	static final String PROTO_PREFIX = PROTO + "://";
	
	/**
	 * Resource URL, no query string. Not even revision number.
	 * If revision ({@link #getPegRev()}) is specified this is the URL at that revision (i.e. "peg").
	 * If protocol is not known, always assumes "http" as CMS servers should be capable of redirecting.
	 * If there are implementations that may return "https" it should be clearly stated (and maybe reconsidered),
	 * as http is always used internally on servers.
	 * @return resource URL, encoded, encoding based on UTF-8 bytes for non-ascii,
	 *  always without trailing slash so when known to be a folder slash should be appended to avoid redirect
	 */
	String getUrl();
	
	/**
	 * Returns URL used in UI operations on the same host, a "URI reference".
	 * For contexts where it is validated or guaranteed that all items are from the same host.
	 * Unlike {@link #getUrl()} all impls must always support this, including when protocol or host is not known.
	 * @return Resource URL from server root starting with slash, encoded, excluding query string
	 */
	String getUrlAtHost();
	
	/**
	 * The exact revision, if specified, i.e. never HEAD or date or anything but the integer.
	 * @return revision number, or null for revision not specified (HEAD in subversion terminology)
	 */
	Long getPegRev();
	
	/**
	 * Local id without hostname, unique within the context of a connection to a server.
	 * Either constructed by backend specific impls, or merely transferred using a generic {@link se.simonsoft.cms.item.impl.CmsItemIdArg} impl.
	 * Note that the future definition of persistent may include fragment identifiers and transforms.
	 * @return logical id, including persistent parameters, i.e. peg rev, but not including actions etc.
	 */
	String getLogicalId();

	/**
	 * Hybrid between persistent id and URL-like id with hostname.
	 * @return logical id as from {@link #getLogicalId()} but with fully qualified server name,
	 *  stripping any other non-persistent info like transforms
	 */
	String getLogicalIdFull();

	/**
	 * @return path in repository
	 */
	CmsItemPath getRelPath();
	
	/**
	 * @return repository root, toString is URL without trailing slash, encoded
	 */
	CmsRepository getRepository();
	
	/**
	 * @return repository root URL without trailing slash, encoded
	 * @deprecated Use {@link #getRepository()}.getUrl()
	 */
	String getRepositoryUrl();
	
	/**
	 * Derives ID with new relative path from repository root.
	 * @param newRelPath Any rel path, null to get repository root ID
	 * @return Same ID but with new relative path
	 */
	CmsItemId withRelPath(CmsItemPath newRelPath);
	
	/**
	 * Derives ID with new pegRev.
	 * @param newPegRev Any peg revision, null to make ID represent HEAD.
	 * @return Same ID but with new peg revision
	 */
	CmsItemId withPegRev(Long newPegRev);
	
	/* Implemented in CmsItemIdFragment
	CmsItemId withBaselineRev(Long baselineRev);
	*/

}
