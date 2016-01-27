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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the property list of a version controlled item.
 * Parses String property values to different Java types depending the
 * format rules for Simonsoft CMS.
 * Provides public getters and setters for the available property types.
 * 
 * Implements CmsItemProperties with no need for prior detachment,
 * because properties are profived to {@link #store(String, String)} and then kept in memory,
 * so there are no open connections to the repository.
 * When used as {@link SvnPropertyMap} modification state is also kept in memory.
 */
public class SvnPropertyMap implements CmsItemProperties {

	private static final Logger logger = LoggerFactory.getLogger(SvnPropertyMap.class);
	
	private HashMap<String, SvnPropertyValue<?>> map;
	
	public SvnPropertyMap() {
		this.map = new HashMap<String, SvnPropertyValue<?>>();
	}
	
	/**
	 * Identifies property values for which json list parsing should be attempted.
	 */
	private static final Pattern QUALIFY_LIST = Pattern.compile("\\[.*\\]$");
	
	// This is the ideal gettor for svnAbstraction.
	public SvnPropertyValue<?> getProperty(String key) {
		
		return map.get(key);
	}
	
	/**
	 * @return only the currently modified properties, frozen i.e. disconnected from this instance
	 */
	public CmsItemProperties getModified() {
		
		CmsItemPropertiesMap modified = new CmsItemPropertiesMap();
		for (Map.Entry<String, SvnPropertyValue<?>> entry : this.map.entrySet()) {
			if(entry.getValue() == null) {
				modified.andDelete(entry.getKey());
			} else if (entry.getValue().isModified()) {
				modified.and(entry.getKey(), entry.getValue().getSvnString());
			}
		}
		
		return modified;
		
	}

	/* (non-Javadoc)
	 * @see se.simonsoft.cms.item.properties.CmsItemProperties#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		
		SvnPropertyValue<?> value = getProperty(key);
		if (value == null) {
			return null;
		}
		if (value instanceof SvnPropertyValueString) {
			return ((SvnPropertyValueString) value).getValue();
		}
		if (value instanceof SvnPropertyValueList) {
			return getString((SvnPropertyValueList) value);
		}
		return null;
	}


	private String getString(SvnPropertyValueList value) {
		if (value.isModified() || value.originalValue == null) {
			return value.getSvnString();
		}
		return value.originalValue;
	}


	/* (non-Javadoc)
	 * @see se.simonsoft.cms.item.properties.CmsItemProperties#getList(java.lang.String)
	 */
	@Override
	public List<String> getList(String key) throws ClassCastException {
		
		SvnPropertyValue<?> value = getProperty(key);
		if (value == null) {
			return null;
		}
		if (value instanceof SvnPropertyValueList) {
			return ((SvnPropertyValueList) value).getValue();
		}
		return null;
	}	
	
	
	public void putProperty(String key, String data) {
		
		SvnPropertyValue<String> value;
		SvnPropertyValue<?> oldValue = map.get(key);
		if (oldValue != null) {
			if (oldValue instanceof SvnPropertyValueString) {
				value = ((SvnPropertyValueString) oldValue).setString(data);
			} else {
				value = new SvnPropertyValueString(data);
				logger.debug("Overwriting property {} of type {} with type {}", 
						new Object[] {oldValue.getClass(), value.getClass()});
			}
		} else {
			value = new SvnPropertyValueString(data);
		}
		// Always doing put since it might be a new object.	
		map.put(key, value);
	}
	
	public void putProperty(String key, List<?> data) {
		
		SvnPropertyValue<?> value;
		SvnPropertyValue<?> oldValue = map.get(key);
		if (oldValue != null) {
			if (oldValue instanceof SvnPropertyValueList) {
				value = ((SvnPropertyValueList) oldValue).setList(data);
			} else {
				value = new SvnPropertyValueList(data);
				logger.debug("Overwriting property {} of type {} with type {}", 
						new Object[] {oldValue.getClass(), value.getClass()});
			}
		} else {
			value = new SvnPropertyValueList(data);
		}
		// Always doing put since it might be a new object.	
		map.put(key, value);
		
	}
	
	// #913 TODO: Implement in getModified() and test coverage.
	public void removeProperty(String key) {
		
		SvnPropertyValue<?> oldValue = getProperty(key);
		if (oldValue != null) {
			map.put(key, null);
		} else {
			logger.info("Property value is already null: " + oldValue);
		}
	}
	
	
	/*
	public void putProperty(String key, SvnPropertyValue<?> value) {
		
		SvnPropertyValue<?> oldValue = map.get(key);
		if (oldValue != null) {
			if (!oldValue.getClass().equals(value.getClass())) {
				logger.debug("Overwriting property of type {} with type {}", oldValue.getClass(), value.getClass());
			}
			// TODO: This guy will crash if switching property type...
			if (value instanceof SvnPropertyValueString) {
				((SvnPropertyValueString) oldValue).setValue((SvnPropertyValueString)value);
			} else {
				// TODO Might be handled differently than String because of mutability changes
				// for example do the comparison with current value here instead of there is no setValue
				throw new SvnFatalException("Currently only plaintext properties are supported");
			}
		} else {
			map.put(key, value);
		}
	}
	*/
	
	/* (non-Javadoc)
	 * @see se.simonsoft.cms.item.properties.CmsItemProperties#containsProperty(java.lang.String)
	 */
	@Override
	public boolean containsProperty(String key) {
		
		return map.containsKey(key);
	}
	
	/* (non-Javadoc)
	 * @see se.simonsoft.cms.item.properties.CmsItemProperties#getKeySet()
	 */
	@Override
	public Set<String> getKeySet() {
		
		return map.keySet();
	}
	
	/**
	 * Parse property value and store in memory depending on value type.
	 * Not meant to be used directly for anything but property reading from backend, 
	 * use {@link #putProperty(String, List)} and {@link #putProperty(String, String)} instead.
	 * @param key The property name
	 * @param value The string value from Subversion
	 */
	public void store(String key, String value) {
		if (value == null) {
			throw new IllegalArgumentException("Property value can not be null");
		}
		SvnPropertyValue<?> parsed = null;
		if (QUALIFY_LIST.matcher(value).matches()) {
			try {
				parsed = new SvnPropertyValueList(value);
			} catch (ValueParseException e) {
				logger.warn("Property '{}' value '{}' looks like a list but is not valid JSON", key, value);
			}
		}
		// fall back to String
		if (parsed == null) {
			parsed = new SvnPropertyValueString(value, false);
		}
		map.put(key, parsed);
	}	
	
}

