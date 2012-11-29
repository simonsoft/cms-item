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

public class FileAdd implements CmsCommitChange {

	private CmsItemPath path;
	private RepoRevision baseRevision;
	private InputStream contents;

	/**
	 * 
	 * @param path
	 * @param baseRevisionForParent
	 * @param contents Will be opened and closed when the item is processed
	 */
	public FileAdd(CmsItemPath path,
			RepoRevision parentFolderBaseRevision,
			InputStream contents) {
		this.path = path;
		this.baseRevision = parentFolderBaseRevision;
		this.contents = contents;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return baseRevision;
	}

	/**
	 * @return Not yet opened stream
	 */
	public InputStream getContents() {
		return contents;
	}

	@Override
	public String toString() {
		// add, no prop support yet, no copy support yet
		return "A___" + getPath().getPath();
	}

}
