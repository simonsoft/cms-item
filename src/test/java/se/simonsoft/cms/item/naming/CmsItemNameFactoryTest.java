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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdArg;
import se.simonsoft.cms.item.info.CmsItemLookup;
import se.simonsoft.cms.item.info.CmsItemNotFoundException;
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

	@Test
	public void testExtensionEmptyGivesSameClearErrorAsMissing() {
		CmsRepository repo = new CmsRepository("http://myhost/svn/repo1");
		CmsItemId folderId = new CmsItemIdArg(repo, new CmsItemPath("/dita/xml/notes"));

		CmsItem folder = mock(CmsItem.class);
		when(folder.getId()).thenReturn(folderId);
		when(folder.isCmsClass("shardparent")).thenReturn(true);
		when(folder.getProperties()).thenReturn(new CmsItemPropertiesMap("cmsconfig:ItemNamePattern", "SEC###"));

		Map<CmsRepository, CmsItemLookup> lookup = new HashMap<>();
		CmsItemNameFactory factory = new CmsItemNameFactory(lookup);

		try {
			factory.getItemPath(folder, "");
			fail("should treat an empty-string extension the same as missing, not fall through to the generic regex error");
		} catch (IllegalArgumentException e) {
			assertEquals("No file extension: folder " + folderId + " does not configure cmsconfig:ItemNameExtension and the caller did not supply a fallback", e.getMessage());
		}
	}

	@Test
	public void testSecondAllocationInSameFolderReusesUncommittedShard() {
		// Two items migrated in the same folder-level transform call, both landing in the same shard-parent
		// folder - TransformServiceXsl commits the whole batch as one patchset at the end, so the shard folder
		// allocated for the first item is not yet known to the real backend when the second item is allocated.
		CmsRepository repo = new CmsRepository("http://myhost/svn/repo1");
		CmsItemPath parentPath = new CmsItemPath("/dita/xml/notes");
		CmsItemId folderId = new CmsItemIdArg(repo, parentPath);

		CmsItem folder = mock(CmsItem.class);
		when(folder.getId()).thenReturn(folderId);
		when(folder.isCmsClass("shardparent")).thenReturn(true);
		when(folder.getProperties()).thenReturn(new CmsItemPropertiesMap("cms:class", "shardparent").and("cmsconfig:ItemNamePattern", "NOTE_00#####"));

		CmsItemPath shardFolderPath = parentPath.append("NOTE_0000000");
		CmsItemId shardFolderId = new CmsItemIdArg(repo, shardFolderPath);

		CmsItemLookup realLookup = mock(CmsItemLookup.class);
		when(realLookup.getItem(folderId)).thenReturn(folder);
		when(realLookup.getImmediateFolders(folderId)).thenReturn(new HashSet<>());
		// Not committed yet - the first allocation below only exists in CmsItemNameFactory's own bookkeeping.
		when(realLookup.getImmediateFiles(shardFolderId)).thenThrow(new CmsItemNotFoundException(shardFolderId));

		Map<CmsRepository, CmsItemLookup> lookupMap = new HashMap<>();
		lookupMap.put(repo, realLookup);
		CmsItemNameFactory factory = new CmsItemNameFactory(lookupMap);

		CmsItemPath first = factory.getItemPath(folder, "dita");
		assertEquals(new CmsItemPath("/dita/xml/notes/NOTE_0000000/NOTE_0000000.dita"), first);

		// Must not throw CmsItemNotFoundException, even though the shard folder from the first allocation has
		// not actually been committed to SVN yet.
		CmsItemPath second = factory.getItemPath(folder, "dita");
		assertEquals(new CmsItemPath("/dita/xml/notes/NOTE_0000000/NOTE_0000001.dita"), second);
	}

	@Test
	public void testMissingShardNotAllocatedByFactoryStillFails() {
		CmsRepository repo = new CmsRepository("http://myhost/svn/repo1");
		CmsItemId folderId = new CmsItemIdArg(repo, new CmsItemPath("/dita/xml/notes"));
		CmsItemId shardId = new CmsItemIdArg(repo, new CmsItemPath("/dita/xml/notes/NOTE_0000000"));
		CmsItem folder = mock(CmsItem.class);
		when(folder.getId()).thenReturn(folderId);
		when(folder.isCmsClass("shardparent")).thenReturn(true);
		when(folder.getProperties()).thenReturn(new CmsItemPropertiesMap("cms:class", "shardparent")
				.and("cmsconfig:ItemNamePattern", "NOTE_00#####"));

		CmsItemLookup lookup = mock(CmsItemLookup.class);
		when(lookup.getItem(folderId)).thenReturn(folder);
		HashSet<CmsItemId> shards = new HashSet<>();
		shards.add(shardId);
		when(lookup.getImmediateFolders(folderId)).thenReturn(shards);
		CmsItemNotFoundException missing = new CmsItemNotFoundException(shardId);
		when(lookup.getImmediateFiles(shardId)).thenThrow(missing);
		CmsItemNameFactory factory = new CmsItemNameFactory(Map.of(repo, lookup));

		try {
			factory.getItemPath(folder, "dita");
			fail("must not hide a missing shard that the factory has not allocated");
		} catch (CmsItemNotFoundException e) {
			assertSame(missing, e);
		}
	}

}
