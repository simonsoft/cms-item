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
package se.simonsoft.cms.item.impl;

import java.util.LinkedList;
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

	/**
	 * @param name complete name, typically a combination of namespace and key
	 * @param value option value
	 */
	public CmsConfigOptionBase(String name, T value) {
		String[] nameSpaceAndKey = extractNamespace(name);
		this.namespace = nameSpaceAndKey.length > 1 ? nameSpaceAndKey[0] : null;
		this.key = nameSpaceAndKey.length > 1 ? nameSpaceAndKey[1] : name;
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
		return ((String) value).trim();
	}

	@Override
	public Boolean getValueBoolean() {
		return value instanceof Boolean ? (Boolean)value : Boolean.parseBoolean((String)value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getValueList() {
		return value instanceof List<?> ? (List<String>)value : getList((String) value);
	}
	
	private static List<String> getList(String multiValue) {
		
		String[] split = multiValue.split("\\|");
		List<String> list = new LinkedList<String>();
		for (String s : split) {
			String t = s.trim();
			if (t.length() == 0) {
				throw new IllegalArgumentException("Got empty value in multi-value configuration option: " + multiValue);
			}
			list.add(t);
		}
		return list;
	}

	private String[] extractNamespace(String key) {
		return key.contains(":") ? key.split(":") : null;
	}

	@Override
	public String toString() {
		return "CmsConfigOptionImpl{" + "namespace=" + namespace + ", key=" + key + ", value=" + value + '}';
	}
	
	

}
