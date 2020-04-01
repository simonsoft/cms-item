/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.structure;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Used to mark intent when calling this low level API,
 * for future refactoring and addition of rules.
 * Identical to {@link CmsAreaPattern}.
 */
public class CmsReleasesPattern extends CmsAreaPattern {

	/**
	 * Release pattern to use if not configured in the current repository.
	 */
	public static final String DEFAULT_RELEASES_PATTERN = "/*/release";
	
	public CmsReleasesPattern(String configValue)
			throws IllegalArgumentException {
		super(configValue);
	}

	/**
	 * See {@link CmsAreaPattern#getPathInside(CmsItemPath, String)} but
	 * note that this can <em>not</em> be used to predict the release path
	 * because profiling might affect the destination file name.
	 * 
	 * Currently the support for transforming with profile info is added in
	 * adapter code.
	 */
	@Override
	public CmsItemPath getPathInside(CmsItemPath master, String destinationLabel) {
		return super.getPathInside(master, destinationLabel);
	}

}
