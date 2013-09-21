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

import java.io.InputStream;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public class FileAdd implements CmsPatchItem, CmsPatchItem.SupportsProp, CmsPatchItem.SupportsContent, CmsPatchItem.SupportsIndividualBase {

	private CmsItemPath path;
	private RepoRevision baseRevision;
	private InputStream contents;
	private CmsItemProperties properties = null;

	/**
	 * 
	 * @param path
	 * @param contents Will be opened and closed when the item is processed
	 */	
	public FileAdd(CmsItemPath path, InputStream contents) {
		this(path, null, contents);
	}	
	
	@Deprecated //"base revision should be set in Changeset"	
	public FileAdd(CmsItemPath path,
			RepoRevision parentFolderBaseRevision,
			InputStream contents) {
		this.path = path;
		this.baseRevision = parentFolderBaseRevision;
		this.contents = contents;
	}
	
	/**
	 * Could be an interface method unless we want the specific class for chaining.
	 * @param properties propset to be executed for {@link CmsItemProperties#getKeySet()} while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 * @return this instance for chaining
	 */
	public CmsPatchItem setPropertyChange(CmsItemProperties properties) {
		this.properties  = properties;
		return this;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return baseRevision;
	}

	@Override
	public InputStream getWorkingFile() {
		return getContents();
	}
	
	/**
	 * @return Not yet opened stream
	 */
	public InputStream getContents() {
		return contents;
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
		return "A" + (getPropertyChange() == null ? '_' : 'M') + "__" + getPath().getPath();
	}

}
