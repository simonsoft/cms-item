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
package se.simonsoft.cms.item.impl;

import java.util.List;
import se.simonsoft.cms.item.config.CmsConfigOption;

/**
 *
 * @author Markus Mattsson
 * @param <T>
 */
public class CmsConfigOptionBase<T> implements CmsConfigOption {

	protected final String namespace;
	protected final String key;
	protected final T value;

	public CmsConfigOptionBase(String key, T value) {
		String[] nameSpaceAndKey = extractNamespace(key);
		this.namespace = nameSpaceAndKey.length > 1 ? nameSpaceAndKey[0] : null;
		this.key = nameSpaceAndKey.length > 1 ? nameSpaceAndKey[1] : key;
		this.value = value;
	}

	@Override
	public String getNamespace() {
		return namespace;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public String getValueString() {
		return (String) value;
	}

	@Override
	public Boolean getValueBoolean() {
		return value instanceof Boolean ? (Boolean)value : Boolean.parseBoolean((String)value);
	}

	@Override
	public List<String> getValueList() {
		throw new UnsupportedOperationException("Not yet implemented.");
	}

	private String[] extractNamespace(String key) {
		return key.contains(":") ? key.split(":") : null;
	}

	@Override
	public String toString() {
		return "CmsConfigOptionImpl{" + "namespace=" + namespace + ", key=" + key + ", value=" + value + '}';
	}
	
	

}
