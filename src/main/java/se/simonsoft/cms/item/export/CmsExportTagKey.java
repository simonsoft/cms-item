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
 * The name of a single metadata item.
 *
 */
public class CmsExportTagKey {

	private final String key;
	private static final Pattern NICE = Pattern.compile("[A-Za-z0-9-_:]{1,100}");
	// Added _ and : compared to MetaKey.
	// AWS S3 and Azure Blob also allows the following that we are restricting:
	// - Maximum 128 chars
	// - Space
	// - Special characters: + . / =
	// - AWS: @

	
	// https://docs.aws.amazon.com/general/latest/gr/aws_tagging.html
	// https://docs.microsoft.com/en-us/rest/api/storageservices/set-blob-tags
	
	public CmsExportTagKey(String key) {

		if (key == null) {
			throw new IllegalArgumentException("TagKey must not be null.");
		}

		Matcher matcher = NICE.matcher(key);
		if (!matcher.matches()) {
			throw new IllegalArgumentException("Not a valid TagKey: "+ key);
		}

		this.key = key;
	}

	public String getKey() {
		
		return this.key;
	}
	
	@Override
	public boolean equals(Object anObject) {
		return this.key.equals(anObject.toString());
	}
	
	@Override
	public int hashCode() {
		return this.key.hashCode();
	}

	@Override
	public String toString() {
		return this.key.toString();
	}
	
}
