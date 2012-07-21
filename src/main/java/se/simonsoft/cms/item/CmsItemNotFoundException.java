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


/**
 * Thrown when communication succeeds but the item is not found.
 * 
 * Declared as a checked exception although this is not really recoverable,
 * because the CMS is a multi-user environment where this type of error is normal
 * due to the time gap between user input and operation execution.
 * 
 * Services that rely on indexed data might want to retry a few times before
 * throwing this error as it may occur because of indexing delays.
 */
public class CmsItemNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	private CmsItemId id;

	public CmsItemNotFoundException(CmsItemId id) {
		super("Not found: " + id);
		this.id = id;
	}
	
	public CmsItemId getId() {
		return id;
	}

}
