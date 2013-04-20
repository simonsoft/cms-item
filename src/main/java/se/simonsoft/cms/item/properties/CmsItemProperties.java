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
package se.simonsoft.cms.item.properties;

import java.util.List;
import java.util.Set;

/**
 * Read-only incarnation of {@link SvnPropertyMap}.
 * 
 * The modification and storage behavior of the SvnProperty* classes
 * is still undefined outside adapter code.
 * 
 * Instances should be safe to keep in memory for a long time,
 * i.e. can not keep an open connection to repository.
 */
public interface CmsItemProperties {

	/**
	 * @param key Property name
	 * @return true if the property is set on the item
	 */
	boolean containsProperty(String key);

	/**
	 * @return Names of all properties that are set on the item
	 * TODO includes binary properties?
	 */
	Set<String> getKeySet();
	
	/**
	 * @param key Property name
	 * @return The value of the property or null if not set
	 *  TODO parse error or null if binary?
	 */
	String getString(String key);

	/**
	 * Parses value to a list of strings, check {@link #containsProperty(String)} first.
	 * @param key Property name
	 * @return Property value parsed to list of values, null on parse error or if not set
	 * @throws ClassCastException If a list value is not string (?)
	 */
	List<String> getList(String key) throws ClassCastException;

}