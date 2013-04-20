/**
 * Copyright (C) 2009-2013 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.CmsItemId;

public class CmsLockQuery {

	private CmsItemId parent;
	private boolean userSame;
	private boolean recursive;
	private String messageGlob;
	
	public CmsItemId getParent() {
		return parent;
	}
	/**
	 * @param parent only get locks on or (if a folder) under this item
	 * @return for chaining
	 */
	public CmsLockQuery setParent(CmsItemId parent) {
		this.parent = parent;
		return this;
	}
	
	public boolean isUserSame() {
		return userSame;
	}
	public CmsLockQuery setUserSame(boolean userSame) {
		this.userSame = userSame;
		return this;
	}
	
	public boolean isRecursive() {
		return recursive;
	}
	public CmsLockQuery setRecursive(boolean recursive) {
		this.recursive = recursive;
		return this;
	}
	
	public String getMessageGlob() {
		return messageGlob;
	}
	
	/**
	 * currently only wildcard (asterisk) meaning 0+ arbitrary chars at beginnning OR end of string, wildcard can not be escaped so meaningful strings should not contain wildcard
	 * @param messageGlob 
	 */
	public CmsLockQuery setMessageGlob(String messageGlob) {
		this.messageGlob = messageGlob;
		return this;
	}
	
}
