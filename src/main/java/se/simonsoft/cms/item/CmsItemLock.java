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
package se.simonsoft.cms.item;

import java.lang.management.LockInfo;
import java.util.Date;

import se.simonsoft.cms.item.info.CmsLocking;

/**
 * Ideas on locking, from CmsItem#getLock.
 * API mimics svn_lock_t struct but does not have a path because to us the relation is from item to lock,
 * and abstractions may allow many items to share the same lock.
 */
public interface CmsItemLock {

	String getComment();

	Date getDateCreation();
	
	Date getDateExpiration();
	
	/**
	 * @return the lock token
	 */
	String getID();

	/**
	 * @return username
	 */
	String getOwner(); 
	
}
