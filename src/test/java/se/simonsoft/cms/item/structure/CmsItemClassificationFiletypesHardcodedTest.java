/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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

import se.simonsoft.cms.item.impl.CmsItemIdArg;

public class CmsItemClassificationFiletypesHardcodedTest {

	@Test
	public void testIsGraphic() {
		CmsItemClassification c = new CmsItemClassificationAdapterFiletypes();
		assertFalse(c.isGraphic(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.xml")));
		assertTrue(c.isGraphic(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.png")));
	}

	@Test
	public void testIsXml() {
		CmsItemClassification c = new CmsItemClassificationAdapterFiletypes();
		assertTrue(c.isXml(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.xml")));
		assertFalse(c.isXml(new CmsItemIdArg("x-svn://y.y/svn/r^/f/file.png")));
	}

	@Test(expected=UnsupportedOperationException.class)
	public void testIsPublishable() {
		new CmsItemClassificationAdapterFiletypes().isPublishable(null);
	}

}
