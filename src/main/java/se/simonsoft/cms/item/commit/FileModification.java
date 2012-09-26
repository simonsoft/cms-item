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

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;

public class FileModification implements CmsCommitChangesetItem {

	private CmsItemPath path;
	private RepoRevision baseRevision;
	private InputStream baseFile;
	private InputStream workingFile;

	/**
	 * @param pathInRepository see {@link CmsCommitChangesetItem#getPath()}
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
	
}
