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
package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Create folder and parent folders only if they didn't exist already.
 */
public class FolderExist implements CmsPatchItem {

	private CmsItemPath path;

	public FolderExist(CmsItemPath path) {
		this.path = path;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	/**
	 * Could be an interface method unless we want the specific class for chaining.
	 * @param properties property change to do on items that are created, but not existing items
	 * @return this instance for chaining
	 */
	public CmsPatchItem setPropertyChange(CmsItemProperties properties) {
		throw new UnsupportedOperationException("not implemented"); // should we have an onCreate callback, and what sync issues would that introduce?
	}
	
	/**
	 * @return null, this change does by design check current state and thus can't use a base revision 
	 */
	@Override
	public RepoRevision getBaseRevision() {
		return null;
	}
	
}
