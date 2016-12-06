/**
 * Copyright (C) 2009-2016 Simonsoft Nordic AB
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
package se.simonsoft.cms.item.uuid;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Ignore;
import org.junit.Test;

public class UuidGeneratorTest {

	final int size = 32 * 32 * 32 * 32;
	// With the current requirements collision risk is relatively high if two instances are created on the same millisecond
	// so this number must be kept sufficiently high for the platforms we build on to safely happen more than 1 ms apart
	final int newInstanceInterval = 1000;

	@Ignore
	@Test
	public void testRequirementsForElementMatchId() {
		UuidGenerator impl = new UuidShortSortable();
		UuidGenerator impl2 = new UuidShortSortable(); // create as soon as possible after first impl
		
		SortedSet<String> all = new TreeSet<String>();
		String prev = "";
		int len = -1;
		for (int i = 0; i < size; i++) {
			String id = impl.getUuid();
			if (i == 0 || i == size / 2 || i + 1 == size) System.out.println(id);
			if (len == -1) len = id.length();
			assertEquals("All ids should be same length, " + len + ", at " + i, 
					len, id.length());
			assertFalse("Not unique at " + i + ": " + id,
					all.contains(id));
			assertTrue("Sequence should be sorted. At " + i + " got " + id + " after " + prev,
					id.compareTo(prev) > 0);
			prev = id;
			all.add(id);
			// next generator
			String id2 = impl2.getUuid();
			assertFalse("Next ID generator should never collide",
					all.contains(id2));
			assertEquals("All ID generators should produce same length",
					len, id2.length());
			all.add(id2);
			// Let's say every now and then someone else generates IDs
			// This is often enough to test the combination of timestamp resolution and the number of random bits.
			// Sequence number is always 000 from this one so it will collide with the first id from generators above.
			// It is unavoidable that this test fails something like every 100 runs, or else it wouldn't be demanding enough.
			if (i % newInstanceInterval == 0) {
				String id3 = new UuidShortSortable().getUuid();
				assertFalse("Other ID generators should never collide. At " + i + " got " + id3,
						all.contains(id3));
				assertEquals("All ID generators should produce same length",
						len, id3.length());
				all.add(id3);
			}
		}
	}
	
	@Test
	public void testInstancesSameMillisecond() {
		SortedSet<String> all = new TreeSet<String>();
		long start = System.currentTimeMillis();
		int tries = 0;
		while (tries++ < 100 && System.currentTimeMillis() <= start + 1) {
			all.add(new UuidShortSortable().getUuid());
		}
		assertEquals("Bad luck? " + (tries - 1 - all.size()) + " collisions during 1-2 ms.", tries - 1, all.size());
	}
	
	@Test
	public void testInstancesSameMillisecondMayCollide() {
		for (int i = 0; i < 10000; i++) {
			try {
				testInstancesSameMillisecond();
			} catch (AssertionError e) {
				return; // pass
			}
		}
		fail("ID generation expected to collide when started at the same millisecond");
	}

}
