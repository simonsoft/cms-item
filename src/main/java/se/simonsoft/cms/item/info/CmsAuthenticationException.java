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

/**
 * Error thrown if request fails due to authentication or authorization which is sometimes difficult to distinguish.
 * It might in the future make sense to subclass this exception either depending on the source of the 
 * exception or to make Authorization issue more specific 
 *
 */
public class CmsAuthenticationException extends RuntimeException {
	
	// TODO: Would it make sense with a flag indicating if issue is authn or authz?
	
	private static final long serialVersionUID = 1L;

	public CmsAuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

}
