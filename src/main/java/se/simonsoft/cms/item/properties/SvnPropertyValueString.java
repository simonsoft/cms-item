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
package se.simonsoft.cms.item.properties;

import java.util.Arrays;
import java.util.List;

/**
 * String value wrapper with support for splitting on newline.
 */
public class SvnPropertyValueString implements SvnPropertyValue<String> {
	
	private String value;
	private boolean modified;
	
	/**
	 * RegEx pattern used to split on newline, Subversion convention.
	 * Note that the value should be trimmed before splitting.
	 */
	public static final String NEWLINE_SPLIT = "\\s*\\n\\s*";
	
	/**
	 * This empty constructor only exist to please kryo de-serialization which needs an no arg constructor
	 * Do not initialize with this constructor
	 */
	@SuppressWarnings("unused")
	private SvnPropertyValueString() {}

	SvnPropertyValueString(String value, boolean modified) {
	
		this.value = value;
		this.modified = modified;
	}
	
	public SvnPropertyValueString(String value) {
		
		this(value, true);
	}

	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getValue()
	 */
	@Override
	public String getValue() {

		return value;
	}

	/**
	 * Splits a value using the Subversion convention with newline separation,
	 * used for example in svn:ignore and svn:externals.
	 * 
	 * @return Trimmed, non-empty lines from the property value,
	 *  null for no such property, empty list for existing property without data,
	 *  trimmed single entry list for no newlines in value,
	 *  immutable.
	 */
	public List<String> getValueNewlineSeparated() {
		String v = getValue();
		if (v == null) {
			return null;
		}
		v = v.trim();
		if (v.length() == 0) {
			return Arrays.asList(new String[] {});
		}
		return Arrays.asList(v.split(NEWLINE_SPLIT));
	}
	

	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#isModified()
	 */
	@Override
	public boolean isModified() {
		return modified;
	}

	/**
	 * Package scoped because the rest of the application should go through {@link SvnPropertyMap}.
	 * @param value the new value
	 */
	SvnPropertyValue<String> setString(String value) {
		
		SvnPropertyValueString result;
		
		if (this.value == null || !this.value.equals(value)) {
			result = new SvnPropertyValueString(value, true);
		} else {
			result = this;
		}
		return result;
	}

	
	/*
	void setValue(SvnPropertyValue<String> b) {
		
		// Currently very simple since we can only store a String.
		setString(b.getValue());
	}
	*/
	
	/* (non-Javadoc)
	 * @see se.simonsoft.adapters.svnadapter.properties.SvnPropertyValue#getSvnString()
	 */
	@Override
	public String getSvnString() {
		
		return this.toString();
	}
	
	public String toString() {
		
		return value;
	}
	
	public boolean equals(Object y) {

		if (y == null) {
			return false;
		}
		return this.toString().equals(y.toString());

	}
	
	public int hashCode() {
		
		return this.toString().hashCode();
	}

	
	
	

}
