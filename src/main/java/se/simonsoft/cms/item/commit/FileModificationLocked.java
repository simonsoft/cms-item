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

import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * Overwrites existing resource at the path without need for a base,
 * but only if a matching {@link CmsPatchset#addLock(se.simonsoft.cms.item.CmsItemLock)} has been given.
 * Currently defers base processing and uploads the whole content stream to server. 
 * Likely an insignificant performance difference when executing close to the server.
 */
public final class FileModificationLocked implements CmsPatchItem, CmsPatchItem.SupportsProp, CmsPatchItem.SupportsContentModification {

	private static final Logger logger = LoggerFactory.getLogger(FileModificationLocked.class);
	
	private final CmsItemPath path;
	private InputStream contents = null;
	private Provider<InputStream> contentsProvider = null;
	private InputStream baseFile;
	private CmsItemProperties properties;
	
	/**
	 * @param pathInRepository see {@link CmsPatchItem#getPath()}
	 * @param workingFile, current contents, stream will be opened when item processing starts and closed afterwards
	 */
	public FileModificationLocked(CmsItemPath pathInRepository, InputStream workingFile) {
		this.path = pathInRepository;
		this.contents = workingFile;
		
		// Would require some lookup implementation in order to get base.
		//TODO: this.baseFile = ??
	}
	
	public FileModificationLocked(CmsItemPath path, Provider<InputStream> contentsProvider) {
		this.path = path;
		this.contentsProvider = contentsProvider;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public InputStream getWorkingFile() {
		return getContents();
	}
	
	/**
	 * @return Not yet opened stream
	 */
	public InputStream getContents() {
		if (contents != null) {
			return contents;
		} else {
			logger.debug("Providing InputStream for FileModificationLocked: " + path);
			return contentsProvider.get();
		}
	}

	public void setBaseFile(InputStream baseFile) {
		this.baseFile = baseFile;
	}
	
	@Override
	public InputStream getBaseFile() {
		return baseFile;
	}
	
	public void setPropertyChange(CmsItemProperties properties) {
		this.properties = properties;
	}

	@Override
	public CmsItemProperties getPropertyChange() {
		return properties;
	}
	
	
	
}
