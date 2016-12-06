/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.Checksum;
import se.simonsoft.cms.item.Checksum.Algorithm;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public final class FileModification implements CmsPatchItem, CmsPatchItem.SupportsProp, CmsPatchItem.SupportsContentModification {

	private CmsItemPath path;
	private InputStream baseFile;
	private InputStream workingFile;
	private CmsItemProperties properties;
	private Checksum baseChecksum;

	/**
	 * @param pathInRepository see {@link CmsPatchItem#getPath()}
	 * @param baseFile, the original file that changes are based on, stream will be opened when item processing starts and closed afterwards
	 * @param workingFile, current contents, stream will be opened when item processing starts and closed afterwards
	 */
	public FileModification(CmsItemPath pathInRepository,
			InputStream baseFile, InputStream workingFile) {
		this.path = pathInRepository;
		this.baseFile = baseFile;
		this.workingFile = workingFile;
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
	public InputStream getBaseFile() {
		return baseFile;
	}

	@Override
	public InputStream getWorkingFile() {
		return workingFile;
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
		// modified, no prop support yet, no copy support yet
		return "M___" + getPath().getPath();
	}
	
	/**
	 * Set the base checksum to ensure that commit is performed against the correct base (i.e. server and client use the same base file).
	 * @param baseChecksum
	 */
	public void setBaseChecksum(Checksum baseChecksum) {
		if (!baseChecksum.has(Algorithm.MD5)){
			throw new IllegalArgumentException("base checksum must provide MD5" + baseChecksum);
		}
		this.baseChecksum = baseChecksum;
	}
	
	public Checksum getBaseChecksum() {
		return baseChecksum;
	}
}
