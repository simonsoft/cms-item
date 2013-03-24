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
package se.simonsoft.cms.item.list;

import static org.junit.Assert.*;

import org.junit.Test;

import se.simonsoft.cms.item.CmsItem;
import se.simonsoft.cms.item.CmsItemId;
import se.simonsoft.cms.item.impl.CmsItemIdArg;

public class IdOnlyItemTest {

	@Test
	public void testGetId() {
		CmsItemId id = new CmsItemIdArg("x-svn://host.name/svn/r^/f");
		CmsItem item = new IdOnlyItem(id);
		assertEquals(id, item.getId());
		try {
			item.getRevisionChanged();
			fail("Not even revision changed is defined by an item id (though there might be peg revision)");
		} catch (UnsupportedOperationException e) {
			assertTrue("got " + e, e.getMessage().contains(id.toString()));
		}
	}

}
