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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;

/**
 * Same equals and hashCode for different impls, important for collections etc.
 */
public abstract class CmsItemIdBase implements CmsItemId {

	public static final String URLENCODE_ENCODING = "UTF-8";
	
	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 * @param path a valid path, file system style
	 * @return urlencoded UTF-8 except slashes
	 */
	protected String urlencode(CmsItemPath path) {
		StringBuffer enc = new StringBuffer();
		try {
			for (String p : path.getPathSegments()) {
				enc.append('/').append(URLEncoder.encode(p, URLENCODE_ENCODING).replace("+", "%20"));
			}
		} catch (Exception e) {
			throw new RuntimeException("Url encoding " + URLENCODE_ENCODING + " failed for path " + path);
		}
		return enc.toString();
	}

	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 */	
	protected String urldecode(String encodedPath) {
		try {
			return URLDecoder.decode(encodedPath, URLENCODE_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Decoding to " + URLENCODE_ENCODING + " failed for path: " + encodedPath);
		}
	}
	
	@Override
	public final boolean equals(Object obj) {
		return obj != null
				&& obj instanceof CmsItemId
				&& equalsId((CmsItemId) obj);
	}

	private final boolean equalsId(CmsItemId id) {
		if (!getRepository().equals(id.getRepository())) return false;
		if (getRelPath() == null) {
			if (id.getRelPath() != null) return false;
		} else if (!getRelPath().equals(id.getRelPath())) return false;
		if (getPegRev() == null) {
			return id.getPegRev() == null;
		} else {
			return getPegRev().equals(id.getPegRev());
		}
	}

	@Override
	public int hashCode() {
		return (getRepository().getPath() + getRelPath() + getPegRev()).hashCode();
	}

}