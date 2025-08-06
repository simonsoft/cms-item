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
package se.simonsoft.cms.item;

import java.util.Date;
import java.util.UUID;

/**
 * API mimics svn_lock_t struct but does not have a path because to us the relation is from item/path to lock.
 */
public interface CmsItemLock {

	String getComment();

	Date getDateCreation();
	
	/**
	 * @return many subversion clients don't support this
	 */
	Date getDateExpiration();
	
	/**
	 * @return the lock token (may contain a prefix in addition to UUID)
	 */
	String getToken();
	
	/**
	 * @return the UUID part of the token
	 */
	UUID getTokenUUID();

	/**
	 * @return username
	 */
	String getOwner();

	/**
	 * @return itemId
	 */
	CmsItemId getItemId(); 
	
}
