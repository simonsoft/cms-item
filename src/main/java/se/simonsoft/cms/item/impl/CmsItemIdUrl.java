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

import java.net.URLEncoder;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;

/**
 * URL-only item id with no support for logical IDs,
 * good for webapp use like Repos "target" arguments.
 * 
 * Can not support {@link #getLogicalId()} and {@link #getLogicalIdFull()}.
 * 
 * For operations having access to a logical ID,
 * use {@link CmsItemIdArg} instead.
 */
public class CmsItemIdUrl implements CmsItemId {

	public static final String URLENCODE_ENCODING = "UTF-8";
	
	private CmsItemPath path;
	private CmsRepository repository;
	private Long pegRev;

	public CmsItemIdUrl(CmsRepository repository, CmsItemPath targetPath) {
		this(repository, targetPath, null);
	}
	
	public CmsItemIdUrl(CmsRepository repository, CmsItemPath targetPath, Long pegRev) {
		this.repository = repository;
		this.path = targetPath;
		this.pegRev = pegRev;
	}

	@Override
	public String getLogicalIdFull() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Logical ID requested for URL-only item id " + getUrl());
	}
	
	@Override
	public String getLogicalId() throws UnsupportedOperationException {
		throw new UnsupportedOperationException("Logical ID requested for URL-only item id " + getUrl());
	}

	@Override
	public CmsItemPath getRelPath() {
		return path;
	}
	
	@Override
	public String getUrl() {
		return getRepositoryUrl() + urlencode(path);
	}

	@Override
	public String getUrlAtHost() {
		return null;
	}
	
	@Override
	public Long getPegRev() {
		return pegRev;
	}

	@Override
	public CmsRepository getRepository() {
		return repository;
	}

	@Override
	public String getRepositoryUrl() {
		return repository.getUrl();
	}

	@Override
	public CmsItemId withRelPath(CmsItemPath newRelPath) {
		return new CmsItemIdUrl(repository, newRelPath);
	}

	@Override
	public CmsItemId withPegRev(Long newPegRev) {
		return new CmsItemIdUrl(repository, path, newPegRev);
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null
				&& obj instanceof CmsItemIdUrl
				&& equalsId((CmsItemIdUrl) obj);
	}

	private boolean equalsId(CmsItemIdUrl id) {
		if (!repository.equals(id.getRepository())) return false;
		if (!path.equals(id.getRelPath())) return false;
		if (pegRev == null) {
			return id.pegRev == null;
		} else {
			return pegRev.equals(id.getPegRev());
		}
	}

	/**
	 * @param path a valid path, file system style
	 * @return urlencoded UTF-8 except slashes
	 */
	static String urlencode(CmsItemPath path) {
		StringBuffer enc = new StringBuffer();
		try {
			for (String p : path.getPathSegments()) {
				enc.append('/').append(URLEncoder.encode(p, URLENCODE_ENCODING));
			}
		} catch (Exception e) {
			throw new RuntimeException("Url encoding " + URLENCODE_ENCODING + " failed for " + path);
		}
		return enc.toString();
	}
	
}
