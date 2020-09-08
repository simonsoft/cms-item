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

/**
 * CMS task areas are ideally browsable by the user who creates them,
 * but for other users available only if the exact URL to a specific task folder is known.
 * 
 * These parent folders will only work as expected, secrecy etc,
 * if accompanied by matching access rules on the DAV server.
 */
@Deprecated
public interface TaskFolderParent {

	/**
	 * @return The URL for the <em>current user</em> where the area can be browsed or mounted
	 */
	String getUrlUserBrowse();
	
	/**
	 * @param secret part of the folder name, provides uniqueness
	 * @return a new folder, not created but unique and reserved, parent folder existing adn writable
	 */
	TaskFolder getNew(TaskSecret secret);
	
}
