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


public class CmsExportTagValueTest {


    @Test
    public void testEmptyValueShouldWork() {
    	CmsExportTagValue cmsExportTagValue = new CmsExportTagValue("");
        assertEquals("", cmsExportTagValue.toString());
    }

    @Test
    public void testNullValueShouldThrow() {

        try {
            new CmsExportTagValue(null);
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("TagValue must not be null.", e.getMessage());
        }

    }

    @Test
    public void testNoSpecialCharsExceptDash() {

        try {
            new CmsExportTagValue("fail*");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagValue: fail*", e.getMessage());
        }

        try {
            new CmsExportTagValue("fail&");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagValue: fail&", e.getMessage());
        }

        try {
            new CmsExportTagValue("fail%");
            fail("Should fail");
        } catch (IllegalArgumentException e) {
            assertEquals("Not a valid TagValue: fail%", e.getMessage());
        }

    }

    @Test
    public void testAlphaNumericsWithDashShouldPass() {
        CmsExportTagValue cmsExportTagValue = new CmsExportTagValue("cms-pass");
        assertEquals("cms-pass", cmsExportTagValue.toString());

    }

    @Test
    public void testPrefixWithColonShouldPass() {
    	CmsExportTagValue cmsExportTagValue = new CmsExportTagValue("prefix:pass");
    	assertEquals("prefix:pass", cmsExportTagValue.toString());
    	
    }

    @Test
    public void testGetObjectFromMapWithTagValue() {

        HashMap<CmsExportTagValue, String> map = new HashMap<>();
        map.put(new CmsExportTagValue("abc"), "123");
        map.put(new CmsExportTagValue("qwe"), "asd");

        assertTrue(map.size() == 2);

        CmsExportTagValue abc = new CmsExportTagValue("abc");
        CmsExportTagValue qwe = new CmsExportTagValue("qwe");

        assertEquals("123" ,map.get(abc));
        assertEquals("asd" ,map.get(qwe));



    }
}
