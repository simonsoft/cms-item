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
package se.simonsoft.cms.item;

import static org.junit.Assert.*;

import org.junit.Test;

public class CmsItemKindTest {

	@Test
	public void testGetKind() {
		assertEquals(
				"toString should have uppercase first because there's always toLowerCase",
				"File", CmsItemKind.File.toString());
		assertEquals("file", CmsItemKind.File.getKind());
		assertEquals("dir", CmsItemKind.Folder.getKind());
		assertEquals("dir", CmsItemKind.Repository.getKind());
		assertEquals("Folder", CmsItemKind.Folder.toString());
	}

	@Test
	public void testIsFolder() {
		assertTrue(CmsItemKind.File.isFile());
		assertFalse(CmsItemKind.Folder.isFile());
		assertFalse(CmsItemKind.File.isFolder());
		assertTrue(CmsItemKind.Folder.isFolder());
		assertTrue(CmsItemKind.Repository.isFolder());
	}

	@Test
	public void testFromString() {
		assertEquals(CmsItemKind.File, CmsItemKind.fromString("File"));
		assertEquals(CmsItemKind.File, CmsItemKind.fromString("file"));
		assertEquals(CmsItemKind.Folder, CmsItemKind.fromString("folder"));
		assertEquals(CmsItemKind.Folder, CmsItemKind.fromString("dir"));
	}

}
