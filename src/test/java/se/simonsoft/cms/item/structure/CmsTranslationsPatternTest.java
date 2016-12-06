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
package se.simonsoft.cms.item.structure;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsTranslationsPatternTest {

	@Test
	public void testGraphicsPattern() {
		CmsTranslationsPattern p = new CmsTranslationsPattern("/*/Trans");
		assertEquals(new CmsTranslationsPattern("Trans"), p.getGraphicsPattern());
		try {
			new CmsTranslationsPattern("Trans").getGraphicsPattern();
		} catch (UnsupportedOperationException e) {
			assertEquals("The pattern 'Trans' is already relative and can't be converted to a graphics translation pattern", e.getMessage());
		}
	}

}
