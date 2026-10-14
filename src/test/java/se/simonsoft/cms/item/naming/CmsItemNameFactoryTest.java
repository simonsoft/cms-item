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
package se.simonsoft.cms.item.naming;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdArg;
import se.simonsoft.cms.item.info.CmsItemLookup;
import se.simonsoft.cms.item.properties.CmsItemPropertiesMap;

public class CmsItemNameFactoryTest {

	@Test
	public void testExtensionMissingGivesClearError() {
		CmsRepository repo = new CmsRepository("http://myhost/svn/repo1");
		CmsItemId folderId = new CmsItemIdArg(repo, new CmsItemPath("/dita/xml/notes"));

		CmsItem folder = mock(CmsItem.class);
		when(folder.getId()).thenReturn(folderId);
		when(folder.isCmsClass("shardparent")).thenReturn(true);
		// Pattern configured, extension deliberately not - the scenario a caller hits when the folder has not
		// been fully bootstrapped and did not supply a fallback extension either.
		when(folder.getProperties()).thenReturn(new CmsItemPropertiesMap("cmsconfig:ItemNamePattern", "SEC###"));

		Map<CmsRepository, CmsItemLookup> lookup = new HashMap<>();
		CmsItemNameFactory factory = new CmsItemNameFactory(lookup);

		try {
			factory.getItemPath(folder, null);
			fail("should require an extension, whether configured on the folder or supplied as a fallback");
		} catch (IllegalArgumentException e) {
			assertEquals("No file extension: folder " + folderId + " does not configure cmsconfig:ItemNameExtension and the caller did not supply a fallback", e.getMessage());
		}
	}

}
