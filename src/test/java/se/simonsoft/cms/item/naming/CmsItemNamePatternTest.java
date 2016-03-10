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
    
    @Test(expected=IllegalArgumentException.class)
    public void testStringLeadingSpace() {

        @SuppressWarnings("unused")
		CmsItemNamePattern name = new CmsItemNamePattern(" A####");
    }

    @Test
    public void testStringIllegalChars() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("thisShould Not be Ok####");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("The name prefix must be alphanumeric and at least one char long", e.getMessage());
        }
    }

    @Test
    public void counterPatternContainsIllegalChar() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00#i####");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Counter must match: ^[#]{2,}$", e.getMessage());
        }
    }

    @Test
    public void counterPatternIsToShort() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00#");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Counter must match: ^[#]{2,}$", e.getMessage());
        }
    }

    @Test
    public void nameDoseNotContainAnyHashes() {

        try {
            CmsItemNamePattern pattern = new CmsItemNamePattern("SEC00");
            assertNull(pattern);
        } catch (IllegalArgumentException e) {
            assertEquals("Name pattern must have at least 2 '#' at the end of the pattern: SEC00", e.getMessage());
        }
    }

    @Test
    public void getCounter() {

        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals(6, name.getCounter().length());
    }

    @Test
    public void getCounterAsZeros() {
        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals("000000", name.getCounterAsZeros());

        CmsItemNamePattern name1 = new CmsItemNamePattern("ok_ok##################");
        assertEquals("000000000000000000", name1.getCounterAsZeros());
    }

    @Test
    public void getFullFolderName() {
        CmsItemNamePattern name = new CmsItemNamePattern("ok_ok######");
        assertEquals("Full folder name is name + counter as zeros", "ok_ok000000", name.getFullNameWithCountZero());
    }

    @Test
    public void indexOfSaneTest() {
        String someHashes = "1234#12";
        assertEquals("Need an working index of to", 4, someHashes.indexOf("#"));

    }

    @Test
    public void indexOfSaneTest2() {
        CmsItemNamePattern name = new CmsItemNamePattern("SEC####");
        assertEquals("SEC", name.getPrefix());
    }

}