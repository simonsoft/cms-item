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
 * Represents the parameters for producing a publication, for use in building output name.
 * 
 * Fields are based on the batch composition ACL code 
 */
public interface PublicationSpec {

	/**
	 * @return file name, mandatory
	 */
	String getName();
	
	/**
	 * @return file name extension for output format, without the dot
	 */
	String getOutputExtension();
	
	/**
	 * @return profile name, null if not profiled
	 */
	String getProfile();
	
	/**
	 * @return the language identifier, null if insignificant in the current batch
	 */
	String getLocale();
	
	/**
	 * @return style name, null if insignificant in the current batch
	 */
	String getStyle();
	
}
