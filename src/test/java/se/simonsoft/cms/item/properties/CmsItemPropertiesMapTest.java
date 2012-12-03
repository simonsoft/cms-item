/**
 * Copyright (C) 2009-2012 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.properties;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

public class CmsItemPropertiesMapTest {

	@Test
	public void testCreate() {
		CmsItemPropertiesMap p = new CmsItemPropertiesMap("cms:test1", "value1").and("cms:test2", "value2");
		assertEquals(2, p.size());
		Iterator<String> it = p.keySet().iterator();
		assertEquals("Should preserve iteration order", "cms:test1", it.next());
		assertEquals("value1", p.get("cms:test1"));
		assertEquals("Should preserve iteration order", "cms:test2", it.next());
		assertEquals("value2", p.get("cms:test2"));
	}
	
	@Test
	public void testMarkDelete() {
		CmsItemPropertiesMap p = new CmsItemPropertiesMap();
		p.andDelete("prop1");
		assertEquals("should store deletions", 1, p.size());
		assertEquals(null, p.get("prop1"));
		
		try {
			p.andDelete("prop1");
			fail("The and* methods aid property operations and should validate that a property is not deleted twice or added and then deleted");
		} catch (IllegalArgumentException e) {
			assertEquals("Property 'prop1' is already deleted", e.getMessage());
		}
		
		p.put("prop2", null);
		assertEquals("map api should also allow delete", 2, p.size());
		try {
			p.andDelete("prop2");
			fail("The and* methods should not affect existing values, even if set through map API. Use map API again to modify.");
		} catch (IllegalArgumentException e) {
			assertEquals("Property 'prop2' is already deleted", e.getMessage());
		}
		
		p.put("propnew", "value");
		try {
			p.andDelete("propnew");
			fail("The and* methods should not modify existing values, even if set through map API. Use map API again to modify.");
		} catch (IllegalArgumentException e) {
			assertEquals("Property 'propnew' is already set", e.getMessage());
		}
		
		assertEquals("value", p.get("propnew"));
		p.put("propnew", null);
		assertEquals(3, p.size());
		assertEquals(null, p.get("propnew"));
	}

	@Test
	public void testAnd() {
		CmsItemPropertiesMap p = new CmsItemPropertiesMap("prop", "val");
		try {
			p.and("prop", "val2");
			fail("The and* methods should not modify existing values, even if set through map API. Use map API again to modify.");
		} catch (IllegalArgumentException e) {
			assertEquals("Property 'prop' is already set", e.getMessage());
		}
		try {
			p.andDelete("prop2").and("prop2", "val2");
			fail("The and* methods should not modify existing values, even if set through map API. Use map API again to modify.");
		} catch (IllegalArgumentException e) {
			assertEquals("Property 'prop2' is already deleted", e.getMessage());
		}
	}
	
	@Test
	public void testBinaryProps() {
		try {
			new CmsItemPropertiesMap().put("key", new Object());
			fail("Not expecting anything but string to be accepted as property value in this impl, for now");
		} catch (IllegalArgumentException e) {
			assertEquals("Property of type java.lang.Object not supported", e.getMessage());
		}
	}
	
}
