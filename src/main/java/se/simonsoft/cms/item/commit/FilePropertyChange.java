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

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Propset on an item with no other changes, see also for example {@link FileModification#getPropertyChange()}.
 * 
 * Svnkit SVNEditor differentiates between file property change and folder property change.
 */
public final class FilePropertyChange implements CmsPatchItem, CmsPatchItem.SupportsProp {

	private CmsItemPath path;
	private CmsItemProperties properties;

	/**
	 * @param path
	 * @param properties property changes as returned by {@link CmsItemProperties#getKeySet()} to be executed on the item, while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 */
	public FilePropertyChange(CmsItemPath path, CmsItemProperties properties) {
		this.path = path;
		this.properties = properties;
	}
	
	/**
	 * @param lock to allow commit to locked file, will be unlocked after commit
	 * @return instance for chaining
	 */
	public FileModification setLock(CmsItemLock lock) {
		throw new UnsupportedOperationException("Not implemented");
	}	
	
	/**
	 * @return property changes as returned by {@link CmsItemProperties#getKeySet()} to be executed on the item, while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 */
	@Override
	public CmsItemProperties getPropertyChange() {
		return properties;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

}
