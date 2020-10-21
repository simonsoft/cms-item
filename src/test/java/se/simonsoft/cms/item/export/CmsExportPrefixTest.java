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

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsExportPrefixTest {

	@Test
	public void test() {
		new CmsExportPrefix("cms4");
		new CmsExportPrefix("cms4-suffix");
		new CmsExportPrefix("CMS4"); // Adding support for uppercase.
		new CmsExportPrefix("01234567890123456789012345678901234567890123456789"); // 50 chars
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLongString() {
		new CmsExportPrefix("01234567890123456789012345678901234567890123456789morethan50");
	}

	
	@Test(expected = IllegalArgumentException.class)
	public void testEmpty() {
		new CmsExportPrefix("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCharPercent() {
		new CmsExportPrefix("cms%");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCharUnderscore() {
		// Currently don't allow underscore since it is generally avoided in URLs.
		new CmsExportPrefix("cms_");
	}
}
