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

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Wraps an actual change with an instruction that there should be
 * an existing folder at the target path, which should be replaced.
 * 
 * Should we REALLY do SupportsIndividualBase here? Use case?
 */
public final class FolderReplace implements CmsPatchItem.TargetIsFolder {

	private CmsPatchItem replacement;

	protected FolderReplace(CmsPatchItem replacement) {
		this.replacement = replacement;
	}
	
	//public FolderReplace(FolderCopy add) {
	//	this((CmsCommitChange) add);
	//}
	
	@Override
	public CmsItemPath getPath() {
		return replacement.getPath();
	}
	
	public CmsPatchItem getReplacement() {
		return replacement;
	}

}
