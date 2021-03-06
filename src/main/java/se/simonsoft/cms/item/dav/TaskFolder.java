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
package se.simonsoft.cms.item.dav;

import java.util.Date;

import se.simonsoft.cms.item.CmsItemPath;

/**
 * Represents a folder created for a specific task,
 * which users access using a URL, {@link #getUrl()},
 * possibly a "secret URL".
 * 
 * The back end uses a relative path, {@link #getRelPath()},
 * and maps this to back end storage using configuration info.
 * 
 * No actual local paths should be exposed.
 */
@Deprecated
public interface TaskFolder {

	/**
	 * @return The user's URL to this area
	 */
	String getUrl();
	
	/**
	 * @return The path from DAV root to the folder
	 */
	CmsItemPath getRelPath();
	
	/**
	 * @return The timestamp after which the share will be automatically deleted, null if never
	 */
	Date getExpires();
	
}
