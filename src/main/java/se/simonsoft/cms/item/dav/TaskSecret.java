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
package se.simonsoft.cms.item.dav;

import java.util.Date;

/**
 * Provides a secret for creating new task folders.
 *
 * Commonly expected to have enough randomness to make
 * it impossible to guess the URL without listing parent folder contents
 * or spying on requests.
 */
public interface TaskSecret {

	/**
	 * @return A name prefix to group the folders, such as an activity name.
	 */
	String getPrefix();
	
	/**
	 * @return The random string that makes the URL secret.
	 */
	String getSecret();
	
	/**
	 * @return The timestamp at creation, normally currentTimeMillis
	 */
	Date getCreation();

	/**
	 * @return The timestamp after which the task folder should be deleted
	 */
	Date getExpiry();
	
}
