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

/**
 * Holder of property value.
 * 
 * Instances should be immutable (safe to pass around), but there's
 * a method {@link #isModified()} to tell if there are local changes
 * or if the value is the current one from the repository.
 * 
 * @param <T> The java abstraction of the stored value
 */
public interface SvnPropertyValue<T> {

	/**
	 * @return true if the value has been modified locally
	 */
	public abstract boolean isModified();	
	
	/**
	 * Returns immutable (or cloned) current value.
	 * If the returned value is mutable it can be changed freely
	 * and changes will not affect the instance's value.
	 * 
	 * @return the parsed property value mapped to a Java type
	 */
	public abstract T getValue();

	/** 
	 * Provides the String representation used when persisting as a Subversion property.
	 * @return The persistence string representation.
	 */
	public abstract String getSvnString();
	
	/**
	 * @return true if same value type AND the written value when committed would be equal
	 */
	public abstract boolean equals(Object obj);

}