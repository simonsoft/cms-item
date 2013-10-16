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

import se.simonsoft.cms.item.impl.CmsItemIdEncoderBase;

/**
 * Controls access to {@link CmsItemPath} encoding, for {@link CmsItemIdEncoderBase}.
 * 
 * Can be deleted if we decide do make urlencoding in CmsRepository public.
 * 
 * If encoding is only done by instances that know of a CmsRepository, the type of repository can decide encoding rules.
 */
public abstract class PathEncoder {
	
	private CmsRepository repository;

	protected PathEncoder(CmsRepository repository) {
		this.repository = repository;
	}
	
	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 * @param path a valid path, file system style
	 * @return urlencoded UTF-8 except slashes
	 */
	protected String urlencode(CmsItemPath path) {
		StringBuffer enc = new StringBuffer();
		try {
			for (String p : path.getPathSegments()) {
				String es = repository.urlencodeSegment(p);
				if (es == null) {
					throw new AssertionError("urlencoding not implemented for repository " + repository); // guard against bad repository impls for example mocked repositories in tests
				}
				enc.append('/').append(es);
			}
		} catch (Exception e) {
			throw new RuntimeException("Url encoding failed for path " + path, e);
		}
		return enc.toString();
	}

	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 */
	protected String urldecode(String encodedPath) {
		return repository.urldecode(encodedPath);
	}

}