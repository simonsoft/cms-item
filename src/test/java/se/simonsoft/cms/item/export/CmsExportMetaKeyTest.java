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

import org.junit.Test;

import java.util.HashMap;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertEquals;

public class CmsExportMetaKeyTest {


    @Test
    public void testEmptyKeyShouldThrow() {

        try {
            new CmsExportMetaKey("");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: ", e.getMessage());
        }

    }

    @Test
    public void testNullKeyShouldThrow() {

        try {
            new CmsExportMetaKey(null);
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("MetaKey must not be null.", e.getMessage());
        }

    }

    @Test
    public void testNoSpecialCharsExceptDash() {

        try {
            new CmsExportMetaKey("fail*");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail*", e.getMessage());
        }

        try {
            new CmsExportMetaKey("fail&");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail&", e.getMessage());
        }

        try {
            new CmsExportMetaKey("fail%");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid MetaKey: fail%", e.getMessage());
        }

    }

    @Test
    public void testAlphaNumericsWithDashShouldPass() {
        CmsExportMetaKey cmsExportMetaKey = new CmsExportMetaKey("cms-pass");
        assertEquals("cms-pass", cmsExportMetaKey.toString());

    }

    @Test
    public void testGetObjectFromMapWithMetaKey() {

        HashMap<CmsExportMetaKey, String> map = new HashMap<>();
        map.put(new CmsExportMetaKey("abc"), "123");
        map.put(new CmsExportMetaKey("qwe"), "asd");

        assertTrue(map.size() == 2);

        CmsExportMetaKey abc = new CmsExportMetaKey("abc");
        CmsExportMetaKey qwe = new CmsExportMetaKey("qwe");

        assertEquals("123" ,map.get(abc));
        assertEquals("asd" ,map.get(qwe));



    }
}
