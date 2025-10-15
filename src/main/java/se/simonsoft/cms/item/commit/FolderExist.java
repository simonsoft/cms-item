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
package se.simonsoft.cms.item.commit;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Create folder and parent folders only if they didn't exist already.
 */
public final class FolderExist implements CmsPatchItem.TargetIsFolder, CmsPatchItem.SupportsProp {

	private CmsItemPath path;
	private CmsItemProperties properties;

	public FolderExist(CmsItemPath path) {
		this(path, null);
	}
	
	public FolderExist(CmsItemPath path, CmsItemProperties properties) {
		this.path = path;
		this.properties = properties;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public String toString() {
		// exists, extending the svn stat syntax
		char propMod = (getPropertyChange() == null) ? '_' : 'M';
		return "E" + propMod +"__" + getPath().getPath() + "/";
	}
	
	@Override
	public CmsItemProperties getPropertyChange() {
		return properties;
	}
	
}
