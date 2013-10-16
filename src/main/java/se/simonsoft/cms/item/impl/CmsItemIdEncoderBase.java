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

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.PathEncoder;

/**
 * For now supporting encoding used in {@link #getUrl()} and {@link #getUrlAtHost()} but not {@link #getLogicalId()}.
 */
public abstract class CmsItemIdEncoderBase extends CmsItemIdBase {

	private Encoder encoder;
	
	public CmsItemIdEncoderBase(CmsRepository repository) {
		this.encoder = new Encoder(repository);
	}

	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 * @param path a valid path, file system style
	 * @return urlencoded UTF-8 except slashes
	 */
	protected String urlencode(CmsItemPath path) {
		return encoder.encode(path);
	}
	
	/**
	 * Helper for one way conversion, not normative. See SvnLogicalId for encoding rules.
	 */
	protected String urldecode(String encodedPath) {
		return encoder.decode(encodedPath);
	}
	
	static class Encoder extends PathEncoder {

		public Encoder(CmsRepository repository) {
			super(repository);
		}

		private String encode(CmsItemPath path) {
			return super.urlencode(path);
		}

		/**
		 * Package visible because needed from CmsItemIdArg to keep the old feature set.
		 */
		String decode(String encodedPath) {
			return super.urldecode(encodedPath);
		}
		
	}
	
}
