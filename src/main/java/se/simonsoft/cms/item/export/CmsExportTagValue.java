/**
 * Copyright (C) 2009-2017 Simonsoft Nordic AB
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

/**
 * The value of a single tag.
 *
 */
public class CmsExportTagValue {

	private final String value;
	private static final Pattern NICE = Pattern.compile("[A-Za-z0-9-_: ]{0,256}");
	// Azure Blob is more restricted than S3:
	// - Maximum 256 chars (same)
	// - letters, numbers, space (only A-Za-z on Azure)
	// - Special characters: + - . / : = _

	
	// https://docs.aws.amazon.com/general/latest/gr/aws_tagging.html
	// https://docs.microsoft.com/en-us/rest/api/storageservices/set-blob-tags
	
	public CmsExportTagValue(String value) {

		if (value == null) {
			throw new IllegalArgumentException("TagValue must not be null.");
		}

		Matcher matcher = NICE.matcher(value);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Not a valid TagValue: " + value);
		}

		this.value = value;
	}

	public String getValue() {
		
		return this.value;
	}
	
	@Override
	public boolean equals(Object anObject) {
		return this.value.equals(anObject.toString());
	}
	
	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public String toString() {
		return this.value.toString();
	}
	
}
