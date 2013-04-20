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
package se.simonsoft.cms.item.list;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import se.simonsoft.cms.item.list.CmsItemListMeta.Key;

public class CmsItemListMetaTest {

	@SuppressWarnings("serial")
	@Test
	public void test() throws MalformedURLException {
		Map<String, Object> data = new HashMap<String, Object>() {{
			put("id", "cms/service/");
			put("extra", 8);
		}};
		CmsItemListMeta meta = new CmsItemListMeta(data);
		meta.put(Key.title, "A Title");
		
		assertEquals("A Title", meta.get("title"));
		assertEquals("A Title", meta.get(Key.title));
		assertTrue(meta.containsKey("title"));
		assertTrue(meta.containsKey(Key.title));
		assertFalse(meta.containsKey("nope"));
		assertFalse(meta.containsKey(Key.resource));
		
		assertEquals(new Integer(8), meta.get("extra"));
		
		assertEquals(null, meta.get(Key.resource));
		meta.put(Key.resource, new URL("http://example.net/resource/url"));
		assertTrue(meta.containsKey(Key.resource));
		assertEquals("http://example.net/resource/url", meta.itemListGetMeta(Key.resource));
		assertEquals(URL.class, meta.remove(Key.resource).getClass());
		assertFalse("Should remove", meta.containsKey(Key.resource));
		
		// fallback is probably a bad idea because it violates principle of least surprise
		//assertEquals("Headline should fall back to title", "A Title", meta.get(Key.headline));
		//assertEquals("Fallback for use of string key too is probably the least surprising", "A Title", meta.get("headline"));
		meta.put("headline", "custom");
		assertEquals("custom", meta.get(Key.headline));
	}

}
