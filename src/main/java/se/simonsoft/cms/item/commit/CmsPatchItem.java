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

import java.io.InputStream;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.properties.CmsItemProperties;

/**
 * A modification of contents and/or properties at a {@link CmsItemPath}
 * in a repository given by execution context.
 * 
 * Implementations should generally <code>final</code>, and conservatively maintained, 
 * so that backends can  safely do instanceof to identify operations.
 * 
 * Any modified behavior may cause existing backends to break the contract.
 * 
 * In other words, implement a WhateverOperation2 than to change WhateverOperation.
 */
public interface CmsPatchItem {

	/**
	 * @return the affected path, i.e. the target where the change will happen
	 */
	CmsItemPath getPath();
	
	/**
	 * Groups folder operations.
	 */
	interface TargetIsFolder extends CmsPatchItem {
	}
	
	/**
	 * For modifying properties on a resource.
	 */
	interface SupportsProp extends CmsPatchItem {
		
		/**
		 * @return null if no property changes, i.e. to keep existing
		 */
		public CmsItemProperties getPropertyChange();
		
	}

	/**
	 * For writing new content to the resource.
	 */
	interface SupportsContent extends CmsPatchItem {
		
		public InputStream getWorkingFile();
		
	}	
	
	/**
	 * Merge support requires a base, also minimizes the amount of data transferred.
	 */
	interface SupportsContentModification extends SupportsContent {
		
		public InputStream getBaseFile();
		
	}
	
	/**
	 * (maybe @)deprecated we should not impose this complexity on backends unless absolutely necessary
	 */
	interface SupportsIndividualBase extends CmsPatchItem {
		
		/**
		 * Important because all operations assume that they know the current state of the repository,
		 * but that state is always from a certain point in time (or actually a time range unless there is an exact revision number).
		 * @return the revision that changes are based on, used to check for conflicts with other changes
		 */
		RepoRevision getBaseRevision();		
		
	}
	
	/**
	 * Possibly, but not entirely, {@link FolderExist}.
	 */
	interface Idempotent extends CmsPatchItem {
		
	}
	
}
