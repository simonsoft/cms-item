/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.CmsItemLock;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public class FileModification implements CmsCommitChange {

	private CmsItemPath path;
	private RepoRevision baseRevision;
	private InputStream baseFile;
	private InputStream workingFile;
	private CmsItemProperties properties;
	private CmsItemLock lock = null;

	/**
	 * @param pathInRepository see {@link CmsCommitChange#getPath()}
	 * @param baseRevision the revision that changes are based on, used to check for conflicts with other changes
	 * @param baseFile, the original file that changes are based on, stream will be opened when item processing starts and closed afterwards
	 * @param workingFile, current contents, stream will be opened when item processing starts and closed afterwards
	 */
	public FileModification(CmsItemPath pathInRepository,
			RepoRevision baseRevision, InputStream baseFile, InputStream workingFile) {
		this.path = pathInRepository;
		this.baseRevision = baseRevision;
		this.baseFile = baseFile;
		this.workingFile = workingFile;
	}
	
	/**
	 * @param lock to allow commit to locked file, will be unlocked after commit
	 * @return instance for chaining
	 */
	public FileModification setLock(CmsItemLock lock) {
		this.lock  = lock;
		return this;
	}
	
	/**
	 * Could be an interface method unless we want the specific class for chaining.
	 * @param properties propset to be executed for {@link CmsItemProperties#getKeySet()} while other properties are left unchanged,
	 *  null value means delete the property, empty value means set or keep it but make it empty
	 * @return this instance for chaining
	 */
	public FileModification setPropertyChange(CmsItemProperties properties) {
		this.properties = properties;
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
	
	public InputStream getBaseFile() {
		return baseFile;
	}

	public InputStream getWorkingFile() {
		return workingFile;
	}
	
	/**
	 * @return null if no property changes
	 */
	public CmsItemProperties getPropertyChange() {
		return properties;
	}
	
	public CmsItemLock getLock() {
		return lock;
	}
	
	@Override
	public String toString() {
		// modified, no prop support yet, no copy support yet
		return "M___" + getPath().getPath();
	}
	
}
