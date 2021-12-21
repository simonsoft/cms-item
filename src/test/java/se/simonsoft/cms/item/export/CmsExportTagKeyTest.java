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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.Test;


public class CmsExportTagKeyTest {


    @Test
    public void testEmptyKeyShouldThrow() {

        try {
            new CmsExportTagKey("");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagKey: ", e.getMessage());
        }

    }

    @Test
    public void testNullKeyShouldThrow() {

        try {
            new CmsExportTagKey(null);
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("TagKey must not be null.", e.getMessage());
        }

    }

    @Test
    public void testNoSpecialCharsExceptDash() {

        try {
            new CmsExportTagKey("fail*");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagKey: fail*", e.getMessage());
        }

        try {
            new CmsExportTagKey("fail&");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagKey: fail&", e.getMessage());
        }

        try {
            new CmsExportTagKey("fail%");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagKey: fail%", e.getMessage());
        }

    }

    @Test
    public void testAlphaNumericsWithDashShouldPass() {
        CmsExportTagKey cmsExportTagKey = new CmsExportTagKey("cms-pass");
        assertEquals("cms-pass", cmsExportTagKey.toString());

    }

    @Test
    public void testPrefixWithColonShouldPass() {
    	CmsExportTagKey cmsExportTagKey = new CmsExportTagKey("prefix:pass");
    	assertEquals("prefix:pass", cmsExportTagKey.toString());
    	
    }

    @Test
    public void testGetObjectFromMapWithTagKey() {

        HashMap<CmsExportTagKey, String> map = new HashMap<>();
        map.put(new CmsExportTagKey("abc"), "123");
        map.put(new CmsExportTagKey("qwe"), "asd");

        assertTrue(map.size() == 2);

        CmsExportTagKey abc = new CmsExportTagKey("abc");
        CmsExportTagKey qwe = new CmsExportTagKey("qwe");

        assertEquals("123" ,map.get(abc));
        assertEquals("asd" ,map.get(qwe));



    }
}
