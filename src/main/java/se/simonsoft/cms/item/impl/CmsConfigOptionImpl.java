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
 */
public class CmsConfigOptionImpl implements CmsConfigOption {

	private String namespace;
	private String key;
	private Object value;

	public CmsConfigOptionImpl(String key, Object value) {
		this(null, key, value);
	}

	public CmsConfigOptionImpl(String namespace, String key, Object value) {
		this.namespace = namespace == null ? extractNamespace(key) : namespace;
		this.key = key;
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

	private String extractNamespace(String key) {
		return key.contains(":") ? key.split(":")[0] : null;
	}

	@Override
	public String toString() {
		return "CmsConfigOptionImpl{" + "namespace=" + namespace + ", key=" + key + ", value=" + value + '}';
	}
	
	

}
