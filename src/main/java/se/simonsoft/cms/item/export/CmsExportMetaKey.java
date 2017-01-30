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
package se.simonsoft.cms.item.export;

/**
 * The name of a single metadata item.
 *
 */
public class CmsExportMetaKey {

	private final String key;
	
	public CmsExportMetaKey(String key) {
		
		// TODO Validate null.
		// TODO Validate key with regex, see CmsItemArg.
		
		this.key = key;
	}

	public String getKey() {
		
		return this.key;
	}
	
	@Override
	public boolean equals(Object anObject) {
		return this.key.equals(anObject);
	}
	
	@Override
	public int hashCode() {
		return this.key.hashCode();
	}
	
	@Override
	public String toString() {
		return this.key.toString();
	}
	
}
