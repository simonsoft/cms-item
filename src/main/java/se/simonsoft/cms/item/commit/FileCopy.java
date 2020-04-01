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
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public final class FileCopy implements CmsPatchItem, CmsPatchItem.SupportsProp {

	private CmsItemPath fromPath;
	private RepoRevision fromRev;
	private CmsItemPath toPath;
	private CmsItemProperties properties = null;

	/**
	 * 
	 * @param fromPath
	 * @param fromRev Support copy historical.
	 *  In unversioned backend we would validate that the file timestamp matches this timestamp,
	 *  so the operation is not mistakenly done on a file that someone else has just modified.
	 *  This would be the same as checkint that a file is up to date before committing. 
	 * @param toPath
	 */
	public FileCopy(CmsItemPath fromPath, RepoRevision fromRev, CmsItemPath pathto) {
		if (fromRev == null) {
			throw new IllegalArgumentException("HEAD as copy from revision is currently not supported");
		}
		this.fromPath = fromPath;
		this.fromRev = fromRev;
		this.toPath = pathto;
	}

	/**
	 * Could be an interface method unless we want the specific class for chaining.
	 * @param properties propset to be executed for {@link CmsItemProperties#getKeySet()} while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 * @return this instance for chaining
	 */
	public CmsPatchItem setPropertyChange(CmsItemProperties properties) {
		this.properties = properties;
		return this;
	}
	
	/**
	 * Alias to {@link #getFromPath()} that shows up in autocomplete when you do getPath.
	 * @return original path
	 */
	public CmsItemPath getPathFrom() {
		return fromPath;
	}
	
	public CmsItemPath getFromPath() {
		return fromPath;
	}
	
	public RepoRevision getFromRev() {
		return fromRev;
	}
	
	@Override
	public CmsItemPath getPath() {
		return toPath;
	}

	/**
	 * @return null if no property changes
	 */
	@Override
	public CmsItemProperties getPropertyChange() {
		return properties;
	}

	@Override
	public String toString() {
		// add, no prop support yet, no copy support yet
		return "A" + (getPropertyChange() == null ? '_' : 'M') + "+_" + getPath().getPath();
	}	
	
}
