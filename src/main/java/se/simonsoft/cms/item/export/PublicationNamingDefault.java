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
package se.simonsoft.cms.item.export;

/**
 * Names the output files based on properties of the publication.
 * Based on buildOutputName from adapter's svncompose.acl.
 */
public class PublicationNamingDefault implements PublicationNaming {

	/**
	 * Name component separator.
	 */
	public static final String SEP = "_";	
	
	@Override
	public String getOutputName(PublicationSpec spec) {
		StringBuffer n = new StringBuffer();
		if (spec.getName() == null || spec.getName().length() == 0) {
			error("name is required");
		}
		n.append(spec.getName());
		if (spec.getProfile() != null) {
			n.append(SEP).append(spec.getProfile());
		}
		if (spec.getLocale() != null) {
			n.append(SEP).append(spec.getLocale());
		}
		if (spec.getStyle() != null) {
			n.append(SEP).append(spec.getStyle());
		}
		if (spec.getOutputExtension() != null) {
			n.append(".").append(spec.getOutputExtension());
		}
		
		return n.toString();
	}

	private void error(String message) {
		throw new IllegalArgumentException("publication naming error: " + message);
	}

}
