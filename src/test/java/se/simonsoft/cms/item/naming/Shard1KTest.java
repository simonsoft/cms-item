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
package se.simonsoft.cms.item.naming;

import org.junit.Before;
import org.junit.Test;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.CmsItemPath;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.impl.CmsItemIdArg;
import se.simonsoft.cms.item.info.CmsItemLookup;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by jonand on 17/02/16.
 */
public class Shard1KTest {

    private CmsRepository repo;
    private CmsItemLookup lookup;

    @Before
    public void setUp() {

        repo = mock(CmsRepository.class);
        lookup = mock(CmsItemLookup.class);
    }


    @Test
    public void parentFolderAndExtensionCantBeNull() {
            CmsItemNaming naming = new Shard1K(repo, lookup);
            try {
                CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
                naming.getItemPath(null, pattern, "tif");
            } catch (IllegalArgumentException e) {
                assertEquals("Folder and extension must not be null", e.getMessage());
            }

            try {
                CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
                naming.getItemPath(new CmsItemPath("/se/simonsoft/cms/item"), pattern, null);
            } catch (IllegalArgumentException e) {
                assertEquals("Folder and extension must not be null", e.getMessage());
            }
    }

    /**
     * There is one folder SEC0001000 and it's not full should return next item path 003.
     */
    @Test
    public void getItemPathShouldReturnNextPathWithNextNumber() throws Exception {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item"));

        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/naming/").getPath())).thenReturn(itemId);

        Set<CmsItemId> folders = new HashSet<CmsItemId>();
        folders.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0001000")));

        when(lookup.getImmediateFolders(itemId)).thenReturn(folders);

        Set<CmsItemId> files = new HashSet<CmsItemId>();
        files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0001000/SEC0001000.tif")));
        files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0001000/SEC0001001.tif")));
        files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0001000/SEC0001002.tif")));

        when(lookup.getImmediateFiles(folders.iterator().next())).thenReturn(files);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/naming/");
        CmsItemNaming naming = new Shard1K(repo, lookup);

        CmsItemPath itemPath = naming.getItemPath(path, pattern, "tif");

        assertNotNull("Should return an item path", itemPath);
        assertEquals("SEC0001003.tif", itemPath.getName());
        assertEquals(itemPath.getExtension(), "tif");
        assertEquals("Same folder but new item",itemPath.getPath(), "/se/simonsoft/cms/item/SEC0001000/SEC0001003.tif");

    }


    /**
     * There is a folder but it's full, should return new empty folder and item 0.
     */
    @Test
    public void folderIsFullGenerateNewOneWithItemZero() {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/"));
        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/").getPath())).thenReturn(itemId);

        Set<CmsItemId> folders = new HashSet<CmsItemId>();
        folders.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC001000")));
        when(lookup.getImmediateFolders(itemId)).thenReturn(folders);

        Set<CmsItemId> files = new HashSet<CmsItemId>();
        for (int i = 0; i <= 999; i++) {
            files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC001000/SEC001000" + i + ".jpeg")));
        }

        when(lookup.getImmediateFiles(folders.iterator().next())).thenReturn(files);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");
        CmsItemNaming naming = new Shard1K(repo, lookup);

        CmsItemPath itemPath = naming.getItemPath(path, pattern, "jpeg");

        assertEquals("Return next folder number and item 0", "/se/simonsoft/cms/item/SEC001001/SEC001001000.jpeg", itemPath.getPath());

    }

    @Test
    public void noPreviousFolderOrItems() {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/"));
        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/").getPath())).thenReturn(itemId);
        when(lookup.getImmediateFolders(itemId)).thenReturn(null);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");
        CmsItemNaming naming = new Shard1K(repo, lookup);

        CmsItemPath itemPath = naming.getItemPath(path, pattern, "jpeg");

        assertEquals("Return folder 0 and item 0", "/se/simonsoft/cms/item/SEC00000/SEC00000000.jpeg", itemPath.getPath());

    }

    @Test
    public void folderExistButIsEmpty() {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/"));
        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/").getPath())).thenReturn(itemId);

        Set<CmsItemId> folders = new HashSet<CmsItemId>();
        folders.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0001000")));
        when(lookup.getImmediateFolders(itemId)).thenReturn(folders);

        Set<CmsItemId> files = new HashSet<CmsItemId>();
        when(lookup.getImmediateFiles(folders.iterator().next())).thenReturn(files);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");
        CmsItemNaming naming = new Shard1K(repo, lookup);

        CmsItemPath itemPath = naming.getItemPath(path, pattern, "jpeg");

        assertEquals("Return item 0 in same folder", "/se/simonsoft/cms/item/SEC0001000/SEC0001000000.jpeg", itemPath.getPath());

    }

    @Test
    public void folderCounterIsAt9999() {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/"));
        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/").getPath())).thenReturn(itemId);

        Set<CmsItemId> folders = new HashSet<CmsItemId>();
        folders.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC0009999")));
        when(lookup.getImmediateFolders(itemId)).thenReturn(folders);

        CmsItemId itemIdWithHighestNumber = getItemIdWithHighestNumber(folders);

        Set<CmsItemId> files = new HashSet<CmsItemId>();
        for (int i = 0; i <= 999; i++) {
            files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC009/SEC009" + i + ".jpeg")));
        }
        when(lookup.getImmediateFiles(itemIdWithHighestNumber)).thenReturn(files);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");
        CmsItemNaming naming = new Shard1K(repo, lookup);

        CmsItemPath itemPath = naming.getItemPath(path, pattern, "jpeg");

        assertEquals("Return folder with number after 9999", "/se/simonsoft/cms/item/SEC0010000", itemPath.getParent().getPath());

    }

    /**
     * Folder counter is the chars between the namepattern and the end index of the name, if the folder name are longer then origin start folder name we will throw error.
     */
    @Test(expected = IllegalStateException.class)
    public void allFoldersAreFull() {

        CmsItemId itemId = new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/"));
        when(repo.getItemId(new CmsItemPath("/se/simonsoft/cms/item/").getPath())).thenReturn(itemId);

        Set<CmsItemId> folders = new HashSet<CmsItemId>();
        for (int i = 0; i <= 10; i++) {
            folders.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC00" + i)));
        }

        Shard1K naming = new Shard1K(repo, lookup);

        when(lookup.getImmediateFolders(itemId)).thenReturn(folders);

        CmsItemId itemIdWithHighestNumber = getItemIdWithHighestNumber(folders);

        Set<CmsItemId> files = new HashSet<CmsItemId>();
        for (int i = 0; i <= 999; i++) {
            files.add(new CmsItemIdArg(repo, new CmsItemPath("/se/simonsoft/cms/item/SEC009/SEC009" + i + ".jpeg")));
        }
        when(lookup.getImmediateFiles(itemIdWithHighestNumber)).thenReturn(files);

        CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");

        naming.getItemPath(path, pattern, "jpeg");
    }

    @Test
    public void testNamePatternWithIllegalChars() {

        CmsItemPath path = new CmsItemPath("/se/simonsoft/cms/item/");

        Shard1K naming = new Shard1K(repo, lookup);

        try {
            naming.getItemPath(path, new CmsItemNamePattern("SEC00!"), "tif");
        } catch (IllegalArgumentException e) {
            assertEquals("The name must be alphanumeric", e.getMessage());
        }
    }

    private CmsItemId getItemIdWithHighestNumber(Set<CmsItemId> immediateFolders) {

        Iterator<CmsItemId> iterator = immediateFolders.iterator();
        ArrayList<CmsItemId> cmsItemIds = new ArrayList<CmsItemId>();
        while (iterator.hasNext()) {
            cmsItemIds.add(iterator.next());
        }

        Collections.sort(cmsItemIds, new CmsItemIdNameComparator());

        return cmsItemIds.get(cmsItemIds.size() - 1);
    }

    private class CmsItemIdNameComparator implements Comparator<CmsItemId> {

        @Override
        public int compare(CmsItemId itemId1, CmsItemId itemId2) {
            return itemId1.getRelPath().getName().compareTo(itemId2.getRelPath().getName());
        }
    }
}