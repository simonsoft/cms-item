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

public final class FolderDelete implements CmsPatchItem.TargetIsFolder, CmsPatchItem.SupportsIndividualBase {

	private CmsItemPath path;
	private RepoRevision base;

	public FolderDelete(CmsItemPath path) {
		this(path, null);
	}
	
	@Deprecated // use patchset base revision
	public FolderDelete(CmsItemPath path, RepoRevision base) {
		this.path = path;
		this.base = base;
	}
	
	@Override
	public CmsItemPath getPath() {
		return path;
	}

	@Override
	public RepoRevision getBaseRevision() {
		return base;
	}

}
