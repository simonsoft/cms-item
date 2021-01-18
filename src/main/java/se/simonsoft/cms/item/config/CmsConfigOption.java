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
package se.simonsoft.cms.item.config;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Represents a single configuration option.
 * By wrapping the value into a class we can extend the features in the future,
 * e.g. providing access to all inherited values (for "merging" values)
 * @author takesson
 *
 */
public interface CmsConfigOption {

	/**
	 * Regex pattern matching config list separators useful when splitting.
	 */
	public static final Pattern LIST = Pattern.compile("[|\\s,]+");
	
	/**
	 * Internal really, it should be safe to consider {@link #getKey()} unique across namespaces for now.
	 * @return the namespace, probably same for all options
	 */
	public String getNamespace();
	
	/**
	 * @return the name/key for the config option, excluding any namespace
	 */
	public String getKey();
	
	/**
	 * @return the value of the config option as String, trimmed. Inherited properties are resolved by override.
	 */
	public String getValueString();
	
	/**
	 * @return the value of the config option as Boolean, where case-insensitive and trimmed "true" becomes True. 
	 */
	public Boolean getValueBoolean();
	
	/**
	 * @return the value of the config option as List. A String value will be split on the pipe character and each element trimmed. 
	 */
	public List<String> getValueList();	

}
