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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmsExportPrefix {

	private final String prefix;
	
	private static final Pattern NICE = Pattern.compile("[a-z0-9-]+");
	
	public CmsExportPrefix(String prefix) {
		Matcher m = NICE.matcher(prefix);
		if (!m.matches()) {
			throw new IllegalArgumentException("Not a valid export prefix: " + prefix);
		}
		
		this.prefix = prefix;
	}
	
	
	@Override
	public boolean equals(Object anObject) {
		return this.prefix.equals(anObject);
	}
	
	@Override
	public int hashCode() {
		return this.prefix.hashCode();
	}
	
	@Override
	public String toString() {
		return this.prefix.toString();
	}

}
