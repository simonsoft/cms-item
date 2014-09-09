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
package se.simonsoft.cms.item.config;

/**
 * Represents a single configuration option.
 * By wrapping the value into a class we can extend the features in the future,
 * e.g. providing access to all inherited values (for "merging" values)
 * @author takesson
 *
 */
public interface CmsConfigOption {

	
	/**
	 * @return the name/key for the config option
	 */
	public String getName();
	
	/**
	 * @return the value of the config option as String, where inherited properties are resolved by override.
	 */
	public String getValueString();
	
	/**
	 * @return the value of the config option as Boolean, where case-insensitive and trimmed "true" becomes True. 
	 */
	public Boolean getValueBoolean();
}
