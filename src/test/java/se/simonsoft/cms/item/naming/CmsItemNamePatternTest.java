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
        CmsItemNamePattern name = new CmsItemNamePattern("name001000");
        assertEquals("name001000", name.getName());
    }

    @Test
    public void testIllegalArgException() {
        try {
            new CmsItemNamePattern("");
        } catch (IllegalArgumentException e) {
            assertEquals("The name pattern can't be null or empty", e.getMessage());
        }
    }

    @Test
    public void testStringAlphanumeric() {
        CmsItemNamePattern name = new CmsItemNamePattern("thisShou1dBeOk");
        assertEquals("thisShou1dBeOk", name.getName());
    }

    @Test
    public void testStringAlphanumericAndUnderScore() {
        CmsItemNamePattern name = new CmsItemNamePattern("thisShouldBe_Ok");
        assertEquals("thisShouldBe_Ok", name.getName());
    }

    @Test
    public void testStringIllegalChars() {
        try {
            new CmsItemNamePattern("thisShould Not be Ok");
        } catch (IllegalArgumentException e) {
            assertEquals("The name must be alphanumeric", e.getMessage());
        }
    }

}