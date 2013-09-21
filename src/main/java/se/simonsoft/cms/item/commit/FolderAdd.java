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

public class FolderAdd implements CmsItemPatch {

	private CmsItemPath path;
	private RepoRevision baseRevision;

	public FolderAdd(CmsItemPath path, RepoRevision baseRevisionForParent) {
		this.path = path;
		this.baseRevision = baseRevisionForParent;
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
	public String toString() {
		// add, no prop support yet, no copy support yet, folder
		return "A___" + getPath().getPath() + "/";
	}

}
