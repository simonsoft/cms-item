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

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by jonand on 17/02/16.
 */
public class CmsItemNamePatternTest {

    @Test(expected = IllegalArgumentException.class)
    public void testWithNull() {
        new CmsItemNamePattern(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithEmptyString() {
        new CmsItemNamePattern("");
    }

    @Test
    public void getNameReturnsName() {

        CmsItemNamePattern name = new CmsItemNamePattern("name####");
        assertEquals("name", name.getPrefix());
    }

    @Test
    public void testIllegalArgException() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("The name pattern can't be null or empty", e.getMessage());
        }
    }

    @Test
    public void testStringAlphanumeric() {

        CmsItemNamePattern name = new CmsItemNamePattern("thisShou1dBeOk########");
        assertEquals("thisShou1dBeOk", name.getPrefix());
    }

    @Test
    public void testStringAlphanumericAndUnderScore() {

        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok####");
        assertEquals("ok_ok", name.getPrefix());
    }

    @Test
    public void testStringIllegalChars() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("thisShould Not be Ok####");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("The name must be alphanumeric and at least one char long", e.getMessage());
        }
    }

    @Test
    public void counterPatternContainsIllegalChar() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00#i####");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Folder counter must match: ^[#]{1,}$", e.getMessage());
        }
    }

    @Test
    public void counterPatternIsToShort() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00###");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Folder counter must match: ^[#]{1,}$", e.getMessage());
        }
    }

    @Test
    public void nameDoseNotContainAnyHashes() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Counter pattern must have be at least 4 # at the end of the pattern", e.getMessage());
        }
    }

    @Test
    public void fileCounterContainsIllegalChars() {
        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00##i#");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("File counter must match: ^[#]{3,3}$", e.getMessage());
        }
    }

    @Test
    public void getFolderCounter() {

        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals(3, name.getFolderCounter().length());
    }

    @Test
    public void  getFileCounter() {

        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals(3, name.getFileCounter().length());
    }

    @Test
    public void getFolderCounterAsZeros() {
        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals("000", name.getFolderCounterAsZeros());

        CmsItemNamePattern name1 = new CmsItemNamePattern("ok_ok##################");
        assertEquals("000000000000000", name1.getFolderCounterAsZeros());
    }
    @Test
    public void getFileCounterAsZeros() {
        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals("000", name.getFileCounterAsZeros());

        CmsItemNamePattern name1 = new CmsItemNamePattern("ok_ok##################");
        assertEquals("000", name1.getFileCounterAsZeros());
    }

    @Test
    public void getFullFolderName() {
        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals("Full folder name is name + folderCounterAsZero + FileCounterAsZero", "ok_ok000000", name.getFullFolderName());
    }

}