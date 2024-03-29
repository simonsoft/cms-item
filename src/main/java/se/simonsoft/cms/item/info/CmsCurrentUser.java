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
package se.simonsoft.cms.item.info;

import java.util.Set;

/**
 * Abstraction providing information about the current user for the current request.
 *
 */
public interface CmsCurrentUser {
	
	/**
	 * @return the username
	 */
	public String getUsername();

	/**
	 * @return a comma-separated list of roles assigned to the user
	 */
	public String getUserRoles();

	/**
	 * @param expectedRoles a set of roles to query
	 * @return true if any of the expected roles have been assigned to the user
	 */
	public boolean hasRole(Set<String> expectedRoles);
}
