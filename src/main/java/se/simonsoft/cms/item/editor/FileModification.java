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
package se.simonsoft.cms.item.editor;

import java.io.File;

import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.properties.CmsItemProperties;

public class FileModification {

	public FileModification(CmsItemPath pathInRepository,
			long baseRevision, File baseFile, File workingFile) {
		
	}
	
	public FileModification setPropsets(CmsItemProperties propertyChanges) {
		// TODO svn 1.7 supports line based prop diff, we should too
		return this;
	}
	
}
